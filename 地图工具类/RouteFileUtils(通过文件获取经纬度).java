package com.chinobot.common.utils;

import com.chinobot.aiuas.bot_prospect.flight.entity.MMCPoint;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * @program: chinobot
 * @description:
 * @author: shizt
 * @create: 2020-03-19 14:02
 */
public class RouteFileUtils {

    /**
     * 获取航线经纬（科比特MMC）
     * @Author: shizt
     * @Date: 2020/3/2 11:18
     */
    public static String getLnglatsByMMC(File file) throws Exception {
        String str = FileUtils.readFileToString(file);
        String routeLnglats = "";
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONArray items = jsonObject.getJSONArray("items");

        Iterator iterator = items.iterator();
        while(iterator.hasNext()){
            JSONObject next = (JSONObject)iterator.next();
            int command = next.getInt("command");
            if(command == 21 || command == 16){
                JSONArray coordinate = next.getJSONArray("coordinate");
                double[] doublesLnglat = GPSUtil.gps84_To_Gcj02(Double.parseDouble(coordinate.getString(0)), Double.parseDouble(coordinate.getString(1)));
                routeLnglats += doublesLnglat[1] + "," + doublesLnglat[0] + ";";
            }
        }

        return routeLnglats;
    }

    /**
     * 获取航线经纬（智航SMD）
     * @Author: shizt
     * @Date: 2020/3/2 11:18
     */
    public static String getLnglatsBySMD(File file) throws Exception {
        String routeLnglats = "";
        Scanner s = new Scanner(file);
        // 不解析头两行
        s.nextLine();
        s.nextLine();

        int count = 1;
        String lng;
        String lat;
        while(s.hasNext()){
            s.next();
            count++;
            if(count == 9){
                lat = s.next();
                lng = s.next();
                routeLnglats += lng + "," + lat + ";";
                s.nextLine();
                count = 1;
            }
        }

        return routeLnglats;
    }

    public static String getLnglatsByDJI(File file) throws IOException {
        String str = FileUtils.readFileToString(file);
        String routeLnglats = "";
        JSONObject jsonObject = JSONObject.fromObject(str);
        // 1-区域扫描，2-带状航线 3-定点航线
        int mode = jsonObject.getInt("mode");
        switch (mode){
            case 1:
                routeLnglats = jsonObject.getString("path");
                break;
            case 2:
                routeLnglats = jsonObject.getString("path");
                break;
            case 3:
                JSONArray pathArr = jsonObject.getJSONArray("path");
                StringBuffer sb = new StringBuffer();
                pathArr.stream().forEach(obj -> {
                    JSONObject jsonObj = (JSONObject) obj;
                    sb.append(jsonObj.getString("lng"))
                            .append(",")
                            .append(jsonObj.getString("lat"))
                            .append(";");
                });
                routeLnglats = sb.toString();
                break;
        }

        return routeLnglats;
    }

    /**
     * 创建MMC航线
     * @Author: shizt
     * @Date: 2020/3/19 11:08
     */
    public static JSONObject createMMCRoute(List<double[]> latlngs, Float flyHeight, Float flySpeed) throws IOException {
        MMCPoint speedPoind  = new MMCPoint();
        // 加速点
        speedPoind.setCommand(178)
                .setCoordinate(new Float[]{0f, 0f, 0f})
                .setParam2(flySpeed);

        // 添加航点
        List<MMCPoint> mmcPoints = new ArrayList<>();
        for(int i = 0, size = latlngs.size(); i < size; i++){
            mmcPoints.add(new MMCPoint()
                    .setCommand(i == 0? 22: 16)
                    .setId(i + 1)
                    .setCoordinate(new Object[]{latlngs.get(i)[0], latlngs.get(i)[1], flyHeight}));
            mmcPoints.add(speedPoind);
        }

        // 航线文件模板
        Resource resource = new ClassPathResource("templates/mmcTemplate.json");
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String data = null;
        StringBuffer sb = new StringBuffer();
        while((data = br.readLine()) != null) {
            sb.append(data);
        }
        br.close();
        isr.close();
        is.close();

        JSONObject mmcjsonObject = JSONObject.fromObject(sb.toString());
        // 组装返回点
        mmcjsonObject.getJSONObject("plannedHomePosition")
                .put("coordinate", new Object[]{latlngs.get(0)[0], latlngs.get(0)[1], 1f});
        // 组装航点
        mmcjsonObject.getJSONArray("items").addAll(mmcPoints);

        return mmcjsonObject;
    }

}
