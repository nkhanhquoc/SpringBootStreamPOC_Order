package ascendcorp.com.order.mapper;

import ascendcorp.com.order.entity.VerifyOrderEntity;
import ascendcorp.com.order.model.VerifyOrder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class VerifyOrderMapper implements DataMapper<VerifyOrder, VerifyOrderEntity>{

}
