package networking.server;

import networking.shared.RMIServer;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {
    public static void main(String[] args) throws IOException, AlreadyBoundException {
        RMIServer server = new RMIServerImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("Server", server);
        System.out.println("Server started");
    }
}
