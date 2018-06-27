package test;

import com.ib.manager.ProxyManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Siteng Jin
 */
public class TestConnection {
    public static void main(String[] args){
        ProxyManager.getInstance().setupProxyUsingConfig();
        
        org.jsoup.Connection con = null;
        
        try{
            //String url = "https://wit1.interactivebrokers.com/cgi-bin/tws/tws_error_reader.pl";
            
            //org.jsoup.nodes.Document doc = Jsoup.connect("https://wit1.interactivebrokers.com/cgi-bin/tws/tws_error_reader.pl")
            //        .timeout(10000)
            //        .get();
            
            //con = Jsoup.connect(url);
            //con.method(Connection.Method.GET);
            //org.jsoup.Connection.Response resp = con.execute();
            
            //org.jsoup.nodes.Document doc = con.get();
            
            
            URL url = new URL("https://wit1.interactivebrokers.com/cgi-bin/tws/tws_error_reader.pl");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            
            System.out.println("Successful!");
        } catch (Exception e){
            e.printStackTrace();
            //System.out.println(con.response().statusCode() + ", " + con.response().statusMessage());
        }
    }
}
