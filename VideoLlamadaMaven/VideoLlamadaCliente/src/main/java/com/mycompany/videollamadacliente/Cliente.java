/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videollamadacliente;

import gui.Ventana;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kvin2
 */
public class Cliente extends Observable implements Runnable {

    private Ventana v;
    private HiloReceptor hilo;
    Socket soc;
    private List<String> ips;

    public Cliente(Ventana v) throws IOException {
        ips = new ArrayList<>();
        soc = new Socket("192.168.1.66", 45000);
        this.v = v;
        hilo= new HiloReceptor(v, soc);
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public void notificar() {
        setChanged();
        notifyObservers();
        System.out.println("Notifico");
    }

    @Override
    public void run() {

        try {

            InetAddress address = InetAddress.getLocalHost();
            PrintStream user = new PrintStream(soc.getOutputStream());
            user.println(address.getHostAddress());
            BufferedReader listaIps = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            while (true) {

                String aux = listaIps.readLine();
                if (!aux.equals("")) {
                    ips.clear();

                    System.out.println(aux);

                    String[] ipes = aux.split(",");
                    if (ipes[0].equals("CON")) {
                        v.setIP(ipes[1]);
                        v.getColgar().setVisible(true);
                        v.getContestar().setVisible(true);
                        Thread.sleep(1000);
                        

                    } else if (ipes[0].equals("OK")) {
                        HiloFrame hf = new HiloFrame(soc,v.getIP());
                        new Thread(hf).start();
                        new Thread(hilo).start();
                    } else if (ipes[0].equals("NO")) {
                        v.notificacion("No contesto");

                    }else if(ipes[0].equals("TAM")){
                        hilo.setTama(Integer.parseInt(ipes[1]));
                    }else {

                        System.out.println(ipes[0]);
                        for (int i = 0; i < ipes.length; i++) {
                            ips.add(ipes[i]);

                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        notificar();
                        user.println("");
                    }

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llamar(String mensaje) throws IOException {
        PrintStream llamar = new PrintStream(soc.getOutputStream());
        llamar.println(mensaje);

    }

    public void contestar(String ip) throws IOException {
        PrintStream llamar = new PrintStream(soc.getOutputStream());
        llamar.println("OK:"+ip);

        HiloFrame hf = new HiloFrame(soc,ip);
        new Thread(hf).start();
        new Thread(hilo).start();
    }

    public void rechazarLlamada() throws IOException {
        PrintStream rechazar = new PrintStream(soc.getOutputStream());
        rechazar.println("NO");
    }
}
