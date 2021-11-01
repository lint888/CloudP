package de.tum.i13.operations;

import de.tum.i13.shared.Constants;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class operationDetails {
    private static final Logger logger= Logger.getLogger(operationDetails.class.getName());
    private Socket S;
    private InputStream I;
    private OutputStream O;

    public void askforHelp(String Commands){
        String Helps=null;
        switch(Commands){
            case "send":{
                System.out.println("Syntax: <send> <text message> \n" + "Will send the given message to the currently connected server");
                break;
            }
            case "connect":{
                System.out.println("Syntax: <connect> <IP Adress> <port Number> \n" + "will try to connect to the given server");
            }

        }


    }
    public int creatConnection(String IP, String portNumber){
        int Success=0;

        int realportNumber=Integer.parseInt(portNumber);

        try{
            S = new Socket(IP,realportNumber);
            I=S.getInputStream();
            O=S.getOutputStream();
            Success =1;
            logger.fine("Connection has been created");
            System.out.println("Connection successfully created");
        }catch(IOException e){
            logger.warning("Connection not successful");
            System.out.println("Connection is not successful");
            Success = -1;
        }
        return Success;
    }
    public void disconnect(){
        try{
            S.close();

        }catch(IOException e) {
            logger.warning("Disconnection failed");
            System.out.println("Problem with Disconnection");
        }

    }
    public int sendMS(String theMessage){
        int Status=0;
        byte tobesentMessage[]=null;
        tobesentMessage=theMessage.getBytes();
        if(tobesentMessage.length>128){
            Status=2;
            logger.warning("String has reached maximum size");

        }
        else{
            try{
                //OutputStream outputStream = S.getOutputStream();
                //String tosend = "hello from server\r\n";



                O.write(tobesentMessage);
                O.flush(); //never forget to flush
                String forlogger = new String(tobesentMessage, StandardCharsets.UTF_8);
                logger.fine(forlogger);
            } catch(IOException e){
                logger.warning("Problems by sending the message");
                System.out.println("Problems by sending the message");
            }
        }
        return Status;

    }
    public String showReply(){
        /*byte[] ReplyfromServer=new byte[Constants.BYTE_ARRAY_SIZE];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try{
            int k=-1;
            while((k=I.read(ReplyfromServer)) >-1){
                buffer.write(ReplyfromServer);
            }
        }catch(IOException e){
            System.out.println("error");
        }
        byte[] finalanswer = new byte[Constants.BYTE_ARRAY_SIZE];
        finalanswer = buffer.toByteArray();
        String finalfinalanswer = new String(finalanswer);
        return finalfinalanswer;*/

        /*boolean end =false;

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[Constants.BYTE_ARRAY_SIZE];
            data = buffer.toByteArray();
            buffer.close();
            String received=null;
            received = new String(data,Constants.TELNET_ENCODING);*/
        /*try{
            BufferedReader inputfromserver = new BufferedReader(new InputStreamReader(S.getInputStream()));
            String inputline;


            while((inputline=inputfromserver.readLine()) != null){
                System.out.println(inputline);
            }

            }catch(Exception e){
            System.out.println("Error happens");
        }*/


        int NumberofBytes= 0;
        boolean end = false;
        String ReplyfromServer = null;
        ByteArrayOutputStream buffered = new ByteArrayOutputStream();
        byte[] data = new byte[Constants.BYTE_ARRAY_SIZE];
        while (!end) {
            try {
                NumberofBytes = this.I.read(data, 0, data.length);
                buffered.write(data, 0, NumberofBytes);
                if(this.I.available() <= 0){
                    end = true;
                }


            } catch (IOException e) {
                System.out.println("Errors!");
            }
        }

        try {
            data = buffered.toByteArray();
            buffered.close();
            ReplyfromServer = new String(data);
            logger.fine(ReplyfromServer);
        } catch (UnsupportedEncodingException e) {
            logger.warning("Encoding problems!");
            System.out.println("Encoding problems!");
        } catch (IOException e) {
            logger.warning("Errors occured by showing reply from server!");
            System.out.println("Errors occured by showing reply from server!");
        }

        return ReplyfromServer;
    }


}
