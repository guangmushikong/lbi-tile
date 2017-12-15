package com.lbi.tile.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "TileMapService")
public class TileMapService {
    private String version;
    private String services;
    private String title;
    private String _abstract;
    private List<X_TileMap> tileMapList;

    @XmlAttribute(name="services")
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
    @XmlElementWrapper(name = "TileMaps")
    @XmlElement(name = "TileMap")
    public List<X_TileMap> getTileMaps() {
        return tileMapList;
    }

    public void setTitle(String val) {
        this.title = val;
    }
    public void setAbstract(String val) {
        this._abstract = val;
    }
    public void setVersion(String val) {
        this.version=val;
    }
    public void setServices(String val) {
        this.services=val;
    }
    public void setTileMaps(List<X_TileMap> val) {
        this.tileMapList = val;
    }

}
