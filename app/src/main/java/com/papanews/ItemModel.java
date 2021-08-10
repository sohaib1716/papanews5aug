package com.papanews;

public class ItemModel {
    private String image;

    private String audioType;

    private String sourceimage;

    private String Views;


    private String video;

    private String LongText;


    private String sourcename;

    private String title;
    private String shortd;
    private String longd;

    private String date;

    private String converted;


    private String category;

    public ItemModel(String title, String image, String shortd,
                     String longd,String sourceimage,
                     String sourcename, String Views,
                     String LongText, String video, String audioType, String converted,String date,String category) {

        this.title = title;
        this.image = image;
        this.shortd = shortd;
        this.longd = longd;
        this.sourceimage = sourceimage;
        this.sourcename = sourcename;
        this.LongText = LongText;
        this.Views = Views;
        this.video = video;
        this.audioType = audioType;
        this.converted = converted;
        this.date = date;
        this.category = category;

    }

    public String getConverted() { return converted; }

    public String getAudioType() { return audioType; }

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

    public String getDate() { return date; }

    public String getCategory() {
        return category;
    }


//    public int getBgColor() { return bgColor; }

}

