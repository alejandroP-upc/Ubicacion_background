package com.example.ubicacion_background;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService extends Service {
    private final IBinder binder = new LocationBinder();
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static double Latitud;
    private static double Longitud;
    private static String Localidad;
    private static String pais;
    private static String direccion;
    public LocationService() {
    }
    public class LocationBinder extends Binder
    {
        LocationService getLocationService(){
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public void onCreate(){
        Log.i("Location service","Created");
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        getLocation();

    }
    public boolean onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }
    public void getLocation()
    {
        final Handler handler= new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            System.out.println("PRUEBA2");
                            Location location = task.getResult();

                            if (location != null) {
                                Geocoder geocoder = new Geocoder(LocationService.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    Latitud=addresses.get(0).getLatitude();
                                    Longitud=addresses.get(0).getLongitude();
                                    pais=addresses.get(0).getCountryName();
                                    Localidad= addresses.get(0).getLocality();
                                    direccion= addresses.get(0).getAddressLine(0);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                Log.i("Service getLocation","post delayed...");
                handler.postDelayed(this,5000);
            }
        });
    }

    public static double getLatitud() {
        return Latitud;
    }

    public static void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public static double getLongitud() {
        return Longitud;
    }

    public static void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public static String getLocalidad() {
        return Localidad;
    }

    public static void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public static String getPais() {
        return pais;
    }

    public static void setPais(String pais) {
        LocationService.pais = pais;
    }

    public static String getDireccion() {
        return direccion;
    }

    public static void setDireccion(String direccion) {
        LocationService.direccion = direccion;
    }
}
