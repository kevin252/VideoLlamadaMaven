/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.eam.videollamadacliente.cliente;

import com.google.common.primitives.Bytes;
import co.edu.eam.videollamadacliente.gui.Ventana;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

    private Ventana ventana;
    private boolean estado;

    public HiloReceptor(Ventana v) {
        this.ventana = v;
        this.estado = true;
    }
    
    @Override
    public void run() {
        try (DatagramSocket socketIn = new DatagramSocket(46000)) {
            while (estado) {
                byte[] buffer = new byte[15000];
                DatagramPacket pktIn = new DatagramPacket(buffer, buffer.length);//                        
                socketIn.receive(pktIn);
                ventana.setJLVideo(pktIn.getData());
            }
            ventana.setJLVideo(new byte[0]);
            ventana.repaint();
        } catch (SocketException ex) {
            Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }    
}
