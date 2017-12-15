package com.lbi.tile.model;

import javax.xml.bind.annotation.XmlAttribute;

public class X_BoundingBox {
    double minx;
    double miny;
    double maxx;
    double maxy;

    @XmlAttribute(name="minx")
    public double getMinX() {
        return minx;
    }
    @XmlAttribute(name="miny")
    public double getMinY() {
        return miny;
    }
    @XmlAttribute(name="maxx")
    public double getMaxX() {
        return maxx;
    }
    @XmlAttribute(name="maxy")
    public double getMaxY() {
        return maxy;
    }

    public void setMinX(double val) {
        this.minx=val;
    }
    public void setMinY(double val) {
        this.miny=val;
    }
    public void setMaxX(double val) {
        this.maxx=val;
    }
    public void setMaxY(double val) {
        this.maxy=val;
    }
}
