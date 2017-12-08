
## 1、地图服务列表  
  
[Demo](http://54.223.166.139:8080)  

<table>
<tr>
	<td>ID</td>    
	<td>名称</td> 
    <td>协议</td>
	<td>输出格式</td>
    <td>URL模板</td>
    <td>备注</td> 
</tr>
<tr>
	<td>1</td>    
	<td>古交</td> 
	<td>XYZ</td>
	<td>png</td>
	<td>http://54.223.166.139:8080/tile/gujiao/{z}/{x}/{y}</td>
    <td> </td> 
</tr>
<tr>
	<td>2</td>    
	<td>世界</td> 
	<td>XYZ</td><td>png</td>
	<td>http://54.223.166.139:8080/tile/world/{z}/{x}/{y}</td>
    <td> osgearth需要 invert_y=true</td> 
</tr>
<tr>
	<td rowspan="5">3</td>    
	<td>城市</td> 
	<td>XYZ</td>
	<td>geojson</td>
	<td>http://54.223.166.139:8080/tile/city/{z}/{x}/{y}</td>
    <td> </td> 
</tr>
<tr>   
	<td>城市</td> 
	<td>WMS</td>
	<td>png</td>
	<td>http://54.223.166.139:8888/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities</td>
    <td> </td> 
</tr>
<tr>   
	<td>城市</td> 
	<td>WMTS</td>
	<td>png/geojson/mapbox-vector</td>
	<td>http://54.223.166.139:8888/geoserver/gwc/service/wmts</td>
    <td> </td> 
</tr>
<tr>   
	<td>城市</td> 
	<td>TMS</td>
	<td>png/geojson</td>
	<td>http://54.223.166.139:8888/geoserver/gwc/service/tms/1.0.0/lbi:s_ods_city_simplify@EPSG:900913@png/{z}/{x}/{y}.png</td>
    <td>支持4326和900913</td> 
</tr>
</table>

<pre><code>
--wmts示例地址
http://54.223.166.139:8888/geoserver/gwc/service/wmts?REQUEST=GetTile&SERVICE=WMTS&VERSION=1.0.0&LAYER=lbi:s_ods_city_simplify&STYLE=&TILEMATRIX=EPSG:900913:5&TILEMATRIXSET=EPSG:900913&FORMAT=application/x-protobuf;type=mapbox-vector&TILECOL=23&TILEROW=13
--tms示例地址
http://54.223.166.139:8888/geoserver/gwc/service/tms/1.0.0/lbi:s_ods_city_simplify@EPSG:900913@geojson/5/22/20.geojson
http://54.223.166.139:8888/geoserver/gwc/service/tms/1.0.0/lbi:s_ods_city_simplify@EPSG:900913@png/5/22/20.png
</code></pre>

## 2、瓦片数据存放目录 

服务器（http://54.223.166.139）瓦片数据统一存放目录/home/ec2-user/tiledata 
   
瓦片坐标投影为900913；	
瓦片图片为png格式；    

<table>
<tr>
	<td>ID</td>    
	<td>名称</td> 
    <td>路径</td>
    <td>备注</td> 
</tr>
<tr>
	<td>1</td>    
	<td>古交</td> 
    <td>/home/ec2-user/tiledata/Gujiao_Image_Mercator</td>
    <td> </td> 
</tr>
<tr>
	<td>2</td>    
	<td>世界</td> 
    <td>/home/ec2-user/tiledata/world_mercator</td>
    <td> </td> 
</tr>
</table>

## 3、地图服务

地图服务引擎lbi-tile,部署路径/home/ec2-user/tomcat1,端口8080		
	
地图渲染引擎Geoserver,部署路径/home/ec2-user/tomcat2,端口8888	


 
