package com.croffgrin.ygocalc.card

import com.croffgrin.ygocalc.io.exists
import org.sqlite.JDBC
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.DriverManager

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
 * A class representing a DevPro/YGOPro card database (cdb).
 * @property filePath the path to the file where the database is located.
 */
class YgoProDB(val path: Path): CardDB() {

    constructor(filePath: String): this(Paths.get(filePath))

    private var _cards = mapOf<Int, Card>()
    private var _cardsByName = mapOf<String, List<Card>>()

    var state: DBStates = DBStates.UNLOADED
        get
        private set
    var loadingException: Exception? = null
        get
        private set

    override val cards: Iterable<Card>
        get() = this._cards.values


    /**
     * Loads the cards from the database at [filePath].
     */
    fun load() {
        try {
            this.loadInternal()
        } catch (ex: Exception) {
            this.state = DBStates.OTHER_ERROR
            this.loadingException = ex
        }
    }

    private fun loadInternal() {
        this.loadingException = null

        val workDir = Paths.get(".").toAbsolutePath().parent
        val target = this.path.toAbsolutePath()
        val open = workDir.relativize(target)

        if (!open.exists()) {
            this.state = DBStates.FILE_DOES_NOT_EXIST
            return
        }

        Class.forName(JDBC::class.java.name) // Loads the class we need  to use as the db driver
        val connect = DriverManager.getConnection("jdbc:sqlite:${open.toString()}")

        val st = connect.createStatement()
        val textResults = st.executeQuery("SELECT * FROM \"$TEXTS_TABLE_NAME\"")
        val data = mutableMapOf<Int, CardData>()

        while (textResults.next()) {
            val id = textResults.getInt(TEXTS_ID)
            val cardData = CardData(id)

            cardData.name = textResults.getString(TEXTS_NAME)
            cardData.desc = textResults.getString(TEXTS_DESC)

            data[id] = cardData
        }

        val dataResults = st.executeQuery("SELECT * FROM \"$DATA_TABLE_NAME\"")
        while (dataResults.next()) {
            val id = dataResults.getInt(DATA_ID)
            val cardData = data[id]

            if (cardData != null) {
                cardData.ot = dataResults.getInt(DATA_OT)
                cardData.alias = dataResults.getInt(DATA_ALIAS)
                cardData.type = dataResults.getInt(DATA_TYPE)

                cardData.atk = dataResults.getInt(DATA_ATK)
                cardData.def = dataResults.getInt(DATA_DEF)
                cardData.level = dataResults.getInt(DATA_LEVEL)
                cardData.race = dataResults.getInt(DATA_RACE)
                cardData.attribute = dataResults.getInt(DATA_ATTRIBUTE)
            }
        }

        this._cards = data.map { pair ->
            val card = pair.value.buildCard()
            if (card != null) {
                pair.key to card
            } else {
                null
            }
        }.filterNotNull().toMap()

        this._cardsByName = this._cards.values.groupBy {
            it.name.toUpperCase()
        }

        this.state = DBStates.LOADED
    }

    /**
     * Gets a [Card] with a provided id
     * @param id the id of the desired [Card]
     * @return the [Card] with the provided id, or null if there is none
     */
    override operator fun get(id: Int): CardOption = if (this._cards.containsKey(id)) {
        val card = this._cards[id]
        if (card != null) {
            CardOption.CardFoundOption(card)
        } else {
            CardOption.CardNotFoundOption()
        }
    } else {
        CardOption.CardNotFoundOption()
    }

    /**
     * Gets all [Card]s with a provided name
     * @param name the name of the desired [Card]s
     * @return the [Card]s with the provided name, or an empty list if there are none
     */
    override operator fun get(name: String): List<Card> {
        val upper = name.toUpperCase()
        if (this._cardsByName.containsKey(upper)) {
            return this._cardsByName[upper] ?: listOf()
        } else {
            return listOf()
        }
    }


    companion object {
        private val DATA_TABLE_NAME = "datas"
        private val DATA_ID = "id"
        private val DATA_OT = "ot"
        private val DATA_ALIAS = "alias"
        private val DATA_SETCODE = "setcode"
        private val DATA_TYPE = "type"
        private val DATA_ATK = "atk"
        private val DATA_DEF = "def"
        private val DATA_LEVEL = "level"
        private val DATA_RACE = "race"
        private val DATA_ATTRIBUTE = "attribute"
        private val DATA_CATEGORY = "category"

        private val TEXTS_TABLE_NAME = "texts"
        private val TEXTS_ID = "id"
        private val TEXTS_NAME = "name"
        private val TEXTS_DESC = "desc"
        private val TEXTS_STR1 = "str1"
        private val TEXTS_STR2 = "str2"
        private val TEXTS_STR3 = "str3"
        private val TEXTS_STR4 = "str4"
        private val TEXTS_STR5 = "str5"
        private val TEXTS_STR6 = "str6"
        private val TEXTS_STR7 = "str7"
        private val TEXTS_STR8 = "str8"
        private val TEXTS_STR9 = "str9"
        private val TEXTS_STR10 = "str10"
        private val TEXTS_STR11 = "str11"
        private val TEXTS_STR12 = "str12"
        private val TEXTS_STR13 = "str13"
        private val TEXTS_STR14 = "str14"
        private val TEXTS_STR15 = "str15"
        private val TEXTS_STR16 = "str16"
    }

    enum class DBStates(val hasValidData: Boolean) {
        UNLOADED(false),
        LOADED(true),
        FILE_DOES_NOT_EXIST(false),
        OTHER_ERROR(false)
    }

}