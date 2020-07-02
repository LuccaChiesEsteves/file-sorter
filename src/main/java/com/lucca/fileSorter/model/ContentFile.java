package com.lucca.fileSorter.model;

public class ContentFile {

    private String name;
    private StringBuilder content;
    private String fullPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringBuilder getContent() {

        return content;
    }

    public void setContent(StringBuilder content) {
        this.content = content;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
