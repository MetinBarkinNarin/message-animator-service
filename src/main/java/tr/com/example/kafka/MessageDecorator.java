package tr.com.example.kafka;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class MessageDecorator implements IMessageDecorator {
    private static Map<String, IMessageDecorator> decorators = new HashMap<>();

    public MessageDecorator() {
    }

    @Override
    public Object messageDecorate(String str) {
        return Optional.ofNullable(str)
                .map(s -> ObjectMapperUtil
                        .readObject(str, Object.class))
                .orElse(str);
    }

    public static IMessageDecorator getDecorator(SendingDataProps dataProps, String dataType) {
//        if (Objects.equals("m_message", dataType)) {
//            return decorators.computeIfAbsent(dataType, dt -> new MMessageDecorator(dataProps));
//        } else if (Objects.equals("base-tactical", dataType)) {
//            return decorators.computeIfAbsent(dataType, dt -> new BaseTacticalModelDecorator(dataProps));
//        } else if (Objects.equals("create-jreap-resource", dataType)) {
//            return decorators.computeIfAbsent(dataType, dt -> new MessageDecorator());
//        }
        return decorators.computeIfAbsent("NO_DECORATION", dt -> new MessageDecorator());
    }
}
