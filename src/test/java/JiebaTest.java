import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import junit.framework.TestCase;
import org.junit.Test;
import shared.Resources;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Nicholas on 22/10/2016.
 */
public class JiebaTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		WordDictionary wd = WordDictionary.getInstance();
		Path customDict = Paths.get(Resources.RESOURCE_PATH + "my_dict.txt");
		wd.loadUserDict(customDict);
	}

	@Test
	public void testSegmenter() {

		String str = "總統蔡英文多次表達同性戀有權結婚，但始終未對修《民法》或立專法表態。在支持立專法的反同團體與主張修《民法》的挺同團體接連在2個周六動員上街表達訴求後，蔡英文11日發表3點呼籲，希望雙方冷靜，透過更多的對話與相互理解，尋求解決之道，不要把對方當仇敵對待。";

		JiebaSegmenter js = new JiebaSegmenter();

		System.out.println(js.sentenceProcess(str));
		//System.out.println(js.process(str, JiebaSegmenter.SegMode.INDEX).toString());
		//System.out.println(js.process(str, JiebaSegmenter.SegMode.SEARCH).toString());

		/*ArrayList<String> s = new ArrayList<>();
		s.add("a");

		System.out.println(s.indexOf("a"));*/
	}
}
