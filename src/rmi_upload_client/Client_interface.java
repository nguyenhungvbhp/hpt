/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_upload_client;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import jdk.nashorn.internal.ir.ContinueNode;
import rmi_download_server.FileServer;
import rmi_download_server.FileServerInt;
import javax.swing.SwingUtilities;

/**
 *
 * @author robot
 */
public class Client_interface extends javax.swing.JFrame {

    public static int numberThread = 0;
    public static final int MAX_THREAD = 4;
    /**
     * Creates new form Client_interface
     */
    private FileClientInt client;
    private FileServerInt server;
    private int state = -1; // 1 la upload, 2 la download
    private File server_path;
    private String client_path = "";
    private String UserName = "";
    private Thread threadObject;
    private UpDownload upload;
    private UpDownload download;

    public Client_interface(final String path, FileClientInt client, FileServerInt server, String userName) throws RemoteException, Exception {
        this.UserName = userName;
        this.client = client;
        this.server = server;
     
        this.server_path = server.getServerFile();
        this.client_path = path;
        this.state = -1;
        initComponents();
        initTime();
        startSync();
    }

    public void updateTable(String path, int x) {
        Vector col = new Vector();
        col.add("Loại");
        col.add("Tên");
        col.add("Kích thước");
        col.add("Ngày cập nhật");
        Vector data = new Vector();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        File f = new File(path);
        File[] allSubFiles = f.listFiles();
        if (allSubFiles == null) {
        } else {
            for (File file : allSubFiles) {
                Vector element = new Vector();
                if (file.isDirectory()) {
                    element.addElement("forder");
                    element.addElement(file.getName());
                    element.addElement(file.length());
                    element.addElement(sdf.format(file.lastModified()));
                } else {
                    element.addElement("file");
                    element.addElement(file.getName());
                    element.addElement(file.length());
                    element.addElement(sdf.format(file.lastModified()));
                }
                data.add(element);
            }
            if (x == 0) {
                TB_client.setModel(new DefaultTableModel(data, col));
            } else {
                TB_server.setModel(new DefaultTableModel(data, col));
            }
        }
    }

    public boolean deleteFile(File delFile) {
        if (delFile.isDirectory()) {
            if (delFile.list().length == 0) {
                delFile.delete();
                return true;
            } else {
                File[] allSubFiles = delFile.listFiles();
                for (File file : allSubFiles) {
                    if (file.isDirectory()) {
                        deleteFile(file);
                    } else {
                        file.delete();
                    }
                }
            }
        } else {
            delFile.delete();
            return true;
        }
        return false;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BT_clientUpLoad = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TB_client = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TB_server = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        BT_serverDownLoad = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BT_clientUpLoad.setText("Upload");
        BT_clientUpLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_clientUpLoadActionPerformed(evt);
            }
        });

        TB_client.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Loại", "Tên", "Ngày cập nhật", "Kích thước "
            }
        ));
        jScrollPane1.setViewportView(TB_client);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 255));
        jLabel1.setText("Client");

        TB_server.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Loại", "Tên", "Ngày cập nhật", "Kích thước"
            }
        ));
        jScrollPane2.setViewportView(TB_server);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setText("Server");

        BT_serverDownLoad.setText("Download");
        BT_serverDownLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_serverDownLoadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BT_clientUpLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addComponent(jLabel1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BT_serverDownLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(58, 58, 58))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BT_clientUpLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BT_serverDownLoad)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initTime() {
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable(server_path.getParent() + "\\" + server_path.getName(), 1);
                updateTable(client_path, 0);
            }
        });
        timer.start();
    }
    private void BT_clientUpLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_clientUpLoadActionPerformed
       
        if(numberThread > MAX_THREAD) {
            JOptionPane.showMessageDialog(null, "Số lượng download và upload quá hạn. Vui lòng chờ!");
        }
        
        numberThread++;
        int row = TB_client.getSelectedRow();

        if (row > -1) {
            if (BT_clientUpLoad.getText().equalsIgnoreCase("Upload")) {
                try {
                    // Cho bat dau upload
                    File clientfile = new File(client_path + "/" + TB_client.getValueAt(row, 1));
                    File serverfile = server.getServerFile();
                    upload = new UpDownload(clientfile, serverfile, 1);
                    new task_download(upload).setVisible(true);

                } catch (RemoteException ex) {
                    Logger.getLogger(Client_interface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Client_interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn file nào để Upload");
        }
    }//GEN-LAST:event_BT_clientUpLoadActionPerformed

    private void BT_serverDownLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_serverDownLoadActionPerformed
        
        if(numberThread > MAX_THREAD) {
            JOptionPane.showMessageDialog(null, "Số lượng download và upload quá hạn. Vui lòng chờ!");
        }
        
        numberThread++;
        int row = TB_server.getSelectedRow();
        if (row > -1) {
            if (BT_serverDownLoad.getText().equalsIgnoreCase("Download")) {
                try {
                    File serverfile = new File(server_path + "/" + TB_server.getValueAt(row, 1));
                    File clientfile = new File(client_path);
                    upload = new UpDownload(serverfile, clientfile, 2);
                    new task_download(upload).setVisible(true);
                    upload.start();
                } catch (Exception ex) {
                    Logger.getLogger(Client_interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn file nào để Download");
        }
    }//GEN-LAST:event_BT_serverDownLoadActionPerformed

    public String getPath(String currentpath) {
        int endIndex = 0;
        for (int i = currentpath.length() - 1; i >= 0; i--) {
            if ((int) currentpath.charAt(i) == 92) {
                endIndex = i;
                break;
            }
        }
        String checkpath = currentpath.substring(0, endIndex);
        return checkpath;
    }

    private void startSync() throws RemoteException, Exception {
        server.synchronous(client);
    }

//    private void stopSync() throws RemoteException {
//        updownload.stopSync();
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_clientUpLoad;
    private javax.swing.JButton BT_serverDownLoad;
    private javax.swing.JTable TB_client;
    private javax.swing.JTable TB_server;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
