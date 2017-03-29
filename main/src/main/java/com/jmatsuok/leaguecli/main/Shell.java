package com.jmatsuok.leaguecli.main;

import com.jmatsuok.leaguecli.main.client.CommandServer;
import com.jmatsuok.leaguecli.main.command.Command;
import com.jmatsuok.leaguecli.main.command.CommandOptions;
import com.jmatsuok.leaguecli.main.command.MalformedOptionsException;
import com.jmatsuok.leaguecli.main.command.impl.CurrentMatch;
import com.jmatsuok.leaguecli.main.command.impl.Help;
import com.jmatsuok.leaguecli.main.command.impl.SummonerInfo;
import com.jmatsuok.leaguecli.main.command.impl.SummonerStats;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Shell for executing commands
 */
public class Shell {

    private Scanner sc;
    private ShellContext ctx;
    private CommandServer server;
    private Map<String, Command> commandRegistry = new HashMap<String, Command>();
    private CLIConfig config;
    private final Logger logger = Logger.getLogger(Shell.class.getName());

    /**
     * Constructs a new instance
     *
     * @param ctx context of execution for this shell
     * @param config configuration options for the League CLI
     */
    public Shell(ShellContext ctx, CLIConfig config) {
        this.sc = new Scanner(ctx.getInput());
        this.ctx = ctx;
        this.server = new CommandServer();
        this.config = config;
        // TODO: Implement automatic command discovery and registration
        commandRegistry.put("current-match", new CurrentMatch(config, server, ctx));
        commandRegistry.put("help", new Help(ctx, commandRegistry));
        commandRegistry.put("summoner-info", new SummonerInfo(config, server, ctx));
        commandRegistry.put("summoner-stats", new SummonerStats(config, server, ctx));
    }

    /**
     * Begins running the shell's main loop
     */
    public void run() {
        String nextLine = "";
        while (!nextLine.equals("exit")) {
            ctx.getOutput().print("> ");
            nextLine = sc.nextLine();
            parseAndRun(nextLine);
        }
        server.tearDown();
    }

    private void parseAndRun(String input) {
        CommandOptions opts = parseArgs(input);
        if (opts == null) {
            logger.warning("Unable to parse, or no arguments to parse.");
            return;
        } else if (opts.getCommandName().equals("exit")) {
            return;
        }
        Command c = commandRegistry.get(opts.getCommandName());
        if (c == null) {
            logger.warning("Unrecognized command: " + opts.getCommandName());
        } else {
            try {
                c.run(opts);
                if (c.getExitCode() < 0) {
                    ctx.getOutput().println("Command Failed with exit code: " + c.getExitCode());
                }
            } catch (Exception e) {
                logger.warning("Command Failed with exit code: " + c.getExitCode());
            }
        }
    }

    //TODO: Handle multiple whitespaces
    private CommandOptions parseArgs(String input) {
        // Command calls are a whitespace delimited list of tokens
        List<String> tokens = Arrays.asList(input.split(" "));
        if (tokens.size() == 0) {
            return new CommandOptions(new ArrayList<String>(), "");
        } else {
            return new CommandOptions(tokens.subList(1, tokens.size()), tokens.get(0));
        }
    }

    /**
     * Registers a command with the shell
     *
     * @param c Command to register
     */
    public void registerCommand(Command c) {
        this.commandRegistry.put(c.getName(), c);
    }
}
