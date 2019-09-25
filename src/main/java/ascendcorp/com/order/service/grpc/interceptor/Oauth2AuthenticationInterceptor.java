package ascendcorp.com.order.service.grpc.interceptor;

import static com.google.common.base.Strings.nullToEmpty;

import ascendcorp.com.order.logger.Logger;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.Objects;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@GRpcGlobalInterceptor
public class Oauth2AuthenticationInterceptor implements ServerInterceptor {

  private static final Logger log = Logger.getInstance(Oauth2AuthenticationInterceptor.class);
  private final ResourceServerTokenServices tokenServices;

  @Autowired
  public Oauth2AuthenticationInterceptor(
      ResourceServerTokenServices tokenServices) {
    this.tokenServices = tokenServices;
  }

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
    String authHeader = nullToEmpty(metadata.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)));
    if (!(authHeader.startsWith("Bearer ") || authHeader.startsWith("bearer "))) {
      return serverCallHandler.startCall(serverCall, metadata);
    }

    try {
      String token = authHeader.substring(7);

      log.info("Bearer Token Authorization header found");

      if (authenticationIsRequired()) {
        Authentication authResult = tokenServices.loadAuthentication(token);

        log.info("Authentication success: {}", authResult);

        SecurityContextHolder.getContext().setAuthentication(authResult);
      }
    } catch (AuthenticationException | OAuth2Exception e) {
      SecurityContextHolder.clearContext();

      log.error("Authentication request failed: {}", e.getMessage());

      throw Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e).asRuntimeException();
    }

    return serverCallHandler.startCall(serverCall, metadata);
  }

  private boolean authenticationIsRequired() {
    Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(existingAuth) || !existingAuth.isAuthenticated()) {
      return true;
    }

    if (existingAuth instanceof AnonymousAuthenticationToken) {
      return true;
    }

    return false;
  }
}
