package fit.edu.se.model.statePattern;

import fit.edu.se.model.Order;
import fit.edu.se.model.OrderState;

public class DeliveredState implements State {
	@Override
	public void handleSuccess(Order order) {
		// Logic for handling success in the delivered state
		order.setState(OrderState.SUCCEEDED);
		order.setStateObject(new SucceededState());
	}

	@Override
	public void handleFailure(Order order) {
		order.setState(OrderState.RETURNED);
		order.setStateObject(new ReturnedState());
		order.setEstimatedDeliveryTime(null);
	}

	@Override
	public OrderState getState() {
		return OrderState.DELIVERED;
	}

}
