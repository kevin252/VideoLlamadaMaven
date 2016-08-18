/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.mycompany.videollamadacliente.Cliente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.CameraUtil;

/**
 *
 * @author kvin2
 */
public class Ventana extends javax.swing.JFrame implements Runnable, Observer {
    String ip;
    Cliente cli;
    DefaultTableModel modelo;

    /**
     * Creates new form Ventana
     */
    public Ventana() {
        try {
            initComponents();
            
            modelo = new DefaultTableModel();
            jTTabla.setModel(modelo);
            cli = new Cliente(this);
            modelo.addColumn("IP");
            cli.addObserver(this);
            Thread t = new Thread(cli);
            t.start();
            new Thread(this).start();
        } catch (IOException ex) {
            Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLVideo = new javax.swing.JLabel();
        jBActivar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTTabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jBActivar.setText("Activar");
        jBActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBActivarActionPerformed(evt);
            }
        });

        jTTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTTablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTTabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(370, 370, 370)
                .addComponent(jBActivar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLVideo, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                .addComponent(jBActivar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBActivarActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_jBActivarActionPerformed

    private void jTTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTTablaMouseClicked
        // TODO add your handling code here:
        ip="";
        int seleccion=jTTabla.getSelectedRow();
        ip="CON:"+String.valueOf(jTTabla.getValueAt(seleccion, 0));
    }//GEN-LAST:event_jTTablaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBActivar;
    private javax.swing.JLabel jLVideo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTTabla;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        CameraUtil cam = new CameraUtil();
        while (true) {
            byte[] data;
            
            try {
                
                data = cam.getDesktop();
                
                jLVideo.setIcon(new ImageIcon(data));
                jLVideo.setText("");
                
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void llenarTabla() {
        
    }
    
    @Override
    public void update(Observable o, Object arg) {
        LimpiarJTable();
        for (int i = 0; i < cli.getIps().size(); i++) {
            modelo.addRow(new Object[]{cli.getIps().get(i)});

        }
    }
    
    
    void LimpiarJTable(){
int a =modelo.getRowCount()-1;
System.out.println(""+a);
for(int i=a; i>=0;i--){
System.out.println("i"+i);
modelo.removeRow(i);
}
}
    
    public void setJLVideo(byte[] data){
       jLVideo.setIcon(new ImageIcon(data));
                jLVideo.setText(""); 
    }
    
   
}