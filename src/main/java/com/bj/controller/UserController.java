/**
 * 
 */
package com.bj.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bj.dao.mapper.TRoleMapper;
import com.bj.dao.mapper.TUserMapper;
import com.bj.pojo.TMenu;
import com.bj.pojo.TRole;
import com.bj.pojo.TRoleMenu;
import com.bj.pojo.TUser;
import com.bj.pojo.TUserRole;
import com.bj.pojo.ZTree;
import com.bj.util.BaseUtil;
import com.bj.util.Contants;
import com.bj.util.Pagination;

import net.sf.json.JSONArray;

/**
 * @author LQK
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Resource
	private TUserMapper tUserMapper;
	/**------------------------------------**/
	/**-----------User --------------------**/
	/**------------------------------------**/
    @GetMapping({"","/","/list"})
    public String goUser(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = tUserMapper.countAll();
    	List<TUser> users = tUserMapper.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("users", users);
        model.put("pagination", pagination);
        return "user/list";
    }

    @GetMapping("/new")
    public String goNewUser(Map<String, Object> model,
            HttpServletRequest request) {
    	List<TRole> roles = tRoleMapper.findAll(0, 1000);
        model.put("roles", roles);
        return "/user/new";
    }

    @Transactional
    @PostMapping("/new")
    public String doNewUser(TUser tUser,
    		Map<String, Object> model,
			final RedirectAttributes redirectAttributes) {
    	tUser.setPassword(BaseUtil.md5(tUser.getPassword()));
    	if(tUserMapper.insert(tUser) > 0) {
    		if(tUser.getRoleIds() != null) {
    			for(int rId: tUser.getRoleIds()) {
    				TUserRole ur = new TUserRole();
    				ur.setUserId(tUser.getId());
    				ur.setRoleId(rId);
    				tUserMapper.insertUR(ur);
    			}
    		}
        	redirectAttributes.addFlashAttribute("message", "保存成功");
    	}else {
        	redirectAttributes.addFlashAttribute("hasError", true);
        	redirectAttributes.addFlashAttribute("message", "保存成功");
    	}
        return "redirect:/user/list";
    }

    @GetMapping("/{id}/edit")
    public String goEditUser(Map<String, Object> model,
			@PathVariable("id") int id,
			final RedirectAttributes redirectAttributes) {
    	TUser tUser = tUserMapper.findById(id);
    	if(tUser != null) {
    		tUser.setRoleIds(tUserMapper.findRolesByUserId(id));
        	List<TRole> roles = tRoleMapper.findAll(0, 1000);
            model.put("roles", roles);
            model.put("tUser", tUser);
    	}else {
    		redirectAttributes.addFlashAttribute("hasError", true);
    		redirectAttributes.addFlashAttribute("message", "记录已经不存在");
            return "redirect:/user/list";
    	}
        return "/user/edit";
    }

    @Transactional
    @PostMapping("/{id}/edit")
    public String doEditUser(@Valid TUser tUser,
    							Map<String, Object> model,
            					HttpServletRequest request,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	try {
	    	if(tUser.getPassword() == null || "".equals(tUser.getPassword().trim())) {
	    		tUser.setPassword(null);
	    	}else {
	        	tUser.setPassword(BaseUtil.md5(tUser.getPassword()));
	    	}
	    	TUser loginUser = (TUser)request.getSession().getAttribute(Contants.SESSION_COMMON_KEY);
	    	if(!"admin".equalsIgnoreCase(loginUser.getUsername()) && tUser.getId() != loginUser.getId()) {
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "非系统管理员只能修改本人信息");
	            return "redirect:/user/list";
	    	}
			if(tUser.getId() != null && tUserMapper.update(tUser) > 0 && tUserMapper.deleteUR(tUser.getId()) >= 0){
	    		if(tUser.getRoleIds() != null) {
	    			for(int rId: tUser.getRoleIds()) {
	    				TUserRole ur = new TUserRole();
	    				ur.setUserId(tUser.getId());
	    				ur.setRoleId(rId);
	    				tUserMapper.insertUR(ur);
	    			}
	    		}
	            redirectAttributes.addFlashAttribute("message", "保存成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "保存失败！");
	    	}
    	}catch (Exception e) {
			LOGGER.error("保存失败,{}", e);
		}
        return "redirect:/user/list";
    }

    @Transactional
    @PostMapping("/{id}/delete")
    public String doDeleteUser(@PathVariable("id") int id,
            					HttpServletRequest request,
    							final RedirectAttributes redirectAttributes) throws IOException {
    	TUser loginUser = (TUser)request.getSession().getAttribute(Contants.SESSION_COMMON_KEY);
    	if(loginUser != null && loginUser.getId() == id) {
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "不能删除自己");
    	}else {
    		TUser tUser = tUserMapper.findById(id);
    		if(!"admin".equalsIgnoreCase(loginUser.getUsername()) && tUser.getId() != loginUser.getId()) {
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "非系统管理员不能删除其他用户");
                return "redirect:/user/list";
        	}
    		if(tUser != null && "admin".equals(tUser.getUsername())) {
                redirectAttributes.addFlashAttribute("hasError", true);
                redirectAttributes.addFlashAttribute("message", "不能删除admin");
                return "redirect:/user/list";
    		}
			if(tUserMapper.delete(id) > 0){
	            redirectAttributes.addFlashAttribute("message", "删除成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "删除失败！");
	    	}
    	}
        return "redirect:/user/list";
    }

    @PostMapping("/valid/username")
    public @ResponseBody String validTaskPwd(@RequestParam("username") String username, 
    			@RequestParam(value = "oldName", defaultValue = "") String oldName) throws IOException {
    	Boolean result = tUserMapper.findByUsernameAndExclude(username, oldName)==null;
        return "{\"valid\": " + result.toString() + "}";
    }

	/**------------------------------------**/
	/**-----------Role --------------------**/
	/**------------------------------------**/
	@Resource
	private TRoleMapper tRoleMapper;
	
    @GetMapping("/role/list")
    public String goRole(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "p", defaultValue = "1") int page) {
    	int count = tRoleMapper.countAll();
    	List<TRole> roles = tRoleMapper.findAll((page - 1) * Pagination.DEFAULT_PAGE_SIZE, Pagination.DEFAULT_PAGE_SIZE);
        Pagination pagination = new Pagination(request, page, count, Pagination.DEFAULT_PAGE_SIZE);
        model.put("roles", roles);
        model.put("pagination", pagination);
        return "/user/role/list";
    }

    @GetMapping("/role/new")
    public String goNewRole(Map<String, Object> model,
            HttpServletRequest request) throws IOException{
        model.put("menus", getaLLMenuData());
        return "/user/role/new";
    }

    @GetMapping("/role/all_menu_data")
    public @ResponseBody String getaLLMenuData() throws IOException {
    	List<TMenu> menus = tRoleMapper.findAllMenu();
    	List<ZTree> zTrees = new ArrayList<>();
    	for(int i=0; i<menus.size(); i++) {
    		TMenu menu = menus.get(i);
    		ZTree zTree = new ZTree();
    		zTree.setId(menu.getId());
    		zTree.setpId(menu.getParentId());
    		zTree.setName(menu.getName()+"----------------"+menu.getCode()+"");
    		zTrees.add(zTree);
    	}
    	JSONArray obj = JSONArray.fromObject(zTrees);
    	String json = obj.toString();
    	LOGGER.info("menu_json:{}", json);
        return json;
    }

    @Transactional
    @PostMapping("/role/new")
    public String doNewRole(TRole tRole,
    		Map<String, Object> model,
            @RequestParam(value = "mIds") int[] mIds,
			final RedirectAttributes redirectAttributes) {
    	if(tRoleMapper.insert(tRole) > 0) {
    		int roleId = tRole.getId();
    		for(int id: mIds) {
    			TRoleMenu trm = new TRoleMenu();
    			trm.setRoleId(roleId);
    			trm.setMenuId(id);
    			tRoleMapper.insertTRM(trm);
    		}
        	redirectAttributes.addFlashAttribute("message", "保存成功");
    	}else {
        	redirectAttributes.addFlashAttribute("hasError", true);
        	redirectAttributes.addFlashAttribute("message", "保存成功");
    	}
        return "redirect:/user/role/list";
    }

    @GetMapping("/role/{id}/edit")
    public String goEditRole(Map<String, Object> model,
			@PathVariable("id") int id,
			final RedirectAttributes redirectAttributes)  throws IOException {
    	TRole tRole = tRoleMapper.findById(id);
    	if(tRole != null) {
    		List<TMenu> myMenus = tRole.gettMenus();
        	List<TMenu> menus = tRoleMapper.findAllMenu();
        	List<ZTree> zTrees = new ArrayList<>();
        	for(int i=0; i<menus.size(); i++) {
        		TMenu menu = menus.get(i);
        		ZTree zTree = new ZTree();
        		zTree.setId(menu.getId());
        		zTree.setpId(menu.getParentId());
        		zTree.setName(menu.getName()+"----------------"+menu.getCode()+"");
        		for(TMenu myMenu:myMenus) {
        			if(menu.getId() == myMenu.getId()) {
        				zTree.setChecked(true);
        				break;
        			}
        		}
        		zTrees.add(zTree);
        	}
        	JSONArray obj = JSONArray.fromObject(zTrees);
        	String mJson = obj.toString();
            model.put("tRole", tRole);
            model.put("menus", mJson);
    	}else {
    		redirectAttributes.addFlashAttribute("hasError", true);
    		redirectAttributes.addFlashAttribute("message", "记录已经不存在");
            return "redirect:/user/role/list";
    	}
        return "/user/role/edit";
    }

    @Transactional
    @PostMapping("/role/{id}/edit")
    public String doEditRole(@Valid TRole tRole,
    							Map<String, Object> model,
    				            @RequestParam(value = "mIds") int[] mIds,
    							final RedirectAttributes redirectAttributes) throws IOException {    	
		if(tRole.getId() != null && tRoleMapper.update(tRole) > 0 && tRoleMapper.deleteTRM(tRole.getId()) > 0){
    		for(int id: mIds) {
    			TRoleMenu trm = new TRoleMenu();
    			trm.setRoleId(tRole.getId());
    			trm.setMenuId(id);
    			tRoleMapper.insertTRM(trm);
    		}
            redirectAttributes.addFlashAttribute("message", "保存成功！");
    	}else{
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "保存失败！");
    	}
        return "redirect:/user/role/list";
    }

    @Transactional
    @PostMapping("/role/{id}/delete")
    public String doDeleteRole(@PathVariable("id") int id,
            					HttpServletRequest request,
    							final RedirectAttributes redirectAttributes) throws IOException {
        	TRole tRole = tRoleMapper.findById(id);
			if(tRole != null && tRoleMapper.deleteTRM(tRole.getId()) > 0 && tRoleMapper.delete(id) > 0){
	            redirectAttributes.addFlashAttribute("message", "删除成功！");
	    	}else{
	            redirectAttributes.addFlashAttribute("hasError", true);
	            redirectAttributes.addFlashAttribute("message", "删除失败！");
	    	} 
        return "redirect:/user/role/list";
    }
}
