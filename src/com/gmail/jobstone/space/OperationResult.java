package com.gmail.jobstone.space;

import java.util.ArrayList;
import java.util.List;

public class OperationResult {

    private final boolean success;
    private final String result;
    private final List<String> changedSpaces;

    public OperationResult(List<String> changedSpaces) {
        this.success = true;
        this.result = "";
        this.changedSpaces = changedSpaces;
    }

    public OperationResult(String result) {
        this.success = false;
        this.result = result;
        this.changedSpaces = new ArrayList<>();
    }

    public boolean success() {
        return this.success;
    }

    public String getResult() {
        return this.result;
    }

    public List<String> getChangedSpaces() {
        return this.changedSpaces;
    }

}
