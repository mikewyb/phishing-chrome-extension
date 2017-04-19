package phishingDB.googleSafeBrowsingAPI;

import java.util.ArrayList;
import java.util.List;

public class ResponseBody {
  class MatchInstance {
    class Threat {
      String url;
    }

    String threatType;
    String platformType;
    Threat threat;
    String cacheDuration;
    String threatEntryType;
  }

  List<MatchInstance> matches;

  List<String> getUrls() {
    List<String> urls = new ArrayList<>();
    for (MatchInstance instance : matches) {
      urls.add(instance.threat.url);
    }
    return urls;
  }
}
