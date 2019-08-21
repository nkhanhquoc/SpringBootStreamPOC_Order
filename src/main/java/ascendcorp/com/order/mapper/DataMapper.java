package ascendcorp.com.order.mapper;

import ascendcorp.com.order.entity.OrderEntity;
import ascendcorp.com.order.model.Order;
import java.io.Serializable;

public interface DataMapper<D extends Order, E extends OrderEntity> extends Serializable {

  E transform(D object);
}
