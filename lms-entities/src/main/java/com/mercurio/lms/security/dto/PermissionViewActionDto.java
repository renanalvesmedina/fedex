package com.mercurio.lms.security.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PermissionViewActionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String viewAction;
	private boolean permission;
	
	private Map<String, PermissionTabCmdDto> permissions = new HashMap<String, PermissionTabCmdDto>();
	
	public PermissionViewActionDto() {}

	public PermissionViewActionDto(String viewAction, boolean permission) {
		super();
		this.viewAction = viewAction;
		this.permission = permission;
	}

	public String getViewAction() {
		return viewAction;
	}
	
	public boolean isPermission() {
		return permission;
	}
	
	public Map<String, PermissionTabCmdDto> getPermissions() {
		return permissions;
	}

}
