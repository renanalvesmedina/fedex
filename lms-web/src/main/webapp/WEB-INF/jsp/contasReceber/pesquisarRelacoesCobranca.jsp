<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="pesquisarRelacoesCobranca" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/pesquisarRelacoesCobranca" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/pesquisarRelacoesCobranca" cmd="cad" disabled="true"/>

			<adsm:tab title="documentosServico"
					  autoLoad="false" 
					  disabled="true"
			 		  masterTabId="cad" 
			 		  copyMasterTabProperties="true"
		   	 		  id="documentosServico" 
					  src="/contasReceber/pesquisarRelacoesCobranca" 
					  cmd="documentosServico" 
					  onShow="myOnShow"
					  boxWidth="140"/>
		</adsm:tabGroup>
</adsm:window>
