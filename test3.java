/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import javax.net.ssl.SSLHandshakeException;
import java.security.cert.CertificateException;
import java.io.PrintStream;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Siteng Jin
 */
public class test3 {
    public static void main(String[] argv){
        System.getProperties().setProperty("proxySet", "true");
        System.getProperties().setProperty("http.proxyHost", "192.9.168.225");
        System.getProperties().setProperty("http.proxyPort", "3128");
        System.getProperties().setProperty("https.proxyHost", "192.9.168.225");
        System.getProperties().setProperty("https.proxyPort", "3128");
        
        Authenticator.setDefault (new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication ("twserror", "twserr67".toCharArray());
            }
        });
        
        try{
            /*
            URL url = new URL("https://wit1.interactivebrokers.com/cgi-bin/tws/tws_error_reader.pl");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            
            while ((inputLine = in.readLine()) != null)
            if(inputLine.contains(">csdem098")){
            System.out.println(inputLine);
            System.out.println(inputLine.replaceAll("\\<.*?>","").replaceAll("\\s+", ""));
            }
            //System.out.println(inputLine);
            in.close();
            */
            
            Authenticator.setDefault (new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication ("twserror", "twserr67".toCharArray());
                }
            });
            
            URL downloadLink = new URL("https://206.106.137.34/tws/data/csdem098.dyizsoxdm.20161107142807.zip");
            //URL downloadLink = new URL("http://interactivebrokers.github.io/downloads/twsapi_macunix.972.18.zip");
            
            //SSLContext ssl = SSLContext.getInstance("TLSv1");
            //ssl.init(null, new TrustManager[]{new SimpleX509TrustManager()}, null);
            //SSLSocketFactory factory = ssl.getSocketFactory();
            SimpleX509TrustManager.disableSSL();
            
            HttpURLConnection con = (HttpURLConnection)downloadLink.openConnection();
            //con.setSSLSocketFactory(factory);
            //InputStream ins = con.getInputStream();
            //InputStreamReader isr = new InputStreamReader(ins);
            
            
            //HttpURLConnection conn = (HttpURLConnection) downloadLink.openConnection();
            con.setRequestMethod("GET");
            InputStream input = con.getInputStream();
            FileOutputStream output = new FileOutputStream("C:\\Users\\Siteng Jin\\Documents\\NetBeansProjects\\Test\\download.zip");
            copy(input, output, 1024);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int n = input.read(buf);
        while (n >= 0) {
            output.write(buf, 0, n);
            n = input.read(buf);
        }
        output.flush();
    }
    
    

}
