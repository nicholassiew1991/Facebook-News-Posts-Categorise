package shared.models.news.categories;

/**
 * Created by Nicholas on 30/8/2016.
 */
public enum MainNewsCategoryEnum {

	UNCATEGORISED(0), SPORT(1), ENTERTAINMENT(2), FINANCE(3), INTERNATIONAL(4), POLITIC(5), SOCIETY(6);

	private int value;

	MainNewsCategoryEnum(int i) {
		this.value = i;
	}

	public int getValue() {
		return this.value;
	}

	public static MainNewsCategoryEnum getEnumValue(String str) {
		try {
			return valueOf(str.toUpperCase());
		}
		catch (Exception e) {
			return null;
		}
	}
}

