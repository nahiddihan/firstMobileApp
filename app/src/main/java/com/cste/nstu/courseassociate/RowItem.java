package com.cste.nstu.courseassociate;





public class RowItem {
    // private int imageId;
    private String date;
    private String time;
    private String postInfo;
    private String postDesc;

    public RowItem(String date, String time, String postInfo,String postDesc) {
        // this.imageId = imageId;
        this.date = date;
        this.time = time;
        this.postInfo = postInfo;
        this.postDesc = postDesc;
    }
    //  public int getImageId() {
    //     return imageId;
    //}
    // public void setImageId(int imageId) {
    //     this.imageId = imageId;
    //  }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getPostInfo() {
        return postInfo ;
    }
    public void setPostInfo(String postInfo) {
        this.postInfo = postInfo;
    }
    public String getPostdesc() {
        return postDesc ;
    }
    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    @Override
    public String toString() {
        return postInfo +":"+ "\n" + postDesc;
    }
}