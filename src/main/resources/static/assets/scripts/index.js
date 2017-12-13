var mapObj;
var cityList;
var commonstyle;
function init(){
    resizeMap();
    initCommonStyle();
    initMap();
}
//var fillColor = 'rgba(149,139,255,0.4)';
//var strokeColor = 'rgb(20,20,20)';
function initCity(){
    $.get(
        "city/getcitylist.json",
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
                    //changeCity($('#m_city').val());
                    var item=cityList[$('#m_city').val()];
                    mapObj.fitBounds([[item.miny,item.minx],[item.maxy, item.maxx]]);
                });
            }
        },"json");
}

function initMap(){
    var baseMaps={
        "Autonavi":L.tileLayer('http://webrd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}'),
        "Google":L.tileLayer('http://mt3.google.cn/vt/lyrs=y@198&hl=zh-CN&gl=cn&src=app&x={x}&y={y}&z={z}&s='),
        "地形":L.tileLayer('http://c.tile.stamen.com/terrain/{z}/{x}/{y}.png')
        //"晕渲图1":L.tileLayer('http://tile.stamen.com/terrain-background/{z}/{x}/{y}.png'),
    };
    var gujiaoLayer = L.tileLayer('city/gujiao/{z}/{x}/{y}.png', {
        tms: true
    });
    var worldLayer = L.tileLayer('city/world/{z}/{x}/{y}.png', {
        tms: true
    });
    var urlTemplate="tms/1.0.0/lbi:s_ods_city_simplify@EPSG:900913@png/{z}/{x}/{y}.png";
    var city_tms_Layer = L.tileLayer(urlTemplate, {
        maxZoom: 11,
        tms: true
    });
    var mvtLayer1=loadGAUL0MVTLayer();
    var mvtLayer2=loadCityMVTLayer();
    var cityLayer=loadCityLayer();
    var overlays={
        '城市XYZ(geojson)':cityLayer,
        '城市TMS(png)':city_tms_Layer,
        '古交XYZ(png)':gujiaoLayer,
        '世界XYZ(png)':worldLayer
    };


    //初始化地图控件
    mapObj = L.map('mapbox', {
        center: [37.9,111.9],
        zoom: 6,
        minZoom:3,
        maxZoom:18,
        zoomControl:false,	//不加载默认zoomControl,
        layers: [baseMaps.Autonavi]
    });
    //LayerControl
    L.control.layers(baseMaps,overlays).addTo(mapObj);
    //mapObj.setView([0,39], 6);
    var zoomControl = L.control.zoom({
        position: 'bottomright',
        zoomInTitle:'放大',
        zoomOutTitle:'缩小'
    }).addTo(mapObj);
    L.control.scale({imperial:false}).addTo(mapObj);//去除英制单位
    viewMapOption();
    mapObj.on('move', viewMapOption);
    //mapObj.on('mousemove', viewCoordinate);
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
function  loadGAUL0MVTLayer() {
    return new L.TileLayer.MVTSource(opts);
}

function  loadCityMVTLayer() {
    return new L.TileLayer.MVTSource({
        url: "city/citymvt/{z}/{x}/{y}",
        //debug: true,
        clickableLayers: ['city'],
        getIDForLayerFeature: function(feature) {
            return feature.properties.code;
        },
        filter: function(feature, context) {
            if (feature.layer.name === 'city')return true;
            return false;
        },
        onClick: function(evt) {
            console.log("click");
        },
        style: function (feature) {
            console.log(feature.properties.code+":"+feature.properties.name+"|"+feature.layer.name);
            //console.log(JSON.stringify(feature));
            //console.log(JSON.parse(feature));
            console.log(feature);
            var style = {};
            var selected = style.selected = {};
            var type = feature.type;
            switch (type) {
                case 1: //'Point'
                    // unselected
                    style.color = '#ff0000';
                    style.radius = 3;
                    // selected
                    selected.color = 'rgba(255,255,0,0.5)';
                    selected.radius = 5;
                    break;
                case 2: //'LineString'
                    // unselected
                    style.color = 'rgba(161,217,155,0.8)';
                    style.size = 3;
                    // selected
                    selected.color = 'rgba(255,25,0,0.5)';
                    selected.size = 3;
                    break;
                case 3: //'Polygon'
                    style.color = 'rgba(149,139,255,0.4)';
                    style.outline = {
                        color: 'rgb(20,20,20)',
                        size: 1
                    };
                    style.selected = {
                        color: 'rgba(255,140,0,0.3)',
                        outline: {
                            color: 'rgba(255,140,0,1)',
                            size: 2
                        }
                    };
                    break;
            }
            return style;
        }
    });
}
function loadCityLayer(){
    var geojsonURL="city/city/{z}/{x}/{y}.json";
    return new L.TileLayer.GeoJSON(geojsonURL,
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
var opts = {
    url: "http://spatialserver.spatialdev.com/services/vector-tiles/gadm2014kenya/{z}/{x}/{y}.pbf",
    //debug: true,
    clickableLayers: ['gadm0', 'gadm1', 'gadm2', 'gadm3', 'gadm4', 'gadm5'],

    getIDForLayerFeature: function(feature) {
        return feature.properties.id;
    },

    /**
     * The filter function gets called when iterating though each vector tile feature (vtf). You have access
     * to every property associated with a given feature (the feature, and the layer). You can also filter
     * based of the context (each tile that the feature is drawn onto).
     *
     * Returning false skips over the feature and it is not drawn.
     *
     * @param feature
     * @returns {boolean}
     */
    filter: function(feature, context) {
        if (feature.layer.name === 'gadm1_label' || feature.layer.name === 'gadm1') {
            return true;
        }

        return false;
    },

    /**
     * When we want to link events between layers, like clicking on a label and a
     * corresponding polygon freature, this will return the corresponding mapping
     * between layers. This provides knowledge of which other feature a given feature
     * is linked to.
     *
     * @param layerName  the layer we want to know the linked layer from
     * @returns {string} returns corresponding linked layer
     */
    /*layerLink: function(layerName) {
        if (layerName.indexOf('_label') > -1) {
            return layerName.replace('_label','');
        }
        return layerName + '_label';
    },*/

    style: function(feature) {
        var style = {};
        var selected = style.selected = {};

        var type = feature.type;
        console.log(feature.properties.id+":"+feature.properties.name+"|"+feature.layer.name);
        console.log(feature);
        switch (type) {
            case 1: //'Point'
                    // unselected
                style.color = '#ff0000';
                style.radius = 3;
                // selected
                selected.color = 'rgba(255,255,0,0.5)';
                selected.radius = 5;
                break;
            case 2: //'LineString'
                    // unselected
                style.color = 'rgba(161,217,155,0.8)';
                style.size = 3;
                // selected
                selected.color = 'rgba(255,25,0,0.5)';
                selected.size = 3;
                break;
            case 3: //'Polygon'
                    // unselected
                style.color = 'rgba(149,139,255,0.4)';
                style.outline = {
                    color: 'rgb(20,20,20)',
                    size: 2
                };
                // selected
                selected.color = 'rgba(255,25,0,0.3)';
                selected.outline = {
                    color: '#d9534f',
                    size: 3
                };
        }

        if (feature.layer.name === 'gadm1_label') {
            style.staticLabel = function() {
                var style = {
                    html: feature.properties.name,
                    iconSize: [125,30],
                    cssClass: 'label-icon-text'
                };
                return style;
            };
        }

        return style;
    }
}