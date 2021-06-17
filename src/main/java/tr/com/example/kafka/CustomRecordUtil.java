package tr.com.example.kafka;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import reactor.kafka.sender.SenderRecord;

import java.util.Map;
import java.util.Optional;

/**
 * Created by nozdemir on 4.03.2020.
 */
final class CustomRecordUtil {
    static <K, V, T> SenderRecord<K, V, T> createSenderRecord(String topicName, K key, V value) {
        return SenderRecord
                .create(
                        createProducerRecord(
                                topicName,
                                key,
                                value
                        ),
                        null
                );
    }
    static <K, V, T> SenderRecord<K, V, T> createSenderRecord(String topicName,  V value) {
        return SenderRecord
                .create(
                        createProducerRecord(
                                topicName,
                                value
                        ),
                        null
                );
    }

    static <K, V> ProducerRecord<K, V> createProducerRecord(String topicName, K key, V value) {
        return new ProducerRecord<>(
                topicName,
                key,
                value
        );
    }
    static <K, V> ProducerRecord<K, V> createProducerRecord(String topicName,  V value) {
        return new ProducerRecord<>(
                topicName,

                value
        );
    }

    static Schema createSchema(String json) {
        return new Schema.Parser().parse(json);
    }

    static Schema parseSchema(String schemaPath) {
        return Optional.ofNullable(schemaPath)
                .map(CustomPathUtil::toPath)
                .map(CustomPathUtil::getFileAsString)
                .map(CustomRecordUtil::createSchema)
                .orElse(Schema.create(Schema.Type.NULL));
    }

    static GenericRecord createGenericRecord(Schema schema, Map<?, ?> fieldMap) {
        return schema.getFields()
                .stream()
                .reduce(new GenericData.Record(schema),
                        (record, field) -> putRecord(
                                record,
                                field.name(),
                                field.schema(),
                                fieldMap.get(field.name())
                        ),
                        (record, record2) -> record = record2
                );
    }

    private static GenericData.Record putRecord(GenericData.Record record,
                                                String name,
                                                Schema fieldSchema,
                                                Object data) {
        switch (fieldSchema.getType()) {
            case RECORD:
                data = createGenericRecord(fieldSchema, (Map<?, ?>) data);
                break;
            case ENUM:
                data = new GenericData.EnumSymbol(fieldSchema, data);
                break;
        }
        record.put(name, data);
        return record;
    }
}
