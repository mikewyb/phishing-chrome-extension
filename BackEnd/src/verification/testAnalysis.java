package verification;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.AnalysisResponse;
import util.AnalysisResult;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class testAnalysis {

  private static void analyze(String requestURL, AnalysisResponse response) {
    //logo link match
    Document doc = null;
    try {
      if (!requestURL.startsWith("http")) {
        requestURL = "http://www." + requestURL;
      }
      doc = Jsoup.connect(requestURL)
              .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) " +
                      "Chrome/41.0.2228.0 Safari/537.36")
              .referrer("http://www.google.com")
              .get();
    } catch (Exception e) {
      //silence for request fail
      System.out.println(e.toString());
      response.setResult(AnalysisResult.Unknown);
      return;
    }

    boolean hasLogo = false;
    Elements all_links = doc.select("a");
    for (Element ele : all_links) {
      if (isContainLogo(ele)) {
        hasLogo = true;
        String link = ele.attr("href");
        if (!isHostMatch(link, requestURL)) {
          if (link.equalsIgnoreCase("/") || link.startsWith("#")) {
            response.setResult(AnalysisResult.Unsafe);
          } else {
            response.setResult(AnalysisResult.Suspicious);
          }
        } else {
          response.setResult(AnalysisResult.Safe);
        }

      }
    }

    if (!hasLogo) {
      response.setResult((AnalysisResult.Suspicious));
    }

    //title matching
    String title = doc.title().split(" ")[0];
    try {
      URL url = new URL(requestURL);
      if (title.compareToIgnoreCase(url.getHost().split("\\.")[0]) == 0) {
        response.setResult(AnalysisResult.Safe);
      }
    } catch (MalformedURLException e) {
      //silence
    }
  }

  private static boolean isContainLogo(Element e) {
//        check current attr
    for (Attribute attr : e.attributes()) {
      if (attr.getValue().toLowerCase().contains("logo")) {
        return true;
      }
    }

//    check if in the img
    Elements imgs = e.select("img");
    for (Element img : imgs) {
      for (Attribute attr : img.attributes()) {
        if (attr.getValue().toLowerCase().contains("logo")) {
          return true;
        }
      }
    }

    return false;
  }

  private static boolean isHostMatch(String requestURL, String analysisURL) {
    try {
      URL rURL = new URL(requestURL);
      URL aURL = new URL(analysisURL);
      System.out.println("compare:" + rURL.getHost() + " with :" + aURL.getHost());
      if (rURL.getHost().equalsIgnoreCase(aURL.getHost())) {
        return true;
      }
    } catch (MalformedURLException e) {
      //silence for illegal url
    }
    return false;
  }



  public static void main(String[] args) {
    int unsafe = 0;
    int safe = 0;
    int suspicious = 0;
    int total = 0;
    int unknown = 0;
    Scanner scan = new Scanner(System.in);
    while(scan.hasNextLine() && total < 100) {
      String url = scan.nextLine();
      AnalysisResponse response = new AnalysisResponse();
      analyze(url, response);
      System.out.println(response.getResult());
      if (response.getResult() == AnalysisResult.Suspicious) {
        suspicious++;
      } else if (response.getResult() == AnalysisResult.Safe) {
        safe++;
      } else if (response.getResult() == AnalysisResult.Unsafe) {
        unsafe++;
      } else if (response.getResult() == AnalysisResult.Unknown) {
        unknown++;
        total--;
      }
      System.out.println("Result:" + response.getResult());
      total++;
    }

    System.out.println("Unsafe:" + unsafe);
    System.out.println("Safe:" + safe);
    System.out.println("suspicious:" + suspicious);
    System.out.println("unknown:" + unknown);
  }

}
