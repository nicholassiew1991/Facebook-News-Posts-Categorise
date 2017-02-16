package shared.models.news.categories;

/**
 * Created by Nicholas on 29/9/2016.
 */
public enum SocietyNewsCategoryEnum {

	UNCATEGORISED(0), DISASTER(1), CRIME(2), ACCIDENT(3), TRAFFIC(4), WARMTH(5), FAMILY(6), POLICE(7), OTHERS(8);

	private int value;

	SocietyNewsCategoryEnum(int i) {
		this.value = i;
	}

	public int getValue() {
		return this.value;
	}

	public static SocietyNewsCategoryEnum getEnumValue(String str) {
		try {
			return valueOf(str.toUpperCase());
		}
		catch (Exception e) {
			return null;
		}
	}
}
