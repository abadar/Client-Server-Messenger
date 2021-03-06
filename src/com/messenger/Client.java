package com.messenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arsalan
 */
public class Client extends javax.swing.JFrame {

    /**
     * Creates new form Client
     */
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread connectThread;
    Thread recievingThread;
    Thread sendThread;
    boolean clientStatus = false, serverStatus = false;

    public Client() {
        initComponents();
        this.setBounds(600, 80, 450, 400);
        disableFields();
    }

    /**
     * Function to setup fields
     */
    private void initialize() {
        try {
            connectionStatus.setText(Util.NOT_CONNECTED);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            enableFields();
        } catch (Exception ex) {
            disableFields();
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel1 = new javax.swing.JLabel();
        ip_Port = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        conversation = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        chat = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        connectionStatus = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Instant LAN Messanger Client");
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Enter IP & Port:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 130, 30);

        ip_Port.setText("127.0.0.1:1234");
        getContentPane().add(ip_Port);
        ip_Port.setBounds(10, 50, 240, 30);

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });
        getContentPane().add(connectButton);
        connectButton.setBounds(260, 50, 150, 30);

        conversation.setEditable(false);
        conversation.setColumns(20);
        conversation.setRows(5);
        conversation.setEnabled(false);
        jScrollPane2.setViewportView(conversation);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 80, 430, 250);

        chat.setColumns(20);
        chat.setRows(1);
        jScrollPane1.setViewportView(chat);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 330, 370, 40);

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        getContentPane().add(sendButton);
        sendButton.setBounds(380, 330, 60, 40);

        connectionStatus.setEditable(false);
        connectionStatus.setText("                         Not Connected");
        connectionStatus.setEnabled(false);
        connectionStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionStatusActionPerformed(evt);
            }
        });
        getContentPane().add(connectionStatus);
        connectionStatus.setBounds(170, 10, 240, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        // TODO add your handling code here:

        Runnable runable = new Runnable() {
            public void run() {
                String[] temp = ip_Port.getText().split(":");
                int port = 0;
                try {
                    port = Integer.parseInt(temp[1]);
                    socket = new Socket(temp[0], port);
                    initialize();
                    connectionStatus.setText(Util.CONNECTED);
                    startListening();
                } catch (Exception ex) {
                    connectionStatus.setText(Util.NOT_CONNECTED);
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        connectThread = new Thread(runable);
        connectThread.start();

    }//GEN-LAST:event_connectButtonActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed

        Runnable runable = new Runnable() {
            public void run() {

                try {
                    // TODO add your handling code here:
                    dataOutputStream.writeUTF(chat.getText()); // send the above line to the server.
                    dataOutputStream.flush(); // flush the stream to ensure that the data reaches the other end.

                    if (!clientStatus) {
                        conversation.append("\nClient: \n [" + Util.getTime() + "]  " + chat.getText());
                        clientStatus = true;
                    } else {

                        conversation.append("\n [" + Util.getTime() + "]  " + chat.getText());
                    }
                    serverStatus = false;
                    chat.setText("");
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        sendThread = new Thread(runable);
        sendThread.start();


    }//GEN-LAST:event_sendButtonActionPerformed

    private void connectionStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_connectionStatusActionPerformed
    private void disableFields() {
        chat.setEnabled(false);
        sendButton.setEnabled(false);
        connectionStatus.setText(Util.NOT_CONNECTED);
    }

    private void enableFields() {
        chat.setEnabled(true);
        sendButton.setEnabled(true);
    }

    private void startListening() {
        Runnable runable = new Runnable() {
            public void run() {
                boolean error = false;
                String text;
                while (true && !error) {
                    try {
                        text = dataInputStream.readUTF();
                        clientStatus = false;
                        if (!serverStatus) {
                            conversation.append("\nServer: \n [" + Util.getTime() + "]  " + text); // wait for the server to send a line of text.                        
                            serverStatus = true;

                        } else {

                            conversation.append("\n [" + Util.getTime() + "]  " + text); // wait for the server to send a line of text. 
                        }
                    } catch (IOException ex) {
                        conversation.append("\nServer Lost");
                        error = true;
                        disableFields();
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };

        recievingThread = new Thread(runable);
        recievingThread.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced dataInputStream Java SE 6) is not available, stay with the default look and feel.
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
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chat;
    private javax.swing.JButton connectButton;
    private javax.swing.JTextField connectionStatus;
    private javax.swing.JTextArea conversation;
    private javax.swing.JTextField ip_Port;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
