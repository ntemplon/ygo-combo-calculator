package com.croffgrin.ygocalc.card

import com.croffgrin.ygocalc.io.exists
import com.croffgrin.ygocalc.io.lines
import com.croffgrin.ygocalc.util.shuffle
import com.croffgrin.ygocalc.util.toIntOrNull
import java.nio.file.Path

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
class Deck private constructor(val name: String, val main: CardSet, val side: CardSet, val extra: CardSet) {

    val isEmpty: Boolean
        get() = this.main.count == 0 && this.side.count == 0 && this.extra.count == 0

    fun drawHand(handSize: Int = 5): GameState {
        val orderedDeck = this.main.cards.toMutableList()
        orderedDeck.shuffle()

        val hand = orderedDeck.take(handSize)
        val restOfDeck = orderedDeck.takeLast(orderedDeck.size - handSize)

        return GameState(deck = restOfDeck, hand = hand, graveyard = listOf(), banished = listOf(), field = GameState.FieldState())
    }

    companion object {
        private enum class ParseStates {
            MainDeck,
            ExtraDeck,
            SideDeck,
            None
        }

        private val MAIN_LINE: String = "#main"
        private val EXTRA_LINE: String = "#extra"
        private val SIDE_LINE: String = "!side"

        fun empty(name: String = ""): Deck = Deck(name, CardSet(), CardSet(), CardSet())

        fun fromYdk(file: Path, db: YgoProDB): DeckOption {
            if (!file.exists()) {
                return DeckOption.DeckNotRead("The provided file does not exist!")
            }

            val fileStr = file.toString()
            if (!fileStr.toUpperCase().endsWith(".YDK")) {
                return DeckOption.DeckNotRead("The provided file is not a ydk file.")
            }

            val deck = Deck.empty(file.fileName.toString())
            var parseState: ParseStates = ParseStates.None
            for (line in file.lines()) {
                val trimmed = line.trim()
                if (trimmed.isEmpty()) {
                    continue
                }

                when (trimmed) {
                    MAIN_LINE -> {
                        parseState = ParseStates.MainDeck
                    }
                    EXTRA_LINE -> {
                        parseState = ParseStates.ExtraDeck
                    }
                    SIDE_LINE -> {
                        parseState = ParseStates.SideDeck
                    }
                    else -> {
                        val index = line.toIntOrNull()
                        if (index != null) {
                            val option: CardOption = db[index]

                            when (option) {
                                is CardOption.CardFoundOption -> {
                                    val card = option.card
                                    when(parseState) {
                                        ParseStates.MainDeck -> deck.main.add(card)
                                        ParseStates.ExtraDeck -> deck.extra.add(card)
                                        ParseStates.SideDeck -> deck.side.add(card)
                                        ParseStates.None -> { }
                                    }
                                }
                                is CardOption.CardNotFoundOption -> {

                                }
                            }
                        }
                    }
                }
            }

            return DeckOption.DeckRead(deck)
        }
    }


    sealed class DeckOption(val exists: Boolean) {
        class DeckRead(val deck: Deck): DeckOption(true)
        class DeckNotRead(val message: String = ""): DeckOption(false)
    }

}