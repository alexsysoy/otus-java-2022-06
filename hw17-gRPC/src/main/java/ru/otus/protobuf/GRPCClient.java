package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.CountingServiceGrpc;
import ru.otus.protobuf.generated.MessageInitialData;
import ru.otus.protobuf.generated.MessageValue;
import ru.otus.protobuf.util.GrpcUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class GRPCClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int FIRST_VALUE = 0;
    private static final int LAST_VALUE = 30;
    private static final long TIME_TO_SLEEP = 1000;
    private final AtomicInteger valueFromServer;

    public GRPCClient() {
        valueFromServer = new AtomicInteger(0);
        System.out.println("Created client instance");
    }

    public static void main(String[] args) throws InterruptedException {
        GRPCClient client = new GRPCClient();
        client.start();
    }

    private void start() throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        MessageInitialData request = MessageInitialData.newBuilder()
                .setFirstValue(FIRST_VALUE)
                .setLastValue(LAST_VALUE)
                .build();

        var stub = CountingServiceGrpc.newStub(channel);
        var latch = new CountDownLatch(1);

        stub.getValueFromServer(request, new StreamObserver<MessageValue>() {
            @Override
            public void onNext(MessageValue value) {
                int count = value.getCount();
                valueFromServer.set(count);
                System.out.printf("Received value from server: %d%n", valueFromServer.get());
            }

            @Override
            public void onError(Throwable t) {
                System.out.printf("Error: %d%n", t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed receipt");
                latch.countDown();
            }
        });

        int currentValueFromServer = 0;
        int currentValue = 0;
        for (int i = 0; i <= 50; i++) {
            GrpcUtil.sleep(TIME_TO_SLEEP);
            int intValueFromServer = valueFromServer.intValue();
            if (currentValueFromServer == intValueFromServer) {
                currentValue++;
            } else {
                currentValue = currentValue + 1 + valueFromServer.getAndSet(0);
            }
            currentValueFromServer = intValueFromServer;
            System.out.printf("Current value: %d%n", currentValue);
        }

        latch.await();
        channel.shutdown();
    }
}
