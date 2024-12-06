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

import com.cl.entity.SushexinxiEntity;
import com.cl.entity.view.SushexinxiView;

import com.cl.service.SushexinxiService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.CommonUtil;
import java.io.IOException;

/**
 * 宿舍信息
 * 后端接口
 * @author 
 * @email 
 * @date 2024-05-11 23:37:17
 */
@RestController
@RequestMapping("/sushexinxi")
public class SushexinxiController {
    @Autowired
    private SushexinxiService sushexinxiService;



    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,SushexinxiEntity sushexinxi,
		HttpServletRequest request){
        EntityWrapper<SushexinxiEntity> ew = new EntityWrapper<SushexinxiEntity>();

		PageUtils page = sushexinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sushexinxi), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,SushexinxiEntity sushexinxi, 
		HttpServletRequest request){
        EntityWrapper<SushexinxiEntity> ew = new EntityWrapper<SushexinxiEntity>();

		PageUtils page = sushexinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, sushexinxi), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( SushexinxiEntity sushexinxi){
       	EntityWrapper<SushexinxiEntity> ew = new EntityWrapper<SushexinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( sushexinxi, "sushexinxi")); 
        return R.ok().put("data", sushexinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(SushexinxiEntity sushexinxi){
        EntityWrapper< SushexinxiEntity> ew = new EntityWrapper< SushexinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( sushexinxi, "sushexinxi")); 
		SushexinxiView sushexinxiView =  sushexinxiService.selectView(ew);
		return R.ok("查询宿舍信息成功").put("data", sushexinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        SushexinxiEntity sushexinxi = sushexinxiService.selectById(id);
		sushexinxi = sushexinxiService.selectView(new EntityWrapper<SushexinxiEntity>().eq("id", id));
        return R.ok().put("data", sushexinxi);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        SushexinxiEntity sushexinxi = sushexinxiService.selectById(id);
		sushexinxi = sushexinxiService.selectView(new EntityWrapper<SushexinxiEntity>().eq("id", id));
        return R.ok().put("data", sushexinxi);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SushexinxiEntity sushexinxi, HttpServletRequest request){
    	sushexinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(sushexinxi);
        sushexinxiService.insert(sushexinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody SushexinxiEntity sushexinxi, HttpServletRequest request){
    	sushexinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(sushexinxi);
        sushexinxiService.insert(sushexinxi);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody SushexinxiEntity sushexinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(sushexinxi);
        sushexinxiService.updateById(sushexinxi);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        sushexinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








}
