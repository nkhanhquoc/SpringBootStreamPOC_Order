package ascendcorp.com.order.service;

import ascendcorp.com.order.mapper.OrderEntityMapper;
import ascendcorp.com.order.model.Order;
import ascendcorp.com.order.repository.OrderRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderExecuteService {

  private OrderRepository orderRepository;

  private OrderStreamService orderStreamService;

  private OrderEntityMapper mapper;

  public OrderExecuteService(OrderRepository orderRepository,
      OrderEntityMapper mapper,
      OrderStreamService orderStreamService) {
    this.orderRepository = orderRepository;
    this.mapper = mapper;
    this.orderStreamService = orderStreamService;
  }

  public void save(Order order){
    orderRepository.save(mapper.transform(order));

  }

  public void sendVerify(Long value){
    Order order = Order.builder()
        .message("new Order")
        .status("INIT")
        .id(UUID.randomUUID().toString())
        .value(value)
        .build();

    this.save(order);

    orderStreamService.sendOrder(order);

  }

}
