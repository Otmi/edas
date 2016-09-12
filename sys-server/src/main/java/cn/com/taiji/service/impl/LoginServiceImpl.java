package cn.com.taiji.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.taiji.LoginService;
import cn.com.taiji.domain.UserAccount;
import cn.com.taiji.domain.UserAccountRepository;
@Service
public class LoginServiceImpl implements  LoginService {
	@Autowired
	UserAccountRepository repository;
	@Override
	public String getToken(String username, String passwor) {
		UserAccount useraccount=	repository.findByFirstname(username);
		return useraccount.getName();
	}

}
