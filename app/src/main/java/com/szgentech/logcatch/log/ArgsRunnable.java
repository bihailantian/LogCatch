package com.szgentech.logcatch.log;

/**
 * Created by xubin on 16-9-14.
 */
public abstract class ArgsRunnable<T> implements Runnable {

    T args;

    public void setArgs(T args) {
        this.args = args;
    }

    public T getArgs() {
        return args;
    }
}
