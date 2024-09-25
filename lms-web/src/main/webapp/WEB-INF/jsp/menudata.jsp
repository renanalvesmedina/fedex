<%@ page import="com.mercurio.lms.configuracoes.model.service.*, 
				 com.mercurio.adsm.framework.model.pojo.Sistema" %>
<%@ include file="/lib/imports.jsp"%>
<% out.print(new MenuLms().getItens(contextName, request)); %>