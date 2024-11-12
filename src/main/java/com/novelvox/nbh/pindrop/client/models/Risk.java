package com.novelvox.nbh.pindrop.client.models;

public enum Risk {
    RED,ORANGE,YELLOW,GREEN;

    public static Risk getRisk(String reasons) {
        Risk found = RED;
        for (Risk w : values())
            if (null!= reasons && reasons.toUpperCase().contains(w.name()))
                found = w;

        return found;
    }
}
