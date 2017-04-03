package io.ycj28c.uitest.util;

public class ExportDataModal {

	private String methodName = null;
	private String imageName = null;
	private String localImagePath = null;
	private String uploadImageUrl = null; //upload to slack url
	private String errorLog = null;
	private String startTime = null;
	private String endTime = null;
	private String description = null;
	private String currentUrl = null;
	private String pageTitle = null;
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getErrorLog() {
		return errorLog;
	}
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUploadImageUrl() {
		return uploadImageUrl;
	}
	public void setUploadImageUrl(String uploadImageUrl) {
		this.uploadImageUrl = uploadImageUrl;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getLocalImagePath() {
		return localImagePath;
	}
	public void setLocalImagePath(String localImagePath) {
		this.localImagePath = localImagePath;
	}
	public String getCurrentUrl() {
		return currentUrl;
	}
	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

}
