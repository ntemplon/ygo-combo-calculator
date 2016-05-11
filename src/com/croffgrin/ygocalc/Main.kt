package com.croffgrin.ygocalc

import com.croffgrin.ygocalc.card.CardDB
import com.croffgrin.ygocalc.card.Deck
import com.croffgrin.ygocalc.gui.DeckViewer
import com.croffgrin.ygocalc.gui.Gui
import com.croffgrin.ygocalc.io.Filters
import java.awt.Dimension
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
fun main(args: Array<String>) {
    Gui.configureGuiSettings()

    //val testPath = "..\\..\\HDD Programs\\YGOPro DevPro\\cards.cdb"
    val testPath = "/media/nathan/Data/HDD Programs/YGOPro DevPro/cards.cdb"
    val db = CardDB(testPath)
    db.load()

    // Open deck
    val chooser = JFileChooser().apply {
        fileFilter = Filters.YdkFilter
        //currentDirectory = Paths.get("D:\\HDD Programs\\YGOPro DevPro\\deck").toFile()
        currentDirectory = Paths.get("/media/nathan/Data/HDD Programs/YGOPro DevPro/deck").toFile()
        preferredSize = Dimension(1000, 700)
    }

    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        val file = chooser.selectedFile
        val deck = Deck.fromYdk(file.toPath(), db)

        val viewer = DeckViewer(deck)
        viewer.isVisible = true
    }
}