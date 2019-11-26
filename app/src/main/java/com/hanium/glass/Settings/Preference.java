package com.hanium.glass.Settings;

public class Preference {
    public float directionMarkOpacity;
    public boolean isStabilize;
    public boolean isCorrect;
    public Color color;

    private static Preference instance;

    public static Preference getInstance() {
        if(instance == null) {
            instance = new Preference();
        }
        return instance;
    }

    public Preference() {
        directionMarkOpacity = 0.5f;

        isStabilize = false;
        isCorrect = false;

        color = new Color();

        color.R = 0.98f;
        color.G = 0.34f;
        color.B = 0.5f;
    }

    public static class Color {
        public float R, G, B;
    }
}
