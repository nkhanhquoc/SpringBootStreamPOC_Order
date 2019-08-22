package ascendcorp.com.order.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InternalServerMessage implements ApplicationMessage {
  INTERNAL_SERVER_ERROR("system_error", "System error"),
  MIGRATION_PROFILE_ERROR("migration_profile_error", "Cannot migration profile error"),
  MIGRATION_SOF_ERROR("migration_sof_error", "Cannot migration Sof error"),
  MIGRATION_SOF_LIMITATION_ERROR("migration_sof_limitation_error",
      "Cannot migration Sof limitation error"),
  MIGRATION_HIERARCHY_ERROR("migration_hierarchy_error", "Cannot migration sale hierarchy error"),
  MIGRATION_IDENTITY_ERROR("migration_identity_error", "Migration Agent Identity Error"),
  MIGRATION_HIERARCHY_CONNECTION_ERROR("migration_hierarchy_connection_error",
      "Cannot migration hierarchy connection error"),
  MIGRATION_SALE_DEVICE_ERROR("migration_sale_device_error", "Cannot migration sale device error");

  private String message;
  private String description;

  @Override
  public int getStatusCode() {
    return 500;
  }

  @Override
  public String getKey() {
    return this.name();
  }
}
