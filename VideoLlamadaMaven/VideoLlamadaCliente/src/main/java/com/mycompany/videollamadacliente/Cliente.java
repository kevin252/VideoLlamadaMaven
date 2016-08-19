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
    Socket soc;
    private List<String> ips;

    public Cliente(Ventana v) throws IOException {
        ips = new ArrayList<>();
        soc = new Socket("localhost", 45000);
        this.v = v;
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
                        v.getColgar().setVisible(true);
                        v.getContestar().setVisible(true);
                        Thread.sleep(1000);

                    } else if (ipes[0].equals("OK")) {
                        HiloFrame hf = new HiloFrame(soc);
                        new Thread(hf).start();

                        HiloReceptor hr = new HiloReceptor(v);
                        new Thread(hr).start();
                    } else if (ipes[0].equals("NO")) {
                        v.notificacion("No contesto");

                    } else {

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

    public void contestar() throws IOException {
        PrintStream llamar = new PrintStream(soc.getOutputStream());
        llamar.println("OK");

        HiloFrame hf = new HiloFrame(soc);
        new Thread(hf).start();

        HiloReceptor hr = new HiloReceptor(v);
        new Thread(hr).start();
    }

    public void rechazarLlamada() throws IOException {
        PrintStream rechazar = new PrintStream(soc.getOutputStream());
        rechazar.println("NO");
    }
}
