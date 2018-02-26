var mapObj;
var cityList;
var commonstyle;
function init(){
    resizeMap();
    initCommonStyle();
    initMap();
    getRemoteIP();
}

function initCity(){
    $.get(
        "http://39.107.104.63:8080/city/getcitylist.json",
        function(json){
            if(json.success){
                var list=json.data;
                cityList=[];
                for(var i=0;i<list.length;i++){
                    var item=list[i];
                    cityList[item.adcode]=item;
                    var li='<option value="'+item.adcode+'"><i class="icon-star"></i>'+item.name+'</option>';
                    $("#m_city").append(li);
                }
                $("#m_city").select2();
                $('#m_city').change(function(){
                    var item=cityList[$('#m_city').val()];
                    mapObj.fitBounds([[item.miny,item.minx],[item.maxy, item.maxx]]);
                });
            }
        },"json");
}
function initBaseMaps(){
    var basemap_normal=L.tileLayer('http://webrd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}');
    var basemap_satellite=L.tileLayer('http://mt3.google.cn/vt/lyrs=y@198&hl=zh-CN&gl=cn&src=app&x={x}&y={y}&z={z}&s=');
    var basemap_terrain=L.tileLayer('http://c.tile.stamen.com/terrain/{z}/{x}/{y}.png');

    var baseMaps={
        "地图":basemap_normal,
        "地球":basemap_satellite,
        "地形":basemap_terrain
    };
    return baseMaps;
}
function initOverlays(){
    var gujiao_satellite_xyz_png_Layer = L.tileLayer('http://39.107.104.63:8080/xyz/gujiao/{x}/{y}/{z}.png', {maxZoom: 17});
    var world_satellite_xyz_png_Layer = L.tileLayer('http://39.107.104.63:8080/xyz/world/{x}/{y}/{z}.jpeg', {maxZoom: 13});
    var china_city_xyz_png_Layer = L.tileLayer('http://39.107.104.63:8080/xyz/city/{x}/{y}/{z}.png', {maxZoom: 13});
    var china_city_xyz_geojson_Layer=initChina_City_xyz_geojson_Layer();

    var gujiao_satellite_tms_png_Layer= L.tileLayer("http://39.107.104.63:8080/tms/1.0.0/gujiao_satellite_raster@EPSG:900913@png/{z}/{x}/{y}.png", {
        maxZoom: 17,
        tms: true
    });
    var world_satellite_tms_png_Layer= L.tileLayer("http://39.107.104.63:8080/tms/1.0.0/world_satellite_raster@EPSG:900913@jpeg/{z}/{x}/{y}.jpeg", {
        maxZoom: 13,
        tms: true
    });
    var china_city_tms_png_Layer= L.tileLayer("http://39.107.104.63:8080/tms/1.0.0/china_city_polygon@EPSG:900913@png/{z}/{x}/{y}.png", {
        maxZoom: 11,
        tms: true
    });


    var overlays={
        '中国城市XYZ(geojson)':china_city_xyz_geojson_Layer,
        '中国城市XYZ(png)':china_city_xyz_png_Layer,
        '中国城市TMS(png)':china_city_tms_png_Layer,
        '古交卫星XYZ(png)':gujiao_satellite_xyz_png_Layer,
        '古交卫星TMS(png)':gujiao_satellite_tms_png_Layer,
        '世界卫星XYZ(png)':world_satellite_xyz_png_Layer,
        '世界卫星TMS(png)':world_satellite_tms_png_Layer
    };
    return overlays;
}
function initMap(){
    var basemap=L.tileLayer('http://webrd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}');
    var baseMaps=initBaseMaps();
    var overlays=initOverlays();

    //初始化地图控件
    mapObj = L.map('mapbox', {
        center: [37.9,111.9],
        zoom: 6,
        minZoom:3,
        maxZoom:18,
        zoomControl:false,	//不加载默认zoomControl,
        layers: [basemap]
    });

    var zoomControl = L.control.zoom({
        position: 'bottomright',
        zoomInTitle:'放大',
        zoomOutTitle:'缩小'
    }).addTo(mapObj);

    L.control.layers(baseMaps,overlays).addTo(mapObj);
    L.control.scale({imperial:false}).addTo(mapObj);//去除英制单位
    viewMapOption();
    mapObj.on('move', viewMapOption);
    mapObj.on('mousemove', viewCoordinate);

    initMenuBar();
    initStatusBar();
}
/**
 * 创建菜单条
 */
function initMenuBar(){
    var menu = L.control({position: 'topleft'});
    menu.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'btn-group'); // create a div with a class "info"
        this._div.id="m_menubar";
        this.update();
        return this._div;
    };
    menu.update = function (props) {
        var str='<select id="m_city" class="select2"></select>';
        this._div.innerHTML = str;
    };
    menu.addTo(mapObj);
    initCity();
}
/**
 * 创建状态条
 */
function initStatusBar() {
    var status = L.control({position: 'topleft'});
    status.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'm_statusbar'); // create a div with a class "info"
        this.update();
        return this._div;
    };
    status.update = function (props) {
        var str='<div class="col-xs-6"><label id="i_coordinate"></label></div>';
        str+='<div class="col-xs-6"><label id="i_map"></label></div>';
        str+='<div class="col-xs-3"><label id="i_show"></label></div>';
        this._div.innerHTML = str;
    };
    status.addTo(mapObj);
}
function getRemoteIP(){
    $("#i_show").text("您的访问IP:"+returnCitySN["cip"]);
}
function initCommonStyle(){
    commonstyle={};
    var myStyle={
        fillColor:'#FD8D3C',
        fillOpacity:0.3,
        color:'#000',
        dashArray:3,
        weight:2
    };
    commonstyle['init']=myStyle;
    myStyle={
        fillColor:'gray',
        fillOpacity:0.3,
        color:'#000',
        dashArray:3,
        weight:2
    };
    commonstyle['gray']=myStyle;
    myStyle={
        fillColor:'blue',
        fillOpacity:0.3,
        color:'#000',
        dashArray:3,
        weight:2
    };
    commonstyle['blue']=myStyle;
    myStyle={
        fillColor:'green',
        fillOpacity:0.3,
        color:'#000',
        dashArray:3,
        weight:2
    };
    commonstyle['green']=myStyle;
    myStyle={
        fillColor:'red',
        fillOpacity:0.7,
        color:'#f03',
        dashArray:'',
        weight:5
    };
    commonstyle['high']=myStyle;
    var svgIcon=new L.DivIcon.SVGIcon({
        color:'red',
        fillColor : 'red',
        iconSize : L.point(10,10)
    });
    var marker=new L.Marker({icon:svgIcon});
    commonstyle['poi']=marker;
}

function initChina_City_xyz_geojson_Layer(){
    var urlTemplate="http://39.107.104.63:8080/xyz/city/{x}/{y}/{z}.json";
    return new L.TileLayer.GeoJSON(urlTemplate,
        {
            clipTiles:true,
            unique:function(feature){
                return feature.properties.code;
            }
        },{
            style:commonstyle['gray'],
            onEachFeature:function(feature,layer){
                if(feature.properties){
                    var labelString='';
                    for(var k in feature.properties){
                        var v=feature.properties[k];
                        labelString+=k+':'+v+'<br/>';
                    }
                    layer.bindLabel(labelString);
                }
                if(!(layer instanceof L.Point)){
                    layer.on('mouseover',function(){
                        layer.setStyle(commonstyle['high']);
                    })
                    layer.on('mouseout',function(){
                        layer.setStyle(commonstyle['gray']);
                    })
                }
            }
        });
}


/**
 * 容器改变触发
 */
function resizeMap(){
    //初始化宽度、高度
    $("#mapbox").height($(window).height()-20);
    //当文档窗口发生改变时 触发
    $(window).resize(function(){
        $("#mapbox").height($(window).height()-20);
    });
}
function viewCoordinate(evt){
    if(evt!=null) $("#i_coordinate").text("当前坐标:"+evt.latlng.lng.toFixed(7)+","+evt.latlng.lat.toFixed(7));
}
/**
 * 显示地图状态信息
 */
function viewMapOption(){
    var bounds=mapObj.getBounds();
    $("#i_map").text("级别="+mapObj.getZoom()+",中心("+bounds.getCenter().lng.toFixed(6)+","+bounds.getCenter().lat.toFixed(6)+"),边界["+bounds.getSouthWest().lng.toFixed(6)+","+bounds.getNorthEast().lng.toFixed(6)+","+bounds.getSouthWest().lat.toFixed(6)+","+bounds.getNorthEast().lat.toFixed(6)+"]");
}
