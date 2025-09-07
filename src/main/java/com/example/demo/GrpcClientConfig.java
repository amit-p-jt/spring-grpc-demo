package com.example.demo;

import com.example.demo.grpc.interceptor.RequestIdClientInterceptor;
import io.grpc.ClientInterceptor;
import net.devh.boot.grpc.client.interceptor.GlobalClientInterceptorConfigurer;
import net.devh.boot.grpc.client.interceptor.GlobalClientInterceptorRegistry;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GrpcClientConfig implements GlobalClientInterceptorConfigurer {

    private final RequestIdClientInterceptor requestIdInterceptor;

    public GrpcClientConfig(RequestIdClientInterceptor requestIdInterceptor) {
        this.requestIdInterceptor = requestIdInterceptor;
    }

    @Override
    public void configureClientInterceptors(List<ClientInterceptor> interceptors) {
        interceptors.add(requestIdInterceptor);
    }
}
