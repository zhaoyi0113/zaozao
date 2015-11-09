package com.education.ws;

/**
 * Created by yzzhao on 11/1/15.
 */
public enum ResponseStatus {USER_EXISTED(1), SUCCESS(0), NOT_LOGIN(-10);

    private int status;

    ResponseStatus(int i) {
        status = i;
    }

    public int value(){
        return status;
    }
}
