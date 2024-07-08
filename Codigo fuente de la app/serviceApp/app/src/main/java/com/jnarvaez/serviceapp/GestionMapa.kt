package com.jnarvaez.serviceapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GestionMapa : FragmentActivity(),OnMapReadyCallback, GoogleMap.OnMapClickListener,
    GoogleMap.OnMapLongClickListener {

    private lateinit var txtLongitud:EditText
    private lateinit var txtLatitud:EditText
    private lateinit var txttitulo:TextView
    private lateinit var mapa:GoogleMap
    private lateinit var btnGuardar:Button
    private lateinit var btnCancelar:Button
    private var id:Int=0
    private lateinit var txtNombre:String
    private lateinit var txtDescripcion:String
    private lateinit var txtAbono:String
    private lateinit var txtFecha:String
    private lateinit var txtObservacion:String
    private var latitud:Double=0.0
    private var longitud:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestion_mapa)

        asignarValores()
        recuperarDatos()
    }
    fun asignarValores(){
        txtLongitud=findViewById(R.id.txtlongitudmp)
        txtLatitud=findViewById(R.id.txtlatitudMapa)
        btnGuardar=findViewById(R.id.btnguardarMapa)
        btnCancelar=findViewById(R.id.btncancelarmapa)
        txttitulo=findViewById(R.id.txtFestividadMapa)

        val mapFragment : SupportMapFragment=supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnGuardar.setOnClickListener {
            actualizar()
        }
        btnCancelar.setOnClickListener {
            finish()
        }
    }
    fun actualizar(){
        val festividad=Festividad(0,txtNombre,txtDescripcion,txtAbono,txtFecha,txtObservacion,latitud,longitud)
        CoroutineScope(Dispatchers.IO).launch {
            val rpta= RetrofitClient.webService.actualizarFestividad(id,festividad)
            runOnUiThread {
                if(rpta.isSuccessful)
                    mostrarMensaje(rpta.body().toString())
            }
        }
    }
    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("informacion")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener{ dialog, which ->
            val intent = Intent(this,Gestion_festividades::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
    fun recuperarDatos(){
        id=intent.getIntExtra("id",0)
        txtNombre=intent.getStringExtra("nombre").toString()
        txttitulo.setText("Festividad: "+txtNombre)
        txtDescripcion=intent.getStringExtra("descripcion").toString()
        txtAbono=intent.getStringExtra("abono").toString()
        txtFecha=intent.getStringExtra("fecha").toString()
        txtObservacion=intent.getStringExtra("observacion").toString()
        latitud=intent.getDoubleExtra("latitud",0.0)
        longitud=intent.getDoubleExtra("longitud",0.0)
        if(latitud!=0.0)
            txtLatitud.setText(""+latitud)
        if(longitud!=0.0)
            txtLongitud.setText(""+longitud)
    }

    override fun onMapReady(p0: GoogleMap) {
        mapa=p0
        mapa.uiSettings.isZoomControlsEnabled=true
        var coordenada=LatLng(-12.038194783331841, -77.06051811587074)
        latitud=intent.getDoubleExtra("latitud",0.0)
        longitud=intent.getDoubleExtra("longitud",0.0)
        if(latitud!=0.0&&longitud!=0.0){
            coordenada=LatLng(latitud, longitud)
        }
        val marcador=MarkerOptions().position(coordenada).title("Local para la festividad: "+txtNombre)
        mapa.addMarker(marcador)
        mapa.moveCamera(CameraUpdateFactory.newLatLng(coordenada))
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenada,17f))

        this.mapa.setOnMapClickListener(this)
        this.mapa.setOnMapLongClickListener(this)
    }

    override fun onMapClick(p0: LatLng) {
        txtLatitud.setText(""+p0.latitude)
        txtLongitud.setText(""+p0.longitude)
        latitud=p0.latitude
        longitud=p0.longitude
        mapa.clear()
        var coordenada=LatLng(latitud, longitud)
        mapa.moveCamera(CameraUpdateFactory.newLatLng(coordenada))
        val marcador=MarkerOptions().position(coordenada).title("Local para la festividad: "+txtNombre)
        mapa.addMarker(marcador)
    }

    override fun onMapLongClick(p0: LatLng) {
        txtLatitud.setText(""+p0.latitude)
        txtLongitud.setText(""+p0.longitude)
        latitud=p0.latitude
        longitud=p0.longitude
        mapa.clear()
        var coordenada=LatLng(latitud, longitud)
        mapa.moveCamera(CameraUpdateFactory.newLatLng(coordenada))
        val marcador=MarkerOptions().position(coordenada).title("Local para la festividad: "+txtNombre)
        mapa.addMarker(marcador)
    }

}