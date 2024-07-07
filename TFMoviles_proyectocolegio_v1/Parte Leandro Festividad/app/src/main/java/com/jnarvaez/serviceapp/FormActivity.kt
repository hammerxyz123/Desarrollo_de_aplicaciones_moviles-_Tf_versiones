package com.jnarvaez.serviceapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormActivity : AppCompatActivity() {

    private lateinit var txtNombre:EditText
    private lateinit var txtDescripcion:EditText
    private lateinit var txtAbono:EditText
    private lateinit var txtFecha:EditText
    private lateinit var txtObservacion:EditText
    private lateinit var btnGuardar:Button
    private lateinit var btnCancelar:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        asignarReferencias()
    }

    private fun asignarReferencias(){
        txtNombre = findViewById(R.id.txtNombre)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        txtAbono = findViewById(R.id.txtAbono)
        txtFecha= findViewById(R.id.txtFecha)
        txtObservacion = findViewById(R.id.txtObservacion)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnGuardar.setOnClickListener {
            agregar()
        }
        btnCancelar.setOnClickListener{
            cancelar()
        }
    }
 private fun  cancelar(){
     finish()
 }
    private fun agregar(){
        val nombre = txtNombre.text.toString()
        val descripcion = txtDescripcion.text.toString()
        val abono = txtAbono.text.toString()
        val fecha = txtFecha.text.toString()
        val observacion = txtObservacion.text.toString()

        val festividad = Festividad( 1,nombre,descripcion,abono,fecha,observacion)

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
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}