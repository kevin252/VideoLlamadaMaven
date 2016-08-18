package co.edu.eam.videollamadaservidor.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloCLiente implements Runnable {

    private Socket con;
    private String ip;
    private Servidor servidor;
    private HiloCLiente receptor;

    public HiloCLiente(Socket con, Servidor servidor) {
        super();
        this.con = con;
        this.servidor = servidor;
        this.receptor = null;
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
                //obtenemos peticion del cliente
                String peticion = entradaTxt.readLine();
                //la procesamos
                validarPeticion(peticion, salidaTxt);
                //enviamos los clientes que estan conectados
                enviarClientes(salidaTxt);
                //validamos si hay que compartir video
                validarVideo();
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloCLiente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para que 2 clientes compartan video
     * @throws SocketException
     * @throws IOException 
     */
    private void validarVideo() throws SocketException, IOException {
        if (receptor != null) {
            //abrimos servidor
            try (DatagramSocket socketIn = new DatagramSocket(45000)) {
                byte[] buffer = new byte[10000];
                //paquete dondre recibiremos 
                DatagramPacket pktIn = new DatagramPacket(buffer, buffer.length);
                //recibimos paquete
                socketIn.receive(pktIn);
                //lo convertimos en bytes para reenviarlo
                byte[] bites = pktIn.getData();
                //abrimos socket para enviar packete al cliente recpetor
                try (DatagramSocket socketOut = new DatagramSocket()) {
                    //creamos paquete a enviar al receptor
                    DatagramPacket pktOut = new DatagramPacket(bites, bites.length, 
                            InetAddress.getByName(receptor.getIp()), 45000);
                    //enviamos paquete
                    socketOut.send(pktOut);
                }
            }
        }
    }

    /**
     * Método para validar la petición que el cliente le hace al servidor
     *
     * @param peticion, peticion del cliente
     * @param salidaTxt, stream para enviarle la respuesta al cliente
     * @throws IOException
     */
    private void validarPeticion(String peticion, PrintStream salidaTxt) throws IOException {
        //validamos que exista una peticion
        if (!peticion.isEmpty() && !peticion.equals("")) {
            String[] p = peticion.split(":");
            //si la peticion es con, se busca el cliente con el que se realizara
            //la conexion de pantallas
            if (p[0].equals("CON")) {
                //buscamos el cliente receptor
                HiloCLiente receptor = buscarHiloCLiente(p[1]);
                //abrimos los flujos para la solicitud
                PrintStream salida = new PrintStream(receptor.getCon().getOutputStream());
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(receptor.getCon().getInputStream()));
                //le solicitamos la conexión
                salida.println("CON");
                //obtenemos respuesta del receptor
                String resp = entrada.readLine();
                //verificamos que la respuesta sea ok
                if (!resp.isEmpty() && !resp.equals("") && resp.equals("OK")) {
                    salidaTxt.println(resp);
                    this.receptor = receptor;
                    receptor.setReceptor(this);
                } else {
                    salidaTxt.println("NO");
                }
            } else {
                //si no, es porque el cliente se acaba de conectar entonces
                //agregamos su ip al hilocliente para identificar el hilo
                setIp(peticion);
            }
        }
    }

    /**
     * Metodo para enviar a los clientes las ips de los demas clientes
     * conectaodos
     *
     * @param salidaTxt
     */
    private void enviarClientes(PrintStream salidaTxt) {
        String ips = "";
        //recorremos los clientes conectados
        for (int i = 0; i < servidor.getClientes().size(); i++) {
            //verificamos que no sea la ip del cliente de este hilo
            if (!servidor.getClientes().get(i).getIp().equals(this.getIp())) {
                //si es el último cliente no le agregamos "," a la cadena
                if (i == servidor.getClientes().size() - 1) {
                    ips += servidor.getClientes().get(i).getIp();
                    break;
                }
                ips += servidor.getClientes().get(i).getIp() + ",";
            }
        }
        //enviamos los clientes
        salidaTxt.println(ips);
    }

    /**
     * Método para buscar un cliente
     *
     * @param ip, ip del cliente a buscar
     * @return el HiloCliente
     */
    private HiloCLiente buscarHiloCLiente(String ip) {
        //recorermos los clientes conectados
        for (int i = 0; i < servidor.getClientes().size(); i++) {
            //verificamos si es el cliente que buscamos
            if (servidor.getClientes().get(i).getIp().equals(ip)) {
                return servidor.getClientes().get(i);
            }
        }
        return null;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Socket getCon() {
        return con;
    }

    public void setCon(Socket con) {
        this.con = con;
    }

    public HiloCLiente getReceptor() {
        return receptor;
    }

    public void setReceptor(HiloCLiente receptor) {
        this.receptor = receptor;
    }
}
