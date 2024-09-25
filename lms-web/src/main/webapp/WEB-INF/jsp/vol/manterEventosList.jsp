<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterEventosAction" >
	<adsm:form  action="/vol/manterEventos">
		<adsm:hidden property="idTipoEvento"/>
		<adsm:textbox dataType="text" property="nmEvento" label="descricao" width="35%" size="60" maxLength="60"/>
		<adsm:combobox property="evento" label="tipoEvento" domain="DM_TP_EVENTO_CEL"
						autoLoad="true" onlyActiveValues="false"/>		
						
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="eventos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
				
	</adsm:form>

	<adsm:grid property="eventos" idProperty="idTipoEvento" selectionMode="check" 
			   rows="14" gridHeight="150" unique="true" service="lms.vol.manterEventosAction.findPaginatedEventosOperacao"
			   rowCountService="lms.vol.manterEventosAction.getRowCountEventosOperacao">
   		<adsm:gridColumn property="nmCodigo" title="codigo" width="15%" align="right"/>
		<adsm:gridColumn property="dsNome" title="nome"   width="60%" />
		<adsm:gridColumn property="tpTipoEvento" title="tipo" isDomain="true"  width="25%" />
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>