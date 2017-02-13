package com.croffgrin.ygocalc.ui.component;

import com.croffgrin.ygocalc.card.CardSet;
import com.croffgrin.ygocalc.card.Deck;

import javax.swing.*;

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
public class VerticalDeckView {
    private JPanel verticalDeckViewPanel;
    private CardSetView mainView;
    private CardSetView extraView;
    private CardSetView sideView;
    private Deck deck;

    public Deck getDeck() {
        return this.deck;
    }

    public void setDeck(Deck deck) {
        if (deck != null) {
            this.mainView.setCardSet(deck.getMain());
            this.extraView.setCardSet(deck.getExtra());
            this.sideView.setCardSet(deck.getSide());
        } else {
            this.mainView.setCardSet(CardSet.Companion.empty());
            this.extraView.setCardSet(CardSet.Companion.empty());
            this.sideView.setCardSet(CardSet.Companion.empty());
        }
    }

    public VerticalDeckView() {
        this.mainView.setName("Main");
        this.extraView.setName("Extra");
        this.sideView.setName("Side");
    }
}
