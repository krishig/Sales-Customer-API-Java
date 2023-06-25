package com.KrishiG.repositories;

import com.KrishiG.enitites.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<CustomerAddress, Long> {
}
