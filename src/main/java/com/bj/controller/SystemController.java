/**
 * 
 */
package com.bj.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.dao.mapper.TSystemParamMapper;
import com.bj.pojo.Parameter;
import com.bj.util.Contants;

/**
 * @author LQK
 *
 */
@Controller
@RequestMapping("/system")
public class SystemController {	
	@Resource
	private TSystemParamMapper tSystemParamMapper;
	
    @GetMapping({"","/","/person"})
    public String goUser(Map<String, Object> model) {
    	Parameter parameter = new Parameter();
    	parameter.setUserCode(tSystemParamMapper.findByKey(Contants.PERSON_USER_CODE_NAME));
    	parameter.setAesKey(tSystemParamMapper.findByKey(Contants.PERSON_AES_KEY_NAME));
    	parameter.setRsaPrivateKey(tSystemParamMapper.findByKey(Contants.PERSON_RSA_PRIVATE_KEY_NAME));
    	parameter.setRsaPublicKey(tSystemParamMapper.findByKey(Contants.PERSON_RSA_PUBLIC_KEY_NAME));
    	parameter.setExpiryDate(tSystemParamMapper.findByKey(Contants.PERSON_EXPIRY_DATE_NAME));
        model.put("parameter", parameter);
        return "system/person";
    }

    @Transactional
    @PostMapping("/person/edit")
    public String doEditPersonParam(@Valid Parameter parameter,
    							Map<String, Object> model,
            					HttpServletRequest request,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	tSystemParamMapper.updateValue(Contants.PERSON_USER_CODE_NAME, parameter.getUserCode());
    	tSystemParamMapper.updateValue(Contants.PERSON_AES_KEY_NAME, parameter.getAesKey());
    	tSystemParamMapper.updateValue(Contants.PERSON_RSA_PRIVATE_KEY_NAME, parameter.getRsaPrivateKey());
    	tSystemParamMapper.updateValue(Contants.PERSON_RSA_PUBLIC_KEY_NAME, parameter.getRsaPublicKey());
    	tSystemParamMapper.updateValue(Contants.PERSON_EXPIRY_DATE_NAME, parameter.getExpiryDate());
    	redirectAttributes.addFlashAttribute("message", "保存成功！");
        return "redirect:/system/person";
    }
}
