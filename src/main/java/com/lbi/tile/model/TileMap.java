package com.lbi.tile.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TileMap")
public class TileMap {
    private String version;
    private String services;
    private String title;
    private String _abstract;
    private String srs;
    private X_BoundingBox XBoundingBox;
    private X_Origin XOrigin;
    private X_TileFormat XTileFormat;
    private X_TileSets tileSets;

    @XmlAttribute(name="tilemapservice")
    public String getServices() {
        return services;
    }
    @XmlAttribute(name="version")
    public String getVersion() {
        return version;
    }

    @XmlElement(name = "Title")
    public String getTitle() {
        return title;
    }
    @XmlElement(name = "Abstract")
    public String getAbstract() {
        return _abstract;
    }
    @XmlElement(name = "SRS")
    public String getSRS() {
        return srs;
    }
    @XmlElement(name = "X_BoundingBox")
    public X_BoundingBox getXBoundingBox() {
        return XBoundingBox;
    }
    @XmlElement(name = "X_Origin")
    public X_Origin getXOrigin() {
        return XOrigin;
    }
    @XmlElement(name = "X_TileFormat")
    public X_TileFormat getXTileFormat() {
        return XTileFormat;
    }
    @XmlElement(name = "TileSets")
    public X_TileSets getTileSets() {
        return tileSets;
    }
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

    public void setXBoundingBox(X_BoundingBox val) {
        this.XBoundingBox =val;
    }
    public void setXOrigin(X_Origin val) {
        this.XOrigin =val;
    }
    public void setXTileFormat(X_TileFormat val) {
        this.XTileFormat =val;
    }
    public void setTileSets(X_TileSets val) {
        this.tileSets=val;
    }
}
