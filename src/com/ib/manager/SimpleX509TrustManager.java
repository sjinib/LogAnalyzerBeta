/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.ib.manager;

import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import org.apache.log4j.Logger;
/**
 *
 * @author Siteng Jin
 */
class SimpleX509TrustManager implements X509TrustManager{
    private static final Logger LOG = Logger.getLogger(SimpleX509TrustManager.class);
    
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }
    
    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
    
    public static void disableSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new   SimpleX509TrustManager() };
            
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(), e);
        }}
}
