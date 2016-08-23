/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.eam.videollamadacliente.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import co.edu.eam.videollamadacliente.util.CameraUtil;

/**
 *
 * @author kvin2
 */
public class HiloFrame implements Runnable {

    private Socket con;
    private CameraUtil camara;
    private String ip;

    public HiloFrame(Socket con, String ip) {
        this.con = con;
        camara = new CameraUtil();
        this.ip = ip;
    }

    @Override
    public void run() {
        camara.initCamera();
        while (true) {
            byte[] data;
            try {                
                data = camara.getFrame();
                
                try {
//crear el paquete
                    try ( // TODO code application logic here
                            //crear el DatagramSocket
                            DatagramSocket udpsoc = new DatagramSocket()) {

//                        int cont = 1;

                        //abrir flujo de salida
//                        PrintStream buffData = new PrintStream(con.getOutputStream());
//                        //enviar el tamaño
//                        buffData.println("TAM:" + data.length);
                        byte[] jh = new byte[15000];
                        //segmentar el arreglo de bytes
//                        while (1024 * cont <= data.length) {
//                            byte[] corte = Arrays.copyOfRange(data, (cont - 1)
//                                    * 1024, cont * 1024);
                        //creamos el DatagramPacket
                        DatagramPacket pkt = new DatagramPacket(data, data.length, InetAddress.getByName(ip), 46000);
                        //enviaamos los bytes
                        udpsoc.send(pkt);
//                            cont++;
//                        }
//                     DatagramPacket   pekt= new DatagramPacket(jh, jh.length);
//                        udpsoc.receive(pekt);
                    }
                } catch (SocketException ex) {
                    Logger.getLogger(HiloFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(HiloFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
