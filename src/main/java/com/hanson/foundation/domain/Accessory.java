package com.hanson.foundation.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hanson.core.domain.IdEntity;

/**
 * 附件 实体    图片、文件等
 * @author hanson
 *
 */
@Entity
@Table(name="accessory")
public class Accessory extends IdEntity{
	
	/*** @Fields name : 附件名*/
	private String name;
	/*** @Fields path : 附件保存路径*/
	private String path;
	/*** @Fields size : 附件大小*/
	private float size;
	/*** @Fields width : 宽度*/
	private int width;
	/*** @Fields height : 高度*/
	private int height;
	/*** @Fields ext : 附件类型（jpg等）*/
	private String ext;
	/*** @Fields info : 信息*/
	private String info;
	
	// 商品
	@ManyToOne
	private Goods goods;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	

}
