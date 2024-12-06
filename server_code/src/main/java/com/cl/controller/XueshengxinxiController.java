package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;

import com.cl.entity.XueshengxinxiEntity;
import com.cl.entity.view.XueshengxinxiView;

import com.cl.service.XueshengxinxiService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.CommonUtil;
import java.io.IOException;

/**
 * 学生信息
 * 后端接口
 * @author 
 * @email 
 * @date 2024-05-11 23:37:17
 */
@RestController
@RequestMapping("/xueshengxinxi")
public class XueshengxinxiController {
    @Autowired
    private XueshengxinxiService xueshengxinxiService;



    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		XueshengxinxiEntity u = xueshengxinxiService.selectOne(new EntityWrapper<XueshengxinxiEntity>().eq("xuehao", username));
        if(u==null || !u.getMima().equals(password)) {
            return R.error("账号或密码不正确");
        }
		String token = tokenService.generateToken(u.getId(), username,"xueshengxinxi",  "学生信息" );
		return R.ok().put("token", token);
	}


	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody XueshengxinxiEntity xueshengxinxi){
    	//ValidatorUtils.validateEntity(xueshengxinxi);
    	XueshengxinxiEntity u = xueshengxinxiService.selectOne(new EntityWrapper<XueshengxinxiEntity>().eq("xuehao", xueshengxinxi.getXuehao()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		xueshengxinxi.setId(uId);
        xueshengxinxiService.insert(xueshengxinxi);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        XueshengxinxiEntity u = xueshengxinxiService.selectById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	XueshengxinxiEntity u = xueshengxinxiService.selectOne(new EntityWrapper<XueshengxinxiEntity>().eq("xuehao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setMima("123456");
        xueshengxinxiService.updateById(u);
        return R.ok("密码已重置为：123456");
    }


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,XueshengxinxiEntity xueshengxinxi,
		HttpServletRequest request){
        EntityWrapper<XueshengxinxiEntity> ew = new EntityWrapper<XueshengxinxiEntity>();

		PageUtils page = xueshengxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xueshengxinxi), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,XueshengxinxiEntity xueshengxinxi, 
		HttpServletRequest request){
        EntityWrapper<XueshengxinxiEntity> ew = new EntityWrapper<XueshengxinxiEntity>();

		PageUtils page = xueshengxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xueshengxinxi), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( XueshengxinxiEntity xueshengxinxi){
       	EntityWrapper<XueshengxinxiEntity> ew = new EntityWrapper<XueshengxinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( xueshengxinxi, "xueshengxinxi")); 
        return R.ok().put("data", xueshengxinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(XueshengxinxiEntity xueshengxinxi){
        EntityWrapper< XueshengxinxiEntity> ew = new EntityWrapper< XueshengxinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( xueshengxinxi, "xueshengxinxi")); 
		XueshengxinxiView xueshengxinxiView =  xueshengxinxiService.selectView(ew);
		return R.ok("查询学生信息成功").put("data", xueshengxinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        XueshengxinxiEntity xueshengxinxi = xueshengxinxiService.selectById(id);
		xueshengxinxi = xueshengxinxiService.selectView(new EntityWrapper<XueshengxinxiEntity>().eq("id", id));
        return R.ok().put("data", xueshengxinxi);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        XueshengxinxiEntity xueshengxinxi = xueshengxinxiService.selectById(id);
		xueshengxinxi = xueshengxinxiService.selectView(new EntityWrapper<XueshengxinxiEntity>().eq("id", id));
        return R.ok().put("data", xueshengxinxi);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody XueshengxinxiEntity xueshengxinxi, HttpServletRequest request){
        if(xueshengxinxiService.selectCount(new EntityWrapper<XueshengxinxiEntity>().eq("xuehao", xueshengxinxi.getXuehao()))>0) {
            return R.error("学号已存在");
        }
    	xueshengxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(xueshengxinxi);
    	XueshengxinxiEntity u = xueshengxinxiService.selectOne(new EntityWrapper<XueshengxinxiEntity>().eq("xuehao", xueshengxinxi.getXuehao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		xueshengxinxi.setId(new Date().getTime());
        xueshengxinxiService.insert(xueshengxinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody XueshengxinxiEntity xueshengxinxi, HttpServletRequest request){
        if(xueshengxinxiService.selectCount(new EntityWrapper<XueshengxinxiEntity>().eq("xuehao", xueshengxinxi.getXuehao()))>0) {
            return R.error("学号已存在");
        }
    	xueshengxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(xueshengxinxi);
    	XueshengxinxiEntity u = xueshengxinxiService.selectOne(new EntityWrapper<XueshengxinxiEntity>().eq("xuehao", xueshengxinxi.getXuehao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		xueshengxinxi.setId(new Date().getTime());
        xueshengxinxiService.insert(xueshengxinxi);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody XueshengxinxiEntity xueshengxinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(xueshengxinxi);
        xueshengxinxiService.updateById(xueshengxinxi);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        xueshengxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








}
