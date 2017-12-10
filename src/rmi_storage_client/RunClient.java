/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_storage_client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.Naming;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.joda.time.DateTimeUtils;
import rmi_storage_server.FileServer;
import rmi_storage_server.FileServerInt;

/**
 *
 * @author robot
 */
public class RunClient extends javax.swing.JFrame {

    /**
     * Creates new form RunClient
     */
    private JFileChooser chooser;
    private FileServerInt server;
    private FileClientInt client;

    public RunClient() {
        initComponents();
    }

    private void connectToServer() throws Exception {
        System.setProperty("java.security.policy","E:\\Subject\\Ky1Nam4\\Hephantan\\BTL_HPT\\rmi_storage_client\\security.policy");
        System.getProperty("java.rmi.server.hostname", getIp());
        String url = "rmi://" + TFip.getText() + ":6969" + "/server";
        server = (FileServerInt) Naming.lookup(url);
        System.out.println("Connected");
        client = new FileClient(InetAddress.getLocalHost());
        clockSync();
    }
    
    // lay dia chi ip cua may ipv4 
    public String getIp() {
        String ipAddress = null;
        Enumeration<NetworkInterface> net = null;
        try {
            net = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (net.hasMoreElements()) {
            NetworkInterface element = net.nextElement();
            Enumeration<InetAddress> addresses = element.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip instanceof Inet4Address) {
                    if (ip.isSiteLocalAddress()) {
                        ipAddress = ip.getHostAddress();
                    }
                }
            }
        }
        return ipAddress;
    }

    private void clockSync() throws Exception {
        String serverIP = TFip.getText();

        // Send request
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(serverIP);
        byte[] buf = new NtpMessage().toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address,
                123);

        // Set the transmit timestamp *just* before sending the packet
        // ToDo: Does this actually improve performance or not?
        NtpMessage.encodeTimestamp(packet.getData(), 40,
                (System.currentTimeMillis() / 1000.0) + 2208988800.0);

        socket.send(packet);

        // Get response
        System.out.println("NTP request sent, waiting for response...\n");
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        // Immediately record the incoming timestamp
        double destinationTimestamp = (System.currentTimeMillis() / 1000.0) + 2208988800.0;

        // Process response
        NtpMessage msg = new NtpMessage(packet.getData());

        // Corrected, according to RFC2030 errata
        double roundTripDelay = (destinationTimestamp - msg.originateTimestamp)
                - (msg.transmitTimestamp - msg.receiveTimestamp);

        double localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2;

        // Display response
        System.out.println("NTP server: " + serverIP);
        System.out.println(msg.toString());

        System.out.println("Dest. timestamp:     "
                + NtpMessage.timestampToString(destinationTimestamp));

        System.out.println("Round-trip delay: "
                + new DecimalFormat("0.00").format(roundTripDelay * 1000)
                + " ms");

        System.out.println("Local clock offset: "
                + new DecimalFormat("0.00").format(localClockOffset * 1000)
                + " ms");
        System.out.println("Current time " + DateTimeUtils.currentTimeMillis());
        DateTimeUtils.setCurrentMillisOffset((long) (localClockOffset * 1000));
        System.out.println("Current time " + DateTimeUtils.currentTimeMillis());

        socket.close();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        TFip = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        TFfordershare = new javax.swing.JTextField();
        BTchose = new javax.swing.JButton();
        BTconnect = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        TFname = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("  IP/localhost");

        jLabel2.setText("  Forder Share");

        BTchose.setText("Choose Folder");
        BTchose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTchoseActionPerformed(evt);
            }
        });

        BTconnect.setText("Connect");
        BTconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTconnectActionPerformed(evt);
            }
        });

        jLabel3.setText(" Name");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BTconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFip, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TFfordershare, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BTchose)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {TFfordershare, TFip, TFname});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TFname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TFfordershare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(BTchose, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BTconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {TFfordershare, TFip, TFname, jLabel1, jLabel2, jLabel3});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTchoseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTchoseActionPerformed
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            TFfordershare.setText(""+ chooser.getSelectedFile());
        }else {
            System.out.println("No Selection");
        }
    }//GEN-LAST:event_BTchoseActionPerformed

    private void BTconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTconnectActionPerformed
        if(TFname.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Viết tên của mình vào để hệ thống xác thực");
            return;
        }
        if(TFip.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Viết địa chỉ IP để kết nối");
        }else if(TFfordershare.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Chọn thư mục để Upload");
        }else {
            try {
                connectToServer();
                new Client_interface(chooser.getSelectedFile().getAbsolutePath(), client, server, TFname.getText()).setVisible(true);
                this.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(RunClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_BTconnectActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RunClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RunClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RunClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RunClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RunClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTchose;
    private javax.swing.JButton BTconnect;
    private javax.swing.JTextField TFfordershare;
    private javax.swing.JTextField TFip;
    private javax.swing.JTextField TFname;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}