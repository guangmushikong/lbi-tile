package com.lbi.tile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TileMap {
    long id;
    long serviceId;
    String title;
    String _abstract;
    String srs;
    String profile;
    String href;

    double minX;
    double minY;
    double maxX;
    double maxY;
    double originX;
    double originY;
    int minZoom;
    int maxZoom;

    int width;
    int height;
    String mimeType;
    String extension;

    int kind;
    String source;
    String fileExtension;

    public void setAbstract(String val){
        this._abstract=val;
    }
    public String getAbstract(){
        return this._abstract;
    }
}
