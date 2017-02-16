package shared.models.news.categories;

public enum EntertainmentNewsCategoryEnum {

	UNCATEGORISED(0), CHINA(1), KOREA(2), OCCIDENT(3);

	private int value;

	EntertainmentNewsCategoryEnum(int i) {
		this.value = i;
	}

	public int getValue() {
		return this.value;
	}

	public static EntertainmentNewsCategoryEnum getEnumValue(String str) {
		try {
			return valueOf(str.toUpperCase());
		}
		catch (Exception e) {
			return null;
		}
	}
}
