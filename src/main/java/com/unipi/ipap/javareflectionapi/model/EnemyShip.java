package com.unipi.ipap.javareflectionapi.model;

public class EnemyShip {

    private String name;
    private Double speed;


    public EnemyShip(String name, Double speed) {
        this.name = name;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "EnemyShip{" +
                "name='" + name + '\'' +
                ", speed=" + speed +
                '}';
    }
}
