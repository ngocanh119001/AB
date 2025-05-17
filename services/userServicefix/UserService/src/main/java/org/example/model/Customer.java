package org.example.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "customers")
public class Customer {
    @Id
    private String customerId;
    private int soLuongDonHang;
    private int diemTichLuy;
    private List<Address> address;
    private List<Object> hoaDon; // Tạm thời giữ Object nếu chưa có class HoaDon
    private User user;
    private CartDetail cart;
    private BankAccount bank;
}
