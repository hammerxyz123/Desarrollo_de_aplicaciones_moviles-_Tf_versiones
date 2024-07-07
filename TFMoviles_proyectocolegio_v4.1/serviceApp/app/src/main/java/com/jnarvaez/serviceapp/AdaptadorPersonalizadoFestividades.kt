package com.jnarvaez.serviceapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jnarvaez.serviceapp.entidad.Festividad


class AdaptadorPersonalizadoFestividades:RecyclerView.Adapter<AdaptadorPersonalizadoFestividades.MiViewHolder>() {

    private var listaFestividad:ArrayList<Festividad> = ArrayList()
    private lateinit var context: Context
    private var eliminarItemFestividad:((Festividad)->Unit)?=null

    fun setEliminarItemFestividad(call:(Festividad)->Unit){
        this.eliminarItemFestividad=call
    }
    fun agregarDatos(itemsFes: ArrayList<Festividad>){
        this.listaFestividad = itemsFes
    }
    fun contexto(context: Context){
        this.context=context
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaNombre = view.findViewById<TextView>(R.id.filaNombre)
        private var filaDescripcion = view.findViewById<TextView>(R.id.filaDescripcion)
        private var filaObservacion = view.findViewById<TextView>(R.id.filaObservacion)
        private var filafecha = view.findViewById<TextView>(R.id.filaFecha)
        var filaEditarFestividad = view.findViewById<Button>(R.id.filaEditarFestividad)
        var filaEliminarFestividad = view.findViewById<Button>(R.id.filaEliminar)
        var filaMapaFestividad=view.findViewById<ImageButton>(R.id.filabtnmapita)
        fun setValores(festividad: Festividad){
            filaNombre.text = festividad.fes_nombre
            filaDescripcion.text = festividad.fes_descripcion
            filaObservacion.text = festividad.fes_observacion
            filafecha.text = festividad.fes_fecha
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) = MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fila2,parent,false)
    )


    override fun getItemCount(): Int {
        return listaFestividad.size
    }
    override fun onBindViewHolder(holder: AdaptadorPersonalizadoFestividades.MiViewHolder, position: Int) {
        val FestividadItem=listaFestividad[position]
        holder.setValores(FestividadItem)

        holder.filaEditarFestividad.setOnClickListener {
            val intent = Intent(context, add_festividades::class.java)
            intent.putExtra("id",listaFestividad[position].fes_id)
            intent.putExtra("nombre",listaFestividad[position].fes_nombre)
            intent.putExtra("descripcion",listaFestividad[position].fes_descripcion)
            intent.putExtra("abono",listaFestividad[position].fes_abono)
            intent.putExtra("fecha",listaFestividad[position].fes_fecha)
            intent.putExtra("observacion",listaFestividad[position].fes_observacion)
            context.startActivity(intent)
        }
        holder.filaMapaFestividad.setOnClickListener {
            val intent = Intent(context, GestionMapa::class.java)
            intent.putExtra("id",listaFestividad[position].fes_id)
            intent.putExtra("nombre",listaFestividad[position].fes_nombre)
            intent.putExtra("descripcion",listaFestividad[position].fes_descripcion)
            intent.putExtra("abono",listaFestividad[position].fes_abono)
            intent.putExtra("fecha",listaFestividad[position].fes_fecha)
            intent.putExtra("observacion",listaFestividad[position].fes_observacion)
            intent.putExtra("latitud",listaFestividad[position].fes_latitud)
            intent.putExtra("longitud",listaFestividad[position].fes_longitud)
            context.startActivity(intent)
        }
        holder.filaEliminarFestividad.setOnClickListener {
            eliminarItemFestividad?.invoke(FestividadItem)
        }
    }
}