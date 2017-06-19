package com.hanson.security.service.impl;

import org.springframework.stereotype.Service;

import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;

@Service
public class UserServiceImpl extends CommonServiceImpl<User> implements IUserService{

}
