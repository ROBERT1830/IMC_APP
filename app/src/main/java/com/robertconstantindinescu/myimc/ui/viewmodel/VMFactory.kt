package com.robertconstantindinescu.myimc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robertconstantindinescu.myimc.domain.Repo


//Esta clas nos ayudará a crear y mandar una instancia de la clase RepoImp al viewmodel.
//Para qeu esta clase funcione como una facotry class, tenemos que implementar la interfaz que observamos
//que será la que instancie el viewModel
 class VMFactory(private val repo:Repo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        /*Aquí construimos la instancia del repo con el contructor que crea el propio sistema a
        * partir de la interfáz Repo. */
        return modelClass.getConstructor(Repo::class.java).newInstance(repo)
    }
}