package tr.com.example.kafka;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by sotlu on 28.05.2019.
 */
@Service
class AnimatorService implements IAnimatorService {

    private KafkaProperties kafkaProperties;

    private Map<String, ITopicSender> senderMap;

    AnimatorService(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
        this.senderMap = new HashMap<>();
    }

    @Override
    public void start(String dataType, SendingDataProps dataProps) {
        TopicSender topicSender = new TopicSender(dataType, dataProps, getBootstrapServersStr());
        Optional.ofNullable(senderMap.put(topicSender.getSenderName(), topicSender))
                .ifPresent(ITopicSender::cancel);
        topicSender.send();


    }


    @Override
    public void stop(String dataType, String topicName) {
        Optional.of(TopicSender.generateSenderName(dataType, topicName))
                .map(senderMap::remove)
                .ifPresent(ITopicSender::cancel);
    }

    private String getBootstrapServersStr() {
        return String.join(",", kafkaProperties.getBootstrapServers());
    }
}
