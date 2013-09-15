package qa.qcri.nadeef.web;

import qa.qcri.nadeef.core.datamodel.NadeefConfiguration;
import qa.qcri.nadeef.core.util.sql.DBConnectionFactory;
import qa.qcri.nadeef.tools.Tracer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

/**
 * Bootstrap class which initialize the Database and Configuration.
 */
public final class Bootstrap {
    /**
     * Default configuration file.
     */
    private static String fileName = "nadeef.conf";

    /**
     * Shutdown hook.
     */
    public static void shutdown() {

    }

    /**
     * Initialize Dashboard.
     */
    public static void start() {
        start(fileName);
    }

    public static void start(String fileName) {
        try {
            NadeefConfiguration.initialize(new FileReader(fileName));
            // set the logging directory
            Path outputPath = NadeefConfiguration.getOutputPath();
            Tracer.setLoggingDir(outputPath.toString());
            Tracer.setLoggingPrefix("dashboard");
            Tracer tracer = Tracer.getTracer(Bootstrap.class);
            tracer.verbose("Tracer initialized at " + outputPath.toString());
            DBConnectionFactory.initializeNadeefConnectionPool();

            DBInstaller.install();
        } catch (FileNotFoundException ex) {
            System.err.println("Nadeef Configuration cannot be found.");
            ex.printStackTrace();
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("NADEEF initialization failed, please check the log for detail.");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}