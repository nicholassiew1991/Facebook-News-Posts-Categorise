package Apps.NewsAnalysis.WeightsDict;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: Need a better design
public class WordWeightsDict {

	private static WordWeightsDict instance = null;

	private final static String dictDirectory = "src//main//resources//WordsWeightsDict//";

	private static final String[] dicts = new String[] {
		"entertainment.txt",  "entertainment_china.txt", "entertainment_korea.txt", "entertainment_occident.txt",
		"finance.txt", "finance_employment.txt", "finance_estate.txt", "finance_industry.txt", "finance_others.txt", "finance_stock.txt",
		"international.txt", "international_asia.txt", "international_oceania.txt", "international_america.txt", "international_europe.txt",
		"politic.txt", "society.txt", "sport.txt",
		"sport_baseball.txt", "sport_basketball.txt", "sport_competitions.txt", "sport_others.txt",
		"society_accident.txt", "society_crime.txt", "society_disaster.txt", "society_family.txt", "society_police.txt", "society_traffic.txt", "society_warm.txt"
	};

	public HashMap<String, HashMap<String, Integer>> allDicts = new HashMap<>();

	public HashMap<String, Integer> entertainment = new HashMap<>();
	public HashMap<String, Integer> entertainmentChina = new HashMap<>();
	public HashMap<String, Integer> entertainmentKorea = new HashMap<>();
	public HashMap<String, Integer> entertainmentOccident = new HashMap<>();

	public HashMap<String, Integer> finance = new HashMap<>();
	public HashMap<String, Integer> financeStock = new HashMap<>();
	public HashMap<String, Integer> financeEstate = new HashMap<>();
	public HashMap<String, Integer> financeIndustry = new HashMap<>();
	public HashMap<String, Integer> financeEmployment = new HashMap<>();
	public HashMap<String, Integer> financeOthers = new HashMap<>();

	public HashMap<String, Integer> international = new HashMap<>();
	public HashMap<String, Integer> internationalAsia = new HashMap<>();
	public HashMap<String, Integer> internationalOceania = new HashMap<>();
	public HashMap<String, Integer> internationalAmerica = new HashMap<>();
	public HashMap<String, Integer> internationalEurope = new HashMap<>();

	public HashMap<String, Integer> politics = new HashMap<>();

	public HashMap<String, Integer> society = new HashMap<>();
	public HashMap<String, Integer> societyDisaster = new HashMap<>();
	public HashMap<String, Integer> societyCrime = new HashMap<>();
	public HashMap<String, Integer> societyAccident = new HashMap<>();
	public HashMap<String, Integer> societyTraffic = new HashMap<>();
	public HashMap<String, Integer> societyWarmth = new HashMap<>();
	public HashMap<String, Integer> societyFamily = new HashMap<>();
	public HashMap<String, Integer> societyPolice = new HashMap<>();
	public HashMap<String, Integer> societyOthers = new HashMap<>();


	public HashMap<String, Integer> sport = new HashMap<>();
	public HashMap<String, Integer> sportBaseBall = new HashMap<>();
	public HashMap<String, Integer> sportBasketBall = new HashMap<>();
	public HashMap<String, Integer> sportCompetitions = new HashMap<>();
	public HashMap<String, Integer> sportOthers = new HashMap<>();

	private WordWeightsDict() {
		loadAllDict();
		//loadAllDict2();
	}

	public static WordWeightsDict getInstance() {
		if (instance == null) {
			instance = new WordWeightsDict();
		}
		return instance;
	}

	private HashMap<String, ArrayList<HashMap<String, Integer>>> allDict = new HashMap<>();

	private void loadAllDict() {

		for (String dict : dicts) {

			if (dict.equalsIgnoreCase("entertainment.txt")) {
				loadDict(dict, entertainment);
			}
			else if (dict.equalsIgnoreCase("entertainment_china.txt")) {
				loadDict(dict, entertainmentChina);
			}
			else if (dict.equalsIgnoreCase("entertainment_korea.txt")) {
				loadDict(dict, entertainmentKorea);
			}
			else if (dict.equalsIgnoreCase("entertainment_occident.txt")) {
				loadDict(dict, entertainmentOccident);
			}
			else if (dict.equalsIgnoreCase("finance.txt")) {
				loadDict(dict, finance);
			}
			else if (dict.equalsIgnoreCase("finance_employment.txt")) {
				loadDict(dict, financeEmployment);
			}
			else if (dict.equalsIgnoreCase("finance_estate.txt")) {
				loadDict(dict, financeEstate);
			}
			else if (dict.equalsIgnoreCase("finance_industry.txt")) {
				loadDict(dict, financeIndustry);
			}
			else if (dict.equalsIgnoreCase("finance_others.txt")) {
				loadDict(dict, financeOthers);
			}
			else if (dict.equalsIgnoreCase("finance_stock.txt")) {
				loadDict(dict, financeStock);
			}
			else if (dict.equalsIgnoreCase("international.txt")) {
				loadDict(dict, international);
			}
			else if (dict.equalsIgnoreCase("international_asia.txt")) {
				loadDict(dict, internationalAsia);
			}
			else if (dict.equalsIgnoreCase("international_oceania.txt")) {
				loadDict(dict, internationalOceania);
			}
			else if (dict.equalsIgnoreCase("international_america.txt")) {
				loadDict(dict, internationalAmerica);
			}
			else if (dict.equalsIgnoreCase("international_europe.txt")) {
				loadDict(dict, internationalEurope);
			}
			else if (dict.equalsIgnoreCase("politic.txt")) {
				loadDict(dict, politics);
			}
			else if (dict.equalsIgnoreCase("society.txt")) {
				loadDict(dict, society);
			}
			else if (dict.equalsIgnoreCase("society_accident.txt")) {
				loadDict(dict, societyAccident);
			}
			else if (dict.equalsIgnoreCase("society_crime.txt")) {
				loadDict(dict, societyCrime);
			}
			else if (dict.equalsIgnoreCase("society_disaster.txt")) {
				loadDict(dict, societyDisaster);
			}
			else if (dict.equalsIgnoreCase("society_family.txt")) {
				loadDict(dict, societyFamily);
			}
			else if (dict.equalsIgnoreCase("society_police.txt")) {
				loadDict(dict, societyPolice);
			}
			else if (dict.equalsIgnoreCase("society_traffic.txt")) {
				loadDict(dict, societyTraffic);
			}
			else if (dict.equalsIgnoreCase("society_warm.txt")) {
				loadDict(dict, societyWarmth);
			}
			else if (dict.equalsIgnoreCase("sport.txt")) {
				loadDict(dict, sport);
			}
			else if (dict.equalsIgnoreCase("sport_baseball.txt")) {
				loadDict(dict, sportBaseBall);
			}
			else if (dict.equalsIgnoreCase("sport_basketball.txt")) {
				loadDict(dict, sportBasketBall);
			}
			else if (dict.equalsIgnoreCase("sport_competitions.txt")) {
				loadDict(dict, sportCompetitions);
			}
			else if (dict.equalsIgnoreCase("sport_others.txt")) {
				loadDict(dict, sportOthers);
			}
		}
	}

	private void loadDict(String fileName, HashMap<String, Integer> hm) {

		try {
			FileReader fr = new FileReader(dictDirectory + fileName);
			BufferedReader br = new BufferedReader(fr);

			String line = null;
			while ((line = br.readLine()) != null) {
				String newLine = line.trim().replaceAll(" +", " ");

				if (newLine.length() > 0) {
					String[] splited = newLine.split(" ");

					String word = splited[0];

					if (splited.length > 2) {

						StringBuilder sb = new StringBuilder();

						int end = splited.length - 2;

						for (int i = 0; i < end; i++) {
							sb.append(splited[i]);

							if (i != (end - 1)) {
								sb.append(" ");
							}
						}
						word = sb.toString();
					}
					hm.put(word, Integer.parseInt(splited[splited.length - 1]));
				}
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
