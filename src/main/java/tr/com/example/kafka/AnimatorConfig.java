package tr.com.example.kafka;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by nozdemir on 17.02.2020.
 */

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@Configuration
@ConfigurationProperties(prefix = "animator")
public class AnimatorConfig extends DefaultAnimatorConfig {
    private String schemaRegistryUrl;
    private Map<String, SendingDataProps> sendingDataProps;

    public String getSchemaRegistryUrl() {
        return Optional.ofNullable(schemaRegistryUrl)
                .orElse(DEFAULT_SCHEMA_REGISTRY_URL);
    }

    public Map<String, SendingDataProps> getSendingDataProps() {
        return Optional.ofNullable(sendingDataProps)
                .orElse(new HashMap<>());
    }
}
