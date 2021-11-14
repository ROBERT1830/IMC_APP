package com.robertconstantindinescu.myimc.domain

import com.robertconstantindinescu.myimc.data.model.ImcEntity
import com.robertconstantindinescu.myimc.vo.Resource

//inTERFÁZ REPO, QUE NOS ASEGURA QUE EL REPO IMPLEMENTARÁ ESTAS FUNCIONES. Es decir, que usado esta
//interfáz y haciendo que RepoImpl la implemente, obligamos a que el Repo tenga estas funcionalidades
interface Repo {

    suspend fun getAllImc(): Resource<List<ImcEntity>>
    suspend fun insertImc(imcEntity: ImcEntity)
    suspend fun updateImc(imcEntity: ImcEntity): Int
    suspend fun deleteImc(imcEntity: ImcEntity)




}