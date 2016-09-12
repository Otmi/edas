package cn.com.taiji.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends
		JpaRepository<UserAccount, String>, JpaSpecificationExecutor<UserAccount> {
	@Query("SELECT u FROM UserAccount u WHERE u.code = :code")
	public UserAccount findOneWithRoles(@Param("code") String code);
	
	@Query("SELECT u FROM UserAccount u WHERE u.code = :code")
	public UserAccount findByFirstname(@Param("code") String code);
}

