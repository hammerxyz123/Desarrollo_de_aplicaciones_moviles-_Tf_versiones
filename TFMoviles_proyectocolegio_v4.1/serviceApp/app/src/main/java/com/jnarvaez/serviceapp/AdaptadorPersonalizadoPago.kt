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

class AdaptadorPersonalizadoPago:RecyclerView.Adapter<AdaptadorPersonalizadoPago.MiViewHolder> (){

    private var listaAlumno:ArrayList<Alumno> = ArrayList()
    private var listaPago:ArrayList<Pago> = ArrayList()
    private var listaCalificacion:ArrayList<Calificacion> = ArrayList()
    private lateinit var context: Context


        fun agregarAlumno(itemsAlu:ArrayList<Alumno>,itemsPag:ArrayList<Pago>,itemsCal:ArrayList<Calificacion>){
            listaAlumno=itemsAlu
            listaPago=itemsPag
            listaCalificacion=itemsCal
        }
        fun contexto(context:Context){
            this.context=context
        }

    class MiViewHolder (var view: View):RecyclerView.ViewHolder(view){
        private var filaNombres=view.findViewById<TextView>(R.id.filanombresPago)
        private var filaFalta=view.findViewById<TextView>(R.id.filaestadoPago2)
        private var filaEstadoPago=view.findViewById<TextView>(R.id.filaestadoPago1)
        private var filaEstadoNota=view.findViewById<TextView>(R.id.filaestadonota)

        var filaEditarPago=view.findViewById<ImageButton>(R.id.filaeditarpago)
        var filaEditarNota=view.findViewById<ImageButton>(R.id.filaeditarnota)
        fun setValores(alumno:Alumno,pago: Pago,calificacion:Calificacion){
            filaNombres.text=alumno.alu_nombre
            var estado=true
            var falta="Falta:"
            if(0==pago.pa_pago1){
                falta+=" Pago 1,"
                estado=false
            }
            if(0==pago.pa_pago2){
                falta+=" Pago 2,"
                estado=false
            }
            if(0==pago.pa_pago3){
                falta+=" Pago 3,"
                estado=false
            }
            if(0==pago.pa_pago4){
                falta+=" Pago 4"
                estado=false
            }
            if(estado){
                filaEstadoPago.text="Pagos al Día!!"
                filaFalta.text=""
            }
            else{
                filaEstadoPago.text="Incompleto"
                filaFalta.text=falta
            }
            var promedio=(calificacion.cal_nota1+calificacion.cal_nota2+calificacion.cal_nota3
                    +calificacion.cal_nota4)/4
            if(promedio>=11)
                filaEstadoNota.text="Aprobado/a"
            else
                filaEstadoNota.text="Desaprobado"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.filapago,parent,false)
        )

    override fun getItemCount(): Int {
        return listaAlumno.size
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val AlumnoItem=listaAlumno[position]
        val PagoItem=listaPago[position]
        val CalificaicionItem=listaCalificacion[position]
        holder.setValores(AlumnoItem,PagoItem,CalificaicionItem)

        holder.filaEditarPago.setOnClickListener {
            val intent = Intent(context, PagoAdd::class.java)
            intent.putExtra("id",listaPago[position].pa_id)
            intent.putExtra("nombre",listaAlumno[position].alu_nombre)
            intent.putExtra("observacion",listaPago[position].pa_observacion)
            intent.putExtra("check1",listaPago[position].pa_pago1)
            intent.putExtra("check2",listaPago[position].pa_pago2)
            intent.putExtra("check3",listaPago[position].pa_pago3)
            intent.putExtra("check4",listaPago[position].pa_pago4)

            context.startActivity(intent)
        }
        holder.filaEditarNota.setOnClickListener {
            val intent=Intent(context, CalificacionAdd::class.java)
            intent.putExtra("id",listaCalificacion[position].cal_id)
            intent.putExtra("nombre",listaAlumno[position].alu_nombre)
            intent.putExtra("nota1",listaCalificacion[position].cal_nota1)
            intent.putExtra("nota2",listaCalificacion[position].cal_nota2)
            intent.putExtra("nota3",listaCalificacion[position].cal_nota3)
            intent.putExtra("nota4",listaCalificacion[position].cal_nota4)
            intent.putExtra("observacion",listaCalificacion[position].cal_observacion)

            context.startActivity(intent)
        }
    }

}