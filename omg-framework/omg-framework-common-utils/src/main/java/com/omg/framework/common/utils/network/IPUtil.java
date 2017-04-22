package com.omg.framework.common.utils.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IPUtil {
    public static List<String> getLocalInetAddress() {
        List<String> inetAddressList = new ArrayList();
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();

            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                Enumeration<InetAddress> addrs = networkInterface.getInetAddresses();

                while (addrs.hasMoreElements()) {
                    inetAddressList.add(((InetAddress) addrs.nextElement()).getHostAddress());
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException("get local inet address fail", e);
        }

        return inetAddressList;
    }

    public static String localhostAddress() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (Throwable e) {
        }
        return "";
    }
}
