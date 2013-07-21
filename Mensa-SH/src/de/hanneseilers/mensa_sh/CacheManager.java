package de.hanneseilers.mensa_sh;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

/**
 * Manager class for file caching
 * @author hannes
 *
 */
public class CacheManager {

	/**
	 * Checks for a cached text file 
	 * @param filename
	 * @return file text or null if file doesn't exsist
	 */
	public static String readCachedFile(Context ctx, String filename){
		try{
			// try to open file
			FileInputStream fIn = ctx.openFileInput(filename);
			DataInputStream in = new DataInputStream(fIn);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			// read lines of file
			String ret = "";
			String line;
			if( in != null ){
				while( (line = br.readLine()) != null ){
					ret += line;
				}
				return ret;
			}
			
		} catch(Exception e){}
		return null;
	}
	
	/**
	 * Writes data to a chached text file
	 * @param filename
	 */
	public static void writeChachedFile(Context ctx, String filename, String text){
		// write data to file
		FileOutputStream fos;
		try {
			fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(text.getBytes());
		} catch (FileNotFoundException e) {
		} catch (IOException e) {}
	}
	
}
