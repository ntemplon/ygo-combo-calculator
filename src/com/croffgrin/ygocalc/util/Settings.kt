package com.croffgrin.ygocalc.util

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
class Settings {

    var database: String = "./cards.cdb"


    fun write() {
        val lines = listOf(
            DATABASE_PATH_KEY + this.database
        )

        Files.createDirectories(SETTINGS_FILE.parent)
        SETTINGS_FILE.writeAllLines(lines)
    }

    companion object {
        private val SEPARATOR: String = "="
        private val DATABASE_PATH_KEY: String = "DATABASE_PATH" + SEPARATOR
        private val SETTINGS_FILE: Path = System.getProperty("user.home").toPath().resolve("YgoCalc").resolve("Settings.cfg");

        fun read(): Settings {
            val settings = Settings()

            if (!SETTINGS_FILE.exists()) {
                return Settings()
            }

            for (line in SETTINGS_FILE.lines()) {
                when {
                    line.startsWith(DATABASE_PATH_KEY) -> settings.database = line.substring(0, DATABASE_PATH_KEY.length).trim()
                }
            }

            return settings
        }
    }
}