package com.sevenmap.spinel.utils;

import java.awt.FileDialog;

public class FileChooser implements Runnable {
    private Thread fileChooserThread;

    private String targetDir;
    private String filePattern;
    private String filename;
    private boolean isClosed = false;

    public FileChooser() {
        fileChooserThread = new Thread(this, this.getClass().getSimpleName());
        fileChooserThread.start();
    }

    public void run() {
        FileDialog fd = new java.awt.FileDialog((java.awt.Frame) null);
        if (filePattern != null)
            fd.setFile(filePattern);
        if (targetDir != null)
            fd.setDirectory(targetDir);
        fd.setVisible(true);
        filename = fd.getFile();
        isClosed = true;
        fileChooserThread.interrupt();
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    public String getFilename() {
        return filename;
    }

    /**
     * Retrieve the FileChooser's state.
     * 
     * @return true if the file chooser has finished its job
     */
    public boolean isDone() {
        return isClosed;
    }

    public Thread getThread() {
        return fileChooserThread;
    }
}