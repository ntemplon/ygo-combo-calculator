package com.croffgrin.ygocalc.io

import java.io.File
import javax.swing.filechooser.FileFilter

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
object Filters {

    /**
     * Creates a filter for a file extension
     * @param ext the extension of files that should be shown, not including the period
     * @return a [FileFilter] that shows only directories and files with the provided extension
     */
    fun extensionFilter(ext: String): FileFilter = object: FileFilter() {
        private val extension: String = ext.toUpperCase()
        private val description: String = "${ext.toUpperCase()} Files (*.$ext)"

        override fun accept(file: File): Boolean {
            if (file.isDirectory) {
                return true
            }

            return file.extension.toUpperCase() == extension
        }

        override fun getDescription(): String = description
    }


    val YdkFilter: FileFilter = extensionFilter(IoConstants.YDK_EXTENSION)
    val CdbFilter: FileFilter = extensionFilter(IoConstants.CDB_EXTENSION)

}