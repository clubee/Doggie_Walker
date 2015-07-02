package com.clubee.doggywalker;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BuscaGeolocalizacao {

    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final String localizacaoEnd,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {

            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                Double char_Lat = null;
                Double char_Long = null;

                try {
                    List<Address> addressList = geocoder.getFromLocationName(localizacaoEnd, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address endereco = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        char_Lat = endereco.getLatitude();
                        char_Long = endereco.getLongitude();
                        sb.append(endereco.getLatitude()).append("\n");
                        sb.append(endereco.getLongitude()).append("\n");
                        result = sb.toString();

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Não conectou no geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("endereco", result);
                        bundle.putDouble("char_lat",char_Lat);
                        bundle.putDouble("char_long",char_Long);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Endereço: " + localizacaoEnd +
                                "\n Não foi possivel capturar a Latitude e Longitude.";
                        bundle.putString("endereco", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}