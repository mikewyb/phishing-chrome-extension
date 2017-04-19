package phishingDB.googleSafeBrowsingAPI;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CheckURLs {
  static List<String> checkURLs(List<String> urls) {
    String result = Caller.call(urls);

    Gson gson = new Gson();
    ResponseBody response = gson.fromJson(result, ResponseBody.class);
    return response.getUrls();
  }

//  public static void main(String[] args) {
//    List<String> urls = new ArrayList<>();
//    urls.add("https://www.google.com");
//    List<String> maliciousUrls = CheckURLs.checkURLs(urls);
//    for (String url : maliciousUrls) {
//      System.out.println(url);
//    }
//  }
}
