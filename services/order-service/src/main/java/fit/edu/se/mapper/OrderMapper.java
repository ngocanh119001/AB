package fit.edu.se.mapper;

import org.mapstruct.Mapper;

import fit.edu.se.dto.order.OrderConcreteResponseDto;
import fit.edu.se.dto.order.OrderRequestDto;
import fit.edu.se.dto.order.OrderResponseDto;
import fit.edu.se.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	
	Order fromOrderRequestDto(OrderRequestDto orderRequestDto);
	
	OrderRequestDto toOrderRequestDto(Order order);
	
	OrderResponseDto toOrderResponseDto(Order order);
	
	OrderConcreteResponseDto toOrderConcreteResponseDto(Order order);
	
	OrderConcreteResponseDto toOrderConcreteResponseDto(OrderResponseDto orderResponseDto);
}
