package ascendcorp.com.order.service.grpc.interceptor;

import static com.google.common.base.Strings.nullToEmpty;

import ascendcorp.com.order.logger.Logger;
import ascendcorp.com.order.service.stream.OrderListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@GRpcGlobalInterceptor
public class BasicInterceptor implements ServerInterceptor {

private final AuthenticationManager authenticationManager;
  private static final Logger log = Logger.getInstance(OrderListener.class);

  @Autowired
  public BasicInterceptor(
      AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
    String authHeader = nullToEmpty(metadata.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)));
    if (!authHeader.startsWith("Basic ")) {
      return serverCallHandler.startCall(serverCall, metadata);
    }

    try {
      String[] tokens = decodeBasicAuth(authHeader);
      String username = tokens[0];

      log.error("Basic Authentication Authorization header found for user: {}", username);
      log.error("Basic Password Authentication Authorization header found for user: {}", tokens[1]);

      if (authenticationIsRequired(username)) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(username, tokens[1]);
        Authentication authResult = authenticationManager.authenticate(authRequest);

        log.error("Authentication success: {}", authResult);

        SecurityContextHolder.getContext().setAuthentication(authResult);
      }
    } catch (AuthenticationException e) {
      SecurityContextHolder.clearContext();

      log.error("Authentication request failed: {}", e.getMessage());

      throw Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e).asRuntimeException();
    }

    return serverCallHandler.startCall(serverCall, metadata);
  }

  private String[] decodeBasicAuth(String authHeader) {
    String basicAuth;
    try {
      basicAuth = new String(Base64.getDecoder().decode(authHeader.substring(6).getBytes(
          StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
      throw new BadCredentialsException("Failed to decode basic authentication token");
    }

    int delim = basicAuth.indexOf(":");
    if (delim == -1) {
      throw new BadCredentialsException("Failed to decode basic authentication token");
    }

    return new String[] { basicAuth.substring(0, delim), basicAuth.substring(delim + 1) };
  }

  private boolean authenticationIsRequired(String username) {
    Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(existingAuth) || !existingAuth.isAuthenticated()) {
      return true;
    }

    if (existingAuth instanceof UsernamePasswordAuthenticationToken
        && !existingAuth.getName().equals(username)) {
      return true;
    }

    if (existingAuth instanceof AnonymousAuthenticationToken) {
      return true;
    }

    return false;
  }

}
