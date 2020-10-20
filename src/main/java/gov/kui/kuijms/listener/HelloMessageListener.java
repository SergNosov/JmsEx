package gov.kui.kuijms.listener;

import gov.kui.kuijms.config.JmsConfig;
import gov.kui.kuijms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.artemis.api.core.JsonUtil;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders messageHeaders,
                       Message message){
        System.out.println("   --- Сообщение получено!!!");
        System.out.println(helloWorldMessage+"\n");
    }

    @JmsListener(destination = JmsConfig.MY_SEND_RSV_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders messageHeaders,
                       Message message) throws JMSException {

        System.out.println("Получен пароль: "+ helloWorldMessage.getMessage());

        HelloWorldMessage payloadMsg = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("World !!!")
                .build();

        System.out.println("--- Отправка отзыв ---");
        jmsTemplate.convertAndSend(message.getJMSReplyTo(),payloadMsg);
    }
}
