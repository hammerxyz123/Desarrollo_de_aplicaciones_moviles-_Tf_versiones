package com.jnarvaez.serviceapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.jnarvaez.serviceapp.entidad.Alumno
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class add_festividades : AppCompatActivity() {

    private lateinit var lbltitulofestividad: TextView
    private lateinit var txtNombre:EditText
    private lateinit var txtDescripcion:EditText
    private lateinit var txtAbono:EditText
    private lateinit var txtFecha:EditText
    private lateinit var txtObservacion:EditText
    private lateinit var btnGuardar:Button
    private lateinit var btnCancelar:Button

    private var modificar:Boolean=false
    private var id:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_festividades)
        asignarReferencias()
        recuperarDatos()
    }

    private fun recuperarDatos(){//editarEscribirDatoExistente
        if(intent.hasExtra("id")){
            modificar=true
            lbltitulofestividad.text="Festividad > Editar"
            id=intent.getIntExtra("id",0)
            txtNombre.setText(intent.getStringExtra("nombre"))
            txtDescripcion.setText(intent.getStringExtra("descripcion"))
            txtAbono.setText(intent.getStringExtra("abono"))
            txtFecha.setText(intent.getStringExtra("fecha"))
            txtObservacion.setText(intent.getStringExtra("observacion"))
        }
    }

    private fun asignarReferencias(){
        lbltitulofestividad=findViewById(R.id.lbltitulofestividad)
        txtNombre = findViewById(R.id.txtNombre)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        txtAbono = findViewById(R.id.txtAbono)
        txtFecha= findViewById(R.id.txtFecha)
        txtObservacion = findViewById(R.id.txtObservacion)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnGuardar.setOnClickListener {
            if(modificar==false)
                agregar()
            else
                actualizar()
        }
        btnCancelar.setOnClickListener{
            val intent= Intent(this, Gestion_festividades::class.java)
            startActivity(intent)
        }
    }
    private fun actualizar(){//editar
        val nombre=txtNombre.text.toString()
        val descripcion=txtDescripcion.text.toString()
        val abono=txtAbono.text.toString()
        val fecha=txtFecha.text.toString()
        val observacion=txtObservacion.text.toString()

        val festividad= Festividad(0,nombre,descripcion,abono,fecha,observacion,0.0,0.0)
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.actualizarFestividad(id,festividad)
            runOnUiThread {
                if(rpta.isSuccessful)
                    mostrarMensaje(rpta.body().toString())
            }
        }
    }

    private fun agregar(){
        val nombre = txtNombre.text.toString()
        val descripcion = txtDescripcion.text.toString()
        val abono = txtAbono.text.toString()
        val fecha = txtFecha.text.toString()
        val observacion = txtObservacion.text.toString()

        val festividad = Festividad( 0,nombre,descripcion,abono,fecha,observacion,0.0,0.0)

        CoroutineScope(Dispatchers.IO).launch{
            val rpta=RetrofitClient.webService.agregarFestividad(festividad)
            runOnUiThread{
                if(rpta.isSuccessful){
                    mostrarMensaje(rpta.body().toString())
                }
            }
        }
    }
    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("informacion")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener{dialog, which ->
            val intent = Intent(this,Gestion_festividades::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}