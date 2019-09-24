package ascendcorp.com.order.service.grpc.interceptor;

import ascendcorp.com.order.logger.Logger;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;


@GRpcGlobalInterceptor
@Order(10)
public class SecurityContextPersistenceInterceptor implements ServerInterceptor {

  private static Logger log = Logger.getInstance(SecurityContextPersistenceInterceptor.class);


  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
    try{
      ServerCall.Listener<ReqT> delegate = serverCallHandler.startCall(serverCall, metadata);
      return new SimpleForwardingServerCallListener<ReqT>(delegate) {
        @Override
        public void onComplete() {
          try {
            super.onComplete();
          } finally {
            SecurityContextHolder.clearContext();
            log.info("SecurityContextHolder now cleared, as request processing completed");
          }
        }

        @Override
        public void onCancel() {
          try {
            super.onCancel();
          } finally {
            SecurityContextHolder.clearContext();
            log.error("SecurityContextHolder now cleared, as request processing was canceled");
          }
        }
      };
    } catch(Throwable t){
      SecurityContextHolder.clearContext();
      throw t;
    }
  }
}
