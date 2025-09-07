package com.example.demo;

import com.example.demo.grpc.interceptor.LoggingServerInterceptor;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GrpcServerConfig implements GlobalServerInterceptorConfigurer {

    private final LoggingServerInterceptor loggingServerInterceptor;

    public GrpcServerConfig(LoggingServerInterceptor loggingServerInterceptor) {
        this.loggingServerInterceptor = loggingServerInterceptor;
    }

    @Override
    public void configureServerInterceptors(List<ServerInterceptor> interceptors) {
        interceptors.add(loggingServerInterceptor);
    }
}
