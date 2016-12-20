package com.croffgrin.ygocalc.util

import java.awt.Dimension
import java.awt.Point
import java.nio.file.Files
import java.nio.file.Path

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

    // Events
    var database: String = DefaultDatabase
    var lastDeck: String = DefaultLastDeck
    var rememberWindow: Boolean = DefaultRememberWindow
    var windowSize: Dimension = DefaultWindowSize
    var windowX: Int = DefaultWindowX
    var windowY: Int = DefaultWindowY
    var chooserSize: Dimension = DefaultChooserSize


    fun write() {
        val lines = listOf(
                DATABASE_PATH_KEY + this.database,
                LAST_DECK_KEY + this.lastDeck,
                REMEMBER_WINDOW_KEY + this.rememberWindow,
                WINDOW_WIDTH_KEY + this.windowSize.width,
                WINDOW_HEIGHT_KEY + this.windowSize.height,
                WINDOW_X_KEY + this.windowX,
                WINDOW_Y_KEY + this.windowY,
                CHOOSER_WIDTH_KEY + this.chooserSize.width,
                CHOOSER_HEIGHT_KEY + this.chooserSize.height
        )

        Files.createDirectories(SETTINGS_FILE.parent)
        SETTINGS_FILE.writeAllLines(lines)
    }

    companion object {
        val SETTINGS_FILE: Path = System.getProperty("user.home").toPath().resolve("YgoCalc").resolve("Settings.cfg");

        private val SEPARATOR: String = "="
        private val DATABASE_PATH_KEY: String = "DATABASE_PATH" + SEPARATOR
        private val LAST_DECK_KEY: String = "LAST_DECK" + SEPARATOR
        private val REMEMBER_WINDOW_KEY: String = "REMEMBER_WINDOW" + SEPARATOR
        private val WINDOW_WIDTH_KEY: String = "WINDOW_WIDTH" + SEPARATOR
        private val WINDOW_HEIGHT_KEY: String = "WINDOW_HEIGHT" + SEPARATOR
        private val WINDOW_X_KEY: String = "WINDOW_X" + SEPARATOR
        private val WINDOW_Y_KEY: String = "WINDOW_Y" + SEPARATOR
        private val CHOOSER_WIDTH_KEY: String = "CHOOSER_WIDTH" + SEPARATOR
        private val CHOOSER_HEIGHT_KEY: String = "CHOOSER_HEIGHT" + SEPARATOR

        val DefaultDatabase: String = "./cards.cdb"
        val DefaultLastDeck: String = ""
        val DefaultRememberWindow: Boolean = true
        val DefaultWindowSize: Dimension = Dimension(1000, 700)
        val DefaultWindowX: Int = 0
        val DefaultWindowY: Int = 0
        val DefaultChooserSize: Dimension = Dimension(1000, 700)

        fun default(): Settings = Settings()

        fun read(): Settings {
            val settings = Settings()

            if (!SETTINGS_FILE.exists()) {
                return settings
            }

            var windowWidth: Int = DefaultWindowSize.width
            var windowHeight: Int = DefaultWindowSize.height
            var chooserWidth: Int = DefaultChooserSize.width
            var chooserHeight: Int = DefaultChooserSize.height
            for (line in SETTINGS_FILE.lines()) {
                when {
                    line.startsWith(DATABASE_PATH_KEY) -> settings.database = line.substring(DATABASE_PATH_KEY.length).trim()
                    line.startsWith(LAST_DECK_KEY) -> settings.lastDeck = line.substring(LAST_DECK_KEY.length).trim()
                    line.startsWith(REMEMBER_WINDOW_KEY) -> settings.rememberWindow = line.substring(REMEMBER_WINDOW_KEY.length).trim().toBoolean()
                    line.startsWith(WINDOW_WIDTH_KEY) -> windowWidth = line.substring(WINDOW_WIDTH_KEY.length).trim().toInt()
                    line.startsWith(WINDOW_HEIGHT_KEY) -> windowHeight = line.substring(WINDOW_HEIGHT_KEY.length).trim().toInt()
                    line.startsWith(WINDOW_X_KEY) -> settings.windowX = line.substring(WINDOW_X_KEY.length).trim().toInt()
                    line.startsWith(WINDOW_Y_KEY) -> settings.windowY = line.substring(WINDOW_Y_KEY.length).trim().toInt()
                    line.startsWith(CHOOSER_WIDTH_KEY) -> chooserWidth = line.substring(CHOOSER_WIDTH_KEY.length).trim().toInt()
                    line.startsWith(CHOOSER_HEIGHT_KEY) -> chooserHeight = line.substring(CHOOSER_HEIGHT_KEY.length).trim().toInt()
                }
            }
            settings.windowSize = Dimension(windowWidth, windowHeight)
            settings.chooserSize = Dimension(chooserWidth, chooserHeight)

            return settings
        }
    }
}

class WindowSettings() {

    var rememberSettings: Boolean = DefaultRememberSettings
    var windowPosition: Point = DefaultWindowPosition

    companion object {
        val DefaultRememberSettings: Boolean = true
        val DefaultWindowPosition: Point = Point(0, 0)
        val DefaultWindowSize: Dimension = Dimension(500, 500)
    }

}