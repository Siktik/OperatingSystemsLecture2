package org.example;

public class DataPoint{
    private String thread;
    private long value;
    public DataPoint(String thread, long value){
        this.thread= thread;
        this.value=value;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return
                "(t:'" + thread + '\'' +
                        "v:" + value +
                        ')';
    }
}
