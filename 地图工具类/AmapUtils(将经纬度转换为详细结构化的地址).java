package com.chinobot.common.utils;

/**
 * 高德
 * @author shizt
 * @date 2019年5月16日
 */
public class AmapUtils {
	
	static final String AMAP_KEY = "902b88679a041f46b5e53273f73ca0b3";
	// 逆地理编码
    static final String AMAP_GEOCODE_REGEO_URL = "https://restapi.amap.com/v3/geocode/regeo";
    
    /**
     * 将经纬度转换为详细结构化的地址
     * @param location
     * @return
     * @author shizt  
     * @date 2019年5月16日
     * @company chinobot
     */
    public static String getAddressByGeocode(String location) {
    	String params = "key=" + AMAP_KEY
					  + "&location=" + location
					  + "&radius=100";
    	String result = HttpUtils.sendGet(AMAP_GEOCODE_REGEO_URL, params);
    	System.out.println("geocode_regeo_result: " + result);
    	
    	return result;
    }
    
//    public static void main(String[] args) {
//    	getAddressByGeocode("113.954371,22.701608");
//    	String location = "116.171696,40.148644";
//    	
//    	String addressJson = AmapUtils.getAddressByGeocode(location);
//    	JSONObject addressObject = JSON.parseObject(addressJson);
//    	System.out.println(addressObject.toJSONString());
//    	JSONObject addressComponent = addressObject.getJSONObject("regeocode")
//   			 									   .getJSONObject("addressComponent");
//    	
//    	AddressBase addressBase = new AddressBase();
//    	addressBase.setAtype("cle_early_warning_record");
//    	addressBase.setCenter(location);
//    	addressBase.setAddress(addressObject.getJSONObject("regeocode").getString("formatted_address"));
//    	addressBase.setProvince(addressComponent.getString("province"));
//    	addressBase.setCity(addressComponent.getString("city"));
//    	addressBase.setDistrict(addressComponent.getString("district"));
//    	addressBase.setStreet(addressComponent.getString("township"));
//    	
//    	System.out.println(JSON.toJSONString(addressBase));
//	}
}
