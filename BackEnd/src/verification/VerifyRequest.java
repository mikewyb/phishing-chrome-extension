package verification;

import java.io.BufferedReader;
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
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import phishingDB.googleSafeBrowsingAPI.CheckURLs;
import util.AnalysisResponse;
import util.AnalysisResult;
import util.RequestBody;

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
    StringBuilder json = new StringBuilder();
    BufferedReader reader = request.getReader();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line).append("\n");
      }
    } finally {
      reader.close();
    }
    Gson gson = new Gson();
    RequestBody requestBody = gson.fromJson(json.toString(), RequestBody.class);

    // verifying logic start here
    AnalysisResponse analysisResponse = new AnalysisResponse();
    analysisResponse.setResult(AnalysisResult.Unknown);

    //check protocol
    analysisResponse.setSecurityProtocol(checkProtocol(requestBody.getURL()));

    // TODO: send URL to lambda for normalization

    // check if normalized URL exist in phishing DB (blacklist)
    analysisResponse.setInBlackList(checkBlackList(requestBody.getURL()));
    if (analysisResponse.isInBlackList()) {
      analysisResponse.setResult(AnalysisResult.Dangerous);
      response.getWriter().append(gson.toJson(analysisResponse));
      return;
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
    // TODO: fetching text from requestURL and analyze text
//		String text = null;
//		try {
//			text = fetchTextData(requestURL);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (isPhishingWebByText(text)) {
//			return AnalysisResult.Negative;
//		}
//
//		// TODO
//		return AnalysisResult.Unknown;
  }

  private String fetchTextData(String requestURL) throws IOException {
    Document doc = Jsoup.connect(requestURL).get();
    String title = doc.title();
    System.out.println("............title............");
    System.out.println(title);
    System.out.println("............body............");
    System.out.println(doc.body().text());
    System.out.println("............base URI............");
    System.out.println(doc.baseUri());
    System.out.println("............text............");
    System.out.println(doc.text());
    // System.out.println("............head............");

    // URL url = new URL(requestURL);
    // System.out.println("protocol = " + url.getProtocol()); //TODO check
    // protocol
    // System.out.println("authority = " + url.getAuthority());
    // System.out.println("host = " + url.getHost()); //TODO check host
    // System.out.println("port = " + url.getPort());
    // System.out.println("path = " + url.getPath());
    // System.out.println("query = " + url.getQuery());
    // System.out.println("filename = " + url.getFile());
    // System.out.println("ref = " + url.getRef());
    return null;
  }

  /**
   * @param text
   * @return true for phishing website otherwise false (which may or may not
   * be a phsihing)
   */
  private boolean isPhishingWebByText(String text) {
    // TODO tf/idf or/and email/title/footer extracting

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
