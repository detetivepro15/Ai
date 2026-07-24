package com.example.network

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object GeminiPacifier {

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)
        .build()

    suspend fun convertToPeaceTerm(dilemmaText: String): GeneratedPacification = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val systemInstruction = """
                    Você é a inteligência pacificadora do PaciFácil. Sua missão é desarmar atritos e traduzir situações de conflito, discórdia ou pendências jurídicas/comerciais/familiares em um Termo de Entendimento e Paz extrajudicial, focado na conciliação sem litígio civil.
                    
                    Ao receber a descrição da situação:
                    1. Sintetize o problema em linguagem serena, neutra e sem acusações.
                    2. Apresente de 2 a 4 pontos práticos do "DIRETO DA BOA AÇÃO" para encerrar a controvérsia.
                    3. Mantenha um tom nobre, conciliador e solene.
                    
                    Sua resposta DEVE seguir exatamente este formato de texto:
                    ACORDO DE ENTENDIMENTO MÚTUO E BOA FÉ

                    SÍNTESE PACIFICADA:
                    "[Resumo sereno e neutro da situação]"

                    DIRETO DA BOA AÇÃO:
                    1. [Primeira ação de boa fé]
                    2. [Segunda ação de boa fé]
                    3. [Terceira ação de boa fé (opcional)]
                    4. Encerramento integral de atritos e compromisso mútuo de paz e cordialidade.
                """.trimIndent()

                val requestJson = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", "Situação de conflito descrita pelo usuário:\n$dilemmaText")
                                })
                            })
                        })
                    })
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", systemInstruction)
                            })
                        })
                    })
                }

                val requestBody = requestJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                val url = "$BASE_URL?key=$apiKey"

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = okHttpClient.newCall(request).execute()
                val responseString = response.body?.string()

                if (response.isSuccessful && !responseString.isNullOrBlank()) {
                    val root = JSONObject(responseString)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCand = candidates.getJSONObject(0)
                        val content = firstCand.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text", "")
                            if (text.isNotBlank()) {
                                return@withContext GeneratedPacification(
                                    protocol = generateProtocolCode(),
                                    termoText = text.trim(),
                                    actionBadge = "Boa Ação Traduzida por IA",
                                    isAiGenerated = true
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Fallback to local engine below
            }
        }

        // Local Smart Template Generator Fallback
        return@withContext generateLocalPacification(dilemmaText)
    }

    private fun generateProtocolCode(): String {
        val num = Random.nextInt(1000, 9999)
        return "PACI-$num"
    }

    private fun generateLocalPacification(input: String): GeneratedPacification {
        val cleanInput = input.trim()
        val protocol = generateProtocolCode()

        val sintese = if (cleanInput.length > 120) {
            cleanInput.take(120) + "..."
        } else {
            cleanInput
        }

        val termoText = """
            ACORDO DE ENTENDIMENTO MÚTUO E BOA FÉ

            SÍNTESE PACIFICADA:
            "$sintese"

            DIRETO DA BOA AÇÃO:
            1. Encerramento imediato e integral de quaisquer atritos ou impasses decorrentes do fato.
            2. Compromisso mútuo de boa fé, diálogo transparente e resolução harmoniosa das pendências.
            3. Fixação de canal direto para solução de dúvidas sem necessidade de intervenção contenciosa ou litígio civil.
            4. Reafirmação da dignidade, respeito e fraternidade entre as partes envolvidas.
        """.trimIndent()

        return GeneratedPacification(
            protocol = protocol,
            termoText = termoText,
            actionBadge = "Boa Ação Identificada",
            isAiGenerated = false
        )
    }
}

data class GeneratedPacification(
    val protocol: String,
    val termoText: String,
    val actionBadge: String,
    val isAiGenerated: Boolean
)
