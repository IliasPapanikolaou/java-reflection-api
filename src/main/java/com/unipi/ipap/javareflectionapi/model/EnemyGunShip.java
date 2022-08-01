package com.unipi.ipap.javareflectionapi.model;

public class EnemyGunShip extends EnemyShip {

    private final String idCode = "100";

    private String getPrivate() {
        return "How did you get this?";
    }

    private String getOtherPrivate(int number, String word) {
        return "How did you get here " + number + " " + word + "?";
    }

    public EnemyGunShip(String name, Double speed) {
        super(name, speed);
        System.out.println("Created gunship with name: " + name + " and speed: " + speed);
    }
}
