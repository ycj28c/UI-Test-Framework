package io.ycj28c.uitest.util;

import in.ashwanthkumar.slack.webhook.Slack;
import in.ashwanthkumar.slack.webhook.SlackMessage;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackUtil {
	private static final Logger log = LoggerFactory.getLogger(SlackUtil.class);
	private int SLACK_CHANNEL_MODE = 1;
	private int SLACK_USER_MODE = 2;
	
	/**
	 * upload the image directly to channel
	 * @param slackCon
	 * @param edModal
	 */
	public void uploadImageToChannel(SlackConnection slackCon, ExportDataModal edModal){
		uploadImageTemplate(slackCon, edModal, SLACK_CHANNEL_MODE);
	}
	
	/**
	 * upload the image directly to user
	 * @param slackCon
	 * @param edModal
	 */
	public void uploadImageToUser(SlackConnection slackCon, ExportDataModal edModal){
		uploadImageTemplate(slackCon, edModal, SLACK_USER_MODE);
	}
	
	private void uploadImageTemplate(SlackConnection slackCon, ExportDataModal edModal, int mode) {
		//initial connection
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//HttpPost uploadFile = new HttpPost("https://slack.com/api/files.upload");
		HttpPost uploadFile = new HttpPost(slackCon.getHost());
		
		//add body information
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		//builder.addBinaryBody("file", new File("C:/Users/ryang/Desktop/myPic.png"), ContentType.APPLICATION_OCTET_STREAM, "myPic.png");
		builder.addBinaryBody("file", new File(edModal.getLocalImagePath()), ContentType.APPLICATION_OCTET_STREAM, edModal.getImageName());
		StringBody token = new StringBody(slackCon.getToken(), ContentType.MULTIPART_FORM_DATA);
		builder.addPart("token", token);
		
		//if immediately post on channel
		StringBody postTo = null;
		if(mode == SLACK_CHANNEL_MODE){
			postTo = new StringBody("#" + slackCon.getChannel(), ContentType.MULTIPART_FORM_DATA);
		}
		if(mode == SLACK_USER_MODE){
			postTo = new StringBody("@" + slackCon.getUser(), ContentType.MULTIPART_FORM_DATA);
		}
		builder.addPart("channels", postTo);
		builder.addPart("title",  new StringBody(edModal.getMethodName(), ContentType.MULTIPART_FORM_DATA));
		builder.addPart("initial_comment",  new StringBody("It is not me, it is a Slack Bot", ContentType.MULTIPART_FORM_DATA));
		
		//send HTTP request
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(uploadFile);
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
		
		//handle the HTTP response
		HttpEntity responseEntity = response.getEntity();
		if(responseEntity!=null){
			String responseJson = null;
			try {
				responseJson = EntityUtils.toString(responseEntity);
			} catch (ParseException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			log.debug("response json is: " + responseJson);
			JSONObject responseObj = new JSONObject(responseJson);
			//String imageUrl = responseObj.getJSONObject("file").getString("url"); //slack api change, url no long existed
			String imageUrl = responseObj.getJSONObject("file").getString("thumb_720");
			log.info("uploaded image url is: " + imageUrl);
		}
	}

//	public String uploadImage(SlackConnection slackCon, String localImagePath, String imageName){
//		//initial connection
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		//HttpPost uploadFile = new HttpPost("https://slack.com/api/files.upload");
//		HttpPost uploadFile = new HttpPost(slackCon.getHost());
//		
//		//add body information
//		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
//		//builder.addBinaryBody("file", new File("C:/Users/ryang/Desktop/myPic.png"), ContentType.APPLICATION_OCTET_STREAM, "myPic.png");
//		builder.addBinaryBody("file", new File(localImagePath), ContentType.APPLICATION_OCTET_STREAM, imageName);
//		StringBody token = new StringBody(slackCon.getToken(), ContentType.MULTIPART_FORM_DATA);
//		builder.addPart("token", token);
//		//if immediately post on channel
//		//StringBody channels = new StringBody("#testforralph", ContentType.MULTIPART_FORM_DATA);
//		//builder.addPart("channels", channels);
//		
//		//send HTTP request
//		HttpEntity multipart = builder.build();
//		uploadFile.setEntity(multipart);
//		CloseableHttpResponse response = null;
//		try {
//			response = httpClient.execute(uploadFile);
//		} catch (IOException e1) {
//			log.error(e1.getMessage());
//		}
//		
//		//handle the HTTP response
//		HttpEntity responseEntity = response.getEntity();
//		if(responseEntity!=null){
//			String responseJson = null;
//			try {
//				responseJson = EntityUtils.toString(responseEntity);
//			} catch (ParseException e) {
//				log.error(e.getMessage());
//			} catch (IOException e) {
//				log.error(e.getMessage());
//			}
//			log.debug("response json is: " + responseJson);
//			JSONObject responseObj = new JSONObject(responseJson);
//			//String imageUrl = responseObj.getJSONObject("file").getString("url"); //slack api change, url no long existed
//			String imageUrl = responseObj.getJSONObject("file").getString("thumb_720");
//			log.info("uploaded image url is: " + imageUrl);
//			return imageUrl;
//		} else {
//			return null;
//		}
//	
//	}

	/**
	 * send message to slack template
	 * @param edModal
	 * @param slackCon
	 * @param mode
	 */
	private void sendResultToTemplate(ExportDataModal edModal, SlackConnection slackCon, int mode){
		if(mode<1||mode>2){
			log.error("Error, mode is {}! The slack sending mode must be 'SLACK_CHANNEL_MODE' 1 or 2 'SLACK_USER_MODE'.", mode);
			return;
		}
		String webhookUrl = slackCon.getWebhook();
		Slack slack = new Slack(webhookUrl);
		try {
			slack.icon(":smiling_imp:").displayName(slackCon.getDisplayName());
			if(mode == SLACK_CHANNEL_MODE){
				slack.sendToChannel(slackCon.getChannel());
			}
			if(mode == SLACK_USER_MODE){
				slack.sendToUser(slackCon.getUser());
			}
			// send text use webhook 
			if(edModal.getDescription()!=null&&!edModal.getDescription().trim().isEmpty()){
				slack.push(new SlackMessage(" *"+edModal.getMethodName()+"*\n")
							.text(" _Test Step:_ \n")
							.preformatted(edModal.getDescription())
							.text(" _Error Log:_ \n")
							.preformatted(edModal.getErrorLog()));
			} else { //if no test step, don't print steps
				slack.push(new SlackMessage(" *"+edModal.getMethodName()+"*\n")
							.text(" _Error Log:_ \n")
							.preformatted(edModal.getErrorLog()));
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}

	}
	
	/**
	 * send test fail result detail to slack channel
	 * @param edModal
	 * @param slackCon
	 */
	public void sendResultToChannel(ExportDataModal edModal, SlackConnection slackCon) {
		sendResultToTemplate(edModal, slackCon, SLACK_CHANNEL_MODE);
	}

	/**
	 * send test fail result detail to slack user
	 * @param edModal
	 * @param slackCon
	 */
	public void sendResultToUser(ExportDataModal edModal, SlackConnection slackCon) {
		sendResultToTemplate(edModal, slackCon, SLACK_USER_MODE);		
	}

}
