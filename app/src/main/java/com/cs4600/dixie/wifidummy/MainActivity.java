package com.cs4600.dixie.wifidummy;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*connects the items on the view with the variable*/
        //val connectButton = findViewById<Button>(R.id.connect_button);

        Button cnnctButton = (Button) findViewById(R.id.connect_button);
        Button fogetButton = (Button) findViewById(R.id.forget_button);
        /*Wifi Connection Settings */
        fogetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText wifiName = (EditText) findViewById(R.id.textbox_wifiname);
                forgetNetwork(wifiName.getText().toString());
            }
        });


        cnnctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*setting the textbox values*/
                EditText wifiName = (EditText) findViewById(R.id.textbox_wifiname);
                EditText wifiPassword = (EditText) findViewById(R.id.textbox_wifipass);
//                TextView givenWiFiName = (TextView) findViewById(R.id.text_wifiname);
//                TextView givenWifiPassword = (TextView) findViewById(R.id.text_wifipass);
//                givenWiFiName.setText(wifiName.getText());
//                givenWifiPassword.setText(wifiPassword.getText());
                String networkSSID = wifiName.getText().toString();
                String networkPass = wifiPassword.getText().toString();
                ConnectToNetworkWPA(networkSSID, networkPass);


            }
        });


    }
    public void forgetNetwork(String SSID) {
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + SSID + "\"")) {
                wifiManager.removeNetwork(i.networkId);


                break;
            }
        }
    }
    public boolean ConnectToNetworkWPA( String networkSSID, String password )
    {
        try {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain SSID in quotes

            conf.preSharedKey = "\"" + password + "\"";

            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            Log.d("connecting", conf.SSID + " " + conf.preSharedKey);

            WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.addNetwork(conf);

            Log.d("after connecting", conf.SSID + " " + conf.preSharedKey);



            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    //wifiManager.removeNetwork(i.networkId);
                    Log.d("re connecting", i.SSID + " " + conf.preSharedKey);

                    break;
                }
            }


            //WiFi Connection success, return true
            return true;
        } catch (Exception ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return false;
        }
    }
}
