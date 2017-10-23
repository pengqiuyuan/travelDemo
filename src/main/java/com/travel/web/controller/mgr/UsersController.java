package com.travel.web.controller.mgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;
import com.travel.entity.Function;
import com.travel.entity.User;
import com.travel.service.account.AccountService;
import com.travel.service.account.ShiroDbRealm.ShiroUser;
import com.travel.service.function.FunctionService;
import com.travel.service.user.UserService;

/**
 * 用户管理的controller
 */
@Controller("usersController")
@RequestMapping(value="/manage/user")
public class UsersController extends BaseController{
	
	private static final String PAGE_SIZE = "15";
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();

	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("registerDate", "时间");
	}
	
	public static Map<String, String> getSortTypes() {
		return sortTypes;
	}

	public static void setSortTypes(Map<String, String> sortTypes) {
		UsersController.sortTypes = sortTypes;
	}

	
	@Override
	@InitBinder
	protected void initBinder(ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class,"registerDate",new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
	}
	
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FunctionService functionService;
	
	/**
	 *  用户管理首页
	 * @param pageNumber  @param pageSize   显示条数
	 * @param sortType  排序
	 * @param model   返回对象
	 * @param request  封装的请 @return
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto")String sortType, Model model,
			ServletRequest request){
		Long userId = getCurrentUserId();
		logger.info("userId"+userId+"用户管理首页");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<User> users = userService.findTenanciesByCondition(userId,searchParams, pageNumber, pageSize, sortType);
		model.addAttribute("users", users);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "/user/index";
	}
	
	
	/**
	 * 操作员编辑页 @param oid 用户ID
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id")long id,Model model){
		User user = userService.findById(id);
		model.addAttribute("user", user);
		model.addAttribute("id", id);
		
		LinkedHashMap<String, LinkedHashMap<Function,String>> functions = new LinkedHashMap<String, LinkedHashMap<Function,String>>();
		List<String> firstName = FunctionController.getFirstNa();
		
		for (String string : firstName) {
			LinkedHashMap<Function,String> map = new LinkedHashMap<Function, String>();
			List<Function> f = functionService.findByFirstName(string);
			for (Function function : f) {
				if(user.getRoleList().contains(function.getRole())){
					map.put(function, "包含");
				}else{
					map.put(function, "不包含");
				}
			}
			functions.put(string, map);
		}
		model.addAttribute("functions",functions);
		return "/user/edit";
	}
	
	/**
	 * 操作员更新页 @param user 用户
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateUser(User user,RedirectAttributes redirectAttributes){
		userService.update(user);
		redirectAttributes.addFlashAttribute("message", "修改用户成功");
	    return "redirect:/manage/user/index";
	}
	
	/**
	 * 新增操作员页 @param uid 租户ID
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addUser(Model model){
		LinkedHashMap<String, List<Function>> functions = new LinkedHashMap<String, List<Function>>();
		LinkedList<String> firstName = FunctionController.getFirstNa();
		for (String string : firstName) {
			functions.put(string, functionService.findByFirstName(string));
		}
		model.addAttribute("functions",functions);
		return "/user/add";
	}
	
	/**
	 * 新增操作 @param Usertest 用户
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String saveUser(User user,ServletRequest request,Model model,RedirectAttributes redirectAttributes){
		String password = request.getParameter("confirmPwdCipher");
		user.setPlainPassword(password);
		boolean flag = userService.isOnly(user.getLoginName());
		if(flag){
			accountService.registerUser(user);
			redirectAttributes.addFlashAttribute("message", "新增用户成功");
			return "redirect:/manage/user/index";
		}
		model.addAttribute("message", "用户名重复！");
	    return "/user/add"; 
	}

	/**
	 * 重置密码 
	 * @param oid 用户ID
	 * @return
	 */
	@RequestMapping(value = "resetPwd", method = RequestMethod.GET)
	public String resetPwd(@RequestParam(value = "id")long id,Model model){
		User user = userService.findById(id);
        model.addAttribute("user", user);
		return "/user/resetPwd";
	}
	
	/**
	 * 更新密码
	 * @param oid 用户id 
	 * @return
	 */
	@RequestMapping(value = "savePwd", method = RequestMethod.POST)
	public String savePwd(@RequestParam(value = "id")long id,ServletRequest request,RedirectAttributes redirectAttributes){
		User user = userService.findById(id);
		String password = request.getParameter("confirmPwdCipher");
		user.setPlainPassword(password);
		accountService.updateUser(user);
		redirectAttributes.addFlashAttribute("message", "更新密码成功");
		return "redirect:/manage/user/index";
	}

	
	/**
	 * 冻结	 * @param oid 用户id
	 */
	@RequestMapping(value = "del", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> del(@RequestParam(value = "id")long id){
		User user = userService.findById(id);
		userService.del(user);
	    Map<String,String> map = new HashMap<String, String>();
		map.put("success", "true");
		return map;
	}
	
	/**
	 * 删除	 * @param oid 用户id
	 */
	@RequestMapping(value = "delUser", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> delUser(@RequestParam(value = "id")long id){
		User user = userService.findById(id);
		userService.realDel(user);
	    Map<String,String> map = new HashMap<String, String>();
		map.put("success", "true");
		return map;
	}
	
	/**
	 * @param oid 用户id
	 */
	@RequestMapping(value = "active", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String,String> lockUser(@RequestParam(value = "id")long id){
		User user = userService.findById(id);
		userService.active(user);
	    Map<String,String> map = new HashMap<String, String>();
		map.put("success", "true");
		return map;
	}
	/**
	 * 用户详细
	 * @param id 用户id
	 */
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String show(@RequestParam(value = "id")long id,Model model){
		
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "/user/info";
	}
	
	/**
	 * GM用户项目权限查看
	 * @param oid 用户ID
	 * @return
	 */
	@RequestMapping(value = "role", method = RequestMethod.GET)
	public String role(@RequestParam(value = "id")long id,Model model){
		User user = userService.findById(id);
		model.addAttribute("user", user);
		model.addAttribute("id", id);
		
		LinkedHashMap<String, LinkedHashMap<Function,String>> functions = new LinkedHashMap<String, LinkedHashMap<Function,String>>();
		List<String> firstName = FunctionController.getFirstNa();
		
		for (String string : firstName) {
			LinkedHashMap<Function,String> map = new LinkedHashMap<Function, String>();
			List<Function> f = functionService.findByFirstName(string);
			for (Function function : f) {
				if(user.getRoleList().contains(function.getRole())){
					map.put(function, "包含");
				}else{
					map.put(function, "不包含");
				}
			}
			functions.put(string, map);
		}
		model.addAttribute("functions",functions);
		return "/user/role";
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
	
	/**
	 * 取出Shiro中的当前用户Id.
	 */
	public String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.name;
	}
	
	
}
