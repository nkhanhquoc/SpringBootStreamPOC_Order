package ascendcorp.com.order.service.grpc.interceptor;

import ascendcorp.com.order.logger.Logger;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.Objects;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.ThrowableAnalyzer;

@GRpcGlobalInterceptor
public class ExceptionTranslationInterceptor implements ServerInterceptor {

  private static final Logger log = Logger.getInstance(ExceptionTranslationInterceptor.class);
  private ThrowableAnalyzer throwableAnalyzer = new ThrowableAnalyzer();
  private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

    ServerCall.Listener<ReqT> delegate = serverCallHandler.startCall(serverCall, metadata);
    return new SimpleForwardingServerCallListener<ReqT>(delegate) {
      @Override
      public void onHalfClose() {
        try {
          super.onHalfClose();
          log.info("Chain processed normally");
        } catch (Exception e) {
          Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
          AuthenticationException authenticationException = (AuthenticationException) throwableAnalyzer
              .getFirstThrowableOfType(AuthenticationException.class, causeChain);

          if (Objects.nonNull(authenticationException)) {
            handleAuthenticationException(authenticationException);
          } else {
            AccessDeniedException accessDeniedException = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);

            if (Objects.nonNull(accessDeniedException)) {
              handleAccessDeniedException(accessDeniedException);
            } else {
              throw e;
            }
          }
        }
      }

      private void handleAuthenticationException(AuthenticationException exception) {
        log.info("Authentication exception occurred, closing call with UNAUTHENTICATED", exception);
        serverCall.close(Status.UNAUTHENTICATED.withDescription(exception.getMessage())
            .withCause(exception), new Metadata());
      }

      private void handleAccessDeniedException(AccessDeniedException exception) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authenticationTrustResolver.isAnonymous(authentication)) {
          log.info("Access is denied (user is anonymous), closing call with UNAUTHENTICATED", exception);
          serverCall.close(Status.UNAUTHENTICATED.withDescription("Authentication is required to access this resource")
              .withCause(exception), new Metadata());
        } else {
          log.info("Access is denied (user is not anonymous), closing call with PERMISSION_DENIED", exception);
          serverCall.close(Status.PERMISSION_DENIED.withDescription(exception.getMessage())
              .withCause(exception), new Metadata());
        }
      }
    };
  }
}
