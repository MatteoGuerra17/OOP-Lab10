package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class AnotherConcurrentGUI {
    private static final long WAITING_TIME = TimeUnit.SECONDS.toMillis(10);
    private final Agent2 agent2 = new Agent2();
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel txt = new JLabel();
    private final JButton stop = new JButton("stop");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");

    public AnotherConcurrentGUI() {
        final JFrame frame = new JFrame();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(txt);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        //actions
        stop.addActionListener(e -> {
            agent2.stopCount();
            up.setEnabled(false);
            down.setEnabled(false);
            stop.setEnabled(false);
        });
        up.addActionListener(e -> agent2.inc());
        down.addActionListener(e -> agent2.dec());
        //
        new Thread(agent2).start();
        new Thread(() -> {
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.stopCounting();
        }).start();
    }
    private void stopCounting() {
        agent2.stopCount();
        SwingUtilities.invokeLater(() -> {
            stop.setEnabled(false);
            up.setEnabled(false);
            down.setEnabled(false);
        });
    }
    //
    private class Agent2 implements Runnable {
        //fields
        private int counter;
        private boolean stop;
        private boolean up = true;

        public void run() {
            while (!this.stop) {
                try {
                    if (this.up) {
                        SwingUtilities.invokeLater(() -> AnotherConcurrentGUI.this.txt.setText(Integer.toString(this.counter)));
                        this.counter++;
                        Thread.sleep(100);
                    } else {
                        SwingUtilities.invokeLater(() -> AnotherConcurrentGUI.this.txt.setText(Integer.toString(this.counter)));
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
