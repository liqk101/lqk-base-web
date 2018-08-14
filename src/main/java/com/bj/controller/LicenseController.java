/**
 * 
 */
package com.bj.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.dao.mapper.TLicenseMapper;
import com.bj.dao.mapper.TRegisterMapper;
import com.bj.pojo.TLicense;
import com.bj.util.AESUtil;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.Pagination;

/**
 * @author LQK
 *
 */
@Controller
@RequestMapping("/license")
public class LicenseController {
	@Resource
	private TLicenseMapper tLicenseMapper;
	@Resource
	private TRegisterMapper tRegisterMapper;

    @GetMapping({"", "/", "/list"})
    public String goLicense1(HttpServletRequest request) {
        return "redirect:/license/list/1";
    }

    @GetMapping("/list/{userType}")
    public String goLicense(Map<String, Object> model,
            HttpServletRequest request,
            @PathVariable String userType,
            @RequestParam(value = "filter", defaultValue = "") String filter,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = tLicenseMapper.countAll(filter, userType);
    	List<TLicense> licenses = tLicenseMapper.findAll(filter, userType, (page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
    	if(licenses != null) {
    		for(int i=0; i<licenses.size(); i++) {
    			TLicense tLicense = licenses.get(i);
    			tLicense.setRegDeviceNum(tRegisterMapper.countByLicId(tLicense.getId()));
    		}
    	}
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("licenses", licenses);
        model.put("pagination", pagination);
        model.put("filter", filter);
        model.put("userType", userType);
        model.put("active"+userType, "active");
        if("1".equals(userType)) {
            model.put("typeName", "个人");
        }else if("2".equals(userType)) {
            model.put("typeName", "企业");
        }else if("3".equals(userType)) {
            model.put("typeName", "试用");
        }
        return "/license/list";
    }

    @GetMapping("/new/{userType}")
    public String goNewLicense(Map<String, Object> model,
            @PathVariable String userType,
            HttpServletRequest request) {
        model.put("userType", userType);
        model.put("active"+userType, "active");
        if("1".equals(userType)) {
            model.put("typeName", "个人");
        }else if("2".equals(userType)) {
            model.put("typeName", "企业");
        }else if("3".equals(userType)) {
            model.put("typeName", "试用");
        }
        return "/license/new";
    }

    @Transactional
    @PostMapping("/new")
    public String doNewLicense(TLicense tLicense,
    		Map<String, Object> model,
			final RedirectAttributes redirectAttributes) {
    	//user code
    	String userCode = BaseUtil.getStrRandom(Contants.USER_CODE_LENGTH);
    	tLicense.setUserCode(userCode);
    	String productId = tLicense.getVersion()+tLicense.getUserType()+tLicense.getUserCode()+tLicense.getLicType()+tLicense.getLicNum();
    	String aesEncode = AESUtil.encrypt(productId, tLicense.getAesKey());
    	tLicense.setAesEncode(aesEncode);
    	if(tLicenseMapper.insert(tLicense) > 0) {
        	redirectAttributes.addFlashAttribute("message", "保存成功");
            return "redirect:/license/list/" + tLicense.getUserType();
    	}else {
            model.put("hasError", true);
            model.put("message", "保存失败");
            return "/license/new";
    	}
    }

    @GetMapping("/{id}/edit")
    public String goEditLicense(Map<String, Object> model,
			@PathVariable("id") int id,
			final RedirectAttributes redirectAttributes) {
    	TLicense tLicense = tLicenseMapper.findById(id);
    	if(tLicense != null) {
            model.put("tLicense", tLicense);
    	}else {
    		redirectAttributes.addFlashAttribute("hasError", true);
    		redirectAttributes.addFlashAttribute("message", "记录已经不存在");
            return "redirect:/license/list";
    	}
    	int userType = tLicense.getUserType();
        model.put("userType", userType);
        model.put("active"+userType, "active");
        if(userType == 1) {
            model.put("typeName", "个人");
        }else if(userType == 2) {
            model.put("typeName", "企业");
        }else if(userType == 3) {
            model.put("typeName", "试用");
        }
        return "/license/edit";
    }

    @Transactional
    @PostMapping("/{id}/edit")
    public String doEditLicense(@Valid TLicense tLicense,
    							Map<String, Object> model,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	String productId = tLicense.getVersion()+tLicense.getUserType()+tLicense.getUserCode()+tLicense.getLicType()+tLicense.getLicNum();
    	String aesEncode = AESUtil.encrypt(productId, tLicense.getAesKey());
    	tLicense.setAesEncode(aesEncode);
		if(tLicense.getId() != null && tLicenseMapper.update(tLicense) > 0){
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
        return "redirect:/license/list/" + tLicense.getUserType();
    }

    @PostMapping("/{id}/delete")
    public String doDeleteLicense(@PathVariable("id") int id,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	int userType = 1;
    	if(tRegisterMapper.countByLicId(id) > 0) {
    		redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "此授权码有设备注册，请确认!");
    	} else {
    		TLicense tLicense = tLicenseMapper.findById(id);
    		if(tLicense != null) {
        		userType = tLicense.getUserType();
                if(tLicense.getUserType() == 1) {
	                redirectAttributes.addFlashAttribute("hasError", true);
	                redirectAttributes.addFlashAttribute("message", "通用授权不可删除！");
                }else {
    	    		if(tLicenseMapper.delete(id) > 0){
    	                redirectAttributes.addFlashAttribute("message", "删除成功！");
    	        	}else{
    	                redirectAttributes.addFlashAttribute("hasError", true);
    	                redirectAttributes.addFlashAttribute("message", "删除失败！");
    	        	}
                }
    		}else {
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "记录已经不存在！");
    		}
    	}
        return "redirect:/license/list/" + userType;
    }
}
