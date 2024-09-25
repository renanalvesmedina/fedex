<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterRedeco" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterRedeco" cmd="list" />
			<adsm:tab title="detalhamento" autoLoad="true" id="cad" src="/contasReceber/manterRedeco" cmd="cad"/>
			<adsm:tab title="documentos" id="itens" src="/contasReceber/manterRedeco" cmd="item" 
			copyMasterTabProperties="true"
			masterTabId="cad"/>
		</adsm:tabGroup>
</adsm:window>