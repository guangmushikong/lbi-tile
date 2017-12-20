package com.lbi.tile.model.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class Xml_Origin {
    @XmlAttribute(name="x")
    double x;
    @XmlAttribute(name="y")
    double y;

    public void setX(double val) {
        this.x=val;
    }
    public void setY(double val) {
        this.y=val;
    }

}
