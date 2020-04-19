package com.chinobot.common.utils;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class PointUtil {
	/**
	 * 
	 * @param lng 经度
	 * @param lat 纬度
	 * @return
	 */
	public static Point2D.Double toPoint(Double lng, Double lat){
		
		return new Point2D.Double(lng, lat);
	}
	/**
	 * 将边界坐标字符串转换为Point2D.Double集合
	 * @param line
	 * @return
	 */
	public static List<Point2D.Double> toPointList(String line){
		List<Point2D.Double> list = new ArrayList<Point2D.Double>();
		if(CommonUtils.isNotEmpty(line)) {
			String[] pointsStr = line.split(";");
			for(String pointStr : pointsStr) {
				if(CommonUtils.isNotEmpty(pointStr)) {
					String[] lnglat = pointStr.split(",");
					Point2D.Double point = toPoint(Double.valueOf(lnglat[0]),Double.valueOf(lnglat[1]));
					list.add(point);
				}
			}
			
		}
		return list;
	}
	/**
	 * 将文件数据转为Point2D.Double集合
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<Point2D.Double> fileToPointList(String path) throws IOException{
		ClassPathResource resource = new ClassPathResource(path);
		InputStream inputStream = resource.getInputStream();
        String jsonString = IOUtils.toString(inputStream);
        List<Point2D.Double> list = new ArrayList<Point2D.Double>();
        if(CommonUtils.isNotEmpty(jsonString)) {
        	String[] split = jsonString.split(",");
        	for(int i=0;i<split.length;i+=2) {
        		Point2D.Double point = toPoint(Double.valueOf(split[i]),Double.valueOf(split[i+1]));
				list.add(point);
        	}
        }
        return list;
	}
	public static List<List<Point2D.Double>> fileToPointListList(String path) throws IOException{
		ClassPathResource resource = new ClassPathResource(path);
		InputStream inputStream = resource.getInputStream();
        String jsonString = IOUtils.toString(inputStream);
        List<List<Point2D.Double>> res = new ArrayList<List<Point2D.Double>>();
        if(CommonUtils.isNotEmpty(jsonString)) {
        	String[] splitJson = jsonString.split("#");
        	for(String s : splitJson) {
        		List<Point2D.Double> list = new ArrayList<Point2D.Double>();
            	String[] split = s.split(",");
            	for(int i=0;i<split.length;i+=2) {
            		Point2D.Double point = toPoint(Double.valueOf(split[i]),Double.valueOf(split[i+1]));
    				list.add(point);
            	}
            	res.add(list);
        	}
        	
        }
        return res;
	}
	/**
	 * 判断点是否在多边形内
	 * @param point 检测点
	 * @param pts   多边形的顶点
	 * @return      点在多边形内返回true,否则返回false
	 */
	public static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts){
	    
	    int N = pts.size();
	    boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
	    int intersectCount = 0;//cross points count of x 
	    double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
	    Point2D.Double p1, p2;//neighbour bound vertices
	    Point2D.Double p = point; //当前点
	    
	    p1 = pts.get(0);//left vertex        
	    for(int i = 1; i <= N; ++i){//check all rays            
	        if(p.equals(p1)){
	            return boundOrVertex;//p is an vertex
	        }
	        
	        p2 = pts.get(i % N);//right vertex            
	        if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests                
	            p1 = p2; 
	            continue;//next ray left point
	        }
	        
	        if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)
	            if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray                    
	                if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray
	                    return boundOrVertex;
	                }
	                
	                if(p1.y == p2.y){//ray is vertical                        
	                    if(p1.y == p.y){//overlies on a vertical ray
	                        return boundOrVertex;
	                    }else{//before ray
	                        ++intersectCount;
	                    } 
	                }else{//cross point on the left side                        
	                    double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y                        
	                    if(Math.abs(p.y - xinters) < precision){//overlies on a ray
	                        return boundOrVertex;
	                    }
	                    
	                    if(p.y < xinters){//before ray
	                        ++intersectCount;
	                    } 
	                }
	            }
	        }else{//special case when ray is crossing through the vertex                
	            if(p.x == p2.x && p.y <= p2.y){//p crossing over p2                    
	                Point2D.Double p3 = pts.get((i+1) % N); //next vertex                    
	                if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
	                    ++intersectCount;
	                }else{
	                    intersectCount += 2;
	                }
	            }
	        }            
	        p1 = p2;//next ray left point
	    }
	    
	    if(intersectCount % 2 == 0){//偶数在多边形外
	        return false;
	    } else { //奇数在多边形内
	        return true;
	    }
	    
	}
}
