/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videoservidor;

import co.edu.eam.videollamadaservidor.servidor.Servidor;


/**
 *
 * @author Heider
 */
public class VideoServidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Thread t = new Thread(new Servidor());
        t.start();
    }
    
}
