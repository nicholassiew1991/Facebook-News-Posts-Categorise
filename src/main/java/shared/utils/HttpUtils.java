package shared.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpUtils {

	public static String httpGet(String urlStr) {

		StringBuilder sb = new StringBuilder();

		try {

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			int responseCode = conn.getResponseCode();

			if (responseCode == 200) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}

				rd.close();
				conn.disconnect();
			}
			else {
				System.err.println(responseCode + ": " + conn.getResponseMessage());
				return null;
			}
		} catch (IOException e) {
			return null;
		}
		return sb.toString();
	}

	public static String httpPost(String urlStr, String[] paramName, String[] paramVal) {

		StringBuilder sb = new StringBuilder();

		try {

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			OutputStream out = conn.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");

			for (int i = 0; i < paramName.length; i++) {
				writer.write(paramName[i]);
				writer.write("=");
				writer.write(URLEncoder.encode(paramVal[i], "UTF-8"));
				writer.write("&");
			}

			writer.close();
			out.close();

			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}

			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();
			conn.disconnect();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
