package com.omg.xxx.dal.model;

import javax.persistence.*;

/**
 * 仅供参考
 * 
 * @author wenjing
 * 
 */

@Table(name = "t_xxx_user")
public class User  {

	@Id
	@GeneratedValue(generator = "JDBC")
	private Long id;

	private String userId;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
