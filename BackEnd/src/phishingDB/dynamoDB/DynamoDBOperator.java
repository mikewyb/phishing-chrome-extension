package phishingDB.dynamoDB;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class DynamoDBOperator {
  private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                                        .withRegion(Regions.US_EAST_1)
                                        .build();
  private static DynamoDBMapper mapper = new DynamoDBMapper(client);

  public static PhishingUrlItem checkUrl(String normalizedUrl) {
    PhishingUrlItem phishingUrlItem = new PhishingUrlItem();
    phishingUrlItem.setNormalizedUrl(normalizedUrl);

    try {
      PhishingUrlItem result = mapper.load(phishingUrlItem);
      if (result != null) {
        return result;
      } else {
        System.out.println("No matching phishing url was found");
      }
    } catch (Exception e) {
      System.err.println("Unable to retrieve data: ");
      System.err.println(e.getMessage());
    }

    return null;
  }

  public static void saveUrl(PhishingUrlItem urlItem) {
    mapper.save(urlItem);
  }

  public static void deleteUrl(PhishingUrlItem urlItem) {
    mapper.delete(urlItem);
  }
}