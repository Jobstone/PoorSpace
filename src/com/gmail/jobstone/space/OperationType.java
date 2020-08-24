package com.gmail.jobstone.space;

public enum OperationType {

    SET,ADD,REMOVE;

    public String getName() {
        switch (this) {
            case SET:
                return "设置";
            case ADD:
                return "添加";
            case REMOVE:
                return "移除";
        }
        return null;
    }

}
