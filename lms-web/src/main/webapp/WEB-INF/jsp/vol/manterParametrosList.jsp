<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterParametrosAction" >
	<adsm:form  action="/vol/manterParametros">
		
		<adsm:lookup service="lms.vol.manterParametrosAction.findLookupFilial" dataType="text" property="filial" 
				idProperty="idFilial" criteriaProperty="sgFilial" label="filial" size="5" maxLength="3"
				action="/municipios/manterFiliais" width="30%"/>				
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parametros"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
						
	</adsm:form>

	<adsm:grid property="parametros" idProperty="idParametro" selectionMode="check" 
			   rows="12" gridHeight="150" unique="true">
		<adsm:gridColumn property="filial.sgFilial" title="filial" width="15%" />
		<adsm:gridColumn property="refresh" title="atualizacaoGerencial" width="20%" />
		<adsm:gridColumn property="alertaCel" title="alertaCel" width="20%" />
		<adsm:gridColumn property="alertaBaixa" title="alertaBaixa" width="20%" />
		<adsm:gridColumn property="emailRecusa" title="emailRecusa" width="25%" />
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>