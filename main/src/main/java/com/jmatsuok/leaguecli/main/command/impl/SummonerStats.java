package com.jmatsuok.leaguecli.main.command.impl;

import com.jmatsuok.leaguecli.main.CLIConfig;
import com.jmatsuok.leaguecli.main.client.CommandServer;
import com.jmatsuok.leaguecli.main.ShellContext;
import com.jmatsuok.leaguecli.main.client.ServerResponse;
import com.jmatsuok.leaguecli.main.command.Command;
import com.jmatsuok.leaguecli.main.command.CommandOptions;
import com.jmatsuok.leaguecli.main.command.model.ChampionStatistics;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;

/**
 * Retrieve detailed statistics for a given player
 */
public class SummonerStats implements Command {

    private static Map<String, Map<String, ChampionStatistics>> statisticsRegistry = new HashMap<String, Map<String, ChampionStatistics>>();
    private static final String QUERY_URL = "https://na.api.pvp.net/api/lol/NA/v1.3/stats/by-summoner/%d/ranked?season=SEASON2017&api_key=%s";
    private static final String SUMMONER_ID_URL = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s";
    private static final Logger logger = Logger.getLogger(SummonerStats.class.getName());
    private static final String NAME_PATTERN_STRING = "^[0-9\\p{L} _\\.]+$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_PATTERN_STRING);
    private static final String CHAMPION_URL = "https://na.api.pvp.net/api/lol/static-data/NA/v1.2/champion/%s?api_key=%s";
    private int exitCode;
    private CLIConfig cfg;
    private CommandServer server;
    private ShellContext ctx;

    /**
     * Construct a new instance of this command.
     *
     * @param config Configuration options for the CLI.
     * @param server Server to execute requests on.
     * @param ctx Context of execution for the command.
     */
    public SummonerStats(CLIConfig config, CommandServer server, ShellContext ctx) {
        this.cfg = config;
        this.server = server;
        this.ctx = ctx;
    }

    /**
     * Retrieve and display detailed statistics for the specified player
     *
     * @param opts The options provided for this command.
     */
    public void run(CommandOptions opts) {
        if (isValidInput(opts)) {
            if (!statisticsRegistry.containsKey(opts.getOptionValue("-n"))) {
                try {
                    logger.finest("Running SummonerStats");

                    ServerResponse id_response = server.getRequest(String.format(
                            SUMMONER_ID_URL, opts.getOptionValue("-n"), cfg.getConfigOption("api-key")));
                    if (id_response.getStatusLine().getStatusCode() != 200) {
                        logger.warning("Server returned status code: " + id_response.getStatusLine().getStatusCode());
                        ctx.getOutput().println("Summoner ID not found for " + opts.getOptionValue("-n"));
                    }
                    JSONArray array = new JSONArray("[" + id_response.getJSONData() + "]");
                    JSONObject obj = array.getJSONObject(0);
                    int summoner_id = obj.getJSONObject(opts.getOptionValue("-n").toLowerCase()).getInt("id");

                    ServerResponse stats_response = server.getRequest(String.format(QUERY_URL,
                            summoner_id, cfg.getConfigOption("api-key")));
                    if (stats_response.getStatusLine().getStatusCode() != 200) {
                        logger.warning("Server returned status code: " + stats_response.getStatusLine().getStatusCode());
                        ctx.getOutput().println("Statistics not found for " + opts.getOptionValue("-n"));
                    }
                    JSONArray stats_array = new JSONArray("[" + stats_response.getJSONData() + "]");
                    JSONObject obj1 = stats_array.getJSONObject(0);
                    JSONArray champion_data = obj1.getJSONArray("champions");

                    if (!statisticsRegistry.containsKey(opts.getOptionValue("-n"))) {
                        statisticsRegistry.put(opts.getOptionValue("-n"), new HashMap<String, ChampionStatistics>());
                    }
                    populateChampionData(champion_data, opts);
                    displayData(opts);
                } catch (Exception e) {
                    logger.warning(e.toString());
                    e.printStackTrace();
                    this.exitCode = -1;
                }
            } else {
                displayData(opts);
            }
        } else {
            logger.warning("Invalid arguments given");
            this.exitCode = -2;
        }
    }

    public boolean isValidInput(CommandOptions opts) {
        // Name option must be present
        if (opts.getOptionValue("-n") == null) {
            return false;
        } else {
            Matcher m = NAME_PATTERN.matcher(opts.getOptionValue("-n"));
            // Name option must provide a valid name
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieve the name of this command.
     *
     * @return the name of this command.
     */
    public String getName() {
        return "summoner-stats";
    }

    /**
     * Retrieve the exit code of this command.
     *
     * @return the exit code of this command.
     */
    public int getExitCode() {
        return exitCode;
    }

    private void displayData(CommandOptions opts) {
        String summoner_name = opts.getOptionValue("-n");
        if (opts.getOptionValue("-c") != null) {
            String champion_name = opts.getOptionValue("-c");
            ChampionStatistics statistics = statisticsRegistry.get(summoner_name).get(champion_name);
            displayChampionData(champion_name, statistics, opts);
        } else {
            // If no champion was specified, display all.
            for (String s : statisticsRegistry.get(summoner_name).keySet()) {
                ChampionStatistics statistics = statisticsRegistry.get(summoner_name).get(s);
                displayChampionData(s, statistics, opts);
            }
        }
    }

    private void displayChampionData(String champion_name, ChampionStatistics statistics, CommandOptions opts) {
        ctx.getOutput().println(champion_name);
        ctx.getOutput().println("-----------------");
        for (String s : statistics.getGameStats().keySet()) {
            ctx.getOutput().println(s + " : " + statistics.getGameStats().get(s));
        }
        ctx.getOutput().println("-----------------");
        for (String s : statistics.getKDAStats().keySet()) {
            ctx.getOutput().println(s + " : " + statistics.getKDAStats().get(s));
        }
        ctx.getOutput().println("-----------------");
        for (String s : statistics.getMultikillStats().keySet()) {
            ctx.getOutput().println(s + " : " + statistics.getMultikillStats().get(s));
        }
        ctx.getOutput().println("-----------------");
        if (opts.getArgs().contains("-v") || opts.getArgs().contains("-vv")) {
            for (String s : statistics.getDamageStats().keySet()) {
                ctx.getOutput().println(s + " : " + statistics.getDamageStats().get(s));
            }
            ctx.getOutput().println("-----------------");
        }
        if (opts.getArgs().contains("-vv")) {
            for (String s : statistics.getMiscStats().keySet()) {
                ctx.getOutput().println(s + " : " + statistics.getMiscStats().get(s));
            }
            ctx.getOutput().println("-----------------");
        }
    }

    private void populateChampionData(JSONArray champion_data, CommandOptions opts) {
        String summoner_name = opts.getOptionValue("-n");
        String champion_name;
        for (int i = 0; i < champion_data.length(); i++) {
            JSONObject object = champion_data.getJSONObject(i);
            JSONObject c = object.getJSONObject("stats");
            champion_name = getChampionName(object.getInt("id"));
            ChampionStatisticsBuilder statisticsBuilder = new ChampionStatisticsBuilder();
            statisticsBuilder.game_stats(c.getInt("totalSessionsPlayed"), c.getInt("totalSessionsWon"),
                    c.getInt("totalSessionsLost"));
            statisticsBuilder.kda_stats(c.getInt("totalChampionKills"), c.getInt("totalDeathsPerSession"),
                    c.getInt("totalAssists"));
            statisticsBuilder.multikill_stats(c.getInt("totalDoubleKills"), c.getInt("totalTripleKills"),
                    c.getInt("totalQuadraKills"), c.getInt("totalPentaKills"));
            statisticsBuilder.damage_stats(c.getInt("totalDamageDealt"), c.getInt("totalPhysicalDamageDealt"),
                    c.getInt("totalMagicDamageDealt"), c.getInt("totalDamageTaken"));
            statisticsBuilder.misc_stats(c.getInt("totalTurretsTaken"), c.getInt("totalMinionKills"),
                    c.getInt("totalGoldEarned"), c.getInt("totalFirstBlood"));
            ChampionStatistics statistics = statisticsBuilder.build();
            if (!statisticsRegistry.get(summoner_name).containsKey(champion_name)) {
                statisticsRegistry.get(summoner_name).put(champion_name.split(",")[0], statistics);
            }
        }
    }

    private String getChampionName(int id) {
        ServerResponse response = server.getRequest(String.format(CHAMPION_URL, id, cfg.getConfigOption("api-key")));
        if (response.getStatusLine().getStatusCode() != 200) {
            logger.warning("Server returned status code: " + response.getStatusLine().getStatusCode());
            return null;
        }
        JSONArray array = new JSONArray("[" + response.getJSONData() + "]");
        JSONObject object = array.getJSONObject(0);
        logger.fine(object.toString());
        return object.getString("name") + ", " + object.getString("title");
    }

    /**
     * Retrieves the manual entry for this command for help
     *
     * @return The man entry for this command
     */
    public String getManEntry() {
        return  "Retrieves detailed champion statistics for the specified player." +
                "Usage: summoner-stats (-n NAME) [-c CHAMPION] [-v[v]]. \n\n" +
                "-n NAME - Name of the summoner to retrieve stats for. \n" +
                "-c CHAMPION - Champion to retrieve stats for. \n" +
                "-v Verbose - Display more details. \n" +
                "-vv Very Verbose - Display all available details.";
    }

}
