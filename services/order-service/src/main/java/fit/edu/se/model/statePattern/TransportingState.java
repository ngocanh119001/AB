package fit.edu.se.model.statePattern;

import java.time.LocalDateTime;

import fit.edu.se.model.Order;
import fit.edu.se.model.OrderState;

public class TransportingState implements State{

	@Override
	public void handleSuccess(Order order) {
		// TODO Auto-generated method stub
		order.setState(OrderState.DELIVERED);
		order.setStateObject(new DeliveredState());
		order.setEstimatedDeliveryTime(LocalDateTime.now());
	}
	
	@Override
	public void handleFailure(Order order) {
		// TODO Auto-generated method stub
		order.setState(OrderState.RETURNED);
		order.setStateObject(new ReturnedState());
		order.setEstimatedDeliveryTime(null);
	}

	@Override
	public OrderState getState() {
		// TODO Auto-generated method stub
		return OrderState.TRANSPORTING;
	}




}
