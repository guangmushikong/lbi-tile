## 1、数据门户
[数据门户](http://cmap.oss-cn-beijing.aliyuncs.com/)  

## 2、地图服务资源  
[地图服务资源](http://39.107.104.63:8080/)    
[XYZ地图服务资源](http://39.107.104.63:8080/xyz/1.0.0/)   
[TMS地图服务资源](http://39.107.104.63:8080/tms/1.0.0/)   


## 3、瓦片数据存放目录 
百度云盘瓦片数据存放目录：/Data/Data Service/cateye-tile/     

OSS瓦片数据存放(OSS bucket)：cateye-tile     
[OSS详细使用说明](/dataOps/oss_guide.md)  

## 4、服务部署
数据门户lbi-map；部署路径(OSS bucket)：cmap   
地图服务引擎lbi-tile；部署路径：/opt/springboot,端口8080   
地图渲染引擎Geoserver；部署路径：/opt/tomcat2,端口8888   
地图服务日志备份路径(OSS bucket)：cateye-log