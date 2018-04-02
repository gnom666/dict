package dict.model;

import java.util.ArrayList;
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
}
