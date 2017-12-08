<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>GeoJSON切片</title>
    <link href="assets/components/openlayers3/ol.css" rel="stylesheet"/>
    <script src="assets/components/openlayers3/ol.js"></script>
    <script src="assets/components/jquery/jQuery-2.1.1.min.js"></script>
    <style>
        body,html,#map{
            padding:0px;
            margin:0px;
            height:100%;
            overflow:hidden;
            background:#ffffff;
        }
        .ol-mouseposition{
            position:absolute;
            bottom:20px;
            left:45%;
            padding:5px;
            background:#fff;
            border:1px solid #ccc;
            font-size:12px;
        }
        ul{
            list-style: none;
            position: absolute;
            right:10px;
            top:10px;
            font-size: 14px;
            z-index:99;
        }
        ul li{
            line-height: 28px;
            height: 28px;
            text-align: center;
            width:40px;
            float:left;
            cursor:pointer;
            background: #ffffff;
        }
        ul li.active{
            background: #007AC2;
            opacity: 0.8;
            color:#ffffff;
        }
        ul li.day{
            border:1px solid #007AC2;
            border-right: none;
            border-top-left-radius: 3px;
            border-bottom-left-radius: 3px;
        }
        ul li.night{
            border:1px solid #007AC2;
            border-left: none;
            border-top-right-radius: 3px;
            border-bottom-right-radius: 3px;
        }
    </style>
</head>
<body>
<ul id="lyrstyle">
    <li class="day active">白天</li>
    <li class="night">黑夜</li>
</ul>
<div id="map" class="map" tabindex="0"></div>
<script>
    var projection4326 = new ol.proj.Projection({
        code: 'EPSG:4326',
        units: 'degrees',
    });
    var lyr = "lbi:s_ods_city_simplify";
    var dayStyle = function(feature,resolution){
        var stroke = new ol.style.Stroke({color: "rgba(0,0,255,0.4)", width: 2});
        var fill = new ol.style.Fill({color:"rgba(0,0,255,0)"});
        var lyr = feature.f.split(".")[0];
        if(lyr=="province"){
            return new ol.style.Style({
                fill: fill,
                stroke: stroke
            });
        }
        else{
            var name = feature.get("name");
            return new ol.style.Style({
                image: new ol.style.Circle({
                    radius: 5,
                    fill: new ol.style.Fill({
                        color: 'rgba(0,0,255,0.4)'
                    })
                }),
                text: new ol.style.Text({ //文本样式
                    font: '15px Calibri,sans-serif',
                    fill: new ol.style.Fill({
                        color: '#000000'
                    }),
                    text:name,
                    offsetX:0,
                    offsetY:12
                })
            });
        }
    };
    var nightStyle = function(feature,resolution){
        var stroke = new ol.style.Stroke({color: "rgba(255,255,255,0.4)", width: 2});
        var fill = new ol.style.Fill({color:"rgba(0,0,0,0.8)"});
        var lyr = feature.f.split(".")[0];
        if(lyr=="province"){
            return new ol.style.Style({
                fill: fill,
                stroke: stroke
            });
        }
        else{
            var name = feature.get("name");
            return new ol.style.Style({
                image: new ol.style.Circle({
                    radius: 5,
                    fill: new ol.style.Fill({
                        color: 'rgba(255,255,255,1)'
                    })
                }),
                text: new ol.style.Text({ //文本样式
                    font: '15px Calibri,sans-serif',
                    fill: new ol.style.Fill({
                        color: '#ffffff'
                    }),
                    text:name,
                    offsetX:0,
                    offsetY:12
                })
            });
        }
    };
    // 行政区划图层
    var vector = new ol.layer.VectorTile({
        // 矢量切片的数据源
        source: new ol.source.VectorTile({
            projection: projection4326,
            format: new ol.format.GeoJSON(),
            tileGrid: ol.tilegrid.createXYZ({
                extent: ol.proj.get('EPSG:4326').getExtent(),
                maxZoom: 22
            }),
            tilePixelRatio: 1,
            // 矢量切片服务地址
            tileUrlFunction: function(tileCoord){
                return 'http://localhost:8888/geoserver/gwc/service/tms/1.0.0/'
                    +lyr+'@My_EPSG:4326@geojson/'+(tileCoord[0]-1)
                    + '/'+tileCoord[1] + '/' + (Math.pow(2,tileCoord[0]-1)+tileCoord[2]) + '.geojson';
            }
        }),
        style:dayStyle
    });
    var tiled = new ol.layer.Tile({
        visible: true,
        source: new ol.source.TileWMS({
            url: 'http://localhost:8888/geoserver/lbi/wms',
            params: {'FORMAT': 'image/png',
                'VERSION': '1.1.1',
                tiled: true,
                STYLES: 'green',
                LAYERS: 'lbi:s_ods_city_simplify',
                tilesOrigin: 73.4510046356223 + "," + 18.1632471876417
            }
        })
    });
    var map = new ol.Map({
        layers: [
            //tiled,
            vector
        ],
        target: 'map',
        controls: ol.control.defaults().extend([
            new ol.control.MousePosition({
                className:"ol-mouseposition",
                coordinateFormat: ol.coordinate.createStringXY(5)
            })
        ]),
        view: new ol.View({
            projection: projection4326,
            minZoom:4,
            maxZoom:18,
            center: [103.2921875, 38.581640625],
            zoom: 4
        })
    });

    $("#lyrstyle li.day").on("click",function(){
        $("#lyrstyle li").removeClass("active");
        $(this).addClass("active");
        vector.setStyle(dayStyle);
        var zoom = map.getView().getZoom();
        map.getView().setZoom(zoom-1);
        $("#map").css("background","#ffffff");
    });
    $("#lyrstyle li.night").on("click",function(){
        $("#lyrstyle li").removeClass("active");
        $(this).addClass("active");
        vector.setStyle(nightStyle);
        var zoom = map.getView().getZoom();
        map.getView().setZoom(zoom+1);
        $("#map").css("background","rgba(0,0,0,0.8)");
    });
</script>
</body>
</html>