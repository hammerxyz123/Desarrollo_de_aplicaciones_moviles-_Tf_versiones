package com.jnarvaez.serviceapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jnarvaez.serviceapp.entidad.Alumno
import com.jnarvaez.serviceapp.entidad.Calificacion
import com.jnarvaez.serviceapp.entidad.Pago

class AdaptadorPersonalizadoAlumnos:RecyclerView.Adapter<AdaptadorPersonalizadoAlumnos.MiViewHolder>() {

    private var listaAlumno:ArrayList<Alumno> = ArrayList()
    private var listaCalficacion:ArrayList<Calificacion> = ArrayList()
    private var listaPago:ArrayList<Pago> = ArrayList()
    private lateinit var context: Context
    private var eliminarItemAlumno:((Alumno)->Unit)?=null

    fun setEliminarItemAlumno(call:(Alumno)->Unit){
        this.eliminarItemAlumno=call
    }

        fun agregarAlumno(itemsAlu:ArrayList<Alumno>,itemsCal:ArrayList<Calificacion>,itemsPag:ArrayList<Pago>){
            listaAlumno=itemsAlu
            listaCalficacion=itemsCal
            listaPago=itemsPag
        }

    fun contexto(context:Context){
        this.context=context
    }

    class MiViewHolder (var view: View):RecyclerView.ViewHolder(view){
        private var filaNombres=view.findViewById<TextView>(R.id.filanombresAlu)
        private var filaSeccion=view.findViewById<TextView>(R.id.filaseccionAlu)
        private var filaContacto=view.findViewById<TextView>(R.id.filacontactoalumno)
        var filaEditarAlumno=view.findViewById<ImageButton>(R.id.filaeditaralumno)
        var filaEliminarAlumno=view.findViewById<ImageButton>(R.id.filaeliminaralumno)
        fun setValores(alumno:Alumno){
            filaNombres.text=alumno.alu_nombre
            filaSeccion.text=alumno.alu_seccion
            filaContacto.text=alumno.alu_contacto
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.filaalumno,parent,false)
    )

    override fun getItemCount(): Int {
        return listaAlumno.size
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val AlumnoItem=listaAlumno[position]
        val CalificacionItem=listaCalficacion[position]
        val PagoItem=listaPago[position]
        holder.setValores(AlumnoItem)

        holder.filaEditarAlumno.setOnClickListener {
            val intent = Intent(context, AlumnoAdd::class.java)
            intent.putExtra("id",listaAlumno[position].alu_id)
            intent.putExtra("nombre",listaAlumno[position].alu_nombre)
            intent.putExtra("contacto",listaAlumno[position].alu_contacto)
            intent.putExtra("fecha",listaAlumno[position].alu_fechaNac)
            intent.putExtra("seccion",listaAlumno[position].alu_seccion)
            intent.putExtra("observacion",listaAlumno[position].alu_observacion)
            context.startActivity(intent)
        }
        holder.filaEliminarAlumno.setOnClickListener {
            eliminarItemAlumno?.invoke(AlumnoItem)
        }
    }

}