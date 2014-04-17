package org.imgedit.application;

import org.apache.log4j.Logger;
import org.imgedit.common.Env;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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

    private static void run() {
        AbstractApplicationContext ac = new FileSystemXmlApplicationContext(SPRING_CONFIGURATION_FILE);
        // As documentation recommends, finalize Spring IoC container gracefully
        ac.registerShutdownHook();
    }

    public static void main(String[] args) {
        configureLogging();
        if (processCommandLineArguments(args)) {
            run();
        }
    }

}
