package com.lbi.tile.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Xml_TileSets {
    @XmlAttribute(name="profile")
    String profile;
    @XmlElement(name = "TileSet")
    List<Xml_TileSet> tileSets;

    public void setProfile(String val) {
        this.profile = val;
    }
    public void setTileSets(List<Xml_TileSet> val) {
        this.tileSets=val;
    }
}
