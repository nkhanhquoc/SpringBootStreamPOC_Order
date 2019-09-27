package ascendcorp.com.order.service.grpc.interceptor;

import static com.google.common.base.Strings.nullToEmpty;

import ascendcorp.com.order.constant.JwtConstants;
import ascendcorp.com.order.logger.Logger;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.util.List;
import java.util.stream.Collectors;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@GRpcGlobalInterceptor
public class JwtAuthenticationInterceptor implements ServerInterceptor {

  private JwtParser parser = Jwts.parser().setSigningKey(JwtConstants.JWT_SIGNING_KEY);

  private static final Logger log = Logger.getInstance(JwtAuthenticationInterceptor.class);

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
      Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

    String authHeader = nullToEmpty(
        metadata.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)));
    if (!(authHeader.startsWith("Jwt ") || authHeader.startsWith("jwt "))) {
      return serverCallHandler.startCall(serverCall, metadata);
    }
    try {
      String token = authHeader.substring(4).trim();
      log.info("JWT Token found: {}", token);
      Jws<Claims> claims = parser.parseClaimsJws(token);
      if (claims.getBody().getSubject() != null) {

        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.getBody().get("authorities");

        Authentication auth = new UsernamePasswordAuthenticationToken(
            claims.getBody().getSubject(),
            null,
            authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (SignatureException je) {
      log.info("SignatureException thrown: {}", je.getMessage());
      throw Status.UNAUTHENTICATED.withDescription(je.getMessage()).withCause(je)
          .asRuntimeException();
    } catch (AuthenticationException e) {
      SecurityContextHolder.clearContext();
      log.info("Authentication request failed: {}", e.getMessage());
      throw Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e)
          .asRuntimeException();
    } catch (Exception ex) {
      log.info("exception: ");
      throw Status.UNAUTHENTICATED.withDescription(ex.getMessage()).withCause(ex)
          .asRuntimeException();
    }
    return serverCallHandler.startCall(serverCall, metadata);
  }
}
