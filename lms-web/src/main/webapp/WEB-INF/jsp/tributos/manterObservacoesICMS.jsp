<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterObservacoesICMS" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/tributos/manterObservacoesICMS" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/tributos/manterObservacoesICMS" cmd="cad" />
			<adsm:tab title="observacoes" id="item" src="/tributos/manterObservacoesICMS" masterTabId="cad" copyMasterTabProperties="true" cmd="item" />			
		</adsm:tabGroup>
</adsm:window>
