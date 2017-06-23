package me.retrodaredevil.pokecreator.util.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author retro
 *
 */
public class SimpleZipFile {

	private static final boolean debug = true;
	
	private File file;
	private ZipFile zipFile;
	
	
	public SimpleZipFile(File file){
		this.file = file;
		try {
			this.zipFile = new ZipFile(file);
		} catch (IOException e) {
			System.out.println(file.getAbsolutePath());
			e.printStackTrace();
		}
		
	}
	
	
	public File getFile(){
		return file;
	}

	public ZipFile getZipFile(){
		return zipFile;
	}
	
	public String getContents(String path){
		return this.getContents(this.getZipEntry(path));
	}
	public ZipEntry getZipEntry(String path){
		return zipFile.getEntry(path);
	}
	@SuppressWarnings("resource")
	public String getContents(ZipEntry entry){
		
		
		InputStream stream;
		try{
			stream = this.getStream(entry);
		} catch(IOException e){
			if(debug){
				e.printStackTrace();
			}
			return null;
		}
		Scanner s = new Scanner(stream);
		
		return s.useDelimiter("\\A").next();
		
	}
	public InputStream getStream(ZipEntry entry) throws IOException{
	
		return this.zipFile.getInputStream(entry);
	}
	
	
	@SuppressWarnings("resource")
	public void write(String path, String toWrite){
		
		
		ZipEntry en = new ZipEntry(path);
		
		ZipOutputStream out;
		try {
			out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(this.file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
//		System.out.println(file.getAbsolutePath());
//		System.out.println(en.getName());

		try {
			out.putNextEntry(en);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		byte[] data = toWrite.getBytes();
		
		
		try {
			//out.write(data, 0, data.length);
			out.write(data);
			out.closeEntry();
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
		
		
		
		
	
}
