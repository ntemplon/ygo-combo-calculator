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
class CardData(val id: Int) {

    /**
     * The name of the card.
     */
    var name: String = ""
    /**
     * The description of the card.
     */
    var desc: String = ""

    /**
     * The ATK stat of the card (0 if not a monster).
     */
    var atk: Int = 0

    /**
     * The DEF stat of the card (0 if not a monster).
     */
    var def: Int = 0

    /**
     * The level of the card (0 if not a monster).
     *
     * For Pendulum Monsters, is 8 bits of scale (twice), then 16 bits of monster level
     */
    var level: Int = 0

    /**
     * The monster type of the card (0 if not a monster).
     *
     * It is bit encoded in the following way:
     *  Bit 0:  Warrior
     *  Bit 1:  Spellcaster
     *  Bit 2:  Fairy
     *  Bit 3:  Fiend
     *  Bit 4:  Zombie
     *  Bit 5:  Machine
     *  Bit 6:  Aqua
     *  Bit 7:  Pyro
     *  Bit 8:  Rock
     *  Bit 9:  Winged-Beast
     *  Bit 10: Plant
     *  Bit 11: Insect
     *  Bit 12: Thunder
     *  Bit 13: Dragon
     *  Bit 14: Beast
     *  Bit 15: Beast-Warrior
     *  Bit 16: Dinosaur
     *  Bit 17: Fish
     *  Bit 18: Sea Serpent
     *  Bit 19: Reptile
     *  Bit 20: Psychic
     *  Bit 21: Divine-Beast
     *  Bit 22: Creator God
     */
    var race: Int = 0

    /**
     * The attribute of the card (0 if not a monster).
     *
     * It is bit encoded in the following way:
     *  Bit 0:  EARTH
     *  Bit 1:  WATER
     *  Bit 2:  FIRE
     *  Bit 3:  WIND
     *  Bit 4:  LIGHT
     *  Bit 5:  DARK
     *  Bit 6:  DIVINE
     */
    var attribute: Int = 0

    /**
     * The territory in which the card is legal.
     *
     * It is bit encoded in the following way (All applicable bits set to 1):
     *  Bit 0:  OCG
     *  Bit 1:  TCG
     *  Bit 2:  Anime
     */
    var ot: Int = 0

    /**
     * The ID of the card that this card is treated the same as, 0 otherwise.  Used for alternate artworks and cards
     * like "A Legendary Ocean" that are treated as other cards.
     */
    var alias: Int = 0

    /**
     * The type of the card.
     *
     * It is bit encoded in the following way (All applicable bits set to 1):
     *  Bit 0:  Monster
     *  Bit 1:  Spell
     *  Bit 2:  Trap
     *  Bit 3:
     *  Bit 4:  Normal Monster
     *  Bit 5:  Effect (Monster)
     *  Bit 6:  Fusion (Monster)
     *  Bit 7:  Ritual (Monster / Spell)
     *  Bit 8:
     *  Bit 9:  Spirit (Monster)
     *  Bit 10: Union (Monster)
     *  Bit 11: Gemini (Monster)
     *  Bit 12: Tuner (Monster)
     *  Bit 13: Synchro (Monster)
     *  Bit 14: Token (Monster)
     *  Bit 15:
     *  Bit 16: Quick-Play (Spell)
     *  Bit 17: Continuous (Spell / Trap)
     *  Bit 18: Equip (Spell)
     *  Bit 19: Field (Spell)
     *  Bit 20: Counter (Trap)
     *  Bit 21: Flip Effect (Monster)
     *  Bit 22: Toon (Monster)
     *  Bit 23: Xyz (Monster)
     *  Bit 24: Pendulum (Monster)
     */
    var type: Int = 0

    /**
     * Converts the data stored in this object into a new Card
     */
    fun buildCard(): Card = when {
        this.type.isMonster() -> buildMonster()
        this.type.isSpell() -> buildSpell()
        this.type.isTrap() -> buildTrap()
        else -> throw IllegalStateException("Type is not Monster, Spell, or Trap.")
    }

    private fun buildMonster(): Card = Card.MonsterCard(this.id, this.name, this.desc, this.atk, this.def)

    private fun buildSpell(): Card = Card.SpellCard(this.id, this.name, this.desc, spellType(this.type))

    private fun buildTrap(): Card = Card.TrapCard(this.id, this.name, this.desc, trapType(this.type))

    private fun spellType(typeBits: Int): Card.SpellTypes = when {
        typeBits.isContinuous() -> Card.SpellTypes.CONTINUOUS
        typeBits.isQuickPlay() -> Card.SpellTypes.QUICK_PLAY
        typeBits.isEquip() -> Card.SpellTypes.EQUIP
        typeBits.isField() -> Card.SpellTypes.FIELD
        typeBits.isRitual() -> Card.SpellTypes.RITUAL
        else -> Card.SpellTypes.NORMAL
    }

    private fun trapType(typeBits: Int): Card.TrapTypes = when {
        typeBits.isContinuous() -> Card.TrapTypes.CONTINUOUS
        typeBits.isCounter() -> Card.TrapTypes.COUNTER
        else -> Card.TrapTypes.NORMAL
    }

    override fun toString(): String = this.name + " [Data]"

    companion object {
        private val MONSTER_BIT_MASK: Int = 1 shl 0
        private val SPELL_BIT_MASK: Int = 1 shl 1
        private val TRAP_BIT_MASK: Int = 1 shl 2
        // No meaning for bit 3
        private val NORMAL_MONSTER_BIT_MASK: Int = 1 shl 4
        private val EFFECT_MONSTER_BIT_MASK: Int = 1 shl 5
        private val FUSION_BIT_MASK: Int = 1 shl 6
        private val RITUAL_BIT_MASK: Int = 1 shl 7
        // No meaning for bit 8
        private val SPIRIT_BIT_MASK: Int = 1 shl 9
        private val UNION_BIT_MASK: Int = 1 shl 10
        private val GEMINI_BIT_MASK: Int = 1 shl 11
        private val TUNER_BIT_MASK: Int = 1 shl 12
        private val SYNCHRO_BIT_MASK: Int = 1 shl 13
        private val TOKEN_BIT_MASK: Int = 1 shl 14
        // No meaning for bit 15
        private val QUICK_PLAY_BIT_MASK: Int = 1 shl 16
        private val CONTINUOUS_BIT_MASK: Int = 1 shl 17
        private val EQUIP_BIT_MASK: Int = 1 shl 18
        private val FIELD_BIT_MASK: Int = 1 shl 19
        private val COUNTER_BIT_MASK: Int = 1 shl 20
        private val FLIP_BIT_MASK: Int = 1 shl 21
        private val TOON_BIT_MASK: Int = 1 shl 22
        private val XYZ_BIT_MASK: Int = 1 shl 23
        private val PENDULUM_BIT_MASK: Int = 1 shl 24

        private infix fun Int.isBitMask(mask: Int) = (this and mask) == mask
        private fun Int.isMonster(): Boolean = this isBitMask MONSTER_BIT_MASK
        private fun Int.isSpell(): Boolean = this isBitMask SPELL_BIT_MASK
        private fun Int.isTrap(): Boolean = this isBitMask TRAP_BIT_MASK
        private fun Int.isNormalMonster(): Boolean = this isBitMask NORMAL_MONSTER_BIT_MASK
        private fun Int.isEffectMonster(): Boolean = this isBitMask EFFECT_MONSTER_BIT_MASK
        private fun Int.isFusion(): Boolean = this isBitMask FUSION_BIT_MASK
        private fun Int.isRitual(): Boolean = this isBitMask RITUAL_BIT_MASK
        private fun Int.isSpirit(): Boolean = this isBitMask SPIRIT_BIT_MASK
        private fun Int.isUnion(): Boolean = this isBitMask UNION_BIT_MASK
        private fun Int.isGemini(): Boolean = this isBitMask GEMINI_BIT_MASK
        private fun Int.isTuner(): Boolean = this isBitMask TUNER_BIT_MASK
        private fun Int.isSynchro(): Boolean = this isBitMask SYNCHRO_BIT_MASK
        private fun Int.isToken(): Boolean = this isBitMask TOKEN_BIT_MASK
        private fun Int.isQuickPlay(): Boolean = this isBitMask QUICK_PLAY_BIT_MASK
        private fun Int.isContinuous(): Boolean = this isBitMask CONTINUOUS_BIT_MASK
        private fun Int.isEquip(): Boolean = this isBitMask EQUIP_BIT_MASK
        private fun Int.isField(): Boolean = this isBitMask FIELD_BIT_MASK
        private fun Int.isCounter(): Boolean = this isBitMask COUNTER_BIT_MASK
        private fun Int.isFlip(): Boolean = this isBitMask FLIP_BIT_MASK
        private fun Int.isToon(): Boolean = this isBitMask TOON_BIT_MASK
        private fun Int.isXyz(): Boolean = this isBitMask XYZ_BIT_MASK
        private fun Int.isPendulum(): Boolean = this isBitMask PENDULUM_BIT_MASK
    }

}