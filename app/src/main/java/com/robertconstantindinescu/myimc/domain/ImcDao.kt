package com.robertconstantindinescu.myimc.domain

import androidx.room.*
import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.vo.Resource


/*La parte de room Dao es la que se encarga de realizar las oprtaciones sobre nuestra bd.
* En sí es una interfáz que por medio de las etiquetas correspondientes hace funciones determinadas
* como hacer una petición a la bd o insertar datos. */
@Dao
interface ImcDao {
    @Query("SELECT * FROM imc_table")
    suspend fun getAllImcRecords(): List<ImcEntity>
    //el onConflict se utiliza en caso de  insertar datos iguales varias veces. No queremos que se
    //inserten de nuevo, sino que cuando se de esa casuística, se sobreescriba el dato.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImc(imcRecord: ImcEntity)

    @Update
    suspend fun updateImc(imcRecord: ImcEntity): Int
    @Delete
    suspend fun deleteImc(imcRecord: ImcEntity)

    @Query("DELETE FROM imc_table WHERE imcId = :id")
    suspend fun deleteById(id:Int)


}