var mapObj;
var cityList;
var commonstyle;

function init(){
    resizeMap();
    initCommonStyle();
    initMap();
}
function initCity(){
    $.get(
        "city/getcitylist",
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

function initMap(){
    var baseMaps={
        "Autonavi":L.tileLayer('http://webrd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}'),
        "Google":L.tileLayer('http://mt3.google.cn/vt/lyrs=y@198&hl=zh-CN&gl=cn&src=app&x={x}&y={y}&z={z}&s='),
        "地形图":L.tileLayer('http://tile.stamen.com/terrain-background/{z}/{x}/{y}.png')
        //"晕渲图2":L.tileLayer('http://c.tile.stamen.com/terrain/{z}/{x}/{y}.png')
    };
    var gujiaoLayer = L.tileLayer('tile/gujiao/{z}/{x}/{y}', {
        tms: true
    });
    var worldLayer = L.tileLayer('tile/world/{z}/{x}/{y}', {
        tms: true
    });
    var cityLayer=loadCityLayer();
    var overlays={
        '城市':cityLayer,
        '古交':gujiaoLayer,
        '世界':worldLayer
    };

    //初始化地图控件
    mapObj = L.map('mapbox', {
        center: [37.9,111.9],
        zoom: 10,
        minZoom:3,
        maxZoom:18,
        zoomControl:false,	//不加载默认zoomControl,
        layers: [baseMaps.Autonavi]
    });
    //LayerControl
    L.control.layers(baseMaps,overlays).addTo(mapObj);
    var zoomControl = L.control.zoom({
        position: 'bottomright',
        zoomInTitle:'放大',
        zoomOutTitle:'缩小'
    }).addTo(mapObj);
    L.control.scale({imperial:false}).addTo(mapObj);//去除英制单位

    initMenuBar();

    viewMapOption();
    mapObj.on('move', viewMapOption);
    mapObj.on('mousemove', viewCoordinate);
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
function loadCityLayer(){
    var geojsonURL="tile/city/{z}/{x}/{y}";
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