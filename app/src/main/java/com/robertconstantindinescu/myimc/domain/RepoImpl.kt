package com.robertconstantindinescu.myimc.domain

import com.robertconstantindinescu.myimc.data.DataSource
import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.vo.Resource

//contiene la fuente de datos de donde ira a hacer la búsqueda.
class RepoImpl(private val dataSource: DataSource): Repo {
    /*
    * la función de suspensión es una función que se puede iniciar, pausar y reanudar
    * (y pausar y reanudar ... si se desea repetidamente) y luego finalizar.
    * En este caso, esta funcion nos devulve el resultado de otra funcion suspend en
    * el datasource que es el qeu recoge los datos de la bd. Cuadno esta función
    * llama a la siguiente se suspende y cuadno la siguiente acaba y devuelve el resultado, esta
    * se reanuda y hace la devolución del resultado. */
    suspend override fun getAllImc(): Resource<List<ImcEntity>> {
       return dataSource.getAllImc()
    }

    //ídem pero para insertar.
    suspend override fun insertImc(imcEntity: ImcEntity) {
        dataSource.insertImc(imcEntity)
    }

    override suspend fun updateImc(imcEntity: ImcEntity): Int{
        return dataSource.updateImc(imcEntity)
    }

    /**
     * El repo, con la ayuda del datasource le dice que se eliminara x dato en la bd. Es decir,
     * este es como que manda al datasource a que elimine los datos que le llega del viewModel.
     * El repo pasa ese dato particular (un objeto imc) al datasource para que este opere con el
     */
    override suspend fun deleteImc(imcEntity: ImcEntity) {
         dataSource.deleteImc(imcEntity)
    }

    override suspend fun deleteImcById(id: Int) {

        dataSource.deleteImcById(id)

    }
}