package com.jmatsuok.leaguecli.main.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Wrapper Class for storing command options
 */
public class CommandOptions {

    private final Logger logger = Logger.getLogger(CommandOptions.class.getName());
    private Map<String, String> optionsMap = new HashMap<String, String>();
    private String commandName;
    private List<String> argv;

    /**
     * Construct a new instance
     *
     * @param options List of options provided to this command
     * @param commandName Name of this command
     */
    public CommandOptions(List<String> options, String commandName) {
        this.argv = options;
        parseOptionsFromList(options);
        this.commandName = commandName;
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

    private void parseOptionsFromList(List<String> options) {
        if (!(options.size() == 0)) {
            for (int i = 0; i < options.size(); i = i + 2) {
                if (options.size() <= i + 1) {
                    this.optionsMap.put(options.get(i), "");
                } else if (options.get(i + 1).startsWith("-")) {
                    continue;
                } else {
                    this.optionsMap.put(options.get(i), options.get(i + 1));
                }
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
