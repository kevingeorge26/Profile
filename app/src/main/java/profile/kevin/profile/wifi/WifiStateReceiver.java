package profile.kevin.profile.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import profile.kevin.profile.MainActivity;

public class WifiStateReceiver extends BroadcastReceiver {
    public WifiStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo != null && networkInfo.isConnected()) {
                WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                String newWifiName = wifiInfo.getSSID();

                if (WifiFragment.configuredWifiList.containsKey(newWifiName)) {
                    String profileName = WifiFragment.configuredWifiList.get(newWifiName);
                    Log.e(MainActivity.LOG_PREFIX, "found the wifi " + newWifiName + "  " + profileName);
                    AudioManager audioManger = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    if (profileName.equals("Silent")) {
                        audioManger.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    } else if (profileName.equals("Vibrate")) {
                        audioManger.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    } else {
                        audioManger.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }
                }
            }
        } else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && !networkInfo.isConnected()) {
                // Wifi is disconnected

            }
        }
    }
}
