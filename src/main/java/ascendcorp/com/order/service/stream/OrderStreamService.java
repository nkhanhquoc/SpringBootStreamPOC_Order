package ascendcorp.com.order.service.stream;

import ascendcorp.com.order.logger.Logger;
import ascendcorp.com.order.model.Order;
import ascendcorp.com.order.stream.processor.OrderStreams;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class OrderStreamService {

  private static final Logger logger = Logger.getInstance(OrderListener.class);

  private final OrderStreams orderStreams;

  public OrderStreamService(OrderStreams orderStreams) {
    this.orderStreams = orderStreams;
  }

  public void sendOrder(final Order order) {
    logger.info("Send order id = {}, status = {}, value = {}",
        order.getId(), order.getStatus(), order.getValue());
    MessageChannel messageChannel = orderStreams.sendVerifyOrder();
    messageChannel.send(MessageBuilder
        .withPayload(order)
        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
        .build());
  }


}
