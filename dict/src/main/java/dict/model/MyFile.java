package dict.model;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@ToString
public class MyFile {
	
	private String name;
	private long size;
	
	/**
	 * Checks if the name contains the word dictamen
	 * @return True if contains the word, false ioc
	 */
	public Boolean containsDict () {
		return name.toLowerCase().contains("dictamen");
	}
	
	/**
	 * Checks if the extension is pdf
	 * @return True if it is a pdf, false ioc
	 */
	public Boolean isPdf () {
		return name.toLowerCase().endsWith("pdf");
	}
	
	/**
	 * Checks if the extension is not a zip
	 * @return True if it is not a zip, false ioc
	 */
	public Boolean notAZip () {
		return !(name.toLowerCase().endsWith("zip"));
	}
	
	/**
	 * Checks if the file is hidden or not
	 * @return True if it is not a hidden file, false ioc
	 */
	public Boolean notHidden () {
		File file = new File(name);
		String justName = file.getName();
		return !(justName.toLowerCase().trim().startsWith("."));
	}
	
	/**
	 * Checks if the pdf contains any text and specifically the word folio
	 * @return True only if the file is a pdf and meets the conditions
	 */
	public Boolean containsText () {
		
		if (!isPdf()) return false;
		
		File file = new File(name);
		String text = "";
		try {
			
			PDDocument document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document).trim();
			document.close();
			
		} 	catch (InvalidPasswordException e) {
			e.printStackTrace();
		} 	catch (IOException e) {
			e.printStackTrace();
		}
		
		if (text.isEmpty()) {
			return false;
		}	else {
			if (!text.toLowerCase().contains("folio")) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the name meets every condition to be adictamination
	 * @return A logic and between all conditions
	 */
	public Boolean checkConditions () {
		return (containsDict() && isPdf() && notAZip() && containsText() && notHidden());
	}
}
