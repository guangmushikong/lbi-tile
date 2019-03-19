package com.lbi.tile.controller;

import com.lbi.tile.model.ContourPoint;
import com.lbi.model.ResultBody;
import com.lbi.tile.service.DemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dem")
public class DemController {
    @Resource(name="demService")
    private DemService demService;

    @RequestMapping(value="/contour",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultBody contour(
            @RequestParam(value = "layerName",defaultValue = "") String layerName,
            @RequestParam("xys") String xys) {
        try{
            if(StringUtils.isNoneEmpty(xys)){
                String[] arr=xys.split(";");
                List<ContourPoint> ptList=new ArrayList<>();
                for(int i=0;i<arr.length;i++){
                    String tmp=arr[i];
                    String[] pts=tmp.split(",");
                    double x=Double.parseDouble(pts[0]);
                    double y=Double.parseDouble(pts[1]);
                    ContourPoint pt=new ContourPoint();
                    pt.setLongitude(x);
                    pt.setLatitude(y);
                    pt.setKind(1);
                    ptList.add(pt);
                }
                if(ptList.size()>0){
                    List<ContourPoint> list;
                    if(layerName.equalsIgnoreCase("jingzhuang")){
                        list=demService.getHeight_jingzhuang(ptList);
                    }else if(layerName.equalsIgnoreCase("gujiao")){
                        list=demService.getHeight_gujiao(ptList);
                    }else {
                        list=demService.getHeight_gujiao(ptList);
                    }
                    return new ResultBody<>(list);
                }else{
                    return new ResultBody<>(-1,"xys数据不能为空");
                }
            }else {
                return new ResultBody<>(-1,"xys不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResultBody<>(-1,e.getMessage());
        }
    }

    @RequestMapping(value="/contour/jingzhuang")
    List<ContourPoint> getHeight_jingzhuang(@RequestBody List<ContourPoint> list)throws Exception{
        return demService.getHeight_jingzhuang(list);
    }

    @RequestMapping(value="/contour/gujiao")
    List<ContourPoint> getHeight_gujiao(@RequestBody List<ContourPoint> list)throws Exception{
        return demService.getHeight_gujiao(list);
    }
}
