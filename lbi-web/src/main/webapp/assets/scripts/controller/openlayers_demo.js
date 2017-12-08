var mapObj;
function init(){
    resizeMap();
    initMap();
}
function initMap(){
    var mousePositionControl = new ol.control.MousePosition({
        coordinateFormat:ol.coordinate.createStringXY(7),
        projection: 'EPSG:4326',
        className: 'custom-mouse-position',
        target: document.getElementById('i_coordinate'),
        undefinedHTML: '&nbsp;'
    });
    var basemap= new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    //var city_wms_layer =initCityWMSLayer();
    var city_vt_layer =initCityWMTSLayer();
    mapObj= new ol.Map({
        controls: ol.control.defaults({
            attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
                collapsible: false
            })
        }).extend([mousePositionControl]),
        target: 'mapbox',
        layers: [
            basemap,
            city_vt_layer
        ],
        view: new ol.View({
            center: ol.proj.fromLonLat([111.9,37.9]),
            zoom: 4
        })
    });
    /*var switcherControl = new OpenLayers.Control.LayerSwitcher();
    mapObj.addControl(switcherControl);
    switcherControl.maximizeControl();*/

    mapObj.on('moveend', viewMapOption);
}
function initCityWMSLayer(){
    return new ol.layer.Tile({
        visible: true,
        source: new ol.source.TileWMS({
            url: 'http://54.223.166.139:8888/geoserver/lbi/wms',
            params: {'FORMAT': 'image/png',
                'VERSION': '1.1.1',
                tiled: true,
                STYLES: '',
                LAYERS: 'lbi:s_ods_city_simplify',
                tilesOrigin: 73.1985473632813 + "," + 3.58836889266968
            }
        })
    });
}
function initCityWMTSLayer(){
    var urlTemplate="http://localhost:8888/geoserver/gwc/service/tms/1.0.0/lbi:s_ods_city_simplify@EPSG%3A4326@pbf/{z}/{x}/{y}.pbf";
    return new ol.layer.VectorTile({
        // 矢量切片的数据源
        source: new ol.source.VectorTile({
            format: new ol.format.MVT(),
            tileGrid: ol.tilegrid.createXYZ({maxZoom: 22}),
            tilePixelRatio: 1,
            // 矢量切片服务地址
            url: urlTemplate
        }),
        // 对矢量切片数据应用的样式
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgb(140,137,129)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgb(220, 220, 220)',
                width: 1
            })
        })
    });
}
/**
 * 容器改变触发
 */
function resizeMap(){
    //初始化宽度、高度
    $("#mapbox").height($(window).height()-60);
    //当文档窗口发生改变时 触发
    $(window).resize(function(){
        $("#mapbox").height($(window).height()-60);
    });
}

function viewCoordinate(evt){
    if(evt!=null) $("#i_coordinate").text("当前坐标:"+evt.latlng.lng.toFixed(7)+","+evt.latlng.lat.toFixed(7));
}
/**
 * 显示地图状态信息
 */
function viewMapOption(evt){
    var view=evt.map.getView();
    var center=ol.proj.transform(view.getCenter(),'EPSG:3857', 'EPSG:4326');
    var extent = view.calculateExtent(evt.map.getSize());
    var bottomLeft = ol.proj.transform(ol.extent.getBottomLeft(extent),
        'EPSG:3857', 'EPSG:4326');
    var topRight = ol.proj.transform(ol.extent.getTopRight(extent),
        'EPSG:3857', 'EPSG:4326');
    $("#i_map").text("级别:"+view.getZoom()+",中心("+ center[0].toFixed(7)+","+center[1].toFixed(7)+"),边界["+bottomLeft[0].toFixed(7)+","+bottomLeft[1].toFixed(7)+","+ topRight[0].toFixed(7)+","+ topRight[1].toFixed(7)+"]");
}
