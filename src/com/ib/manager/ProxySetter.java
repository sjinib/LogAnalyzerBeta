/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.manager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;


/**
 *
 * @credit to Ernie Parker
 */
public class ProxySetter {
    private static String getProxy() {
        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            if (ipAddress.equals("127.0.0.1")) {
                NetworkInterface in = NetworkInterface.getByName("eth0");
                Enumeration<InetAddress> addresses = in.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress element = addresses.nextElement();
                    ipAddress = element.getHostAddress();
                }
            }
            if (ipAddress.matches("192.168.12[01].[0-9]+")) {
                return "192.168.121.254";
            }
            if (ipAddress.matches("192.168.132.*")) {
                return "192.168.133.254";
            }
        } catch (UnknownHostException e) {
            return null;
        } catch (SocketException e) {
            return null;
        }
        return null;
    }    
}
