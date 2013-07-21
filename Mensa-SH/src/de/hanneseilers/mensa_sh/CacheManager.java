package de.hanneseilers.mensa_sh;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
			
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
			long cacheHoldTime = Long.parseLong(( sharedPref.getString("CACHE_HOLD_TIME", "-1") )) * 60 * 60 * 100;
			
			// try to open file
			FileInputStream fin = ctx.openFileInput(filename);
			DataInputStream in = new DataInputStream(fin);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			File f = ctx.getFileStreamPath(filename);
			Long timeDiff = System.currentTimeMillis() - f.lastModified();
			
			// read lines of file
			String ret = "";
			String line;
			if( (in != null) && (timeDiff < cacheHoldTime) ){
				System.out.println("> loading cached file " + filename);
				while( (line = br.readLine()) != null ){
					ret += line;
				}
				return ret;
			}
			System.out.println("> not not use cached file " + filename);
			
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
