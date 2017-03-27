package com.jmatsuok.leaguecli.main.command.impl;

import com.jmatsuok.leaguecli.main.CLIConfig;
import com.jmatsuok.leaguecli.main.client.CommandServer;
import com.jmatsuok.leaguecli.main.ShellContext;
import com.jmatsuok.leaguecli.main.client.ServerResponse;
import com.jmatsuok.leaguecli.main.command.Command;
import com.jmatsuok.leaguecli.main.command.CommandOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;

/**
 * Command for retrieving basic summoner info for a given player.
 */
public class SummonerInfo implements Command {

    //TODO: Implement a help entry for this command

    private String REQUEST_URL;

    private int exitCode = 1;
    private CLIConfig config;
    private CommandServer server;
    private ShellContext ctx;
    private Logger logger = Logger.getLogger(SummonerInfo.class.getName());

    /**
     * Constructs a new instance of this command.
     *
     * @param config Configuration options for the CLI.
     * @param server Server to execute requests on.
     * @param ctx Context of execution for the command.
     */
    public SummonerInfo(CLIConfig config, CommandServer server, ShellContext ctx) {
        this.config = config;
        this.server = server;
        this.ctx = ctx;
    }

    /**
     * Retrieve and display the basic Summoner Information for the specified player.
     *
     * @param options The options provided for this command
     */
    public void run(CommandOptions options) {
        try {
            // TODO: Error checking/handling on status code
            logger.fine("Running SummonerInfo");
            REQUEST_URL = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" +
                    options.getOptionValue("-n") + "?api_key=" + config.getConfigOption("api-key");
            ServerResponse response = server.getRequest(REQUEST_URL);
            JSONArray array = new JSONArray("[" + response.getJSONData() +"]");
            JSONObject object = array.getJSONObject(0);
            JSONObject obj = object.getJSONObject(options.getOptionValue("-n").toLowerCase());
            logger.fine(obj.toString());
            ctx.getOutput().println("Summoner Name:" + obj.getString("name"));
            ctx.getOutput().println("Summoner ID:" + obj.getInt("id"));
            ctx.getOutput().println("Summoner Level: " + obj.getInt("summonerLevel"));
            exitCode = 0;
        } catch (Exception e) {
            logger.warning(e.toString());
            exitCode = -1;
        }
    }

    /**
     * Retrieve the name of this command
     *
     * @return the name of this command
     */
    public String getName() {
        return "summoner-info";
    }

    /**
     * Retrieve the exit code for this command
     *
     * @return -1 if the command fails
     * @return 0 if the command succeeds
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
        return  "Retrieves very basic information on the specified player (summoner ID, name) \n\n" +
                "Usage: summoner-info (-n NAME) \n\n" +
                "-n NAME - Name of the summoner to retrieve basic data on";
    }
}
