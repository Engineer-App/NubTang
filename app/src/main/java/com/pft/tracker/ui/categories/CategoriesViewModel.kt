package com.pft.tracker.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.CategoryEntity
import com.pft.tracker.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriesViewModel(private val container: AppContainer) : ViewModel() {

    val categories: StateFlow<List<CategoryEntity>> = container.categoryRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun save(id: Long, name: String, type: String, monthlyBudget: Double?, existing: CategoryEntity?) {
        viewModelScope.launch {
            val entity = CategoryEntity(
                id = id,
                name = name,
                categoryType = type,
                icon = existing?.icon,
                monthlyBudget = monthlyBudget,
                displayOrder = existing?.displayOrder ?: 0
            )
            if (id == 0L) container.categoryRepository.insert(entity)
            else container.categoryRepository.update(entity)
        }
    }

    fun delete(category: CategoryEntity) {
        viewModelScope.launch { container.categoryRepository.delete(category) }
    }
}
