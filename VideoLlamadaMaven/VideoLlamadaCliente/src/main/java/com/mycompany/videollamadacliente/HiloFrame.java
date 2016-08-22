/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videollamadacliente;

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
import util.CameraUtil;

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
        while (true) {
            byte[] data;

            try {

                data = camara.getDesktop();
                try {
//crear el paquete
                    try ( // TODO code application logic here
                            //crear el DatagramSocket
                            DatagramSocket udpsoc = new DatagramSocket()) {
                        
                        int cont = 1;
                        
                        //abrir flujo de salida
                        PrintStream buffData = new PrintStream(con.getOutputStream());
                        //enviar el tamaño
                        buffData.println("TAM:" + data.length);
                        //segmentar el arreglo de bytes
                        while (1024 * cont <= data.length) {
                            byte[] corte = Arrays.copyOfRange(data, (cont - 1)
                                    * 1024, cont * 1024);
                            //creamos el DatagramPacket
                            DatagramPacket pkt = new DatagramPacket(corte, 1024, InetAddress.getByName(ip), 47000);
                            //enviaamos los bytes
                            udpsoc.send(pkt);
                            cont++;
                        }
                    }
                } catch (SocketException ex) {
                    Logger.getLogger(HiloFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(HiloFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
