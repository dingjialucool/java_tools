package com.chinobot.common.utils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

public class FileUtil {
	
	/** 缓冲器大小 */
    private static final int BUFFER = 1024;
    
	public static void main(String[] args) {
//		String newPath = "D:\\labelme\\Zips\\test";
//		copyDir("D:\\labelme\\Annotations\\test\\", newPath);
//		copyDir("D:\\labelme\\Images\\test\\", newPath);
//		copyDir("D:\\labelme\\Masks\\test\\", newPath);
		try {
//			TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(new File("D:\\111.tar")));
//			deTarFile("D:\\", tais);
//			tais.close();
//			System.out.println("解压成功");
			
//			unzip("D:\\DJI_0001.zip", "D:\\test");
			
//			unRarFile("D:\\test.rar", "D:\\test");
			
			File file = new File("D:\\aaa\\DJI_0314.JPG");
			String lng = FileUtil.getFileMetadata(file, "GPS Longitude");
			String lat = FileUtil.getFileMetadata(file, "GPS Latitude");
			System.out.println(pointToLatlong(lng));
			System.out.println(pointToLatlong(lat));
			
//			delFile(file);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static Double pointToLatlong(String point) {
		Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
		Double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("'")).trim());
		Double miao = Double.parseDouble(point.substring(point.indexOf("'") + 1, point.indexOf("\"")).trim());
		Double duStr = du + fen / 60 + miao / 60 / 60;
		return duStr;
	}
	
	public static String getFileMetadata(File file, String key) {
		String value = "";
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			for (Directory directory : metadata.getDirectories()) {
				boolean ct = false;
				for (Tag tag : directory.getTags()) {
					String tagName = tag.getTagName();
					value = tag.getDescription();
					if (tagName.equals(key)) {
						ct = true;
						break;
					}else {
						value = "";
					}
				}
				if (ct) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return value;
	}
	
	public static File createDir(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public static File createFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			return file;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public static void copyFile(File sourceFile, File targetFile) {
		try {
			// 新建文件输入流并对它进行缓冲
			FileInputStream input = new FileInputStream(sourceFile);
			BufferedInputStream inBuff = new BufferedInputStream(input);
	
			// 新建文件输出流并对它进行缓冲
			FileOutputStream output = new FileOutputStream(targetFile);
			BufferedOutputStream outBuff = new BufferedOutputStream(output);
	
			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
	
			// 关闭流
			inBuff.close();
			outBuff.close();
			output.close();
			input.close();	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 读入TXT文件
	 */
	public static String readToString(String filePath) {
		StringBuffer sb = new StringBuffer();
		FileReader reader = null;
		BufferedReader br = null;
		String line;
		try {
			reader = new FileReader(filePath);
			br = new BufferedReader(reader);
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 递归删除目录已经下面的所有文件
	 * 慎用、慎用、慎用
	 * @param file
	 * @return
	 */
	public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
	
	
	public static void copyDir(String sourcePath, String newPath) {
		try {
			File file = new File(sourcePath);
			String[] filePath = file.list();
			if (filePath == null || filePath.length == 0) {
				return;
			}
			if (!(new File(newPath)).exists()) {
				(new File(newPath)).mkdir();
			}
			for (int i = 0; i < filePath.length; i++) {
				if ((new File(sourcePath + File.separator + filePath[i])).isDirectory()) {
					copyDir(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
				}
				if (new File(sourcePath + File.separator + filePath[i]).isFile()) {
					copyFile(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			File oldFile = new File(oldPath);
			File file = new File(newPath);
			FileInputStream in = new FileInputStream(oldFile);
			FileOutputStream out = new FileOutputStream(file);

			byte[] buffer = new byte[2097152];
			int readByte = 0;
			while ((readByte = in.read(buffer)) != -1) {
				out.write(buffer, 0, readByte);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
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

	public static final InputStream byte2Input(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	public static void tarFile(File file, TarArchiveOutputStream taos) throws Exception {
		TarArchiveEntry tae = new TarArchiveEntry(file);
		tae.setSize(file.length());// 大小
		tae.setName(new String(file.getName().getBytes("gbk"), "ISO-8859-1"));// 不设置会默认全路径
		taos.putArchiveEntry(tae);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int count;
		byte data[] = new byte[1024];
		while ((count = bis.read(data, 0, 1024)) != -1) {
			taos.write(data, 0, count);
		}
		bis.close();
		taos.closeArchiveEntry();
	}

	public static void untar(String srcPath, String destPath) {
		try {
			TarArchiveEntry tae = null;
			TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(new File(srcPath)));
			while ((tae = tais.getNextTarEntry()) != null) {
				String dir = destPath + File.separator + tae.getName();// tar档中文件
				File dirFile = new File(dir);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirFile));
	
				int count;
				byte data[] = new byte[1024];
				while ((count = tais.read(data, 0, 1024)) != -1) {
					bos.write(data, 0, count);
				}
				bos.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	 /**
	  * 创建文件
	  * 根据压缩包内文件名和解压缩目的路径，创建解压缩目标文件，
	  * 生成中间目录
     * @param dstPath 解压缩目的路径
     * @param fileName 压缩包内文件名
     *
     * @return 解压缩目标文件
     *
     * @throws IOException
     */
    private static File createFile(String dstPath, String fileName) throws IOException {
        String[] dirs = fileName.split("/");//将文件名的各级目录分解
        File     file = new File(dstPath);
        if (dirs.length > 1) {//文件有上级目录
            for (int i = 0; i < dirs.length - 1; i++) {
                file = new File(file, dirs[i]);//依次创建文件对象知道文件的上一级目录
            }
            if (!file.exists()) {
                file.mkdirs();//文件对应目录若不存在，则创建
                System.out.println("mkdirs: " + file.getCanonicalPath());
            }
            file = new File(file, dirs[dirs.length - 1]);//创建文件
            return file;
        } else {
            if (!file.exists()) {
                file.mkdirs();//若目标路径的目录不存在，则创建
                System.out.println("mkdirs: " + file.getCanonicalPath());
            }
            file = new File(file, dirs[0]);//创建文件
            return file;
        }
    }
    
	/**
	 * 压缩成ZIP 方法2
	 * 
	 * @param srcFiles 需要压缩的文件列表
	 * @param out      压缩文件输出流
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[1024];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    
	  /**
	   * 解压缩方法
     *
     * @param zipFileName 压缩文件名
     * @param dstPath 解压目标路径
     *
     * @return
     */
    public static boolean unzip(String srcPath, String destPath) {
        System.out.println("zip uncompressing...");
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(srcPath));
            ZipEntry       zipEntry       = null;
            byte[]         buffer         = new byte[BUFFER];//缓冲器
            int            readLength     = 0;//每次读出来的长度
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {//若是zip条目目录，则需创建这个目录
                    File dir = new File(destPath + File.separator + zipEntry.getName());
                    if (!dir.exists()) {
                        dir.mkdirs();
                        System.out.println("mkdirs: " + dir.getCanonicalPath());
                        continue;//跳出
                    }
                }
                File file = createFile(destPath, zipEntry.getName());//若是文件，则需创建该文件
                System.out.println("file created: " + file.getCanonicalPath());
                OutputStream outputStream = new FileOutputStream(file);
                while ((readLength = zipInputStream.read(buffer, 0, BUFFER)) != -1) {
                    outputStream.write(buffer, 0, readLength);
                }
                outputStream.close();
                System.out.println("file uncompressed: " + file.getCanonicalPath());
            }    // end while
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("unzip fail!");
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("unzip fail!");
            return false;
        }
        System.out.println("unzip success!");
        return true;
    }
    
    /**
     * 根据原始rar路径，解压到指定文件夹下.
     *
     * @param srcRarPath       原始rar路径
     * @param dstDirectoryPath 解压到的文件夹
     */
    public static void unrar(String srcPath, String destPath) {
        if (!srcPath.toLowerCase().endsWith(".rar")) {
            System.out.println("非rar文件！");
            return;
        }
        File dstDiretory = new File(destPath);
        if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
            dstDiretory.mkdirs();
        }
        Archive a = null;
        try {
            a = new Archive(new File(srcPath));
            if (a != null) {
                a.getMainHeader().print(); // 打印文件信息.
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // 文件夹
                        File fol = new File(destPath + File.separator
                                + fh.getFileNameString());
                        fol.mkdirs();
                    } else { // 文件
                        File out = new File(destPath + File.separator
                                + fh.getFileNameString().trim());
                        try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * 生成.json格式文件
	 */
	public static File createJsonFile(String jsonString, String filePath, String fileName) {
		// 拼接文件完整路径
		String fullPath = filePath + File.separator + fileName;
		// 保证创建一个新文件
		File file = new File(fullPath);
		// 生成json格式文件
		try {
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();

			if(jsonString.indexOf("'")!=-1){
				//将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
				jsonString = jsonString.replaceAll("'", "\\'");
			}
			if(jsonString.indexOf("\"")!=-1){
				//将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
				jsonString = jsonString.replaceAll("\"", "\\\"");
			}

			if(jsonString.indexOf("\r\n")!=-1){
				//将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
				jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
			}
			if(jsonString.indexOf("\n")!=-1){
				//将换行转换一下，因为JSON串中字符串不能出现显式的换行
				jsonString = jsonString.replaceAll("\n", "\\u000a");
			}

			// 格式化json字符串
			jsonString = JsonFormatTool.formatJson(jsonString);

			// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回是否成功的标记
		return file;
	}


}
