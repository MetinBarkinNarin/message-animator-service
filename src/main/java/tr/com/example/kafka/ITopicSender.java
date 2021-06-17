package tr.com.example.kafka;

/**
 * Created by nozdemir on 2.03.2020.
 */
public interface ITopicSender {
    void send();

    void cancel();
}
