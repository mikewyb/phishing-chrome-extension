package verification;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO formatting request and response
		// TODO adding real functional check of phishing
		// TODO since Get method is not a good way to send request with body, may consider re-work
		
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
		
		//TODO: analyze
		result = analyze(requestBody.URL);
		
		//TODO: format the response, the following for testing
		response.getWriter().append("Anti-Phishing Served at: ").append(request.getContextPath());
		response.getWriter().println();
		response.getWriter().append("Verifying information:").append(json.toString());
		response.getWriter().println();
		response.getWriter().append("Result:").append(result.name());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO considering report of phishing by communities
		doGet(request, response);
	}
	
	private String fetchTextData(String URL) {
		//TODO 
		return null;
	}
	
	/**
	 * 
	 * @param text
	 * @return true for phishing website otherwise false (which may or may not be a phsihing)
	 */
	private boolean isPhishingWebByText(String text) {
		//TODO tf/idf or/and       email/title/footer extracting
		
		return false;
	}
	
	/**
	 * 
	 * @param URL
	 * @return
	 */
	private AnalysisResult analyze(String URL) {
		//TODO: fetching text from URL and analyze text
		String text = fetchTextData(URL);
		if (isPhishingWebByText(text)) {
			return AnalysisResult.Negative;
		}
		

		//TODO
		return AnalysisResult.Unknown;
	}

}
