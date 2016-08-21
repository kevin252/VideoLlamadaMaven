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
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloCLiente implements Runnable {

    private Socket con;
    private String ip;
    private Servidor servidor;
    private HiloCLiente receptor;
    private PrintStream salidaTxt;
    private BufferedReader entradaTxt;
    private int tamanoImg;

    public HiloCLiente(Socket con, Servidor servidor) {
        super();
        this.con = con;
        this.servidor = servidor;
        this.receptor = null;
        this.tamanoImg = 0;
    }

    @Override
    public void run() {
        try {
            //conexion sea larga...
            //abrir flujos....
            System.out.println("CONECTO");
            salidaTxt = new PrintStream(this.con.getOutputStream());
            entradaTxt = new BufferedReader(new InputStreamReader(this.con.getInputStream()));
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
     *
     * @throws SocketException
     * @throws IOException
     */
    private void validarVideo() throws SocketException, IOException {
        if (receptor != null && tamanoImg != 0) {
            //abrimos servidor
            try (DatagramSocket socket = new DatagramSocket(con.getLocalSocketAddress())) {
                byte[] buffer = new byte[1024];
                int cont = 1;
                while (1024 * cont <= tamanoImg) {
                    DatagramPacket pktIn = new DatagramPacket(buffer, buffer.length);
                    socket.receive(pktIn);
                    byte[] cortes = pktIn.getData();
                    System.out.println(cortes.toString()+" "+cortes.length);
                    //abrimos socket para enviar packete al cliente recpetor
                    //creamos paquete a enviar al receptor
                    DatagramPacket pktOut = new DatagramPacket(cortes, cortes.length,
                            receptor.getCon().getLocalSocketAddress());
                    //enviamos paquete
                    socket.send(pktOut);
                    cont++;
                }
                this.tamanoImg = 0;
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
                HiloCLiente aux = buscarHiloCLiente(p[1]);
                //le solicitamos la conexión
                aux.notificar("CON," + ip);
                //verificamos que la respuesta sea ok                
            } else if (p[0].equals("OK")) {//si es ok, contesto una llamada 
                //añadimos los receptores a cada cliente
                receptor = buscarHiloCLiente(p[1]);
                receptor.setReceptor(this);
            } else if (p[0].equals("NO")) {
                //Si responde no, notificamos que el clientre rechazo la llamada
                HiloCLiente aux = buscarHiloCLiente(p[1]);
                aux.notificar(p[0]);
            } else if (p[0].endsWith("TAM")){      
                tamanoImg = Integer.parseInt(p[1]);
                receptor.notificar("TAM," + ip);
            } else {
                //si no, es porque el cliente se acaba de conectar entonces
                //agregamos su ip al hilocliente para identificar el hilo
                ip = peticion;
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
                if (i == servidor.getClientes().size() - 1
                        || servidor.getClientes().get(i + 1).getIp().equals(this.ip)) {
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

    public void notificar(String msj) {
        salidaTxt.println(msj);
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

    public PrintStream getSalidaTxt() {
        return salidaTxt;
    }

    public BufferedReader getEntradaTxt() {
        return entradaTxt;
    }
}
