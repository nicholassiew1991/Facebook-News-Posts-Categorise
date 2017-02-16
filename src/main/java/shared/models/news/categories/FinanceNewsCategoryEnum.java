package shared.models.news.categories;

/**
 * Created by Nicholas on 29/9/2016.
 */
public enum FinanceNewsCategoryEnum {

	UNCATEGORISED(0), STOCK(1), ESTATE(2), INDUSTRY(3), EMPLOYMENT(4), OTHERS(5);

	private int value;

	FinanceNewsCategoryEnum(int i) {
		this.value = i;
	}

	public int getValue() {
		return this.value;
	}

	public static FinanceNewsCategoryEnum getEnumValue(String str) {
		try {
			return valueOf(str.toUpperCase());
		}
		catch (Exception e) {
			return null;
		}
	}
}
