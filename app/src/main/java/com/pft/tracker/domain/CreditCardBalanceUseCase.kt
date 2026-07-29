package com.pft.tracker.domain

import com.pft.tracker.data.local.dao.CreditCardDao
import com.pft.tracker.data.local.dao.CreditLimitGroupDao
import com.pft.tracker.data.local.entity.CreditCardEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Implements doc §4.2: currentUsed(card) and availableLimit(card), including the
 * shared credit-limit-group case where all cards in a group report the same
 * remaining limit computed off the group's combined usage.
 */
class CreditCardBalanceUseCase(
    private val cardDao: CreditCardDao,
    private val groupDao: CreditLimitGroupDao
) {

    fun observeCurrentUsed(cardId: Long): Flow<Double> =
        cardDao.observeUsed(cardId).map { it?.currentUsed ?: 0.0 }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeAvailableLimit(card: CreditCardEntity): Flow<Double> {
        val groupId = card.creditLimitGroupId
        if (groupId == null) {
            return observeCurrentUsed(card.id).map { used -> card.creditLimit - used }
        }
        return combine(
            groupDao.observeById(groupId),
            cardDao.observeByGroup(groupId)
        ) { group, cardsInGroup -> group to cardsInGroup }
            .flatMapLatest { (group, cardsInGroup) ->
                if (group == null || cardsInGroup.isEmpty()) {
                    flowOf(0.0)
                } else {
                    val usedFlows = cardsInGroup.map { observeCurrentUsed(it.id) }
                    combine(usedFlows) { usedAmounts -> group.sharedLimit - usedAmounts.sum() }
                }
            }
    }

    fun observeGroupMates(card: CreditCardEntity): Flow<List<CreditCardEntity>> {
        val groupId = card.creditLimitGroupId ?: return flowOf(emptyList())
        return cardDao.observeByGroup(groupId).map { cards -> cards.filter { it.id != card.id } }
    }

    fun observeActiveTotalUsed(): Flow<Double> =
        combine(cardDao.observeActive(), cardDao.observeAllUsed()) { active, used ->
            val activeIds = active.map { it.id }.toSet()
            used.filter { it.id in activeIds }.sumOf { it.currentUsed }
        }
}
