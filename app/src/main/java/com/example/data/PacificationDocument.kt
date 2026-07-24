package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pacification_documents")
data class PacificationDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val protocol: String,
    val dilemma: String,
    val termoText: String,
    val actionBadge: String = "Boa Ação Identificada",
    val status: String = "Em fase de entendimento e boa fé.",
    val direction: String = "Aguardando validação mútua sem litígio civil.",
    val category: String = "Geral",
    val timestamp: Long = System.currentTimeMillis()
)
