package demo1.ddd.app;

import demo1.ddd.domain.AuditMessage;
import demo1.ddd.domain.SendResult;
import demo1.ddd.infra.KafkaTemplate;

public class AuditMessageProducer {
    private KafkaTemplate kafkaTemplate;

    public SendResult send(AuditMessage message) {
        kafkaTemplate.send("some topic", message.getBody());
        return SendResult.SUCCESS;
    }
}
