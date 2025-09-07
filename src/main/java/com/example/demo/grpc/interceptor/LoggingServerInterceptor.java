package com.example.demo.grpc.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class LoggingServerInterceptor implements ServerInterceptor {

    private static final Context.Key<String> REQUEST_ID_CTX = Context.key("requestId");
    private static final Metadata.Key<String> REQUEST_ID_HEADER =
            Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // Extract or generate request ID
        String requestId = headers.get(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        // Attach request ID to gRPC context
        Context ctxWithRequestId = Context.current().withValue(REQUEST_ID_CTX, requestId);

        // Wrap the server call to log outgoing responses
        String finalRequestId = requestId;

        ServerCall<ReqT, RespT> loggingCall = new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void sendMessage(RespT message) {
                log.info("[{}] Outgoing response: {}", finalRequestId, message);
                super.sendMessage(message);
            }
        };

        // Intercept the call with context
        ServerCall.Listener<ReqT> listener = Contexts.interceptCall(ctxWithRequestId, loggingCall, headers, next);

        // Wrap listener to log incoming requests
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {
            @Override
            public void onMessage(ReqT message) {
                log.info("[{}] Incoming request: {}", finalRequestId, message);
                super.onMessage(message);
            }
        };
    }

    // Utility to get request ID anywhere in this gRPC thread
    public static String getRequestId() {
        return REQUEST_ID_CTX.get();
    }
}
