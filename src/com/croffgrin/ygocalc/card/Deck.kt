package com.croffgrin.ygocalc.card

import com.croffgrin.ygocalc.util.exists
import com.croffgrin.ygocalc.util.lines
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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

    companion object {
        private val MAIN_LINE: String = "#main"
        private val EXTRA_LINE: String = "#extra"
        private val SIDE_LINE: String = "!side"

        fun empty(name: String): Deck = Deck(name, CardSet(), CardSet(), CardSet())

        fun fromYdk(file: Path, db: CardDB): Deck {
            if (!file.exists()) {
                throw IllegalArgumentException("The provided file does not exist!")
            }

            val fileStr = file.toString()
            if (!fileStr.toUpperCase().endsWith(".YDK")) {
                throw IllegalArgumentException("The provided file is not a ydk file.")
            }

            val deck = Deck.empty(fileStr.substring(0, fileStr.length - 4)) // 4 is the length of ".ydk"
            var inMain: Boolean = false
            var inExtra: Boolean = false
            var inSide: Boolean = false
            for (line in file.lines()) {
                val trimmed = line.trim()
                if (trimmed.isEmpty()) {
                    continue
                }

                when (trimmed) {
                    MAIN_LINE -> {
                        inMain = true
                        inExtra = false
                        inSide = false
                    }
                    EXTRA_LINE -> {
                        inMain = false
                        inExtra = true
                        inSide = false
                    }
                    SIDE_LINE -> {
                        inMain = false
                        inExtra = false
                        inSide = true
                    }
                    else -> {
                        val card: Card? = try {
                            db[line.toInt()]
                        } catch (ex: Exception) {
                            null
                        }

                        if (card != null) {
                            when {
                                inMain -> deck.main.add(card)
                                inExtra -> deck.extra.add(card)
                                inSide -> deck.side.add(card)
                            }
                        }
                    }
                }
            }

            return deck
        }
    }

}