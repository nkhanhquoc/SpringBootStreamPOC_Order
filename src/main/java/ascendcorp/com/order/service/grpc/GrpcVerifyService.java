package ascendcorp.com.order.service.grpc;

import ascendcorp.com.order.GrpcOrder;
import ascendcorp.com.order.OrderRequest;
import ascendcorp.com.order.OrderResponse;
import ascendcorp.com.order.VerifyServiceGrpc;
import ascendcorp.com.order.logger.Logger;
import ascendcorp.com.order.mapper.VerifyOrderMapper;
import ascendcorp.com.order.model.VerifyOrder;
import ascendcorp.com.order.repository.VerifyOrderRepository;
import ascendcorp.com.order.service.stream.OrderListener;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class GrpcVerifyService extends VerifyServiceGrpc.VerifyServiceImplBase {

  private static final Logger logger = Logger.getInstance(OrderListener.class);

  private VerifyOrderRepository verifyOrderRepository;
  private VerifyOrderMapper mapper;

  public GrpcVerifyService(
      VerifyOrderRepository verifyOrderRepository,
      VerifyOrderMapper mapper) {
    this.verifyOrderRepository = verifyOrderRepository;
    this.mapper = mapper;
  }

  @Override
  public void verifyOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {

    GrpcOrder order = request.getOrder();
    String verifyStatus = order.getValue() > 100 ? "DENIED" : "ACCEPT";
    VerifyOrder verifyOrder = VerifyOrder.builder()
        .id(order.getId())
        .oldStatus(order.getStatus())
        .newStatus(verifyStatus)
        .build();

    verifyOrderRepository.save(mapper.transform(verifyOrder));


    OrderResponse response = OrderResponse.newBuilder()
        .setOrder(
            GrpcOrder.newBuilder()
            .setValue(order.getValue())
            .setStatus(verifyStatus)
            .setId(order.getId())
            .setMessage(order.getMessage())
            .build()
        )
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();

  }
}
