package com.croffgrin.ygocalc.gui;

import com.croffgrin.ygocalc.card.Card;
import com.croffgrin.ygocalc.card.Deck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class DeckViewer extends YgoCalcFormBase {
    private final Deck deck;

    private JList<Card> mainList;
    private JList<Card> extraList;
    private JList<Card> sideList;
    private JButton closeButton;
    private JPanel mainPanel;

    public DeckViewer(Deck deck) {
        super(deck.getName());
        this.deck = deck;

        this.mainList.setListData(deck.getMain().cardArray());
        this.extraList.setListData(deck.getExtra().cardArray());
        this.sideList.setListData(deck.getSide().cardArray());

        this.closeButton.addActionListener(e -> this.dispose());
        this.setContentPane(this.mainPanel);
        this.setSize(new Dimension(1200, 700));
    }

    public Deck getDeck() {
        return this.deck;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}