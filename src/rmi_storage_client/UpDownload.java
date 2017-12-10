/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi_storage_client;

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
import rmi_storage_server.FileServerInt;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class UpDownload {

    private File clientFile;
    private File serverFile;
    private FileServerInt server;
    private FileClientInt client;
    private String Username = "";
    private int state; // 1 la upload, 2 la download
    private boolean check = true; // false la pause, true la tiep tuc down
    private boolean wait = true;

    public UpDownload() {

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

//    @Override
//    public void run() {
//        try {
//            while (true) {
//                upDown(serverFile, clientFile);
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(UpDownload.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
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
//        String default_server = destination.getParent() + "\\" + destination.getName();
//        System.out.println(default_server);
//        if (source.isDirectory()) {
//            destination = new File(destination.getParent() + "\\" + destination.getName() + "\\" + source.getName());
//            if (!destination.exists()) {
//                if (!destination.mkdirs()) {
//                    throw new IOException("Could not create path "
//                            + destination);
//
//                }
//            } else if (!destination.isDirectory()) {
//                throw new IOException(
//                        "Source and Destination not of the same type:"
//                        + source.getCanonicalPath() + " , "
//                        + destination.getCanonicalPath());
//            }
//            String[] sources = source.list();
//            Set<String> srcNames = new HashSet<String>(Arrays.asList(sources));
//            String[] dests = destination.list();
//
////            System.out.println(srcNames);
////            System.out.println(dests);
////            for (String fileName : dests) {
////                if (!srcNames.contains(fileName)) {
////                    //System.out.println("file "+ fileName+ " không có");
////                    delete(new File(destination, fileName));
////                    client.setSyncState("Người dùng " + this.Username + " : " + " " + fileName + " đã xóa và được cập nhật trên server");
////                    server.showSyncState(client);
////                }
////            }
//
//            for (String fileName : sources) {
//                File srcFile = new File(source, fileName);
//                File destFile = new File(destination, fileName);
//                System.out.println(srcFile);
//                System.out.println(destFile);
//                uploadFile(srcFile, destFile);
//            }
//        } else {
//            System.out.println(source.getName());
//            System.out.println(destination.getParent());
//            destination = new File(destination.getParent() + "\\" + destination.getName() + "\\" + source.getName());
//            //            System.out.println(destination);
//            if (destination.exists() && destination.isDirectory()) {
//                delete(destination);
//                client.setSyncState("Người dùng " + this.Username + " : " + "thư mục " + destination.getName() + " đã được xóa đi ");
//                server.showSyncState(client);
//            }
//            if (destination.exists() && source.exists()) {
//                long sts = source.lastModified();
//                long dts = destination.lastModified();
//                if (sts != dts || source.length() != destination.length()) {
//                    copyFile(source, destination);
//                    client.setSyncState("Người dùng " + this.Username + " : " + "file " + source.getName() + " đã được upload");
//                    server.showSyncState(client);
//                }
//            } else if (destination.createNewFile() && source.exists()) {
//                copyFile(source, destination);
//                client.setSyncState("Người dùng " + this.Username + " : " + "file " + source.getName() + " đã được upload");
//                server.showSyncState(client);
//            } else {
//                client.setSyncState("Người dùng " + this.Username + " : " + "file " + source.getName() + " không tạo được trên file đích");
//                server.showSyncState(client);
//            }
//        }
    }
    
    private void splitFile(File srcFile, File desFile) {
        FileInputStream fis;
        FileOutputStream fos;
        int numFile = 4;
        int sizeSrcFile = (int) srcFile.length();
        int sizeEachFile = (int) srcFile.length() / numFile;
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
        list.add(new File(srcFile.getAbsoluteFile()+ ".part1"));
        list.add(new File(srcFile.getAbsoluteFile()+ ".part2"));
        list.add(new File(srcFile.getAbsoluteFile()+ ".part3"));
        list.add(new File(srcFile.getAbsoluteFile()+ ".part4"));
        try {
            fos = new FileOutputStream(srcFile, true);
            for (File file : list) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                bytesRead = fis.read(fileBytes, 0,(int)  file.length());
                fos.write(fileBytes);
                fos.flush();
                fileBytes = null;
                fis.close();
                fis = null;
//                file.delete();
            }
            fos.close();
            fos = null;
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void copyFile(File srcFile, File destFile) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            if (srcFile.isFile()) {
                if (state == 1) {
                    is = server.getFileInputStream(srcFile);
                    os = new FileOutputStream(destFile, false);
                } else if (state == 2) {
                    is = new FileInputStream(srcFile);
                    os = server.getFileOutputStream(destFile);
                }

                splitFile(srcFile, destFile);
                mergeFile(destFile);
                System.out.println("Da tai thanh cong");
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
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }

        boolean successTimestampOp = destFile.setLastModified(srcFile.lastModified());
        if (!successTimestampOp) {
//            JOptionPane.showMessageDialog(null, "Lỗi trong quá trình sửa đổi ngày cập nhật file :"
//                    + destFile);
            client.setSyncState("Người dùng " + this.Username + " : " + "Lỗi trong quá trình sửa đổi ngày cập nhật file : " + destFile);
            server.showSyncState(client);
        }
    }

    public void stopSync() throws RemoteException {
        DateTimeUtils.setCurrentMillisSystem();
        if (state == 0) {
            client.setSyncState("Người dùng " + this.Username + " : đã dừng Upload");
        }
        server.showSyncState(client);
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
