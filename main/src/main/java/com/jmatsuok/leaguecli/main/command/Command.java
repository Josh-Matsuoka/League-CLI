package com.jmatsuok.leaguecli.main.command;

/**
 * Abstract interface for Commands
 */
public interface Command {

    /**
     * Runs the command
     *
     * @param options The options provided for this command
     */
    public void run(CommandOptions options);

    /**
     * Retrieves the exit code of this command
     *
     * @return exit code
     */
    public int getExitCode();

    /**
     * Retrieves the name of this command
     *
     * @return the name of this command
     */
    public String getName();

    /**
     * Retrieve the manual entry for this command for help
     *
     * @return The man entry for this command
     */
    public String getManEntry();

}
