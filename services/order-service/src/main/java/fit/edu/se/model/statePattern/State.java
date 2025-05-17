package fit.edu.se.model.statePattern;

import fit.edu.se.model.Order;
import fit.edu.se.model.OrderState;

public interface State {
	void handleSuccess(Order order);
	void handleFailure(Order order);
	
	OrderState getState();
}
 