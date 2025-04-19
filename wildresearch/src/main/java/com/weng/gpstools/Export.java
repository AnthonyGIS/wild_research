/*
Copyright 2012 fangqing.fan@hotmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.weng.gpstools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.csvreader.CsvWriter;

import android.os.Environment;
import android.util.Log;

public class Export {
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static String export(List<Gps> list,boolean dataSwitch){
		StringBuffer result = new StringBuffer();
		
		if(Constant.EXPORT_XML){
			result.append(writeXMLToSD(list, dataSwitch));
		}
		if(Constant.EXPORT_CSV){
			result.append(writeCsvToSD(list, dataSwitch));
		}
		
		return result.toString();
	}

    
    public static String writeCsvToSD(List<Gps> list,boolean dataSwitch){  
    	String sdStatus = Environment.getExternalStorageState();
    	String result = null;
    	if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
    		Log.d("TestFile", "SD card is not avaiable/writeable right now.");
    		return "SD card is not avaiable/writeable right now.";
    	}
        try {  
        	String pathName="/sdcard/gpstools/";
    		String fileName= dataSwitch ? "gps_edit_"+ df.format(new Date()) +".csv":"gps_auto_"+ df.format(new Date()) +".csv"; 
    		File path = new File(pathName);
    		File file = new File(pathName + fileName);
    		if( !path.exists()) {
    			Log.d("TestFile", "Create the path:" + pathName);
    			path.mkdir();
    		}
    		if( !file.exists()) {
    			Log.d("TestFile", "Create the file:" + fileName);
    			file.createNewFile();
    		}
            CsvWriter wr =new CsvWriter(pathName+fileName,',',Charset.forName("UTF8"));  
            String[][] contents =csvFormat(list,dataSwitch); 
            for (int i = 0; i < contents.length; i++) {
            	wr.writeRecord(contents[i]);
            }
            wr.close();  
            
            result = "\nCVS文件保存成功!\n"+pathName+fileName;
         } catch (IOException e) {  
        	 result = "Error on writeFilToSD.";
            e.printStackTrace();  
         } finally{
     		return result;
     	} 
    }  
    
    public static String writeXMLToSD(List<Gps> list,boolean dataSwitch) {
    	String sdStatus = Environment.getExternalStorageState();
    	String result = null;
    	if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
    		Log.d("TestFile", "SD card is not avaiable/writeable right now.");
    		return "SD card is not avaiable/writeable right now.";
    	}
    	try {
    		String pathName="/sdcard/gpstools/";
    		
    		String fileName= dataSwitch ? "gps_edit_"+ df.format(new Date()) +".xml":"gps_auto_"+ df.format(new Date()) +".xml";
    		File path = new File(pathName);
    		File file = new File(pathName + fileName);
    		if( !path.exists()) {
    			Log.d("TestFile", "Create the path:" + pathName);
    			path.mkdir();
    		}
    		if( !file.exists()) {
    			Log.d("TestFile", "Create the file:" + fileName);
    			file.createNewFile();
    		}
    		FileOutputStream stream = new FileOutputStream(file);
    		String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<data>";
    		byte[] buf = (s+xmlFormat(list,dataSwitch)+"\n</data>").getBytes();
    		stream.write(buf);
    		stream.close();
    		result = "XML文件保存成功!\n"+pathName+fileName;
    	} catch(Exception e) {
    		Log.e("TestFile", "Error on writeFilToSD.");
    		result = "Error on writeFilToSD.";
    		e.printStackTrace();
    	}finally{
    		return result;
    	}
    }
    
    private static String xml_auto = "\n<gps>\n"+
								"<latitude>%f</latitude>\n"+
								"<longitude>%f</longitude>\n"+
								"<high>%f</high>\n"+
								"<direct>%f</direct>\n"+
								"<speed>%f</speed>\n"+
								"<gpsTime>%s</gpsTime>\n"+
								"</gps>";
    
    private static String xml_edit = "\n<gps>\n"+
									"<latitude>%f</latitude>\n"+
									"<longitude>%f</longitude>\n"+
									"<high>%f</high>\n"+
									"<direct>%f</direct>\n"+
									"<speed>%f</speed>\n"+
									"<gpsTime>%s</gpsTime>\n"+
									"<description>%s</description>\n"+
									"</gps>";
					    
    private static String xmlFormat(List<Gps> list,boolean dataSwitch){
    	
    	StringBuffer result = new StringBuffer();
    	for(Gps l:list){
    		result.append(String.format(dataSwitch?xml_edit:xml_auto,l.getLatitude(),l.getLongitude(),l.getHigh(),l.getDirect(),l.getSpeed(),l.getGpsTime(),l.getDescription()));
    	}
    	return result.toString();
    }
    
    private static String[][] csvFormat(List<Gps> list,boolean dataSwitch){
    	String[][] result;
    	result = dataSwitch ? new String[list.size()][7]: new String[list.size()][6];
    	for(int i=0;i<list.size();i++){
    		result[i][0] = String.valueOf(list.get(i).getLatitude());
    		result[i][1] = String.valueOf(list.get(i).getLongitude());
    		result[i][2] = String.valueOf(list.get(i).getHigh());
    		result[i][3] = String.valueOf(list.get(i).getDirect());
    		result[i][4] = String.valueOf(list.get(i).getSpeed());
    		result[i][5] = String.valueOf(list.get(i).getGpsTime());
    		if(dataSwitch){
    			result[i][6] = String.valueOf(list.get(i).getDescription());
    		}
    	}
    	return result;
    }

}
