package me.abitno.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.http.AndroidHttpClient;

public class NetHelper {

	public static HttpResponse postResponse(Context c, String url, Map<String, String> headers, Map<String, String> params) throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient client = createHttpsClient();
		HttpPost request = new HttpPost();
		request.setHeader("Accept-Encoding", "gzip");
		if (c != null) {
			request.setHeader("User-Agent", "VPlayer/" + AndroidContextUtils.getVersionCode(c));
		}
		if (headers != null) {
			for (String h : headers.keySet())
				request.setHeader(h, headers.get(h));
		}

		if (params != null) {
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			for (String p : params.keySet())
				postParameters.add(new BasicNameValuePair(p, params.get(p)));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(formEntity);
		}
		request.setURI(new URI(url));
		return client.execute(request);
	}

	public static HttpResponse getResponse(Context c, String url, Map<String, String> headers) throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient client = createHttpsClient();
		HttpGet request = new HttpGet();
		request.setHeader("Accept-Encoding", "gzip");
		if (c != null) {
			request.setHeader("User-Agent", "VPlayer/" + AndroidContextUtils.getVersionCode(c));
		}
		if (headers != null) {
			for (String h : headers.keySet())
				request.setHeader(h, headers.get(h));
		}
		request.setURI(new URI(url));
		return client.execute(request);
	}

	public static int getIntResponse(Context c, String url) {
		try {
			String l = getStringResponse(c, url);
			return StringUtils.isBlank(l) ? 0 : Integer.parseInt(l);
		} catch (Exception e) {
			Log.e("getIntResponse#" + url, e);
			return 0;
		}
	}

	public static String getStringResponse(Context c, String url) {
		return getStringResponse(c, url, null);
	}

	public static String getStringResponse(Context c, String url, Map<String, String> headers) {
		InputStreamReader isr = null;
		BufferedReader in = null;
		try {
			isr = new InputStreamReader(AndroidHttpClient.getUngzippedContent(getResponse(c, url, headers).getEntity()));
			in = new BufferedReader(isr);
			return in.readLine();
		} catch (Exception e) {
			Log.e("getStringResponse#" + url, e);
			return "";
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static HttpClient createHttpsClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
