package ascendcorp.com.order.controller;

import ascendcorp.com.order.service.grpc.ClientGrpc;
import ascendcorp.com.order.service.stream.OrderExecuteService;
import ascendcorp.com.order.ulti.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  private final OrderExecuteService orderExecuteService;
  private final ClientGrpc clientGrpc;

  public OrderController(OrderExecuteService orderExecuteService,
      ClientGrpc clientGrpc) {
    this.orderExecuteService = orderExecuteService;
    this.clientGrpc = clientGrpc;
  }

  @GetMapping("/order")
  public ResponseEntity order(@RequestParam("order") Long value){

    orderExecuteService.sendVerify(value);
    return ResponseFactory.success();
  }

  @GetMapping("/order-grpc")
  public ResponseEntity orderGrpc(@RequestParam("order") Long value){

    clientGrpc.sendOrder(value);
    return ResponseFactory.success();
  }

}
