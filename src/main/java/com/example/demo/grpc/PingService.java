package com.example.demo.grpc;

import com.example.demo.exception.InvalidNameException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.v1.PingRequest;
import com.example.demo.v1.PingResponse;
import com.example.demo.v1.PingerGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PingService extends PingerGrpc.PingerImplBase {

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        if (request.getName().equals("throw1")) {
            throw new InvalidNameException("Throwing exception 1");
        }

        if (request.getName().equals("throw2")) {
            throw new UserNotFoundException("Throwing exception 2");
        }

        String msg = "PONG > " + request.getName();
        PingResponse reply = PingResponse.newBuilder().setMessage(msg).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
