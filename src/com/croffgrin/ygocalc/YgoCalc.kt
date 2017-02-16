package com.croffgrin.ygocalc

import com.croffgrin.ygocalc.card.YgoProDB
import com.croffgrin.ygocalc.card.Deck
import com.croffgrin.ygocalc.ui.Gui
import com.croffgrin.ygocalc.ui.MainForm
import com.croffgrin.ygocalc.ui.component.ExceptionInfoDialog
import com.croffgrin.ygocalc.io.Filters
import com.croffgrin.ygocalc.ui.builder.*
import com.croffgrin.ygocalc.util.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.nio.file.Paths
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JMenu

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

    // Fields
    private val form: MainForm by lazy { MainForm().apply {
        addWindowListener(YgoFormListener)
    }}
    private var db: YgoProDB? = null
        get() = field
        set(value) {
            val oldValue = field
            field = value

            // Update Settings
            if (value != null) {
                settings.database = value.path.toAbsolutePath().toString()
            } else {
                settings.database = ""
            }

            // Inform Listeners
            this.dbChangedEvent.dispatch(ChangedArgs(oldValue, value))
        }
    private var deck: Deck = Deck.empty()
        get() = field
        set(value) {
            val oldValue = field
            field = value

            this.deckLoadedEvent.dispatch(ChangedArgs(oldValue, value))
        }
    private var settings: Settings = Settings.default()

    val debugMenu: JMenu = menu {
        item("View Settings File") {  }
    }


    // Events
    private val dbChangedEvent = Event<ChangedArgs<YgoProDB?>>()
    val dbChanged = this.dbChangedEvent.handle
    private val deckLoadedEvent = Event<ChangedArgs<Deck>>()
    val deckLoaded = this.deckLoadedEvent.handle


    // Initialization
    init {
        this.dbChanged.addListener { this.deck = Deck.empty() }
    }


    // Public Methods
    fun start() {
        Gui.configureGuiSettings()
        settings = Settings.read()

        form.pack()
        if (settings.mainWindowSettings.rememberSettings) {
            form.size = settings.mainWindowSettings.windowSize
            form.setLocation(settings.mainWindowSettings.windowPosition.x, settings.mainWindowSettings.windowPosition.y)
        }
        this.loadDB()
        this.loadDeck()
        form.isVisible = true
    }

    fun close() {
        settings.mainWindowSettings = WindowSettings.fromWindow(form, settings.mainWindowSettings.rememberSettings)
        settings.write()
    }

    fun openDeck() {
        val db = this.db ?: return

        val chooser = JFileChooser().apply {
            fileFilter = Filters.YdkFilter
            val lastDeck = Paths.get(settings.lastDeck).toAbsolutePath()
            if (lastDeck.exists()) {
                currentDirectory = if (lastDeck.isDirectory()) {
                    lastDeck.toFile()
                } else {
                    lastDeck.parent.toFile()
                }
            }
            preferredSize = settings.chooserSize
        }

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            val file = chooser.selectedFile
            val path = file.toPath()

            val option = Deck.fromYdk(path, db)
            if (option is Deck.DeckOption.DeckRead) {
                this.deck = option.deck
            } else {
                this.deck = Deck.empty()
            }

            settings.lastDeck = path.toString()
        }
    }

    fun selectDB() {
        val chooser = JFileChooser().apply {
            fileFilter = Filters.CdbFilter
            preferredSize = settings.chooserSize
        }

        val chosenDB: String = if (chooser.showOpenDialog(this.form) == JFileChooser.APPROVE_OPTION) {
            chooser.selectedFile.toString()
        } else {
            return
        }

        this.settings.database = chosenDB

        val db = YgoProDB(chosenDB)
        db.load()
        this.db = db
    }

    @JvmOverloads
    fun editSettings(parent: JFrame? = null) {

    }

    fun tester() {
        val ex = IllegalArgumentException("No arguments!")
        val dialog = ExceptionInfoDialog(ex, form, true)
        dialog.isVisible = true
    }


    // Private Methods
    private fun loadDB() {
        val settingsPath = Paths.get(settings.database)
        val defaultPath = Paths.get(Settings.DefaultDatabase)

        val db = if (settingsPath.exists() || !defaultPath.exists()) {
            YgoProDB(settingsPath)
        } else {
            YgoProDB(defaultPath)
        }

        db.load()
        this.db = db
    }

    private fun loadDeck() {
        val settingsPath = Paths.get(settings.lastDeck)
        val db = this.db
        if (settingsPath.exists() && db != null) {
            try {
                val option = Deck.fromYdk(settingsPath, db)
                if (option is Deck.DeckOption.DeckRead) {
                    this.deck = option.deck
                } else {
                    this.deck = Deck.empty()
                }
            } catch (ex: Exception) {
                this.deck = Deck.empty()
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