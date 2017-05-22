package org.wsipersd.core.security.web;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientFactory {
	public static HttpClient buildHttpClient(int maxTotal, int maxPerRoute) throws Exception{
		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		final TrustManager[] trustAllCerts =
		{ new DummyTrustManager() };

		final SSLContext sslContext = SSLContext.getInstance("TLSv1");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

		final HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
		final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

		httpClientBuilder.setSSLHostnameVerifier(hostnameVerifier);
		httpClientBuilder.setSSLSocketFactory(sslSocketFactory);

		final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build();

		final PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		connMgr.setMaxTotal(maxTotal);
		connMgr.setDefaultMaxPerRoute(maxPerRoute);
		httpClientBuilder.setConnectionManager(connMgr);

		return httpClientBuilder.build();

	}
	
	static class DummyTrustManager extends X509ExtendedTrustManager {
		private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[0];

		@Override
		public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException
		{
			return;
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) throws CertificateException
		{
			return;
		}

		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			return ACCEPTED_ISSUERS;
		}

		@Override
		public void checkClientTrusted(final X509Certificate[] arg0, final String arg1, final Socket arg2) throws CertificateException
		{
			return;
		}

		@Override
		public void checkClientTrusted(final X509Certificate[] arg0, final String arg1, final SSLEngine arg2)
				throws CertificateException
		{
			return;
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] arg0, final String arg1, final Socket arg2) throws CertificateException
		{
			return;
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] arg0, final String arg1, final SSLEngine arg2)
				throws CertificateException
		{
			return;
		}

	}

}
