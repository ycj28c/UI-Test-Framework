package demo.restful.auth;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Host name verifier that does not perform nay checks.
 */
public class NullHostNameVerifier implements HostnameVerifier {

	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}
