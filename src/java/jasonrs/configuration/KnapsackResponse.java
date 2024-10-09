package jasonrs.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class KnapsackResponse {
    @JsonProperty("maxValue")
    private int maxValue;

    @JsonProperty("selectedBins")
    private List<Integer> selectedBins;

    public KnapsackResponse(int maxValue, List<Integer> selectedBins) {
        this.maxValue = maxValue;
        this.selectedBins = selectedBins;
    }

    // Getters and Setters
    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public List<Integer> getSelectedBins() {
        return selectedBins;
    }

    public void setSelectedBins(List<Integer> selectedBins) {
        this.selectedBins = selectedBins;
    }
}

