package fit.edu.se.model.statePattern;

import fit.edu.se.model.Order;
import fit.edu.se.model.OrderState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SucceededState implements State{

	@Override
	public void handleSuccess(Order order) {
		// TODO Auto-generated method stub
		log.warn("Order is already succeeded. No further action needed.");
		
	}
	
	@Override
	public void handleFailure(Order order) {
		// TODO Auto-generated method stub
		log.warn("Order is already succeeded. Cannot process failure.");
	}

	@Override
	public OrderState getState() {
		// TODO Auto-generated method stub
		return OrderState.SUCCEEDED;
	}




}
