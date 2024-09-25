package com.mercurio.lms.security.dto;

import java.io.Serializable;

public class PermissionWidgetPropertyDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String widgetProperty;
	private String permission;
	
	public PermissionWidgetPropertyDto() {}

	public PermissionWidgetPropertyDto(String widgetProperty, String permission) {
		super();
		this.widgetProperty = widgetProperty;
		this.permission = permission;
	}

	public String getWidgetProperty() {
		return widgetProperty;
	}
	
	public String getPermission() {
		return permission;
	}

}
