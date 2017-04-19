package verification;

import java.io.BufferedReader;
import java.io.IOException;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO formatting request and response
		// TODO adding real functional check of phishing
		// TODO since Get method is not a good way to send request with body,
		// may consider re-work

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

		AnalysisResult result = AnalysisResult.Unknown;
		
		//TODO: send URL to lambda for normalization
		
		//TODO: check if normalized URL exist in phishing DB
		List<String> urls = new ArrayList<>();
		urls.add(requestBody.URL);
		List<String> maliciousUrls = CheckURLs.checkURLs(urls);
		if (!maliciousUrls.isEmpty()) {
			response.getWriter().append("No malicious from DB");
		} else {
			response.getWriter().append("Bing ! from DB!");
		}
		
		
		
		
	//  public static void main(String[] args) {
//	    List<String> urls = new ArrayList<>();
//	    urls.add("http://caixa.suportefgtsliberadoparasaque.com/");
//	    urls.add("http://www.cwrucsa.com/images/home/?a=1");
//	    urls.add("https://www.google.com");
//	    List<String> maliciousUrls = CheckURLs.checkURLs(urls);
//	    for (String url : maliciousUrls) {
//	      System.out.println(url);
//	    }
		
		
		
		//TODO: analyze

		// TODO: send requestURL to lambda for normalization

		// TODO: check if normalized requestURL exist in phishing DB

		// TODO: analyze

		result = analyze(requestBody.URL);

		// TODO: format the response, the following for testing
		response.getWriter().append("V2 Anti-Phishing Served at: ").append(request.getContextPath());
		response.getWriter().println();
		response.getWriter().append("Verifying information:").append(json.toString());
		response.getWriter().println();
		response.getWriter().append("Result:").append(result.name());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO considering report of phishing by communities
		doGet(request, response);
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
//		System.out.println("............head............");
		
		
//		URL url = new URL(requestURL);
//		System.out.println("protocol = " + url.getProtocol());  //TODO check protocol
//		System.out.println("authority = " + url.getAuthority());
//		System.out.println("host = " + url.getHost());   //TODO check host
//		System.out.println("port = " + url.getPort());
//		System.out.println("path = " + url.getPath());
//		System.out.println("query = " + url.getQuery());
//		System.out.println("filename = " + url.getFile());
//		System.out.println("ref = " + url.getRef());
		return null;
	}

	/**
	 * 
	 * @param text
	 * @return true for phishing website otherwise false (which may or may not
	 *         be a phsihing)
	 */
	private boolean isPhishingWebByText(String text) {
		// TODO tf/idf or/and email/title/footer extracting

		return false;
	}

	/**
	 * 
	 * @param requestURL
	 * @return
	 */
	private AnalysisResult analyze(String requestURL) {
		// TODO: fetching text from requestURL and analyze text
		String text = null;
		try {
			text = fetchTextData(requestURL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isPhishingWebByText(text)) {
			return AnalysisResult.Negative;
		}

		// TODO
		return AnalysisResult.Unknown;
	}

}
