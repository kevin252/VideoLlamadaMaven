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

    public HiloFrame(Socket con) {
        this.con = con;
        camara = new CameraUtil();
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
                            DatagramSocket udpsoc = new DatagramSocket()) {
                        //crear el paquete
                        int cont = 1;
                        PrintStream buffData= new PrintStream(con.getOutputStream());
                        buffData.println("TAM:"+data.length);
                        while (1024 * cont <= data.length) {
                            byte[] corte = Arrays.copyOfRange(data, (cont - 1)
                                    * 1024, cont * 1024);

                            DatagramPacket pkt = new DatagramPacket(corte, 1024, con.getRemoteSocketAddress());

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
