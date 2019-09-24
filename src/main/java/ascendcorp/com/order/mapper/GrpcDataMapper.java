package ascendcorp.com.order.mapper;

import ascendcorp.com.order.GrpcOrder;
import ascendcorp.com.order.entity.OrderEntity;

public interface GrpcDataMapper <D extends GrpcOrder, E extends OrderEntity>{

  E transform(D object);
  D transform(E object);
}
