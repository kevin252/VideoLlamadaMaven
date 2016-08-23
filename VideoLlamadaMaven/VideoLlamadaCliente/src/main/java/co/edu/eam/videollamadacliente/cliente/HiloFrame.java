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

    private CameraUtil camara;
    private String ip;
    private boolean estado;

    public HiloFrame(Socket con, String ip) {
        camara = new CameraUtil();
        this.ip = ip;
        estado = true;
    }

    @Override
    public void run() {
        System.out.println(ip);
        camara.initCamera();
        while (estado) {
            byte[] data;
            try {                
                data = camara.getFrame();                
                try {
                    try (DatagramSocket udpsoc = new DatagramSocket()) {                      
                        DatagramPacket pkt = new DatagramPacket(data, data.length, InetAddress.getByName(ip), 46000);                        
                        udpsoc.send(pkt);//                      
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

    public void setEstado(boolean estado) {
        this.estado = estado;
    }   

}
