package com.lbi.tile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class T_TileMap {
    int id;
    String layerName;
    String title;
    String srs;
    String profile;
    String href;
    String url;
    int sType;
    double minX;
    double minY;
    double maxX;
    double maxY;
    double originX;
    double originY;
    int tileWidth;
    int tileHeight;
    String mimeType;
    String extension;
    String fileExtension;
}
