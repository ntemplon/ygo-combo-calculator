package com.croffgrin.ygocalc

import com.croffgrin.ygocalc.card.CardDB
import com.croffgrin.ygocalc.card.Deck
import com.croffgrin.ygocalc.gui.Gui
import com.croffgrin.ygocalc.gui.MainForm
import com.croffgrin.ygocalc.io.Filters
import com.croffgrin.ygocalc.util.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.nio.file.Paths
import javax.swing.JFileChooser
import javax.swing.JFrame

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
    private var db: CardDB? = null
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
    private var deck: Deck? = null
        get() = field
        set(value) {
            val oldValue = field
            field = value

            this.deckLoadedEvent.dispatch(ChangedArgs(oldValue, value))
        }
    private var settings: Settings = Settings()


    // Events
    private val dbChangedEvent = Event<ChangedArgs<CardDB?>>()
    val dbChanged = this.dbChangedEvent.handle
    private val deckLoadedEvent = Event<ChangedArgs<Deck?>>()
    val deckLoaded = this.deckLoadedEvent.handle


    // Initialization
    init {
        this.dbChanged.addListener { this.deck = null }
    }


    // Public Methods
    fun start() {
        Gui.configureGuiSettings()
        settings = Settings.read()

        form.pack()
        form.size = settings.windowSize
        form.setLocation(settings.windowX, settings.windowY)

        this.loadDB()
        this.loadDeck()
        form.isVisible = true
    }

    fun close() {
        settings.windowSize = form.size
        settings.windowX = form.x
        settings.windowY = form.y
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
            this.deck = Deck.fromYdk(path, db)

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

        val db = CardDB(chosenDB)
        db.load()
        this.db = db
    }

    @JvmOverloads
    fun editSettings(parent: JFrame? = null) {

    }


    // Private Methods
    private fun loadDB() {
        val settingsPath = Paths.get(settings.database)
        val defaultPath = Paths.get(Settings.DefaultDatabase)

        val db = if (settingsPath.exists() || !defaultPath.exists()) {
            CardDB(settingsPath)
        } else {
            CardDB(defaultPath)
        }

        db.load()
        this.db = db
    }

    private fun loadDeck() {
        val settingsPath = Paths.get(settings.lastDeck)
        val db = this.db
        if (settingsPath.exists() && db != null) {
            try {
                this.deck = Deck.fromYdk(settingsPath, db)
            } catch (ex: Exception) {

                this.deck = null
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