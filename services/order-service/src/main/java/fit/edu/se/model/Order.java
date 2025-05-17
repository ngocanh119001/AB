package fit.edu.se.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import fit.edu.se.model.statePattern.DeliveredState;
import fit.edu.se.model.statePattern.PendingState;
import fit.edu.se.model.statePattern.ReturnedState;
import fit.edu.se.model.statePattern.State;
import fit.edu.se.model.statePattern.SucceededState;
import fit.edu.se.model.statePattern.TransportingState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order implements Persistable<String> {
	@Id
	String orderId;
	String customerId;
	String vendorId;
	String notes;
	LocalDateTime estimatedDeliveryTime;
	String addressId;
	OrderState orderState;
	Integer totalPrice;
	@Transient
	transient State stateObject;
	@Transient
	transient List<OrderDetail> orderDetails;
	String voucherId;
	
	@Transient
    boolean isNew = false;

	
	@CreatedDate
	@Column("created_at")
	LocalDateTime createdAt;
	
	@LastModifiedDate
	@Column("updated_at")
	LocalDateTime updatedAt;
	
	public void setState(OrderState state) {
        this.orderState = state;
        this.stateObject = createStateObject(state);
    }

    public void setState(State stateObject) {
        this.stateObject = stateObject;
        this.orderState = stateObject.getState();
    }

    public State getStateObject() {
        if (stateObject == null) {
            this.stateObject = createStateObject(orderState);
        }
        return stateObject;
    }
	
	private State createStateObject(OrderState state) {
        switch (state) {
            case PENDING:
                return new PendingState();
            case TRANSPORTING:
                return new TransportingState();
            case DELIVERED:
                return new DeliveredState();
            case SUCCEEDED:
                return new SucceededState();
            case RETURNED:
            	return new ReturnedState();
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
	

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return orderId;
	}
	
}
