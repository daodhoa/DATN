package com.dataflow;

public class Path {
    private String refId;
    private String startId;
    private String endId;

    public Path() {
    }

    public Path(String refId, String startId, String endId) {
        this.refId = refId;
        this.startId = startId;
        this.endId = endId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getStartId() {
        return startId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    public String getEndId() {
        return endId;
    }

    public void setEndId(String endId) {
        this.endId = endId;
    }
}
