package com.chinobot.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @ClassName: ZipUtil
 * @Description: zip解压
 * @author: dingjialu
 * @date:2019年6月4日 下午7:24:12
 */
public class ZipUtil {

	public static void compress(File f, String baseDir, ZipOutputStream zos) {
		if (!f.exists()) {
			return;
		}
		File[] fs = f.listFiles();
		BufferedInputStream bis = null;
		byte[] bufs = new byte[1024 * 10];
		FileInputStream fis = null;
		try {
			for (int i = 0; i < fs.length; i++) {
				String fName = fs[i].getName();
				if (fs[i].isFile()) {
					ZipEntry zipEntry = new ZipEntry(baseDir + fName);
					zos.putNextEntry(zipEntry);
					fis = new FileInputStream(fs[i]);
					bis = new BufferedInputStream(fis, 1024 * 10);
					int read = 0;
					while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
						zos.write(bufs, 0, read);
					}
				} else if (fs[i].isDirectory()) {
					compress(fs[i], baseDir + fName + "/", zos);
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bis)
					bis.close();
				if (null != fis)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean decompressZip(MultipartFile files, String descDir) {
		File file2 = new File("D:\\temp\\");

		try {
			FileUtils.copyInputStreamToFile(files.getInputStream(), file2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File zipFile = file2;
		boolean flag = false;
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile, Charset.forName("gbk"));// 防止中文目录，乱码
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);
				// 指定解压后的文件夹+当前zip文件的名称
				String outPath = (descDir + zipEntryName).replace("/", File.separator);
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				// 保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
				System.err.println("当前zip解压之后的路径为：" + outPath);
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[2048];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			flag = true;
			// 必须关闭，要不然这个zip文件一直被占用着，要删删不掉，改名也不可以，移动也不行，整多了，系统还崩了。
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings("rawtypes")
	public static boolean decompressZip(InputStream ins, String descDir) {
		File file2 = new File("D:\\temp\\");

		try {
			FileUtils.copyInputStreamToFile(ins, file2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File zipFile = file2;
		boolean flag = false;
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile, Charset.forName("gbk"));// 防止中文目录，乱码
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);
				// 指定解压后的文件夹+当前zip文件的名称
				String outPath = (descDir + zipEntryName).replace("/", File.separator);
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				// 保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
				System.err.println("当前zip解压之后的路径为：" + outPath);
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[2048];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			flag = true;
			// 必须关闭，要不然这个zip文件一直被占用着，要删删不掉，改名也不可以，移动也不行，整多了，系统还崩了。
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean decompressZip(InputStream ins, String descDir, String temp) {
		File file2 = new File(temp);

		try {
			FileUtils.copyInputStreamToFile(ins, file2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File zipFile = file2;
		boolean flag = false;
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile, Charset.forName("gbk"));// 防止中文目录，乱码
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);
				// 指定解压后的文件夹+当前zip文件的名称
				String outPath = (descDir + zipEntryName).replace("/", File.separator);
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				// 保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
				System.err.println("当前zip解压之后的路径为：" + outPath);
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[2048];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			flag = true;
			// 必须关闭，要不然这个zip文件一直被占用着，要删删不掉，改名也不可以，移动也不行，整多了，系统还崩了。
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	@SuppressWarnings("rawtypes")
	public static boolean decompressZipFile(File zipFile, String descDir) {
		boolean flag = false;
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile, Charset.forName("gbk"));// 防止中文目录，乱码
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zip.getInputStream(entry);
				// 指定解压后的文件夹+当前zip文件的名称
				String outPath = (descDir + zipEntryName).replace("/", File.separator);
				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
				if (!file.exists()) {
					file.mkdirs();
				}
				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) {
					continue;
				}
				// 保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
				System.err.println("当前zip解压之后的路径为：" + outPath);
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[2048];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			flag = true;
			// 必须关闭，要不然这个zip文件一直被占用着，要删删不掉，改名也不可以，移动也不行，整多了，系统还崩了。
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 获取文件夹下的所有文件
	 * 
	 * @param path
	 */
	public static List traverseFolder(String path) {
		List<String> list = new ArrayList<String>();
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (null == files || files.length == 0) {
				System.out.println("文件夹是空的!");
				return null;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("文件夹:" + file2.getAbsolutePath());
						traverseFolder(file2.getAbsolutePath());
					} else {
						System.out.println("文件:" + file2.getAbsolutePath());
						list.add(file2.getAbsolutePath());
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		return list;
	}

	/**
	 * 删除文件夹及文件夹下的所有文件
	 * 
	 * @param dirPath
	 */
	public static void deleteDir(String dirPath) {
		File file = new File(dirPath);
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			if (files == null) {
				file.delete();
			} else {
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i].getAbsolutePath());
				}
				file.delete();
				System.out.println("删除了文件夹");
			}
		}
	}
	
	/**
	 * 删除文件夹下的所有文件
	 * 
	 * @param dirPath
	 */
	public static void deleteDirOfFiles(String dirPath) {
		File file = new File(dirPath);
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			if (files == null) {
				file.delete();
			} else {
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i].getAbsolutePath());
				}
			}
		}
	}
	
	/**
	 * 将Byte数组转换成文件
	 * @param bytes
	 * @param filePath
	 * @param fileName
	 */
	public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
		File dir = new File(filePath);
		System.out.println(dir.isDirectory());
		if (!dir.exists()) {// 判断文件目录是否存在
		      dir.mkdirs();
	    }
	    file = new File(filePath , fileName);
	    fos = new FileOutputStream(file);
	    bos = new BufferedOutputStream(fos);
	     bos.write(bytes);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (bos != null) {
	                try {
	                   bos.close();
	                } catch (IOException e) {
	                   e.printStackTrace();
	               }
	            }
	          if (fos != null) {
	              try {
	                     fos.close();
	              } catch (IOException e) {
	                    e.printStackTrace();
	                }
	           }
	         }
	   }
}
