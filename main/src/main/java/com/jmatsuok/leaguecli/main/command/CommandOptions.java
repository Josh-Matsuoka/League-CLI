package com.jmatsuok.leaguecli.main.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wrapper Class for storing command options
 */
public class CommandOptions {

    private final Logger logger = Logger.getLogger(CommandOptions.class.getName());
    private Map<String, String> optionsMap = new HashMap<String, String>();
    private String commandName;
    private List<String> argv = new ArrayList<String>();
    private static final String ARG_STRING = "(\\s+(\\-\\w+)\\s+(\\w+\\s?\\w+)?)";
    private static final String CMD_STRING = "([\\w\\-]+)" + ARG_STRING + "*";
    private static final Pattern ARG_PATTERN = Pattern.compile(ARG_STRING);
    private static final Pattern CMD_PATTERN = Pattern.compile(CMD_STRING);

    /**
     * Construct a new instance
     *
     * @param rawInput The raw input to this command.
     */
    public CommandOptions(String rawInput) {
        parseOptionsFromList(rawInput);
    }

    /**
     * Retrieve the value of the provided option
     *
     * @param option Option to get the value of.
     * @return the value of the requested option if it exists
     * @return null if the option does not exist.
     */
    public String getOptionValue(String option) {
        return optionsMap.get(option);
    }

    /**
     * Add the given option and value to the ones currently tracked.
     *
     * @param option The option associated with the value
     * @param value The value of this option
     */
    public void addOption(String option, String value) {
        this.optionsMap.put(option, value);
    }

    private void parseOptionsFromList(String input) {
        Matcher m = CMD_PATTERN.matcher(input);
        Matcher m2 = ARG_PATTERN.matcher(input);
        logger.info("Input: " + input);
        if (m.matches()) {
            logger.info("Command Pattern matched");
            this.commandName = m.group(1);
            this.argv.add(m.group(1));
        }
        while (m2.find()) {
            logger.info("Arg pattern matched");
            if (m2.groupCount() > 2) {
                logger.info("Adding arg: " + m2.group(2) + " -> " + m2.group(3));
                this.addOption(m2.group(2), m2.group(3));
                this.argv.add(m2.group(2));
                this.argv.add(m2.group(3));
            } else {
                logger.info("Adding arg: " + m2.group(2));
                this.addOption(m2.group(2), "");
                this.argv.add(m2.group(2));
            }
        }
        logger.finest(this.optionsMap.toString());
    }

    /**
     * Retrieve the command these options were provided for.
     *
     * @return the name of the command the options were for.
     */
    public String getCommandName() {
        return commandName;
    }

    public List<String> getArgs() {
        return argv;
    }
}
