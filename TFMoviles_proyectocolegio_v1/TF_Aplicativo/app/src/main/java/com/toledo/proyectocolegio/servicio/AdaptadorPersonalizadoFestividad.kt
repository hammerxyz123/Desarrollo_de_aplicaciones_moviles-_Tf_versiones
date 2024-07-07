package com.toledo.proyectocolegio.servicio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toledo.proyectocolegio.R
import com.toledo.proyectocolegio.entidades.Festividad

class AdaptadorPersonalizadoFestividad : RecyclerView.Adapter<AdaptadorPersonalizadoFestividad.MiViewHolder>() {
    private var listaFestividad:ArrayList<Festividad> = ArrayList()

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        private var filaNombre = view.findViewById<TextView>(R.id.filanombresAlu)
        private var filaDescripcion = view.findViewById<TextView>(R.id.filanombresAlu)

        fun setValores(festividad: Festividad){
            filaNombre.text = festividad.fes_nombre
            filaDescripcion.text = festividad.fes_descipcion
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.filaalumno,parent,false)
    )

    override fun onBindViewHolder(holder: AdaptadorPersonalizadoFestividad.MiViewHolder, position: Int) {
        val festividadItem = listaFestividad[position]
        holder.setValores(festividadItem)
    }

    override fun getItemCount(): Int {
        return listaFestividad.size
    }
}