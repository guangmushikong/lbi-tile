package com.lbi.tile.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class X_TileSets {
    private String profile;
    private List<X_TileSet> tileSets;


    @XmlAttribute(name="profile")
    public String getProfile() {
        return profile;
    }
    @XmlElement(name = "TileSet")
    public List<X_TileSet> getTileSets() {
        return tileSets;
    }
    public void setProfile(String val) {
        this.profile = val;
    }
    public void setTileSets(List<X_TileSet> val) {
        this.tileSets=val;
    }
}
