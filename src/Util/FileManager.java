package Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

public class FileManager {
	public static String ARTICAL_IMAGE_SUBPATH = "image";
	public static String ARTICAL_CONTENT_SUBPATH = "artical";
	public static String USER_ICON_SUBPATH = "userIcon";
	public static String JPG = ".jpg";
	public static String HTML = ".html";
	public static String PNG = ".png";
	
	public static String saveInputStream(String parentPath,String suffix,InputStream inputStream) {
		File parentFile = new File(parentPath);
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		File file = getUniuqeFile(parentFile, suffix);
		
		String fileName = file.getName();
		
		FileOutputStream outputStream = null;
		
		try{
			byte[] bytes = new byte[1024];
			int read = 0;
			outputStream = new FileOutputStream(file);
			while((read = inputStream.read(bytes)) != -1){
				outputStream.write(bytes);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			file = null;
		}finally {
			try {
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
				fileName = null;
			}
			
		}
		
		return fileName;
	}
	
	public static String saveBytes(String parentPath,String suffix,byte[] bytes) {
		File parentFile = new File(parentPath);
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		File file = getUniuqeFile(parentFile, suffix);
		
		String fileName = file.getName();
		
		FileOutputStream outputStream = null;
		
		try{
			outputStream = new FileOutputStream(file);
			outputStream.write(bytes);
		}
		catch(Exception e){
			e.printStackTrace();
			fileName = null;
		}finally {
			try {
				outputStream.flush();
				outputStream.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
				fileName = null;
			}
			
		}
		
		return fileName;
	}
	
	public static File getUniuqeFile(File parentFile,String suffix) {
		File file = new File(parentFile,new Date().getTime()+suffix);
		while(file.exists()){
			file = new File(parentFile,new Date().getTime()+suffix);
		}
		return file;
	}
	
	public static String saveFile(String localBasePath,
			String subPath,String suffix,InputStream inputStream) {
		String fileName = saveInputStream(localBasePath+File.separator+subPath, suffix, inputStream);
		if(fileName!=null){
			return File.separator+subPath+File.separator+fileName;
		}
		else{
			return null;
		}
	}
	
	public static String saveFile(String localBasePath,
			String subPath,String suffix,byte[] bytes) {
		String fileName = saveBytes(localBasePath+File.separator+subPath, suffix, bytes);
		if(fileName!=null){
			return File.separator+subPath+File.separator+fileName;
		}
		else{
			return null;
		}
	}
	
//	public static String saveImage(String basePah,InputStream inputStream){
//		
//		String path = basePah+File.separator+IMAGE;
//		File file = new File(path,""+new Date().getTime()+IMAGE_SUFFIX);
//		if(!file.getParentFile().exists()){
//			file.getParentFile().mkdirs();
//		}
//		
//		while(file.exists()){
//			file = new File(path,""+new Date().getTime()+IMAGE_SUFFIX);
//		}
//		
//		FileOutputStream outputStream = null;
//		
//		try{
//			byte[] bytes = new byte[1024];
//			int read = 0;
//			outputStream = new FileOutputStream(file);
//			while((read = inputStream.read(bytes)) != -1){
//				outputStream.write(bytes);
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			return "";
//		}finally {
//			try {
//				inputStream.close();
//				outputStream.flush();
//				outputStream.close();
//				
//			} catch (Exception e2) {
//				// TODO: handle exception
//				e2.printStackTrace();
//				return "";
//			}
//			
//		}
//		return "/"+IMAGE+"/"+file.getName();
//	}
	
//	public static String saveArtical(String basePah,byte[] bytes) {
//		String path = basePah+File.separator+ARTICAL_CONTENT_SUBPATH;
//		File file = new File(path,""+new Date().getTime()+HTML);
//		if(!file.getParentFile().exists()){
//			file.getParentFile().mkdirs();
//		}
//		
//		while(file.exists()){
//			file = new File(path,""+new Date().getTime()+HTML);
//		}
//		
//		FileOutputStream outputStream = null;
//		
//		try{
//			outputStream = new FileOutputStream(file);
//			outputStream.write(bytes);
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			return "";
//		}finally {
//			try {
//				outputStream.flush();
//				outputStream.close();
//				
//			} catch (Exception e2) {
//				// TODO: handle exception
//				e2.printStackTrace();
//				return "";
//			}
//			
//		}
//		return "/"+ARTICAL_CONTENT_SUBPATH+"/"+file.getName();
//	}
	
	public static boolean deleteFile(String path){
        File file = new File(path);
        if (file.exists() == false) {
            return false;
        }
        return file.delete();
	}

}
