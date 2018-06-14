import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 3;

    public static final String NAME = "Lappo";

    public JFrame frame;

    public boolean running = false;

    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public Game() {

        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void run() {

        long lastTime = System.nanoTime();

        // 1,000,000,000 nanoseconds per second
        // 60 ticks per second

        double nanoSecondsPerSecond = 1000000000D;
        double ticksPerSecond = 60D;

        double nanoSecondsPerTick = nanoSecondsPerSecond / ticksPerSecond;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (running) {

            long now = System.nanoTime();

            delta += (now - lastTime) / nanoSecondsPerTick;
            lastTime = now;

            boolean shouldRender = false;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;

                shouldRender = true;
            }

            if (shouldRender) {

                frames++;
                render();

            }

            // Only goes through the if statement every one second

            if (System.currentTimeMillis() - lastTimer > 1000) {

                lastTimer += 1000;

                System.out.println("frames: " + frames + ", ticks: " + ticks);

                frames = 0;
                ticks = 0;

            }

        }

    }

    public void tick() {

        tickCount++;

        for (int i = 0; i < pixels.length; i++) {

            pixels[i] = i + tickCount;

        }

    }

    public void render() {

        BufferStrategy bufferStrategy = getBufferStrategy();

        if (bufferStrategy == null) {

            createBufferStrategy(3);

            return;

        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.RED);
        graphics.fillRect(0,0,getWidth(),getHeight());

        graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        graphics.dispose();
        bufferStrategy.show();

    }

    public synchronized void start() {

        // TODO:

        running = true;

        new Thread(this).start();

    }

    public synchronized void stop() {

        // TODO:

        running = false;

    }

    public static void main(String[] args) {

        new Game().start();

    }

}
