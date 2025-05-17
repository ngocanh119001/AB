package fit.edu.se.model.statePattern;

import fit.edu.se.model.Order;
import fit.edu.se.model.OrderState;
import lombok.AllArgsConstructor;

public class ReturnedState implements State {
	
	
	@Override
	public void handleSuccess(Order order) {
		// Logic for handling success in the delivered state
		System.out.println("Order is already delivered. No further action needed.");
	}

	@Override
	public void handleFailure(Order order) {
		// Logic for handling failure in the delivered state
		System.out.println("Order is already delivered. Cannot process failure.");
	}

	@Override
	public OrderState getState() {
		return OrderState.RETURNED;
	}

}
