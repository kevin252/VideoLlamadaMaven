/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videollamadacliente;

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

    private List<String> ips;

    public Cliente() {
        ips = new ArrayList<>();
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    
       public void notificar(){
        setChanged();
        notifyObservers();
        System.out.println("Notifico");
    }
    @Override
    public void run() {

        try {
            Socket soc = new Socket("localhost", 45000);
            InetAddress address = InetAddress.getLocalHost();
            PrintStream user = new PrintStream(soc.getOutputStream());
            user.println(address.getHostAddress());
                                        BufferedReader listaIps = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            while (true) {

               if (!listaIps.equals("")) {
                    ips.clear();
                    String aux=listaIps.readLine();
                   System.out.println(aux);
                    String[] ipes = listaIps.readLine().split(",");
                    for (int i = 0; i <ipes.length; i++) {
                        ips.add(ipes[i]);
                    }
                    notificar();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
}
