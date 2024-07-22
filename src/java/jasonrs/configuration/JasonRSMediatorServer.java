package jasonrs.configuration;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jasonrs.configuration.Constants.HOST;
import static jasonrs.configuration.Constants.SOCKET_SERVER_PORT;

public class JasonRSMediatorServer {
    private static final Logger logger = LoggerFactory.getLogger(JasonRSMediatorServer.class);
    private static SocketIOServer server;

    public JasonRSMediatorServer() {
        try {
            Configuration conf = new Configuration();
            conf.setHostname(HOST);
            conf.setPort(SOCKET_SERVER_PORT);

            server = new SocketIOServer(conf);

            server.addConnectListener(new ConnectListener() {
                @Override
                public void onConnect(SocketIOClient client) {
                    logger.info("Client {} connected", client.getSessionId());
                }
            });

            server.addDisconnectListener(new DisconnectListener() {
                @Override
                public void onDisconnect(SocketIOClient client) {
                    logger.info("Client {} disconnected", client.getSessionId());
                }
            });

            server.start();
            logger.info("Socket.IO server started on {}:{}", HOST, SOCKET_SERVER_PORT);

        } catch (Exception e) {
            logger.error("Error starting Socket.IO server", e);

        }
    }

    public static SocketIOServer getServer() {
        return server;
    }

    public static void stopServer() {
        if (server != null) {
            server.stop();
            logger.info("Socket.IO server stopped");
        }
    }
}
