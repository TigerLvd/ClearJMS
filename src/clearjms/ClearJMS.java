/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clearjms;

import javax.jms.*;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import javajmsclass.JavaJMSClass;

/**
 *
 * @author LVD
 */
public class ClearJMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String str = "jms/Res"; // input name here.

        try {
            Context ctx = new InitialContext();
            QueueConnectionFactory qcf = (QueueConnectionFactory)
                    ctx.lookup("jms/__defaultConnectionFactory");
            QueueConnection con = qcf.createQueueConnection();
            QueueSession session = con.createQueueSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Queue queue = null;
            try {
                queue = (Queue) ctx.lookup(str);
            } catch (NameNotFoundException nnfe) {
                queue = session.createQueue(str);
                ctx.bind(str, queue);
            }

            QueueReceiver receiver = session.createReceiver(queue);
            con.start();

            for (int j = 0; j < 100; j++) {
                ObjectMessage objMessage = (ObjectMessage) receiver.receive();

                try {
                    JavaJMSClass.MessageJMS res = (JavaJMSClass.MessageJMS)
                            objMessage.getObject();
                    for (int i = 0; i < res.list.size(); i++) {
                        System.out.print(res.list.get(i) + " ");
                    }
                    System.out.print(res.forward);
                    System.out.println();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    JavaJMSClass.ResultJMS res = (JavaJMSClass.ResultJMS)
                            objMessage.getObject();
                    for (int i = 0; i < res.list.size(); i++) {
                        System.out.print(res.list.get(i) + " ");
                    }
                    System.out.print(res.forward + " " + res.sum);
                    System.out.println();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
