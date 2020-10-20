package gov.kui.kuijms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KuiJmsApplication {

    public static void main(String[] args) throws Exception {
//      Для встроенного брокера сообщений
//        ActiveMQServer server = ActiveMQServers.newActiveMQServer(new ConfigurationImpl()
//                .setPersistenceEnabled(false)
//                .setJournalDirectory("target/data/journal")
//                .setSecurityEnabled(false)
//                .addAcceptorConfiguration("invm","vm://0"));
//
//        server.start();

        SpringApplication.run(KuiJmsApplication.class, args);
    }

}
