package com.lbi.map;

import com.vividsolutions.jts.geom.Coordinate;

public class CoordTransform {
    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    //private static double pi = 3.1415926535897932384626;  //π
    private static double a = 6378245.0;  //长半轴
    private static double ee = 0.00669342162296594323;  //扁率
    /**
     * GCJ-02 坐标转换成 BD-09 坐标
     * @param gd 火星坐标(GCJ-02)
     * @return 百度坐标(BD-09)
     */
    public static Coordinate bd_encrypt(Coordinate gd){
        double x = gd.x;
        double y = gd.y;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new Coordinate(bd_lon,bd_lat);
    }

    /**
     * BD-09 坐标转换成 GCJ-02 坐标
     * @param bd 百度坐标(BD-09)
     * @return 火星坐标(GCJ-02)
     */
    public static Coordinate bd_decrypt(Coordinate bd){
        double x = bd.x - 0.0065;
        double y = bd.y - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new Coordinate(gg_lon,gg_lat);
    }

    /**
     * 世界标准地理坐标(WGS-84) 转换成 中国国测局地理坐标（GCJ-02）<火星坐标>
     *
     * ####只在中国大陆的范围的坐标有效，以外直接返回世界标准坐标
     * @param wgs84 世界标准地理坐标(WGS-84)坐标
     * @return 国测局地理坐标（GCJ-02）<火星坐标>
     */
    public static Coordinate Wgs84ToGcj02(Coordinate wgs84){
        if(out_of_china(wgs84))return wgs84;
        double dlat = transformlat(wgs84.x - 105.0, wgs84.y - 35.0);
        double dlng = transformlng(wgs84.x - 105.0, wgs84.y - 35.0);
        double radlat = wgs84.y / 180.0 * Math.PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) *  Math.PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) *  Math.PI);
        double mglat = wgs84.y + dlat;
        double mglng = wgs84.x + dlng;

        return new Coordinate(mglng,mglat);
    }

    /**
     *  中国国测局地理坐标（GCJ-02）<火星坐标> 转换成 世界标准地理坐标(WGS-84)
     *
     * ####此接口有1－2米左右的误差，需要精确定位情景慎用
     * @param gcj02 国测局地理坐标（GCJ-02）<火星坐标>
     * @return 世界标准地理坐标(WGS-84)坐标
     */
    public static Coordinate Gcj02ToWgs84(Coordinate gcj02){
        if(out_of_china(gcj02))return gcj02;
        double dlat = transformlat(gcj02.x - 105.0, gcj02.y - 35.0);
        double dlng = transformlng(gcj02.x - 105.0, gcj02.y - 35.0);
        double radlat = gcj02.y / 180.0 * Math.PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) *  Math.PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) *  Math.PI);
        double mglat = gcj02.y + dlat;
        double mglng = gcj02.x + dlng;

        return new Coordinate(gcj02.x*2-mglng,gcj02.y*2-mglat);
    }

    /**
     *  世界标准地理坐标(WGS-84)坐标 转换成 百度地理坐标（BD-09)
     *
     * @param wgs84 世界标准地理坐标(WGS-84)坐标
     * @return 百度地理坐标（BD-09)
     */
    public static Coordinate Wgs84ToBd09(Coordinate wgs84){
        return null;
    }

    /**
     *  国测局地理坐标（GCJ-02）<火星坐标> 转换成 百度地理坐标（BD-09)
     *
     * @param gcj02 国测局地理坐标（GCJ-02）<火星坐标>
     * @return 百度地理坐标（BD-09)
     */
    public static Coordinate Gcj02ToBd09(Coordinate gcj02){
        double x = gcj02.x;
        double y = gcj02.y;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new Coordinate(bd_lon,bd_lat);
    }

    /**
     *  百度地理坐标（BD-09) 转换成 国测局地理坐标（GCJ-02）<火星坐标>
     *
     * @param bd09 百度地理坐标（BD-09)
     * @return  国测局地理坐标（GCJ-02）<火星坐标>
     */
    public static Coordinate Bd09ToGcj02(Coordinate bd09){
        double x = bd09.x - 0.0065;
        double y = bd09.y - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);

        return new Coordinate(gg_lon,gg_lat);
    }

    /**
     *  百度地理坐标（BD-09) 转换成 世界标准地理坐标（WGS-84）
     *
     * @param bd09 百度地理坐标（BD-09)
     * @return  世界标准地理坐标（WGS-84）
     */
    public static Coordinate Bd09ToWgs84(Coordinate bd09){
        return null;
    }
    private static double transformlat(double lng, double lat){
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * Math.PI) + 320 * Math.sin(lat * Math.PI / 30.0)) * 2.0 / 3.0;

        return ret;
    }
    private static double transformlng(double lng, double lat){
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * Math.PI) + 40.0 * Math.sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * Math.PI) + 300.0 * Math.sin(lng / 30.0 * Math.PI)) * 2.0 / 3.0;

        return ret;
    }
    /**
     * 判断是否在国内，不在国内不做偏移
     * @param pt 坐标
     * @return
     */
    private static boolean out_of_china(Coordinate pt){
        if(pt.x < 72.004 || pt.x > 137.8347) return true;
        if(pt.y < 0.8293 || pt.y > 55.8271) return true;

        return false;
    }
}
