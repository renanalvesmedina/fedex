package com.mercurio.lms.security.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PermissionTabCmdDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tabCmd;
	private String permission;
	
	private Map<String, PermissionWidgetPropertyDto> permissions = new HashMap<String, PermissionWidgetPropertyDto>();

	public PermissionTabCmdDto() {}

	public PermissionTabCmdDto(String tabCmd, String permission) {
		super();
		this.tabCmd = tabCmd;
		this.permission = permission;
	}

	public String getTabCmd() {
		return tabCmd;
	}
	
	public String getPermission() {
		return permission;
	}

	public Map<String, PermissionWidgetPropertyDto> getPermissions() {
		return permissions;
	}

}
