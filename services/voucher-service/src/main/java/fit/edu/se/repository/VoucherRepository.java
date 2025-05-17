package fit.edu.se.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import fit.edu.se.entity.Voucher;

public interface VoucherRepository extends MongoRepository<Voucher, String> {

	Page<Voucher> findByVendorId(String vendorId, Pageable pageable);
	
	List<Voucher> findByVendorId(String vendorId);
}
