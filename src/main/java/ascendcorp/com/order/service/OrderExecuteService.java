package ascendcorp.com.order.service;

import ascendcorp.com.order.mapper.VerifyOrderMapper;
import ascendcorp.com.order.model.Order;
import ascendcorp.com.order.model.VerifyOrder;
import ascendcorp.com.order.repository.VerifyOrderRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderExecuteService {

  private VerifyOrderRepository verifyOrderRepository;

  private OrderStreamService orderStreamService;

  private VerifyOrderMapper mapper;

  public OrderExecuteService(VerifyOrderRepository verifyOrderRepository,
      VerifyOrderMapper mapper,
      OrderStreamService orderStreamService) {
    this.verifyOrderRepository = verifyOrderRepository;
    this.mapper = mapper;
    this.orderStreamService = orderStreamService;
  }


  public void verifyOrder(Order order){
    String verifyStatus = order.getValue() > 100 ? "DENIED" : "ACCEPT";
    VerifyOrder verifyOrder = VerifyOrder.builder()
        .id(order.getId())
        .oldStatus(order.getStatus())
        .newStatus(verifyStatus)
        .build();

    order.setStatus(verifyStatus);

    orderStreamService.sendOrder(order);
    verifyOrderRepository.save(mapper.transform(verifyOrder));
  }

}
