package com.chinobot.common.utils;

import java.util.Arrays;

public class GPSUtil {
	public static double pi = 3.1415926535897932384626;  
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;  
    public static double a = 6378245.0;  
    public static double ee = 0.00669342162296594323;  
    private static final Double PI = Math.PI;
    private static final Double PK = 180 / PI;

    /**
     * @Description: 根据经纬度计算两点之间的距离
     *
     * @param lat_a a的纬度
     * @param lng_a a的经度
     * @param lat_b b的纬度
     * @param lng_b b的经度
     * @return 距离
     */
    public static String getDistance(double lat_a, double lng_a, double lat_b, double lng_b) {
      double t1 =
          Math.cos(lat_a / PK) * Math.cos(lng_a / PK) * Math.cos(lat_b / PK) * Math.cos(lng_b / PK);
      double t2 =
          Math.cos(lat_a / PK) * Math.sin(lng_a / PK) * Math.cos(lat_b / PK) * Math.sin(lng_b / PK);
      double t3 = Math.sin(lat_a / PK) * Math.sin(lat_b / PK);

      double tt = Math.acos(t1 + t2 + t3);
      String res = (6366000 * tt) + "";
      return res.substring(0, res.indexOf("."));
    }
    public static double transformLat(double x, double y) {  
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y  
                + 0.2 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;  
        return ret;  
    }  

    public static double transformLon(double x, double y) {  
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1  
                * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0  
                * pi)) * 2.0 / 3.0;  
        return ret;  
    }  
    public static double[] transform(double lat, double lon) {  
        if (outOfChina(lat, lon)) {  
            return new double[]{lat,lon};  
        }  
        double dLat = transformLat(lon - 105.0, lat - 35.0);  
        double dLon = transformLon(lon - 105.0, lat - 35.0);  
        double radLat = lat / 180.0 * pi;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
        double mgLat = lat + dLat;  
        double mgLon = lon + dLon;  
        return new double[]{mgLat,mgLon};  
    }  
    public static boolean outOfChina(double lat, double lon) {  
        if (lon < 72.004 || lon > 137.8347)  
            return true;  
        if (lat < 0.8293 || lat > 55.8271)  
            return true;  
        return false;  
    }  
    /** 
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System 
     * 
     * @param lat 
     * @param lon 
     * @return 
     */  
    public static double[] gps84_To_Gcj02(double lat, double lon) {  
        if (outOfChina(lat, lon)) {  
            return new double[]{lat,lon};  
        }  
        double dLat = transformLat(lon - 105.0, lat - 35.0);  
        double dLon = transformLon(lon - 105.0, lat - 35.0);  
        double radLat = lat / 180.0 * pi;  
        double magic = Math.sin(radLat);  
        magic = 1 - ee * magic * magic;  
        double sqrtMagic = Math.sqrt(magic);  
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);  
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);  
        double mgLat = lat + dLat;  
        double mgLon = lon + dLon;  
        return new double[]{mgLat, mgLon};  
    }  

    /** 
     * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return 
     * */  
    public static double[] gcj02_To_Gps84(double lat, double lon) {  
        double[] gps = transform(lat, lon);  
        double lontitude = lon * 2 - gps[1];  
        double latitude = lat * 2 - gps[0];  
        return new double[]{latitude, lontitude};  
    }  
    /** 
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标 
     * 
     * @param lat 
     * @param lon 
     */  
    public static double[] gcj02_To_Bd09(double lat, double lon) {  
        double x = lon, y = lat;  
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);  
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);  
        double tempLon = z * Math.cos(theta) + 0.0065;  
        double tempLat = z * Math.sin(theta) + 0.006;  
        double[] gps = {tempLat,tempLon};  
        return gps;  
    }  

    /** 
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param 
     * bd_lat * @param bd_lon * @return 
     */  
    public static double[] bd09_To_Gcj02(double lat, double lon) {  
        double x = lon - 0.0065, y = lat - 0.006;  
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);  
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);  
        double tempLon = z * Math.cos(theta);  
        double tempLat = z * Math.sin(theta);  
        double[] gps = {tempLat,tempLon};  
        return gps;  
    }  

    /**将gps84转为bd09 
     * @param lat 
     * @param lon 
     * @return 
     */  
    public static double[] gps84_To_bd09(double lat,double lon){  
        double[] gcj02 = gps84_To_Gcj02(lat,lon);  
        double[] bd09 = gcj02_To_Bd09(gcj02[0],gcj02[1]);  
        return bd09;  
    }  
    public static double[] bd09_To_gps84(double lat,double lon){  
        double[] gcj02 = bd09_To_Gcj02(lat, lon);  
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);  
        //保留小数点后六位  
        gps84[0] = retain6(gps84[0]);  
        gps84[1] = retain6(gps84[1]);  
        return gps84;  
    }  

    /**保留小数点后六位 
     * @param num 
     * @return 
     */  
    private static double retain6(double num){  
        String result = String .format("%.6f", num);  
        return Double.valueOf(result);  
    }
    
    public static void main(String[] args) {
    	Double[] ds = {113.8591168939236,22.79568808641853,113.86159990250822,22.797454160121397,113.86126466760805,22.79861898907737,113.8612785244003,22.79863563960813,113.86125636167492,22.798735709988264,113.86085454077275,22.798627978385614,113.86173578423463,22.798923906556883,113.86157509964877,22.799596619396883,113.86452089311271,22.7987084161938,113.86452089311271,22.7987084161938,113.86582329662986,22.796244491459447,113.86545775635193,22.800763289972302,113.86567119539748,22.801363184727368,113.86573216719458,22.80136309424283,113.86553541383024,22.80165793526238,113.86527490159199,22.801683332645748,113.86612020285807,22.801846025063416,113.86604538146457,22.801976737206942,113.86594562132362,22.802174176657285,113.86594563327147,22.802360353753052,113.86469018837339,22.802476158070828,113.86340425449335,22.801891799301817,113.8625729100109,22.802396043202265,113.8616916279058,22.801763867746484,113.8619106170066,22.802791664886584,113.8616750774677,22.802955983231808,113.86118736611752,22.803140154316754,113.85888164614819,22.80016504329332,113.86529169738377,22.804289788545326,113.87161624167832,22.79842878945452,113.87358988363056,22.798140020026793};
    	//gcj02_To_Gps84()
    	for(int i=0;i<ds.length;i+=2) {
    		System.out.println(
    				Arrays.toString(
    						gcj02_To_Gps84(ds[i+1], ds[i])));
    	}
		
	}
}
