package com.robertconstantindinescu.myimc.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.robertconstantindinescu.myimc.AppDatabase
import com.robertconstantindinescu.myimc.R
import com.robertconstantindinescu.myimc.data.DataSource
import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.databinding.FragmentCalculatorBinding
import com.robertconstantindinescu.myimc.domain.RepoImpl
import com.robertconstantindinescu.myimc.ui.viewmodel.MainViewModel
import com.robertconstantindinescu.myimc.ui.viewmodel.VMFactory
import kotlinx.android.synthetic.main.fragment_calculator.*
import kotlinx.android.synthetic.main.fragment_calculator.tv_resultInfo
import kotlinx.android.synthetic.main.fragment_calculator.tv_resultNum
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


class CalculatorFragment : Fragment() {

    //instanciación del viewmodel.
    private val viewModel by viewModels<MainViewModel> {
        VMFactory(
            RepoImpl(
                DataSource(AppDatabase.getDatabase(requireActivity().application))
            )
        )
    }

    private lateinit var mAdapter: MainAdapter

    //variable lateinit del binding
    private lateinit var mBinding: FragmentCalculatorBinding

    //variable lateinit del radiobutton para sacar el texto de cada radioButton.
    private lateinit var radioButton: RadioButton
    private lateinit var imc: ImcEntity
    var fromRecycler: Boolean = false
    var date: String? = null


    //vacío pq no pasamos argumentos por bundle a este fragment.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Obtenemos los argumentos del fragment anterior. En función de si clicamos en el recycler
         * o en el fab, tendremos unos argumetnos u otros.
         */
//        requireArguments().let {
//            imc = it.getParcelable<ImcEntity>("imc")!!
//        }


    }

    //inflamos el fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         *Comprobamos si el objeto que nos viene tiene id distinto de 0.
         * En la tabla, el id está seteado a 0, con lo cual si pasamos un objeto vacio
         * el id es cero. Si es cero no rellenamos los campos pq estamos en insertar
         * Pero si es diferente de cero llamaos a setUi para rellenar los campos
         * e indicamos que venimos del recycler.
         */


        //variable bool para marcas si se ha clicado el btn calcular.
        var btnCalcularClicked = false
        //función para obtener el texto de cada radio button en función de cual está clicado.
        getRadioButtonData()
        //varaible para almacenar el resultado de la operacion imc.
        var resultNum: Float = 0F

        //listener para boton de save.
        mBinding.btnSave.setOnClickListener {
            //si se han validado los campos
            if (validateFields()) {
                //y si no se ha pulsado el btn calcular, haremos el calculo nuevamente. (Es decir, que
                // pulsando el botón de guardar también te hace el calculo en caso de que no pulses
                // el de calcular. Se podría quitar por ejemplo el botón de calcular
                // y dejar el de guardar que haga las dos funciones y el codigo queda mas limpio)
                //if (!btnCalcularClicked) {
                    getCurrentDate()
                    tv_resultNum.text = calculateResultImc().toString()
                    resultNum = tv_resultNum.text.toString().toFloat()
                    tv_resultInfo.text = setResultInfo(resultNum)


                    val items = resources.getStringArray(R.array.array_options_saveItem)
                    //Construimos el alert dialog pasandole el contexto y una serie de funciones
                    //para setear el titulo y las opciones
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.dialog_options_menu2)
                        .setItems(items) { dialogInterface, i ->
                            when (i) {
                                0 -> {
                                    insertImc(resultNum)
                                    clearFields()
                                    Snackbar.make(
                                        mBinding.root,
                                        getString(R.string.save_succesfully),
                                        Snackbar.LENGTH_SHORT
                                    ).show()

                                }
                                1 -> {
                                    return@setItems
                                }
                            }
                        }.show()

            }

        }
    }

    /**
     * Método que obtiene la fecha directamente con el formato adecuado en el que se indica el mes con
     * el respectivo nombre.
     */
    private fun getCurrentDate() {
        val d = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MMMM-yyyy", Locale("es", "ES"))
        val strDate = dateFormat.format(d)
        date = strDate
    }


    private fun insertImc(resultNum: Float) {
        fromRecycler = false
        /**
         * insertamos llamando al viewModel. Pero haciendo la construcción del objeto y sus propiedades.
         */
        // TODO: 14/11/21 HACER QUE SE GUARDE LA FEHCA AUTOMÁTICAMENTE.
        viewModel.insertImc(
            ImcEntity(
                nombre = setName(),
                peso = etWeigth.text.toString().toFloat(),
                altura = etHeight.text.toString().toFloat(),
                sexo = radioButton.text.toString(),
                resultadoNum = tv_resultNum.text.toString().toFloat(),
                resultadoInfo = setResultInfo(resultNum),
                fecha = date!!


            )
        )

        val text: String = mBinding.tvResultInfo.text.toString()
        val bundle = Bundle()
        bundle.putString("imc", text)
        val fragment = MainFragment()
        fragment.arguments = bundle



    }

    private fun updateImc() {
        /**
         * con el objeto en cuestión, vamos a modificar sus propiedades y llamamos al viewModel
         * para que se desencadene la acción de modificar.
         */
        with(imc) {
            nombre = mBinding.etName.text.toString().trim()
            peso = mBinding.etWeigth.text.toString().toFloat()
            altura = mBinding.etHeight.text.toString().toFloat()
            resultadoNum = mBinding.tvResultNum.text.toString().toFloat()
            resultadoInfo = mBinding.tvResultInfo.text.toString().trim()
        }
        var id = viewModel.updateImc(imc)
    }

    private fun setUi(imc: ImcEntity) {

        //seteamos os campos.
        with(mBinding) {
            etName.setText(imc.nombre)
            etWeigth.setText(imc.peso.toString())
            etHeight.setText(imc.altura.toString())
            if (imc.sexo.equals("Hombre")) {
                rb_sex_grouprb_sex_group.check(R.id.rb_sex_man);

            } else rb_sex_grouprb_sex_group.check(R.id.rb_sex_woman);
        }
    }


    private fun setName(): String {
        var txt: String = ""
        //si el nombre está vacio se guarda anónimo para aquellos que tiene un poquillo de verguenza
        if (etName.text.toString().isEmpty()) {
            txt = "Anónimo"
        }
        //sinó, se guarda el nombre y afrontas la realidad :)
        else {
            txt = etName.text.toString().trim()

        }
        return txt
    }

    //limpiado de campos
    private fun clearFields() {
        if (etName.text != null) {
            etName.getText()!!.clear()
        }

        etWeigth.getText()!!.clear()
        etHeight.getText()!!.clear()
    }

    //validación de campos.
    //fijarse que se ha hecho la validación al revés del órden en que están los campos
    //esto es para que cuando se haga el focus, empiece por el primer campo
    private fun validateFields(): Boolean {

        var isValid = true

        if (mBinding.etHeight.text.toString().trim().isEmpty()) {
            mBinding.tilHeight.error = getString(R.string.required)
            mBinding.etHeight.requestFocus()
            isValid = false
        }
        if (mBinding.etWeigth.text.toString().trim().isEmpty()) {
            mBinding.tilWeight.error = getString(R.string.required)
            mBinding.etWeigth.requestFocus()
            isValid = false
        }
        //devolvemos true o false si se han validad o no.
        return isValid

    }

    //calculo de imc.
    private fun calculateResultImc(): Float {
        var result: Float = 0F
        var weigth = mBinding.etWeigth.text.toString().toFloat()
        var height = mBinding.etHeight.text.toString().toFloat()
        result = weigth / (height * height) * 10000
        return String.format("%.2f", result).toFloat()
    }

    //devolvemos el tupo de string con el mensaje adecuado en función del sexo para setear el resultadoInfo
    private fun setResultInfo(result: Float): String {
        var resultInfo: String = ""
        if (mBinding.rbSexMan.isChecked) {
            if (result < 18.5) resultInfo = getString(R.string.peso1)
            if (result >= 18.5 && result <= 24.9) resultInfo = getString(R.string.peso2)
            if (result >= 25.0 && result <= 29.9) resultInfo = getString(R.string.peso3)
            if (result > 30.0) resultInfo = getString(R.string.peso4)
        }
        if (mBinding.rbSexWoman.isChecked) {
            if (result < 18.5) resultInfo = getString(R.string.peso1)
            if (result >= 18.5 && result <= 23.9) resultInfo = getString(R.string.peso2)
            if (result >= 24.0 && result <= 28.9) resultInfo = getString(R.string.peso3)
            if (result > 29.0) resultInfo = getString(R.string.peso4)
        }
        return resultInfo
    }

    //cogemos el texto de los radio button y seteamos la variable radioButton con ese id en particular.
    //luego para crear el objeto cogeremos el texto de ese radio button.
    private fun getRadioButtonData() {
        mBinding.rbSexGrouprbSexGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val idRbChecked: RadioButton = mBinding.rbSexGrouprbSexGroup.findViewById(checkedId)
                this.radioButton = idRbChecked

            }
        )

    }

}