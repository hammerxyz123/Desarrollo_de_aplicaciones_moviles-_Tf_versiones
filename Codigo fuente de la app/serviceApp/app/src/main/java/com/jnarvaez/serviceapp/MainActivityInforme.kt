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
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.jnarvaez.serviceapp.databinding.ActivityMainInformeBinding
import com.jnarvaez.serviceapp.entidad.Alumno
import com.jnarvaez.serviceapp.entidad.Calificacion
import com.jnarvaez.serviceapp.entidad.Festividad
import com.jnarvaez.serviceapp.entidad.Pago
import com.jnarvaez.serviceapp.servicio.AlumnoDetalle
import com.jnarvaez.serviceapp.servicio.AlumnoDetallePago
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
    private var listaCalificacion: ArrayList<Calificacion> = ArrayList()

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

        findViewById<Button>(R.id.btnFestividades).setOnClickListener {
            verificarPermisos(it, "festividades")
        }

        findViewById<Button>(R.id.btnAlumnosPagos).setOnClickListener {
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
                    "alumnos" -> obtenerDatosCombinadosAlumnosyNotas()
                    "festividades" -> obtenerFestividades()
                    "pagos" -> obtenerDatosCombinadosAlumnosyPagos()
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
            titulo.alignment = Element.ALIGN_CENTER
            documento.add(titulo)

            val tabla = PdfPTable(5)
            tabla.widthPercentage = 100f
            tabla.setWidths(floatArrayOf(2f, 3f, 1f, 2f, 2f))

            val encabezados = arrayOf(
                "NOMBRE", "DESCRIPCION", "ABONO", "FECHA", "OBSERVACION"
            )

            for (encabezado in encabezados) {
                val cell = PdfPCell(Phrase(encabezado, FontFactory.getFont("arial", 12f, Font.BOLD, BaseColor.WHITE)))
                cell.backgroundColor = BaseColor(0, 121, 182)
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.paddingTop = 5f
                tabla.addCell(cell)
            }

            for (festividad in listaFestividades) {
                tabla.addCell(crearCelda(festividad.fes_nombre))
                tabla.addCell(crearCelda(festividad.fes_descripcion))
                tabla.addCell(crearCelda(festividad.fes_abono))
                tabla.addCell(crearCelda(festividad.fes_fecha))
                tabla.addCell(crearCelda(festividad.fes_observacion))
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






    /*---------------------------------------------------------------------------------------------------*/
    private fun obtenerDatosCombinadosAlumnosyPagos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseAlumnos = RetrofitClient.webService.obtenerAlumnos()
                val responsePagos = RetrofitClient.webService.obtenerPagos()


                if (responseAlumnos.isSuccessful && responsePagos.isSuccessful) {
                    val listaAlumnos = responseAlumnos.body()?.listaAlumnos ?: emptyList()
                    val listaPagos = responsePagos.body()?.listaPagos ?: emptyList()


                    val listaDetallePago = listaAlumnos.map { alumno ->
                        val pago = listaPagos.find { it.pa_id == alumno.alu_id }

                        AlumnoDetallePago(
                            nombre = alumno.alu_nombre,
                            seccion = alumno.alu_seccion,
                            pago1 = pago?.pa_pago1?.toString() ?: "",
                            pago2 = pago?.pa_pago2?.toString() ?: "",
                            pago3 = pago?.pa_pago3?.toString() ?: "",
                            pago4 = pago?.pa_pago4?.toString() ?: "",

                        )
                    }

                    withContext(Dispatchers.Main) {
                        crearPDFDatosCombinadosPagos(listaDetallePago)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivityInforme, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivityInforme, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerDatosCombinadosAlumnosyNotas() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseAlumnos = RetrofitClient.webService.obtenerAlumnos()
                val responseCalificaciones = RetrofitClient.webService.obtenerCalificaciones()

                if (responseAlumnos.isSuccessful && responseCalificaciones.isSuccessful) {
                    val listaAlumnos = responseAlumnos.body()?.listaAlumnos ?: emptyList()
                    val listaCalificaciones = responseCalificaciones.body()?.listaCalificaciones ?: emptyList()

                    val listaDetalles = listaAlumnos.map { alumno ->
                        val calificacion = listaCalificaciones.find { it.cal_id == alumno.alu_id }


                        AlumnoDetalle(
                            nombre = alumno.alu_nombre,
                            seccion = alumno.alu_seccion,
                            nota1 = calificacion?.cal_nota1?.toString() ?: "",
                            nota2 = calificacion?.cal_nota2?.toString() ?: "",
                            nota3 = calificacion?.cal_nota3?.toString() ?: "",
                            nota4 = calificacion?.cal_nota4?.toString() ?: "",
                            observacion = alumno.alu_observacion
                        )
                    }

                    withContext(Dispatchers.Main) {
                        crearPDFDatosCombinadosNotas(listaDetalles)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivityInforme, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivityInforme, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun crearPDFDatosCombinadosNotas(listaDetalles: List<AlumnoDetalle>) {
        try {
            val carpeta = "informes datos combinados de Notas"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$carpeta"

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
                Toast.makeText(this, "CARPETA CREADA", Toast.LENGTH_SHORT).show()
            }
            val archivo = File(dir, "DatosCombinadosNotas.pdf")
            val fos = FileOutputStream(archivo)

            val documento = Document()
            PdfWriter.getInstance(documento, fos)

            documento.open()

            val titulo = Paragraph(
                "Datos Combinados de Alumnos y Notas\n\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLUE)
            )
            titulo.alignment = Element.ALIGN_CENTER
            documento.add(titulo)

            val tabla = PdfPTable(7)
            tabla.widthPercentage = 100f
            tabla.setWidths(floatArrayOf(2f, 1f, 1f, 1f, 1f, 1f, 1f))

            val encabezados = arrayOf(
                "NOMBRE", "SECCION", "NOTA 1", "NOTA 2", "NOTA 3", "NOTA 4",
                 "OBSERVACION"
            )

            for (encabezado in encabezados) {
                val cell = PdfPCell(Phrase(encabezado, FontFactory.getFont("arial", 12f, Font.BOLD, BaseColor.WHITE)))
                cell.backgroundColor = BaseColor(0, 121, 182)
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.paddingTop = 5f
                tabla.addCell(cell)
            }

            for (detalle in listaDetalles) {
                tabla.addCell(crearCelda(detalle.nombre))
                tabla.addCell(crearCelda(detalle.seccion))
                tabla.addCell(crearCelda(detalle.nota1))
                tabla.addCell(crearCelda(detalle.nota2))
                tabla.addCell(crearCelda(detalle.nota3))
                tabla.addCell(crearCelda(detalle.nota4))
                tabla.addCell(crearCelda(detalle.observacion))
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

    private fun crearPDFDatosCombinadosPagos(listaDetalles: List<AlumnoDetallePago>) {
        try {
            val carpeta = "informes datos combinados de Pagos"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$carpeta"

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
                Toast.makeText(this, "CARPETA CREADA", Toast.LENGTH_SHORT).show()
            }
            val archivo = File(dir, "DatosCombinadosPagos.pdf")
            val fos = FileOutputStream(archivo)

            val documento = Document()
            PdfWriter.getInstance(documento, fos)

            documento.open()

            val titulo = Paragraph(
                "Datos Combinados de Alumnos y Pagos\n\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLUE)
            )
            titulo.alignment = Element.ALIGN_CENTER
            documento.add(titulo)

            val tabla = PdfPTable(6)
            tabla.widthPercentage = 100f
            tabla.setWidths(floatArrayOf(2f, 1f, 1f, 1f, 1f, 1f))

            val encabezados = arrayOf(
                "NOMBRE", "SECCION",
                "PAGO 1", "PAGO 2", "PAGO 3", "PAGO 4"
            )

            for (encabezado in encabezados) {
                val cell = PdfPCell(Phrase(encabezado, FontFactory.getFont("arial", 12f, Font.BOLD, BaseColor.WHITE)))
                cell.backgroundColor = BaseColor(0, 121, 182)
                cell.horizontalAlignment = Element.ALIGN_CENTER
                cell.paddingTop = 5f
                tabla.addCell(cell)
            }

            for (detalle in listaDetalles) {
                tabla.addCell(crearCelda(detalle.nombre))
                tabla.addCell(crearCelda(detalle.seccion))
                tabla.addCell(crearCelda(detalle.pago1))
                tabla.addCell(crearCelda(detalle.pago2))
                tabla.addCell(crearCelda(detalle.pago3))
                tabla.addCell(crearCelda(detalle.pago4))
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

    private fun crearCelda(contenido: String): PdfPCell {
        val cell = PdfPCell(Phrase(contenido, FontFactory.getFont("arial", 10f, Font.NORMAL, BaseColor.BLACK)))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.paddingTop = 5f
        cell.borderColor = BaseColor.GRAY
        return cell
    }

}