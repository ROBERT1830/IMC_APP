package com.robertconstantindinescu.myimc.data.model

import android.os.Parcelable
import android.text.Editable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Date


/*Esta es nuestro modelo de datos. Usamos Entity para denotar que nuestor modelo de datos consiste en
 una tabla con una serie de columnas. un id, nombre etc.... Entity es una etiqueta que usa room
 para convertir nuestra data class en esa tabla.*/
@Entity(tableName = "imc_table")
@Parcelize
data class ImcEntity(
    @PrimaryKey(autoGenerate = true)
    val imcId: Long = 0L,
    @ColumnInfo(name = "nombre")
    var nombre: String = "",

    @ColumnInfo(name = "peso")
    var peso: Float = 0F,

    @ColumnInfo(name = "altura")
    var altura: Float = 0F,

    @ColumnInfo(name = "sexo")
    var sexo: String = "",

    @ColumnInfo(name = "resultado_numero")
    var resultadoNum: Float = 0F,

    @ColumnInfo(name = "resultado_info")
    var resultadoInfo: String = "",

    @ColumnInfo(name = "fecha")
    var fecha: String


//Aunqeu no lo hemos usado, es util tener un toString con los datos.

):Parcelable {


    override fun toString(): String {
        return "ImcEntity(imcId=$imcId, nombre='$nombre', peso=$peso, altura=$altura, sexo='$sexo', resultadoNum=$resultadoNum, resultadoInfo='$resultadoInfo')"
    }
}