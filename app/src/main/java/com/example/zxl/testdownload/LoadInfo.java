package com.example.zxl.testdownload;

/**
 * 当前下载信息
 */
public class LoadInfo {
    private int fileSize;//文件长度
    private int completeSize;//已下载的长度
    private String url;//文件路径

    public LoadInfo(int fs, int cSize, String address) {
        fileSize = fs;
        completeSize = cSize;
        url = address;
    }

    /**
     * @return the fileSize
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the completeSize
     */
    public int getCompleteSize() {
        return completeSize;
    }

    /**
     * @param completeSize the completeSize to set
     */
    public void setCompleteSize(int completeSize) {
        this.completeSize = completeSize;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}