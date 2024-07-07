package com.toledo.proyectocolegio

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.toledo.proyectocolegio.entidades.Pago
import com.toledo.proyectocolegio.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PagoAdd : AppCompatActivity() {

    private lateinit var txtAlumnoPago:TextView
    private lateinit var txtObservacion:EditText
    private lateinit var checkPago1:CheckBox
    private lateinit var checkPago2:CheckBox
    private lateinit var checkPago3:CheckBox
    private lateinit var checkPago4:CheckBox
    private lateinit var btnGuardar:Button
    private lateinit var btnCancelar:Button

    private var id:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pago_add)

        asignarReferencias()
        recuperarDatos()
    }

    private fun recuperarDatos(){//editarEscribirDatoExistente
        if(intent.hasExtra("id")){
            id=intent.getIntExtra("id",0)
            txtAlumnoPago.setText(intent.getStringExtra("nombre"))
            txtObservacion.setText(intent.getStringExtra("observacion"))
            val check1:Int=intent.getIntExtra("check1",0)
            val check2:Int=intent.getIntExtra("check2",0)
            val check3:Int=intent.getIntExtra("check3",0)
            val check4:Int=intent.getIntExtra("check4",0)
            if(check1==1) checkPago1.isChecked=true
            if(check2==1) checkPago2.isChecked=true
            if(check3==1) checkPago3.isChecked=true
            if(check4==1) checkPago4.isChecked=true
        }
    }
    private fun asignarReferencias(){
        txtAlumnoPago=findViewById(R.id.txtalumnopago)
        txtObservacion=findViewById(R.id.txtobservacionpago)
        checkPago1=findViewById(R.id.checkpago1)
        checkPago2=findViewById(R.id.checkpago2)
        checkPago3=findViewById(R.id.checkpago3)
        checkPago4=findViewById(R.id.checkpago4)
        btnGuardar=findViewById(R.id.btnGuardarPago)
        btnCancelar=findViewById(R.id.btnCancelarPago)
        btnCancelar.setOnClickListener {
            val intent= Intent(this,GestionPago::class.java)
            startActivity(intent)
        }
        btnGuardar.setOnClickListener {
            actualizar()
        }
    }
    private fun actualizar(){
        var check1:Int=0
        var check2:Int=0
        var check3:Int=0
        var check4:Int=0
        if(checkPago1.isChecked) check1=1
        if(checkPago2.isChecked) check2=1
        if(checkPago3.isChecked) check3=1
        if(checkPago4.isChecked) check4=1
        val observacion=txtObservacion.text.toString()

        val pago=Pago(0,check1,check2,check3,check4,observacion)
        CoroutineScope(Dispatchers.IO).launch {
            val rpta=RetrofitClient.webService.actualizarPagos(id,pago)
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
            val intent= Intent(this,GestionPago::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}