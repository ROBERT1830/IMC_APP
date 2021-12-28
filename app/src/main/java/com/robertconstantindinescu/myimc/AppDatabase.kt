package com.robertconstantindinescu.myimc

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.domain.ImcDao



//Esta clase es la encargada de crear la base de datos en sí.
/*Usamos la etiqueta @Database para indicar que esta clase será la que defina nuestra bd.
* las entidades las pasamos en forma de array a pesar de que solo tenemos una. La versión es la 1
* y le decimos que queremos tener un historial de la bse de datos. En caso de hacer futuros cambios de version etc..*/
@Database(entities = arrayOf(ImcEntity::class), version = 2, exportSchema = true)
//Al hacer esta clase abstracta lo que vamos a tener que hacer es extender de roomdatabase
//* para decirle a room que esta clase sera el punto de partida en la creacion de la instanica de
// room para poner los datos e irlso a buscar.
abstract class AppDatabase: RoomDatabase() {
   //definimos el dao para poder acceder a el y definir las operaciones
    abstract fun imcDao(): ImcDao

    //creamos el singleton---Explicación

    /*cuando nosotors creamos una isntanica de room, esa misma instanica tiene que persistir por toda
    * la app. esto es debido a que vamos a estar en diferentes sitios de nuestra app usando esta
    * instancia para guardar info y obtenerla. Puede pasar qu en dos fragmentos distintos, tengamos una
    * instancia de appdatabase, luego en otro fragmento otra instancia. Y es más, puede pasar que se este modificando
    * un mismo tipo de dato de la tabla. Con lo cual se da un problema de concurrencia.

    * en una tabala. Con lo que hay un problema de concuerencia en e¡dodne se modificara un mismo elemento a al vez.
    * Esto es un problema porque tenemos dos lugares con dos isntancias distintas que estan modificando la misma tabala y el mimo objeto
    * al mismo tiempoo o casi igual y el resultado va a depender de cual termine primero.*/
    /*
    * Es decir que nada mas arrancar la app, android corre la aplication class que es el putno de partida.  Entonces
    * en ese putno de partida nosotros necesitamos generar ese singleton porque será ese punto de partida el que contendra esta instnica de la base de datos
    * y a partir de ahi esta isntancia va a permanecer por toda la app y sera la misma. */



    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            //si la instancia de la bd, es nula la crea, sino la devuelve.
            INSTANCE = INSTANCE ?: Room.databaseBuilder(//este databaeBuilder lo qeu hace es que te guarda los datos en disco con lo qeu si apagas el movil cierra la app etc.. los datos siguen estando pq crea una tabala interna en room.
                context.applicationContext, AppDatabase::class.java, "imc_table"
            ).build()
            return INSTANCE!!
        }

    }
}



