/**
 * 
 */
package com.bj.controller;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bj.dao.mapper.TRegisterLogMapper;
import com.bj.dao.mapper.TRegisterMapper;
import com.bj.dao.mapper.TUserMapper;
import com.bj.pojo.TRegister;
import com.bj.pojo.TRegisterLog;
import com.bj.pojo.TUser;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.Pagination;

/**
 * @author LQK
 *
 */
@Controller
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	private TUserMapper tUserMapper;
	
	@Resource
	private TRegisterMapper tRegisterMapper;
	
	@Resource
	private TRegisterLogMapper tRegisterLogMapper;
    
    @GetMapping({"", "/", "/login"})
    public String goLogin(HttpServletRequest request) {
    	if(request.getSession().getAttribute(Contants.SESSION_COMMON_KEY) != null) {
        	return "redirect:/stat";
    	}else {
            return "login";
    	}
    }

    @PostMapping("/login")
    public String doLogin(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "vrifyCode") String vrifyCode) {
    	String captchaId = (String) request.getSession().getAttribute("vrifyCode");    
        LOGGER.info("vrifyCode: {}/{}", vrifyCode, captchaId);
        if(captchaId != null && captchaId.equals(vrifyCode)) {  
        	TUser tUser = tUserMapper.findByUsername(username);
        	if(tUser != null && BaseUtil.md5(password).equals(tUser.getPassword())){
                LOGGER.info("[{}]成功登录",username);
        		request.getSession().setAttribute(Contants.SESSION_COMMON_KEY, tUser);
        		request.getSession().setAttribute("vrifyCode","");  
            	return "redirect:/stat";
        	}else {
            	model.put("hasError", true);
            	model.put("message", "用户名或密码错误"); 
            	return "login";
        	}
        }else{
        	model.put("hasError", true);
        	model.put("message", "错误的验证码"); 
        	return "login";
        }
    }

    @GetMapping("/logout")
    public String doLogout(Map<String, Object> model,
            HttpServletRequest request) {
		request.getSession().removeAttribute(Contants.SESSION_COMMON_KEY);
    	return "login";
    }

	
    @GetMapping({"/register","/register/list"})
    public String goRegisterList1(HttpServletRequest request) {
        return "redirect:/register/list/1";
    }
    
    @GetMapping("/register/list/{userType}")
    public String goRegisterList(Map<String, Object> model,
            HttpServletRequest request,
            @PathVariable String userType,
            @RequestParam(value = "filter", defaultValue = "") String filter,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = tRegisterMapper.countAll(filter, userType);
    	List<TRegister> registers = tRegisterMapper.findAll(filter, userType, (page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("registers", registers);
        model.put("pagination", pagination);
        model.put("filter", filter);
        model.put("active"+userType, "active");
        model.put("userType", userType);
        if("1".equals(userType)) {
            model.put("typeName", "个人");
        }else if("2".equals(userType)) {
            model.put("typeName", "企业");
        }else if("3".equals(userType)) {
            model.put("typeName", "试用");
        }
        return "register/list";
    }
    
    @GetMapping("/register/log")
    public String goLog(Map<String, Object> model,
    		HttpServletRequest request,
    		@RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = tRegisterLogMapper.countAll();
    	List<TRegisterLog> tRegisterLogs = tRegisterLogMapper.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("tRegisterLogs", tRegisterLogs);
        model.put("pagination", pagination);
        return "register/log";
    }

    @GetMapping("/stat")
    public String goStat(Map<String, Object> model,
    		HttpServletRequest request,
    		@RequestParam(value = "p", defaultValue = "0") int page) {
    	//详细列表数据
    	int tab = 1;
    	if(page > 0) {
    		tab = 3;
    	}else {
    		page = 1;
    	}
    	int customPageSize = 8;
    	int count = tRegisterLogMapper.countAll();
    	List<TRegisterLog> tRegisterLogs = tRegisterLogMapper.findAll((page - 1) * customPageSize, customPageSize);
        Pagination pagination = new Pagination(request, page, count, customPageSize);
        model.put("tRegisterLogs", tRegisterLogs);
        model.put("pagination", pagination);
        
        //数字统计数据
        int count1 = tRegisterLogMapper.countByUserType(1);
        int count2 = tRegisterLogMapper.countByUserType(2);
        int count3 = tRegisterLogMapper.countByUserType(3);
        model.put("count", count);
        model.put("count1", count1);
        model.put("count2", count2);
        model.put("count3", count3);
        NumberFormat numberFormat = NumberFormat.getInstance();  
        numberFormat.setMaximumFractionDigits(2);   
        String count1P = numberFormat.format((float)count1/(float)count*100);
        String count2P = numberFormat.format((float)count2/(float)count*100);
        String count3P = numberFormat.format((float)count3/(float)count*100);
        model.put("count1P", count1P);
        model.put("count2P", count2P);
        model.put("count3P", count3P);
        
        //地图数据
        List<TRegisterLog> tCityLogs = tRegisterLogMapper.findGroupByCity();
        StringBuffer mapData = new StringBuffer();
        for(int i=0; i<tCityLogs.size(); i++) {
        	TRegisterLog log = tCityLogs.get(i);
        	if(log.getCity() == null || "".equals(log.getCity().trim())) {
        		continue;
        	}
        	//{name:"厦门",value:[118.1,24.46,183]},{name:"武汉",value:[114.31,30.52,199]}
        	mapData.append("{name: '").append(log.getCity()).append("', value: [").append(log.getX()).append(",").append(log.getY()).append(",").append(log.getCount()).append("]},");
        }
        if(mapData.length() > 1)mapData = new StringBuffer(mapData.substring(0, mapData.length()-1));
        model.put("mapData", mapData);
        
        //近期半年走势数据,barDataX,barDataY1,barDataY2
        StringBuffer barDataX = new StringBuffer();
        StringBuffer barDataY1 = new StringBuffer();
        StringBuffer barDataY2 = new StringBuffer();
        for(int i=5; i>=0; i--) {
            Calendar now = Calendar.getInstance();
        	now.add(Calendar.MONTH, -i);
        	int month = now.get(Calendar.MONTH) + 1;
        	barDataX.append("'").append(month).append("月',");
        	now.set(Calendar.DAY_OF_MONTH, 1);
        	now.set(Calendar.HOUR_OF_DAY, 0);
        	now.set(Calendar.MINUTE, 0);
        	now.set(Calendar.SECOND, 0);
        	now.set(Calendar.MILLISECOND, 0);
        	Calendar eTime = Calendar.getInstance();
        	eTime.setTime(now.getTime());
        	eTime.add(Calendar.MONTH, +1);
        	int sbNum = tRegisterLogMapper.countGroupByMacCpuByTime(now.getTime(), eTime.getTime()).size();
        	int zcNum = tRegisterLogMapper.countByTime(now.getTime(), eTime.getTime());
        	barDataY1.append(sbNum).append(",");
        	barDataY2.append(zcNum).append(",");
        }
        if(barDataX.length() > 1)barDataX = new StringBuffer(barDataX.substring(0, barDataX.length()-1));
        if(barDataY1.length() > 1)barDataY1 = new StringBuffer(barDataY1.substring(0, barDataY1.length()-1));
        if(barDataY2.length() > 1)barDataY2 = new StringBuffer(barDataY2.substring(0, barDataY2.length()-1));
        model.put("barDataX", barDataX);
        model.put("barDataY1", barDataY1);
        model.put("barDataY2", barDataY2);
        
        //省份统计
        List<TRegisterLog> tProvinceLogs = tRegisterLogMapper.findGroupByProvince();
        StringBuffer pieData = new StringBuffer();
        for(int i=0; i<tProvinceLogs.size(); i++) {
        	TRegisterLog log = tProvinceLogs.get(i);
        	if(log.getProvince() == null || "".equals(log.getProvince().trim())) {
        		continue;
        	}
        	pieData.append("{name: '").append(log.getProvince()).append("', value: ").append(log.getCount()).append("},");
        }
        if(pieData.length() > 1)pieData = new StringBuffer(pieData.substring(0, pieData.length()-1));
        model.put("pieData", pieData);
        model.put("tab", tab);
        return "stat";
    }
}
