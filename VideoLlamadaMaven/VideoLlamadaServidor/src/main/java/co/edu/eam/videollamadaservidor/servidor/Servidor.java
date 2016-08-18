package co.edu.eam.videollamadaservidor.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor implements Runnable {

    private List<HiloCLiente> clientes;

    public Servidor() {
        clientes = new ArrayList<>();
    }

    @Override
    public void run() {
        try (ServerSocket ssocket = new ServerSocket(45000);) {
            while (true) {
                try {
                    Socket soc = ssocket.accept();
                    HiloCLiente h = new HiloCLiente(soc,this);
                    clientes.add(h);
                    new Thread(h).start();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    //maricon
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<HiloCLiente> getClientes() {
        return clientes;
    }

    public void setClientes(List<HiloCLiente> clientes) {
        this.clientes = clientes;
    }

    
}
