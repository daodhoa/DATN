package com.dataflow;

public class Executables {
    private Pineline pineline;

    public Executables() {
        this.pineline = null;
    }

    public Executables(Pineline pineline) {
        this.pineline = pineline;
    }

    public Pineline getPineline() {
        return pineline;
    }

    public void setPineline(Pineline pineline) {
        this.pineline = pineline;
    }
}
