package com.pft.tracker.ui.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.RecurringTransactionEntity
import com.pft.tracker.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecurringViewModel(private val container: AppContainer) : ViewModel() {

    val plans: StateFlow<List<RecurringTransactionEntity>> = container.recurringTransactionRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleActive(plan: RecurringTransactionEntity) {
        viewModelScope.launch {
            container.recurringTransactionRepository.update(plan.copy(isActive = !plan.isActive))
        }
    }

    fun delete(plan: RecurringTransactionEntity) {
        viewModelScope.launch {
            container.recurringTransactionRepository.delete(plan)
        }
    }
}
