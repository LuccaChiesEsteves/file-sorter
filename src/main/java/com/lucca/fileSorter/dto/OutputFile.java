package com.lucca.fileSorter.dto;

import com.lucca.fileSorter.model.Sale;
import com.lucca.fileSorter.model.Salesman;

import java.util.List;

public class OutputFile {

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public OutputFile(String name, String content) {
        this.name = name;
        this.content = content;
    }

    private String name;
    private String content;

}
