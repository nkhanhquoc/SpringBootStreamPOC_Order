package ascendcorp.com.order.service.grpc;

import ascendcorp.com.order.GrpcOrder;
import ascendcorp.com.order.OrderRequest;
import ascendcorp.com.order.OrderResponse;
import ascendcorp.com.order.VerifyServiceGrpc;
import ascendcorp.com.order.logger.Logger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import java.io.File;
import java.io.IOException;
import org.lognet.springboot.grpc.autoconfigure.GRpcAutoConfiguration;
import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties({GRpcServerProperties.class})
public class ClientGrpc{

  private static final Logger logger = Logger.getInstance(ClientGrpc.class);

  ManagedChannel channel;

  private GRpcServerProperties grpcServerProperties;

  public ClientGrpc(GRpcServerProperties grpcServerProperties) throws IOException {
    this.channel = NettyChannelBuilder.
        forAddress("localhost",50051)
        .sslContext(
            GrpcSslContexts
                .forClient()
                .trustManager(grpcServerProperties.getSecurity().getCertChain().getFile())
                .build())
        .intercept(new ClientBasicAuthInterceptor())
        .build();
  }

  public GrpcOrder sendOrder(String value){

    VerifyServiceGrpc.VerifyServiceBlockingStub verifyStub =
        VerifyServiceGrpc.newBlockingStub(channel);

//    GrpcOrder grpcOrder = GrpcOrder.newBuilder()
//        .setId(UUID.randomUUID().toString())
//        .setMessage("new Order")
//        .setStatus("INIT")
//        .setValue(value)
//        .build();

    OrderResponse response = verifyStub.verifyOrder(OrderRequest.newBuilder()
        .setOrderId(value)
        .build());

    logger.info("response from grpc server: "+response.getOrder());
    return response.getOrder();

  }


}
