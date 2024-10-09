package jasonrs.usecases.services;

import com.corundumstudio.socketio.SocketIOServer;
import jasonrs.configuration.JasonRSMediatorServer;
import jasonrs.configuration.KnapsackRequest;
import jasonrs.configuration.KnapsackResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/knapsack")
public class KnapsackController {
    private SocketIOServer server = JasonRSMediatorServer.getServer();
    @PostMapping("/optimize")
    public KnapsackResponse optimize(@RequestBody KnapsackRequest request) {
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
        System.out.println(selectedBins);
        return new KnapsackResponse(dp[n][request.getCapacity()], selectedBins);
    }
}
