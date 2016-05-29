package com.croffgrin.ygocalc

import com.croffgrin.ygocalc.card.CardDB
import com.croffgrin.ygocalc.card.Deck
import com.croffgrin.ygocalc.gui.Gui
import com.croffgrin.ygocalc.gui.MainForm
import com.croffgrin.ygocalc.io.Filters
import com.croffgrin.ygocalc.util.Settings
import com.croffgrin.ygocalc.util.exists
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.nio.file.Paths
import javax.swing.JFileChooser

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
object YgoCalc {

    private val form: MainForm = MainForm().apply {
        addWindowListener(YgoFormListener)
    }

    private var db: CardDB? = null
    private var settings: Settings = Settings()


    fun start() {
        Gui.configureGuiSettings()
        settings = Settings.read()
        this.loadDB()
        form.isVisible = true
    }

    fun close() {
        settings.write()
    }

    fun openDeck(): Deck.DeckOption {
        val db = this.db ?: return Deck.DeckOption.NoDatabaseLoaded()

        val chooser = JFileChooser().apply {
            fileFilter = Filters.YdkFilter
            currentDirectory = Paths.get("D:\\HDD Programs\\YGOPro DevPro\\deck\\").toFile()
            preferredSize = settings.chooserSize
        }

        return if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            val file = chooser.selectedFile
            settings.chooserSize = chooser.size
            Deck.DeckOption.DeckSelected(Deck.fromYdk(file.toPath(), db))
        } else {
            Deck.DeckOption.NoUserSelection()
        }
    }

    fun selectDB() {
        val chooser = JFileChooser().apply {
            fileFilter = Filters.CdbFilter
            preferredSize = settings.chooserSize
        }
    }


    // Private Methods
    private fun loadDB() {
        if (Paths.get(settings.database).exists()) {
            this.db =  CardDB(settings.database)
            this.form.setDatabaseValid(true)
            this.form.database = settings.database
        } else {
            this.db = null
            this.form.setDatabaseValid(false)
            this.form.database = if (settings.database.trim().length > 0) {
                settings.database
            } else {
                "No Database Loaded"
            }
        }
    }
}

object YgoFormListener: WindowAdapter() {
    override fun windowClosing(e: WindowEvent?) {
        YgoCalc.close()
    }
}

fun main(args: Array<String>) {
    YgoCalc.start()
}