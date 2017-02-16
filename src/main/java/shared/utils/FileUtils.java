package shared.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicholas on 10/7/2016.
 */
public class FileUtils {
	
	public static boolean isFileValid(String path) {
		return isFileValid(new File(path));
	}

	public static boolean isFileValid(File f) {
		return f.exists() && f.length() > 0;
	}

	public static List<String> readFileLine(String path) {
		return readFileLine(new File(path));
	}

	public static List<String> readFileLine(File f) {

		List<String> lines = new ArrayList<>();

		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String line = null;

			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static void initJsonFile(File f) {

		if (!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (f.length() <= 0) {
			JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
			JsonObject pageJsonObject = jsonObjectBuilder.build();
			JsonWriter jsonWriter = JsonUtils.getPrettyPrintJsonWriter(f.getPath());
			jsonWriter.writeObject(pageJsonObject);
			jsonWriter.close();
		}
	}
}
