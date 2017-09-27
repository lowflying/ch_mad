package com.cherena.myapp;
import java.io.Serializable;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Cherena on 25/09/2017.
 */

public class BikeStation implements Serializable{

    @SerializedName("number")
    long IDNumber;
    @SerializedName("name")
    String Name;
    @SerializedName("address")
    String Address;
    @SerializedName("position")
    Position Position;
    @SerializedName("bike_stands")
    String BikeStands;
    @SerializedName("available_bike_stands")
    int AvailableBikes;

    public class Position {
        Double lat;
        Double lng;
    }


}


