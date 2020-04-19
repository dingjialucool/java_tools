package com.chinobot.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.chinobot.common.utils.log.LogTypeName;
import com.chinobot.common.utils.log.LoggerUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPHelper {

	private Session session;
	private ChannelSftp channelSftp;
	private Channel channel;
	private Logger log = LoggerUtils.logger(LogTypeName.SHELL);
	/**
	 * 超时数,一分钟
	 */
	private final static int TIMEOUT = 60000;
	private final static int BYTE_LENGTH = 1024;

	public SFTPHelper(String userName, String password, String host) {
		try {
			String[] arr = host.split(":");
			String ip = arr[0];
			int port = arr.length > 1 ? Integer.parseInt(arr[1]) : 22;

			JSch jSch = new JSch();
			session = jSch.getSession(userName, ip, port);
			if (null != password) {
				session.setPassword(password);
			}
			session.setTimeout(TIMEOUT);
			Properties properties = new Properties();
			properties.put("StrictHostKeyChecking", "no");
			session.setConfig(properties);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 获得服务器连接 注意：操作完成务必调用close方法回收资源
	 * 
	 * @see SFTPHelper#close()
	 * @return
	 */
	public boolean connection() {
		try {
			if (!isConnected()) {
				session.connect();

				channelSftp = (ChannelSftp) session.openChannel("sftp");
				channelSftp.connect();
			}
			return true;
		} catch (JSchException e) {
			log.error("", e);
			return false;
		}
	}
	
	public void execCommand(String[] command) {
		BufferedReader reader;
		try {
			ChannelShell channelSl = (ChannelShell) session.openChannel("shell");
			channelSl.setPty(true);
			channelSl.connect();
            for(int i=0; i<command.length; i++) {
            	OutputStream os = channelSl.getOutputStream();
	            os.write((command[i] + "\r\n").getBytes());
	            if(i == command.length-1) {
	            	os.write(("echo \"execCommandClose\"\r\n").getBytes());
	            }
	            os.flush();
            }
//          输出响应信息
            InputStream in = channelSl.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
            	//System.out.println(buf);
            	log.info(buf);
            	if("execCommandClose".equals(buf)) {
            		break;
            	}
            }
            
            reader.close();
            in.close();
            channelSl.getOutputStream().close();
            channelSl.disconnect();
            
		} catch (IOException e) {
			log.error("", e);
		} catch (JSchException e) {
			log.error("", e);
		}
	}
	
//	public void execCommand(String command) {
//		BufferedReader reader = null;
//		try {
//			channel = session.openChannel("exec");
//			((ChannelExec) channel).setCommand(command);
//
//			channel.setInputStream(null);
//			((ChannelExec) channel).setErrStream(System.err);
//
//			channel.connect();
//			InputStream in = channel.getInputStream();
//			reader = new BufferedReader(new InputStreamReader(in));
//			String buf = null;
//			while ((buf = reader.readLine()) != null) {
//				System.out.println(buf);
//			}
//		} catch (IOException e) {
//			log.error("", e);
//		} catch (JSchException e) {
//			log.error("", e);
//		} finally {
//			try {
//				reader.close();
//			} catch (IOException e) {
//				log.error("", e);
//			}
//		}
//	}

	/**
	 * 从sftp服务器下载指定文件到本地指定目录
	 * 
	 * @param remoteFile 文件的绝对路径+fileName
	 * @param localPath  本地临时文件路径
	 * @return
	 */
	public boolean get(String remoteFile, String localPath) {
		if (isConnected()) {
			try {
				channelSftp.get(remoteFile, localPath);
				return true;
			} catch (SftpException e) {
				log.error("", e);
			}
		}
		return false;
	}

	/**
	 * 读取sftp上指定文件数据
	 * 
	 * @param remoteFile
	 * @return
	 */
	public byte[] getFileByte(String remoteFile) {
		byte[] fileData;
		try (InputStream inputStream = channelSftp.get(remoteFile)) {
			byte[] ss = new byte[BYTE_LENGTH];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			int rc = 0;
			while ((rc = inputStream.read(ss, 0, BYTE_LENGTH)) > 0) {
				byteArrayOutputStream.write(ss, 0, rc);
			}
			fileData = byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			log.error("", e);
			fileData = null;
		}
		return fileData;
	}

	/**
	 * 读取sftp上指定（文本）文件数据,并按行返回数据集合
	 *
	 * @param remoteFile
	 * @param charsetName
	 * @return
	 */
	public List<String> getFileLines(String remoteFile, String charsetName) {
		List<String> fileData;
		try (InputStream inputStream = channelSftp.get(remoteFile);
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
			String str;
			fileData = new ArrayList<>();
			while ((str = bufferedReader.readLine()) != null) {
				fileData.add(str);
			}
		} catch (Exception e) {
			log.error("", e);
			fileData = null;
		}
		return fileData;
	}

	/**
	 * 上传本地文件到sftp服务器指定目录
	 * 
	 * @param localFile
	 * @param remoteFile
	 * @return
	 */
	public boolean put(String localFile, String remoteFile) {
		if (isConnected()) {
			try {
				channelSftp.put(localFile, remoteFile);
				return true;
			} catch (SftpException e) {
				log.error("", e);
				return false;
			}
		}
		return false;
	}

	/**
	 * 从sftp服务器删除指定文件
	 * 
	 * @param remoteFile
	 * @return
	 */
	public boolean delFile(String remoteFile) {
		if (isConnected()) {
			try {
				channelSftp.rm(remoteFile);
				return true;
			} catch (SftpException e) {
				log.error("", e);
			}
		}
		return false;
	}

	/**
	 * 列出指定目录下文件列表
	 * 
	 * @param remotePath
	 * @return
	 */
	public Vector ls(String remotePath) {
		Vector vector = null;
		if (isConnected()) {
			try {
				vector = channelSftp.ls(remotePath);
			} catch (SftpException e) {
				vector = null;
				log.error("", e);
			}
		}
		return vector;
	}

	/**
	 * 列出指定目录下文件列表
	 * 
	 * @param remotePath
	 * @param filenamePattern
	 * @return 排除./和../等目录和链接,并且排除文件名格式不符合的文件
	 */
	public List<ChannelSftp.LsEntry> lsFiles(String remotePath, Pattern filenamePattern) {
		List<ChannelSftp.LsEntry> lsEntryList = null;
		if (isConnected()) {
			try {
				Vector<ChannelSftp.LsEntry> vector = channelSftp.ls(remotePath);
				if (vector != null) {
					lsEntryList = vector.stream().filter(x -> {
						boolean match = true;
						if (filenamePattern != null) {
							Matcher mtc = filenamePattern.matcher(x.getFilename());
							match = mtc.find();
						}
						if (match && !x.getAttrs().isDir() && !x.getAttrs().isLink()) {
							return true;
						}
						return false;
					}).collect(Collectors.toList());
				}
			} catch (SftpException e) {
				lsEntryList = null;
				log.error("", e);
			}
		}
		return lsEntryList;
	}

	/**
	 * 判断链接是否还保持
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if (session.isConnected() && channelSftp.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 关闭连接资源
	 */
	public void close() throws IOException {
		if (channel != null && channel.isConnected()) {
			channel.disconnect();
		}
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.quit();
		}
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	public static void main(String[] args) throws IOException {
		SFTPHelper sftpHelper = new SFTPHelper("kxlong", "6819720wolf", "172.31.226.17");
		if (sftpHelper.connection()) {
//			 boolean boo = sftpHelper.put("C:\\Users\\Lenovo\\Documents\\WXWork\\1688854118781564\\Cache\\File\\2019-09\\V0.1_01_01_1.zip","/home/wz/works/V0.1_01_01_1/");
//			 if(boo) {
//				 try {
//					sftpHelper.close();
//				} catch (IOException e) {
//					log.error("", e);
//				}
//			 }
			//sftpHelper.get("/home/plep/html/Annotations/c6b22e7c6732ed8eac95ce02a6580525/aa502e833fc8875db4d7df6ffb55d21d.xml", "D:\\programDoc\\aaa.xml");
			sftpHelper.execCommand(new String[] {"cd /home/","ls"});
			System.out.println("命令结束");
			sftpHelper.close();
		}else {
			System.out.println("22222");
		}
	}
}
