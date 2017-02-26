package com.croffgrin.ygocalc.card

import java.lang.reflect.Field

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
class GameState(deck: List<Card> = listOf(), hand: List<Card> = listOf(), graveyard: List<Card> = listOf(), banished: List<Card> = listOf(), field: FieldState = FieldState()) {

    val _deck: MutableList<Card> = deck.toMutableList()
    val _hand: MutableList<Card> = hand.toMutableList()
    val _grave: MutableList<Card> = graveyard.toMutableList()
    val _banished: MutableList<Card> = graveyard.toMutableList()

    val deck: List<Card> = this._deck
    val hand: List<Card> = this._hand
    val graveyard: List<Card> = this._grave
    val banished: List<Card> = this._banished

    class FieldState {

    }

}