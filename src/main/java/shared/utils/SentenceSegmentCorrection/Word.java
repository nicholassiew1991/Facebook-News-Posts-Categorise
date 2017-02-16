package shared.utils.SentenceSegmentCorrection;

/**
 * Created by user on 2016/10/27.
 */
public class Word {
	public String word;//一個詞
	public String[] nexts = new String[1000];  //記錄他連接的下一個詞
	public int[] nextFrequency = new int[1000];//記錄他連接的下 個詞的頻率
	public int nextCount = 0;//紀錄上面的陣列用到哪裡


	public boolean equals(String word) {
		if (this.word.equals(word)) {
			return true;
		}
		else {
			return false;
		}

	}

	public Word(String word) {
		this.word = word;
	}

	//把下一個詞加入，如果是新的給他陣列新位子，是舊的就頻率加1
	public void addNext(String next) {
		if (nextIsNew(next)) {
			nexts[nextCount] = next;
			nextFrequency[nextCount]++;
			nextCount++;

		}
		else {
			nextFrequency[nextIndex(next)]++;
		}

	}

	//檢查下一個詞是否是新的
	public boolean nextIsNew(String next) {
		for (int i = 0; i < nextCount; i++) {
			if (nexts[i].equals(next)) {
				return false;
			}
		}
		return true;
	}


	public int nextIndex(String next) {
		for (int i = 0; i < nextCount; i++) {
			if (nexts[i].equals(next)) {
				return i;
			}
		}
		return -1;

	}


}

