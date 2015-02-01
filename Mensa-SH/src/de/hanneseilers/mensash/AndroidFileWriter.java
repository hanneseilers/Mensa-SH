package de.hanneseilers.mensash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import de.mensa.sh.core.FileWriterInterface;

public class AndroidFileWriter implements FileWriterInterface {
	
	private File mFile = null;
	
	public AndroidFileWriter() {}
	
	@Override
	public void setFile(String aFile){
		mFile = new File( MainActivity.getInstance().getFilesDir(), aFile );
	}
	
	@Override
	public String readFromFile(){
		if( exsist() && isFile() && canRead() ){
			try {
				
				BufferedReader vReader = new BufferedReader( new FileReader(mFile) );
				String vFileText = "";
				String vLine;
				while( (vLine = vReader.readLine()) != null ){
					vFileText += vLine;
				}
				
				vReader.close();
				System.out.println("read from cache file " + getName());
				return vFileText;
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	@Override
	public boolean writeToFile(String aText){
		try {
			
			mFile.createNewFile();
			if( exsist() && isFile() && canWrite() ){
				
					OutputStreamWriter vWriter = new OutputStreamWriter( new FileOutputStream(mFile) );
					vWriter.write(aText);
					vWriter.close();
					System.out.println("write to cache file " + getName());
					return true;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public long lastModified(){
		if( mFile != null ){
			return mFile.lastModified();
		}
		
		return 0L;
	}
	
	@Override
	public boolean delete(){
		if( mFile != null ){
			return mFile.delete();
		}
		
		return false;
	}
	
	@Override
	public boolean exsist(){
		return mFile != null && mFile.exists();
	}
	
	@Override
	public boolean isFile(){
		return mFile != null && mFile.isFile();
	}
	
	@Override
	public boolean canWrite(){
		return mFile != null && mFile.canWrite();
	}
	
	@Override
	public boolean canRead(){
		return mFile != null && mFile.canRead();
	}
	
	@Override
	public String getPath(){
		return mFile != null ? mFile.getPath() : "";
	}
	
	@Override
	public String getName(){
		return mFile != null ? mFile.getName() : "";
	}
	
	
}
