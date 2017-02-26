package com.croffgrin.ygocalc.card

import java.nio.file.Path
import java.util.*

/**
 * Copyright (c) 2016 Nathan S. Templon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
class CardSet {
    private val _cards = ArrayList<Card>(60)

    val cards: List<Card> = _cards
    val count: Int
        get() = this.cards.count()

    /**
     * Indices go from 1 to [count]
     */
    operator fun get(index: Int): Card {
        if (index < 1 || index > this.count) {
            throw IllegalArgumentException("The index must be between 1 and the number of cards in the deck (${this.count}).")
        }

        return this.cards[index - 1]
    }

    /**
     * If [card] is null, then the card at that position is removed.  Else, the card at that position is replaced
     * Indices go from 1 to [count]
     */
    operator fun set(index: Int, card: Card?) {
        if (index < 1 || index > this.count) {
            throw IllegalArgumentException("The index must be between 1 and the number of cards in the deck (${this.count}).")
        }

        if (card == null) {
            this.removeAt(index)
        } else {
            this._cards[index - 1] = card
        }
    }

    /**
     * Indices go from 1 to [count]
     * @return The card that was removed from the deck.
     */
    fun removeAt(index: Int): Card {
        if (index < 1 || index > this.count) {
            throw IllegalArgumentException("The index must be between 1 and the number of cards in the deck (${this.count}).")
        }

        return this._cards.removeAt(index - 1)
    }


    fun add(card: Card): Boolean = this._cards.add(card)

    fun cardArray(): Array<Card> = this._cards.toTypedArray()


    companion object {
        fun empty(): CardSet = CardSet()
    }

}