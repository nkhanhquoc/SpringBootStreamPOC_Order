package ascendcorp.com.order.service.grpc.credentials;

import ascendcorp.com.order.constant.JwtConstants;
import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import java.util.concurrent.Executor;

public class JwtCallCredential extends CallCredentials {

  private String value;

  public JwtCallCredential(String value) {
    this.value = value;
  }

  @Override
  public void applyRequestMetadata(RequestInfo requestInfo, Executor executor,
      MetadataApplier metadataApplier) {
    executor.execute(() -> {
      try {
        Metadata meta = new Metadata();
        meta.put(JwtConstants.AUTHORIZATION_METADATA_KEY,
            String.format("%s %s", JwtConstants.BEARER_TYPE, value));
        metadataApplier.apply(meta);
      } catch (Throwable e) {
        metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
      }
    });

  }

  @Override
  public void thisUsesUnstableApi() {

  }
}
