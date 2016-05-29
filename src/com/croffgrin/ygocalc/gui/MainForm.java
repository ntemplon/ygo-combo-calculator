package com.croffgrin.ygocalc.gui;

import com.croffgrin.ygocalc.YgoCalc;
import com.croffgrin.ygocalc.card.Deck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

/**
 * Copyright (c) 2016 Nathan S. Templon
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
public class MainForm extends JFrame {

    private JPanel mainPanel;
    private JPanel dbBar;
    private JLabel dbLabel;
    private JTextField dbField;
    private JButton dbButton;

    // Properties
    public String getDatabase() {
        return this.dbField.getText();
    }

    public void setDatabase(String database) {
        this.dbField.setText(database);
    }

    public void setDatabaseValid(Boolean valid) {
        if (valid) {
            this.dbField.setForeground(Color.BLACK);
        } else {
            this.dbField.setForeground(Color.RED);
        }
    }


    public MainForm() {
        super("Ygo Combo Calculator");

        JMenuBar menuBar = new JMenuBar();

        // Create "File" menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFileItem = new JMenuItem("Open");
        openFileItem.addActionListener(this::openMenuItemClicked);
        fileMenu.add(openFileItem);

        fileMenu.add(new JPopupMenu.Separator());
        JMenuItem exitFileItem = new JMenuItem("Exit");
        exitFileItem.addActionListener(this::exitMenuItemClicked);
        fileMenu.add(exitFileItem);

        // Create "Edit" menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem editSettingsItem = new JMenuItem("Settings");
        editSettingsItem.addActionListener(this::settingsMenuItemClicked);
        editMenu.add(editSettingsItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        this.setJMenuBar(menuBar);
        this.setContentPane(this.mainPanel);

        this.setSize(new Dimension(1000, 700));

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void openMenuItemClicked(ActionEvent e) {
        Deck.DeckOption option = YgoCalc.INSTANCE.openDeck();
    }

    private void exitMenuItemClicked(ActionEvent e) {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void settingsMenuItemClicked(ActionEvent e) {

    }
}
