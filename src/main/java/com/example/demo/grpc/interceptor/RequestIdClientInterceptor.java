package com.example.demo.grpc.interceptor;

import io.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class RequestIdClientInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> REQUEST_ID_HEADER =
            Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // Add requestId from context to headers automatically
                String requestId = LoggingServerInterceptor.getRequestId(); // get from server interceptor context
                if (requestId != null) {
                    headers.put(REQUEST_ID_HEADER, requestId);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
