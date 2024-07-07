package com.jnarvaez.serviceapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import com.jnarvaez.serviceapp.entidad.Alumno
import com.jnarvaez.serviceapp.entidad.Calificacion
import com.jnarvaez.serviceapp.entidad.Pago
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityInforme : AppCompatActivity() {

    private lateinit var tvPayments:TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var radiobtnPago1:RadioButton
    private lateinit var radiobtnPago2:RadioButton
    private lateinit var radiobtnPago3:RadioButton
    private lateinit var radiobtnPago4:RadioButton
    private var listaAlumno:ArrayList<Alumno> = ArrayList()
    private var listaPago:ArrayList<Pago> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_informe)

            asignarValores()
            cargarValores()
            llenarValores()
    }
    private fun asignarValores(){
        progressBar = findViewById(R.id.progressBar)
        tvPayments = findViewById(R.id.tvPayments)
        radiobtnPago1=findViewById(R.id.radiobtnPago1)
        radiobtnPago2=findViewById(R.id.radiobtnPago2)
        radiobtnPago3=findViewById(R.id.radiobtnPago3)
        radiobtnPago4=findViewById(R.id.radiobtnPago4)



        // Configurar los botones
        findViewById<Button>(R.id.btnAlumnosNotas).setOnClickListener {
            // Acción para Alumnos + Notas
        }

        findViewById<Button>(R.id.btnPagosAlumnos).setOnClickListener {
            // Acción para Pagos de Alumnos
        }

        findViewById<Button>(R.id.btnHistorialFestividades).setOnClickListener {
            // Acción para Historial festividades
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            finish()
        }
    }
    private fun cargarValores(){
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.obtenerAlumnos()
            val rpta1=RetrofitClient.webService.obtenerPagos()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaAlumno=rpta.body()!!.listaAlumnos
                    listaPago=rpta1.body()!!.listaPagos
                }else{
                    Log.d("===","Error en servicio")
                }
            }
        }
    }
    private fun llenadoAux(tipoPago:Int){
        var numPago=0
        var numPagosTotal=(listaAlumno.size)
        progressBar.max=numPagosTotal
        var numPago1=0
        var numPago2=0
        var numPago3=0
        var numPago4=0
        for (alumno in listaPago){
            if(alumno.pa_pago1==1)
                numPago1+=1
            else if(alumno.pa_pago2==1)
                numPago2+=1
            else if(alumno.pa_pago3==1)
                numPago3+=1
            else if(alumno.pa_pago4==1)
                numPago4+=1
        }
        if(tipoPago==1) numPago=numPago1
        if(tipoPago==2) numPago=numPago2
        if(tipoPago==3) numPago=numPago3
        if(tipoPago==4) numPago=numPago4
        val mensaje="Hay "+numPago+" de "+numPagosTotal+" Pagos "+tipoPago+" en este año"
        tvPayments.setText(mensaje)
            progressBar.progress= numPago
    }
    private fun llenarValores(){
        radiobtnPago1.setOnClickListener {
            radiobtnPago2.isChecked=false
            radiobtnPago3.isChecked=false
            radiobtnPago4.isChecked=false
            llenadoAux(1)
        }
        radiobtnPago2.setOnClickListener {
            radiobtnPago1.isChecked=false
            radiobtnPago3.isChecked=false
            radiobtnPago4.isChecked=false
            llenadoAux(2)
        }
        radiobtnPago3.setOnClickListener {
            radiobtnPago2.isChecked=false
            radiobtnPago1.isChecked=false
            radiobtnPago4.isChecked=false
            llenadoAux(3)
        }
        radiobtnPago4.setOnClickListener {
            radiobtnPago2.isChecked=false
            radiobtnPago3.isChecked=false
            radiobtnPago1.isChecked=false
            llenadoAux(4)
        }
    }
}