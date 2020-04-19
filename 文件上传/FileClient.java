package com.chinobot.common.file.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.chinobot.common.constant.GlobalConstant;
import com.chinobot.common.file.entity.OriginFiles;
import com.chinobot.common.file.entity.UploadFile;
import com.chinobot.common.file.service.IUploadFileService;
import com.chinobot.common.utils.CommonUtils;
import com.chinobot.common.utils.ZipUtil;

import net.coobird.thumbnailator.Thumbnails;

@Component
public class FileClient {
	
	@Autowired
	private FastDFSClient fastDFSClient;
	
	@Autowired
	IUploadFileService uploadFileService;
	
	// 上传单个文件到文件服务器
	public UploadFile uploadFile(File file) throws Exception {
		String filePath = fastDFSClient.uploadFile (file);
		UploadFile uploadFile = new UploadFile();
		uploadFile.setOriginName (file.getName());
		uploadFile.setCurrentName (filePath.substring (filePath.lastIndexOf ("/") + 1));
		uploadFile.setSize (BigDecimal.valueOf (file.length()));
		uploadFile.setDataStatus (GlobalConstant.DATA_STATUS_VALID);
		uploadFile.setPath (filePath);
		uploadFileService.save(uploadFile);
		return uploadFile;
	}
	
	 /**
     * 下载图片
     * @param filePath   路径
     * @param thumbnail  是否缩略图
	 * @param pictureSize 控制图片清晰度
     * @return 
     */
    public byte[] downloadImage(String filePath, boolean thumbnail, String pictureSize) {
    	if(thumbnail) {	//生成缩略图
    		try {
    			byte[] fileByte = fastDFSClient.downloadFile(filePath);
    			ByteArrayOutputStream bo = new ByteArrayOutputStream();
    			
    			Integer width = null;
    			Integer height = null;
    			if(CommonUtils.isNotEmpty(pictureSize)) {
    				String[] split = pictureSize.split(",");
        			if(split.length==2) {
        				width = Integer.parseInt(split[0]);
        				height = Integer.parseInt(split[1]);
        			}
    			}
    			if(width != null && height != null) {
    				Thumbnails.of(new ByteArrayInputStream(fileByte)).size(width, height).toOutputStream(bo);
    			}else {
    				Thumbnails.of(new ByteArrayInputStream(fileByte)).size(276, 175).toOutputStream(bo);
				}
    			
    			return bo.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return downloadFile(filePath);
    }
	
	
	public List<UploadFile> uploadFile(File[] files) throws Exception {
		List<UploadFile> listUploadFiles = new ArrayList<UploadFile>();
		for(File file : files) {
			// 上传到文件服务器
            String filePath = fastDFSClient.uploadFile (file);
            UploadFile uploadFile = new UploadFile();
            uploadFile.setOriginName (file.getName());
            uploadFile.setCurrentName (filePath.substring (filePath.lastIndexOf ("/") + 1));
            uploadFile.setSize (BigDecimal.valueOf (file.length()));
            uploadFile.setDataStatus (GlobalConstant.DATA_STATUS_VALID);
            uploadFile.setPath (filePath);
            listUploadFiles.add(uploadFile);
		}
		return listUploadFiles;
	}
	
    public List<UploadFile> uploadFile(MultipartFile file) throws IOException {
    	List<UploadFile> listUploadFiles = new ArrayList<UploadFile>();
    	// 上传到文件服务器
        String filePath = fastDFSClient.uploadFile (file);
        UploadFile uploadFile = new UploadFile();
        uploadFile.setOriginName (file.getOriginalFilename ());
        uploadFile.setCurrentName (filePath.substring (filePath.lastIndexOf ("/") + 1));
        uploadFile.setSize (BigDecimal.valueOf (file.getSize ()));
        uploadFile.setDataStatus (GlobalConstant.DATA_STATUS_VALID);
        uploadFile.setPath (filePath);
        listUploadFiles.add(uploadFile);
		/*
		 * if(file.getOriginalFilename ().matches(".*.zip")) { String temPath =
		 * "D:\\red_ant_file\\";//临时文件夹，用于存放解压后的文件 boolean bo =
		 * ZipUtil.decompressZip(file, temPath); if(bo) { List list =
		 * ZipUtil.traverseFolder(temPath);//获取文件夹下的所有文件 for (Object path : list) { File
		 * file2 = new File((String) path);
		 * 
		 * 
		 * 
		 * 
		 * // 上传到文件服务器 String filePath = fastDFSClient.uploadFile (file2); UploadFile
		 * uploadFile = new UploadFile(); uploadFile.setOriginName (file2.getName());
		 * uploadFile.setCurrentName (filePath.substring (filePath.lastIndexOf ("/") +
		 * 1)); uploadFile.setSize (BigDecimal.valueOf (file2.length()));
		 * uploadFile.setDataStatus (GlobalConstant.DATA_STATUS_VALID);
		 * uploadFile.setPath (filePath); listUploadFiles.add(uploadFile); }
		 * ZipUtil.deleteDir(temPath);//删除临时文件夹下的所有文件 }
		 * 
		 * }else { // 上传到文件服务器 String filePath = fastDFSClient.uploadFile (file);
		 * UploadFile uploadFile = new UploadFile(); uploadFile.setOriginName
		 * (file.getOriginalFilename ()); uploadFile.setCurrentName (filePath.substring
		 * (filePath.lastIndexOf ("/") + 1)); uploadFile.setSize (BigDecimal.valueOf
		 * (file.getSize ())); uploadFile.setDataStatus
		 * (GlobalConstant.DATA_STATUS_VALID); uploadFile.setPath (filePath);
		 * listUploadFiles.add(uploadFile); }
		 */
        

        return listUploadFiles;
    }

    /**
     * @param filePath 文件地址
     * @description 下载文件
     */
    public byte[] downloadFile(String filePath) {
        return fastDFSClient.downloadFile (filePath);
    }

	public List<OriginFiles> originFile(MultipartFile file,String catalogId) throws IOException {
		List<OriginFiles> listUploadFiles = new ArrayList<OriginFiles>();
    	if(file.getOriginalFilename ().matches(".*.zip")) {
    		String temPath = "D:\\red_ant_file\\";//临时文件夹，用于存放解压后的文件
    		boolean bo = ZipUtil.decompressZip(file, temPath);
    		if(bo) {
    			List list = ZipUtil.traverseFolder(temPath);//获取文件夹下的所有文件
    			for (Object path : list) {
    				File file2 = new File((String) path);
    				MultipartFile multipartFile = null;
    				 // 需要导入commons-fileupload的包
    		        FileItem fileItem = new DiskFileItem("copyfile.txt", Files.probeContentType(file2.toPath()),false,file2.getName(),(int)file2.length(),file2.getParentFile());
    		        byte[] buffer = new byte[4096];
    		        int n;
    		        try (
    		        		InputStream inputStream = new FileInputStream(file2); 
    		        		OutputStream os = fileItem.getOutputStream()){
    		           while ( (n = inputStream.read(buffer,0,4096)) != -1){
    		               os.write(buffer,0,n);
    		           }
    		            //也可以用IOUtils.copy(inputStream,os);
    		           multipartFile  = new CommonsMultipartFile(fileItem);
    		            System.out.println(multipartFile.getName());
    		        }catch (IOException e){
    		            e.printStackTrace();
    		        }
    				
    				
    				
    				// 上传到文件服务器
    		        String filePath = fastDFSClient.uploadFile(file2);
    		        OriginFiles uploadFile = new OriginFiles();
    		        uploadFile.setOriginName (multipartFile.getOriginalFilename().substring(0,multipartFile.getOriginalFilename().indexOf(".")));
    		        uploadFile.setCurrentName (filePath.substring (filePath.lastIndexOf ("/") + 1));
    		        uploadFile.setSize (BigDecimal.valueOf (multipartFile.getSize ()));
    		        uploadFile.setDataStatus (GlobalConstant.DATA_STATUS_VALID);
    		        uploadFile.setPath (filePath);
    		        uploadFile.setCatalogId(catalogId);
    		        uploadFile.setType(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().indexOf(".")+1));
    		        listUploadFiles.add(uploadFile);
				}
    			ZipUtil.deleteDir(temPath);//删除临时文件夹下的所有文件
    		}
    	   
    	}else {
    		// 上传到文件服务器
            String filePath = fastDFSClient.uploadFile(file);
            OriginFiles uploadFile = new OriginFiles();
            uploadFile.setOriginName (file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf(".")));
            uploadFile.setCurrentName (filePath.substring (filePath.lastIndexOf ("/") + 1));
            uploadFile.setSize (BigDecimal.valueOf (file.getSize ()));
            uploadFile.setDataStatus (GlobalConstant.DATA_STATUS_VALID);
            uploadFile.setType(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1));
            uploadFile.setPath (filePath);
            uploadFile.setCatalogId(catalogId);
            listUploadFiles.add(uploadFile);
    	}
        

        return listUploadFiles;
	}
}
