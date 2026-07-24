package com.example.data

import kotlinx.coroutines.flow.Flow

class PacificationRepository(private val dao: PacificationDao) {

    val allDocuments: Flow<List<PacificationDocument>> = dao.getAllDocuments()

    fun getByProtocol(protocol: String): Flow<PacificationDocument?> {
        return dao.getByProtocol(protocol.trim())
    }

    suspend fun getDirectByProtocol(protocol: String): PacificationDocument? {
        return dao.getDirectByProtocol(protocol.trim())
    }

    suspend fun insertDocument(doc: PacificationDocument): Long {
        return dao.insertDocument(doc)
    }

    suspend fun deleteDocument(doc: PacificationDocument) {
        dao.deleteDocument(doc)
    }

    suspend fun seedInitialDataIfNeeded() {
        if (dao.getCount() == 0) {
            val samples = listOf(
                PacificationDocument(
                    protocol = "PACI-123",
                    dilemma = "Atrito residencial por conta de barulho de reforma em horários não convencionais.",
                    termoText = """ACORDO DE ENTENDIMENTO MÚTUO E BOA FÉ

SÍNTESE PACIFICADA:
"Atrito residencial referente aos horários de ruído decorrentes de obra no condomínio."

DIRETO DA BOA AÇÃO:
1. Readequação dos horários de reforma para o período comercial (09:00 às 17:00).
2. Comunicação prévia aos vizinhos afetados via canal oficial com 24h de antecedência.
3. Encerramento integral de atritos e restauração do ambiente de cordialidade no condomínio.""".trimIndent(),
                    actionBadge = "Boa Ação Identificada",
                    status = "Em fase de entendimento e boa fé.",
                    direction = "Aguardando validação mútua sem litígio civil.",
                    category = "Vizinhança",
                    timestamp = System.currentTimeMillis() - 86400000L * 2
                ),
                PacificationDocument(
                    protocol = "PACI-1024",
                    dilemma = "Atraso no reembolso de despesas de prestação de serviços freelances.",
                    termoText = """ACORDO DE ENTENDIMENTO MÚTUO E BOA FÉ

SÍNTESE PACIFICADA:
"Ajuste e cronograma pacífico para quitação de honorários de serviço prestado."

DIRETO DA BOA AÇÃO:
1. Parcelamento amigável do saldo devedor em duas parcelas quinzenais sem incidência de juros moratórios.
2. Emissão dos comprovantes de transferência assim que efetuados os depósitos.
3. Compromisso ético de manutenção da parceria profissional futura.""".trimIndent(),
                    actionBadge = "Acordo Financeiro Pacificado",
                    status = "Pacificado e Concluído",
                    direction = "Compromisso firmado com quitação parcial realizada.",
                    category = "Serviços",
                    timestamp = System.currentTimeMillis() - 86400000L * 5
                )
            )
            dao.insertDocuments(samples)
        }
    }
}
