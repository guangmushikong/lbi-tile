<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome - </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/leaflet.css" rel="stylesheet">
    <link href="css/leaflet.label.css" rel="stylesheet">
    <link href="css/select2.css" rel="stylesheet">
</head>
<body onload="init()">
<div id="mapbox" style="width: 100%; height:100%;"></div>
<div class="m_statusbar">
    <div id="i_coordinate"></div>
    <div><label id="i_map">级别=</label></div>
</div>
</body>
<script src="assets/components/jquery/jQuery-2.1.4.min.js"></script>
<script src="assets/components/select2/select2.js"></script>
<script src="assets/components/leaflet/leaflet.js"></script>
<script src="assets/components/leaflet/leaflet.label.js"></script>
<script src="assets/components/leaflet/TileLayer.GeoJSON.js"></script>
<script src="assets/components/leaflet-svgicon/svg-icon.js"></script>

<script src="assets/scripts/controller/tmap.js"></script>
</html>