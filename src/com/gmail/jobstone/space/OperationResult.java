package com.gmail.jobstone.space;

public class OperationResult<T> {

    private final boolean success;
    private final String message;
    private final T content;

    public static OperationResult SUCCESS = new OperationResult(true);
    public static OperationResult FAIL = new OperationResult(false);

    public OperationResult(T content) {
        this(content, true);
    }

    public OperationResult(T content, boolean success) {
        this.success = success;
        this.message = null;
        this.content = content;
    }

    public OperationResult(String message) {
        this.success = false;
        this.message = message;
        this.content = null;
    }

    public OperationResult(boolean success) {
        this.success = success;
        this.message = null;
        this.content = null;
    }

    public boolean success() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public T getContent() {
        return this.content;
    }

}
