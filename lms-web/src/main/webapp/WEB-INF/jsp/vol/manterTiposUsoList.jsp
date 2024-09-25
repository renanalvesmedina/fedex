<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterTiposUsoAction" >
	<adsm:form  action="/vol/manterTiposUso">
	
		<adsm:hidden property="idTiposUso" />
		<adsm:textbox property="dsNome" dataType="text" label="nome" maxLength="30" size="40"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tiposUso"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
		
	</adsm:form>

	<adsm:grid property="tiposUso" idProperty="idTiposUso" selectionMode="check" 
			   rows="14" gridHeight="150" unique="true" service="lms.vol.manterTiposUsoAction.findPaginatedTiposUso"
			   rowCountService="lms.vol.manterTiposUsoAction.getRowCountTiposUso" >
		<adsm:gridColumn property="dsNome" title="descricao" width="100%" />
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>