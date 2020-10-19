package gov.kui.kuijms.listener;

import gov.kui.kuijms.config.JmsConfig;
import gov.kui.kuijms.model.HelloWorldMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class HelloMessageListener {

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders messageHeaders,
                       Message message){
        System.out.println("   --- Сообщение получено!!!");
        System.out.println(helloWorldMessage+"\n");
    }
}
