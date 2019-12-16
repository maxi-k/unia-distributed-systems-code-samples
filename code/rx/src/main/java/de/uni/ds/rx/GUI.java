package de.uni.ds.rx;

import java.util.function.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class GUI {

    private JFrame frame;
    private JTextField input;
    private JLabel output;

    public GUI(final String initialText, final Consumer<String> callback) {
        createFrame(initialText);

        this.input.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { callback.accept(input.getText()); }
            
        });
    }

    private void createFrame(final String initialText) {
        // 1. Create the frame.
        this.frame = new JFrame("RxDemo");

        // 2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 3. Create components and put them in the frame.
        this.input = new JTextField(initialText);
        this.output = new JLabel(initialText);
        frame.getContentPane().add(input, BorderLayout.NORTH);
        frame.getContentPane().add(output, BorderLayout.CENTER);

        // 4. Size the frame.
        frame.setSize(500, 500);
        // frame.pack();

        // 5. Show it.
        frame.setVisible(true);
    }

    public void updateText(final String newText) {
        this.output.setText(newText);
    }

}