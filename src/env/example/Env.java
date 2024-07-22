package example;

// Environment code for project jasonRestIot

import jason.asSyntax.*;
import jason.environment.*;
import jason.asSyntax.parser.*;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.logging.*;

import static jasonrs.configuration.Constants.HOST;
import static jasonrs.configuration.Constants.SOCKET_SERVER_PORT;

public class Env extends Environment {

    private Logger logger = Logger.getLogger("jasonRestIot."+Env.class.getName());
    Socket socket;
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        super.init(args);
        try {
            addPercept(ASSyntax.parseLiteral("percept("+args[0]+")"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            socket = IO.socket("http://"+HOST+":"+SOCKET_SERVER_PORT);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                public void call(Object... arg0) {
                    addPercept("bob", Literal.parseLiteral("salut"));

                }
            });
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    logger.info("- Connect error -");
                }

            });

            socket.on("bob", new Emitter.Listener() {

                public void call(Object... args) {

                    //String message="calcul CSP";
                    addPercept("alice", Literal.parseLiteral("salut"));
                }
            });
            socket.on("serviceVoirie", new Emitter.Listener() {

                public void call(Object... args) {
                    // On ajoute de perception l'agent voirie

                    addPercept("voirie", Literal.parseLiteral("charge_voirie("+args[0]+")"));
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("executing: "+action+", but not implemented!");
        if (true) { // you may improve this condition
             informAgsEnvironmentChanged();
        }
        return true; // the action was executed with success
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
