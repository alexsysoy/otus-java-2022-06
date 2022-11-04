import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlterThreads {
    private static final Logger logger = LoggerFactory.getLogger(AlterThreads.class);

    private final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        var alterThread = new AlterThreads();
        var commonResource = new CommonResource(1, false);
        new Thread(() -> alterThread.action("Первый поток", Boolean.TRUE, commonResource)).start();
        new Thread(() -> alterThread.action("Второй поток", Boolean.FALSE, commonResource)).start();
    }

    public void action(String name, Boolean isFirstThread, CommonResource commonResource) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                lock.lock();
                if (isFirstThread && !commonResource.isFirstThreadFinish()) {
                    logger.info("{} : {}", name, commonResource.getCount());
                    commonResource.setFirstThreadFinish(true);
                    sleep();
                }
                if (!isFirstThread && commonResource.isFirstThreadFinish()) {
                    logger.info("{} : {}", name, commonResource.getCount());
                    commonResource.setFirstThreadFinish(false);
                    commonResource.countMoveToNext();
                    sleep();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
