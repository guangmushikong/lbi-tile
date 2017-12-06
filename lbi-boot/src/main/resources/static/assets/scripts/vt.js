var mapObj;
function init(){
    initMap2();
}
function initMapBoxMap(){
    mapboxgl.accessToken = 'pk.eyJ1IjoibGl1bWsiLCJhIjoiY2phY2pyZWp6MGFtMjJ6cGw2YnhqM2F6aCJ9.7ganszcmTQ4q_yRYeLc99g';
    var map = new mapboxgl.Map({
        container: 'mapbox',
        style: 'mapbox://styles/mapbox/streets-v9'
    });
}
function initMap2(){
    var url='http://t0.tianditu.com/vec_c/wmts';
    var matrixIds = [];
    for (var i=0; i<14; ++i) {
        matrixIds[i] = {
            identifier:1+i,
            topLeftCorner : new L.LatLng(90,-180)
        };
    }
    var emap  = new L.TileLayer.WMTS( url ,
        {
            tileSize:256,
            layer: "vec",
            style: "default",
            tilematrixSet: "c",
            format: "tile",
            matrixIds: matrixIds,
            attribution: "<a href='https://github.com/mylen/leaflet.TileLayer.WMTS'>GitHub</a>&copy; <a href='http://www.ign.fr'>IGN</a>"
        }
    );
    mapObj = L.map('mapbox',{crs:L.CRS.EPSG4326,center: {lon:112 , lat:40},zoom: 13})
    mapObj.addLayer(emap)
}
function initMap(){
    //初始化地图控件
    mapObj = L.map('mapbox',{
            crs: L.CRS.EPSG4326
        })
        .setView([39.904989,116.405285], 7);
    var basemap=L.tileLayer('http://webrd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}');
    basemap.addTo(mapObj);
    //var mvtSource=loadCityMVTLayer();
    var mvtSource=loadWMTSLayer();
    mapObj.addLayer(mvtSource);
}
function loadWMTSLayer(){
    var url = "http://localhost:8888/geoserver/gwc/service/wmts";
    var matrixIds = [];
    for (var i=0; i<14; ++i) {
        matrixIds[i] = {
            identifier:1+i,
            topLeftCorner : new L.LatLng(90,-180)
        };
    }
    var layerIGNScanStd = "lbi:s_ods_city_simplify";
    var ign = new L.TileLayer.WMTS( url ,
        {
            layer: layerIGNScanStd,
            style: "generic",
            tilematrixSet: "EPSG:4326",
            format: "image/png",
            matrixIds: matrixIds
            //attribution: "<a href='https://github.com/mylen/leaflet.TileLayer.WMTS'>GitHub</a>&copy; <a href='http://www.ign.fr'>IGN</a>"
        }
    );
    return ign;
}
function  loadPoiMVTLayer() {
    return new L.TileLayer.MVTSource({
        url: "city/poimvt/{z}/{x}/{y}",
        tms: true,
        debug: true,
        clickableLayers: ['poi'],
        getIDForLayerFeature: function(feature) {
            return feature.properties.code;
        },
        filter: function(feature, context) {
            if (feature.layer.name === 'poi')return true;
            return false;
        },
        onClick: function(evt) {
            console.log("click");
        },
        style: function (feature) {
            console.log(feature.properties.id+":"+feature.properties.name+"|"+feature.layer.name);
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
                    style.color = 'red';
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
function  loadCityMVTLayer() {
    var urlTemplate="http://localhost:8888/geoserver/gwc/service/wmts?REQUEST=GetTile&SERVICE=WMTS&VERSION=1.0.0&LAYER=lbi:s_ods_city_simplify&STYLE=&TILEMATRIX=EPSG:4326:{z}&TILEMATRIXSET=EPSG:4326&FORMAT=application/x-protobuf;type=mapbox-vector&TILECOL={x}&TILEROW={y}";
    return new L.TileLayer.MVTSource({
        url: urlTemplate,
        tms: true,
        debug: true,
        clickableLayers: ['lbi:s_ods_city_simplify'],
        getIDForLayerFeature: function(feature) {
            return feature.properties.adcode;
        },
        filter: function(feature, context) {
            if (feature.layer.name === 'lbi:s_ods_city_simplify')return true;
            return false;
        },
        onClick: function(evt) {
            console.log("click");
        },
        style: function (feature) {
            console.log(feature.properties.adcode+":"+feature.properties.name+"|"+feature.layer.name);
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
                    style.color = 'red';
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
