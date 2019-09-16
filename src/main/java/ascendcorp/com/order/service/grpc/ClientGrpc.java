package ascendcorp.com.order.service.grpc;

import ascendcorp.com.order.GrpcOrder;
import ascendcorp.com.order.OrderRequest;
import ascendcorp.com.order.OrderResponse;
import ascendcorp.com.order.VerifyServiceGrpc;
import ascendcorp.com.order.logger.Logger;
import ascendcorp.com.order.service.stream.OrderListener;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClientGrpc {

  private static final Logger logger = Logger.getInstance(OrderListener.class);

  ManagedChannel channel;

  public ClientGrpc() {
    this.channel = ManagedChannelBuilder.
        forAddress("localhost",50051)
        .build();
  }

  public void sendOrder(long value){

    VerifyServiceGrpc.VerifyServiceBlockingStub verifyStub =
        VerifyServiceGrpc.newBlockingStub(channel);

    GrpcOrder grpcOrder = GrpcOrder.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setMessage("new Order")
        .setStatus("INIT")
        .setValue(value)
        .build();

    OrderResponse response = verifyStub.verifyOrder(OrderRequest.newBuilder()
        .setOrder(grpcOrder)
        .build());

    logger.info("response from grpc server: "+response.getOrder().getMessage());

  }


}
