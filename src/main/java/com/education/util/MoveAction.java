package com.education.util;

/**
 * Created by yzzhao on 1/15/16.
 */
public enum MoveAction {
    UP("UP"), DOWN("DOWN");

    private String action;

    MoveAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
