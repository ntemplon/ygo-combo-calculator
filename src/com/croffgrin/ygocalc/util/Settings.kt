package com.croffgrin.ygocalc.util

import com.croffgrin.ygocalc.gui.component.ExceptionInfoDialog
import com.croffgrin.ygocalc.io.IoUtil
import com.croffgrin.ygocalc.io.readAllText
import java.awt.Dimension
import java.awt.Point
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.JFrame
import javax.swing.JOptionPane

/*
 * Copyright (c) 2016 Nathan S. Templon
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
class Settings private constructor() {

    var database: String = DefaultDatabase
    var lastDeck: String = DefaultLastDeck
    var mainWindowSettings: WindowSettings = WindowSettings()
    var chooserSize: Dimension = DefaultChooserSize


    fun write() {
        val text = IoUtil.gson.toJson(this)
        Files.createDirectories(SETTINGS_FILE.parent)
        SETTINGS_FILE.writeAllLines(listOf(text))
    }

    companion object {
        val SETTINGS_FILE: Path = System.getProperty("user.home").toPath().resolve("YgoCalc").resolve("Settings.cfg")

        val DefaultDatabase: String = "./cards.cdb"
        val DefaultLastDeck: String = ""
        val DefaultRememberWindow: Boolean = true
        val DefaultWindowSize: Dimension = Dimension(1000, 700)
        val DefaultWindowX: Int = 0
        val DefaultWindowY: Int = 0
        val DefaultChooserSize: Dimension = Dimension(1000, 700)

        fun default(): Settings = Settings()

        fun read(): Settings {
            if (SETTINGS_FILE.exists()) {
                try {
                    return IoUtil.gson.fromJson(SETTINGS_FILE.readAllText(), Settings::class.java)
                } catch (ex: Exception) {
                    ExceptionInfoDialog(ex).isVisible = true
                    return Settings.default()
                }
            } else {
                return Settings.default()
            }
        }
    }
}

data class WindowSettings(
        val rememberSettings: Boolean = DefaultRememberSettings,
        val windowPosition: Point = DefaultWindowPosition,
        val windowSize: Dimension = DefaultWindowSize) {

    companion object {
        val DefaultRememberSettings: Boolean = true
        val DefaultWindowPosition: Point = Point(0, 0)
        val DefaultWindowSize: Dimension = Dimension(500, 500)

        fun fromWindow(window: JFrame, rememberSettings: Boolean) = WindowSettings(
                rememberSettings = rememberSettings,
                windowPosition = Point(window.x, window.y),
                windowSize = window.size
        )
    }

}