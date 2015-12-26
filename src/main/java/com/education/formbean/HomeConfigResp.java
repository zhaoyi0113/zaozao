package com.education.formbean;

/**
 * Created by yzzhao on 12/25/15.
 */
public class HomeConfigResp {
    private int id;
    private String image;
    private String fileName;

    public HomeConfigResp() {
    }

    public HomeConfigResp(int id, String image, String fileName) {
        this.id = id;
        this.image = image;
        this.fileName= fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
