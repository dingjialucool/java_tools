package com.chinobot.common.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @ClassName: GaodeUtils   
 * @Description: 高德计算工具类  
 * @author: Lenovo  
 * @date:2020年4月9日 下午2:28:26
 */
public class GaodeUtils {
	
	public static class Poi{
        private Double lat;
        private Double lng;
        public Poi(Double lng,Double lat){
            this.lat = lat;
            this.lng = lng;
        }
 
    }
 
    /**
     * 计算经纬度围成的实际面积（平方公里）
     * @return
     */
    public static BigDecimal getArea(List<Poi> ring){
        double sJ = 6378137;
        double Hq = 0.017453292519943295;
        double c = sJ *Hq;
        double d = 0;
 
        if (3 > ring.size()) {
            return new BigDecimal( 0);
        }
 
        for (int i = 0; i < ring.size() - 1; i ++){
            Poi h = ring.get(i);
            Poi k = ring.get(i + 1);
            double u = h.lng * c * Math.cos(h.lat * Hq);
            double hhh = h.lat * c;
            double v = k.lng * c * Math.cos(k.lat *Hq);
            d = d + (u * k.lat * c - v * hhh);
        }
        Poi g1 = ring.get(ring.size()-1);
        Poi point = ring.get(0);
        double eee = g1.lng * c * Math.cos(g1.lat * Hq);
        double g2 = g1.lat * c;
        double k = point.lng * c * Math.cos(point.lat * Hq);
        d += eee * point.lat * c - k * g2;
        return new BigDecimal( 0.5*Math.abs(d)).divide(new BigDecimal(1000000));
    }


}
