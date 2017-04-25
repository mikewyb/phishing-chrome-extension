package verification;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import phishingDB.dynamoDB.PhishingUrlItem;
import phishingDB.googleSafeBrowsingAPI.CheckURLs;
import util.AnalysisResponse;
import util.AnalysisResult;
import util.RequestBody;

import static phishingDB.dynamoDB.DynamoDBOperator.checkUrl;
import static util.ReadAllLines.readAllLines;

/**
 * Servlet implementation class VerifyRequest
 */
@WebServlet("/VerifyRequest")
public class VerifyRequest extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public VerifyRequest() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * Both Get and Post currently are same
   *
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   * response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    // parse request
    Gson gson = new Gson();
    RequestBody requestBody = gson.fromJson(readAllLines(request.getReader()), RequestBody.class);

    // verifying logic start here
    AnalysisResponse analysisResponse = new AnalysisResponse();
    analysisResponse.setResult(AnalysisResult.Unknown);

    //check protocol
    analysisResponse.setSecurityProtocol(checkProtocol(requestBody.getURL()));
    if (!analysisResponse.isSecurityProtocol()) {
      analysisResponse.setResult(AnalysisResult.Unsafe);
    }

    // TODO: send URL to lambda for normalization

    // check if normalized URL exist in phishing DB (blacklist)
    analysisResponse.setInBlackList(checkBlackList(requestBody.getURL()));
    if (analysisResponse.isInBlackList()) {
      analysisResponse.setResult(AnalysisResult.Dangerous);
      response.getWriter().append(gson.toJson(analysisResponse));
      return;
    }

    //check if in our own DynamoDB
    PhishingUrlItem item = checkUrl(requestBody.getURL());
    if (item != null) {
      if (item.getVerified()) {
        analysisResponse.setInBlackList(true);
        analysisResponse.setResult(AnalysisResult.Dangerous);
      } else {
        analysisResponse.setResult(AnalysisResult.Suspicious);
      }
    }

    // TODO: check if URL exist in whitelist

    // TODO: analyze
    analyze(requestBody, analysisResponse);

    response.getWriter().append(gson.toJson(analysisResponse));
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   * response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    // TODO considering report of phishing by communities
    doGet(request, response);
  }



  /**
   * @param request
   * @param response
   */
  private void analyze(RequestBody request, AnalysisResponse response) {
    //logo link match
    Document doc = null;
    try {
      doc = Jsoup.connect(request.getURL()).get();
    } catch (IOException e) {
      //silence for request fail
      response.setResult(AnalysisResult.Unsafe);
    }
    Elements all_links = doc.select("a");
    System.out.println("Result size:" + all_links.size());
    for (Element ele : all_links) {
      String link = ele.attr("href");
      for (Attribute attr : ele.attributes()) {
        if (attr.getValue().toLowerCase().contains("logo")) {
          if (!isHostMatch(link, request.getURL())) {
            response.setResult(AnalysisResult.Suspicious);
          }
          break;
        }
      }
    }

    //title matching
    String title = doc.title().split(" ")[0];
    try {
      URL requestURL = new URL(request.getURL());
      if (title.compareToIgnoreCase(requestURL.getHost().split(".")[0]) == 0) {
        response.setResult(AnalysisResult.Safe);
      }
    } catch (MalformedURLException e) {
      //silence
    }
  }

  private boolean isHostMatch(String requestURL, String analysisURL) {
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

  private boolean checkProtocol(String requestURL) {
    try {
      URL url = new URL(requestURL);
      if (!url.getProtocol().equalsIgnoreCase("https")) {
        return false;
      } else {
        return true;
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean checkBlackList(String url) {
    List<String> urls = new ArrayList<>();
    urls.add(url);
    List<String> maliciousUrls = CheckURLs.checkURLs(urls);
    if (maliciousUrls.size() == 0) {
      return false;
    } else {
      return true;
    }
  }

}
