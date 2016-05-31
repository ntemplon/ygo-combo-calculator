package com.croffgrin.ygocalc.card

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
class CardFilter(
        val name: String,
        val archtype: String? = null,
        val cardName: String? = null,
        val minLevel: Int? = null,
        val maxLevel: Int? = null
) {

    // Properties
    private val upperArchtype: String? = this.archtype?.toUpperCase(Locale.US)
    private val upperCardName: String? = this.cardName?.toUpperCase(Locale.US)


    // Public Methods
    fun accept(card: Card): Boolean {
        return this.matchesCardName(card) &&
                this.matchesArchtype(card) &&
                this.matchesMinLevel(card) &&
                this.matchesMaxLevel(card)
    }


    // Private Methods
    private fun matchesCardName(card: Card): Boolean {
        if (this.upperCardName == null) {
            return true
        } else {
            return card.upperName == this.upperCardName
        }
    }

    private fun matchesArchtype(card: Card): Boolean {
        if (upperArchtype == null) {
            return true
        } else {
            return card.upperName.contains(this.upperArchtype)
        }
    }

    private fun matchesMinLevel(card: Card): Boolean {
        if (this.minLevel == null) {
            return true
        } else {
            if (card is Card.MonsterCard) {
                return card.level >= this.minLevel
            } else {
                return false
            }
        }
    }

    private fun matchesMaxLevel(card: Card): Boolean {
        if (this.maxLevel == null) {
            return true
        } else {
            if (card is Card.MonsterCard) {
                return card.level <= this.maxLevel
            } else {
                return false
            }
        }
    }

}