package com.croffgrin.ygocalc.gui

import javax.swing.UIManager

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

object Gui {
    fun configureGuiSettings() {
        // Enable hardware acceleration
        System.setProperty("sun.java2d.opengl", "true")

        // Set look and feel
        try {
            UIManager.setLookAndFeel(Gui.LAF_NAME)
        } catch (ex: Exception) {

        }
    }

    private val NIMBUS: String? = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.name == "Nimbus" }?.className
    private val METAL: String? = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.name == "Metal" }?.className
    private val MOTIF: String? = UIManager.getInstalledLookAndFeels().firstOrNull { info -> info.name == "Motif" }?.className
    private val NATIVE: String? = UIManager.getSystemLookAndFeelClassName()
    private val PREFERENCE_ORDER = listOf(
            METAL,
            NATIVE,
            NIMBUS,
            MOTIF
    )
    val LAF_NAME: String = PREFERENCE_ORDER.first { it != null } ?: ""
}