/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package test;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.*;
/**
 *
 * @author Siteng Jin
 */
class SimpleX509TrustManager implements X509TrustManager
{
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }
    
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
    
    public static void disableSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new   SimpleX509TrustManager() };
            
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }}
}
