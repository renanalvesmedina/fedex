<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.cargoOperacionalService" >
	<adsm:form action="/carregamento/manterCargosOperacionais" idProperty="idCargoOperacional" >
		<adsm:textbox dataType="text" property="dsCargo" label="nomeCargo" maxLength="60" size="60" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="cargos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="cargos" idProperty="idCargoOperacional" defaultOrder="dsCargo:asc" selectionMode="check" rows="13" gridHeight="200" unique="true">
		<adsm:gridColumn property="dsCargo" title="nomeCargo" width="90%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>