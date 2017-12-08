<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Demo - OpenLayers</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="assets/components/openlayers3/ol.css" rel="stylesheet"/>
    <link href="css/loc.css" rel="stylesheet"/>
</head>
<body onload="init()">
<div id="mapbox" style="width: 100%; height:600px;border: 1px solid #888;"></div>
<div class="m_statusbar">
    <div id="i_coordinate"></div>
    <div><label id="i_map">级别=</label></div>
</div>
</body>
<script src="assets/components/jquery/jQuery-2.1.1.min.js"></script>
<script src="assets/components/openlayers3/ol.js"></script>

<script src="assets/scripts/controller/openlayers_demo.js"></script>
</html>