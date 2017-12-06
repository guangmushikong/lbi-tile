package com.lbi.map;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * 
 * 地图类
 * @version	1.0
 * @author liumk
 */
public class TMap {
	public static long transMercator = 0;
	/**
	 * 
	 * 测距
	 * 
	 * @param a	经纬度坐标1
	 * @param b	经纬度坐标2
	 * @return 距离(米)
	 */
	public static double getDistance(Coordinate a, Coordinate b){
    	double c=a.y* Math.PI/180;
    	double d=b.y* Math.PI/180;
    	double dis=(Math.asin(Math.sqrt(Math.pow(Math.sin((c-d)/2),2)+ Math.cos(c)* Math.cos(d)* Math.pow(Math.sin((a.x-b.x)* Math.PI/180/2),2)))*12756274);
    	return dis;
    }
	/**
	 * 
	 * 根据偏移距离计算经纬度
	 * <p>根据原始点经纬度坐标、偏移量，计算偏移后的经纬度坐标	</p>
	 * @param pt	经纬度坐标
	 * @param w	东西方向的偏移量，向东为正，向西为负，单位：米
	 * @param s	南北方向的偏移量，向北为正，向南为负，单位：米
	 * @return 经纬度坐标
	 */
	public static Coordinate getLngLatByOffset(Coordinate pt,double w,double s){
    	double x,y;    	
    	x=pt.x+ Math.asin(Math.sin(Math.round(w)/12756274.0)/ Math.cos(pt.y* Math.PI/180))*360/ Math.PI;
    	y=pt.y+ Math.asin(Math.round(s)/12756274.0)*360/ Math.PI;
    	return  new Coordinate(x,y);
    }
	/**
	 * 
	 * 经纬度转墨卡托坐标
	 * @param lonLat	经纬度坐标
	 * @return 墨卡托坐标
	 */
	public static Coordinate lonLat2Mercator(Coordinate lonLat){
		double x = lonLat.x * 20037508.34 / 180;
        double M_PI = Math.PI;
        double y = Math.log(Math.tan((90 + lonLat.y) * M_PI / 360)) / (M_PI / 180);
        y = y * 20037508.34 / 180;
        return new Coordinate(x, y);
	}
	/**
	 * 
	 * 墨卡托坐标转经纬度
	 * @param mercator 墨卡托坐标
	 * @return 经纬度
	 */
	public static Coordinate Mercator2lonLat(Coordinate mercator){
		double x = mercator.X / 20037508.34 * 180;
        double y = mercator.Y / 20037508.34 * 180;
        double M_PI = Math.PI;
        y = 180 / M_PI * (2 * Math.atan(Math.exp(y * M_PI / 180)) - M_PI / 2);
        return new Coordinate(x, y);
	}
	
	/**
	 * 实际距离转换墨卡托距离
	 * @param lnglat
	 * @param distance
	 * @return
	 */
	public static long getMercatorDistance(Coordinate lnglat, int distance) {
		double c = lnglat.y* Math.PI/180;
		double lng = lnglat.x - Math.asin(Math.sqrt(Math.pow(Math.sin(distance/12756274.0),2)/ Math.pow(Math.cos(c), 2)))/(Math.PI/180/2);
		Coordinate another = new Coordinate(lng,lnglat.y);
		Coordinate l = lonLat2Mercator(lnglat),
	    		s = lonLat2Mercator(another);
	    long dist = Math.round(Math.ceil(Math.abs(l.X - s.X)));
	    return dist;	
	}
	
	/**
	 * 返回实际距离转换成墨卡托距离
	 * @param radius
	 * @return long
	 */
	public static long transMercator(int radius) {//半径的距离(米)
		if (transMercator==0) {
			Coordinate a = new Coordinate(116.987,39.09);
			transMercator = getMercatorDistance(a, radius);
		}
		return transMercator;
	}
	
	
	public static void main(String args[]){
//		SPoint p=new SPoint(1.001875417139462E7,1.5028131257091936E7);
//		LngLat l=Mercator2lonLat(p);
//		System.out.println(l.lat+","+l.lng);
		//73.265915  135.242
		//3.409029   53.5539
		//66.51326044810476,45.00000000626406
		//79.17133464435,90.00000001252808
		Coordinate a = new Coordinate(116.987,39.09);
		Coordinate b = new Coordinate(116.345,39.09);
////		
//		double c=a.lat*Math.PI/180;
//    	//double d=b.lat*Math.PI/180;
		double dis = TMap.getDistance(a,b);
   		System.out.println(dis);
//    	//逆运算 在同一纬度的经度的算法
//    	 double cc = a.lng - Math.asin(Math.sqrt(Math.pow(Math.sin(dis/12756274),2)/Math.pow(Math.cos(c), 2)))/(Math.PI/180/2);
 //   	System.out.println("cc  "+cc);
//		System.out.println(TMap.getLngLatByOffset(a,-55469,0).lng);
//		System.out.println(TMap.getMercatorDistance(a, 55469));
	//	System.out.println(TMap.transMercator(5000));
	}
}
