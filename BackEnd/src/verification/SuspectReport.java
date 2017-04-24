package verification;

import com.google.gson.Gson;
import phishingDB.dynamoDB.PhishingUrlItem;
import util.RequestBody;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static phishingDB.dynamoDB.DynamoDBOperator.saveUrl;
import static util.ReadAllLines.readAllLines;

/**
 * Servlet implementation class VerifyRequest
 */
@WebServlet("/SuspectRequest")
public class SuspectReport extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public SuspectReport() {
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
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   * response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    // parse request
    Gson gson = new Gson();
    RequestBody requestBody = gson.fromJson(readAllLines(request.getReader()), RequestBody.class);

    try {
      PhishingUrlItem item = new PhishingUrlItem();
      item.setNormalizedUrl(requestBody.getURL());
      saveUrl(item);
      response.getWriter().append("successful");
    } catch (Exception e) {
      response.getWriter().append("fail");
    }


  }
}
