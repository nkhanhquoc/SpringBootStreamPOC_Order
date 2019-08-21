package ascendcorp.com.order.mapper;

import ascendcorp.com.order.entity.VerifyOrderEntity;
import ascendcorp.com.order.model.VerifyOrder;
import java.io.Serializable;

public interface DataMapper<D extends VerifyOrder, E extends VerifyOrderEntity> extends Serializable {

  E transform(D object);
}
