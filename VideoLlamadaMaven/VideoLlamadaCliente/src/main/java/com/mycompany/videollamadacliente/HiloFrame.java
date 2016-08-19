/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videollamadacliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import util.CameraUtil;

/**
 *
 * @author kvin2
 */
public class HiloFrame implements Runnable{
private Socket con;
private CameraUtil camara;
    
    public HiloFrame(Socket con) {
        this.con=con;
        camara=new CameraUtil();
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
               
                DatagramPacket pkt = new DatagramPacket("hola".getBytes(),"hola".getBytes().length,InetAddress.getByName("196.168.1.12"), 45000);
                
                udpsoc.send(pkt);
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
