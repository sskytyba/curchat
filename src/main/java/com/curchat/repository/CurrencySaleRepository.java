package com.curchat.repository;

import com.curchat.model.CurrencySale;
import com.curchat.model.UserAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CurrencySaleRepository extends CrudRepository<CurrencySale, Long> {

    @Modifying
    @Query("UPDATE CurrencySale c " +
            "SET c.buyer = :buyer, c.status = com.curchat.model.CurrencySaleStatus.SOLD " +
            "WHERE c.id = :id and c.status = com.curchat.model.CurrencySaleStatus.NEW")
    int sale(@Param("buyer") UserAccount buyer, @Param("id") Long id);

    @Modifying
    @Query("UPDATE CurrencySale c " +
            "SET c.seller = :seller, c.status = com.curchat.model.CurrencySaleStatus.SOLD " +
            "WHERE c.id = :id and c.status = com.curchat.model.CurrencySaleStatus.NEW")
    int purchase(@Param("seller") UserAccount seller, @Param("id") Long id);
}
