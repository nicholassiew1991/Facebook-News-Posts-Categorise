package shared.models.tags;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

/**
 * Created by Nicholas on 20/10/2016.
 */
public class TagPoliticsEvents extends AbstractTag {

	@Id
	private String key;
	@Field
	private ArrayList<String> values = new ArrayList<>();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	public void addValue(String val) {
		this.values.add(val);
	}

	@Override
	public String toString() {
		return this.key + " : " + this.values;
	}
}
