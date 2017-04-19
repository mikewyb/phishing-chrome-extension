package phishingDB.googleSafeBrowsingAPI;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Caller {
  private static final String ENDPOINT = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=";
  private static final String API_KEY = "AIzaSyD2du-55_V_AW-ombE9flymMm58e_Xaj4I";

  static String call(List<String> urls) {
    RequestBody requestBody = new RequestBody(urls);
    Gson gson = new Gson();
    String requestPayload = gson.toJson(requestBody);

    HttpClient httpClient = HttpClients.createDefault();
    try {
      HttpPost request = new HttpPost(ENDPOINT + API_KEY);
      StringEntity params = new StringEntity(requestPayload);
      request.addHeader("content-type", "application/json");
      request.addHeader("Accept","application/json");
      request.setEntity(params);
      HttpResponse response = httpClient.execute(request);
      HttpEntity entity = response.getEntity();

      String result = new BufferedReader(new InputStreamReader(entity.getContent()))
          .lines().collect(Collectors.joining("\n"));

      System.out.println(result);
      return result;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return "";
  }
}
