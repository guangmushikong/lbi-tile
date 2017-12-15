package com.lbi.tile.model;

import javax.xml.bind.annotation.XmlAttribute;

public class X_TileMap {
    private String title;
    private String srs;
    private String profile;
    private String href;

    public X_TileMap(String title, String srs, String profile, String href){
        this.title=title;
        this.srs=srs;
        this.profile=profile;
        this.href=href;
    }
    @XmlAttribute(name="href")
    public String getHref() {
        return href;
    }
    @XmlAttribute(name="profile")
    public String getProfile() {
        return profile;
    }
    @XmlAttribute(name="srs")
    public String getSrs() {
        return srs;
    }
    @XmlAttribute(name="title")
    public String getTitle() {
        return title;
    }



}
