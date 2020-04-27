package com.uniza.mr.model;

/**
 * Definition of file used as attachment to business entity.
 */
public class EntityFileData {
    /* Attachment index*/
    private int index;
    /* File name */
    private String name;
    /* File size in bytes */
    private long size;
    /* flag denotes if action was successful */
    private boolean uploaded;

    public EntityFileData() {
    }

    public EntityFileData(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}
