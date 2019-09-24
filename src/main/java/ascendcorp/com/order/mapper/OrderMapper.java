package ascendcorp.com.order.mapper;

import ascendcorp.com.order.entity.OrderEntity;
import ascendcorp.com.order.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class OrderMapper implements DataMapper<Order, OrderEntity>{

}
