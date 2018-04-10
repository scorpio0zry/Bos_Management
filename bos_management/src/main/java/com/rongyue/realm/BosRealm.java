package com.rongyue.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongyue.domain.system.Permission;
import com.rongyue.domain.system.Role;
import com.rongyue.domain.system.User;
import com.rongyue.service.system.PermissionService;
import com.rongyue.service.system.RoleSerivce;
import com.rongyue.service.system.UserService;


public class BosRealm extends AuthorizingRealm{
	@Autowired
	private UserService userService;
	@Autowired
	private RoleSerivce roleSerivce;
	@Autowired
	private PermissionService permissionService;
	
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//根据当前用户查询其角色和权限
		Subject subject = SecurityUtils.getSubject();
		//获取当前用户
		User user = (User) subject.getPrincipal();
		//获取角色
		List<Role> roles = roleSerivce.findByUser(user);
		for (Role role : roles) {
			authorizationInfo.addRole(role.getKeyword());
		}
		//获取权限
		List<Permission> permissions = permissionService.findByUser(user);
		for (Permission permission : permissions) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		return authorizationInfo;
	}
	
	//认证....
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		//查询用户是否存在
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		
		if(user == null){
			//用户名不存在
			return null;
		}
		//用户名存在 ，进行认证
		return new SimpleAuthenticationInfo(user, user.getPassword(),getName());
	}

}
