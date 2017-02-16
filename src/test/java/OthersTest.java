import org.junit.Test;
import shared.models.tags.TagPoliticsEvents;
import shared.models.tags.TagPoliticsPeoples;
import shared.models.tags.TagPoliticsTerms;
import shared.mongodb.MongoNewsTagDict;
import shared.utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nicholas on 14/10/2016.
 */
public class OthersTest {

	@Test
	public void test() {
		/*WordDictionary wd = WordDictionary.getInstance();
		Path customDict = Paths.get(Resources.RESOURCE_PATH + "my_dict.txt");
		wd.loadUserDict(customDict);
		JiebaSegmenter js = new JiebaSegmenter();
		System.out.println(js.sentenceProcess("92共識是什麼東西？"));
		System.out.println(js.sentenceProcess("九二共識是什麼東西？"));*/

		/*System.out.println(MongoNewsTagDict.getInstance());
		System.out.println(MongoTest.getInstance());*/
		HashMap<String, Integer> hm = new HashMap<>();
		/*hm.put("a", 1);
		hm.put("bc", 2);

		System.out.println(hm.containsKey("a"));
		System.out.println(hm.containsKey("b"));
		System.out.println(hm.get("a"));
		System.out.println(hm.get("b"));
		System.out.println(hm.size());*/

		Correction c = new Correction();
		ArrayList<String> cc = new ArrayList<>();
		cc.addAll(Arrays.asList(c.sr));
		System.out.println(cc);
		System.out.println(c.sr.length);
		System.out.println(cc.size());
	}

	@Test
	public void readPoliticsEvents() {

		List<String> f = FileUtils.readFileLine("src//main//resources//NewsTagsDict//politics_events.txt");
		ArrayList<TagPoliticsEvents> e = new ArrayList<>();

		for (String ff : f) {
			String[] str = ff.split("-");
			String[] vals = str[1].split(",");

			TagPoliticsEvents t = new TagPoliticsEvents();
			t.setKey(str[0]);
			t.addValue(t.getKey());
			for (String val : vals) {
				t.addValue(val);
			}

			e.add(t);
		}

		MongoNewsTagDict mntd = MongoNewsTagDict.getInstance();
		mntd.savePoliticsEventsTag(e);
	}

	@Test
	public void readPoliticsPeoples() {

		List<String> f = FileUtils.readFileLine("src//main//resources//NewsTagsDict//politics_peoples.txt");
		ArrayList<TagPoliticsPeoples> e = new ArrayList<>();

		for (String ff : f) {
			String[] aa = ff.split("=");
			TagPoliticsPeoples t = new TagPoliticsPeoples();

			t.setKey(aa[0]);

			for (int i = 0; i < aa.length; i++) {
				t.addValue(aa[i]);
			}
			e.add(t);
		}

		System.out.println(e.get(0).getClass());

		MongoNewsTagDict mntd = MongoNewsTagDict.getInstance();
		mntd.savePoliticsPeoplesTag(e);
	}

	@Test
	public void readPoliticsTerms() {

		List<String> f = FileUtils.readFileLine("src//main//resources//NewsTagsDict//politics_term.txt");
		ArrayList<TagPoliticsTerms> e = new ArrayList<>();

		for (String ff : f) {
			TagPoliticsTerms t = new TagPoliticsTerms();

			t.setKey(ff);
			t.addValue(ff);
			e.add(t);
		}

		MongoNewsTagDict mntd = MongoNewsTagDict.getInstance();
		mntd.savePoliticsTermsTag(e);
	}
}
