package com.papanews;

public class savePost_Model {
    private String image;

    private String sourceimage;

    private String Views;


    private String video;

    private String LongText;


    private String sourcename;

    private String title, shortd, longd;


    public savePost_Model(String image, String title, String shortd,
                          String longd,String sourceimage,
                          String sourcename, String Views,
                          String LongText, String video) {
        this.image = image;
        this.title = title;
        this.shortd = shortd;
        this.longd = longd;
        this.sourceimage = sourceimage;
        this.sourcename = sourcename;
        this.LongText = LongText;
        this.Views = Views;
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public String gettitle() {
        return title;
    }

    public String getshortd() {
        return shortd;
    }

    public String getlongd() {
        return longd;
    }

    public String getSourceimage() { return sourceimage; }

    public String getSourcename() { return sourcename; }

    public String getViews() { return Views; }

    public String getLongText() { return LongText; }

    public String getVideo() { return video; }
}
