package com.lbi.tile.service;

import com.lbi.tile.config.MyProps;
import com.lbi.tile.dao.MetaDao;
import com.lbi.tile.model.TileMap;
import com.lbi.tile.model.TileMapService;
import com.lbi.tile.model.TileSet;
import com.lbi.tile.model.xml.*;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("metaService")
public class MetaService {
    @Resource(name="metaDao")
    private MetaDao metaDao;
    @Resource(name="myProps")
    private MyProps myProps;

    public Root_Services getServices(){
        Root_Services u=new Root_Services();
        List<TileMapService> serviceList=metaDao.getTileMapServiceList();
        List<Node_TileMapService> nServiceList=new ArrayList<>();
        for(TileMapService s:serviceList){
            String href=s.getHref();
            href=href.replace("${mapserver}",myProps.getMapServer());
            Node_TileMapService nService=new Node_TileMapService(s.getTitle(),s.getVersion(),href);
            nServiceList.add(nService);
        }
        u.setTileMapServices(nServiceList);
        return u;
    }

    public Root_TileMapService getTileMapService(long serviceId,String version){
        TileMapService nService=metaDao.getTileMapServiceById(serviceId);
        Root_TileMapService u = new Root_TileMapService();
        u.setVersion(nService.getVersion());
        u.setTitle(nService.getTitle());
        if(isNoneEmpty(nService.getAbstract()))u.setAbstract(nService.getAbstract());
        //parent href
        u.setServices("http://"+myProps.getMapServer());
        //child list
        List<TileMap> tileMapList=metaDao.getTileMapList(serviceId);
        List<Node_TileMap> nTileMapList=new ArrayList<>();
        for(TileMap m:tileMapList){
            String href=m.getHref();
            href=href.replace("${mapserver}",myProps.getMapServer());
            Node_TileMap nTileMap=new Node_TileMap(m.getTitle(),m.getSrs(),m.getProfile(),href);
            nTileMap.setGroup(m.getGroup());
            nTileMapList.add(nTileMap);
        }
        u.setTileMaps(nTileMapList);
        return u;
    }
    public Root_TileMap getTileMap(
            long serviceId,
            String version,
            String layerName,
            String srs,
            String extension){
        TileMap u =metaDao.getTileMapById(serviceId,layerName,srs,extension);
        if(u!=null){
            Root_TileMap item=new Root_TileMap();
            item.setVersion(version);
            //parent href
            TileMapService nService=metaDao.getTileMapServiceById(u.getServiceId());
            String tilemapservice=nService.getHref();
            tilemapservice=tilemapservice.replace("${mapserver}",myProps.getMapServer());
            item.setServices(tilemapservice);
            if(isNoneEmpty(u.getAbstract()))item.setAbstract(u.getAbstract());
            item.setTitle(u.getTitle());
            item.setSRS(u.getSrs());

            Node_BoundingBox boundingBox =new Node_BoundingBox();
            boundingBox.setMinX(u.getMinX());
            boundingBox.setMinY(u.getMinY());
            boundingBox.setMaxX(u.getMaxX());
            boundingBox.setMaxY(u.getMaxY());
            item.setXBoundingBox(boundingBox);

            Node_Origin origin =new Node_Origin();
            origin.setX(u.getOriginX());
            origin.setY(u.getOriginY());
            item.setXOrigin(origin);

            Node_TileFormat tileFormat =new Node_TileFormat();
            tileFormat.setWidth(u.getWidth());
            tileFormat.setHeight(u.getHeight());
            tileFormat.setMimeType(u.getMimeType());
            tileFormat.setExtension(u.getExtension());
            item.setXTileFormat(tileFormat);

            //child list
            Node_TileSets tileSets=new Node_TileSets();
            List<Node_TileSet> nTileSetList=new ArrayList<>();
            List<TileSet> tileSetList=metaDao.getTileSetList(u.getId());
            for(TileSet t:tileSetList){
                String href=t.getHref();
                href=href.replace("${mapserver}",myProps.getMapServer());
                Node_TileSet nTileSet=new Node_TileSet(href,t.getUnitsPerPixel(),t.getSortOrder());
                nTileSetList.add(nTileSet);
            }
            tileSets.setProfile(u.getProfile());
            tileSets.setTileSets(nTileSetList);
            item.setTileSets(tileSets);
            return item;
        }
        return null;
    }

    public List<TileMap> getTileMapList(){
        List<TileMap> mapList=new ArrayList<>();
        List<TileMap> list=null;
        list=metaDao.getTileMapList(1);
        if(list!=null)mapList.addAll(list);
        list=metaDao.getTileMapList(2);
        if(list!=null)mapList.addAll(list);
        for(TileMap m:mapList){
            String href=m.getHref();
            href=href.replace("${mapserver}",myProps.getMapServer());
            m.setHref(href);
        }
        return mapList;
    }

    public TileMap getTileMapById(long mapId){
        TileMap m=metaDao.getTileMapById(mapId);
        String href=m.getHref();
        href=href.replace("${mapserver}",myProps.getMapServer());
        m.setHref(href);
        return m;
    }
    public List<TileSet> getTileSetList(long mapId){
        List<TileSet> mapSetList=metaDao.getTileSetList(mapId);
        for(TileSet t:mapSetList){
            String href=t.getHref();
            href=href.replace("${mapserver}",myProps.getMapServer());
            t.setHref(href);
        }
        return mapSetList;
    }
}
