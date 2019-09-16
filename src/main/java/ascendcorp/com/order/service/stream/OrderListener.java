package ascendcorp.com.order.service.stream;

import ascendcorp.com.order.logger.Logger;
import ascendcorp.com.order.model.Order;
import ascendcorp.com.order.stream.processor.OrderStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

  private static final Logger logger = Logger.getInstance(OrderListener.class);

  private OrderExecuteService orderExecuteService;

  @Autowired
  public OrderListener(OrderExecuteService orderExecuteService) {
    this.orderExecuteService = orderExecuteService;
  }

  @StreamListener(OrderStreams.INPUT)
  public void handleOrder(@Payload Order order) {
    logger.info("Order Id = {}.  order status: {}", order.getId(),order.getStatus());

    orderExecuteService.verifyOrder(order);

  }

}
