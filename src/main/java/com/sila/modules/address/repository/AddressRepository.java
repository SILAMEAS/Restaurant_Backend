package com.sila.modules.address.repository;

import com.sila.modules.address.model.Address;
import com.sila.modules.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    List<Address> findAllByUser(User user);
}
