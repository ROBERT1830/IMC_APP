package com.robertconstantindinescu.myimc.vo

import java.lang.Exception



/*Las sealed class se usan para almacenar datos en diferentes estados.
* Cada una de estas clases tienen el mismo retorno, porque retornan el mismo tipode dato. Pero a cada una
    * le pasamos un dato distinto con lo que haremos una manipulacion diferente al dato que traeremos
    * del servidor.
    * Yo busco la info en el servidor y luego la traigo a este resource y después
    * desde el ui ya tenemos este resource listo con la info separada en los diferentes estados*/
sealed class Resource<out T>{
    class Loading<out T>: Resource<T>()
    //cuando obtenemos los datos de la bd, los guardamos en succes.
    data class Succes<out T>(val data: T): Resource<T>()
    data class Failure(val exception: Exception): Resource<Nothing>()
}
