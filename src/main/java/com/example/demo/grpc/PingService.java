package com.example.demo.grpc;

import com.example.demo.v1.PingRequest;
import com.example.demo.v1.PingResponse;
import com.example.demo.v1.PingerGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class PingService extends PingerGrpc.PingerImplBase {

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        String msg = "PONG > " + request.getName();
        PingResponse reply = PingResponse.newBuilder().setMessage(msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
