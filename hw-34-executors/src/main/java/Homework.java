import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Homework {
    private static final Logger logger = LoggerFactory.getLogger(Homework.class);
    private boolean switcher = false;

    private synchronized void action(boolean onSwitch) {
        int counter = 1;
        int increment = 1;
        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (switcher == onSwitch) {
                    this.wait();
                }
                switcher = onSwitch;
                logger.info("{}", counter);
                counter = counter + increment;
                if(counter == 10) {
                    increment = -increment;
                }
                if(counter == 0) {
                    sleep();
                    notifyAll();
                    return;
                }
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Homework pingPong = new Homework();
        new Thread(() -> pingPong.action(true)).start();
        new Thread(() -> pingPong.action(false)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000); // 1s
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
