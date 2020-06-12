## 1、数据门户
[数据门户](http://cmap.oss-cn-beijing.aliyuncs.com/)    
[服务列表](http://cmap.oss-cn-beijing.aliyuncs.com/directory.html)  
[数据设计](/dataOps/database.md) 

[凤凰云数据门户](http://211.154.194.45:8888)
## 2、地图服务资源  
[凤凰云地图服务资源](http://211.154.194.45:8080/)    
[XYZ地图服务资源](http://211.154.194.45:8080/xyz/1.0.0/)   
[TMS地图服务资源](http://211.154.194.45:8080/tms/1.0.0/)   


## 3、瓦片数据存放目录 
百度云盘瓦片数据存放目录：/Data/Data Service/cateye-tile/     

OSS瓦片数据存放(OSS bucket)：cateye-tile     
[OSS详细使用说明](/dataOps/oss_guide.md)  

## 4、服务部署
地图瓦片数据路径：/home/dev/data/tile
地图服务引擎路径：/home/dev/springboot,端口8080   
```
nohup java -jar lbi-tile-1.0-SNAPSHOT.jar --spring.profiles.active=prod &
```
