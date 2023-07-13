import io.grpc.ServerBuilder;
import services.GrpcServiceImpl;

public class GrpcServer {
    private static final int PORT = 80;

    public static void main(String[] args) throws Exception {
        var grpcService = new GrpcServiceImpl();
        var server = ServerBuilder.forPort(PORT).addService(grpcService).build();
        server.start();
        System.out.println("Started to listen on port " + PORT);
        server.awaitTermination();
    }
}
