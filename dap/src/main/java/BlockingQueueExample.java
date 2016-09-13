import java.util.UUID;
import java.util.concurrent.*;

public class BlockingQueueExample {

    public static void main(String[] args) throws Exception {

        TransferQueue<String> queue = new LinkedTransferQueue<>();
        Runnable p = () -> {
            for (; ; ) queue.add(UUID.randomUUID().toString());
        };
        new Thread(p).start();

        Thread.sleep(5000);

        System.out.println(queue.remainingCapacity());
    }
}