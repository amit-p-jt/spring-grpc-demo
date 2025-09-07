package com.example.demo.grpc;

import com.example.demo.v1.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PongService extends PongerGrpc.PongerImplBase {

    @GrpcClient("pingerService")
    private PingerGrpc.PingerBlockingStub pingerClient;

    @Override
    public void pong(PongRequest request, StreamObserver<PongResponse> responseObserver) {
        PingRequest pingRequest = PingRequest.newBuilder()
                .setName(request.getName())
                .build();

        PingResponse pingResponse = pingerClient.ping(pingRequest);

        PongResponse.Builder pongBuilder = PongResponse.newBuilder();

        if (pingResponse.hasMessage()) {
            pongBuilder.setMessage("Pong received: " + pingResponse.getMessage());
        } else if (pingResponse.hasError()) {
            pongBuilder.setError(pingResponse.getError());
        }

        responseObserver.onNext(pongBuilder.build());
        responseObserver.onCompleted();
    }
}
