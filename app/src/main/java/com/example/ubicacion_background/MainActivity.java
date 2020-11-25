package com.example.ubicacion_background;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LocationService localizacion;
    private boolean bound=false;
    private ServiceConnection connection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            LocationService.LocationBinder locationBinder = (LocationService.LocationBinder) binder;
            localizacion=locationBinder.getLocationService();
            bound=true;
            Log.i("OnServiceConnected","servicio enlazado"+localizacion.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
                bound=false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayUbicacion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent =new Intent(this,LocationService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        Log.i("On Start","Ejecuto intent"+intent.toString());

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bound)
        {
            unbindService(connection);
            bound=false;
        }
        Log.i("On Stop","bound"+bound);
    }
    public void displayUbicacion(){
        final TextView latitudView=(TextView)findViewById(R.id.text_latitud);
        final TextView longitudView=(TextView)findViewById(R.id.text_longitud);
        final TextView localidadView=(TextView)findViewById(R.id.text_localidad);
        final TextView paisView=(TextView)findViewById(R.id.text_pais);
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double latitud=0.0;
                double longitud=0.0;
                String pais="";
                String localidad="";
                String direccion="";
                if(bound&&localizacion!=null)
                {
                    latitud=localizacion.getLatitud();
                    longitud=localizacion.getLongitud();
                    pais=localizacion.getPais();
                    localidad=localizacion.getLocalidad();
                    direccion=localizacion.getDireccion();
                }
               // String latitudStr=String.format(Locale.getDefault(),"%1$,.sf miles",latitud);
                String latitudStr=String.valueOf(latitud);
                latitudView.setText(latitudStr);
               // String longitudStr=String.format(Locale.getDefault(),"%1$,.sf miles",longitud);
                String longitudStr=String.valueOf(longitud);
                latitudView.setText(longitudStr);
                paisView.setText(pais);
                localidadView.setText(localidad);

                handler.postDelayed(this,5000);
                Log.i("display ubicacion","actualizando interfaz");
            }
        });
    }
}