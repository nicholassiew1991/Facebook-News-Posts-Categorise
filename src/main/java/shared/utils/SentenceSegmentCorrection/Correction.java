package shared.utils.SentenceSegmentCorrection;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.apache.commons.lang3.StringUtils;
import shared.models.news.NewsInfo;
import shared.models.news.categories.MainNewsCategoryEnum;
import shared.mongodb.SpringMongo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/11/14.
 */
public class Correction {
	public Words words = new Words(1000000);

	//不是詞的東西
	String[] sr = new String[]{"(", " ", ":", ",", ")", "、", "更新", "比", "天", "度", "分", "次", "就", "很", "我", "。」", "),", "/"
		, "發稿", "時間", "。", "在", "第", "是", "年", "但", "以", "我", "的", "要", "也", "但", "與", "被", "有", "希望", "今年"
		, "，", "說", "說--", "，", "今天", "：", "（", "）", "今", "對", "他", "月"};
	//紀錄上面那些不是詞的東西，特殊詞，下方程式會再提
	public ArrayList<String> spWord = new ArrayList<String>();
/*  debug 用
    public ArrayList<String> corWord = new ArrayList<String>();

*/

	public Correction() {
		//紀錄上面那些不是詞的東西，為了方便呼叫 indexOf 確認他有沒有在裡面
		for (int i = 0; i < sr.length; i++) {
			spWord.add(sr[i]);
		}


		SpringMongo sm = SpringMongo.getInstance();


		List<NewsInfo> n = sm.getNews(MainNewsCategoryEnum.POLITIC);


		JiebaSegmenter se = new JiebaSegmenter();

		int count = 0;


		for (NewsInfo news : n) {
			if (news.getContent() != null) {

				String str = news.getContent();
				str = StringUtils.substringBefore(str, "看了這");

				List<String> b = se.sentenceProcess(str);


				for (String str2 : b) {
					//  如果 詞不是文章最後一個     且     詞也不是特殊詞                     且  下一個詞也不是 特殊詞
					if (b.indexOf(str2) + 1 < b.size() && spWord.indexOf(str2) == -1 && spWord.indexOf(b.get(b.indexOf(str2) + 1)) == -1)
					//就把該詞和下一個詞給記錄下來
					{
						words.addNext(str2, b.get(b.indexOf(str2) + 1));
					}

				}

				count++;
			}

		}
		//debug  用
           /* for(int i=0;i<words.wordCount;i++){
                for(int j=0;j<words.words[i].nextCount;j++){
                    if(words.words[i].nextFrequency[j]>20){

                    System.out.println(words.words[i].word+"--"+words.words[i].nexts[j]);
                    corWord.add(words.words[i].word);


                    }


                }




            }*/


	}


	//矯正方法，給他結巴斷好的list  回傳矯正後的 list
	public List<String> cor(List<String> sr) {
		//存放矯正結果
		ArrayList<String> co = new ArrayList<String>();
		//存放要刪除的字
		String[] remove = new String[1000];
		int count = 0;
		for (String s : sr) {
			//如果 該詞不是list 最後一個     也不是特殊詞或新詞                           下個詞也不是特殊詞或新詞
			if (sr.indexOf(s) + 1 < sr.size() && words.wordIndex(s) != -1 && words.wordIndex(sr.get(sr.indexOf(s) + 1)) != -1
				//且 下一個詞也在  該詞 的 下一個詞的辭庫裡，也就是說它不是該詞的下一個詞的新詞
				&& words.words[words.wordIndex(s)].nextIndex(sr.get(sr.indexOf(s) + 1)) != -1) {

				System.out.println(words.words[words.wordIndex(s)].word);
				System.out.println(words.words[words.wordIndex(s)].nextIndex(sr.get(sr.indexOf(s) + 1)));
				//如果  該詞 和 下一個詞  這組合 已出現 20次以上(20能任意調)，把她合成新詞，然後把下個詞放到刪除名單
				if (words.words[words.wordIndex(s)].nextFrequency[words.words[words.wordIndex(s)].nextIndex(sr.get(sr.indexOf(s) + 1))] > 20) {


					String z = s + sr.get(sr.indexOf(s) + 1);
					remove[count] = sr.get(sr.indexOf(s) + 1);
					count++;
					co.add(z);
				}
				else {
					co.add(s);
				}


			}
			else {
				co.add(s);
			}
		}

		//刪除多出現的詞，應為下一個詞被合併 然後 又加入  會出現2次
		for (int i = 0; i < count; i++) {
			co.remove(remove[i]);
		}


		return co;
	}


}
