package com.gaocs.utils;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * @Description
 **/
public class ComHttpUtil {

    private static final String DEF_ENCODING = "utf-8";

    static {
        //禁用 HttpURLConnection 的重试机制
        System.setProperty("sun.net.http.retryPost", "false");
    }

    public static String post(String url, String requestData) throws
		Exception {
        OutputStream ostream = null;
        InputStream istream = null;
        try {
            byte[] postData = requestData.getBytes(DEF_ENCODING);
            URLConnection conn = createRequest(url, "POST");
            conn.setRequestProperty("Content-type", "text/json");
            conn.setRequestProperty("Content-length", String.valueOf(postData.length));
            conn.setRequestProperty("Keep-alive", "true");
            ostream = conn.getOutputStream();
            ostream.write(postData);
            ostream.flush();
            ostream.close();

            ByteArrayOutputStream ms = new ByteArrayOutputStream();
            istream = conn.getInputStream();
            byte[] buf = new byte[4096];
            int count;
            while ((count = istream.read(buf, 0, buf.length)) > 0) {
                ms.write(buf, 0, count);
            }
            istream.close();
            String msg = new String(ms.toByteArray(), DEF_ENCODING);
            System.out.println(msg);
            return msg;
        } catch (Exception ex) {
            throw new Exception("HTTP POST出现异常  " + ex.getMessage(), ex);
        } finally {
            try {
                if (ostream != null) {
                    ostream.close();
                }
                if (istream != null) {
                    istream.close();
                }
            } catch (IOException e) {
                throw new Exception("关闭流出现异常" + e.getMessage(), e);
            }
        }
    }

    private static URLConnection createRequest(String strUrl, String
			strMethod) throws Exception {
        URL url = new URL(strUrl);
        // weblogic
        // URL url = new URL(null,strUrl ,new sun.net.www.protocol.controller
		// .Handler());
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setRequestMethod(strMethod);
            httpsConn.setSSLSocketFactory(getSSLSocketFactory());
            httpsConn.setHostnameVerifier(hv);
        } else if (conn instanceof HttpURLConnection) {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod(strMethod);
            httpConn.setReadTimeout(50 * 1000);
        }
        return conn;
    }

    private static synchronized SSLSocketFactory getSSLSocketFactory() throws
			Exception {
        if (sslFactory != null) {
            return sslFactory;
        }

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new X509TrustManager[]{X509}, null);
        sslFactory = sc.getSocketFactory();
        return sslFactory;
    }

	public static String parseRequest(HttpServletRequest request) throws
		Exception {
		InputStream in = request.getInputStream();
		int len = request.getContentLength();
		if (len <= 0)
			len = 2048;
		// return;
		byte[] tmp = new byte[2048];
		byte[] buffer = new byte[len];
		int totalLen = 0;
		while (true) {
			int readLen = in.read(tmp, 0, 2048);
			if (readLen <= 0)
				break;

			if ((readLen + totalLen) > len) {
				// 扩大缓冲区
				len = len + readLen + 2048;
				byte[] aa = new byte[len];
				System.arraycopy(buffer, 0, aa, 0, totalLen);
				buffer = aa;
			}
			System.arraycopy(tmp, 0, buffer, totalLen, readLen);
			totalLen = totalLen + readLen;
		}

		String reqData;
		String encoding = request.getCharacterEncoding();
		if (encoding == null){
			reqData = new String(buffer, 0, totalLen);
		}else {
			reqData = new String(buffer, 0, totalLen, encoding);
		}
		return  reqData;
	}

	public static void flushString(HttpServletResponse response, String dataString) {
		PrintWriter out = null;
		response.setHeader("Content-type", "text/plain;charset=utf-8");
		try {
			out = response.getWriter();
			if (out != null) {
				out.write(dataString);
				out.flush();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}


	private static SSLSocketFactory sslFactory = null;

    private static X509TrustManager X509 = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    private static HostnameVerifier hv = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    };
}
