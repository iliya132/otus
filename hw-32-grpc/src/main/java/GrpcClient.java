import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.CounterRequest;
import ru.otus.protobuf.generated.CounterResponse;
import ru.otus.protobuf.generated.RemoteCounterServiceGrpc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GrpcClient {
    private final AtomicLong lastValueFromServer = new AtomicLong(0L);
    private final AtomicLong lastUsedValueFromServer = new AtomicLong(0L);
    private final AtomicLong counter = new AtomicLong(0L);
    private final AtomicBoolean isCompleted = new AtomicBoolean(false);
    private final AtomicInteger iterationNumber = new AtomicInteger(0);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 80;

    public static void main(String[] args) throws InterruptedException {
        new GrpcClient().start();
    }

    private void start() throws InterruptedException {
        requestStream();
        printNumbers();
    }

    private void requestStream() {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var stub = RemoteCounterServiceGrpc.newStub(channel);
        var request = CounterRequest.newBuilder().setFirstValue(0L).setLastValue(10L).build();
        stub.count(request, new StreamObserver<>() {
            @Override
            public void onNext(CounterResponse value) {
                System.out.println("new value from server: " + value.getCount());
                lastValueFromServer.set(value.getCount());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
                isCompleted.set(true);
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed reading from server");
            }
        });
    }

    private void printNumbers() throws InterruptedException {
        Thread newThread = new Thread(() -> {
            while (!isCompleted.get() && iterationNumber.getAndIncrement() < 50) {
                var serverValue = getServerValueOrZeroIfUsed();
                var current = counter.addAndGet(++serverValue);
                System.out.println(current);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        newThread.start();
        newThread.join();

    }

    private Long getServerValueOrZeroIfUsed() {
        if (lastValueFromServer.get() == lastUsedValueFromServer.get()) {
            return 0L;
        } else {
            lastUsedValueFromServer.set(lastValueFromServer.get());
            return lastValueFromServer.get();
        }
    }
}
