package com.chinobot.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;



/**
 *
 * 常用字符串,日期,金额,类型转换,Cookie等操作工具类
 *
 * @author 曾建
 *
 */
public class CommonUtils {
    private static final Logger log = Logger.getLogger(CommonUtils.class);
    private CommonUtils() {

    }

    /**
     * 判断字符串是否为空 为空:true 不为空:false;
     *
     * @param targetvalue
     *            需要判断的值
     * @return
     */
    public static boolean isEmpty(String targetvalue) {
        if (StringUtils.isEmpty(targetvalue)) {
            return true;
        }
        return StringUtils.isEmpty(targetvalue.trim());
    }

    /**
     * 判断字符串是否不为空 不为空:true 为空:false;
     *
     * @param targetvalue
     *            需要判断的值
     * @return
     */
    public static boolean isNotEmpty(String targetvalue) {
        return !isEmpty(targetvalue);
    }

    /**
     * 去空
     *
     * @param obj
     * @return
     */
    public static String hanldNull(Object obj) {
        if (null == obj) {
            return "";
        }
        if ("null".equals(obj.toString())) {
            return "";
        }
        return obj.toString().trim();
    }
    /**
     * <p>
     * 方法描述: 处理字符串长度
     * </p>
     * @param str
     * @param length
     * @return
     * @author laihb
     */
    public static String handString(String str, int length){
    	if(isEmpty(str)){
    		return str;
    	}
    	if(str.length()>length && length >0){
    		return str.substring(0, length);
    	}
    	return str;
    }
    /**
     * <p>
     * 方法描述：时间戳转为字符串
     * </p>
     * @param timestamp
     * @return
     * @author laihb
     */
    public static String handTimestamp(Timestamp timestamp) {
		if (null == timestamp || "null".equals(timestamp.toString())) {
			return "";
		}
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.format(timestamp);
		} catch (Exception e) {

		}
		return "";
	}
    /**
	 * Long类型NULL值变0
	 *
	 * @return
	 */
    public static Long LongNull(Long val) {
        if (null == val) {
            return 0L;
        }
        if ("null".equals(val.toString())) {
            return 0L;
        }
        return val;
    }

    /**
     *
     * <p>
     * 判断Object类型的值是否不为空(不为空返回true)
     * </p>
     * @param obj
     * @return
     */
    public static boolean objNotEmpty(Object obj){
        if (null != obj && !"".equals(hanldNull(obj))) {
            return true;
        }
        return false;
    }

    /**
     *
     * <p>
     * 判断Object类型的值是否为空(为空返回true)
     * </p>
     * @param obj
     * @return
     */
    public static boolean isObjEmpty(Object obj){
        return !objNotEmpty(obj);
    }

    /**
     *
     * <p>
     * 判断字符串数组是否为空,不为空true,为空false
     * </p>
     * @param strs
     * @return
     */
    public static boolean arrayisNotEmpty(String[] strs) {
        if (strs != null && strs.length > 0 && !strs[0].equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串数组是否包含一个元素,包含为true,不包含为false
     * @param arr 一个String数组
     * @param str 一个字符串元素
     * @return
     */
    public static boolean arrayContain(String[] arr,String str){
    	if(arrayisNotEmpty(arr) && isNotEmpty(str)){
    		for (String string : arr) {
				if(str.equals(string)){
					return true;
				}
			}
    	}
    	return false;
    }
    /**
     * 将字符串转换成整型(为空时返回0)
     * @param value 要转换的字符串
     * @return
     * @throws NumberFormatException
     */
    public static Integer getIntValue(String value) throws RuntimeException {
        return isEmpty(value) ? 0 : Integer.valueOf(hanldNull(value));
    }


    /**
     * 方法描述: BigDecimal 转换为int
     * @param value
     * @return
     * @throws RuntimeException
     * @author zhangzw
     */
    public static int  getBigDecimalToIntValue(Object value ) throws RuntimeException{
    	 BigDecimal ret = null;
    	  if( value != null ) {
              if( value instanceof BigDecimal ) {
                  ret = (BigDecimal) value;
              }
    	  }
    	if(objNotEmpty(ret)){
    		BigDecimal decimal = new BigDecimal(hanldNull(ret));
    		return decimal.intValue();
    	}
    	return 0;
    }


    /**
     * 将字符串转换成长整型(为空时返回-1)
     * @param value 要转换的字符串
     * @return
     * @throws NumberFormatException
     */
    public static Long getLongValue(String value) throws RuntimeException {
        return isEmpty(value) ? -1 : Long.parseLong(hanldNull(value));
    }

    /**
     * 将字符串数组转换成整型数组(为空时返回null)
     * @param values 字符串数组
     * @return
     */
    public static Integer[] getIntArrayValue(String[] values) {
        if (values != null && values.length > 0) {
        	Integer[] intvalues = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                intvalues[i] = Integer.valueOf(hanldNull(values[i]));
            }
            return intvalues;
        }
        return null;
    }

    /**
     * 将字符串数组转换成长整型数组(为空时返回null)
     * @param values 字符串数组
     * @return
     */
    public static Long[] getLongArrayValue(String[] values) {
        if (values != null && values.length > 0) {
            Long[] intvalues = new Long[values.length];
            for (int i = 0; i < values.length; i++) {
                intvalues[i] = Long.parseLong(hanldNull(values[i]));
            }
            return intvalues;
        }
        return null;
    }

    /**
     * 获取字符输出流(并在里面设置了响应类型与字符编码gbk)
     * @param response
     * @return
     * @throws IOException
     */
    public static PrintWriter getWriter(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=gbk");
        response.setCharacterEncoding("gbk");
        return response.getWriter();
    }

    /**
     * 获取字符输出流(并在里面设置了响应类型与字符编码utf-8)
     * @param response
     * @return
     * @throws IOException
     */
    public static PrintWriter getWriterUTF(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        return response.getWriter();
    }

    /**
     * 获取字符输出流(通过传入字符编码设置了响应类型与字符编码)
     * @param response
     * @return
     * @throws IOException
     */
    public static PrintWriter getWriter(HttpServletResponse response, String encoding)
            throws IOException {
        if (isEmpty(encoding)) {
            encoding = "gbk";
        }
        response.setContentType("text/html;charset=" + encoding + "");
        response.setCharacterEncoding(encoding);
        return response.getWriter();
    }

    /**
     *
     * <p>
     * 关闭文本输出流
     * </p>
     * @param pw
     */
    public static void closeWriter(PrintWriter pw){
        pw.flush();
        pw.close();
    }

    /**
     *
     * <p>
     * 获取去空之后的16进制解码之后的值
     * </p>
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getURLDecoderValue(String value) throws UnsupportedEncodingException{
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        return hanldNull(URLDecoder.decode(value, "utf-8"));
    }

    /**
     * 对值进行16进制解码(如果传入的值为空则返回null,编码为空则采用gbk解码)
     * @param value 需要解码的值
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getDecode(String value, String encoding)
            throws UnsupportedEncodingException {
        if (isEmpty(value)) {
            return "";
        }
        return isEmpty(encoding) ? URLDecoder.decode(value, "utf-8") : URLDecoder.decode(value,encoding);
    }

    /**
     * 用指定的编码方式获取页面上name=encodeURI(encodeURI(value))转码之后的值
     * @param targetcontext
     * @return
     * @throws Exception
     */
    public static String convertdecodeEncoding(String targetcontext, String encoding){
        if(isEmpty(targetcontext)){
            return "";
        }
        try {
            return URLDecoder.decode(targetcontext, encoding);
        } catch (Exception e) {
            log.error("转换编码失败",e);
        }
        return "";
    }

    /**
     * 手动将iso-8859-1编码转换成gbk编码格式的值
     * @return String
     */
    public static String converToGBK(String value) {
    	if(isEmpty(value)){
    		return "";
    	}
    	try {
    		return new String(value.getBytes("iso-8859-1"), "gbk");
    	} catch (Exception e) {
    		log.error("转换编码失败",e);
    	}
    	return "";
    }
    /**
     * 手动将iso-8859-1编码转换成uft-8编码格式的值
     * @return String
     */
    public static String converToUTF(String value) {
        if(isEmpty(value)){
            return "";
        }
        try {
                return new String(value.getBytes("iso-8859-1"), "UTF-8");
        } catch (Exception e) {
            log.error("转换编码失败",e);
        }
        return "";
    }

    /**
     * 手动将iso-8859-1编码转换成指定编码格式的值
     * @param targetcontext
     * @return
     * @throws Exception
     */
    public static String convertBytesEncoding(String targetcontext,String encoding)  {
        if(isEmpty(targetcontext)){
            return "";
        }
        try {
            return new String(targetcontext.getBytes("iso-8859-1"), encoding);
        } catch (Exception e) {
            log.error("转换编码失败",e);
        }
        return "";
    }

    /**
     * 获取服务器年份 格式为:2012
     *
     * @return
     */
    public static String getDateyyyy() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**
     * 获取服务器年份 格式为:01
     *
     * @return
     */
    public static String getDateMM() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(date);
    }

    /**
     * 获取服务器年份 格式为:01
     *
     * @return
     */
    public static String getDatedd() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }
    
    /**
     * 方法描述: 获取服务器当前时间 格式为:201101
     * @return
     * @author yaoxf
     */
    public static String getDateyyyyMM() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(date);
    }

    /**
     * 获取服务器当前时间 格式为:2011-01-01
     *
     * @return
     */
    public static String getDateyyyyMMdd() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取服务器当前时间 格式为:110101
     *
     * @return
     */
    public static String getNowDateyyMMdd() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        return sdf.format(date);
    }

    /**
     * 获取服务器当前时间 格式为:11-01-01
     *
     * @return
     */
    public static String getDateyyMMdd() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 方法描述: 获取服务器当前时间 格式为:2011-01-01 18:20:20
     * @return
     * @author yaoxf
     */
    public static String getDateyyyyMMddHHmmss() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将20110101格式字符串的转行成2011-01-01字符串
     * @param datestr
     * @return
     */
    public static String convertDate(String datestr) {
        if (isEmpty(datestr)) {
            return null;
        }
        if (datestr.length() != 8) {
            return datestr;
        }
        return datestr.substring(0, 4) + "-" + datestr.substring(4, 6) + "-"+ datestr.substring(6, 8);
    }

    /**
     *
     * <p>
     * 返回服务器日期
     * </p>
     * @return
     */
    public static Date getDateValue() {
        return new Date();
    }

    /**
    *
    * <p>
    * 返回服务器日期LocalDateTime
    * </p>
    * @return
    */
   public static LocalDateTime getLocalDateTimeValue() {
	   
       return LocalDateTime.now();
   }
    
    /**
     * 将日期格式的字符串转换为java.util.Date类型的时间(不带时分秒)
     *
     * @param datestr
     *            2011-01-01
     * @return
     * @throws ParseException
     */
    public static Date getStringToDate(String datestr) {
        if (isEmpty(datestr)) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(hanldNull(datestr));
        } catch (Exception e) {
            throw new RuntimeException("字符串转换成日期格式发生错误,请检查字符串格式是否正确", e);
        }
    }
    /**
     * 获取服务器当前时间 格式为:自定义
     * @param formatStr 自定义返回格式
     * @return
     */
    public static String getDateByFormat(String formatStr){
    	if (isEmpty(formatStr)) {
            return "";
        }
    	SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
    	Date date = new Date();
    	return sdf.format(date);
    }

    /**
	 * 获取本机ip地址
	 * @return
	 */
	public static String getLocalhostIp(){
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

    /**
     * 将日期格式的字符串转换为java.util.Date类型的时间(带时分秒)
     *
     * @param datestr
     *            2011-01-01 18:00:00
     * @return
     * @throws ParseException
     */
    public static Date getStringToDateDetails(String datestr) throws RuntimeException {
        if (isEmpty(datestr)) {
            return null;
        }
        try {
            if (datestr.length() <= 10) {
                datestr = datestr + " 00:00:00";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(datestr);
        } catch (Exception e) {
            throw new RuntimeException("字符串转换成日期格式发生错误,请检查字符串格式是否正确:" + datestr);
        }
    }

    /**
     * 将日期格式的字符串转换为java.util.Date类型的时间(带时分秒),如果String没有带时分秒自动设置成功23:59:59
     *
     * @param datestr
     *            2011-01-01 18:00:00
     * @return
     * @throws ParseException
     */
    public static Date getStringToDateTime(String datestr) throws RuntimeException {
        if (isEmpty(datestr)) {
            return null;
        }
        try {
            if (datestr.length() <= 10) {
                datestr = datestr + " 23:59:59";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(datestr);
        } catch (Exception e) {
            throw new RuntimeException("字符串转换成日期格式发生错误,请检查字符串格式是否正确:" + datestr);
        }
    }

    /**
     * 将Thu Nov 02 00:00:00 CST 2006类型的日期值格式化为2006-11-02
     * @param obj
     * @return
     * @throws ParseException
     */
    public static String formatObjecttoDate(Object obj) throws ParseException {
    	if(!objNotEmpty(obj)){
    		return null;
    	}
        SimpleDateFormat usdateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
                Locale.US);
        Date date = usdateFormat.parse(hanldNull(obj));
        SimpleDateFormat zhdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return zhdateFormat.format(date);
    }

    /**
     * 将Thu Nov 02 00:00:00 CST 2006类型的日期值格式化为2006-11-02 00:00:00
     * @param obj
     * @return
     * @throws ParseException
     */
    public static String formatObjecttoDateDetails(Object obj) throws ParseException {
        SimpleDateFormat usdateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
                Locale.US);
        Date date = usdateFormat.parse(hanldNull(obj));
        SimpleDateFormat zhdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return zhdateFormat.format(date);
    }

    /**
     * 将java.util.Date类型的日期值格式化为2011-01-01类型的字符串
     * @param date java.util.Date
     * @return
     */
    public static String dateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 将java.util.Date类型的日期值格式化为2011-01-01 00:00:00类型的字符串
     * @param date java.util.Date
     * @return
     */
    public static String dateDetailsFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    
    /**
     * 	计算时间
     * @param field
     * @param amount
     * @return
     */
    public static Date calDate(int field, int amount) {
    	Calendar cal = Calendar.getInstance();
    	cal.add(field, amount);
    	return cal.getTime();
    }
    
    /**
     * 比较两个日期(格式为yyyyMMddHHmmss)
     *
     * @param onedateStr
     *            日期+时间字符串
     * @param anotherdateStr
     *            日期+时间字符串
     * @return 前者大于后者 为:1 ; 前者等于后者 为:0 ; 前者小于后者 为:-1
     * @throws Exception
     */
    public static int compareDateYMDHMS(String onedateStr, String anotherdateStr){
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
            Date oneDate = formater.parse(onedateStr);
            Date anotherDate = formater.parse(anotherdateStr);
            return oneDate.compareTo(anotherDate);
        } catch (Exception e) {
            throw new RuntimeException("日期的格式(" + onedateStr + "," + anotherdateStr + ")不正确", e);
        }
    }

    /**
     * 比较两个日期(格式为yyyy-MM-dd)
     *
     * @param onedateStr
     *            日期字符串
     * @param anotherdateStr
     *            日期字符串
     * @return 前者大于后者 为:1 ; 前者等于后者 为:0 ; 前者小于后者 为:-1
     * @throws Exception
     */
    public static int compareDate(String onedateStr, String anotherdateStr){
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            Date oneDate = formater.parse(onedateStr);
            Date anotherDate = formater.parse(anotherdateStr);
            return oneDate.compareTo(anotherDate);
        } catch (Exception e) {
            throw new RuntimeException("日期的格式(" + onedateStr + "," + anotherdateStr + ")不正确", e);
        }
    }



    /**
     * 比较两个日期(格式为yyyy-MM-dd)
     *
     * @param onedateStr
     *            日期字符串
     * @param anotherdateStr
     *            日期字符串
     * @return 前者大于后者 为:1 ; 前者等于后者 为:0 ; 前者小于后者 为:-1
     * @throws Exception
     */
    public static int compareDateTime(String onedateStr, String anotherdateStr){
        try {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date oneDate = formater.parse(onedateStr);
            Date anotherDate = formater.parse(anotherdateStr);
            return oneDate.compareTo(anotherDate);
        } catch (Exception e) {
            throw new RuntimeException("日期的格式(" + onedateStr + "," + anotherdateStr + ")不正确", e);
        }
    }

    /**
     * 计算出两个时间之间的时间差(毫秒数)
     *
     * @param onedateTimeStr
     *            2011-12-31
     * @param anotherdateTimeStr
     *            2011-01-01
     * @return
     * @throws ParseException
     */
    public static long getDifferMiniSecond(String onedateTimeStr, String anotherdateTimeStr)
            throws ParseException {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date oneDateTime = formater.parse(onedateTimeStr);
        Date anotherDateTime = formater.parse(anotherdateTimeStr);
        long difference = oneDateTime.getTime() - anotherDateTime.getTime();
        return difference;
    }

    /**
     * 计算出两个时间之间的时间差(毫秒数)
     *
     *            2011-12-31
     *            2011-01-01
     * @return
     * @throws ParseException
     */
    public static long getDifferMiniSecond(Date onedateTime, Date anotherdateTime)
            throws ParseException {
        long difference = onedateTime.getTime() - anotherdateTime.getTime();
        return difference;
    }


    /**
     * 获取两个日期之间相差的小时数,前者减后者
     *
     * @param onedateTimeStr
     *            以yyyy-MM-dd HH:mm:ss开头为格式的日期字符串
     * @param anotherdateTimeStr
     *            以yyyy-MM-dd HH:mm:ss开头为格式的日期字符串
     * @return 前者减后者的小时数
     * @throws Exception
     */
    public static long getDifferHour(String onedateTimeStr, String anotherdateTimeStr)
            throws Exception {
        try {
            return getDifferMiniSecond(onedateTimeStr, anotherdateTimeStr) / 3600000;
        } catch (Exception e) {
            throw new Exception("日期的格式(" + onedateTimeStr + ", " + anotherdateTimeStr + ")的格式不正确");
        }
    }

    /**
     * 获取两个日期之间相差的天数,前者减后者
     *
     * @param onedateTimeStr
     *            以yyyy-MM-dd HH:mm:ss开头为格式的日期字符串
     * @param anotherdateTimeStr
     *            以yyyy-MM-dd HH:mm:ss开头为格式的日期字符串
     * @return 前者减后者的天数
     * @throws Exception
     */
    public static long getDifferDay(String onedateTimeStr, String anotherdateTimeStr)
            throws Exception {
        return getDifferHour(onedateTimeStr, anotherdateTimeStr) / 24;
    }

    /**
     * 将金额转换为中文格式的金额
     *
     * @param digit 最多两位小数,支持最大金额为999999999999.99
     * @return 中文格式的金额
     */
    public static String parseMoneyToUpper(double digit) {
        // 将数据格式化为四位小数
        DecimalFormat df = new DecimalFormat("#.0000");
        StringBuffer sbDigit = new StringBuffer(df.format(digit));
        sbDigit.replace(sbDigit.length() - 2, sbDigit.length(), "00");
        String sDigit = "";// 将double转化为string
        sDigit = sbDigit.toString();
        sDigit = sDigit.substring(0, sDigit.length() - 5) + sDigit.substring(sDigit.length() - 4);// 去除小数点
        if (sDigit.length() > 16) {
            return "款项过大！";
        }
        if (sDigit.length() < 16) {
            int iLength = 16 - sDigit.length();
            for (int i = 0; i < iLength; i++) {
                sDigit = "0" + sDigit;
            }
        }
        if (sDigit.equals("0000000000000000")) {
            return "零元整";
        }
        String sChinese = sDigit;
        String sFour = "";// 每四位构造一个string
        boolean bPreStr = true;// 前一个string是否构造成功
        sDigit = "";// 总字符串
        // 将字符串分为四组，每一组单独处理，都处理完后串接
        for (int i = 0; i < 4; i++) {
            sFour = convertZhCoin(sDigit, sChinese.substring(i * 4, i * 4 + 4), i, bPreStr);
            if (sFour.length() == 0 || sFour.length() == 1) {
                bPreStr = false;
            } else if (sFour.charAt(sFour.length() - 2) < '0'
                    || sFour.charAt(sFour.length() - 2) > '9') {
                bPreStr = false;
            } else {
                bPreStr = true;
            }
            sDigit = sDigit + sFour;
        }
        // 去掉字符串最前面的"0"
        for (;;) {
            if (sDigit.charAt(0) == '0') {
                sDigit = sDigit.substring(1);
            } else {
                break;
            }
        }
        sChinese = "";
        for (int i = 0; i < sDigit.length(); i++) {
            if (sDigit.charAt(i) >= '0' && sDigit.charAt(i) <= '9') {
                switch (sDigit.charAt(i)) {
                case '1': {
                    sChinese = sChinese + "壹";
                    break;
                }
                case '2': {
                    sChinese = sChinese + "贰";
                    break;
                }
                case '3': {
                    sChinese = sChinese + "叁";
                    break;
                }
                case '4': {
                    sChinese = sChinese + "肆";
                    break;
                }
                case '5': {
                    sChinese = sChinese + "伍";
                    break;
                }
                case '6': {
                    sChinese = sChinese + "陆";
                    break;
                }
                case '7': {
                    sChinese = sChinese + "柒";
                    break;
                }
                case '8': {
                    sChinese = sChinese + "捌";
                    break;
                }
                case '9': {
                    sChinese = sChinese + "玖";
                    break;
                }
                case '0': {
                    sChinese = sChinese + "零";
                    break;
                }
                }
            } else {
                sChinese = sChinese + sDigit.charAt(i);
            }
        }
        if (!sDigit.endsWith("分"))// 有"分"不加"整"
        {
            sChinese = sChinese + "整";
        }
        return sChinese;
    }

    /**
     * 数字转换为中文金额
     * @param sStr
     * @param sFour
     * @param i
     * @param bPre
     * @return String
     */
    protected static String convertZhCoin(String sStr, String sFour, int i, boolean bPre) {
        // 回传结果
        String result = "";
        for (int j = 0; j < 4; j++) {
            if (sFour.charAt(j) != '0') {// 处理每一位数值时，在前面是否需要加“零”
                if (j == 0) {// 处理千位
                    if (!bPre) {
                        result = result + '0';
                    }
                    result = result + sFour.charAt(j);
                } else {// 处理百、十、个位
                    if (sFour.charAt(j - 1) == '0') {
                        result = result + '0';
                    }
                    result = result + sFour.charAt(j);
                }
                switch (j) {// 单独处理"角"和"分"
                case 0: {
                    if (i == 3) {
                        result = result + '角';
                    } else {
                        result = result + '仟';
                    }
                    break;
                }
                case 1: {
                    if (i == 3) {
                        result = result + '分';
                    } else {
                        result = result + '佰';
                    }
                    break;
                }
                case 2: {
                    result = result + '拾';
                    break;
                }
                case 3: {
                    if (!result.equals("")) {
                        // 处理单位
                        switch (i) {
                        case 0: {
                            result = result + "亿";
                            break;
                        }
                        case 1: {
                            result = result + "万";
                            break;
                        }
                        case 2: {
                            result = result + "元";
                            break;
                        }
                        }
                    }
                }
                }
            } else {
                // 当个位为零时，处理单位
                if (!result.equals("") && j == 3) {
                    switch (i) {
                    case 0: {
                        result = result + "亿";
                        break;
                    }
                    case 1: {
                        result = result + "万";
                        break;
                    }
                    }
                }
                // 是否加"元"字
                if (i == 2 && j == 3 && (!sStr.equals("") || !result.equals(""))) {
                    result = result + "元";
                }
            }
        }
        return result;
    }

    /**
     * 自1970年1月1日到现在的毫秒数(可用作测试某段代码执行的时间差,或生产唯一不重复的随机数)
     * @return
     */
    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 判断字符串是否有中文,如果有则返回true
     *
     * @param str
     * @return
     */
    public static boolean hasChinese(String str) {
        if(isEmpty(str)){
            return false;
        }
        if (str.getBytes().length != str.length()) {
            return true;
        }
        return false;
    }


    /**
     * 将List集合转换为数组
     * @param list List集合
     * @return Object[]
     */
    @SuppressWarnings("unchecked")
    public static Object[] listtoArray(List list) {
        return (list != null && list.size() > 0) ? list.toArray() : null;
    }

    /**
     * 添加cookie
     * @param response
     * @param name cookie的名称
     * @param value cookie的值
     * @param maxAge cookie存放的时间(以秒为单位,假如存放三天,即3*24*60*60; 如果值为0,cookie将随浏览器关闭而清除)
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0)
            cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 根据名称获取cookie的值
     * @param request
     * @param name cookie的名称
     * @return
     */
    public static String getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = CommonUtils.readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie.getValue();
        } else {
            return null;
        }
    }

    protected static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                cookieMap.put(cookies[i].getName(), cookies[i]);
            }
        }
        return cookieMap;
    }

    /**
     * 去除html代码
     * @param inputString
     * @return
     */
    public static String htmltoText(String inputString) {
        //如果传入进行的值为空则返回空字符串
        if (StringUtils.isBlank(inputString)) {
            return "";
        }
        String htmlStr = inputString; //含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            textStr = StringUtils.trimToEmpty(htmlStr);
        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        return textStr;//返回文本字符串
    }

    /**
     *
     * <p>
     * 验证一个字符串是否为yyyy-MM-dd类型的日期类型;是返回true
     * </p>
     * @param value
     * @return
     */
    public static boolean validDateType(String value){
        if(isEmpty(value)){
            return false;
        }
        try {
            getStringToDate(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * <p>
     * 验证一个字符串是否为yyyy-MM-dd类型的日期类型,并进行日期正确性判断;是返回true(推荐使用)
     * </p>
     * @param datevalue
     * @return
     */
    public static boolean validDateStyle(String datevalue){
        if(isEmpty(datevalue)){
            return false;
        }
        String eL="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(datevalue);
         return m.matches();

    }

    /***
     * 获取URI的路径,如路径为http://www.unisure.com.cn/action/index.jsp?method=add, 得到的值为"/action/index.jsp"
     * @param request
     * @return
     */
    public static String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 获取完整请求路径(含内容路径及请求参数)
     * @param request
     * @return
     */
    public static String getRequestURIWithParam(HttpServletRequest request) {
        return getRequestURI(request)+ (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }

    /**
     * 用于流水号位数不足补0的字符串
     */
    private final static String FILL_ZERO_STR="00000000000000000000000000000";

    /**
     * 当流水号不足length长度时那么在前面填充0
     * @param curId
     * @param length
     * @return
     */
    public static String fillZero(String curId,int length){
        String serialNumber=FILL_ZERO_STR+curId;
        return serialNumber.substring(serialNumber.length()-length);
    }

    /**
     *
     * <p>
     * in Sql的字符串拼接
     * </p>
     * @param strs 数组
     * @param isChar 是否是字符串如果是的或者值前加上“'”单引号
     * @return
     */
    public static String splitJointInSqlStr(Object[] strs,boolean isChar){
        if(strs==null || strs.length==0){
            throw new RuntimeException("需要解析的字符串数组不能为空。");
        }
        StringBuilder strBui=new StringBuilder();
        int arraySize=strs.length;
        for(int i=0;i<arraySize;i++){
            if(isChar){
                strBui.append("'");
            }
            strBui.append(strs[i]);
            if(isChar){
                strBui.append("'");
            }
            if(i!=arraySize-1){
                strBui.append(",");
            }
        }
        return  strBui.toString();
    }
    /**
     *
     * <p>
     * in Sql的字符串拼接 like拼接专用
     * </p>
     * @param strs 数组
     * @param colname 列名
     * @return
     */
    public static String splitJointInSqlStr(String[] strs,String colname){
        if(strs==null || strs.length==0){
            throw new RuntimeException("需要解析的字符串数组不能为空。");
        }
        StringBuilder strBui=new StringBuilder();
        int arraySize=strs.length;
        for(int i=0;i<arraySize;i++){
    		strBui.append(" ")
        		.append(colname)
        		.append(" like '%")
        		.append(strs[i])
        		.append("%' ");

            if(i!=arraySize-1){
                strBui.append("or");
            }
        }

        return  strBui.toString();
    }







    /**
     * <p>
     * 职业资格证书等级
     * </p>
     * <li>职业资格证书等级代码反转 如：1转换成5</li><br>
     * @param sPqslevel
     * @return
     */
    public static String changePqslevel(String sPqslevel){
        int num=5;
        int level=getIntValue(sPqslevel);
        return String.valueOf((num-level+1));
    }
    /**
     * <p>
     * 查找指定数字在数组中的下标位置
     * </p>
     * <li></li><br>
     * @return
     */
    public static int getIndex(long[] arrayList, long stemp){
    	int index=-1;
       for(int i=0;i<arrayList.length;i++){
    	   if(arrayList[i]==stemp){
    		   index=i;
    		   break;
    	   }
       }
       return index;
    }
    /**
     *对传入日期类型做格式转换成String
     * @return
     */
    public static String  getDatetoString(Date date,String sfmt) {
    	if(null==date){
    		//date= new Date();
    		date = new Date();
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat(sfmt);
    	return sdf.format(date);
    }

    /**
	 * 去除html代码
	 * @return
	 */
	public static String getHtmltoText(String content) {
		// 如果传入进行的值为空则返回空字符串
		if (StringUtils.isBlank(content)) {
			return "";
		}
		String textStr = "";
		try {
		//System.err.println("过滤前的参数值："+content);
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		// 正则表达式
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 定义HTML标签的正则表达式与html关键字过滤
			//String regEx_html = "alert|iframe|frame|having|expression|location|href|src|object|load|table|onmousedown|master|restore|rename|select|insert|update|delete|alter|exec|create|drop|truncate|xp_cmdshell|exec master|net|and|or|onmouseover|onmouseup|onmousemove|onmouseout|style[\\s]*?=";//[/<>\"']|
			String regEx_html = "alert|iframe|frame|expression|location|onmousedown|restore|insert|update|delete|alter|drop|truncate|xp_cmdshell|onmouseover|onmouseup|onmousemove|onmouseout|style[\\s]*?=";//[/<>\"']|
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(content);
			content = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(content);
			content = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(content);
			content = m_html.replaceAll(""); // 过滤其它标签
			content = content.replaceAll("<", "&lt;");
			content = content.replaceAll(">", "&gt;");
			//content = content.replaceAll("--", "");
			content = content.replaceAll("'", "’");
			content = content.replaceAll("\"", "“");
			content = content.replaceAll(";", "；");
			// 过滤后的值再去掉前后空格
			textStr = hanldNull(content);
			//System.err.println("过滤后的参数值："+textStr);
		} catch (Exception e) {
		    log.error("过滤字符出错",e);
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 两个javaBean属性复制－－－源对象里的属性为空的不复制
	 * @param o1 源对象
	 * @param o2 目标对象
	 */
	public static void copyProperties(Object o1, Object o2) {
		// 判断两个不对像不能为null
		if(!(objNotEmpty(o1) && objNotEmpty(o2))){
			return;
		}
		String fileName, str, getName, setName;
		List<Field> fields = new ArrayList<Field>();
		List<Field> srcfields = new ArrayList<Field>();
		Method getMethod = null;
		Method setMethod = null;
		try {
			Class c1 = o1.getClass();
			Class c2 = o2.getClass();

			Field[] fs1 = c1.getDeclaredFields();
			Field[] fs2 = c2.getDeclaredFields();
			//两个类属性比较剔除不相同的属性，只留下相同的属性
			for (int i = 0; i < fs2.length; i++) {
				for (int j = 0; j < fs1.length; j++) {
					if (fs1[j].getName().equals(fs2[i].getName()) && !fs1[j].getName().equals("serialVersionUID")) {
						fields.add(fs1[j]);
						srcfields.add(fs2[i]);//++++
						break;
					}
				}
			}
			if (null != fields && fields.size() > 0) {
				for (int i = 0; i < fields.size(); i++) {
					//获取属性名称
					Field f = fields.get(i);
					Field tof = srcfields.get(i);
					fileName = f.getName();
					//属性名第一个字母大写
					str = fileName.substring(0, 1).toUpperCase();
					//拼凑getXXX和setXXX方法名
					getName = "get" + str + fileName.substring(1);
					setName = "set" + str + fileName.substring(1);
					//获取get、set方法
					getMethod = c1.getMethod(getName, new Class[] {});
					if (tof.getType() == Date.class) {
						//System.out.println("-----------------------");
						setMethod = c2.getMethod(setName, new Class[] { tof
								.getType() });
						Object o = getMethod.invoke(o1, new Object[] {});
						//System.out.println(fileName + " : " + o);

						//将属性值放入另一个对象中对应的属性
						if (objNotEmpty(o)) {
//							Date newO = CommonUtils.getStringToDate(o.toString().trim());
							Date newO = null;
							if(o instanceof Date){
								newO = (Date)o;
							}else {
								newO = CommonUtils.getStringToDate(o.toString().trim());
							}
							//System.out.println("o2.setMethod = " + setMethod);
							setMethod.invoke(o2, new Date[] { newO });
						}

					} else {
						setMethod = c2.getMethod(setName, new Class[] { tof
								.getType() });
						//获取属性值
						Object o = getMethod.invoke(o1, new Object[] {});
						//System.out.println(fileName + " : " + o);

						//将属性值放入另一个对象中对应的属性
						if (isNotEmpty(hanldNull(o))) {
							//System.out.println("o2.setMethod = " + setMethod);
							setMethod.invoke(o2, new Object[] { o });
						}
					}


				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 两个javaBean属性复制－－－源对象里的属性为空的不复制为""空字符串可以复制
	 * @param o1 源对象
	 * @param o2 目标对象
	 */
	public static void copyProperties1(Object o1, Object o2) {
		// 判断两个不对像不能为null
		if(!(objNotEmpty(o1) && objNotEmpty(o2))){
			return;
		}
		String fileName, str, getName, setName;
		List<Field> fields = new ArrayList<Field>();
		List<Field> srcfields = new ArrayList<Field>();
		Method getMethod = null;
		Method setMethod = null;
		try {
			Class c1 = o1.getClass();
			Class c2 = o2.getClass();

			Field[] fs1 = c1.getDeclaredFields();
			Field[] fs2 = c2.getDeclaredFields();
			//两个类属性比较剔除不相同的属性，只留下相同的属性
			for (int i = 0; i < fs2.length; i++) {
				for (int j = 0; j < fs1.length; j++) {
					if (fs1[j].getName().equals(fs2[i].getName()) && !fs1[j].getName().equals("serialVersionUID")) {
						fields.add(fs1[j]);
						srcfields.add(fs2[i]);//++++
						break;
					}
				}
			}
			if (null != fields && fields.size() > 0) {
				for (int i = 0; i < fields.size(); i++) {
					//获取属性名称
					Field f = fields.get(i);
					Field srcf = srcfields.get(i);
					fileName = f.getName();
					//属性名第一个字母大写
					str = fileName.substring(0, 1).toUpperCase();
					//拼凑getXXX和setXXX方法名
					getName = "get" + str + fileName.substring(1);
					setName = "set" + str + fileName.substring(1);
					//获取get、set方法
					getMethod = c1.getMethod(getName, new Class[] {});
					if (srcf.getType() == Date.class) {
						//System.out.println("-----------------------");
						setMethod = c2.getMethod(setName, new Class[] { srcf
								.getType() });
						Object o = getMethod.invoke(o1, new Object[] {});
						//System.out.println(fileName + " : " + o);

						//将属性值放入另一个对象中对应的属性
						if (null != o) {
							Date newO = null;
							if(o instanceof Date){
								newO = (Date)o;
							}else {
								newO = CommonUtils.getStringToDate(o.toString().trim());
							}
							//System.out.println("o2.setMethod = " + setMethod);
							setMethod.invoke(o2, new Date[] { newO });
						}

					} else {
						setMethod = c2.getMethod(setName, new Class[] { f
								.getType() });
						//获取属性值
						Object o = getMethod.invoke(o1, new Object[] {});
						//System.out.println(fileName + " : " + o);

						//将属性值放入另一个对象中对应的属性
						if (null != o) {
							//System.out.println("o2.setMethod = " + setMethod);
							setMethod.invoke(o2, new Object[] { o });
						}
					}


				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取上一季度的开始月份和结束月份（注意：返回结束日期是实际结束日期的下一个月1号0时0分，比如当前月份5
	 * 则返回的是 2月1号-5月1号0时0分0秒）
	 * @param startDate
	 * @return
	 */
	public static Date[] getPreQuarterRange(Date startDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		int month = cal.get(Calendar.MONTH)+1;//指定日期的月份

		// 2-4月 则返回 去年11月到本年1月（2月 1日0时）
		if (month >= 2 && month <= 4) {

			return new Date[]{
					newDate(cal.get(Calendar.YEAR)-1, Calendar.NOVEMBER, 1),
					newDate(cal.get(Calendar.YEAR), Calendar.FEBRUARY, 1)
				};
		}

		// 5-7月 则返回 本年2月到本年4月（5月1日0时）
		if (month >= 5 && month <= 7) {

			return new Date[]{
					newDate(cal.get(Calendar.YEAR), Calendar.FEBRUARY, 1),
					newDate(cal.get(Calendar.YEAR), Calendar.MAY, 1)
				};
		}

		// 8-10月 则返回 本年5月到本年7月（8月1日0时）
		if (month >= 8 && month <= 10) {

			return new Date[]{
					newDate(cal.get(Calendar.YEAR), Calendar.MAY, 1),
					newDate(cal.get(Calendar.YEAR), Calendar.AUGUST, 1)
				};
		}

		// 11-12月  	本年 8月到10月 （11月1日0时）
		// 1 月 		则返回 去年8月到 去年10月（11月1日0时）
		if (month == 1 ||  month == 11 || month == 12) {
			int year = cal.get(Calendar.YEAR);
			//如果是本年1月，则上个季度为去年的8-10月
			if (month == 1) year = year-1;
			return new Date[]{
					newDate(year, Calendar.AUGUST, 1),
					newDate(year, Calendar.NOVEMBER, 1)
				};
		}

		return null;
	}

	/**
	 * 构建一个年月日日期，月份从0开始计
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private static Date newDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(year, month, day);
		return c.getTime();
	}


    /**
	 * 将输出的字符串用ISO8859-1编码
	 * @param str
	 * @return
	 */
	public static String formToDecode(String str) {
		if (str == null) return null;
		str = str.trim();
		try {
			return new String(str.getBytes("utf-8"));
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 将数据库中的处理方式数值转化为处理方式的值
	 *
	 * @param arr
	 *            处理方式数组
	 * @param obj
	 *            Object 数据的下标值
	 * @return
	 */
	public static String getStr(String[] arr, Object obj,String mode) {
		String retStr = "";
		String[] objStr = null;
		StringBuffer strBuffer = new StringBuffer();
		if (obj!=null&&obj!="") {
			objStr= obj.toString().split(";");
			for(int i=0;i<objStr.length;i++){
				if("A".equals(objStr[i])){
					strBuffer.append(arr[10]);
				}else if("B".equals(objStr[i])){
					strBuffer.append(arr[11]);
				}else if("C".equals(objStr[i])){
					strBuffer.append(arr[12]);
				}else if("D".equals(objStr[i])){
					strBuffer.append(arr[13]);
				}else if("E".equals(objStr[i])){
					strBuffer.append(arr[14]);
				}else if("F".equals(objStr[i])){
					strBuffer.append(arr[15]);
				}else if("G".equals(objStr[i])){
					strBuffer.append(arr[16]);
				}else if("H".equals(objStr[i])){
					strBuffer.append(arr[17]);
				}else if("I".equals(objStr[i])){
					strBuffer.append(arr[18]);
				}else if("J".equals(objStr[i])){
					strBuffer.append(arr[19]);
				}else if("K".equals(objStr[i])){
					strBuffer.append(arr[20]);
				}else {
					strBuffer.append(arr[Integer.valueOf(objStr[i])]);
				}

				if(i!=objStr.length-1){
					strBuffer.append(StringUtils.isNotEmpty(mode)&&"tab".equals(mode)?"；</br>":"；");
				}
				retStr = strBuffer.toString();
			}
		}

		return retStr;
	}
	/**
	 * 判断是否是手机号
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){
		if(CommonUtils.isEmpty(mobiles)){
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-1,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	/**
     * 获取与当前时间相隔多后的日期；
     * @return
     */
    public static Date getEndDate(int field,int amount){
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(field, amount);
    	return calendar.getTime();
    }
    /**
     * 替换空格和换行和回车
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    /**
     * <p>
     * 方法描述：判断请求是手机端还是pc端
     * </p>
     * @param request
     * @return
     * @author laihb
     */
    public static boolean isMobileDevice(HttpServletRequest request){
    	String header = request.getHeader("user-agent");
    	// 手机端 参数头                        苹果          安桌        洛基亚
    	String[] deviceArr = new String[]{"mac os", "android", "windows phone"};

    	if(isEmpty(header)){
    		return false;
    	}

    	for (int i = 0; i < deviceArr.length; i++) {
    		if(header.toLowerCase().indexOf(deviceArr[i])>0){
    			return true;
    		}
		}

    	return false;
    }
    public static void main(String[] args) {
    	//boolean a = isMobileNO("11111111111");

    	System.out.println(getUUID());

    	//String aa = StringUtils.rightPad("19-Z00001", 20, "0");
		//System.err.println(aa);
		//System.err.println(aa.length());
	}

    /**
     * 获取当前Timestamp日期
     * @return
     */
    public static Timestamp getNowTimestamp() {
    	Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowdate=sdf.format(date);
        Timestamp timestamp = Timestamp.valueOf(nowdate);
        return timestamp;
    }


    /**
       * 补零
       * @param length 补零后的长度
       * @param source 需要补零的长符串
     * @return
       * @return
       */
    public static String fillZero(int length, String source) {
          StringBuilder result = new StringBuilder(source);
          for(int i=result.length(); i<length ; i++){
              result.insert(0, '0');
          }
          return result.toString();
      }


    /**
	 * <p>
	 * 方法描述: 格式化数字
	 * </p>
	 * @param number   数字
	 * @param fmt      输出格式，例："#,##0.00"
	 * @return
	 */
	public static String format(Object number, String fmt){
		if(number==null){
			return null;
		}
		DecimalFormat decimalFormat = new DecimalFormat(fmt);
		return decimalFormat.format(number);
	}


    /**
     * 方法描述: 返回UUID
     * @return
     * @author yaoxf
     */
    public static String getUUID(){
    	return UUID.randomUUID().toString().replace("-", "");
    }
    /**
	 * 参数比如："aa","bb","11","22",代表aa="11" and bb="22"
	 */
	public static QueryWrapper getEqQueryWrapper(Object... objects) {
		QueryWrapper wrapper = new QueryWrapper();
		int half = objects.length/2;
		for(int i=0; i<half; i++) {
			wrapper.eq(objects[i], objects[i+half]);
		}
		return wrapper;
	}
	/**
	 * 参数比如："aa","bb","11","22",代表aa="11" and bb="22"
	 */
	public static UpdateWrapper getEqUpdateWrapper(Object... objects) {
		UpdateWrapper wrapper = new UpdateWrapper();
		int half = objects.length/2;
		for(int i=0; i<half; i++) {
			wrapper.eq(objects[i], objects[i+half]);
		}
		return wrapper;
	}
	
	public static String doLongString(String str, int length) {
		if(StringUtils.isNotBlank(str) && str.length() > length ) {
			str = str.substring(0, length-3) + "...";
		}
		return str;
	}
	
}
