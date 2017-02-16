package shared.utils.SentenceSegmentCorrection;

/**
 * Created by user on 2016/10/27.
 */
public class Words {

	public Word[] words;
	public int wordCount = 0;//拿來紀錄上面的陣列 用了多少

	public Words(int wordAmount) {
		words = new Word[wordAmount];
	}

	//檢查是否是新詞
	public boolean wordIsNew(String word) {
		for (int i = 0; i < wordCount; i++) {
			if (words[i].equals(word)) {
				return false;
			}
		}
		return true;
	}

	public int wordIndex(String word) {
		for (int i = 0; i < wordCount; i++) {
			if (words[i].equals(word)) {
				return i;
			}
		}
		return -1;
	}

	//將該詞和該詞的下一個詞紀錄下來
	public void addNext(String word, String next) {
		if (wordIsNew(word)) {
			words[wordCount] = new Word(word);
			words[wordCount].addNext(next);
			wordCount++;
		}
		else {

			words[wordIndex(word)].addNext(next);


		}

	}

	;


}
