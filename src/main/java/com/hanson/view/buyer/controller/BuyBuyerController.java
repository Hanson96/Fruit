package com.hanson.view.buyer.controller;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.hanson.core.annotation.Log;
import com.hanson.core.constant.Globals;
import com.hanson.core.exception.BusinessException;
import com.hanson.core.mv.JModelAndView;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.WebFormHelper;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.domain.GoodsItem;
import com.hanson.foundation.domain.OrderForm;
import com.hanson.foundation.domain.SystemLog.LogType;
import com.hanson.foundation.service.IGoodsItemService;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.foundation.service.IOrderFormService;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

@Controller
@RequestMapping("/buyer")
public class BuyBuyerController {

	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsItemService goodsItemService;
	@Autowired
	private IOrderFormService orderFormService;
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/buy_now")
	public ModelAndView buy_now(HttpServletRequest request){
		JModelAndView mv = new JModelAndView("buy.html", 12, request);
		
		return mv;
	}
	
	/**
	 * 购买信息页
	 * @param request
	 * @param type
	 * @param goods_data
	 * @param goods_item_data
	 * @return
	 */
	@RequestMapping("/buy_info")
	public ModelAndView buy_info(HttpServletRequest request, String type, String goods_data, String goods_item_data){
		JModelAndView mv = new JModelAndView("buy_info.html", 12, request);
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		OrderForm order = new OrderForm();
		if(StringUtils.isEmpty(type)){
			throw new BusinessException("参数有误，请重试");
		}else{
			order.setNumber(createOrderNumber(user));
			order.setUser(user);
			order.setConsignee(user.getName());
			order.setPhone(user.getPhone());
			order.setAddress(user.getAddress());
			this.orderFormService.save(order);
			List<GoodsItem> goods_item_list = new ArrayList<GoodsItem>();
			if(StringUtils.equals(type, "goods_buy")){
				// 商品详情页直接购买
				JSONObject g_data = new JSONObject(goods_data);
				Goods goods = this.goodsService.getObjById(Long.valueOf(g_data.getString("goods_id")));
				GoodsItem goods_item = new GoodsItem();
				goods_item.setGoods(goods);
				goods_item.setPrice(goods.getPrice());
				goods_item.setCount(CommUtil.null2Int(g_data.getString("count")));
				goods_item.setOrder(order);
				this.goodsItemService.save(goods_item);
			}else if(StringUtils.equals(type, "goods_item_buy")){
				// 购物车中结算
				JSONObject g_item_data = new JSONObject(goods_item_data);
				String goods_item_ids = g_item_data.getString("goods_item_ids");
				for(String goods_item_id : goods_item_ids.split(",")){
					if(StringUtils.isNotEmpty(goods_item_id)){
						GoodsItem goods_item = this.goodsItemService.getObjById(Long.valueOf(goods_item_id));
						goods_item.setPrice(goods_item.getGoods().getPrice());
						goods_item.setCart(null);
						goods_item.setOrder(order);
						this.goodsItemService.update(goods_item);
					}
				}
			}
		}
		if(order.getId()!=null){
			// 重新查出订单 的商品项 计算总价    不能通过order.getGoods_item_list()获取，因为存在session缓存   查出来的不是最新数据
			List<GoodsItem> goods_item_list = this.goodsItemService.queryByProperties(new String[]{"order.id"}, new Object[]{order.getId()}, -1, -1);
			order.setTotal_price(BigDecimal.valueOf(calculateTotalPrice(goods_item_list)));
			this.orderFormService.update(order);
			mv.addObject("order", order);
		}
		String session_id = UUID.randomUUID().toString();
		WebUtils.setSessionAttribute(request, Globals.SESSION_UUID, session_id);
		mv.addObject("session_id", session_id);
		return mv;
	}
	
	
	/**
	 * 未支付的订单进行支付
	 * @param request
	 * @param obj_id
	 * @return
	 */
	@RequestMapping("/buy_order")
	public ModelAndView buy_order(HttpServletRequest request, String obj_id){
		JModelAndView mv = new JModelAndView("buy_info.html", 12, request);
		if(StringUtils.isEmpty(obj_id)){
			throw new BusinessException("参数有误");
		}else{
			OrderForm order = this.orderFormService.getObjById(Long.valueOf(obj_id));
			if(order.getPay_status() == OrderForm.PayStatus.PAID.value()){
				throw new BusinessException("您的订单已经支付过，不允许再支付");
			}
			order.setTotal_price(BigDecimal.valueOf(calculateTotalPrice(order.getGoods_item_list())));
			this.orderFormService.update(order);
			mv.addObject("order", order);
		}
		String session_id = UUID.randomUUID().toString();
		WebUtils.setSessionAttribute(request, Globals.SESSION_UUID, session_id);
		mv.addObject("session_id", session_id);
		return mv;
	}
	
	@Log(title="买家支付订单", type=LogType.SPECIAL, entityName="OrderForm")
	@ResponseBody
	@RequestMapping("/buy_info_confirm")
	public Map buy_info_confirm(HttpServletRequest request, String session_id, String obj_id, OrderForm order){
		Map data = new HashMap();
		boolean result = false;
		String error_msg = "";
		String log_content = "操作了id为"+obj_id+"的OrderForm对象。"; // 日志内容
		String session_id_true = (String) WebUtils.getSessionAttribute(request, Globals.SESSION_UUID);
		WebUtils.setSessionAttribute(request, Globals.SESSION_UUID, null);
		if(StringUtils.isEmpty(session_id) || !StringUtils.equals(session_id, session_id_true)){
			error_msg = "不允许重复提交表单";
		}
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		OrderForm order_true = StringUtils.isEmpty(obj_id) ? null : this.orderFormService.getObjById(Long.valueOf(obj_id));
		if(order_true==null){
			error_msg = "参数有误";
		}else if(!order_true.getUser().getId().equals(user.getId())){
			error_msg = "此订单不属于你，不允许提交";
		}else{
			order_true = (OrderForm) WebFormHelper.toPo(request, order, order_true, "obj", 0);
			double total_price = calculateTotalPrice(order_true.getGoods_item_list());
			order_true.setTotal_price(BigDecimal.valueOf(total_price));
			order_true.setPay_status(OrderForm.PayStatus.PAID.value());
			order_true.setPay_time(new Date());
			result = this.orderFormService.update(order_true);
		}
		if(result){ // 支付成功  增加商品的销量
			for(GoodsItem goods_item : order_true.getGoods_item_list()){
				Goods goods = goods_item.getGoods();
				if(goods.getActivity_status() == Goods.ActivityStatus.GROUP.value() && goods.getGroup()!=null){
					goods.setGroup_sold_count(goods.getGroup_sold_count() + goods_item.getCount());
				}
				goods.setSales_count(goods.getSales_count() + goods_item.getCount());
				this.goodsService.update(goods);
			}
			MessageFormat.format("<br/>订单编号[{0}]，共{1}种商品，总金额：{2}", order_true.getNumber(),
					order_true.getGoods_item_list().size(), order_true.getTotal_price());
			log_content += "订单编号:。<br/>";
		}
		log_content += "。<br/>";
		data.put("log_content", log_content);
		data.put("result", result);
		data.put("error_msg", error_msg);
		return data;
	}
	
	/**
	 * 计算订单总价
	 * @param goods_item_list
	 * @return
	 */
	private double calculateTotalPrice(List<GoodsItem> goods_item_list){
		double total_price = 0;
		for(GoodsItem goods_item : goods_item_list){
			Goods goods = goods_item.getGoods();
			if(goods==null){
				this.goodsItemService.maintainDelete(goods_item.getId()); // 商品不存在   那么商品项需要移除    不能作为支付
			}else{
				double item_money = CommUtil.mul(goods_item.getPrice(), goods_item.getCount());
				total_price += item_money;
			}
		}
		total_price = CommUtil.formatMoney(total_price); // 只保留两位小数
		return total_price;
	}
	
	/**
	 * 生成订单编号  日期数字+6位用户id
	 * @param user
	 * @return
	 */
	private String createOrderNumber(User user){
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String date_str = df.format(date);
		String u_str = String.format("%06d", user.getId());
		return date_str + u_str;
	}
	
}
