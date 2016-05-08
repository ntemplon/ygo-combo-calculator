package com.croffgrin.ygocalc

import com.croffgrin.ygocalc.card.CardDB
import javax.swing.JOptionPane
import javax.swing.UIManager

/**
 * Created by Hortator on 4/29/2016.
 */
fun main(args: Array<String>) {
    val popup: (String) -> Unit = { msg -> JOptionPane.showMessageDialog(null, msg) }

    configureGui()

    val testPath = "..\\..\\HDD Programs\\YGOPro DevPro\\cards.cdb"
    //val testPath = "data\\cards.cdb"
    val db = CardDB(testPath)
    db.load()
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
            NIMBUS,
            METAL,
            MOTIF,
            NATIVE
    )
    val LAF_NAME: String = PREFERENCE_ORDER.first { it != null } ?: ""
}