package com.lbi.tile.model.xml;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "TileMapService")
public class XmlRoot_TileMapService {
    @XmlAttribute(name="version")
    String version;
    @XmlAttribute(name="services")
    String services;
    @XmlElement(name = "Title")
    String Title;
    @XmlElement(name = "Abstract")
    String Abstract;
    @XmlElementWrapper(name = "TileMaps")
    @XmlElement(name = "TileMap")
    List<Xml_TileMap> tileMapList;

    public void setTitle(String val) {
        this.Title = val;
    }
    public void setAbstract(String val) {
        this.Abstract = val;
    }
    public void setVersion(String val) {
        this.version=val;
    }
    public void setServices(String val) {
        this.services=val;
    }
    public void setTileMaps(List<Xml_TileMap> val) {
        this.tileMapList = val;
    }

}
