package networking.client;

import networking.shared.RMIServer;
import networking.shared.Message;
import networking.shared.RMIClient;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientImpl implements RMIClient, Client {

    private String username;
    private PropertyChangeSupport property;
    private RMIServer server;

    public RMIClientImpl() {
        this.property = new PropertyChangeSupport(this);
    }

    @Override
    public void messageReceived(String message) {
        System.out.println("Received: " + message);
        property.firePropertyChange("NewMessage", null, message);
    }

    @Override
    public String getUsername() {
        return this.username;
    }

  @Override
    public void startClient()
  {
      try {
          Registry registry = LocateRegistry.getRegistry("localhost", 1099);
          this.server = (RMIServer) registry.lookup("Server");
          UnicastRemoteObject.exportObject(this, 0);
          server.registerClient(this);
          System.out.println("Client connected to server ");
      } catch (RemoteException | NotBoundException e){
          e.printStackTrace();
      }
  }

  @Override
  public void sendMessage(Message message) {
        try {
            server.sendMessage(new Message(this.username + ": " + message));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public void greetingsMessage(Message message) {
        try {
            this.server.greetingsMessage(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addListener(String name, PropertyChangeListener listener) {
        this.property.addPropertyChangeListener(name, listener);
    }

    @Override
    public void removeListener(String name, PropertyChangeListener listener) {
        this.property.removePropertyChangeListener(name, listener);
    }
}
