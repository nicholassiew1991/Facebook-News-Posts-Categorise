package shared.models.facebook;

import java.util.HashMap;

/**
 * Created by Nicholas Siew on 13/4/2016.
 */
public class Reaction {

	public enum Type {NONE, LIKE, LOVE, WOW, HAHA, SAD, ANGRY, THANKFUL}

	private HashMap<Type, Long> reactionData;

	private void initHashMap() {

		reactionData = new HashMap<Type, Long>();

		for (Type t : Type.values()) {
			reactionData.put(t, (long) 0);
		}
	}

	public Reaction() {
		initHashMap();
	}

	public Reaction(Reaction re) {
		this.reactionData = re.getReactionData();
	}

	public void setReactionData(HashMap<Type, Long> data) {
		this.reactionData = data;
	}

	public HashMap<Type, Long> getReactionData() {
		return new HashMap<Type, Long>(reactionData);
	}

	@Override
	public String toString() {
		return reactionData.toString();
	}
}
