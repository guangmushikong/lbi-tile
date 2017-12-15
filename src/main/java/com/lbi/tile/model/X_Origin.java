package com.lbi.tile.model;

import javax.xml.bind.annotation.XmlAttribute;

public class X_Origin {
    double x;
    double y;

    @XmlAttribute(name="x")
    public double getX() {
        return x;
    }
    @XmlAttribute(name="y")
    public double getY() {
        return y;
    }


    public void setX(double val) {
        this.x=val;
    }
    public void setY(double val) {
        this.y=val;
    }

}
