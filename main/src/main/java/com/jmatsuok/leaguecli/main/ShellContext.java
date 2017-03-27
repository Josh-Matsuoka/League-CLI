package com.jmatsuok.leaguecli.main;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Tracks the context that the League CLI shell is executing in.
 */
public class ShellContext {

    private InputStream input;
    private PrintStream output;
    private PrintStream err;

    /**
     * Constructs a new instance with the given input, output, and error streams.
     *
     * @param input Input stream the shell is reading from
     * @param output Output stream the shell is writing to
     * @param err Error stream the shell is writing to
     */
    public ShellContext(InputStream input, PrintStream output, PrintStream err) {
        this.input = input;
        this.output = output;
        this.err = err;
    }

    /**
     * Retrieves the input stream that the shell is reading from
     *
     * @return input stream being read from
     */
    public InputStream getInput() {
        return input;
    }

    /**
     * Retrieves the output stream that the shell is writing to
     *
     * @return output stream being written to
     */
    public PrintStream getOutput() {
        return output;
    }

    /**
     * Retrieves the error stream that the shell is writing to
     *
     * @return error stream being written to.
     */
    public PrintStream getErr() {
        return err;
    }
}
