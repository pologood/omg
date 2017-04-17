package com.omg.xxx.dal.dao;


import com.omg.xxx.dal.model.User;
import tk.mybatis.mapper.common.Mapper;

/**
 * 仅供参考
 * 
 * @author Anders
 * 
 */
public interface UserMapper extends Mapper<User>{

	User selectById(Long id);
	
	void deleteById(Long id);
}
