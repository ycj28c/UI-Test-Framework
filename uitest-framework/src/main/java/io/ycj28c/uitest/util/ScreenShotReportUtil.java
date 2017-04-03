/** 
 * Class <code>ReportUtil</code> the report with screen shot
 * 
 * @author 	RalphYang
 * @version 1.0 2015/07/28
 * @since 	JDK 1.8
 */
package io.ycj28c.uitest.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenShotReportUtil {
	private static final Logger log = LoggerFactory.getLogger(ScreenShotReportUtil.class);
	private static List<Map<String, String>> filling = new ArrayList<Map<String,String>>();
	
	/**
	 * add new screenshot information to the final report
	 * @param edModal
	 */
	public static void addToReport(ExportDataModal edModal){	
		if(edModal.getMethodName() == null||edModal.getMethodName().trim().equals("")
				||edModal.getImageName() == null||edModal.getImageName().trim().equals("")
				||edModal.getErrorLog() == null||edModal.getErrorLog().trim().equals("")
				||edModal.getStartTime() == null||edModal.getStartTime().trim().equals("")
				||edModal.getEndTime() == null||edModal.getEndTime().trim().equals("")){
			log.warn(" addFailTest: parmater should not be null");
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("method", edModal.getMethodName());
		map.put("image", edModal.getImageName());
		map.put("log", tranformatToHtml(edModal.getErrorLog()));
		map.put("startTime", edModal.getStartTime());
		map.put("endTime", edModal.getEndTime());
		map.put("description", tranformatToHtml(edModal.getDescription()));
		map.put("pageTitle", tranformatToHtml(edModal.getPageTitle()));
		map.put("currentUrl", tranformatToHtml(edModal.getCurrentUrl()));
		filling.add(map);	
	}
	
	/**
	 * trans format the string to html format
	 * @param str
	 * @return
	 */
	private static String tranformatToHtml(String str){
		if(str==null||str.trim().isEmpty()){
			return null;
		}
		str = str.replaceAll("(\r\n|\n)", "<br />");
		return str;
	}
	
	/**
	 * generate screenshot report at target path
	 * @param outputPath
	 */
	public static void generateReport(String outputPath){
		log.info("\n\n** BEGIN to generate report.");
		if(outputPath == null||outputPath.trim().equals("")){
			log.error(" generateReport: target path should not be empty");
		}
		
		File fp = new File(outputPath);  
		if (!fp.exists() &&!fp.isDirectory())  //if folder not existed, create one
		{       
			log.info(" folder {} doesn't exist, create new one", outputPath);
			fp.mkdir();    
		}
		
		String reportPath = outputPath + "/" + "screenShotReport.html";
		log.info(" report path is {}", reportPath);
		
	    PrintStream printStream = null;
		try {
			printStream = new PrintStream(new FileOutputStream(reportPath));
		} catch (FileNotFoundException e) {
			log.error(" The report output path '{}' is not available", reportPath);
		} 
	    
		StringBuilder sb = new StringBuilder();
	    //generate html head and body
	    sb.append("<html>");  
	    sb.append("<head>");  
	    sb.append("<title>TestNG Fail Report</title>");  
	    sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");  
	    sb.append("<style type=\"text/css\">");  
	    sb.append("TABLE{border-collapse:collapse;border-left:solid 1 #000000; border-top:solid 1 #000000;padding:5px;}");  
	    sb.append("TH{border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");  
	    sb.append("TD{font:normal;border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");  
	    sb.append("</style></head>");  
	    sb.append("<body bgcolor=\"#FFF8DC\">");  
	    sb.append("<div align=\"center\">");  
	    sb.append("<br/>");  
	    sb.append("<br/>");  
	    
	    //generate table head
	    sb.append("<table border='1' width='900' height='600' align='center'>"); 
	    sb.append("<tr>");
	    sb.append("<th colspan='2' bgcolor='#28C469'>TestNG Fail Test</td>");
	    sb.append("</tr>");

	    //generate the table column
	    for(int i=0;i<filling.size();i++){
	    	Map<String,String> map = filling.get(i);
	    	
	    	/*
	    	//fill any columns
	    	for(Entry<String, String> entry: map.entrySet()){
	    		log.info("The key is {}, the value is {}", entry.getKey(), entry.getValue());
	    		if(entry.getKey().equals("image")){ //image only display image
	    			sb.append("<tr>");
	    			sb.append("<td colspan='2'><img src=\""+entry.getValue()+"\" width='900' height='600'/></td>");
	    			sb.append("</tr>");
	    		} else {
	    			sb.append("<tr>");
		    		sb.append("<td>"+entry.getKey()+"</td>");
		    		sb.append("<td>"+entry.getValue()+"</td>");
		    		sb.append("</tr>");
	    		}
	    	} */
	    	
	    	//fill the data into html
	    	sb.append("<tr>");
    		sb.append("<td>Method Name</td>");
    		sb.append("<td>"+map.get("method")+"</td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td colspan='2'><img src=\""+map.get("image")+"\" width='900' height='600'/></td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td>Start Time</td>");
    		sb.append("<td>"+map.get("startTime")+"</td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td>End Time</td>");
    		sb.append("<td>"+map.get("endTime")+"</td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td>Description</td>");
    		sb.append("<td>"+map.get("description")+"</td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td>Page Title</td>");
    		sb.append("<td>"+map.get("pageTitle")+"</td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td>Page Url</td>");
    		sb.append("<td>"+map.get("currentUrl")+"</td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td>Error Log</td>");
    		sb.append("<td>"+map.get("log")+"</td>");
    		sb.append("</tr>");
    		
    		sb.append("<tr>");
    		sb.append("<td colspan='2' height='15' bgcolor='#071418'></td>");
    		sb.append("</tr>");
	    }
	    
	    sb.append("</table></div></body></html>");  
	    printStream.println(sb.toString()); 
	}
}
