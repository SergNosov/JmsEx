package gov.kui.kuijms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.kuijms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${jmsconfig.myqueue}")
    private String myQueue;

    @Value("${jmsconfig.my_send_rsv_queue}")
    private String mySendRsvQueue;


    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Вам срочное сообщение !!!")
                .build();

        jmsTemplate.convertAndSend(myQueue, message);

        log.info("--- Cообщение отправлено!");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndResiveMessage() throws JMSException {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("!!! Hello ")
                .build();

        Message receviedMsg = jmsTemplate.sendAndReceive(mySendRsvQueue, session -> {
            try {
                Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "gov.kui.kuijms.model.HelloWorldMessage");

                log.info("--- Отправка пароля ---");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException("boom "+e.getMessage());
            }
        });
        log.info("Получен отзыв: "+receviedMsg.getBody(String.class)+"\n");
    }
}
