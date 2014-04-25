package org.imgedit.webservice;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service("cli")
public class CliHandler {

    private static final Logger LOG = Logger.getLogger(WebServerHandler.class);

    private final static String BASE_DIRECTORY = "base-directory";
    private final static String PORT = "port";

    @Value("${default.directory}")
    private String BASE_DIRECTORY_DEFAULT = "./";

    @Value("${default.port}")
    private int PORT_DEFAULT = 8080;

    @Autowired
    private ArgumentsStorage argumentsStorage;

    private final Options options;
    private final CommandLineParser parser;
    private CommandLine line;
    private boolean parsedSuccessfully;


    @SuppressWarnings("AccessStaticViaInstance")
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

    @PostConstruct
    public void run() {
        try {
            line = parser.parse(options, argumentsStorage.getAll());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
        if (line == null || line.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("web server", "The following options are supported", options, "Enjoy!");
        } else {
            parsedSuccessfully = true;
        }
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

    public boolean isParsedSuccessfully() {
        return parsedSuccessfully;
    }
}
