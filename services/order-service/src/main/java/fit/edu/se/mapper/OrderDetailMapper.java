package fit.edu.se.mapper;

import org.mapstruct.Mapper;

import fit.edu.se.dto.order.OrderDetailRequestDto;
import fit.edu.se.dto.order.OrderDetailResponseDto;
import fit.edu.se.model.OrderDetail;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
	OrderDetail fromOrderDetailRequestDto(OrderDetailRequestDto orderDetailRequestDto);
	OrderDetailResponseDto toOrderDetailResponseDto(OrderDetail orderDetail);
}
