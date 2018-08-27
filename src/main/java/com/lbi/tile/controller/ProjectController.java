
package com.lbi.tile.controller;

import com.lbi.model.ResultBody;
import com.lbi.tile.model.DataSetDO;
import com.lbi.tile.model.ProjectDO;
import com.lbi.tile.model.TileMap;
import com.lbi.tile.model.TileSet;
import com.lbi.tile.service.MetaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/*************************************
 * Class Name: ProjectController
 * Description:〈Project〉
 * @create 2018/8/27
 * @since 1.0.0
 ************************************/
@CrossOrigin
@RestController
public class ProjectController {
    @Resource(name="metaService")
    MetaService metaService;

    @RequestMapping(value="/projects", method = RequestMethod.GET)
    public ResultBody getProjectList() {
        List<ProjectDO> list=metaService.getProjectList();
        return new ResultBody(list);
    }

    @RequestMapping(value="/projects/{projectId}", method = RequestMethod.GET)
    public ResultBody getProject(@PathVariable("projectId") long projectId) {
        ProjectDO result=metaService.getProjectById(projectId);
        return new ResultBody(result);
    }

    @RequestMapping(value="/projects/{projectId}/datasets", method = RequestMethod.GET)
    public ResultBody getDatasetList(@PathVariable("projectId") long projectId) {
        List<DataSetDO> list=metaService.getDataSetList(projectId);
        return new ResultBody(list);
    }

    @RequestMapping(value="/projects/{projectId}/datasets/{datasetId}", method = RequestMethod.GET)
    public ResultBody getDataset(
            @PathVariable("projectId") long projectId,
            @PathVariable("datasetId") long datasetId) {
        DataSetDO result=metaService.getDataSetById(datasetId);
        return new ResultBody(result);
    }

    @RequestMapping(value="/meta/maps", method = RequestMethod.GET)
    public ResultBody getMapList() {
        List<TileMap> list=metaService.getTileMapList();
        return new ResultBody(list);
    }
    @RequestMapping(value="/meta/maps/{mapid}", method = RequestMethod.GET)
    public ResultBody getMapById(@PathVariable("mapid") Long mapid) {
        TileMap map=metaService.getTileMapById(mapid);
        return new ResultBody(map);
    }
    @RequestMapping(value="/meta/maps/{mapid}/mapsets", method = RequestMethod.GET)
    public ResultBody getMapSetList(@PathVariable("mapid") Long mapid) {
        List<TileSet> list=metaService.getTileSetList(mapid);
        return new ResultBody(list);
    }
}
