package com.noobisoftcontrolcenter.needfortoken.model;

import java.util.List;

public class MetadataRequest {
    private String filename;
    private List<com.noobisoftcontrolcenter.needfortoken.model.Metadata> metadata;

    // Getters and Setters
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<com.noobisoftcontrolcenter.needfortoken.model.Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }
}
