package com.jnarvaez.serviceapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.jnarvaez.serviceapp.entidad.Calificacion
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalificacionAdd : AppCompatActivity() {

    private lateinit var txtAlumnoCalificacion:TextView
    private lateinit var txtNota1:EditText
    private lateinit var txtNota2:EditText
    private lateinit var txtNota3:EditText
    private lateinit var txtNota4:EditText
    private lateinit var txtObservacion:EditText
    private lateinit var btnGuardar:Button
    private lateinit var btnCancelar:Button

    private var id:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calificacion_add)

        asignarReferencias()
        recuperarDatos()
    }

    private fun recuperarDatos(){//editarEscribirDatoExistente
        if(intent.hasExtra("id")){
            id=intent.getIntExtra("id",0)
            txtAlumnoCalificacion.setText(intent.getStringExtra("nombre"))
            txtObservacion.setText(intent.getStringExtra("observacion"))
            txtNota1.setText(intent.getIntExtra("nota1",0).toString())
            txtNota2.setText(intent.getIntExtra("nota2",0).toString())
            txtNota3.setText(intent.getIntExtra("nota3",0).toString())
            txtNota4.setText(intent.getIntExtra("nota4",0).toString())
        }
    }
    private fun asignarReferencias(){
        txtAlumnoCalificacion=findViewById(R.id.txtalumnoNota)
        txtObservacion=findViewById(R.id.txtobservacionNota)
        txtNota1=findViewById(R.id.txtnota1)
        txtNota2=findViewById(R.id.txtnota2)
        txtNota3=findViewById(R.id.txtnota3)
        txtNota4=findViewById(R.id.txtnota_4)
        btnGuardar=findViewById(R.id.btnGuardarNota)
        btnCancelar=findViewById(R.id.btnCancelarNota)
        btnCancelar.setOnClickListener {
            finish()
        }
        btnGuardar.setOnClickListener {
            actualizar()
        }
    }
    private fun actualizar(){
        val nota1=Integer.parseInt(txtNota1.text.toString())
        val nota2=Integer.parseInt(txtNota2.text.toString())
        val nota3=Integer.parseInt(txtNota3.text.toString())
        val nota4=Integer.parseInt(txtNota4.text.toString())
        val observacion=txtObservacion.text.toString()
        val calificacion=Calificacion(0,nota1,nota2,nota3,nota4,observacion)

        CoroutineScope(Dispatchers.IO).launch {
            val rpta= RetrofitClient.webService.actualizarCalificacion(id,calificacion)
            runOnUiThread {
                if(rpta.isSuccessful)
                    mostrarMensaje(rpta.body().toString())
            }
        }
    }

    private  fun mostrarMensaje(mensaje:String){
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent= Intent(this, GestionPago::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}