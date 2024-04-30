package com.chessendings;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.*;
public class ChessComboUI {
    static JFrame buildFrame(){
        JFrame frame = new JFrame("Chess endings finder");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel TextInputPanel = new JPanel(new BorderLayout());
        JPanel checkBoxList = new JPanel( new GridLayout());
        JButton button = makeButtonComponent();
        Component checkBox = makeCheckBoxComponent();
        frame.add(button, BorderLayout.EAST);
        frame.add(checkBox, BorderLayout.WEST);
        frame.setSize(500, 350);
        frame.setVisible(true);
        frame.repaint();
        
        return frame;
    }

    static Component makeCheckBoxComponent(){
        
        JCheckBox checkBox = new JCheckBox("This is a checkbox");
        return checkBox;
    }

    static Component makeTextBoxComponent(String text){
        JTextField textBox = new JTextField(text);
        textBox.setSize(20, 100);
        return null;
    }

    static JButton makeButtonComponent(){
        JButton button = new JButton();
        button.setText("Find Endings");
        button.setSize(20, 60);
        return button;
    }
}
