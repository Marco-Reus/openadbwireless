package bvb.de.openadbwireless.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2016/4/17.
 */
public class DeviceInfoUtil {
    private static WifiManager wifiManager = null;

    public static boolean isWifi(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiManager.isWifiEnabled() && wifiInfo.getSSID() != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String getIpv4(Context context) {
        if (isWifi(context) && wifiManager != null) {
            int ip = wifiManager.getConnectionInfo().getIpAddress();
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
                    + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
        }
        return null;
    }
}
