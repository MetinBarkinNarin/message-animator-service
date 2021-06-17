package tr.com.example.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/animator")
class AnimatorController implements IAnimatorController {

    private IAnimatorService animatorService;
    private AnimatorConfig animatorConfig;

    public AnimatorController(IAnimatorService animatorService,
                              AnimatorConfig animatorConfig) {
        this.animatorService = animatorService;
        this.animatorConfig = animatorConfig;
    }

    @Override
    @GetMapping(
            value = "/start/{dataType}",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE
    )
    public void start(@PathVariable(value = "dataType") String dataType,
                      @RequestParam(value = "topicName", required = false) String topicName,
                      @RequestParam(value = "fileName", required = false) String fileName,
                      @RequestParam(value = "key", required = false) Integer key,
                      @RequestParam(value = "workerNumber", required = false) Integer workerNumber,
                      @RequestParam(value = "times", required = false) Long times,
                      @RequestParam(value = "interval", required = false) Long interval,
                      @RequestParam(value = "sendingType", required = false) SendingType sendingType,
                      @RequestParam(value = "schemaFileName", required = false) String schemaFileName) throws NoSuchFieldException {
        SendingDataProps dataProps = animatorConfig.getSendingDataProps().get(dataType);
        Optional.ofNullable(dataProps).orElseThrow(NoSuchFieldException::new);

        Optional.ofNullable(topicName).ifPresent(dataProps::setTopicName);
        Optional.ofNullable(key).ifPresent(dataProps::setKey);
        Optional.ofNullable(fileName).ifPresent(dataProps::setFileName);
        Optional.ofNullable(workerNumber).ifPresent(dataProps::setWorkerNumber);
        Optional.ofNullable(times).ifPresent(dataProps::setTimes);
        Optional.ofNullable(interval).ifPresent(dataProps::setInterval);
        Optional.ofNullable(sendingType).ifPresent(dataProps::setSendingType);
        Optional.ofNullable(schemaFileName).ifPresent(dataProps::setSchemaFileName);


        animatorService.start(dataType, dataProps);
    }

    @Override
    @GetMapping(
            value = "/stop/{dataType}/{topicName}",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE
    )
    public void stop(@PathVariable(value = "dataType") String dataType,
                     @PathVariable(value = "topicName") String topicName) {

        animatorService.stop(dataType, topicName);
    }

    @Autowired
    public void setSchemaRegistryUrl(AnimatorConfig animatorConfig) {
        TopicSender.schemaRegistryUrl = animatorConfig.getSchemaRegistryUrl();
    }
}
