package com.jmatsuok.leaguecli.main.command.impl;

import com.jmatsuok.leaguecli.main.ShellContext;
import com.jmatsuok.leaguecli.main.command.Command;
import com.jmatsuok.leaguecli.main.command.CommandOptions;

import java.util.Map;

/**
 * Retrieves and displays detailed information on the specified command,
 * or a list of commands if no command is specified.
 */
public class Help implements Command {

    private int exitCode = 0;
    private ShellContext ctx;
    private Map<String, Command> commandRegistry;

    /**
     * Constructs a new instance
     *
     * @param ctx Context of execution for this command
     * @param commandRegistry Registry of all currently available commands
     */
    public Help(ShellContext ctx, Map<String, Command> commandRegistry) {
        this.ctx = ctx;
        this.commandRegistry = commandRegistry;
    }

    /**
     * Retrieve and display detailed information on the specified command,
     * or a list of commands if no command is specified
     *
     * @param opts The options provided for this command
     */
    public void run(CommandOptions opts) {
        // Help invoked with no arguments, give a list of available commands
        if (opts.getArgs().size() == 0) {
            ctx.getOutput().println("Available Commands: ");
            for (String s : commandRegistry.keySet()) {
                ctx.getOutput().println(s);
            }
            ctx.getOutput().println("exit");
        }
        else {
            for (String s : opts.getArgs()) {
                if (commandRegistry.get(s) != null) {
                    ctx.getOutput().println(commandRegistry.get(s).getManEntry());
                    ctx.getOutput().println();
                }
                else {
                    ctx.getOutput().println("Unrecogized command: " + s);
                    this.exitCode = -1;
                }
            }
        }
    }

    /**
     * Retrieve the name of this command.
     *
     * @return the name of this command.
     */
    public String getName() {
        return "help";
    }

    /**
     * Retrieve the manual entry for this command for help
     *
     * @return The man entry for this command
     */
    public String getManEntry() {
        return "Invoke with no arguments to list all available commands, \n" +
                "Otherwise gives detailed information on a command. \n\n" +
                "Usage: help [COMMAND] \n\n" +
                "COMMAND - Cmmand to retrieve detailed information on";
    }

    /**
     * Retrieve the exit code of this command.
     *
     * @return the exit code of this command.
     */
    public int getExitCode() {
        return exitCode;
    }
}
