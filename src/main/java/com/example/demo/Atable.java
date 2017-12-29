package com.example.demo;

/**
 * @author ludonglue
 * @date 2017/12/27
 */
public class Atable {
    private String name;
    private Double weight;

    public Atable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Atable{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
