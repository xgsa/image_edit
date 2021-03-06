package org.imgedit.application;

import org.apache.log4j.Logger;
import org.imgedit.common.Env;

public class App extends Env {

    private static final Logger LOG = Logger.getLogger(App.class);

    private static final String SPRING_CONFIGURATION_FILE = "META-INF/application.xml";


    private static boolean processCommandLineArguments(String[] args) {
        if (args.length >= 1) {
            // Pass command line option to the "directoryScanner" Spring bean.
            System.getProperties().setProperty("directory_to_scan", args[0]);
            return true;
        } else {
            LOG.error("Error: Please specify the directory with images to scan.");
            return false;
        }
    }

    public static void main(String[] args) {
        configureLogging();
        if (processCommandLineArguments(args)) {
            runSpring(SPRING_CONFIGURATION_FILE);
        }
    }

}
