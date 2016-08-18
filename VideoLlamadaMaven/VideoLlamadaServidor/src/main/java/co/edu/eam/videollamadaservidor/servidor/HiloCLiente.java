package co.edu.eam.videollamadaservidor.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloCLiente implements Runnable {

    private Socket con;
    private String ip;
    private Servidor servidor;

    public HiloCLiente(Socket con, Servidor servidor) {
        super();
        this.con = con;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try {
            //conexion sea larga...
            //abrir flujos....
            System.out.println("CONECTO");
            PrintStream salidaTxt = new PrintStream(this.con.getOutputStream());
            BufferedReader entradaTxt = new BufferedReader(new InputStreamReader(this.con.getInputStream()));
            while (true) {
                String peticion = entradaTxt.readLine();
                if (!peticion.isEmpty() && !peticion.equals("")) {
                    setIp(peticion);
                }
                String ips = "";
                for (int i = 0; i < servidor.getClientes().size(); i++) {
                   // if (!servidor.getClientes().get(i).getIp().equals(this.getIp())) {
                        if (i == servidor.getClientes().size() - 1) {
                            ips += servidor.getClientes().get(i).getIp();
                            break;
                        }
                        ips += servidor.getClientes().get(i).getIp() + ",";
                   // }//
                }
                salidaTxt.println(ips);
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloCLiente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
