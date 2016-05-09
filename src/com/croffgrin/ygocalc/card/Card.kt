package com.croffgrin.ygocalc.card

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

/**
 * @property name The name of the card.
 * @property description The description of the card.
 */
sealed class Card private constructor(val id: Int, val name: String, val description: String) {

    class MonsterCard(id: Int, name: String, description: String, val attack: Int, val defense: Int): Card(id, name, description) {
        override fun toString(): String = this.name + " [Monster]"
    }

    class SpellCard(id: Int, name: String, val text: String, val type: SpellTypes): Card(id, name, text) {
        private val _toStringVal: String by lazy {
            val typeDesc = this.type.description
            val append = if (typeDesc.isBlank()) {
                ""
            } else {
                "/$typeDesc"
            }
            "${this.name} [Spell$append]"
        }

        override fun toString(): String = this._toStringVal
    }

    class TrapCard(id: Int, name: String, val text: String, val type: TrapTypes): Card(id, name, text) {
        private val _toStringVal: String by lazy {
            val typeDesc = this.type.description
            val append = if (typeDesc.isBlank()) {
                ""
            } else {
                "/$typeDesc"
            }
            "${this.name} [Trap$append]"
        }

        override fun toString(): String = this._toStringVal
    }

    enum class SpellTypes(val description: String) {
        NORMAL(""),
        QUICK_PLAY("Quick-Play"),
        CONTINUOUS("Continuous"),
        EQUIP("Equip"),
        FIELD("Field"),
        RITUAL("Ritual")
    }

    enum class TrapTypes(val description: String) {
        NORMAL(""),
        CONTINUOUS("Continuous"),
        COUNTER("Counter")
    }


    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Card) {
            return false
        }

        return this.id == other.id
    }

    override fun hashCode(): Int = this.id

}