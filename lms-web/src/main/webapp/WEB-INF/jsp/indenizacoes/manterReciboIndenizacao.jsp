<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterReciboIndenizacao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"         id="pesq" src="/indenizacoes/manterReciboIndenizacao"       cmd="list" onShow="onTabShow"/>
		<adsm:tab title="detalhamento"     id="cad"  src="/indenizacoes/manterReciboIndenizacao"       cmd="cad"  onHide="onTabHide" disabled="true"/>
		<adsm:tab title="documentos"       id="documentos" src="/indenizacoes/manterReciboIndenizacao" cmd="documentos" disabled="true" masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"  boxWidth="90" />
		<adsm:tab title="filiaisDebitadas" id="debitadas" src="/indenizacoes/manterReciboIndenizacao"  cmd="debitadas"  disabled="true"                                                  onShow="onTabShow"  boxWidth="100" />
		<adsm:tab title="MDA"              id="mda"       src="/indenizacoes/manterReciboIndenizacao"  cmd="mda"        disabled="true" masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="parcelas"         id="parcelas"  src="/indenizacoes/manterReciboIndenizacao"  cmd="parcelas"   disabled="true" masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="anexo"        	   id="anexo"     src="/indenizacoes/manterReciboIndenizacao"  cmd="anexo"      disabled="true" masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="eventos"          id="eventos"   src="/indenizacoes/manterReciboIndenizacao"  cmd="eventos"    disabled="true" masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow" />
	</adsm:tabGroup>
</adsm:window>
