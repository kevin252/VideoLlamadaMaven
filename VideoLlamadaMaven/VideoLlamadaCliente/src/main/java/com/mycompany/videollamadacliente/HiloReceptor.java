/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videollamadacliente;

import com.google.common.primitives.Bytes;
import gui.Ventana;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author kvin2
 */
public class HiloReceptor implements Runnable {

    private Socket con;
    private Ventana ventana;
    private int tama;
           

    public HiloReceptor(Ventana v, Socket con) {
        this.ventana = v;
        this.con = con;
        this.tama=0;
    }

    public void setTama(int tama) {
        this.tama = tama;
    }
    
    

    @Override
    public void run() {
        try (DatagramSocket socketIn = new DatagramSocket(con.getLocalSocketAddress())) {
            while (true) {
                
                if(tama>0){
                byte[] buffer = new byte[1024];
                //paquete dondre recibiremos 
                
                    
                    int cont = 1;
                    byte[] img = new byte[tama];
                    while (1024 * cont <= tama) {
                        DatagramPacket pktIn = new DatagramPacket(buffer, buffer.length);
                        img = Bytes.concat(img, pktIn.getData());
                        socketIn.receive(pktIn);
                        System.out.println(pktIn.getData().toString() + " " + pktIn.getData().length);
                        cont++;
                    }
                    ventana.setJLVideo(img);
                    tama=0;
                }
            
            }
        } catch (SocketException ex) {
            Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
