package com.jnarvaez.serviceapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.jnarvaez.serviceapp.databinding.ActivityMainInformeBinding
import com.jnarvaez.serviceapp.entidad.Alumno
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.entidad.Pago
import com.jnarvaez.serviceapp.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class MainActivityInforme : AppCompatActivity() {

    private lateinit var tvPayments: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var radiobtnPago1: RadioButton
    private lateinit var radiobtnPago2: RadioButton
    private lateinit var radiobtnPago3: RadioButton
    private lateinit var radiobtnPago4: RadioButton
    lateinit var binding: ActivityMainInformeBinding
    private var listaAlumno: ArrayList<Alumno> = ArrayList()
    private var listaFestividades: ArrayList<Festividad> = ArrayList()
    private var listaPago: ArrayList<Pago> = ArrayList()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isAccepted ->
        if (isAccepted) Toast.makeText(this, "Permisos CONCEDIDOS", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, "Permisos DENEGADOS", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainInformeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        asignarValores()
        cargarValores()
        llenarValores()
    }

    private fun asignarValores() {
        progressBar = findViewById(R.id.progressBar)
        tvPayments = findViewById(R.id.tvPayments)
        radiobtnPago1 = findViewById(R.id.radiobtnPago1)
        radiobtnPago2 = findViewById(R.id.radiobtnPago2)
        radiobtnPago3 = findViewById(R.id.radiobtnPago3)
        radiobtnPago4 = findViewById(R.id.radiobtnPago4)

        // Configurar los botones
        findViewById<Button>(R.id.btnAlumnosNotas).setOnClickListener {
            verificarPermisos(it, "alumnos")
        }

        findViewById<Button>(R.id.btnHistorialFestividades).setOnClickListener {
            verificarPermisos(it, "festividades")
        }

        findViewById<Button>(R.id.btnPagosAlumnos).setOnClickListener {
            verificarPermisos(it, "pagos")
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            finish()
        }
    }

    private fun cargarValores() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClient.webService.obtenerAlumnos()
            val rpta1 = RetrofitClient.webService.obtenerPagos()
            runOnUiThread {
                if (rpta.isSuccessful) {
                    listaAlumno = rpta.body()!!.listaAlumnos
                    listaPago = rpta1.body()!!.listaPagos
                } else {
                    Log.d("===", "Error en servicio")
                }
            }
        }
    }

    private fun llenadoAux(tipoPago: Int) {
        var numPago = 0
        val numPagosTotal = listaAlumno.size
        progressBar.max = numPagosTotal
        var numPago1 = 0
        var numPago2 = 0
        var numPago3 = 0
        var numPago4 = 0
        for (pago in listaPago) {
            if (pago.pa_pago1 == 1)
                numPago1 += 1
            if (pago.pa_pago2 == 1)
                numPago2 += 1
            if (pago.pa_pago3 == 1)
                numPago3 += 1
            if (pago.pa_pago4 == 1)
                numPago4 += 1
        }
        numPago = when (tipoPago) {
            1 -> numPago1
            2 -> numPago2
            3 -> numPago3
            else -> numPago4
        }
        val mensaje = "Hay $numPago de $numPagosTotal Pagos $tipoPago en este año"
        tvPayments.text = mensaje
        progressBar.progress = numPago
    }

    private fun llenarValores() {
        radiobtnPago1.setOnClickListener {
            radiobtnPago2.isChecked = false
            radiobtnPago3.isChecked = false
            radiobtnPago4.isChecked = false
            llenadoAux(1)
        }
        radiobtnPago2.setOnClickListener {
            radiobtnPago1.isChecked = false
            radiobtnPago3.isChecked = false
            radiobtnPago4.isChecked = false
            llenadoAux(2)
        }
        radiobtnPago3.setOnClickListener {
            radiobtnPago2.isChecked = false
            radiobtnPago1.isChecked = false
            radiobtnPago4.isChecked = false
            llenadoAux(3)
        }
        radiobtnPago4.setOnClickListener {
            radiobtnPago2.isChecked = false
            radiobtnPago3.isChecked = false
            radiobtnPago1.isChecked = false
            llenadoAux(4)
        }
    }

    private fun verificarPermisos(view: View, tipo: String) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "PERMISOS CONCEDIDOS", Toast.LENGTH_SHORT).show()
                when (tipo) {
                    "alumnos" -> obtenerAlumnos()
                    "festividades" -> obtenerFestividades()
                    "pagos" -> obtenerPagos()
                }
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                Snackbar.make(
                    view,
                    "ESTE PERMISO ES NECESARIO PARA CREAR EL ARCHIVO",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") {
                    requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }.show()
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun obtenerAlumnos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerAlumnos()

                if (response.isSuccessful) {
                    response.body()?.let {
                        listaAlumno = it.listaAlumnos
                        withContext(Dispatchers.Main) {
                            crearPDFAlumnos()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivityInforme, "Error al obtener alumnos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivityInforme, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerFestividades() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerFestividades()
                if (response.isSuccessful) {
                    response.body()?.let {
                        listaFestividades = it.listaFestividad
                        withContext(Dispatchers.Main) {
                            crearPDFFestividades()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivityInforme, "Error al obtener festividad", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivityInforme, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerPagos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerPagos()
                if (response.isSuccessful) {
                    response.body()?.let {
                        listaPago = it.listaPagos
                        withContext(Dispatchers.Main) {
                            crearPDFPagos()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivityInforme, "Error al obtener pagos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivityInforme, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun crearPDFAlumnos() {
        try {
            val carpeta = "informes alumnos"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$carpeta"

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
                Toast.makeText(this, "CARPETA CREADA", Toast.LENGTH_SHORT).show()
            }
            val archivo = File(dir, "Alumnos.pdf")
            val fos = FileOutputStream(archivo)

            val documento = Document()
            PdfWriter.getInstance(documento, fos)

            documento.open()

            val titulo = Paragraph(
                "Lista de alumnos \n\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLUE)
            )
            documento.add(titulo)

            val tabla = PdfPTable(5)
            tabla.addCell("NOMBRE")
            tabla.addCell("FECHA DE NACIMIENTO")
            tabla.addCell("CONTACTO")
            tabla.addCell("SECCION")
            tabla.addCell("OBSERVACION")

            for (i in listaAlumno.indices) {
                tabla.addCell(listaAlumno[i].alu_nombre)
                tabla.addCell(listaAlumno[i].alu_fechaNac)
                tabla.addCell(listaAlumno[i].alu_contacto)
                tabla.addCell(listaAlumno[i].alu_seccion)
                tabla.addCell(listaAlumno[i].alu_observacion)
            }
            documento.add(tabla)

            documento.close()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: DocumentException) {
            e.printStackTrace()
        }
    }

    private fun crearPDFFestividades() {
        try {
            val carpeta = "informes festividades"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$carpeta"

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
                Toast.makeText(this, "CARPETA CREADA", Toast.LENGTH_SHORT).show()
            }
            val archivo = File(dir, "Festividades.pdf")
            val fos = FileOutputStream(archivo)

            val documento = Document()
            PdfWriter.getInstance(documento, fos)

            documento.open()

            val titulo = Paragraph(
                "Lista de festividades \n\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLUE)
            )
            documento.add(titulo)

            val tabla = PdfPTable(5)
            tabla.addCell("NOMBRE")
            tabla.addCell("DESCRIPCION")
            tabla.addCell("ABONO")
            tabla.addCell("FECHA")
            tabla.addCell("OBSERVACION")

            for (i in listaFestividades.indices) {
                tabla.addCell(listaFestividades[i].fes_nombre)
                tabla.addCell(listaFestividades[i].fes_descripcion)
                tabla.addCell(listaFestividades[i].fes_abono)
                tabla.addCell(listaFestividades[i].fes_fecha)
                tabla.addCell(listaFestividades[i].fes_observacion)
            }
            documento.add(tabla)

            documento.close()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: DocumentException) {
            e.printStackTrace()
        }
    }

    private fun crearPDFPagos() {
        try {
            val carpeta = "informes pagos"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$carpeta"

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
                Toast.makeText(this, "CARPETA CREADA", Toast.LENGTH_SHORT).show()
            }
            val archivo = File(dir, "Pagos.pdf")
            val fos = FileOutputStream(archivo)

            val documento = Document()
            PdfWriter.getInstance(documento, fos)

            documento.open()

            val titulo = Paragraph(
                "Lista de pagos \n\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLUE)
            )
            documento.add(titulo)

            val tabla = PdfPTable(4)
            tabla.addCell("PAGO 1")
            tabla.addCell("PAGO 2")
            tabla.addCell("PAGO 3")
            tabla.addCell("PAGO 4")

            for (i in listaPago.indices) {
                tabla.addCell(listaPago[i].pa_pago1.toString())
                tabla.addCell(listaPago[i].pa_pago2.toString())
                tabla.addCell(listaPago[i].pa_pago3.toString())
                tabla.addCell(listaPago[i].pa_pago4.toString())
            }
            documento.add(tabla)

            documento.close()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: DocumentException) {
            e.printStackTrace()
        }
    }
}
