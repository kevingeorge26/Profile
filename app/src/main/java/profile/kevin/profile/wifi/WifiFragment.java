package profile.kevin.profile.wifi;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import profile.kevin.profile.MainActivity;
import profile.kevin.profile.R;

public class WifiFragment extends Fragment {

    // method to create the fragment
    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private static List<String> availableWifiSSID;
    private static List<String> profileNameList = Arrays.asList("Silent", "Vibrate", "Normal");
    private static AdapterView.OnItemSelectedListener profileSpinnerClick;
    private static ArrayAdapter<String> availableWifiListAdapter, availableSoundProfilesAdapter;
    // <wifiSSID, profileName>
    public static HashMap<String, String> configuredWifiList = new HashMap<>();

    public WifiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached("Wifi");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAvailableWIfi();

        availableWifiListAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                availableWifiSSID);

        availableSoundProfilesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                profileNameList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addNewWifiSetting();
    }

    private void getAvailableWIfi() {
        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        availableWifiSSID = new ArrayList<String>();
        for(WifiConfiguration wifi: configuredNetworks) {
            availableWifiSSID.add(wifi.SSID);
        }
    }

    // create the UI to show the new save wifi
    private void saveNewConfiguredWifi(String wifiSSID, String profileName) {
        configuredWifiList.put(wifiSSID, profileName);

        LinearLayout savedWifiEntryContainer  = new LinearLayout(getActivity());
        savedWifiEntryContainer.setOrientation(LinearLayout.HORIZONTAL);
        TextView wifiTextView = new TextView(getActivity());
        wifiTextView.setText(wifiSSID);
        savedWifiEntryContainer.addView(wifiTextView);

        TextView profileTextView = new TextView(getActivity());
        profileTextView.setText(profileName);
        savedWifiEntryContainer.addView(profileTextView);

        Button removeWifiButton = new Button(getActivity());
        removeWifiButton.setText("Delete");
        removeWifiButton.setOnClickListener(new View.OnClickListener(){
            private String deletedWifiSSID;

            @Override
            public void onClick(View v) {
                configuredWifiList.remove(this.deletedWifiSSID);
                LinearLayout deleteLayout = (LinearLayout) v.getParent();
                ((ViewGroup) deleteLayout.getParent()).removeView(deleteLayout);
            }

            public View.OnClickListener init(String wifiSSID) {
                this.deletedWifiSSID = wifiSSID;
                return this;
            }
        }.init(wifiSSID));
        savedWifiEntryContainer.addView(removeWifiButton);

        LinearLayout configuredWIfiContainer = (LinearLayout) getView().findViewById(R.id.configured_wifi_container);
        configuredWIfiContainer.addView(savedWifiEntryContainer);
    }

    // add new wifi UI
    private void addNewWifiSetting() {
        LinearLayout newWifiSettingLayout = new LinearLayout(getActivity());
        newWifiSettingLayout.setPadding(0,0,0,0);
        newWifiSettingLayout.setOrientation(LinearLayout.HORIZONTAL);

        final Spinner wifiSpinner = new Spinner(getActivity());
        wifiSpinner.setAdapter(availableWifiListAdapter);
        wifiSpinner.setPadding(5, 5, 5, 5);
        newWifiSettingLayout.addView(wifiSpinner);

        final Spinner profileSpinner = new Spinner(getActivity());
        profileSpinner.setAdapter(availableSoundProfilesAdapter);
        profileSpinner.setPadding(5, 5, 5, 5);
        newWifiSettingLayout.addView(profileSpinner);

        Button addWifiButton = new Button(getActivity());
        addWifiButton.setText("Add");
        newWifiSettingLayout.addView(addWifiButton);
        addWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedWifi = wifiSpinner.getSelectedItem().toString();
                String selectedProfile = profileSpinner.getSelectedItem().toString();
                saveNewConfiguredWifi(selectedWifi, selectedProfile);
            }
        });


        LinearLayout addNewWifiContainer = (LinearLayout) getView().findViewById(R.id.wifi_scroll_child);
        addNewWifiContainer.addView(newWifiSettingLayout);
    }
}
