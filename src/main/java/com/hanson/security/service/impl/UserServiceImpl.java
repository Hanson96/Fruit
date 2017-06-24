package com.hanson.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanson.core.query.QueryObject;
import com.hanson.core.service.impl.CommonServiceImpl;
import com.hanson.core.tools.SimpleMap;
import com.hanson.foundation.domain.Cart;
import com.hanson.foundation.domain.OrderForm;
import com.hanson.foundation.domain.SystemLog;
import com.hanson.foundation.service.ICartService;
import com.hanson.foundation.service.IOrderFormService;
import com.hanson.foundation.service.ISystemLogService;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;

@Service
public class UserServiceImpl extends CommonServiceImpl<User> implements IUserService{

	@Autowired
	private ICartService CartService;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private ISystemLogService systemLogService;
	
	@Override
	public boolean cascadeDelete(Long id) {
		User user = this.getObjById(id);
		// 删除用户的购物车
		for(Cart cart : user.getCart_list()){
			this.CartService.maintainDelete(cart.getId());
		}
		// 取消订单关联
		for(OrderForm order : user.getOrder_list()){
			order.setUser(null);
			this.orderFormService.update(order);
		}
		// 取消日志关联
		QueryObject qo = new QueryObject();
		qo.addQuery("obj.user.id", new SimpleMap("user_id", user.getId()), "=");
		for(SystemLog log : this.systemLogService.query(qo)){
			log.setUser(null);
			this.systemLogService.update(log);
		}
		return this.delete(id);
	}
	
	@Override
	public boolean maintainDelete(Long id) {
		boolean result = cascadeDelete(id);
		return result;
	}
}
