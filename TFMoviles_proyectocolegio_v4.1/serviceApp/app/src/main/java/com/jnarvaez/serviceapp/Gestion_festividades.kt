package com.jnarvaez.serviceapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Gestion_festividades : AppCompatActivity() {

    private lateinit var rvFestividad:RecyclerView
    private lateinit var btnNuevo:FloatingActionButton
    private lateinit var btnRegresar:Button
    private var adaptador:AdaptadorPersonalizadoFestividades = AdaptadorPersonalizadoFestividades()
    private var listaFestividad:ArrayList<Festividad> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gestion_festividades)

        asignarRefencias()
        cargarDatos();
    }

    private fun asignarRefencias(){
        rvFestividad = findViewById(R.id.rvFestividad)
        btnNuevo = findViewById(R.id.btnNuevo)
        btnRegresar = findViewById(R.id.btnRegresar)
        rvFestividad.layoutManager = LinearLayoutManager(this)
        btnNuevo.setOnClickListener {
            val intent = Intent(this,add_festividades::class.java)
            startActivity(intent)
        }
        btnRegresar.setOnClickListener {
            val intent = Intent(this,Menu::class.java)
            startActivity(intent)
        }
        adaptador.contexto(this)//editar

        adaptador.setEliminarItemFestividad{ //eliminar
            eliminar(it.fes_id,it.fes_nombre,it.fes_descripcion,it.fes_observacion,it.fes_fecha)
        }

    }
    fun eliminar(id:Int,nombre:String,descripcion:String, observacion:String, fecha:String){
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Confimación")
        ventana.setMessage("¿Desea eliminar la festividad: "+nombre+"?")
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            CoroutineScope(Dispatchers.IO).launch{
                val rpta=RetrofitClient.webService.eliminarFestividad(id)
                runOnUiThread {
                    if(rpta.isSuccessful)
                        mostrarMensaje(rpta.body().toString())
                }
            }
        })
        ventana.setNegativeButton("Cancelar",null)
        ventana.create().show()
    }

    private  fun mostrarMensaje(mensaje:String){
        val ventana=AlertDialog.Builder(this)
        ventana.setTitle("Información")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent= Intent(this, Gestion_festividades::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
    private fun cargarDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerFestividades()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaFestividad = rpta.body()!!.listaFestividad
                    adaptador.agregarDatos(listaFestividad)
                    mostrarDatos();
                }else{
                    Log.d("===", "Error en servicio")
                }
            }
        }
    }

    private fun mostrarDatos(){
        rvFestividad.adapter = adaptador
    }


}