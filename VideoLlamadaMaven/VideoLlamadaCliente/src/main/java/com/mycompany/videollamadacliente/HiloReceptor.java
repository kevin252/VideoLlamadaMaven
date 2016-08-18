/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videollamadacliente;

import gui.Ventana;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author kvin2
 */
public class HiloReceptor implements Runnable{
private Ventana ventana;
    public HiloReceptor(Ventana v) {
        this.ventana=v;
    }

    
    
    @Override
    public void run() {
        try (DatagramSocket socketIn = new DatagramSocket(45000)) {
                        byte[] buffer = new byte[10000];
                        //paquete dondre recibiremos 
                        DatagramPacket pktIn = new DatagramPacket(buffer, buffer.length);
                        //recibimos paquete
                       socketIn.receive(pktIn);
                       byte[] bites = pktIn.getData();

                        ventana.setJLVideo(bites);
               
                        
    } catch (SocketException ex) {
        Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
    }
    
}
}
