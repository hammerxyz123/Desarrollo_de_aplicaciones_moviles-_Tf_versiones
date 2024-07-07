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
import com.jnarvaez.serviceapp.entidad.Calificacion
import com.jnarvaez.serviceapp.entidad.Pago
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlumnoAdd : AppCompatActivity() {

    private lateinit var lbltitulo:TextView
    private lateinit var txtNombreAlumno:EditText
    private lateinit var txtFechaNacimientoAlumno:EditText
    private lateinit var txtContactoAlumno:EditText
    private lateinit var txtSeccionAlumno:EditText
    private lateinit var txtObservacionesAlumno:EditText
    private lateinit var btnGuardarAlumno:Button
    private lateinit var btnCancelarAlumno:Button

    private var modificar:Boolean=false
    private var id:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alumno_add)

        asignarValores()
        recuperarDatos()
    }

    private fun recuperarDatos(){//editarEscribirDatoExistente
        if(intent.hasExtra("id")){
            modificar=true
            lbltitulo.text="Gestión Alumnos > Editar"
            id=intent.getIntExtra("id",0)
            txtNombreAlumno.setText(intent.getStringExtra("nombre"))
            txtObservacionesAlumno.setText(intent.getStringExtra("observacion"))
            txtContactoAlumno.setText(intent.getStringExtra("contacto"))
            txtSeccionAlumno.setText(intent.getStringExtra("seccion"))
            txtFechaNacimientoAlumno.setText(intent.getStringExtra("fecha"))
        }
    }

    private fun asignarValores(){
        lbltitulo=findViewById(R.id.lbltitulofestividad)
        txtNombreAlumno=findViewById(R.id.txtaddAlumnombre)
        txtFechaNacimientoAlumno=findViewById(R.id.txtaddAlumnacimiento)
        txtContactoAlumno=findViewById(R.id.txtaddAlumcontacto)
        txtSeccionAlumno=findViewById(R.id.txtaddAlumseccion)
        txtObservacionesAlumno=findViewById(R.id.txtaddAlumobservaciones)
        btnGuardarAlumno=findViewById(R.id.btnguardaralumno)
        btnCancelarAlumno=findViewById(R.id.btncancelaralumno)

        btnCancelarAlumno.setOnClickListener {
            finish()
        }
        btnGuardarAlumno.setOnClickListener {
            if(modificar==false)
                agregar()
            else
                actualizar()
        }
    }

    private fun actualizar(){//editar
        val nombres=txtNombreAlumno.text.toString()
        val contacto=txtContactoAlumno.text.toString()
        val observacion=txtObservacionesAlumno.text.toString()
        val fecha=txtFechaNacimientoAlumno.text.toString()
        val seccion=txtSeccionAlumno.text.toString()

        val alumno=Alumno(0,nombres,fecha,contacto,seccion,observacion)
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.actualizarAlumnos(id,alumno)
            runOnUiThread {
                if(rpta.isSuccessful)
                    mostrarMensaje(rpta.body().toString())
            }
        }
    }

    private fun agregar(){
        val nombres=txtNombreAlumno.text.toString()
        val fecha=txtFechaNacimientoAlumno.text.toString()
        val contacto=txtSeccionAlumno.text.toString()
        val seccion=txtSeccionAlumno.text.toString()
        val observacion=txtObservacionesAlumno.text.toString()

        val alumno=Alumno(1,nombres,fecha,contacto,seccion,observacion)
        val calificacion=Calificacion(1,0,0,0,0,"")
        val pago=Pago(1,0,0,0,0,"")

        CoroutineScope(Dispatchers.IO).launch {
            val rpta1=RetrofitClient.webService.agregarAlumnos(alumno)
            val rpta2=RetrofitClient.webService.agregarCalificaciones(calificacion)
            val rpta3=RetrofitClient.webService.agregarPagos(pago)
            runOnUiThread {
                if(rpta1.isSuccessful)
                    mostrarMensaje(rpta1.body().toString())
            }
        }
    }
    private  fun mostrarMensaje(mensaje:String){
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent= Intent(this, GestionAlumno::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}