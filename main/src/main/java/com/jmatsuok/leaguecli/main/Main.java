package com.jmatsuok.leaguecli.main;

import java.util.logging.Logger;

/**
 * Created by jmatsuok on 15/03/17.
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String CONFIG_PATH = "/home/jmatsuok/IdeaProjects/league-cli/res/league-cli.cfg";
    private static CLIConfig config = new CLIConfig(CONFIG_PATH);

    public static void main(String[] argv) {
        // TODO: Automatic command discovery
        // May need some reflection? Discover all implementers of Command, register them?
        logger.info("Starting Shell");
        ShellContext ctx = new ShellContext(System.in, System.out, System.err);
        Shell shell = new Shell(ctx, config);
        shell.run();
        logger.info("Closing Shell");
    }

}
