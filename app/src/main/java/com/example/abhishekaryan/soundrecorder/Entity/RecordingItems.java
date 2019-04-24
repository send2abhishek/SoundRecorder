package com.example.abhishekaryan.soundrecorder.Entity;

public class RecordingItems {

    private int id;
    private String fileName;
    private String filePath;
    private long    fileLength;
    private long    fileAdded;

    public RecordingItems(int id, String fileName, String filePath, long fileLength, long fileAdded) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileLength = fileLength;
        this.fileAdded = fileAdded;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileLength() {
        return fileLength;
    }

    public long getFileAdded() {
        return fileAdded;
    }
}
