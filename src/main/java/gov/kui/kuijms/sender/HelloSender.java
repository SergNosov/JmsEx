package gov.kui.kuijms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.kui.kuijms.config.JmsConfig;
import gov.kui.kuijms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

//   @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        System.out.println("--- Отправляю сообщение! ");

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("!!! Вам срочное сообщение !!!")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

        System.out.println("--- Cообщение отправлено! ");
        System.out.println(" ");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndResiveMessage() throws JMSException {

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("!!! Hello ")
                .build();

        Message receviedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RSV_QUEUE, session -> {
            try {
                Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "gov.kui.kuijms.model.HelloWorldMessage");
                System.out.println("--- Отправка пароля ---");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException("boom "+e.getMessage());
            }
        });

        System.out.println("Получен отзыв: "+receviedMsg.getBody(String.class)+"\n");
    }
}
