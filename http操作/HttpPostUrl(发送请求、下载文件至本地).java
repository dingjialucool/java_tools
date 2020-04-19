package com.chinobot.common.utils;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Post Method
 */
public class HttpPostUrl {
	
	private static final int defaultTimeOut = 5;
	
	public static String postJson(String url, Map<String, Object> paramMap) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        HttpURLConnection  conn = (HttpURLConnection) realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setConnectTimeout(defaultTimeOut * 1000);
        conn.setReadTimeout(defaultTimeOut * 1000);
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        System.out.println(JSON.toJSONString(paramMap));
        out.print(JSON.toJSONString(paramMap));
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        // 使用finally块来关闭输出流、输入流
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 向指定URL发送POST请求
     * @param url
     * @param paramMap
     * @return 响应结果
     */
    public static String sendPost(String url, Map<String, String> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 设置请求属性
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                while (ite.hasNext()) {
                    String key = ite.next();// key
                    String value = paramMap.get(key);
                    //param += key + "=" + value + "&";
                    param += key + "=" + URLEncoder.encode(String.valueOf(value), "utf-8") + "&";
                }
                param = param.substring(0, param.length() - 1);
            }

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += new String(line.getBytes("GBK"),"UTF-8");
            }
        } catch (Exception e) {
            System.err.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 数据流post请求
     * @param urlStr
     * @param xmlInfo
     */
    public static String doPost(String urlStr, String xmlInfo) {
        String reStr="";
        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(new String(xmlInfo.getBytes("utf-8")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                reStr += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    /**
     *  下载文件至本地
     * @param urlString
     * @param filename
     * @param savePath
     * @throws Exception
     */
    public static void download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }


    /**
     * 测试主方法
     * @param args
     */
    public static void main(String[] args) {
        Map<String, String> mapParam = new HashMap<String, String>();
        mapParam.put("name", "张三");
        mapParam.put("validation","test");
        String pathUrl = "http://localhost/testPost.action";
        String result = sendPost(pathUrl, mapParam);
    }

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			if(!StringUtils.isEmpty(param)) {
				urlNameString += "?" + param;
			}
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

}
