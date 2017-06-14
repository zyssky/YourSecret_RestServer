package Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

public class FileManager {
	public static String IMAGE = "image";
	public static String ARTICAL = "artical";
	public static String IMAGE_SUFFIX = ".jpg";
	public static String ARTICAL_SUFFIX = ".html";
	
	public static String saveImage(String basePah,InputStream inputStream){
		
		String path = basePah+File.separator+IMAGE;
		File file = new File(path,""+new Date().getTime()+IMAGE_SUFFIX);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		while(file.exists()){
			file = new File(path,""+new Date().getTime()+IMAGE_SUFFIX);
		}
		
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
			return "";
		}finally {
			try {
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
				return "";
			}
			
		}
		return "/"+IMAGE+"/"+file.getName();
	}
	
	public static String saveArtical(String basePah,byte[] bytes) {
		String path = basePah+File.separator+ARTICAL;
		File file = new File(path,""+new Date().getTime()+ARTICAL_SUFFIX);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		while(file.exists()){
			file = new File(path,""+new Date().getTime()+ARTICAL_SUFFIX);
		}
		
		FileOutputStream outputStream = null;
		
		try{
			outputStream = new FileOutputStream(file);
			outputStream.write(bytes);
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}finally {
			try {
				outputStream.flush();
				outputStream.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
				return "";
			}
			
		}
		return "/"+ARTICAL+"/"+file.getName();
	}
	
	public static boolean deleteFile(String path){
        File file = new File(path);
        if (file.exists() == false) {
            return false;
        }
        return file.delete();
	}

}
