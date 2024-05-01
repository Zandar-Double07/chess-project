package com.chessendings;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.*;
public class ChessComboUI extends JFrame {
    JCheckBox checkMateBox;
    JCheckBox staleMateBox;
    JCheckBox insufficientBox;
    JCheckBox repetitionBox;
    JCheckBox fifetyMoveBox;

    JButton findCombosButton;

    JTextField fileInput;
    JTextArea sanInput;
    public ChessComboUI(){

        //Set up frame 
        this.setTitle("Chess Game Endings Finder");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(500, 350);

        //Create Left side of the rame
        JPanel textInputPanel = new JPanel(new BorderLayout(30, 30));

        sanInput = makeSANInputComponent("chess-san-string");
        fileInput = makeFileInputComponent("insert a file path here");
        findCombosButton = makeButtonComponent();
        textInputPanel.add(sanInput, BorderLayout.CENTER);
        textInputPanel.add(fileInput, BorderLayout.NORTH);
        textInputPanel.add(findCombosButton, BorderLayout.SOUTH);

        //Create right side of the frame
        JPanel checkBoxList = new JPanel( new GridLayout(5, 1, 10, 10));
        this.add(textInputPanel, BorderLayout.WEST);
        this.add(checkBoxList, BorderLayout.EAST);

        //Check Box components
        checkMateBox = makeCheckBoxComponent("checkmate");
        staleMateBox = makeCheckBoxComponent("stalemate");
        insufficientBox = makeCheckBoxComponent("insuicient material");
        repetitionBox = makeCheckBoxComponent("repetition");
        fifetyMoveBox = makeCheckBoxComponent("50 move rule");
        
        checkBoxList.add(checkMateBox);
        checkBoxList.add(staleMateBox);
        checkBoxList.add(insufficientBox);
        checkBoxList.add(repetitionBox);
        checkBoxList.add(fifetyMoveBox);

        //add all components and inish set up
        this.setVisible(true);
        this.repaint();
    }

    private static JCheckBox makeCheckBoxComponent(String text){
        JCheckBox checkBox = new JCheckBox(text);
        return checkBox;
    }

    private static JTextField makeFileInputComponent(String text){
        JTextField textBox = new JTextField();
        textBox.setText(text);
        textBox.setSize(20, 100);
        return textBox;
    }

    private static JTextArea makeSANInputComponent(String text){
        JTextArea textBox = new JTextArea();
        textBox.setSize(20, 100);
        return textBox;
    }

    private static JButton makeButtonComponent(){
        JButton button = new JButton();
        button.setText("Find Endings");
        button.setSize(20, 60);
        return button;
    }

    public boolean doCheckMate(){
        return checkMateBox.isSelected();
    }

    public boolean doStaleMate(){
        return staleMateBox.isSelected();
    }

    public boolean doInsuicientMaterial(){
        return insufficientBox.isSelected();
    }

    public boolean doRepetition(){
        return repetitionBox.isSelected();
    }

    public boolean do50MoveRule(){
        return fifetyMoveBox.isSelected();
    }
}
