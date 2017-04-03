package com.jmatsuok.leaguecli.main.command.impl;

import com.jmatsuok.leaguecli.main.CLIConfig;
import com.jmatsuok.leaguecli.main.ShellContext;
import com.jmatsuok.leaguecli.main.client.CommandServer;
import com.jmatsuok.leaguecli.main.client.ServerResponse;
import com.jmatsuok.leaguecli.main.command.Command;
import com.jmatsuok.leaguecli.main.command.CommandOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jmatsuok on 30/03/17.
 */
public class RankedInfo implements Command {

    private static final String QUERY_URL = "https://na.api.pvp.net/api/lol/NA1/v2.5/league/by-summoner/%d?api-key=%s";
    private static final String SUMMONER_ID_URL = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s";
    private static final String NAME_PATTERN_STRING = "^[0-9\\p{L} _\\.]+$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_PATTERN_STRING);
    private static final Logger logger = Logger.getLogger(RankedInfo.class.getName());
    private int exitCode;
    private CLIConfig cfg;
    private CommandServer server;
    private ShellContext ctx;

    public RankedInfo(CLIConfig cfg, CommandServer server, ShellContext ctx) {
        this.cfg = cfg;
        this.server = server;
        this.ctx = ctx;
    }

    public String getName() {
        return "ranked-info";
    }

    public int getExitCode() {
        return exitCode;
    }

    public void run(CommandOptions opts) {
        if (opts.getOptionValue("-n") == null || !isValidInput(opts.getOptionValue("-n"))) {
            this.exitCode = -2;
            ctx.getOutput().println("Summoner Name not provided or was invalid name.");
            return;
        }
        try {
            // Retrieve the summonerID of the given summoner
            ServerResponse idResponse = server.getRequest(String.format(SUMMONER_ID_URL,
                    opts.getOptionValue("-n"), cfg.getConfigOption("api-key")));
            if (idResponse.getStatusLine().getStatusCode() != 200) {
                logger.warning("Server returned status code: " + idResponse.getStatusLine().getStatusCode());
                this.exitCode = -1;
            }
            logger.fine("Server Returned: " + idResponse.getJSONData());
            JSONArray array = new JSONArray("[" + idResponse.getJSONData() + "]");
            JSONObject obj = array.getJSONObject(0);
            int summoner_id = obj.getJSONObject(opts.getOptionValue("-n").toLowerCase()).getInt("id");

            // Query the ranked statistics using the retrieved summoner ID
            ServerResponse rankedResponse = server.getRequest(String.format(QUERY_URL,
                    summoner_id, cfg.getConfigOption("api-key")));
            logger.info(rankedResponse.toString());

            this.exitCode = 0;
        } catch (Exception e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            this.exitCode = -1;
        }
    }

    private boolean isValidInput(String name) {
        Matcher m = NAME_PATTERN.matcher(name);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    public String getManEntry() {
        return "Usage: ranked-info -n <Summoner Name> \n\n" +
                "Retrieves ranked information on the given summoner";
    }
}
