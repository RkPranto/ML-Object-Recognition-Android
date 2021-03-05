package com.rezwan_cs.ml_project;

import java.io.Serializable;

public class ImageModel implements Serializable {
    String id, name, imageLink, detectionText;

    public ImageModel(){}

    public ImageModel(String id, String name, String detectionText, String imageLink) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.detectionText = detectionText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetectionText() {
        return detectionText;
    }

    public void setDetectionText(String detectionText) {
        this.detectionText = detectionText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", detectionText='" + detectionText + '\'' +
                '}';
    }
}
