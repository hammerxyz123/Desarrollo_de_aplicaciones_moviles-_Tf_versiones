package com.jnarvaez.serviceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jnarvaez.serviceapp.entidad.Festividad


class AdaptadorPersonalizadoFestividades:RecyclerView.Adapter<AdaptadorPersonalizadoFestividades.MiViewHolder>() {

    private var listaFestividad:ArrayList<Festividad> = ArrayList()

    fun agregarDatos(items: ArrayList<Festividad>){
        this.listaFestividad = items
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaNombre = view.findViewById<TextView>(R.id.filaNombre)
        private var filaDescripcion = view.findViewById<TextView>(R.id.filaDescripcion)

        fun setValores(festividad: Festividad){
            filaNombre.text = festividad.fes_nombre
            filaDescripcion.text = festividad.fes_descripcion
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) = MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fila2,parent,false)
    )

    override fun onBindViewHolder(holder: AdaptadorPersonalizadoFestividades.MiViewHolder, position: Int) {
        val festividadItem = listaFestividad[position]
        holder.setValores(festividadItem)
    }

    override fun getItemCount(): Int {
        return listaFestividad.size
    }
}