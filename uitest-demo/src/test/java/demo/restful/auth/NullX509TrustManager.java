package demo.restful.auth;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Trust manager that does not perform nay checks.
 */
public class NullX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		// TODO Auto-generated method stub
		System.out.println();
	}

	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		// TODO Auto-generated method stub
		System.out.println();
	}

	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return new X509Certificate[0];
	}

}