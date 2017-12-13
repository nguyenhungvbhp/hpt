/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_upload_client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTimeUtils;
import rmi_download_server.FileServerInt;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class UpDownload extends Thread {

    private File clientFile;
    private File serverFile;
    private FileServerInt server;
    private FileClientInt client;
    private String Username = "";
    private int state; // 1 la upload, 2 la download
    private boolean check = false; // false la pause, true la tiep tuc down
    private boolean wait = true;
    private static int countTotalFile;
    private Thread threadObject;
    private Object lock = new Object();
    private File source;
    private File destination;

    public UpDownload() {

    }

    public UpDownload(File source, File destination, int state) {
        this.source = source;
        this.destination = destination;
        this.state = state;
    }

    public UpDownload(FileClientInt client, FileServerInt server,
            int state, String userName) {
        this.client = client;
        this.server = server;
        this.clientFile = clientFile;
        this.serverFile = serverFile;
        this.Username = userName;
        this.state = state;
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String sts = sdf.format(source.lastModified());
        String dts = sdf.format(destination.lastModified());
        System.out.println(sts);
        System.out.println(dts);
        destination = new File(destination.getParent() + "\\" + destination.getName() + "\\" + source.getName());
        //

//        if (source.isFile()) {
//            if (state == 1) {
//                System.out.println("begin running");
//                splitFile(source, destination);
//                mergeFile(destination);
//                System.out.println("Da upload thanh cong");
//            } else if (state == 2) {
//                splitFile(source, destination);
//                mergeFile(destination);
//                System.out.println("Da tai thanh cong");
//            }
//
////                splitFile(srcFile, destFile);
////                mergeFile(destFile);
////                System.out.println("Da tai thanh cong");
//        }
//        if (source.isDirectory()) {
//            // Neu thu muc dich khong ton tai thi tao ra thu muc moi
//            if (!destination.exists()) {
//                destination.mkdirs();
//            }
//            File[] listFile = source.listFiles();
//            for (File f : listFile) {
////                    System.out.println(f.getAbsolutePath());
////                    System.out.println(destFile);
//                copyFile(new File(f.getAbsolutePath()), new File(destFile + "\\" + f.getName()));
//            }
//        }
        try {
            FileInputStream fis;
            FileOutputStream fos;
            int sizeSrcFile = (int) source.length();
            int sizeEachFile = 1 * 1024 * 256;
            int nChunks = 0, read = 0, readLength = sizeEachFile;
            byte[] byteChunkPart;

            fis = new FileInputStream(source);
            while (sizeSrcFile > 0) {
//                    Thread.sleep(300);
                if (sizeSrcFile <= sizeEachFile) {
                    readLength = sizeSrcFile;
                }
                byteChunkPart = new byte[readLength];
                read = fis.read(byteChunkPart, 0, (int) readLength);
                sizeSrcFile -= read;
                nChunks++;
                countTotalFile = nChunks;
                System.out.println(countTotalFile);
                fos = new FileOutputStream(new File(destination.getAbsoluteFile() + ".part") + Integer.toString(nChunks));
                fos.write(byteChunkPart);
                fos.flush();
                fos.close();
                byteChunkPart = null;
                fos = null;
                synchronized (this) {
                    while (isCheck()) {
                        wait();
                    }
                }

            }
            fis.close();

        } catch (Exception ex) {
            Logger.getLogger(UpDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
        mergeFile(destination);
        boolean successTimestampOp = destination.setLastModified(source.lastModified());
    }

    public void delete(File file) throws RemoteException {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                delete(subFile);
            }
        }
        if (file.exists()) {
            if (!file.delete()) {
                //JOptionPane.showMessageDialog(null ,"Không thể xóa file : " + file);
                client.setSyncState("Người dùng " + this.Username + " : " + "Không thể xóa file " + file.getName());
                server.showSyncState(client);
            } else {
                client.setSyncState("Người dùng " + this.Username + " : " + "Xóa file thành công " + file.getName());
                server.showSyncState(client);
            }
        }
    }

    public void upDownLoad(File source, File destination) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String sts = sdf.format(source.lastModified());
        String dts = sdf.format(destination.lastModified());
        System.out.println(sts);
        System.out.println(dts);
        destination = new File(destination.getParent() + "\\" + destination.getName() + "\\" + source.getName());
        copyFile(source, destination);
    }

    private void splitFile(File srcFile, File desFile) {
        FileInputStream fis;
        FileOutputStream fos;
        int sizeSrcFile = (int) srcFile.length();
        int sizeEachFile = 16 * 1024 * 1024;
        int nChunks = 0, read = 0, readLength = sizeEachFile;
        byte[] byteChunkPart;
        try {
            fis = new FileInputStream(srcFile);
            while (sizeSrcFile > 0) {
                if (sizeSrcFile <= sizeEachFile) {
                    readLength = sizeSrcFile;
                }
                byteChunkPart = new byte[readLength];
                read = fis.read(byteChunkPart, 0, (int) readLength);
                sizeSrcFile -= read;
                nChunks++;
                countTotalFile = nChunks;
                System.out.println(countTotalFile);
                fos = new FileOutputStream(new File(desFile.getAbsoluteFile() + ".part") + Integer.toString(nChunks));
                fos.write(byteChunkPart);
                fos.flush();
                fos.close();
                byteChunkPart = null;
                fos = null;
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void mergeFile(File srcFile) {
        FileOutputStream fos;
        FileInputStream fis;
        byte[] fileBytes;
        int bytesRead = 0;
        List<File> list = new ArrayList<File>();
        System.out.println(countTotalFile);
        for (int i = 1; i <= countTotalFile; i++) {
            list.add(new File(srcFile.getAbsoluteFile() + ".part" + i));
        }
        try {
            fos = new FileOutputStream(srcFile, true);
            for (File file : list) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                bytesRead = fis.read(fileBytes, 0, (int) file.length());
                fos.write(fileBytes);
                fos.flush();
                fileBytes = null;
                fis.close();
                fis = null;
                file.delete();
            }
            fos.close();
            fos = null;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void copyFile(File srcFile, File destFile) throws Exception {

        if (srcFile.isFile()) {
            if (state == 1) {
                System.out.println("begin running");
                splitFile(srcFile, destFile);
                mergeFile(destFile);
                System.out.println("Da upload thanh cong");
            } else if (state == 2) {
                splitFile(srcFile, destFile);
                mergeFile(destFile);
                System.out.println("Da tai thanh cong");
            }

        }
        if (srcFile.isDirectory()) {
            // Neu thu muc dich khong ton tai thi tao ra thu muc moi
            if (!destFile.exists()) {
                destFile.mkdirs();
            }
            File[] listFile = srcFile.listFiles();
            for (File f : listFile) {
//                    System.out.println(f.getAbsolutePath());
//                    System.out.println(destFile);
                copyFile(new File(f.getAbsolutePath()), new File(destFile + "\\" + f.getName()));
            }
        }

//        boolean successTimestampOp = destFile.setLastModified(srcFile.lastModified());
//        if (!successTimestampOp) {
////            JOptionPane.showMessageDialog(null, "Lỗi trong quá trình sửa đổi ngày cập nhật file :"
////                    + destFile);
//            client.setSyncState("Người dùng " + this.Username + " : " + "Lỗi trong quá trình sửa đổi ngày cập nhật file : " + destFile);
//            server.showSyncState(client);
//        }
    }

//    public void stopSync() throws RemoteException {
//        DateTimeUtils.setCurrentMillisSystem();
//        if (state == 0) {
//            client.setSyncState("Người dùng " + this.Username + " : đã dừng Upload");
//        }
//        server.showSyncState(client);
//    }
    public boolean isCheck() {
        return check;
    }

    void pause() {
        check = true;
    }

    synchronized void resumeThread() {
        check = false;
        notify();
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getstate() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
