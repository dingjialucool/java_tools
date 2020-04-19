package com.chinobot.aiuas.bot_collect.info.util.kml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.chinobot.aiuas.bot_collect.info.entity.vo.CollectPolygon;
import com.chinobot.common.utils.CommonUtils;
import com.chinobot.common.utils.GPSUtil;

/**
 * 
 * @ClassName: PackageKml   
 * @Description: java生成kml文件  
 * @author: djl  
 * @date:2020年1月13日 上午10:55:25
 */
public class PackageKmlUtil {

	public static void packKml(List<CollectPolygon> infoList,String name,String temPath) throws IOException {
		
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("kml", "http://www.opengis.net/kml/2.2");
        //给根节点kml添加属性  
        root.addAttribute("xmlns:gx", "http://www.google.com/kml/ext/2.2")
        .addAttribute("xmlns:kml", "http://www.opengis.net/kml/2.2")
        .addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");  
        document.setRootElement(root);
        
        //给根节点kml添加子节点  Document
        Element documentElement = root.addElement("Document");  
        //添加name节点 
        documentElement.addElement("name").addText(name+".kml"); 
        //Style节点
        Element styleElement = documentElement.addElement("Style");
        styleElement.addAttribute("id", "PolyStyle00051");
        // LabelStyle  
        Element labelStyleElement1 = styleElement.addElement("LabelStyle");
        labelStyleElement1.addElement("color").addText("00000000");
        labelStyleElement1.addElement("scale").addText("0");
        // LabelStyle  
        Element labelStyleElement2 = styleElement.addElement("LabelStyle");
        labelStyleElement2.addElement("color").addText("7f000000");
        labelStyleElement2.addElement("scale").addText("0.4");
        // PolyStyle
        Element polyStyleElement = styleElement.addElement("PolyStyle");
        polyStyleElement.addElement("color").addText("7fffbee8");
        
        for (CollectPolygon collectPolygon : infoList) {
        	
        	Element placeMarkElement = documentElement.addElement("Placemark");
            placeMarkElement.addAttribute("id", collectPolygon.getInfoId());
            placeMarkElement.addElement("name").addText(collectPolygon.getInfoName()); 
            placeMarkElement.addElement("Snippet").addAttribute("maxLines", "0"); 
            placeMarkElement.addElement("description").addText("");
            placeMarkElement.addElement("styleUrl").addText("#PolyStyle00051");
            Element multiGeometryElement = placeMarkElement.addElement("MultiGeometry");
            Element  polygonElement= multiGeometryElement.addElement("Polygon");
            Element outerBoundElement = polygonElement.addElement("outerBoundaryIs");
            Element linearElement = outerBoundElement.addElement("LinearRing");
            //转换坐标
            String transformLnglats = transformLnglats(collectPolygon.getLnglats());
            //封装成polygon
            String polygon = getPolygon(transformLnglats);
            
            linearElement.addElement("coordinates").addText(polygon);
		}
        
       

       //将kml写出本地
        OutputFormat format = OutputFormat.createPrettyPrint();  
        format.setEncoding("utf-8");//设置编码格式  
        XMLWriter xmlWriter;
		try {
			xmlWriter = new XMLWriter(new FileOutputStream(temPath),format);
			xmlWriter.write(document); 
	        xmlWriter.close();  
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}  

        
        System.out.println("success");
	}
	
	 /**
	 * 坐标转换(火星坐标系 (GCJ-02) to 84)
	 * @param lnglats
	 * @param lnglatSystem
	 * @return
	 */
	private static String transformLnglats(String lnglats) {
		String[] rings = lnglats.split(";");
		StringBuilder resultRings = new StringBuilder();
		for (String str : rings) {
			String[] split = str.split(",");
			double[] gps84_To_Gcj02 = GPSUtil.gcj02_To_Gps84(Double.parseDouble(split[1]), Double.parseDouble(split[0]));
			resultRings.append(gps84_To_Gcj02[1] + "," + gps84_To_Gcj02[0] +";");
		}
		String ls = resultRings.toString();
		ls = ls.substring(0, ls.length()-1);
		return ls;
	}
	
	private static String getPolygon(String lngLats) {
		
		if(CommonUtils.isEmpty(lngLats)) {
			return "";
		}
		StringBuilder polygon = new StringBuilder();
    	String[] split = lngLats.split(";");
    	for (int i = 0; i < split.length; i++) {
    		polygon = polygon.append(split[i]+",0 ");
		}
		
		return polygon.toString();
	}
	
	public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
}
