package com.example.zxl.testdownload;

/**
 * 存储下载信息Bean
 */
public class DownloadInfo {
    private int threadId;//线程ID
    private int startPos;//下载起始位置
    private int endPos;//下载结束位置
    private int completeSize;//下载完成量
    private String url;//资源URL

    public DownloadInfo(int tId, int sp, int ep, int cSize, String address) {
        threadId = tId;
        startPos = sp;
        endPos = ep;
        completeSize = cSize;
        url = address;
    }

    /**
     * @return the threadId
     */
    public int getThreadId() {
        return threadId;
    }

    /**
     * @param threadId the threadId to set
     */
    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    /**
     * @return the startPos
     */
    public int getStartPos() {
        return startPos;
    }

    /**
     * @param startPos the startPos to set
     */
    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    /**
     * @return the endPos
     */
    public int getEndPos() {
        return endPos;
    }

    /**
     * @param endPos the endPos to set
     */
    public void setEndPos(int endPos) {
        this.endPos = endPos;
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

    @Override
    public String toString() {
        return "threadId:" + threadId + ",startPos:" + startPos + ",endPos：" + endPos + ",completeSize:" + completeSize + ",url:" + url;
    }
}