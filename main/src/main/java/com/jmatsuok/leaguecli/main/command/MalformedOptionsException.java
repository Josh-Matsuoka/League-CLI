package com.jmatsuok.leaguecli.main.command;

/**
 * Indicates that the options provided to a command were invalid.
 */
public class MalformedOptionsException extends IllegalArgumentException {

    private String option;

    /**
     * Construct a new instance with the erroneous option
     *
     * @param option
     */
    public MalformedOptionsException(String option) {
        super("Invalid Command Option: " + option);
        this.option = option;
    }

    /**
     * Retrieve the bad option that caused the exception
     *
     * @return The erroneous option
     */
    public String getOption() {
        return this.option;
    }
}
