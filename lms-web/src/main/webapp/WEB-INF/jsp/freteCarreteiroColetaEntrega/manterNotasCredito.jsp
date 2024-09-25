<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterNotasCredito" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="freteCarreteiroColetaEntrega/manterNotasCredito" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="freteCarreteiroColetaEntrega/manterNotasCredito" cmd="cad" disabled="true"/>
			<adsm:tab title="anexo" masterTabId="cad" disabled="true" id="anexo" src="freteCarreteiroColetaEntrega/manterNotasCredito" cmd="anexo" autoLoad="false"/>
		</adsm:tabGroup>
</adsm:window>
   