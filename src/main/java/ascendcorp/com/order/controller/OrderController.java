package ascendcorp.com.order.controller;

import ascendcorp.com.order.service.OrderExecuteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  private final OrderExecuteService orderExecuteService;

  public OrderController(OrderExecuteService orderExecuteService) {
    this.orderExecuteService = orderExecuteService;
  }

  @GetMapping("/order")
  public void order(@RequestParam("order") Long value){

    orderExecuteService.sendVerify(value);
  }
}
