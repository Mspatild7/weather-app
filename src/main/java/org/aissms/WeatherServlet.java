package org.aissms;
import org.json.*;
import javax.servlet.ServletException;
	
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


public class WeatherServlet extends HttpServlet {

	static String step1 = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%s&appid=%s";
	static String step2 = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
	static String apikey = "f40c476ba641c34571c222dae153cc10";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		String city = req.getPathInfo();
		city = city.substring(1, city.length());
		String response = getWeatherData(city);
		out.println(response);
		out.flush();
	}

	private String getWeatherData(String city) throws IOException {
		long start = System.nanoTime();
		String coData = getCoordinates(city);
		long end = System.nanoTime();
		long diff = (end-start)/1000000;
		System.out.println("Step1 Time:"+diff+"ms");
		String lat = getValue(coData, "lat");
		String lon = getValue (coData, "lon");
		String url = String.format(step2, lat, lon, apikey);
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
		String result = getResponse(request);
		diff = (System.nanoTime()-end)/1000000;
		System.out.println("Step2 Time:"+diff+"ms");
		return result;
	}
		// get coordinates
	private String getCoordinates (String city) throws IOException {
		String url = String.format(step1, city, 1, apikey);
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
		return getResponse(request);
	}

	private String getResponse (HttpRequest request) throws IOException {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			return response.body();
		} catch (InterruptedException e) {
			return null;
		}
	}
	private String getValue (String json1, String ky) {
		
		int len = cd.length()-1;
        String s = cd.substring(1,len);
        JSONObject json =  new JSONObject(s);
            return json.get(ky).toString();

	}
}