package com.lbi.tile.model;

import javax.xml.bind.annotation.XmlAttribute;

public class X_TileFormat {
    int width;
    int height;
    String mime_type;
    String extension;

    @XmlAttribute(name="width")
    public int getWidth() {
        return width;
    }
    @XmlAttribute(name="height")
    public int getHeight() {
        return height;
    }
    @XmlAttribute(name="mime-type")
    public String getMimeType() {
        return mime_type;
    }
    @XmlAttribute(name="extension")
    public String getExtension() {
        return extension;
    }


    public void setWidth(int val) {
        this.width=val;
    }
    public void setHeight(int val) {
        this.height=val;
    }
    public void setMimeType(String val) {
        this.mime_type=val;
    }
    public void setExtension(String val) {
        this.extension=val;
    }

}
