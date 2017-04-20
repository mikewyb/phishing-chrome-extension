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
      String url;
      Url(String url) {
        this.url = url;
      }
    }

    String[] threatTypes = {"MALWARE", "SOCIAL_ENGINEERING"};
    String[] platformTypes = {"WINDOWS"};
    String[] threatEntryTypes = {"URL"};
    List<Url> threatEntries = new ArrayList<>();

    ThreatInfo(List<String> urls) {
      for (String url : urls) {
        threatEntries.add(new Url(url));
      }
    }
  }

  Client client;
  ThreatInfo threatInfo;

  RequestBody(List<String> urls) {
    this.client = new Client();
    this.threatInfo = new ThreatInfo(urls);
  }
}
