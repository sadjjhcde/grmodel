package grm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//developed by Alexey Lang
//alexey.n.lang@gmail.com
//https://github.com/sadjjhcde

public class GRModel {

    private static final int FRAME_TIMEOUT_MS = 100;

    public static void main(String[] args) {
        new GRModel();
    }

    public GRModel() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    System.out.println(ex.toString());
                }

                GRModelPanel grModelPanel = new GRModelPanel();
                JFrame frame = new JFrame("Grow-Reduce Model");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(grModelPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
                Timer timer = new Timer(FRAME_TIMEOUT_MS, grModelPanel);
                timer.setInitialDelay(FRAME_TIMEOUT_MS * 10);
                timer.start();
            }
        });
    }

    public static class GRModelPanel extends JPanel implements ActionListener {

        private final int[][] possibleCells = {
                {LIMIT, 0, 0},
                {0, LIMIT, 0},
                {0, 0, LIMIT}
        };

        private int[][][] cells = new int[MODEL_SIZE][MODEL_SIZE][3];

        public GRModelPanel() {
            //initialize model
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    cells[i][j] = new int[]{0, 0, 0};
                }
            }

            // 1
//            buildFullRandomModel(4);

            // 2
            buildFullRandomModel(20);

            // 3
//            buildFullRandomModel(120);

            // 4
//            buildRandomSquareModel(60, 60, 20, 5);

            // 5
//            buildAlternationSquareModel(50, 50, 20, 6);

            // 6
//            buildAlternationSquareModel(25, 25, 3, 50);

            // 7
//            buildAlternationSquareModel(20, 20, 2, 80);

            // 8
//            buildRandomSquareModel(10, 10, 3, 60);

            // 9
//            buildRandomSquareModel(10, 10, 4, 45);
        }

        private void buildFullRandomModel(int oneColorCellsCount) {
            Random rnd = new Random();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < oneColorCellsCount; j++) {
                    int x = rnd.nextInt(cells.length);
                    int y = rnd.nextInt(cells[0].length);
                    cells[x][y] = possibleCells[i];
                }
            }
        }

        private void buildAlternationSquareModel(int leftUpX, int leftUpY, int distance, int squareSize) {
            for (int x = 0; x < squareSize; x++) {
                for (int y = 0; y < squareSize; y++) {
                    cells[leftUpX + x * distance][leftUpY + y * distance] = possibleCells[(x + y) % possibleCells.length];
                }
            }
        }

        private void buildRandomSquareModel(int leftUpX, int leftUpY, int distance, int squareSize) {
            Random rnd = new Random();
            for (int x = 0; x < squareSize; x++) {
                for (int y = 0; y < squareSize; y++) {
                    cells[leftUpX + x * distance][leftUpY + y * distance] = possibleCells[rnd.nextInt(possibleCells.length)];
                }
            }
        }

        private static final int WINDOW_SIZE_PIXELS = 600;
        private static final int CELL_SIZE_PIXELS = 3;

        private static final int MODEL_SIZE = WINDOW_SIZE_PIXELS / CELL_SIZE_PIXELS;
        private static final int LIMIT = 250;
        private static final int GROW_LIMIT = 100;
        private static final int REDUCE_LIMIT = 200;
        private static final int STEP = 10;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            updateModel();

            //redraw
            for (int x = 0; x < cells.length; x++) {
                for (int y = 0; y < cells[0].length; y++) {
                    g.setColor(new Color(cells[x][y][0], cells[x][y][1], cells[x][y][2]));
                    g.fillRect(x * CELL_SIZE_PIXELS, y * CELL_SIZE_PIXELS, x * CELL_SIZE_PIXELS + CELL_SIZE_PIXELS, y * CELL_SIZE_PIXELS + CELL_SIZE_PIXELS);
                }
            }
        }

        private void updateModel() {
            int[][][] newFrame = new int[MODEL_SIZE][MODEL_SIZE][3];
            for (int x = 0; x < cells.length; x++) {
                for (int y = 0; y < cells[0].length; y++) {
                    int[] temp_cell = cells[x][y];
                    if (x > 0) {
                        processCell(temp_cell, cells[x - 1][y]);
                    }
                    if (x < cells.length - 1) {
                        processCell(temp_cell, cells[x + 1][y]);
                    }
                    if (y > 0) {
                        processCell(temp_cell, cells[x][y - 1]);
                    }
                    if (y < cells.length - 1) {
                        processCell(temp_cell, cells[x][y + 1]);
                    }
                    newFrame[x][y] = temp_cell;

                }
            }
            cells = newFrame;
        }

        private void processCell(int[] current, int[] neighbour) {
            if (neighbour[0] >= GROW_LIMIT && current[0] < LIMIT - STEP) {
                current[0] += STEP;
            }
            if (neighbour[1] >= GROW_LIMIT && current[1] < LIMIT - STEP) {
                current[1] += STEP;
            }
            if (neighbour[2] >= GROW_LIMIT && current[2] < LIMIT - STEP) {
                current[2] += STEP;
            }
            if (neighbour[0] >= REDUCE_LIMIT && current[1] >= STEP) {
                current[1] -= STEP;
            }
            if (neighbour[1] >= REDUCE_LIMIT && current[2] >= STEP) {
                current[2] -= STEP;
            }
            if (neighbour[2] >= REDUCE_LIMIT && current[0] >= STEP) {
                current[0] -= STEP;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(WINDOW_SIZE_PIXELS, WINDOW_SIZE_PIXELS);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.revalidate();
            this.repaint();
        }
    }
}