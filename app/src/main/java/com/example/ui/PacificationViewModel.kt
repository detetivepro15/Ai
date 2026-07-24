package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.PacificationDocument
import com.example.data.PacificationRepository
import com.example.network.GeminiPacifier
import com.example.network.GeneratedPacification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PacificationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PacificationRepository

    val allDocuments: StateFlow<List<PacificationDocument>>

    private val _dilemmaInput = MutableStateFlow("")
    val dilemmaInput: StateFlow<String> = _dilemmaInput.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _generatedResult = MutableStateFlow<GeneratedPacification?>(null)
    val generatedResult: StateFlow<GeneratedPacification?> = _generatedResult.asStateFlow()

    private val _lastSavedDoc = MutableStateFlow<PacificationDocument?>(null)
    val lastSavedDoc: StateFlow<PacificationDocument?> = _lastSavedDoc.asStateFlow()

    private val _searchResult = MutableStateFlow<PacificationDocument?>(null)
    val searchResult: StateFlow<PacificationDocument?> = _searchResult.asStateFlow()

    private val _searchExecuted = MutableStateFlow(false)
    val searchExecuted: StateFlow<Boolean> = _searchExecuted.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PacificationRepository(database.pacificationDao())

        allDocuments = repository.allDocuments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun onDilemmaChanged(newText: String) {
        _dilemmaInput.value = newText
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        _searchExecuted.value = false
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun convertAndGenerate() {
        val dilemma = _dilemmaInput.value.trim()
        if (dilemma.isBlank()) return

        viewModelScope.launch {
            _isGenerating.value = true
            _generatedResult.value = null

            val pacification = GeminiPacifier.convertToPeaceTerm(dilemma)
            _generatedResult.value = pacification

            // Auto save to database
            val doc = PacificationDocument(
                protocol = pacification.protocol,
                dilemma = dilemma,
                termoText = pacification.termoText,
                actionBadge = pacification.actionBadge,
                status = "Em fase de entendimento e boa fé.",
                direction = "Aguardando validação mútua sem litígio civil.",
                timestamp = System.currentTimeMillis()
            )
            val id = repository.insertDocument(doc)
            _lastSavedDoc.value = doc.copy(id = id.toInt())

            _isGenerating.value = false
            _snackbarMessage.value = "Termo de Paz ${pacification.protocol} gerado e salvo!"
        }
    }

    fun searchProtocol(code: String = _searchQuery.value) {
        val query = code.trim()
        if (query.isBlank()) return

        viewModelScope.launch {
            _isSearching.value = true
            _searchExecuted.value = true

            val doc = repository.getDirectByProtocol(query)
            _searchResult.value = doc
            _isSearching.value = false
        }
    }

    fun deleteDocument(doc: PacificationDocument) {
        viewModelScope.launch {
            repository.deleteDocument(doc)
            if (_searchResult.value?.id == doc.id) {
                _searchResult.value = null
            }
            _snackbarMessage.value = "Documento ${doc.protocol} removido."
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun setQuickSampleDilemma(sample: String) {
        _dilemmaInput.value = sample
    }
}
