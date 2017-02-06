package com.croffgrin.ygocalc.gui.component

import java.awt.GridBagLayout
import javax.swing.*

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
class ExceptionInfoControl @JvmOverloads constructor(exception: Exception = Exception()) : JPanel() {

    private val typeTextField: JTextField = JTextField().apply { isEditable = false }
    private val messageArea: JTextArea = JTextArea().apply { isEditable = false }
    private val stackTraceArea: JTextArea = JTextArea().apply { isEditable = false }

    var exception = exception
        get() = field
        set(value) {
            field = value
            this.typeTextField.text = value.javaClass.canonicalName
            this.messageArea.text = value.message
            this.stackTraceArea.text = value.stackTrace
                    .map { it -> it.toString() }
                    .joinToString(separator = System.lineSeparator())
        }

    init {
        this.layout = GridBagLayout()

        this.add(JLabel("Exception Type:"), java.awt.GridBagConstraints().apply {
            gridx = 1
            gridy = 1
            anchor = java.awt.GridBagConstraints.LINE_START
        })

        this.add(this.typeTextField, java.awt.GridBagConstraints().apply {
            gridx = 2
            gridy = 1
            weightx = 1.0
            fill = java.awt.GridBagConstraints.BOTH
        })

        this.add(JLabel("Message:"), java.awt.GridBagConstraints().apply {
            gridx = 1
            gridy = 2
            gridwidth = 2
            anchor = java.awt.GridBagConstraints.LINE_START
        })

        this.add(this.messageArea, java.awt.GridBagConstraints().apply {
            gridx = 1
            gridy = 3
            gridwidth = 2
            weightx = 1.0
            fill = java.awt.GridBagConstraints.BOTH
        })

        this.add(JLabel("Stack Trace:"), java.awt.GridBagConstraints().apply {
            gridx = 1
            gridy = 4
            anchor = java.awt.GridBagConstraints.LINE_START
        })

        this.add(JScrollPane(this.stackTraceArea), java.awt.GridBagConstraints().apply {
            gridx = 1
            gridy = 5
            gridwidth = 2
            weightx = 1.0
            weighty = 1.0
            fill = java.awt.GridBagConstraints.BOTH
        })
    }

}

class ExceptionInfoDialog @JvmOverloads constructor(exception: Exception = Exception()) : JDialog() {

    private var exceptionInfoControl: ExceptionInfoControl = ExceptionInfoControl(exception)

    var exception: Exception
        get() = this.exceptionInfoControl.exception
        set(value) {
            this.exceptionInfoControl.exception = value
        }
}