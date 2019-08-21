package ascendcorp.com.order.stream.processor;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OrderStreams {

  String OUTPUT = "order-verify-request";
  String INPUT = "order-verify-response";

  @Input(INPUT)
  SubscribableChannel getVerifiedOrder();

  @Output(OUTPUT)
  MessageChannel sendVerifyOrder();
}
