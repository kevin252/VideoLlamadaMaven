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

    private Socket con;
    private Ventana ventana;
    private int tama;

    public HiloReceptor(Ventana v, Socket con) {
        this.ventana = v;
        this.con = con;
        this.tama = 0;
    }

    public void setTama(int tama) {
        this.tama = tama;
    }

    @Override
    public void run() {
        try (DatagramSocket socketIn = new DatagramSocket(46000)) {
            
            while (true) {
                System.out.println("Estamos en receptor");
//                if (tama > 0) {
                    byte[] buffer = new byte[15000];
                    //paquete dondre recibiremos 

                    int cont = 1;
                    byte[] img = new byte[tama];
                    //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

//                    while (1024 * cont <= tama) {
                        DatagramPacket pktIn = new DatagramPacket(buffer, buffer.length);
//                        
                       socketIn.receive(pktIn);
//                        outputStream.write(pktIn.getData());
//                        System.out.println(pktIn.getData().toString() + " " + pktIn.getData().length);
//                        cont++;
//                    }
                   // img=outputStream.toByteArray();
                    ventana.setJLVideo(pktIn.getData());
                    tama = 0;
//                }

            }
        } catch (SocketException ex) {
            Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloReceptor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
