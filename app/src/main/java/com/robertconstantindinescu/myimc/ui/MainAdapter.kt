package com.robertconstantindinescu.myimc.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robertconstantindinescu.myimc.R

import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.databinding.ImcRowBinding
import kotlinx.android.synthetic.main.imc_row.view.*

//El adaptador tiene en el constructor parametros como el contexto, la lista de Imc y una interfaz que
//usaremos más adelante para añadir más funcionalidades a la app.
class MainAdapter(private val context: Context, private var listaImc: List<ImcEntity>,
                 private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<MainAdapter.MainViewHolder>()  {



    //intefáz para gestionar los clics en cada uno de los items del recycler.
    interface OnItemClickListener {
        fun onImcItemClicl(imcEntity: ImcEntity, position: Int)
        fun onDeleteImc(imc: ImcEntity, position: Int)
    }

    fun setImcList(imcList: List<ImcEntity>){
        this.listaImc = imcList
        notifyDataSetChanged()
    }

    //Retorna la vista inflada con todoso los datos  unidos.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        return MainViewHolder(LayoutInflater.from(context).inflate(R.layout.imc_row, parent, false))

    }

    //se encarga de hacer el binding.
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        holder.itemView.setOnClickListener {
//            itemClickListener.onImcItemClicl(listaImc[position])
//        }
        when(holder){
            //cuadno el objeto a unir es de tipo MainViewHolder, llamamos a la función bind del viewHolder que
            //es quien va a realizar el bindeo real. pasaremos el objeto en la posición correspondiente de la lista.
            is MainViewHolder -> {
                with(holder){
                    /**
                     * a cada uno de los elementos, le asociamos un listener para que escuche cuadno
                     * es presionado de forma contínua.
                     */
                    setClick(listaImc[position], position)
                    holder.bind(listaImc[position])
                }


            }
        }

    }



    override fun getItemCount(): Int {
        //retornamos el numero de elementos de la lista para que el adapter sea consciente de ellos.
        return listaImc.size
    }

    inner  class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ImcRowBinding.bind(itemView)
        //hacemos la unión de los datos a cada una de las vistas...
        fun bind(imc: ImcEntity){
            with(binding) {
                tvResultInfo.text = imc.resultadoInfo
                tvResultNum.text = imc.resultadoNum.toString()
                tvName.text = imc.nombre
                tvSexo.text = imc.sexo
                tvPeso.text = imc.peso.toString()
                tvAltura.text = imc.altura.toString()
            }

        }
        fun setClick(imc: ImcEntity, position: Int){
            with(binding.root){
                /**
                 * Listener para hacer la edición de los datos de un elemento seleccionado en
                 * particular.
                 */
                setOnClickListener {
                    itemClickListener.onImcItemClicl(imc, position)
                }
                /**
                 * Listener para hacer la eliminación del elemento seleccionado
                 * Esta interfaz, la implementa el MainFragment para hacer la eliminación
                 */
                setOnLongClickListener {
                    itemClickListener.onDeleteImc(imc, position)
                    true

                }
            }
        }

    }
}