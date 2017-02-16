package shared.models.news.categories;

public enum SportNewsCategoryEnum {

	UNCATEGORISED(0), BASEBALL(1), BASKETBALL(2), OTHERS(3);

	private int value;

	SportNewsCategoryEnum(int i) {
		this.value = i;
	}

	public int getValue() {
		return this.value;
	}

	public static SportNewsCategoryEnum getEnumValue(String str) {
		try {
			return valueOf(str.toUpperCase());
		}
		catch (Exception e) {
			return null;
		}
	}
}
