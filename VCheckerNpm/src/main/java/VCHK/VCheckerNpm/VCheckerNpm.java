package VCHK.VCheckerNpm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.art;
import model.artifactList;

public class VCheckerNpm {
	public static void main(String[] args) {
		boolean result=false;    	
    	
    	String file="./"+args[0];
    	result=CheckVersions(file);
    	if(result){
    		System.out.println("The artifacts in the versions are located in npmjs.org");
    	}else{
    		System.out.println("The artifacts in the versions are not located in npmjs.org");
    	} 
	}
	
	private static boolean CheckVersions(String file) {
		boolean comprobar=true;
		//We get the artifact and the version from config.json
		ObjectMapper mapper = new ObjectMapper();			
		artifactList al=null;
		try {
			al = mapper.readValue(new File(file), artifactList.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//tenemos todos los artefactos en al
		Iterator<art> it=al.getArtifacts().iterator();
		
		while(it.hasNext()){
			art artefacto=it.next();
			comprobar=CheckVersion(artefacto);
			if(!comprobar){
				return comprobar; 
			}			
		}	
		return comprobar;
	}

	private static boolean CheckVersion(art artefacto) {
//		 running the npm view comand
		String cmd="npm view "+artefacto.getArtifact()+"@"+artefacto.getVersion();
		Process process=null;
		InputStream is=null;
		
	    try {
	    	process=Runtime.getRuntime().exec(cmd);	
	    	System.out.println("consulta ejecutada");
			is=process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder out = new StringBuilder();
		    String line;
		    String json="";
			while ((line = reader.readLine()) != null) {
			        out.append(line.trim());			       
			}
			json=json+out.toString();
			if(json.equals(""))
				return false;
			
			System.out.println(json);  //Prints the string content read from input stream
		    reader.close();
		    
		    ArrayList<String>palJson=ObtenerPalabrasJson(json);
		    if(artefacto.getArtifact().equals(palJson.get(palJson.indexOf("name:")+1))){		    	
		    	for(int versions=palJson.indexOf("versions:")+1;versions<palJson.indexOf("maintainers:");versions++){
		    		if(artefacto.getVersion().equals(palJson.get(versions))){
		    			return true;
		    		}
		    	}		    	
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	}

	private static ArrayList<String> ObtenerPalabrasJson(String json) {
		ArrayList<String>result=new ArrayList<String>();
		//We have the information from npmjs.org in the string json
	    String delimiter=",{}'[]";
	    StringTokenizer st=new StringTokenizer(json,delimiter);
	    while(st.hasMoreTokens()){
	    	result.add(st.nextToken().trim());
	    }
		return result;
	}	
}