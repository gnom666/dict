package dict.divider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import dict.model.MyDirectory;
import dict.model.MyFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * General functions to manipulate directories and files
 * @author jorge.rios
 *
 */
public class General {
	
	private List<MyDirectory> directories;
	
	public General () {
		directories = new ArrayList<>();
	}
	
	/**
	 * Displays directory content recursively
	 * @param dir Root directory
	 * @return
	 */
	public General displayDirectoryContents(File dir) {
		
		File[] filesDetected = dir.listFiles();
		MyDirectory currentDir = new MyDirectory();
		for (File file : filesDetected) {
			
			if (file.isDirectory()) {
				//System.out.println("directory:" + file.getCanonicalPath());
				displayDirectoryContents(file);
			} 	else {
				//System.out.println("     file:" + file.getCanonicalPath());
				if (!file.getAbsolutePath().contains(".DS_Store")) {
					MyFile myFile = new MyFile();
					myFile.setName(file.getAbsolutePath());
					myFile.setSize(sizeInMB(file));
					currentDir.getFiles().add(myFile);
				}
			}
		}
		directories.add(currentDir);
		
		return this;
	}

	/**
	 * Gets the size of the file in Bytes
	 * @param file
	 * @return
	 */
	public long sizeInMB(File file) {
		long bytes = file.length();
		//long kilobytes = (bytes / 1024);
		//long megabytes = (kilobytes / 1024);
		return bytes;
	}
	
	/**
	 * Creates the necessary directories
	 */
	public void createDirs (String dictDir, String scriptDir) {
		
		File file = new File(dictDir);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory " + dictDir + " is created!");
            } else {
                System.out.println("Failed to create directory " + dictDir + "!");
            }
        }
        
        file = new File(scriptDir);
        if (!file.exists()) {
            if (file.mkdir()) {
            	 System.out.println("Directory " + scriptDir + " is created!");
            } else {
                System.out.println("Failed to create directory " + scriptDir + "!");
            }
        }
	}
	
	/**
	 * Copies a file
	 * @param source Source file
	 * @param dest Destination file
	 */
	public void copyFile (File source, File dest) {
	    InputStream is = null;
	    OutputStream os = null;
	     
        try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
	        byte[] buffer = new byte[2048];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
        } 	catch (FileNotFoundException e) {
			e.printStackTrace();
		} 	catch (IOException e) {
			e.printStackTrace();
		}	finally {
			try {
				is.close();
				os.close();
			} 	catch (IOException e) {
				e.printStackTrace();
			}
		}
	    	
	}
	
	/**
	 * Copies a file
	 * @param sourcePath Source path
	 * @param destPath Destination path
	 */
	public void copyFile (String sourcePath, String destPath) {
	    InputStream is = null;
	    OutputStream os = null;
	    
	    System.out.println(sourcePath + " --> " + destPath);
	    
	    File source = new File(sourcePath);
	    File dest = new File(destPath);
	    
	    /*int copy = 1;
	    while (dest.exists()) {
	    		copy++;
	    		String extension = extractExtension(destPath);
	    		dest = new File(destPath.replace("." + extension, "_" + copy + "." + extension));
	    }*/
	     
        try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
	        byte[] buffer = new byte[2048];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
        } 	catch (FileNotFoundException e) {
			e.printStackTrace();
		} 	catch (IOException e) {
			e.printStackTrace();
		}	finally {
			try {
				is.close();
				os.close();
			} 	catch (IOException e) {
				e.printStackTrace();
			}
		}
	    	
	}

	/**
	 * Gets the name and path of the biggest file in the directory
	 * @param dir Directory
	 * @return Full file path
	 */
	public String biggestFileInDir (MyDirectory dir) {
		
		String biggest = "";
		long biggestSize = 0;
		
		for (MyFile file : dir.getFiles()) {
			if (!file.checkConditions() && file.notHidden() && file.getSize() > biggestSize) {
				biggest = file.getName();
				biggestSize = file.getSize();
			}
		}
		
		return biggest;
	}

	/**
	 * Unzips a file in the given directory
	 * @param zipFilePath The path of the zip file
	 * @param destDir destination directory
	 */
	public void unZip (String zipFilePath, String destDir) {
        
		File dir = new File(destDir);
        
		// create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        
        //buffer for read and write data to file
        byte[] buffer = new byte[2048];
        try {
            fis = new FileInputStream (zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                
                //create directories for sub directories in zip
                new File (newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                		fos.write(buffer, 0, len);
                }
                fos.close();
                
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } 	catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	
	/**
	 * Compress a directory into a zip file
	 * @param dirPath Directory to be compressed
	 * @param filePath Compressed zip file
	 */
    public  void zipDirectory(String dirPath, String filePath) {
        String sourceFile = dirPath;
        FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath);
			ZipOutputStream zipOut = new ZipOutputStream(fos);
	        File fileToZip = new File(sourceFile);
	 
	        zipFile(fileToZip, fileToZip.getName(), zipOut);
	        
	        zipOut.close();
	        fos.close();
	        
		} 	catch (FileNotFoundException e) {
			e.printStackTrace();
		} 	catch (IOException e) {
			e.printStackTrace();
		}
        
    }
 
    /**
     * Compress a file
     * @param fileToZip File to compress
     * @param fileName File name
     * @param zipOut Output stream
     */
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + File.separator + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis;
		try {
			fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileName);
	        zipOut.putNextEntry(zipEntry);
	        byte[] bytes = new byte[2048];
	        int length;
	        while ((length = fis.read(bytes)) >= 0) {
	            zipOut.write(bytes, 0, length);
	        }
	        fis.close();
		} 	catch (FileNotFoundException e) {
			e.printStackTrace();
		} 	catch (IOException e) {
			e.printStackTrace();
		}
        
    }
	
	/**
	 * Extracts the name of a file given the file path
	 * @param filePath File path
	 * @return Name of the file
	 */
	public String extractName (String filePath) {
		File f = new File(filePath);
		return f.getName();
	}
	
	/**
	 * Extracts the directory of a file given the file path
	 * @param filePath File path
	 * @return Directory of the file
	 */
	public String extractDir (String filePath) {
		File file = new File(filePath);
	    if(file.exists()){
	    		return file.getAbsoluteFile().getParent();
	    }
	    return "";
	}
	
	/**
	 * Extracts the extension of a file
	 * @param filePath File path
	 * @return Extension of the file
	 */
	public String extractExtension (String filePath) {
		String fileName = new File(filePath).getName();
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
	}
	
	
	/**
	 * Finds the minimum value
	 * @param a Value 1
	 * @param b Value 2
	 * @param c Value 3
	 * @return
	 */
    private int minimum(int a, int b, int c) {
         return Math.min(a, Math.min(b, c));
    }

    /**
     * Computes the Levenshtein distance
     * @param str1 String 1
     * @param str2 String 2
     * @return Int value of distance
     */
    public int computeLevenshteinDistance(String str1, String str2) {
        return computeLevenshteinDistance(str1.toCharArray(),
                                          str2.toCharArray());
    }

    /**
     * Computes the Levenshtein distance
     * @param str1 String 1
     * @param str2 String 2
     * @return Int value of distance
     */
    private int computeLevenshteinDistance(char [] str1, char [] str2) {
        int [][]distance = new int[str1.length+1][str2.length+1];

        for(int i=0;i<=str1.length;i++){
                distance[i][0]=i;
        }
        for(int j=0;j<=str2.length;j++){
                distance[0][j]=j;
        }
        for(int i=1;i<=str1.length;i++){
            for(int j=1;j<=str2.length;j++){ 
                  distance[i][j]= minimum(distance[i-1][j]+1,
                                        distance[i][j-1]+1,
                                        distance[i-1][j-1]+
                                        ((str1[i-1]==str2[j-1])?0:1));
            }
        }
        return distance[str1.length][str2.length];
        
    }
	
    /**
     * Determines whether or not two path strings are similar according our characteristics
     * @param str1 Path string 1
     * @param str2 Path string 1
     * @return If similar or not
     */
    public boolean areSimilar (String str1, String str2) {
    		
    		Pattern p = Pattern.compile("\\(DOCUME.*\\)");
		Matcher m = p.matcher(str1.replace(" ", "").trim());
		if (m.find()) {
			str1 = str1.replaceAll("\\(DOCUME.*\\)", "").replace(extractExtension(str1), "");
		}
		
		p = Pattern.compile("\\(DICT.*\\)");
		m = p.matcher(str1.replace(" ", "").trim());
		if (m.find()) {
			str1 = str1.replaceAll("\\(DICT.*\\)", "").replace(extractExtension(str1), "");
		}
		
		p = Pattern.compile("\\(DOCUME.*\\)");
		m = p.matcher(str2.replace(" ", "").trim());
		if (m.find()) {
			str2 = str2.replaceAll("\\(DOCUME.*\\)", "").replace(extractExtension(str2), "");
		}
		
		p = Pattern.compile("\\(DICT.*\\)");
		m = p.matcher(str2.replace(" ", "").trim());
		if (m.find()) {
			str2 = str2.replaceAll("\\(DICT.*\\)", "").replace(extractExtension(str2), "");
		}
		
		if (str1.toLowerCase().replace(" ", "").equals(str2.toLowerCase().replace(" ", ""))) 
			return true;
    		
    		return false;
    }
    
    /**
     * Determines the type of the document
     * @param file File path
     * @return 1=DICT 2=DOCU 0=OTHER
     */
    public int docType (String file) {
    		// 1=DICT 2=DOCU 0=OTHER
    		
    		Pattern p = Pattern.compile("\\(DICT.*\\)");
		Matcher m = p.matcher(file.replace(" ", "").trim());
		if (m.find()) {
			return 1;
		}
		
		p = Pattern.compile("\\(DOCU.*\\)");
		m = p.matcher(file.replace(" ", "").trim());
		if (m.find()) {
			return 2;
		}
		
		return 0;
    }
    
    /**
     * Determines whether or not two file paths are couple (DICT-DOCU)
     * @param str1 File path 1
     * @param str2 File path 2
     * @return True if they are type 1,2 or 2,1 and have the same prefix 
     */
    public boolean areCouple (String str1, String str2) {
    		int typeStr1 = docType(str1);
    		int typeStr2 = docType(str2);
    		
    		if ((typeStr1 == 1 && typeStr2 == 2 || typeStr1 == 2 && typeStr2 == 1) && areSimilar(str1, str2))
    			return true;
    		return false;
    }
	
}
