package com.croffgrin.ygocalc.ui;

import com.croffgrin.ygocalc.YgoCalc;
import com.croffgrin.ygocalc.card.YgoProDB;
import com.croffgrin.ygocalc.card.Deck;
import com.croffgrin.ygocalc.ui.component.VerticalDeckView;
import com.croffgrin.ygocalc.util.*;
import kotlin.Unit;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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
    private VerticalDeckView deckView;
    private JPanel deckPanel;
    private JTabbedPane tabbedPane;
    private JList filterList;
    private JButton createButton;
    private JButton editButton;

    // Properties
    public String getDatabase() {
        return this.dbField.getText();
    }

    public void setDatabase(String database) {
        this.dbField.setText(database);
    }

    public void setDatabaseValid(Boolean valid) {
        final Font font = this.dbField.getFont();
        if (valid) {
            this.dbField.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
            this.dbField.setForeground(Color.BLACK);
        } else {
            this.dbField.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
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
        JMenuItem runTesterItem = new JMenuItem("Run Tester");
        runTesterItem.addActionListener(this::runTesterMenuItemClicked);
        fileMenu.add(runTesterItem);

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

        // Button Listeners
        this.dbButton.addActionListener(this::chooseDBClick);

        // YGOCalc Listeners
        YgoCalc.INSTANCE.getDbChanged().addListener(this::dbUpdated);
        YgoCalc.INSTANCE.getDeckLoaded().addListener(this::deckLoaded);

        this.setSize(new Dimension(1000, 700));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void loadDeck(Deck deck) {
        this.deckView.setDeck(deck);

        final Border border = this.deckPanel.getBorder();
        if (border instanceof TitledBorder) {
            String title;
            if (deck != null) {
                title = "Deck (" + deck.getName() + ")";
            } else {
                title = "Deck";
            }
            ((TitledBorder) border).setTitle(title);
        }

        this.revalidate();
        this.repaint();
    }

    private void openMenuItemClicked(ActionEvent e) {
        YgoCalc.INSTANCE.openDeck();
    }

    private void runTesterMenuItemClicked(ActionEvent e) {
        YgoCalc.INSTANCE.tester();
    }

    private void exitMenuItemClicked(ActionEvent e) {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void settingsMenuItemClicked(ActionEvent e) {

    }

    private void chooseDBClick(ActionEvent e) {
        YgoCalc.INSTANCE.selectDB();
    }

    private kotlin.Unit dbUpdated(ChangedArgs<YgoProDB> args) {
        if (args.getNewValue() != null) {
            this.dbField.setText(args.getNewValue().getPath().toAbsolutePath().toString());

            final YgoProDB.DBStates state = args.getNewValue().getState();
            if (state.getHasValidData()) {
                this.setDatabaseValid(true);
                this.dbField.setToolTipText(null);
            } else {
                this.setDatabaseValid(false);
                if (state == YgoProDB.DBStates.FILE_DOES_NOT_EXIST) {
                    this.dbField.setToolTipText("The specified file does not exist.");
                } else if (state == YgoProDB.DBStates.UNLOADED) {
                    this.dbField.setToolTipText("The data from this database was not loaded.");
                } else if (state == YgoProDB.DBStates.OTHER_ERROR) {
                    this.dbField.setToolTipText("<html>An error was encountered during loading of type \"" + args.getNewValue().getLoadingException().getClass().getName() + ".\"" +
                            "<br>Message:   " + args.getNewValue().getLoadingException().getMessage() + "</html>");
                }
            }
        } else {
            this.dbField.setText("No Database Loaded");
            this.setDatabaseValid(false);
        }

        return Unit.INSTANCE;
    }

    private kotlin.Unit deckLoaded(ChangedArgs<Deck> args) {
        this.loadDeck(args.getNewValue());
        return Unit.INSTANCE;
    }
}
