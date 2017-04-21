package phishingDB.dynamoDB;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="phishing_urls")
public class PhishingUrlItem {
  private String normalizedUrl;
  private String originalUrl;
  private Boolean online;
  private Boolean verified;
  private String verificationTime;
  private String ipAddress;
  private String source;
  private String target;
  private String submissionTime;
  private Integer upVote;
  private Integer downVote;


  @DynamoDBHashKey(attributeName="normalized_url")
  public String getNormalizedUrl() { return normalizedUrl;}
  public void setNormalizedUrl(String normalizedUrl) {this.normalizedUrl = normalizedUrl;}

  @DynamoDBAttribute(attributeName = "original_url")
  public String getOriginalUrl() { return originalUrl;}
  public void setOriginalUrl(String originalUrl) {this.originalUrl = originalUrl;}

  @DynamoDBAttribute(attributeName = "verified")
  public Boolean getVerified() { return verified; }
  public void setVerified(Boolean verified) { this.verified = verified; }

  @DynamoDBAttribute(attributeName = "verification_time")
  public String getVerificationTime() { return verificationTime; }
  public void setVerificationTime(String verificationTime) { this.verificationTime = verificationTime; }

  @DynamoDBAttribute(attributeName = "ip_address")
  public String getIpAddress() { return ipAddress; }
  public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

  @DynamoDBAttribute(attributeName = "source")
  public String getSource() { return source; }
  public void setSource(String source) { this.source = source; }

  @DynamoDBAttribute(attributeName = "target")
  public String getTarget() { return target; }
  public void setTarget(String target) { this.target = target; }

  @DynamoDBAttribute(attributeName = "submission_time")
  public String getSubmissionTime() { return submissionTime; }
  public void setSubmissionTime(String submissionTime) { this.submissionTime = submissionTime; }

  @DynamoDBAttribute(attributeName = "online")
  public Boolean getOnline() { return online; }
  public void setOnline(Boolean online) { this.online = online; }

  @DynamoDBAttribute(attributeName = "up_vote")
  public Integer getUpVote() { return upVote; }
  public void setUpVote(Integer upVote) { this.upVote = upVote; }

  @DynamoDBAttribute(attributeName = "down_vote")
  public Integer getDownVote() { return downVote; }
  public void setDownVote(Integer downVote) { this.downVote = downVote; }
}
