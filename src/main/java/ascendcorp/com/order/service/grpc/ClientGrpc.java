package ascendcorp.com.order.service.grpc;

import ascendcorp.com.order.GrpcOrder;
import ascendcorp.com.order.OrderRequest;
import ascendcorp.com.order.OrderResponse;
import ascendcorp.com.order.VerifyServiceGrpc;
import ascendcorp.com.order.constant.JwtConstants;
import ascendcorp.com.order.logger.Logger;
import ascendcorp.com.order.service.grpc.credentials.JwtCallCredential;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.MetadataUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.lognet.springboot.grpc.autoconfigure.GRpcServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties({GRpcServerProperties.class})
public class ClientGrpc{

  private static final Logger logger = Logger.getInstance(ClientGrpc.class);

  ManagedChannel channel;

  @Autowired
  private RedisTemplate redisTemplate;

  public ClientGrpc(GRpcServerProperties grpcServerProperties) throws IOException {
    this.channel = NettyChannelBuilder.
        forAddress("localhost",50051)
        .sslContext(
            GrpcSslContexts
                .forClient()
                .trustManager(grpcServerProperties.getSecurity().getCertChain().getFile())
                .build())
//        .intercept(new ClientBasicAuthInterceptor()) //for Basic Auth

        .build();
  }

  public GrpcOrder sendOrder(String value, String token){

    //for JWT
    String jwt = getJwt();
    logger.info("JWT Token: {}",jwt);
    JwtCallCredential jwtToken = new JwtCallCredential(jwt);

    VerifyServiceGrpc.VerifyServiceBlockingStub verifyStub =
        VerifyServiceGrpc
            .newBlockingStub(channel)
            .withCallCredentials(jwtToken);
//for OAuth2
//    verifyStub = token(verifyStub, token);

    OrderResponse response = verifyStub.verifyOrder(OrderRequest.newBuilder()
        .setOrderId(value)
        .build());

    logger.info("response from grpc server: "+response.getOrder());
    return response.getOrder();

  }

  private VerifyServiceGrpc.VerifyServiceBlockingStub token(
      VerifyServiceGrpc.VerifyServiceBlockingStub stub, String token){

    Metadata metadata = new Metadata();
    metadata.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER),
        "Bearer "+token);
    stub = MetadataUtils.attachHeaders(stub,metadata);
    return stub;
  }

  private static String getJwt() {
    List<GrantedAuthority> grantedAuthorities = AuthorityUtils
        .commaSeparatedStringToAuthorityList("ROLE_USER");

    return Jwts.builder()
        .setSubject("test1") // client's identifier
        .claim("authorities", grantedAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()))
        .signWith(SignatureAlgorithm.HS256, JwtConstants.JWT_SIGNING_KEY)
        .compact();
  }


}
