package ascendcorp.com.order.mapper;

import ascendcorp.com.order.entity.BaseEntity;
import ascendcorp.com.order.model.BaseModel;
import java.io.Serializable;

public interface DataMapper<D extends BaseModel, E extends BaseEntity> extends Serializable {

  E transform(D object);
  D transform(E object);
}
