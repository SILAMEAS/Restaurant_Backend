package com.sila.repository;

import com.sila.model.Address;
import com.sila.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Address add SET add.currentUsage = :status WHERE add.id = :addressId")
    void updateAddressCurrentUsageMatch(Long addressId, boolean status);

    @Modifying
    @Transactional
    @Query("UPDATE Address add SET add.currentUsage = :status WHERE add.user.id =:userId")
    void updateAddressCurrentUsageMisMatch(Long userId, boolean status);

    Boolean existsAddressByUser(User user);
}
