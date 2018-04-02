package dict.divider;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import dict.model.MyFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class to classify and rename Dictaminations
 * @author jorge.rios
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dictamination {
	
	private String text;
	private String name;

	
	/**
	 * Load the pdf file text
	 * @param path File path
	 * @return
	 */
	public Dictamination fileToString (String path) {
		
		File file = new File(path);
		try {
			
			PDDocument document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document);
			if (text.trim().isEmpty()) System.out.println("IMAGE PDF!!!");
			document.close();
			
		} 	catch (InvalidPasswordException e) {
			e.printStackTrace();
		} 	catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * Load the pdf file text
	 * @param path File path
	 * @return
	 */
	public Dictamination fileToString (MyFile myFile) {
		
		File file = new File(myFile.getName());
		try {
			
			PDDocument document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document);
			if (text.trim().isEmpty()) System.out.println("IMAGE PDF!!!");
			document.close();
			
		} 	catch (InvalidPasswordException e) {
			e.printStackTrace();
		} 	catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Find the regular expression and separate the :right part
	 * @param regex Regular expression 
	 * @return
	 */
	public Dictamination detectRegex (String regex) {
		//TODO: check if this is killing a mosquito with a bazooka 
		Arrays.asList(text.split("\n")).forEach(line->{
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(line.toLowerCase().trim());
			if (m.find() && line.contains(":")) {
				this.name = line.split(":")[1].trim().replaceAll("/", "_").concat(".pdf");
			}	
		});
		return this;
	}
	
	
	
	
}
