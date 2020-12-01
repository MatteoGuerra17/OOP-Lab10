package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class ConcurrentGUI {

    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel txt = new JLabel();
    private final JButton stop = new JButton("stop");

    public ConcurrentGUI() {
        final JFrame frame = new JFrame();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        final JButton up = new JButton("up");
        final JButton down = new JButton("down");
        panel.add(txt);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        /*
         * create an Agent
         */
        final Agent agent = new Agent();
        new Thread(agent).start();
        // action
        stop.addActionListener(e -> {
            agent.stopCount();
            up.setEnabled(false);
            down.setEnabled(false);
            stop.setEnabled(false);
        });
        up.addActionListener(e -> agent.inc());
        down.addActionListener(e -> agent.dec());
    }
    /*
     * create Agent class
     */
    private class Agent implements Runnable {
        //fields
        private int counter;
        private boolean stop;
        private boolean up = true;

        public void run() {
            while (!this.stop) {
                try {
                    if (this.up) {
                        SwingUtilities.invokeLater(() -> ConcurrentGUI.this.txt.setText(Integer.toString(this.counter)));
                        this.counter++;
                        Thread.sleep(100);
                    } else {
                        SwingUtilities.invokeLater(() -> ConcurrentGUI.this.txt.setText(Integer.toString(this.counter)));
                        this.counter--;
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void inc() {
            this.up = true;
        }
        public void dec() {
            this.up = false;
        }
        public void stopCount() {
            this.stop = true;
        }
    }
}
