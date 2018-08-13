package com.curchat.repository;

import com.curchat.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByName(String name);
}
