package ascendcorp.com.order.config;

import ascendcorp.com.order.stream.processor.OrderStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(OrderStreams.class)
public class StreamConfig {

}
