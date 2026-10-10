package com.simoesctt.cornstore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoesctt.cornstore.data.AppEntry
import com.simoesctt.cornstore.data.CatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState {
    object Loading : UiState
    data class Ready(val apps: List<AppEntry>, val query: String) : UiState
    data class Error(val message: String) : UiState
}

class CatalogViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var allApps: List<AppEntry> = emptyList()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            CatalogRepository.fetch()
                .onSuccess { catalog ->
                    allApps = catalog.apps.sortedBy { it.label.lowercase() }
                    _state.value = UiState.Ready(allApps, "")
                }
                .onFailure { e ->
                    _state.value = UiState.Error(e.message ?: "Unknown error")
                }
        }
    }

    fun search(query: String) {
        val filtered = if (query.isBlank()) {
            allApps
        } else {
            val q = query.lowercase()
            allApps.filter {
                it.label.lowercase().contains(q) ||
                it.packageName.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.topics.any { t -> t.lowercase().contains(q) }
            }
        }
        _state.value = UiState.Ready(filtered, query)
    }
}
