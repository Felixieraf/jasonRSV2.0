package jasonrs.usecases.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import jasonrs.configuration.JasonRSMediatorServer;
import jasonrs.configuration.KnapsackRequest;
import jasonrs.configuration.KnapsackResponse;
import org.glassfish.jersey.server.ManagedAsync;
import org.jdeferred.Deferred;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;


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

    @POST
    @Path("/optimize")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public KnapsackResponse optimize(KnapsackRequest request) {
        int n = request.getWeights().length;
        int[][] dp = new int[n + 1][request.getCapacity() + 1];

        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= request.getCapacity(); w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                } else if (request.getWeights()[i - 1] <= w) {
                    dp[i][w] = Math.max(request.getValues()[i - 1] + dp[i - 1][w - request.getWeights()[i - 1]], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        int maxValue = dp[n][request.getCapacity()];

        // Pour trouver les bacs sélectionnés
        List<Integer> selectedBins = new ArrayList<>();
        int w = request.getCapacity();
        for (int i = n; i > 0 && maxValue > 0; i--) {
            if (maxValue != dp[i - 1][w]) {
                selectedBins.add(i - 1);
                maxValue -= request.getValues()[i - 1];
                w -= request.getWeights()[i - 1];
            }
        }
        System.out.println("Max Value: " + dp[n][request.getCapacity()]);
        System.out.println("Selected Bins: " + selectedBins);
        return new KnapsackResponse(dp[n][request.getCapacity()], selectedBins);
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
