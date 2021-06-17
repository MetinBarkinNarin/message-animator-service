package tr.com.example.kafka;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.java.Log;
import org.apache.avro.Schema;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Log
class TopicSender implements ITopicSender {
    private static final String senderNameFormat = "{0}-{1}"; // [data-type]-[topic-name]
    static String schemaRegistryUrl;

    private String senderName;
    private Schema schema;
    private SendingDataProps dataProps;
    private ParallelFlux<?> sendingFlux;
    private Scheduler scheduler;
    private KafkaSender<Object, Object> sender;
    private Disposable subscription;

    TopicSender(String dataType, @NotNull SendingDataProps dataProps, String bootstrapServers) {
        this.dataProps = dataProps;
        this.senderName = generateSenderName(dataType, dataProps.getTopicName());
        this.scheduler = Schedulers.newElastic(senderName);
        this.sendingFlux = prepareFlux(dataType, dataProps);
        this.schema = CustomRecordUtil.parseSchema(dataProps.getSchemaFileName());
        this.sender = Optional.of(bootstrapServers)
                .map(this::createProducerProps)
                .map(TopicSenderUtil::reactiveKafkaSender)
                .orElseThrow(NoSuchFieldError::new);
    }

    @Override
    public void send() {
        subscription = sender
//                .createOutbound()
                .send(sendingFlux
                        .map(this::createRecord)
                )
                .subscribe();
    }

    @Override
    public void cancel() {
        subscription.dispose();
        scheduler.dispose();
//        sender.close();
    }

    private SenderRecord<Object, Object, Object> createRecord(Object data) {
        data = Objects.equals(dataProps.getSendingType(), SendingType.AVRO)
                ? CustomRecordUtil.createGenericRecord(schema, ObjectMapperUtil.objectToMap(data))
                : data;

        return CustomRecordUtil.createSenderRecord(
                dataProps.getTopicName(),
                dataProps.getKey(),
                data);
    }


    private ParallelFlux<?> prepareFlux(String dataType, SendingDataProps dataProps) {
        IMessageDecorator decorator = MessageDecorator.getDecorator(dataProps, dataType);

        Flux<?> rawFlux = CustomFluxUtil.fileToFlux(dataProps.getFileName())
                .map(decorator::messageDecorate)
//                .map(s -> ObjectMapperUtil.readObject(s, M_Message.class))
//                .map(ObjectMapperUtil::writeToString)
                .repeat(dataProps.getTimes() - 1)
                .doOnNext((s) -> ProgressLoggerUtil.logProgress(
                        dataType,
                        dataProps.getTopicName(),
                        dataProps.getKey())
                )
                .doOnSubscribe(s -> log.info("STARTED..."))
                .doOnComplete(() -> log.info("FINISHED..."))
                .doOnCancel(() -> log.info("CANCELED..."));

        return CustomFluxUtil.setIntervalToFlux(rawFlux, dataProps.getInterval())
                .parallel(dataProps.getWorkerNumber())
                .runOn(scheduler);
    }

    private Map<String, Object> createProducerProps(String bootstrapServers) {
        Map<String, Object> map = new CustomProducerConfigBuilder()
                .bootstrapServers(bootstrapServers)
                .keySerializer(IntegerSerializer.class)
                .valueSerializer(getValueSerializerClass())
                .build();
        map.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                schemaRegistryUrl);
        return map;
    }

    private Class<?> getValueSerializerClass() {
        return Objects.equals(dataProps.getSendingType(), SendingType.AVRO)
                ? KafkaAvroSerializer.class
                : JsonSerializer.class;
    }

    public String getSenderName() {
        return senderName;
    }

    static String generateSenderName(String dataType, String topicName) {
        return MessageFormat.format(
                senderNameFormat,
                dataType,
                topicName
        );
    }
}
