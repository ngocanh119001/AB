package fit.edu.se.repo;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import fit.edu.se.model.Address;

public interface AddressRepository extends R2dbcRepository<Address, String> {

}
