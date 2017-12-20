package com.lbi.tile.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TileMap")
public class XmlRoot_TileMap {
    @XmlAttribute(name="version")
    String version;
    @XmlAttribute(name="tilemapservice")
    String services;

    @XmlElement(name = "Title")
    String title;
    @XmlElement(name = "Abstract")
    String _abstract;
    @XmlElement(name = "SRS")
    String srs;

    @XmlElement(name = "BoundingBox")
    Xml_BoundingBox boundingBox;
    @XmlElement(name = "Origin")
    Xml_Origin origin;
    @XmlElement(name = "TileFormat")
    Xml_TileFormat tileFormat;
    @XmlElement(name = "TileSets")
    Xml_TileSets tileSets;


    /*@XmlElementWrapper(name = "TileSets")
    @XmlElement(name = "TileSet")
    public List<T_TileSet> getTileSets() {
        return tileSets;
    }*/

    public void setVersion(String val) {
        this.version=val;
    }
    public void setServices(String val) {
        this.services=val;
    }

    public void setTitle(String val) {
        this.title = val;
    }
    public void setAbstract(String val) {
        this._abstract = val;
    }

    public void setSRS(String val) {
        this.srs = val;
    }

    public void setXBoundingBox(Xml_BoundingBox val) {
        this.boundingBox =val;
    }
    public void setXOrigin(Xml_Origin val) {
        this.origin =val;
    }
    public void setXTileFormat(Xml_TileFormat val) {
        this.tileFormat =val;
    }
    public void setTileSets(Xml_TileSets val) {
        this.tileSets=val;
    }
}
