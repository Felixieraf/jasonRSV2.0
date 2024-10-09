package jasonrs.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KnapsackRequest {
    @JsonProperty("weights")
    private int[] weights;

    @JsonProperty("values")
    private int[] values;

    @JsonProperty("capacity")
    private int capacity;

    // Getters and Setters
    public int[] getWeights() {
        return weights;
    }

    public void setWeights(int[] weights) {
        this.weights = weights;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
