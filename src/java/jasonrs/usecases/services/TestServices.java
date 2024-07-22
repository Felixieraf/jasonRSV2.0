package jasonrs.usecases.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jasonrs.configuration.JasonRSMediatorServer;
import org.jdeferred.Deferred;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;



/**
 * Root resource (exposed at "user" path)
 */
@Path("voirie")
public class TestServices {

    private SocketIOServer server = JasonRSMediatorServer.getServer();
    private Deferred<String, Void, Void> deferred;

    public TestServices() {
        System.out.println("Instantiating  Voirie");
        server.removeAllListeners("bob");

        server.addEventListener("bob", String.class, new DataListener<String>() {


            public void onData(SocketIOClient client, String data, AckRequest ack)
                    throws Exception {
                System.out.println("Data received... : " + data);
                deferred.resolve(data);
            }

        });
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("getIt")
    public String getIt() {

        server.getBroadcastOperations().sendEvent("bob");
        return "Got it!";
    }
/*
    @POST
    @Path("contraint")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getAsync(@Suspended final AsyncResponse asyncResponse, String charge) {
        JSONObject json=new JSONObject(charge);
        System.out.println(json.getDouble("charge"));
        double charge_value= json.getDouble("charge");

        System.out.println(charge_value);
        asyncResponse.setTimeout(5000, TimeUnit.MILLISECONDS);
        asyncResponse.setTimeoutHandler(new TimeoutHandler() {


            public void handleTimeout(AsyncResponse arg0) {
                arg0.resume(Response.status(Response.Status.REQUEST_TIMEOUT)
                        .entity("Operation timed out")
                        .build());
            }
        });

        server.getBroadcastOperations().sendEvent("serviceVoirie", charge_value);

        deferred = new DeferredObject<String, Void, Void>();
        Promise<String, Void, Void> promise = deferred.promise();
        promise.done(new DoneCallback<String>() {
            public void onDone(String result) {
                System.out.println("Sending data");
                asyncResponse.resume(Response.status(Response.Status.OK)
                        .entity(result)
                        .build());
            }
        });
        promise.fail(new FailCallback<Void>() {
            public void onFail(Void rejection) {
                System.out.println("Fail to send data");
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Promise rejected")
                        .build());
            }
        });
    }*/
}
