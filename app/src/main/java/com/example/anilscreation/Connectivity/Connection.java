package com.example.anilscreation.Connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
    public static boolean isConnected(Context context) {
        NetworkInfo info = Connection.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

}
