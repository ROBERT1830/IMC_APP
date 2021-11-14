package com.robertconstantindinescu.myimc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.domain.Repo
import com.robertconstantindinescu.myimc.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Clase que se encarga de recoger los datos del repo según una arquitextura MVVM.
class MainViewModel(private val repo: Repo): ViewModel() {

    //Usando liveData que es reactivo, cada vez qeu hagamos un emit con unos datos
    //se activará el observer de la vista y se mostrarán los datos.
    //Este live data usa algo llamado Dispatchers que por asi decirlo hace operaciones fuera del
    // subproceso principal, evitando así que el hilo principal se bloquee.
    //Es decir, que lo que es la recogida de datos de la base de datos la estmaos haciendo en un
    //segundo hilo. Eso lo hacemos usando Dispatchers.
    fun getAllImc() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            //emitimos los datos que hemos recopilado del repo. (ver como repo busca los datos)
            emit(repo.getAllImc())
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    //para insertar usamos viewModelScope que es una especie de corrutina y launch es una función
    //que crea la corrutina en sí. Este launch, crea una nueva corrutina y hace que se realiza
    // la solicitud de la info en un subproceso diferente.
    //esto sirve apra lo mismo, evitar que la app se bloquee.
    fun insertImc(imcEntity: ImcEntity){
        viewModelScope.launch {
            repo.insertImc(imcEntity)
        }
    }

    fun updateImc(imcEntity: ImcEntity): Int {
        var id: Int = 0
        viewModelScope.launch {
            id = repo.updateImc(imcEntity)
        }
        return id
    }

    /**
     * el mainViewModel llama al repo para que este se encarge del resto de la operación,
     * ya que viewModel no tiene que saber de dónde vienen los datos ni que se hace con ellos.
     * El solo los recibe del repo y se los pasa a la vista. Las demás operaciones, es inconsciente.
     * (Principio de responsabilidad unica)
     */
    fun deleteImC(imcEntity: ImcEntity){


        viewModelScope.launch {
             repo.deleteImc(imcEntity)
        }


    }

}