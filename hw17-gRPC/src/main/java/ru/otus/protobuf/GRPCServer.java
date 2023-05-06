package ru.otus.protobuf;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.RemoteCountingService;

import java.io.IOException;
import java.util.concurrent.Executors;

public class GRPCServer {

    private static final int SERVER_PORT = 8190;
    private final Server server;

    public GRPCServer() {
        this.server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new RemoteCountingService())
                .executor(Executors.newFixedThreadPool(5))
                .build();
        System.out.println("Created server instance");
    }

    public static void main(String[] args) throws Exception {
        GRPCServer grpcServer = new GRPCServer();
        grpcServer.start();
        grpcServer.server.awaitTermination();
    }

    private void start() throws IOException {
        server.start();
        System.out.println("Server started and waiting for client connections... ");
    }
}
