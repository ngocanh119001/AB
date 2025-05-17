package iuh.fit.se.model;

import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TypeAlias("VENDOR") // Quan trọng!
@SuperBuilder
@ToString(callSuper = true)
public class Vendor extends Customer {

    private String shopName;
    private String shopDescription;
}