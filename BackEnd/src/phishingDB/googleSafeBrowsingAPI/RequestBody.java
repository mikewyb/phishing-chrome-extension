package phishingDB.googleSafeBrowsingAPI;

import java.util.ArrayList;
import java.util.List;

public class RequestBody {
  class Client {
    public String clientId = "PCS_project";
    public String clientVersion = "0.1";
  }

  class ThreatInfo {
    class Url {
      public String url;
      public Url(String url) {
        this.url = url;
      }
    }

    public String[] threatTypes = {"MALWARE", "SOCIAL_ENGINEERING"};
    public String[] platformTypes = {"WINDOWS"};
    public String[] threatEntryTypes = {"URL"};
    public List<Url> threatEntries = new ArrayList<>();

    public ThreatInfo(List<String> urls) {
      for (String url : urls) {
        threatEntries.add(new Url(url));
      }
    }
  }

  public Client client;
  public ThreatInfo threatInfo;

  public RequestBody(List<String> urls) {
    this.client = new Client();
    this.threatInfo = new ThreatInfo(urls);
  }

//  public static void main(String[] args) {
//    Gson gson = new Gson();
//    List<String> urls = new ArrayList<>();
//    urls.add("www1");
//    urls.add("www3");
//    RequestBody body = new RequestBody(urls);
//    String str = gson.toJson(body);
//    System.out.println(str);
//  }

}
