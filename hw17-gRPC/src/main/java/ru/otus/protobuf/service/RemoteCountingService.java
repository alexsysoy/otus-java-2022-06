package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.CountingServiceGrpc;
import ru.otus.protobuf.generated.MessageInitialData;
import ru.otus.protobuf.generated.MessageValue;
import ru.otus.protobuf.util.GrpcUtil;

public class RemoteCountingService extends CountingServiceGrpc.CountingServiceImplBase {
    private static final long TIME_TO_SLEEP = 2000;

    @Override
    public void getValueFromServer(MessageInitialData request, StreamObserver<MessageValue> responseObserver) {
        System.out.println("Work with the client has begun");
        for (int i = request.getFirstValue() + 1; i <= request.getLastValue();  i++) {
            GrpcUtil.sleep(TIME_TO_SLEEP);
            MessageValue messageValue = MessageValue.newBuilder()
                    .setCount(i)
                    .build();

            responseObserver.onNext(messageValue);
            System.out.printf("Message with the count value was sent: %d%n", i);
        }
        responseObserver.onCompleted();
        System.out.println("The work with the client is finished");
    }
}
