package org.imgedit.webservice;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;


public class CliHandler {

    private static final Logger LOG = Logger.getLogger(WebServerHandler.class.getName());

    public final static String BASE_DIRECTORY = "base-directory";
    public final static String PORT = "port";

    public final static String BASE_DIRECTORY_DEFAULT = "./";
    public final static int PORT_DEFAULT = 8080;

    private Options options;
    private CommandLine line;
    private CommandLineParser parser;


    public CliHandler() {
        options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help")
                .withDescription("Print help and exit")
                .create("h"));
        options.addOption(OptionBuilder.withLongOpt(BASE_DIRECTORY)
                .withDescription("Files storage/upload directory (current directory by default)")
                .hasArg()
                .withArgName("DIRECTORY")
                .create("d"));
        options.addOption(OptionBuilder.withLongOpt(PORT)
                .withDescription(String.format("Port to start server on (%s by default)", PORT_DEFAULT))
                .hasArg()
                .withArgName("NUMBER")
                .create("p"));
        parser = new PosixParser();
    }

    public boolean run(String[] args) {
        line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        if (line == null || line.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("web server", "The following options are supported", options, "Enjoy!");
            return false;
        }
        return true;
    }

    public String getBaseDirectory() {
        return getStringOption(BASE_DIRECTORY, BASE_DIRECTORY_DEFAULT);
    }

    public int getPort() {
        return getIntOption(PORT, PORT_DEFAULT);
    }

    private Integer getIntOption(String optionName, int defaultValue) {
        int result = defaultValue;
        if (line.hasOption(optionName)) {
            String portStr = line.getOptionValue(optionName);
            try {
                result = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                LOG.error(String.format("The '%s' value is not a valid number. " +
                        "The default one (%s) is used.", portStr, defaultValue));
            }
        }
        return result;
    }

    private String getStringOption(String optionName, String defaultValue) {
        return line.hasOption(optionName) ? line.getOptionValue(optionName) : defaultValue;
    }
}
