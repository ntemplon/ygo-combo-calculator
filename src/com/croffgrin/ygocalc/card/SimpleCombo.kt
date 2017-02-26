package com.croffgrin.ygocalc.card

/**
 * Copyright (c) 2017 Nathan Templon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
class SimpleCombo(val handSlots: List<(Card) -> Boolean>) : Combo, CompositeCombo.PartialCombo {

    constructor(vararg handSlots: (Card) -> Boolean) : this(handSlots.toList())

    override fun canBePerformedBy(state: GameState): Boolean {
        val hand = state.hand

        val matches = handSlots.map { slot -> hand.filter { card -> slot.invoke(card) } }

        if (matches.any { it.count() == 0 }) {
            return false
        }

        if (matches.count { it.count() > 0 } < handSlots.count()) {
            return false
        }

        return matches(hand, this.handSlots)
    }

    override fun remainderAfterAllocating(startState: GameState): List<GameState> = Companion.remainderAfterAllocating(startState.hand, this.handSlots).map { GameState(hand = it.cards) }

    companion object {
        private fun remainderAfterAllocating(cards: List<Card>, slots: List<(Card) -> Boolean>): List<ComboContinuation> {
            if (slots.count() <= 0) {
                return listOf()
            } else {
                val currentSlot = slots[0]
                if (slots.count() == 1) {
                    return cards.withIndex().filter {
                        currentSlot.invoke(it.value)
                    }.map {
                        val remainingCards = cards.toMutableList()
                        remainingCards.removeAt(it.index)
                        ComboContinuation(remainingCards, slots.takeLast(slots.count() - 1))
                    }
                } else {
                    return cards.withIndex().filter {
                        currentSlot.invoke(it.value)
                    }.flatMap {
                        val remainingCards = cards.toMutableList()
                        remainingCards.removeAt(it.index)
                        remainderAfterAllocating(remainingCards, slots.takeLast(slots.count() - 1))
                    }
                }
            }
        }

        private fun matches(cards: List<Card>, slots: List<(Card) -> Boolean>): Boolean {
            if (slots.count() <= 0) {
                return false
            } else {
                val currentSlot = slots[0]
                if (slots.count() == 1) {
                    return cards.any { currentSlot.invoke(it) }
                } else {
                    return cards.withIndex().filter { currentSlot.invoke(it.value) }.any {
                        val remaining = cards.toMutableList()
                        remaining.removeAt(it.index)
                        return@any matches(remaining, slots.takeLast(slots.size - 1))
                    }
                }
            }
        }
    }

    data class ComboContinuation(val cards: List<Card>, val slots: List<(Card) -> Boolean>)

}
