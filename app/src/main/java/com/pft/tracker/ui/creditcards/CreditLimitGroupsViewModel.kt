package com.pft.tracker.ui.creditcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pft.tracker.data.local.entity.CreditCardEntity
import com.pft.tracker.data.local.entity.CreditLimitGroupEntity
import com.pft.tracker.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GroupRow(
    val group: CreditLimitGroupEntity,
    val memberCards: List<CreditCardEntity>,
    val totalUsed: Double
)

class CreditLimitGroupsViewModel(private val container: AppContainer) : ViewModel() {

    val groups: StateFlow<List<GroupRow>> = combine(
        container.creditLimitGroupRepository.observeAll(),
        container.creditCardRepository.observeAll(),
        container.creditCardRepository.observeAllUsed()
    ) { groups, cards, used ->
        val usedMap = used.associateBy({ it.id }, { it.currentUsed })
        groups.map { g ->
            val members = cards.filter { it.creditLimitGroupId == g.id }
            GroupRow(g, members, members.sumOf { usedMap[it.id] ?: 0.0 })
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCards: StateFlow<List<CreditCardEntity>> = container.creditCardRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveGroup(id: Long, name: String, sharedLimit: Double) {
        viewModelScope.launch {
            val entity = CreditLimitGroupEntity(id = id, name = name, sharedLimit = sharedLimit)
            if (id == 0L) container.creditLimitGroupRepository.insert(entity)
            else container.creditLimitGroupRepository.update(entity)
        }
    }

    fun deleteGroup(group: CreditLimitGroupEntity) {
        viewModelScope.launch {
            val members = allCards.value.filter { it.creditLimitGroupId == group.id }
            members.forEach { container.creditCardRepository.update(it.copy(creditLimitGroupId = null)) }
            container.creditLimitGroupRepository.delete(group)
        }
    }

    fun setCardMembership(card: CreditCardEntity, groupId: Long?) {
        viewModelScope.launch {
            container.creditCardRepository.update(card.copy(creditLimitGroupId = groupId))
        }
    }
}
