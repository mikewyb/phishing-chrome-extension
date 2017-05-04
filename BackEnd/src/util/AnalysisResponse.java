package util;

import lombok.Data;

@Data
public class AnalysisResponse {
	
	/**
	 * true for https, false otherwise
	 */
	private boolean isSecurityProtocol;
	
	/**
	 * is it in the blacklist?
	 */
	private boolean isInBlackList;
	
	/**
	 * is it in the whitelist?
	 */
	private boolean isInWhiteList;
	
	/**
	 * Suggest final result
	 */
	private AnalysisResult result;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Security Protocol:").append(isSecurityProtocol).append("\n");
		sb.append("In blacklist:").append(isInBlackList).append("\n");
		sb.append("In whitelist:").append(isInWhiteList).append("\n");
		sb.append("Result:").append(result).append("\n");
		return sb.toString();
	}	

}
