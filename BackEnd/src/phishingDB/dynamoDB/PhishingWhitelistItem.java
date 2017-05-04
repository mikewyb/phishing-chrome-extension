package phishingDB.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="phishing_whitelist")
public class PhishingWhitelistItem {
  private String domainName;
  private Boolean valid;
  private String source;
  private Integer ranking;

  @DynamoDBHashKey(attributeName="domain_name")
  public String getDomainName() { return domainName; }
  public void setDomainName(String domainName) { this.domainName = domainName; }

  @DynamoDBAttribute(attributeName = "valid")
  public Boolean getValid() { return valid; }
  public void setValid(Boolean valid) { this.valid = valid; }

  @DynamoDBAttribute(attributeName = "source")
  public String getSource() { return source; }
  public void setSource(String source) { this.source = source; }

  @DynamoDBAttribute(attributeName = "ranking")
  public Integer getRanking() { return ranking; }
  public void setRanking(Integer ranking) { this.ranking = ranking; }
}
