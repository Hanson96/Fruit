package com.hanson.view.web.controller;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanson.core.mv.JModelAndView;
import com.hanson.core.query.IPageObject;
import com.hanson.core.query.QueryObject;
import com.hanson.core.tools.CommUtil;
import com.hanson.core.tools.QueryHelper;
import com.hanson.core.tools.SimpleMap;
import com.hanson.core.tools.WebViewHelper;
import com.hanson.foundation.domain.Goods;
import com.hanson.foundation.service.IGoodsClassService;
import com.hanson.foundation.service.IGoodsService;
import com.hanson.foundation.service.IGroupService;
import com.hanson.security.domain.User;
import com.hanson.security.service.IUserService;
import com.hanson.security.shiro.ShiroUtils;

/**
 * 商品搜索
 * @author hanson
 *
 */
@Controller
public class SearchController {

	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsClassService goodsClassService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGroupService groupService;
	
	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request, String currentPage, String pageRows,String orderBy, String orderType,
			String q_group_id, String q_price_low, String q_price_high, String q_sort){
		JModelAndView mv = new JModelAndView("search.html", 0, request);
		Map queryMap = QueryHelper.queryParamsIntoModel(request, mv);
		QueryObject qo = new QueryObject(mv, currentPage, pageRows, orderBy, orderType);
		String[] fields = {"name"};
		QueryHelper.addQueryBatch(Goods.class, qo, request, fields, "like");
		String[] fields1 = {"goods_class.id","recommend"};
		QueryHelper.addQueryBatch(Goods.class, qo, request, fields1, "=");
		// 是否是团购活动
		if(StringUtils.isNotEmpty(q_group_id)){
			if(StringUtils.equals(q_group_id, "not_group")){
				qo.addQuery("and obj.group.id is null ", null);
				qo.addQuery("obj.activity_status", new SimpleMap("activity_status", Goods.ActivityStatus.NORMAL.value()), "=");
			}else if(StringUtils.equals(q_group_id, "is_group")){
				qo.addQuery("and obj.group.id is not null ", null);
				qo.addQuery("obj.activity_status", new SimpleMap("activity_status", Goods.ActivityStatus.GROUP.value()), "=");
			}else{
				qo.addQuery("obj.group.id", new SimpleMap("group_id", Long.valueOf(q_group_id)), "=");;
				qo.addQuery("obj.activity_status", new SimpleMap("activity_status", Goods.ActivityStatus.GROUP.value()), "=");
			}
		}
		// 价格范围
		if(StringUtils.isNotEmpty(q_price_low)){
			double price_low = CommUtil.null2Double(q_price_low);
			qo.addQuery("obj.price",new SimpleMap("price_low", BigDecimal.valueOf(price_low)), ">=");
		}
		if(StringUtils.isNotEmpty(q_price_high)){
			double price_high = CommUtil.null2Double(q_price_high);
			qo.addQuery("obj.price",new SimpleMap("price_high", BigDecimal.valueOf(price_high)), "<=");
		}
		// 价格排序
		if(StringUtils.equals(q_sort, "low_to_high")){
			qo.setOrderBy("price");
			qo.setOrderType("asc");
		}else if(StringUtils.equals(q_sort, "high_to_low")){
			qo.setOrderBy("price");
			qo.setOrderType("desc");
		}
		IPageObject pageObj = this.goodsService.list(qo);
		mv.addObject("pageObj", pageObj);
		mv.addObject("queryMap_JSON", new JSONObject(queryMap).toString());
		User user = this.userService.getObjById(ShiroUtils.getUserId());
		mv.addObject("goods_class_list", this.goodsClassService.listAll());
		mv.addObject("group_list", this.groupService.listAll());
		mv.addObject("ActivityStatus", WebViewHelper.enumToMap(Goods.ActivityStatus.values()));
		return mv;
	}
	
}
