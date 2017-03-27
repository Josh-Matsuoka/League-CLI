package com.jmatsuok.leaguecli.main.command.impl;

import com.jmatsuok.leaguecli.main.CLIConfig;
import com.jmatsuok.leaguecli.main.ShellContext;
import com.jmatsuok.leaguecli.main.command.Command;
import com.jmatsuok.leaguecli.main.client.CommandServer;
import com.jmatsuok.leaguecli.main.client.ServerResponse;
import com.jmatsuok.leaguecli.main.command.CommandOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Retrieves and displays data on the current match for the given player.
 */
public class CurrentMatch implements Command {

    private String REQUEST_URL;
    private String REQ_URL;
    private static final String NAME_PATTERN_STRING = "^[0-9\\p{L} _\\.]+$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_PATTERN_STRING);


    private int exitCode;
    private CLIConfig config;
    private CommandServer server;
    private ShellContext ctx;
    private Logger logger = Logger.getLogger(SummonerInfo.class.getName());

    /**
     * Constructs a new instance
     *
     * @param config Configuration options for the CLI.
     * @param server Server to execute requests on.
     * @param ctx Context of execution for the command.
     */
    public CurrentMatch(CLIConfig config, CommandServer server, ShellContext ctx) {
        this.config = config;
        this.server = server;
        this.ctx = ctx;
    }

    /**
     * Retrieve and display detailed data on the current match for the given player.
     *
     * @param options The options provided for this command
     */
    public void run(CommandOptions options) {
        try {
            logger.fine("Running SummonerInfo");
            // Validate the name input, ensure it is a valid summoner name
            Matcher m = NAME_PATTERN.matcher(options.getOptionValue("-n"));
            if (!m.matches()) {
                logger.warning("Name provided was invalid.");
                ctx.getOutput().println("Must provide a valid summoner name!");
                this.exitCode = -2;
                return;
            }
            // Map summoner name to summoner id via the summoner-info query
            REQUEST_URL = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" +
                    options.getOptionValue("-n") + "?api_key=" + config.getConfigOption("api-key");
            ServerResponse id_response = server.getRequest(REQUEST_URL);
            JSONArray array = new JSONArray("[" + id_response + "]");
            JSONObject object = array.getJSONObject(0);
            logger.fine("Raw Object: " + object.toString());
            JSONObject obj = object.getJSONObject(options.getOptionValue("-n").toLowerCase());
            int summoner_id = obj.getInt("id");
            // Use this summoner id to query the current match data
            REQ_URL = "https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/"+
                    summoner_id + "?api_key=" + config.getConfigOption("api-key");
            ServerResponse match_response = server.getRequest(REQ_URL);
            ctx.getOutput().println(match_response.getJSONData());
            JSONArray matchData = new JSONArray("[" + match_response.getJSONData() + "]");
            JSONObject matchobj1 = matchData.getJSONObject(0);
            ctx.getOutput().println(matchobj1.getString("gameMode"));
            ctx.getOutput().println(matchobj1.getString("gameType"));
            JSONObject matchobj2 = matchobj1.getJSONObject("participants");
            ctx.getOutput().println(matchobj2.toString());
        } catch (Exception e) {
            logger.warning(e.toString());
            e.printStackTrace();
            this.exitCode = -1;
        }
    }

    /**
     * Retrieve the name of this command.
     *
     * @return the name of this command.
     */
    public String getName() {
        return "current-match";
    }

    /**
     * Retrieve the exit code of this command.
     *
     * @return the exit code of this command.
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Retrieve the manual entry for this command for help
     *
     * @return The man entry for this command
     */
    public String getManEntry() {
        return "Retrieves data on the specified player's current match \n\n" +
                "Usage: current-match (-n NAME) \n\n" +
                "-n NAME - Name of the player to retrieve current match data on";
    }
}
