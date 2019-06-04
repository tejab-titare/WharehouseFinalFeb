/*package com.app.validator;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication
@Controller
public class FormValidationApplication {

	@GetMapping("/")
	public String showForm(Person person) {
		return "register";
	}

	@PostMapping("/")
	public String register(@Valid Person person, Errors errors, Model model) {
		if (errors.hasErrors()) {
			return "register";
		} else {
			model.addAttribute("message", "Registration successfully...");
			return "register";
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(FormValidationApplication.class, args);
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {

	@NotNull
	@Size(min = 2, max = 10, message = "length shoud be in between 2 to 10")
	private String name;

	@NotEmpty(message = "Email field should not be empty")
	@Email(regexp = "^(.+)@(.+)$", message = "Invalid email pattern")
	private String email;

	@Pattern(regexp = "[7-9][0-9]{9}", message = "invalid mobile number.")
	@Size(max = 10, message = "digits should be 10")
	private String mobile;
}

#### This file contains all key:value details for application #####
## use only symbols dash(-),dot(.), underscore(_)
# JDBC Properties
dc=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/test
un=root
pwd=root

# Hibernate Properties
dialect=org.hibernate.dialect.MySQL5Dialect
showsql=true
fmtsql=true
#ddlauto=create
ddlauto=update

# MVC Properties
mvc.prefix=/WEB-INF/views/
mvc.suffix=.jsp

## Email Properties #

email.host=smtp.gmail.com
email.port=587
email.usr=tejabtitare1@gmail.com
email.pwd=7798007658

email.auth=true
email.secure=true


..........main controller...

Conversation opened. 1 read message.

Skip to content
Using Gmail with screen readers

5 of 105
forget password
Inbox
x

Ramanuj LaljiPrasad Sahu
Attachments
12:06 PM (47 minutes ago)
to me

please check attachments.
5 Attachments
Thanks a lot.Received, thank you.Ok, I will check.

package com.airinfotech.controller;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.airinfotech.service.UserService;
import com.airinfotech.utils.SpringUtils;
import com.airinfotech.value.DepartmentVO;
import com.airinfotech.value.UserVo;

@Controller
public class MainController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model) {
		return "adminPage";
	}

	@RequestMapping(value = {"/","/login"}, method = RequestMethod.GET)
	public String loginPage(Model model, Principal principal ) { 
		Map<String, Object> m = new HashMap<String, Object>();		
		m.put("department", userService.getDepartment());
		model.addAllAttributes(m);
		return "loginPage";
	}

	@RequestMapping(value = {"/forget"}, method = RequestMethod.POST)
	public String forgotPassword(@ModelAttribute("userVo")UserVo userVo, 
			BindingResult result, ModelMap model,Principal principal ) { 
		userVo=userService.forgotPassword(userVo.getEmail());	
		Map<String, Object> m = new HashMap<String, Object>();		
		m.put("department", userService.getDepartment());
		m.put("userVo", userVo);
		model.addAllAttributes(m);
		return "loginPage";
	}

	@RequestMapping(value = "/department", method = RequestMethod.GET)
	public @ResponseBody List<DepartmentVO> getDepartment(Model model) {
		return userService.getDepartment();
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			model.addAttribute("message", "Hi " + principal.getName()
			+ "<br> You do not have permission to access this page!");
		} else {
			model.addAttribute("msg",
					"You do not have permission to access this page!");
		}
		return "403Page";
	}

	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String userRegister(@ModelAttribute("userVo")UserVo userVo, 
			BindingResult result, ModelMap model) {		
		userVo=userService.userRegister(userVo);
		model.addAttribute("user", userVo);
		return "redirect:/login";		
	}	

	@RequestMapping(value = "/createusers", method = RequestMethod.GET)
	public String defineUserRoles(Model model, Principal principal) {
		if(principal==null) {
			model=SpringUtils.expireSession(principal, model);
			return "redirect:/login";
		}
		String userName = principal.getName();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("user", userName);
		m.put("userVo",new UserVo());
		m.put("users", userService.getUsers());
		m.put("keyareas", userService.getKeyAreas());
		m.put("permission", userService.getUserPermission(userName));	
		model.addAllAttributes(m);
		return "defineusers";
	}

	@RequestMapping(value="/getuserallinformation", method = RequestMethod.GET)
	public @ResponseBody UserVo getUserAllInformation(@RequestParam("email") String email){	
		UserVo userVo=userService.getUserDetails(email);
		userVo=userService.getUserSelectedDepartment(userVo);
		userVo.setScreenVOs(userService.getUserScreen(userVo.getEmail()));
		return userVo;
	}

	@RequestMapping(value = "/updateuserinfo", method = RequestMethod.POST)
	public @ResponseBody UserVo updateDefineUserInfo(@RequestBody UserVo userVo, 
			ModelMap model, Principal principal) {
		if(principal==null) {			
			userVo.setError("User Session Expired. Please login again!");
			return userVo;
		}		
		userVo.setUseremail(principal.getName());
		userVo=userService.updateUser(userVo);		
		return userVo;
	}

	@RequestMapping(value = "/deactivateuser", method = RequestMethod.POST)
	public @ResponseBody UserVo deactivateUser(@RequestBody UserVo userVo, 
			ModelMap model, Principal principal) {
		if(principal==null) {			
			userVo.setError("User Session Expired. Please login again!");
			return userVo;
		}		
		userVo.setUseremail(principal.getName());
		userVo=userService.deactiveUser(userVo);
		return userVo;
	}

	@RequestMapping(value="/verifyaadhar", method = RequestMethod.GET)
	public @ResponseBody UserVo getAadharNo(@RequestParam("aadharno") String aadharno){	
		UserVo userVo=new UserVo();
		userVo.setAadharno(aadharno);
		return userService.verifyUserAadhar(userVo);
	}
}
MainController.java
Displaying MainController.java.
.......................Iservice....


Conversation opened. 1 read message.

Skip to content
Using Gmail with screen readers

5 of 105
forget password
Inbox
x

Ramanuj LaljiPrasad Sahu
Attachments
12:06 PM (48 minutes ago)
to me

please check attachments.
5 Attachments
Thanks a lot.Received, thank you.Ok, I will check.

package com.airinfotech.service;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.airinfotech.value.DepartmentVO;
import com.airinfotech.value.KeyAreaVo;
import com.airinfotech.value.ScreenVO;
import com.airinfotech.value.UserVo;

public interface UserService extends UserDetailsService{

	public UserVo userRegister(UserVo userVo);

	public UserVo forgotPassword(String emailid);

	public List<UserVo> getUsers();

	public UserVo updateUser(UserVo userVo);

	public UserVo deactiveUser(UserVo userVo);

	public UserVo verifyUserAadhar(UserVo userVo);

	public List<KeyAreaVo> getKeyAreas();

	public UserVo findUserInfo(String userName);

	public List<String> getUserRoles(String userName);	

	public List<UserVo> getUserDepartmentWise(String deptcode);

	public UserVo addUserDepartment(UserVo userVo);

	public List<ScreenVO> getUserScreen(String email);

	public List<DepartmentVO> getDepartment();

	public UserVo deleteUserDepartment(UserVo userVo);

	public UserVo deleteUserScreenPermission(UserVo userVo);

	public UserVo addUserUserScreenPermission(UserVo userVo);

	public UserVo getUserSelectedDepartment(UserVo userVo);

	public UserVo getUserDetails(String email);

	public List<ScreenVO> getUserRolePermission(String email);

	public Map<String, List<ScreenVO>> getUserPermission(String email);
}
UserService.java
Displaying UserService.java.

,..........iserviceImpl.........


Conversation opened. 1 read message.

Skip to content
Using Gmail with screen readers

5 of 105
forget password
Inbox
x

Ramanuj LaljiPrasad Sahu
Attachments
12:06 PM (49 minutes ago)
to me

please check attachments.
5 Attachments
Thanks a lot.Received, thank you.Ok, I will check.

package com.airinfotech.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.airinfotech.dao.UserDAO;
import com.airinfotech.service.UserService;
import com.airinfotech.value.DepartmentVO;
import com.airinfotech.value.KeyAreaVo;
import com.airinfotech.value.ScreenVO;
import com.airinfotech.value.UserVo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVo userVo = userDAO.findUserInfo(username);
		if (userVo == null) 
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		List<String> roles= userDAO.getUserRoles(username);
		List<GrantedAuthority> grantList= new ArrayList<GrantedAuthority>();
		if(roles!= null)  {
			for(String role: roles)  {				
				GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
				grantList.add(authority);
			}
		}    
		UserDetails userDetails = (UserDetails) new User(userVo.getEmail(),userVo.getPassword(),grantList);
		return userDetails;
	}
	@Override
	public UserVo userRegister(UserVo userVo) {
		return userDAO.registerUser(userVo);
	}
	@Override
	public UserVo forgotPassword(String emailid) {
		return userDAO.forgotPassword(emailid);
	}
	@Override
	public List<UserVo> getUsers(){
		return userDAO.getUsers();
	}
	@Override
	public UserVo updateUser(UserVo userVo) {
		return userDAO.updateUser(userVo);
	}
	@Override
	public UserVo deactiveUser(UserVo userVo) {
		return userDAO.deactiveUser(userVo);
	}
	@Override
	public UserVo verifyUserAadhar(UserVo userVo) {
		return userDAO.verifyUserAadhar(userVo);
	}
	@Override
	public List<KeyAreaVo> getKeyAreas(){
		return userDAO.getKeyAreas();
	}
	@Override
	public UserVo findUserInfo(String userName) {
		return userDAO.findUserInfo(userName);
	}

	@Override
	public List<String> getUserRoles(String userName) {
		return userDAO.getUserRoles(userName);
	}

	@Override
	public List<UserVo> getUserDepartmentWise(String deptcode) {
		return userDAO.getUserDepartmentWise(deptcode);
	}
	@Override
	public UserVo addUserDepartment(UserVo userVo) {
		return userDAO.addUserDepartment(userVo);
	}
	@Override
	public List<ScreenVO> getUserScreen(String email ){
		return userDAO.getUserScreen(email);
	}
	@Override
	public List<DepartmentVO> getDepartment() {
		return userDAO.getDepartment();
	}
	@Override
	public UserVo deleteUserDepartment(UserVo userVo) {
		return userDAO.deleteUserDepartment(userVo);
	}
	@Override
	public UserVo deleteUserScreenPermission(UserVo userVo) {
		return userDAO.deleteUserScreenPermission(userVo);
	}
	@Override
	public UserVo addUserUserScreenPermission(UserVo userVo) {
		return userDAO.addUserUserScreenPermission(userVo);
	}
	@Override
	public UserVo getUserSelectedDepartment(UserVo userVo) {
		return userDAO.getUserSelectedDepartment(userVo);
	}
	@Override
	public UserVo getUserDetails(String email) {
		return userDAO.getUserDetails(email);
	}
	@Override
	public List<ScreenVO> getUserRolePermission(String email) {
		return userDAO.getUserRolePermission(email);
	}
	
	@Override
	public  Map<String, List<ScreenVO>> getUserPermission(String email) {
		return userDAO.getUserPermission(email);
	}
}
UserServiceImpl.java
Displaying UserService.java.

........iDao...........


Conversation opened. 1 read message.

Skip to content
Using Gmail with screen readers

5 of 105
forget password
Inbox
x

Ramanuj LaljiPrasad Sahu
Attachments
12:06 PM (51 minutes ago)
to me

please check attachments.
5 Attachments
Thanks a lot.Received, thank you.Ok, I will check.

package com.airinfotech.dao;

import java.util.List;
import java.util.Map;

import com.airinfotech.value.DepartmentVO;
import com.airinfotech.value.KeyAreaVo;
import com.airinfotech.value.ScreenVO;
import com.airinfotech.value.UserVo;

public interface UserDAO {

	public UserVo findUserInfo(String userName);

	public List<String> getUserRoles(String userName);

	public UserVo registerUser(UserVo userVo);    

	public UserVo forgotPassword(String emailid); 

	public List<UserVo> getUsers();

	public UserVo updateUser(UserVo userVo);

	public UserVo deactiveUser(UserVo userVo);

	public UserVo verifyUserAadhar(UserVo userVo);

	public List<KeyAreaVo> getKeyAreas();

	public List<UserVo> getUserDepartmentWise(String deptcode);
	
	public List<DepartmentVO> getDepartment();
	
	public UserVo addUserDepartment(UserVo userVo);

	public List<ScreenVO> getUserScreen(String email); 
	
	public UserVo deleteUserDepartment(UserVo userVo);
	
	public UserVo deleteUserScreenPermission(UserVo userVo);
	
	public UserVo addUserUserScreenPermission(UserVo userVo);
	
	public UserVo getUserSelectedDepartment(UserVo userVo); 
	
	public UserVo getUserDetails(String email);
	
	public List<ScreenVO> getUserRolePermission(String email);
	
	public Map<String, List<ScreenVO>> getUserPermission(String email);
	
}
UserDAO.java
Displaying UserService.java.

.............DaoImpl......


Conversation opened. 1 read message.

Skip to content
Using Gmail with screen readers

5 of 105
forget password
Inbox
x

Ramanuj LaljiPrasad Sahu
Attachments
12:06 PM (51 minutes ago)
to me

please check attachments.
5 Attachments
Thanks a lot.Received, thank you.Ok, I will check.

package com.airinfotech.daoimpl;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airinfotech.common.AIRConstant;
import com.airinfotech.dao.UserDAO;
import com.airinfotech.mapper.DepartmentListMapper;
import com.airinfotech.mapper.DepartmentalUserMapper;
import com.airinfotech.mapper.KeyAreaMapper;
import com.airinfotech.mapper.ScreenListMapper;
import com.airinfotech.mapper.SelectedUserDepartmentMapper;
import com.airinfotech.mapper.UserDetailMapper;
import com.airinfotech.mapper.UserListMapper;
import com.airinfotech.mapper.UserMapper;
import com.airinfotech.mapper.UserRolePermissionMapper;
import com.airinfotech.utils.TimestampUtils;
import com.airinfotech.utils.VerhoeffAlgorithm;
import com.airinfotech.value.DepartmentVO;
import com.airinfotech.value.KeyAreaVo;
import com.airinfotech.value.ScreenVO;
import com.airinfotech.value.UserMonthlyEdraftVO;
import com.airinfotech.value.UserVo;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class UserDAOImpl extends JdbcDaoSupport implements UserDAO {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	HttpServletRequest request;

	@Autowired
	public UserDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public UserVo findUserInfo(String userName) {
		String sql = "select u.email,u.password "//
				+ " from users u where u.email = ? and u.isenable='Y'";
		Object[] params = new Object[] { userName };
		UserMapper mapper = new UserMapper();
		try {
			UserVo userInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
			return userInfo;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<String> getUserRoles(String userName) {
		String sql = "select r.userrole "//
				+ " from roles r where r.email = ? ";

		String sql = "SELECT s.accesscode FROM userpermission AS up\\n" + 
				"INNER JOIN screen AS s ON up.screenid=s.screenid\\n" + 
				"WHERE up.email=?";


		Object[] params = new Object[] { userName };
		List<String> roles = this.getJdbcTemplate().queryForList(sql,params, String.class);
		return roles;
	}

	public UserVo registerUser(UserVo userVo) {		
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
				.withProcedureName("proc_addUser");
		try {
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",userVo.getEmail());
			inParamMap.put("p_firstname",userVo.getFirstname());
			inParamMap.put("p_lastname",userVo.getLastname());
			inParamMap.put("p_enable","Y");
			inParamMap.put("p_contact",userVo.getContact());
			inParamMap.put("p_password",userVo.getPassword());
			inParamMap.put("p_address",userVo.getAddress());
			inParamMap.put("p_pincode",userVo.getPincode());
			inParamMap.put("p_city",userVo.getCity());
			inParamMap.put("p_state",userVo.getState());
			inParamMap.put("p_lastupdate",TimestampUtils.getDate());
			inParamMap.put("p_role","USER");		
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			userVo.setError((String)out.get("p_error"));
			if(StringUtils.equals(userVo.getError(),"SUCCESS")) {
				userVo=addUserDepartment(userVo);
			}
		}catch (Exception e) {
			e.printStackTrace();
			userVo.setError(e.getMessage());
		}
		userVo.setPassword(null);
		return userVo;
	}

	public UserVo forgotPassword(String emailid) {
		UserVo userVo=new UserVo();
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getForgetPassword");
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",emailid);
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			String error=(String)out.get("p_error");
			userVo.setError(error);
			if(error.equals("SUCCESS")) {
				userVo.setPassword((String)out.get("p_password"));
				userVo.setEmail(emailid);
				SimpleMailMessage email = new SimpleMailMessage();		
				email.setFrom(AIRConstant.SENDER_MAIL_ID);
				email.setTo(emailid);
				email.setSubject(AIRConstant.FORGOT_PASSWORD_CONFIRMATION_MAIL_SUBJECT);
				email.setText(createConfirmationMailTemplate(userVo));
				mailSender.send(email);
				userVo.setError("Password send to yours register Email.Please check your Email and Confirm.");
			}
		}catch(Exception e) {
			userVo.setError(e.getMessage());
		}
		return userVo;
	}

	public String createConfirmationMailTemplate(UserVo userVo) {
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("Hello,");
		stringBuilder.append("\\n\\n\\n\\n");
		stringBuilder.append("We have sent you this email in response to your request to forgot  your password on CaseUpload Utility\\n" + 
				"\\n" + 
				"your credentials are as follows,\\n" + 
				"\\n" + 
				"Username -"+userVo.getEmail()+"\\n" + 
				"Password -"+userVo.getPassword()+"\\n" + 
				"We recommend that you keep your password secure and not share it with anyone.");	

		stringBuilder.append("\\n\\n\\n\\n\\n\\n\\n\\n Thank You!\\n\\n AIR Infotech Team");
		return stringBuilder.toString();		
	}

	@Override
	public List<UserVo> getUsers() {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
				.withProcedureName("proc_getUsers")
				.returningResultSet("users", new UserListMapper() );
		Map<String, Object> out = jdbcCall.execute();	
		List<UserVo> userVos =((List<UserVo>) out.get("users")); 
		return userVos;
	}

	@Override
	public UserVo updateUser(UserVo userVo) {
		if(StringUtils.equals(userVo.getStatus(),"NA" )) {
			userVo.setError("Please Select atleastone department");
			return userVo;
		}
		if(StringUtils.equals(userVo.getKeyarea(), "NA")) 
			userVo.setKeyarea("");
		userVo=updateUserRegisterInformation(userVo);
		userVo=deleteUserDepartment(userVo);
		userVo=addUserDepartment(userVo);
		userVo=deleteUserScreenPermission(userVo);
		userVo=addUserUserScreenPermission(userVo);
		return userVo;
	}

	private UserVo updateUserRegisterInformation(UserVo userVo) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_updateUserInfo");
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",userVo.getEmail());
			inParamMap.put("p_initial",userVo.getInitial());
			inParamMap.put("p_code",userVo.getCode());
			inParamMap.put("p_status",userVo.getStatus());
			inParamMap.put("p_aadharno",userVo.getAadharno());
			inParamMap.put("p_keyarea",userVo.getKeyarea());
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);			
			userVo.setError((String)out.get("p_error"));
		}catch (Exception e) {
			e.printStackTrace();
			userVo.setError(e.getMessage());
		}
		return userVo;
	}

	@Override
	public UserVo deactiveUser(UserVo userVo) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
				.withProcedureName("proc_deactivateUser");
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		inParamMap.put("p_email",userVo.getEmail());	;
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Map<String, Object> out = jdbcCall.execute(in);			
		userVo.setError((String)out.get("p_error"));
		return userVo;
	}

	@Override
	public UserVo verifyUserAadhar(UserVo userVo) {	    
		userVo.setVerifyaadhar(VerhoeffAlgorithm.validateAadharNumber(userVo.getAadharno()));
		return userVo;		
	}

	@Override
	public List<KeyAreaVo> getKeyAreas() {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getKeyAreas")
					.returningResultSet("keyareas", new KeyAreaMapper());			
			Map<String, Object> out = jdbcCall.execute();					
			List<KeyAreaVo> keyAreaVos =((List<KeyAreaVo>) out.get("keyareas")); 
			return keyAreaVos;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<UserVo> getUserDepartmentWise(String deptcode) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getDepartmentEmployee")
					.returningResultSet("departmentemployee", new DepartmentalUserMapper());
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_departmentcode",deptcode);
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			List<UserVo> userVos =((List<UserVo>) out.get("departmentemployee")); 
			return userVos;	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DepartmentVO> getDepartment() {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getDepartment")
					.returningResultSet("department", new DepartmentListMapper());			
			Map<String, Object> out = jdbcCall.execute();	
			List<DepartmentVO> departmentVOs =((List<DepartmentVO>) out.get("department")); 
			return departmentVOs;	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserVo addUserDepartment(UserVo userVo) {
		try {
			List<Integer> departmentVOs=userVo.getDeparment();
			if(!CollectionUtils.isEmpty(departmentVOs)) {
				for(Integer departmentVO:departmentVOs) {
					SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
							.withProcedureName("proc_addUserDepartment");			
					Map<String, Object> inParamMap = new HashMap<String, Object>();
					inParamMap.put("p_email",userVo.getEmail());
					inParamMap.put("p_departmentid",departmentVO);
					SqlParameterSource in = new MapSqlParameterSource(inParamMap);
					Map<String, Object> out = jdbcCall.execute(in);	
					userVo.setError((String)out.get("p_error"));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userVo;
	}

	@Override
	public List<ScreenVO> getUserScreen(String email) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getUserScreen")
					.returningResultSet("screens", new ScreenListMapper());		
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",email);			
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			List<ScreenVO> screenVOs =((List<ScreenVO>) out.get("screens")); 
			return screenVOs;	
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserVo deleteUserDepartment(UserVo userVo) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_deleteUserDepartment");		
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",userVo.getEmail());			
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			userVo.setError((String)out.get("p_error"));
		}catch (Exception e) {
			userVo.setError(e.getMessage());
			e.printStackTrace();
		}
		return userVo;
	}

	@Override
	public UserVo deleteUserScreenPermission(UserVo userVo) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_deleteUserScreenPermission");		
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",userVo.getEmail());			
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			userVo.setError((String)out.get("p_error"));
		}catch (Exception e) {
			userVo.setError(e.getMessage());
			e.printStackTrace();
		}
		return userVo;
	}

	@Override
	public UserVo addUserUserScreenPermission(UserVo userVo) {		
		List<ScreenVO> screenVOs=userVo.getScreenVOs();
		if(!CollectionUtils.isEmpty(screenVOs)) {
			for(ScreenVO screenVO:screenVOs) {

				try {
					SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
							.withProcedureName("proc_addUserScreenpermission");		
					Map<String, Object> inParamMap = new HashMap<String, Object>();
					inParamMap.put("p_email",userVo.getEmail());
					inParamMap.put("p_screenid",screenVO.getScreenId());
					inParamMap.put("p_createby",userVo.getUseremail());
					SqlParameterSource in = new MapSqlParameterSource(inParamMap);
					Map<String, Object> out = jdbcCall.execute(in);	
					userVo.setError((String)out.get("p_error"));
				}catch (Exception e) {
					userVo.setError(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return userVo;
	}

	@Override
	public UserVo getUserSelectedDepartment(UserVo userVo) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getUserSelectedDepartment")
					.returningResultSet("department", new SelectedUserDepartmentMapper());	
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",userVo.getEmail());			
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			List<DepartmentVO> departmentVOs =((List<DepartmentVO>) out.get("department"));
			userVo.setDepartmentVOs(departmentVOs);			
		}catch (Exception e) {
			userVo.setError(e.getMessage());
			e.printStackTrace();
		}
		return userVo;		
	}

	@Override
	public UserVo getUserDetails(String email) {
		UserVo userVo=new UserVo();
		try {
			userVo.setEmail(email);
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getUserDetailsByEmailId")
					.returningResultSet("userdetails", new UserDetailMapper());	
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",userVo.getEmail());			
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			List<UserVo> userVos =((List<UserVo>) out.get("userdetails"));			
			try {
				userVo =userVos.get(0);
			}catch (Exception e) {
				userVo.setError(e.getMessage());
			}

		}catch (Exception e) {
			userVo.setError(e.getMessage());
			e.printStackTrace();
		}
		return userVo;	
	}

	@Override
	public List<ScreenVO> getUserRolePermission(String email) {
		try {
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.getDataSource())
					.withProcedureName("proc_getUserRolePermission")
					.returningResultSet("userrolepermission", new UserRolePermissionMapper());	
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("p_email",email);			
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> out = jdbcCall.execute(in);	
			List<ScreenVO> departmentVOs =((List<ScreenVO>) out.get("userrolepermission"));	
			return departmentVOs;
		}catch (Exception e) {			
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, List<ScreenVO>> getUserPermission(String email) {	
		List<ScreenVO> permission=getUserRolePermission(email);
		Map<String, List<ScreenVO>> groupbyScreen = permission.stream().collect(Collectors.groupingBy(ScreenVO::getGroupcode));
		return groupbyScreen;		
		StringJoiner stringJoiner=new StringJoiner(",");
		List<String> permission=getUserRolePermission(email);
		for(String s:permission) {
			if(StringUtils.isNotBlank(s))
				stringJoiner.add("\\'ROLE_"+s+"\\'");
		}
		return stringJoiner.toString();
	}	
}
UserDAOImpl.java
Displaying UserService.java.
..........*/