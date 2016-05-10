package com.croffgrin.ygocalc

import com.croffgrin.ygocalc.card.CardDB
import com.croffgrin.ygocalc.card.Deck
import com.croffgrin.ygocalc.gui.DeckViewer
import java.awt.Dimension
import java.io.File
import java.nio.file.Paths
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.UIManager
import javax.swing.filechooser.FileFilter

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
    val popup: (String) -> Unit = { msg -> JOptionPane.showMessageDialog(null, msg) }

    configureGui()

    val testPath = "..\\..\\HDD Programs\\YGOPro DevPro\\cards.cdb"
    val db = CardDB(testPath)
    db.load()

    // Open deck
    val chooser = JFileChooser()
    chooser.apply {
        fileFilter = object : FileFilter() {
            override fun accept(f: File): Boolean {
                if (f.isDirectory) {
                    return true
                } else {
                    return f.extension.toLowerCase() == "ydk"
                }
            }

            override fun getDescription(): String = "YDK Files (*.ydk)"
        }

        currentDirectory = Paths.get("D:\\HDD Programs\\YGOPro DevPro\\deck").toFile()
        preferredSize = Dimension(1000, 700)
    }

    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        val file = chooser.selectedFile
        val deck = Deck.fromYdk(file.toPath(), db)

        val viewer = DeckViewer(deck)
        viewer.isVisible = true
    }
}


private fun configureGui() {
    // Enable hardware acceleration
    System.setProperty("sun.java2d.opengl", "true")

    // Set look and feel
    try {
        UIManager.setLookAndFeel(GuiConstants.LAF_NAME)
    } catch (ex: Exception) {

    }
}


object GuiConstants {
    private val NIMBUS: String? = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.getName().equals("Nimbus") }?.className
    private val METAL: String? = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.getName().equals("Metal") }?.className
    private val MOTIF: String? = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.getName().equals("Motif") }?.className
    private val NATIVE: String? = UIManager.getSystemLookAndFeelClassName()
    private val PREFERENCE_ORDER = listOf(
            NATIVE,
            NIMBUS,
            METAL,
            MOTIF
    )
    val LAF_NAME: String = PREFERENCE_ORDER.first { it != null } ?: ""
}