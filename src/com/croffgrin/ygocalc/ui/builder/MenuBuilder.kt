package com.croffgrin.ygocalc.ui.builder

import java.awt.event.ActionEvent
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

/**
 * Copyright (c) 2017 Nathan Templon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
fun menuBar(op: JMenuBar.() -> Unit): JMenuBar {
    val menuBar = JMenuBar()
    menuBar.op()
    return menuBar
}

fun JMenuBar.menu(name: String = "", op: JMenu.() -> Unit) {
    val menu = JMenu(name)
    menu.op()
    this.add(menu)
}

fun menu(name: String = "", op: JMenu.() -> Unit): JMenu {
    val menu = JMenu(name)
    menu.op()
    return menu
}

fun JMenu.item(name: String = "", action: (ActionEvent) -> Unit) {
    val menuItem = JMenuItem(name)
    menuItem.addActionListener(action)
    this.add(menuItem)
}

fun JMenu.subMenu(name: String = "", op: JMenuItem.() -> Unit) {
    val menuItem = JMenuItem(name)
    menuItem.op()
    this.add(menuItem)
}

fun JMenuItem.item(name: String = "", action: (ActionEvent) -> Unit) {
    val menuItem = JMenuItem(name)
    menuItem.addActionListener(action)
    this.add(menuItem)
}

fun JMenu.separator() {
    this.addSeparator()
}