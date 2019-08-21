package ascendcorp.com.order.stream.processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OrderStreams {

  String INPUT = "order-verify-request";
  String OUTPUT = "order-verify-response";

  @Input(INPUT)
  SubscribableChannel getVerifyOrder();

  @Output(OUTPUT)
  MessageChannel sendVerifiedOrder();
}
