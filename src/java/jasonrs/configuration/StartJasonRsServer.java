package jasonrs.configuration;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import static jasonrs.configuration.Constants.BASE_URI;
import static jasonrs.configuration.Constants.USECASES_SERVICES;

/** Main class.**/
public class StartJasonRsServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartJasonRsServer.class);

    /***
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages(USECASES_SERVICES);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        Runtime.getRuntime().addShutdownHook(new Thread(httpServer::shutdownNow));
        return httpServer;
    }

    /*** Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        try {
            new JasonRSMediatorServer(); // Start your Socket.IO server
            final HttpServer server = startServer();

            LOGGER.info("Jersey app started at {}", BASE_URI);
            System.out.println(String.format("Jersey app started at %s\nHit enter to stop it...", BASE_URI));
            System.in.read();
        } catch (IOException e) {
            LOGGER.error("Error starting application", e);
        }
    }
}
