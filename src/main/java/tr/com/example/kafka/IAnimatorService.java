package tr.com.example.kafka;

/**
 * Created by nozdemir on 2.03.2020.
 */
public interface IAnimatorService {
    void start(String dataType, SendingDataProps dataProps) ;

    void stop(String dataType, String topicName);
}
