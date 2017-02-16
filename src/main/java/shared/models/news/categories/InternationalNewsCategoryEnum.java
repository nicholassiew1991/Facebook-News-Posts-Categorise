package shared.models.news.categories;

/**
 * Created by Nicholas on 29/9/2016.
 */
public enum InternationalNewsCategoryEnum {

	UNCATEGORISED(0), ASIA(1), OCEANIA(2), AMERICA(3), EUROPE(4);

	private int value;

	InternationalNewsCategoryEnum(int i) {
		this.value = i;
	}

	public int getValue() {
		return this.value;
	}

	public static InternationalNewsCategoryEnum getEnumValue(String str) {
		try {
			return valueOf(str.toUpperCase());
		}
		catch (Exception e) {
			return null;
		}
	}
}
