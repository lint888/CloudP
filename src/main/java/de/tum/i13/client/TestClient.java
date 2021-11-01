package de.tum.i13.client;

import de.tum.i13.shared.Constants;


import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;
import de.tum.i13.operations.operationDetails;

import static de.tum.i13.shared.LogSetup.setupLogging;

public class TestClient {

    private final static Logger LOGGER = Logger.getLogger(TestClient.class.getName());
    static operationDetails operations;

    public static void main(String[] args) throws IOException {
        setupLogging("test.log");
        operations = new operationDetails();
        int connected=0;

        //Socket s = new Socket("clouddatabases.msrg.in.tum.de",5551);
        //LOGGER.info("Connecting to server");
        //TODO


        //LOGGER.info("Getting the outputstream and inputstream");
        //TODO
        BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        while(!quit){
            System.out.print("EchoClient> ");
            try{
                String input = cons.readLine();
                String[] tokens = input.trim().split("\\s+");
                int CheckifStringtoolong=0;
                if(tokens != null){
                    if(tokens.length == 2 && tokens[0].equals("help")){
                        operations.askforHelp(tokens[1]);
                    }
                    else if(tokens.length == 3 && tokens[0].equals("connect")){
                        String IPAdress = tokens[1];
                        String PortNumber = tokens[2];
                        operations.creatConnection(IPAdress,PortNumber);
                        connected=1;
                        if(connected==1){
                            System.out.println(operations.showReply());

                        }
                    }
                    else if(tokens[0].equals("send")){
                        if(connected==1){
                            String tobesentMessage=null;
                            tobesentMessage=transformtheMessage(tokens);
                            CheckifStringtoolong = operations.sendMS(tobesentMessage);
                            if(CheckifStringtoolong==2){
                                System.out.println("The maximum size of a string has been reached. Pleasr try a smaller string");
                            }else{
                                System.out.println(operations.showReply());
                            }

                        }
                        else{
                            System.out.println("Not connected");
                        }
                    }
                    else if(tokens[0].equals("disconnect")){
                        operations.disconnect();
                        System.out.println("Disconnected");
                        connected=0;
                    }
                    else if(tokens[0].equals("quit")){
                        quit=true;
                        System.out.println("Quit");
                    }
                    else{
                        System.out.println("Invalid Commands. Please try again");
                        System.out.println("Right commands are: connect, send, disconnect and quit");

                    }
                }
            }catch (Exception e) {
                LOGGER.throwing(TestClient.class.getName(), "main", e);
            }

            //LOGGER.info("sending hello echo");



            //LOGGER.info("printing what the server has returned");



        }
        }


    static String transformtheMessage(String[] tobeTransformedMessage){
        String Message=null;
        StringBuilder tosend = new StringBuilder();
        int i=1;
        for( i = 1; i< tobeTransformedMessage.length;i++){
            if(i == tobeTransformedMessage.length-1){
                tosend.append(tobeTransformedMessage[i]);
                tosend.append("\r\n");
            }
            else{
                tosend.append(tobeTransformedMessage[i]);
                tosend.append(" ");
            }
        }
        Message = tosend.toString();
        return Message;
    }





}

