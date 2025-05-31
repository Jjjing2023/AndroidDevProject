package edu.northeastern.numad25sum_tianjingliu;

public class LinkItem {
    private String name;
    private String url;

    public LinkItem(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
}
