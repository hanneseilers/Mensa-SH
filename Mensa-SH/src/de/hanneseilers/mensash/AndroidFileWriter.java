package de.hanneseilers.mensash;

import de.mensa.sh.core.FileWriterInterface;

public class AndroidFileWriter implements FileWriterInterface {
	
	public AndroidFileWriter() {}

	@Override
	public boolean canRead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canWrite() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exsist() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long lastModified() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String readFromFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFile(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean writeToFile(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
