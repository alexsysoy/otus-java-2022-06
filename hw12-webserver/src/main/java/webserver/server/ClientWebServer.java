package webserver.server;

public interface ClientWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
