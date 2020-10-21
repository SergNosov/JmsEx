package gov.kui.kuijms.listener;

import gov.kui.kuijms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "${jmsconfig.myqueue}")
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders messageHeaders,
                       Message message){
        log.info("--- Сообщение получено: "+helloWorldMessage+"\n");
    }

    @JmsListener(destination = "${jmsconfig.my_send_rsv_queue}")
    public void listenForHello(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders messageHeaders,
                       Message message) throws JMSException {

        log.info("Получен пароль: "+ helloWorldMessage.getMessage());

        HelloWorldMessage payloadMsg = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("World !!!")
                .build();

        log.info("--- Отправка отзыв ---");
        jmsTemplate.convertAndSend(message.getJMSReplyTo(),payloadMsg);
    }
}
