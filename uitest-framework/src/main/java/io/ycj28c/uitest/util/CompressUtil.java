package io.ycj28c.uitest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressUtil {
	private static Logger log = LoggerFactory.getLogger(CompressUtil.class);
	
	/**
	 * 
	 * @param sourceFolder : the source folder want to be compressed
	 * @param targetZip : the output zip file directory
	 */
	public static boolean zipCompress(String sourceFolder, String targetZip){
		File file = new File(sourceFolder);
		File zipFile = new File(targetZip);
		
		InputStream input = null; 
		ZipOutputStream zipOut = null;
	     
		try{
			zipOut = new ZipOutputStream(new FileOutputStream(zipFile)) ;  
			zipOut.setComment("automation report compressed");
			int temp = 0 ;  
			if(file.isDirectory()){
				File lists[] = file.listFiles();
				for(int i=0;i<lists.length;i++){  
					input = new FileInputStream(lists[i]);
					zipOut.putNextEntry(new ZipEntry(file.getName()+File.separator+lists[i].getName()));
					while((temp=input.read())!=-1){
						zipOut.write(temp); 
					}  
					input.close();  
				}  
			}  
			zipOut.close();
		}catch (IOException e){
			log.error(" zipCompress: Error occour when doing compressed", e);
			return false;
		}
		return true;
	}
	
     
     
}
