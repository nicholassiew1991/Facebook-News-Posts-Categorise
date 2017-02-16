package shared.utils;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

public class JsonUtils {

	public static JsonObject httpGetJsonObject(String url) {

		String response = HttpUtils.httpGet(url);

		if (response == null) {
			return null;
		}
		return getJsonObject(new StringReader(response));
	}

	public static JsonObject getJsonObject(Reader str) {
		JsonReader reader = Json.createReader(str);
		return reader.readObject();
	}

	public static JsonWriter getPrettyPrintJsonWriter(String path) {

		OutputStream os = null;

		try {
			HashMap<String, Object> properties = new HashMap<>(1);
			properties.put(JsonGenerator.PRETTY_PRINTING, true);
			os = new FileOutputStream(path);
			JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
			return writerFactory.createWriter(os, Charset.forName("UTF-8"));
			//return writerFactory.createWriter(os);
		} catch (FileNotFoundException ex) {
			System.err.println("File not found.");
			return null;
		}
	}
}
