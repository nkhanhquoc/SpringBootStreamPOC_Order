package ascendcorp.com.order.service.grpc.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;

@GRpcGlobalInterceptor
public class ClientBasicAuthInterceptor implements ClientInterceptor {


  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    return new SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {

      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        System.out.println("Prepare add authorization to header");
        headers.put(Metadata.Key.of("Authorization",Metadata.ASCII_STRING_MARSHALLER),"Basic Y2xpZW50MTpwYXNzd29yZA==");

        super.start(responseListener, headers);
      }
    };
  }
}
