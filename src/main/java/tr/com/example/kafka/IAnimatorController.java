package tr.com.example.kafka;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * Created by nozdemir on 2.03.2020.
 */
public interface IAnimatorController {
    @GetMapping(
            value = "/start/{dataType}",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE
    )
    void start(@PathVariable(value = "dataType") String dataType,
               @RequestParam(value = "topicName", required = false) String topicName,
               @RequestParam(value = "fileName", required = false) String fileName,
               @RequestParam(value = "key", required = false) Integer key,
               @RequestParam(value = "workerNumber", required = false) Integer workerNumber,
               @RequestParam(value = "times", required = false) Long times,
               @RequestParam(value = "interval", required = false) Long interval,
               @RequestParam(value = "sendingType", required = false) SendingType sendingType,
               @RequestParam(value = "schemaFileName", required = false) String schemaFileName) throws NoSuchFieldException, IOException;

    @GetMapping(
            value = "/stop/{dataType}/{topicName}",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE
    )
    void stop(@PathVariable(value = "dataType") String dataType,
              @PathVariable(value = "topicName") String topicName);
}
