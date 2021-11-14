package com.robertconstantindinescu.myimc.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.robertconstantindinescu.myimc.AppDatabase
import com.robertconstantindinescu.myimc.R
import com.robertconstantindinescu.myimc.data.DataSource
import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.databinding.FragmentMainBinding
import com.robertconstantindinescu.myimc.domain.RepoImpl
import com.robertconstantindinescu.myimc.ui.viewmodel.MainViewModel
import com.robertconstantindinescu.myimc.ui.viewmodel.VMFactory
import com.robertconstantindinescu.myimc.vo.Resource
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), MainAdapter.OnItemClickListener {

    //Inlcuir el viewBindig para encontrar los disitintos elementos de la vista
    private lateinit var mBinding: FragmentMainBinding
    //instancia lateinit del adapter para usarlo posteriormente. Se inicializa más tarde
    private lateinit var mAdapter: MainAdapter
    private lateinit var lista: List<ImcEntity>



    //Inicialización del viewModel usando inyección de dependencias. Si nos vijamos en la clase
    /*MainViewModel, su constructor trabaja con una interfáz. Con lo cual no podemos pasarle la
    * instancia del repositorio asi tal cual como una  interfaz. Entonces usamos una clase como
    * auxiliar llamada VMFFactory para contruir una clase a partir de esa interfaz y que si que podremos
    * pasar como parametro en el constructor. */
    private val viewModel by viewModels<MainViewModel> { VMFactory(
        RepoImpl(
            DataSource(AppDatabase.getDatabase(requireActivity().application))))
        /*Esta inicialización lo que está haciendo es decir. Como yo soy un fragment que va a buscar
        * la lista de información al viewModel (patrón mvvm), tengo que generar ya la ruta de acceso a
        * esa info. Por lo tanto, voy a inicializar esa ruta, diciendo de dónde cogeré los datos yo
        * como vista (del viewModel), de dónde los cogerá el viewModel (del repo) y de donde
        * obtendrá esos datos el repositorio (del Datasource)
        * Esto se llama inyección de dependencias. Estamos pasando a una clase como el MainViewModel,
        * otras clases de als cuales depende para poder hacer su trabajo de coger la data y presentarsela
        * a la vista.    */
    }

    //Vacío
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflamos el fragment
        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        //retornamos la raíz
        return mBinding.root
//

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Función que setea el recicler view.
        setupRecyclerView()
        //Función que setea los observadores de LiveData del viewModel. (Explicado bajo)
        setUpObservers()
        //Cuando pulsamos el fab, navegamos al fragment en cuestion
        /**
         * Resulta que si pulsamos el fab, iniciamos el fragment de calcular, pero con un
         * objeto vacio. Esto nos permitira poder hacer la función de añadir.
         * para editar (más abajo) pasaremos el objeto en cuestión.
         */
        val bundle = Bundle()
        bundle.putParcelable("imc", ImcEntity())
        mBinding.fabAddCalcul.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_calculatorFragment, bundle)
        }
    }



    private fun setupRecyclerView() {
        //Inicializamos el adaptador, pasando el contexto, una lista vacia y la interfáz
        //que es this pq esta misma clase implementa la interfáz del adaptador que define el click.
        mAdapter = MainAdapter(requireContext(), listOf(), this)
        //Aplicamos ciertas características al adaptador y lo seteamos.
        mBinding.rvImcList.apply {
            //todos los elementos tendrán el mismo tamaño
            setHasFixedSize(true)
            //definimos el tipo de layout
            mBinding.rvImcList.layoutManager = LinearLayoutManager(requireContext())
            //seteamos el adaptador
            adapter = mAdapter
            //añadimos barra separadora
//            addItemDecoration(
//                DividerItemDecoration(requireContext(),
//                    DividerItemDecoration.VERTICAL))
        }
    }

    private fun setUpObservers() {
        //Usamos LieveData. Este tipo de datos es reactivo y cada vez que
        // suceda un cambio, en este caso en la función getAllImc se ejecutará el
        // bloque de código correspondiente.
        //usamos para ello un observador que "observa ese cambio en los datos".
        /*El observer se debe de unir por así decirlo al live data, e ir observando a medida que el
        * live data emite valores. Puede pasar que este fragment muera y el observer quede en memoria
        * y si se han hecho varias emisiones de datos tendremos muchos observers por ahi en memoria
        * y eso supone un problma. para evitar eso, el observe nos permite porner el dueño del ciclo
        * de vida del fragmento como parámetro. Cuando muere el fragmento, sabe que se pasa por el
        * onDestroy y se rompe esa union con el observer evitando leaks de memoria.
        * Cuando eso pasa, el observer se guarda en una pila y cuando se necesite otra vez se vuele a usar
        * evitando ser creado desde cero otra vez. */

        viewModel.getAllImc().observe(viewLifecycleOwner,  {

            when(it){
                //el tipo de dato que se nos devuelve es del tipo Resource<List<ImcEntity>>.
                //Esta era una sealed class, con lo que en función del tipo de dato
                //haremos una cosa u otra.
                is Resource.Failure -> {}
                is Resource.Loading -> {

                }
                //Cuando los datos son de tipo Succes
                is Resource.Succes -> {

                    //mandaremos los datos al adapter para que los una al recycler.Pero hay que hacer un apunte
                    /*Resulta que nosotros recibimos los datos en formato Resource<List<ImcEntity>>.
                    * pero si nos fijamos en el constructor del adapter, el no puede recibir ese tiepo de
                    * datos, sino una lista normal. Con lo cual usaremos la claususla map
                    * para crear una lista de objetos ImcEntity a partir de los datos obtenidos en el it*/
                     lista = it.data.map {
                        ImcEntity(it.imcId, it.nombre, it.peso, it.altura, it.sexo, it.resultadoNum,
                            it.resultadoInfo)
                    }
                    Log.d("LISTA", lista.toString())
                    //Esa lista creada automaticametnen por el map, la pasamos aqui al adapter.
                    //mAdapter.setImcList(lista)
                    mBinding.rvImcList.adapter = MainAdapter(requireContext(), lista, this)

                }
            }
        })
    }

    /**
     * Cuadno se hace un click en alguno de los elementos, creamos un bundle y llamos al
     * mismo fragmento que usamos para añadir datos, pero ahora pasando los datos
     * del objeto seleccionado como argumentos.
     */
    override fun onImcItemClicl(imcEntity: ImcEntity, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("imc", imcEntity)
        findNavController().navigate(R.id.action_mainFragment_to_calculatorFragment, bundle)

    }

    override fun onDeleteImc(imc: ImcEntity, position: Int) {
        //Nos preparamos un array de strings con las opciones a mostrar
        val items = resources.getStringArray(R.array.array_options_item)
        //Construimos el alert dialog pasandole el contexto y una serie de funciones
        //para setear el titulo y las opciones
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_options_menu)
            .setItems(items) { dialogInterface, i ->
                when (i) {
                    //si el usuario pulsa la primera opcion, llamamos a un metodo para confirmar
                    //la eliminacion
                    0 -> confirmDeleteAction(imc)
                    //está por implementar. Abriremos el email para mandar un correo al entrenador
                    //personal con tus datos de imc.
                    1 -> goToEmail()
                }
            }.show()

    }

    private fun goToEmail() {
        TODO("Not yet implemented")
    }

    fun confirmDeleteAction(imc: ImcEntity){
        //construimos el dialog de confirmación
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_delete_title)
                //seteamos el boton positivo con su titulo y la función respectiva-
            .setPositiveButton(R.string.dialog_delete_confirm,
                DialogInterface.OnClickListener{ //si pulsa esta opcion haremos uso de una interfaz
                    //para gestionar la acción.
                    dialogInterface, which ->
                    //eliminamos el registro imc de la base de datos y se lo notificamos al adapter
                    //para que haga cambios en el recycler. La notificación viene data por una nueva
                    //llamada a setUpObservers() que recopila nuevamente los datos de la bd acutalizada
                    //con el dato borrado. Lo he intentado hacer con notifyItemRemoved
                    // pero no funciona... pq no se actualizaba la lista del adapter. Esta es la
                    //solución que le he dado. Hay que hacerlo más eficaz, pero funciona....
                        viewModel.deleteImC(imc)
                        setUpObservers()
                })
                //si hace click en cancelar, no hacemos nada
            .setNegativeButton(R.string.dialog_delete_cancel, null)
            .show()
    }
}