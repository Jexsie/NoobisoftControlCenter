package com.noobisoftcontrolcenter.needfortoken;

import java.util.List;

public class MetadataRequest {
    private String filename;
    private List<Metadata> metadata;

    // Getters and Setters
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }
}
