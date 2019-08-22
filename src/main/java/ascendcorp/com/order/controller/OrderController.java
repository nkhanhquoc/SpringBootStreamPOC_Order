package ascendcorp.com.order.controller;

import ascendcorp.com.order.service.OrderExecuteService;
import ascendcorp.com.order.ulti.ResponseFactory;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity order(@RequestParam("order") Long value){

    orderExecuteService.sendVerify(value);
    return ResponseFactory.success();
  }
}
