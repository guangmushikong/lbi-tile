package com.lbi.tile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TileSet {
    long id;
    long mapId;
    String href;
    String unitsPerPixel;
    String sortOrder;
}
