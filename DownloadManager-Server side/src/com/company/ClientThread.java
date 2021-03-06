package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by Sapir on 29.03.2017.
 */
public class ClientThread extends Thread {

    private static final int MAX_FILE_LENGTH =1000 ;
    Socket clientSocket;
    UploadedFile uploadedFile;
    private InputStream inputStream;
    private OutputStream outputStream;
    private  OutputStream fileOutputStream;
    private FileInputStream fileInputStream;

    public static final int UPLOAD = 100;
    public static final int DOWNLOAD = 101;
    public static final int OKAY = 102;
    public static final int FAILURE = 103;

    boolean shouldDecrement = false;


    public ClientThread(UploadedFile uploadedFile, Socket clientSocket) {
        this.uploadedFile = uploadedFile;
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            int action=inputStream.read();
            switch (action) {
                case UPLOAD:
                    upload();
                    break;
                case DOWNLOAD:
                    download();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            handleCloseOfStreams();
            if(fileOutputStream!=null)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(fileInputStream!=null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(clientSocket!=null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (uploadedFile!=null&& uploadedFile.isLocked())
                    uploadedFile.unlock();
        }

    }

    // מעלים אל השרת
    private void upload() throws IOException {
        boolean lock=true;
        synchronized (uploadedFile) {
            if (!uploadedFile.isLocked() && uploadedFile.concurrentDownload.get() == 0) {  // כשיש הצלחה הוא משנה מצב
                uploadedFile.lock();  // נועלים לכבודך !
                lock = false;
            }
        }
        outputStream.write(lock ? FAILURE : OKAY);
        if(lock)
            return;
        int fileNameLength = inputStream.read();
        if(fileNameLength == -1) {
            uploadedFile.unlock();
            return;
        }
        byte[] fileNameBytes = new byte[fileNameLength];
        int actuallyRead = inputStream.read(fileNameBytes);
        if(actuallyRead != fileNameLength){
            uploadedFile.unlock();
            return;
        }
        fileOutputStream = new FileOutputStream(uploadedFile);
        byte[] buffer=new byte[1024]; // check if its size not large than 1M
        int counter=0;
        while((actuallyRead = inputStream.read(buffer)) != -1 && counter<MAX_FILE_LENGTH) {
            fileOutputStream.write(buffer,0,actuallyRead);
            counter++;
//            try {
//                Thread.sleep(15);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        fileOutputStream.close();
        fileOutputStream = null;
        uploadedFile.setFileNameBytes(fileNameBytes);
        uploadedFile.increaseVersion();
        uploadedFile.unlock();
    }



    private void download() throws IOException {
        if(uploadedFile.isLocked()) {
            outputStream.write(FAILURE);
            return;
        }else{
            outputStream.write(OKAY);
        }
        byte[] versionBytes = new byte[4];
        ByteBuffer.wrap(versionBytes).putInt(uploadedFile.getVersion());
        outputStream.write(versionBytes);
        int shouldSendFile = inputStream.read();
        if(shouldSendFile != OKAY) {
            return;
        }
        uploadedFile.concurrentDownload.incrementAndGet();
        shouldDecrement = true;
        outputStream.write(uploadedFile.getFileNameBytes().length);// ם הקובץ וגודלו ב- 2 השורות הלו
        outputStream.write(uploadedFile.getFileNameBytes());
        fileInputStream = new FileInputStream(uploadedFile);
        //TODO: reading/downloading lock
        byte[] fileSizeBytes=new byte[8];
        ByteBuffer.wrap(fileSizeBytes).putLong(uploadedFile.length());
        outputStream.write(fileSizeBytes);
        int oneByte;
        while((oneByte = fileInputStream.read()) != -1) {
            outputStream.write(oneByte);
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        fileInputStream.close();
        fileInputStream = null;
    }

    private void handleCloseOfStreams() {
        if (inputStream != null)
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (outputStream != null)
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}







/*public static final int DOWNLOAD = 100;
    public static final int UPLOAD = 101;
    public static final String PATH = "c:\\sapir";

    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private File file;

    public ClientThread(Socket clientSocket, File file) {
        this.clientSocket = clientSocket;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            int action = inputStream.read();
            switch (action) {
                case DOWNLOAD:
                    download();
                    break;
                case UPLOAD:
                    upload();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            handleCloseOfStreams();
        }
    }

    private void download() throws IOException {
        outputStream.write(file.getName().length());
        outputStream.write(file.getName().getBytes());

        InputStream inputStreamFile = null;
        try {
            inputStreamFile = new FileInputStream(file);
            int oneByte;
            while ((oneByte = inputStream.read()) != -1) {
                byte b = (byte) oneByte;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStreamFile != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    private void upload() throws IOException {
        inputStream = clientSocket.getInputStream();
        outputStream = clientSocket.getOutputStream();
        outputStream.write(UPLOAD);
        int actuallyRead = inputStream.read();  //size
        byte[] inputBytes = new byte[actuallyRead];// arr with this size
        actuallyRead = inputStream.read(inputBytes); // read the name
        String fileName = new String(inputBytes);
        File file = new File(PATH + "\\" + fileName + ".jpg");
        OutputStream outputStreamFile = new FileOutputStream(file);
        while ((actuallyRead = inputStream.read()) != -1) {
            outputStreamFile.write((byte) actuallyRead);
        }
    }



*/