package com.jmatsuok.leaguecli.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by jmatsuok on 17/03/17.
 */
public class CLIConfig {

    private Map<String, String> config = new HashMap<String, String>();
    private static final Logger logger = Logger.getLogger(CLIConfig.class.getName());

    public CLIConfig(String path) {
        parseConfig(path);
    }

    public String getConfigOption(String opt) {
        return config.get(opt);
    }

    private void parseConfig(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            try {
                String line = br.readLine();
                while (line != null) {
                    String[] split = line.split("=");
                    config.put(split[0], split[1]);
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
        } catch (IOException ioe) {
            logger.warning(ioe.toString());
        }
    }
}
