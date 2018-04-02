package dict.divider;

import java.awt.image.ReplicateScaleFilter;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dict.model.MyFile;

public class Main {
	
	
	public static void main(String[] args) {
		
		String originalDir = "/Users/jorge.rios/Work/Corpus/descomp/";
		String dictDir = "/Users/jorge.rios/Work/Corpus/dictamination/";
		String scriptDir = "/Users/jorge.rios/Work/Corpus/script/";
		String dictZip = "/Users/jorge.rios/Work/Corpus/dictaminations.zip";
		String scriptZip = "/Users/jorge.rios/Work/Corpus/scripts.zip";
		
		String secondCorpusDir = "/Users/jorge.rios/Work/Corpus/descomp2/";
		String dictDir2 = "/Users/jorge.rios/Work/Corpus/dictamination2/";
		String scriptDir2 = "/Users/jorge.rios/Work/Corpus/script2/";
		String dictZip2 = "/Users/jorge.rios/Work/Corpus/dictaminations2.zip";
		String scriptZip2 = "/Users/jorge.rios/Work/Corpus/scripts2.zip";
		
		General gral = new General();
		Dictamination dict = new Dictamination();
		
		// create destination directories
		gral.createDirs(dictDir, scriptDir);
		gral.createDirs(dictDir2, scriptDir2);
		
		// make a first pass unzipping the zip files
		gral.displayDirectoryContents(new File(originalDir));
		gral.getDirectories().forEach(dir->{
			dir.getFiles().forEach(file->{
				if (!file.notAZip()) { // if its a zip
					gral.unZip(file.getName(), gral.extractDir(file.getName()));
				}
			});
		});
		
		// make a second pass classifying the files
		gral.getDirectories().clear();
		gral.displayDirectoryContents(new File(originalDir));
		gral.getDirectories().forEach(dir->{
			System.out.println("----------------------");
			
			// copy dictamination file (only one per dir)
			boolean oneCopied = false;
			String copiedName = "";
			for (MyFile file : dir.getFiles()) {
				if (file.checkConditions() && !oneCopied) {
					dict.fileToString(file).detectRegex("(^folio *:.*)").getName();
					gral.copyFile(file.getName(), dictDir + dict.getName());
					oneCopied = true;
					copiedName = dict.getName().replace(".pdf", "");
				}
			}
			
			// determine the biggest file in directory and copy it as the script file
			String biggest = gral.biggestFileInDir (dir);
			if (!biggest.trim().isEmpty() && !biggest.contains(".DS_Store")) {
				String newName = biggest.replace(gral.extractName(biggest), copiedName + "." + gral.extractExtension(biggest));
				gral.copyFile(biggest, scriptDir + gral.extractName(newName));
			}	else
				System.out.println("EMPTY DIR: " + dir);
		});
		
		
		// analize second corpus
		gral.getDirectories().clear();
		gral.displayDirectoryContents(new File(secondCorpusDir));
		System.out.println(gral.getDirectories());
		
		for (int i = 0; i < gral.getDirectories().get(0).getFiles().size()-1; i++) {
			
			System.out.println("----------------------");
			String stri = gral.getDirectories().get(0).getFiles().get(i).getName();
			String strii = gral.getDirectories().get(0).getFiles().get(i+1).getName();

			System.out.println(stri
							+ "\n" + strii
							+ "\n" + gral.areCouple(stri, strii));
			
			if (gral.areCouple(stri, strii)) {
				if (gral.docType(stri) == 1) {
					String name = dict.fileToString(stri).detectRegex("(^folio *:.*)").getName();
					gral.copyFile(stri, dictDir2 + name);
					gral.copyFile(strii, scriptDir2 + name.replace("pdf", gral.extractExtension(strii)));
				}
			}
				
			
		}
		
		
		// compress the created and filled directories
		gral.zipDirectory(dictDir, dictZip);
		gral.zipDirectory(scriptDir, scriptZip);
		gral.zipDirectory(dictDir2, dictZip2);
		gral.zipDirectory(scriptDir2, scriptZip2);
		
	}

}
