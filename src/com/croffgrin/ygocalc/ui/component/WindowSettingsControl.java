package com.croffgrin.ygocalc.ui.component;

import com.croffgrin.ygocalc.util.WindowSettings;

import javax.swing.*;
import java.awt.*;

/**
 * Copyright (c) 2017 Nathan S. Templon
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
public class WindowSettingsControl {

    private JLabel titleLabel;
    private IntegerTextField yField;
    private IntegerTextField widthField;
    private IntegerTextField heightField;
    private IntegerTextField xField;
    private JPanel mainPanel;
    private JCheckBox rememberBox;


    public String getTitle() {
        return this.titleLabel.getText();
    }

    public void setTitle(String title) {
        this.titleLabel.setText(title);
    }

    public WindowSettings getSettings() {
        return new WindowSettings(this.rememberBox.isSelected(),
                new Point((Integer)this.xField.getValue(), (Integer)this.yField.getValue()),
                new Dimension((Integer)this.widthField.getValue(), (Integer)this.heightField.getValue()));
    }

    public void setSettings(WindowSettings settings) {
        this.rememberBox.setSelected(settings.getRememberSettings());
        this.xField.setValue(settings.getWindowPosition().getX());
        this.yField.setValue(settings.getWindowPosition().getY());
        this.widthField.setValue(settings.getWindowSize().getWidth());
        this.heightField.setValue(settings.getWindowSize().getHeight());
    }

}
