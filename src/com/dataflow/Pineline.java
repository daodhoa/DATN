package com.dataflow;

import java.util.List;

public class Pineline {
    private Components components;
    private List<Path> paths;
    public Pineline(){
        this.components = null;
        this.paths = null;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }
}
