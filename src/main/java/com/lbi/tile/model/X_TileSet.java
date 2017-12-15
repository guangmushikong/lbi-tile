package com.lbi.tile.model;

import javax.xml.bind.annotation.XmlAttribute;

public class X_TileSet {
    private String href;
    private String units_per_pixel;
    private String order;


    public X_TileSet(String href, String units_per_pixel, String order){
        this.order=order;
        this.units_per_pixel=units_per_pixel;
        this.href=href;
    }
    @XmlAttribute(name="href")
    public String getHref() {
        return href;
    }
    @XmlAttribute(name="units-per-pixel")
    public String getUnitsPerPixel() {
        return units_per_pixel;
    }
    @XmlAttribute(name="order")
    public String getOrder() {
        return order;
    }


}
