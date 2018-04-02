package dict.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class MyDirectory {

	List<MyFile> files;
	
	public MyDirectory() {
		files = new ArrayList<>();
	}
	
	public MyDirectory sort () {
		
		Collections.sort(files, new Comparator<MyFile>() {
			   public int compare(MyFile obj1, MyFile obj2) {
			      return obj1.getName().compareTo(obj2.getName());
			   }
			});
		
		return this;
	}
}
