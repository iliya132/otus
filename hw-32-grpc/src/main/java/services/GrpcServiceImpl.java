package services;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.CounterRequest;
import ru.otus.protobuf.generated.CounterResponse;
import ru.otus.protobuf.generated.RemoteCounterServiceGrpc;

public class GrpcServiceImpl extends RemoteCounterServiceGrpc.RemoteCounterServiceImplBase {

    @Override
    public void count(CounterRequest request, StreamObserver<CounterResponse> responseObserver) {
        var firstValue = request.getFirstValue();
        var lastValue = request.getLastValue();
        var currentValue = firstValue;
        do {
            responseObserver.onNext(CounterResponse.newBuilder().setCount(currentValue).build());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (++currentValue < lastValue);
        responseObserver.onCompleted();
    }
}
