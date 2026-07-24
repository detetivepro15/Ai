package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PacificationDao {
    @Query("SELECT * FROM pacification_documents ORDER BY timestamp DESC")
    fun getAllDocuments(): Flow<List<PacificationDocument>>

    @Query("SELECT * FROM pacification_documents WHERE LOWER(protocol) = LOWER(:protocol) LIMIT 1")
    fun getByProtocol(protocol: String): Flow<PacificationDocument?>

    @Query("SELECT * FROM pacification_documents WHERE LOWER(protocol) = LOWER(:protocol) LIMIT 1")
    suspend fun getDirectByProtocol(protocol: String): PacificationDocument?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: PacificationDocument): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(docs: List<PacificationDocument>)

    @Delete
    suspend fun deleteDocument(doc: PacificationDocument)

    @Query("SELECT COUNT(*) FROM pacification_documents")
    suspend fun getCount(): Int
}
