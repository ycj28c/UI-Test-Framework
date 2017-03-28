package io.ycj28c.uitest.framework;

import org.openqa.selenium.WebDriver;
import org.springframework.core.env.Environment;

/**
 * Static methods around web elements for logout
 */
public class LogoutHelper {	
	
	public static void directLogout(Environment env, WebDriver driver) {
		String parameter = "logoutUrl";
		getUrlByLoginMode(env, driver, parameter);
	}
	
	public static void directLogin(Environment env, WebDriver driver) {
		String parameter = "logoinUrl";
		getUrlByLoginMode(env, driver, parameter);
	}
	
	public static Integer getLoginMode(Environment env){
		return env.getProperty("login_mode", Integer.class);
	}
	
	public static String getLoginUrl(Environment env){
		String parameter = "loginUrl";
		
		String str = getParameterByLoginMode(env, parameter);
		if(str == null ||str.isEmpty()){
			throw new IllegalArgumentException("Error, unable to get loginUrl "+ parameter);
		}
		return str;
	}
	
	public static String getLogoutUrl(Environment env){
		String parameter = "logoutUrl";
		
		String str = getParameterByLoginMode(env, parameter);
		if(str == null ||str.isEmpty()){
			throw new IllegalArgumentException("Error, unable to get logoutUrl "+ parameter);
		}
		return str;
	}
	
	private static String getParameterByLoginMode(Environment env, String parameter){
		Integer loginMode = env.getProperty("login_mode", Integer.class);
		if(loginMode == null){
			return env.getRequiredProperty(parameter, String.class);
		}
		
		if(loginMode instanceof Integer){
			String url = parameter + loginMode;
			if(loginMode == 1){ //if 1, allow to use logoutUrl instead of logoutUrl1
				if(env.getProperty(url, String.class) == null){
					return env.getRequiredProperty(parameter, String.class);
				} 
			} 
			return env.getRequiredProperty(url, String.class);
		}
		return null;
	}
	/**
	 * get URL function by login mode
	 * @param env
	 * @param driver
	 * @param parameter
	 */
	private static void getUrlByLoginMode(Environment env, WebDriver driver, String parameter){
		Integer loginMode = env.getProperty("login_mode", Integer.class);
		
		if(loginMode == null){ //if no login mode, use default login
			driver.get(env.getRequiredProperty(parameter, String.class));
			return;
		}
		
		if(loginMode instanceof Integer){
			String url = parameter + loginMode;
			if(loginMode == 1){ //if 1, allow to use logoutUrl instead of logoutUrl1
				if(env.getProperty(url, String.class) == null){
					driver.get(env.getRequiredProperty(parameter, String.class));
					return;
				} 
			} 
			driver.get(env.getRequiredProperty(url, String.class));
			return;
		}
		
		throw new IllegalArgumentException("Error, get "+parameter+" behavior not successfully");
	}
	
}
