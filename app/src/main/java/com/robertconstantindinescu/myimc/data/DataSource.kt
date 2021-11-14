package com.robertconstantindinescu.myimc.data

import com.robertconstantindinescu.myimc.AppDatabase
import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.vo.Resource

//Clase que se encarga de conectar con la base de datos para extraer los datos.
/*A esta clase, le pasamos en el construcutr el AppDatabase que es el que nos crea la instancia
* de room para trabajar con la base de datos. Recuerda que esto se ha inicializado justo en la
* vista, es decir en el fragmento MainFragment. Desde allí, hemos inicializado toda  la ruta
* de dependencias. */
class DataSource(private val appDatabase: AppDatabase) {

    //esta función conecta con la base de datos a través del Dao y realizaautomáticamente
    //la operación de extraer todos los datos de la bd. Esto se procesa gracias a room.
    suspend fun getAllImc(): Resource<List<ImcEntity>>{
        //los datos que se extraen se guardan en la clase Resource.Succes. Este metodo retona
        //esa clase con los datos y es lo que el repo cogera, madnará al viewModel y este a la vista.
        return Resource.Succes(appDatabase.imcDao().getAllImcRecords())
    }

    //ídem para insertar. Pero en este caso, debemos pasar un objeto imc. y EL Dao hace la operación de insert
    //automaticamente.
    suspend fun insertImc(imcEntity: ImcEntity){


        appDatabase.imcDao().insertImc(imcEntity)
    }

    suspend fun updateImc(imcEntity: ImcEntity): Int{
       return appDatabase.imcDao().updateImc(imcEntity)
    }

    /**
     * El datasource, es quien opera con la base de datos y hace la operacion llamadno al dao de room
     */
    suspend fun deleteImc(imcEntity: ImcEntity){
          appDatabase.imcDao().deleteImc(imcEntity)
    }
}