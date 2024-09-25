<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterPreFaturas" type="main">
		<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterPreFaturas.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterPreFaturas.do" cmd="cad" onShow="myOnShow"/>
			<adsm:tab title="documentosServicoTitulo" id="documentoServico" masterTabId="cad"
			src="/contasReceber/manterPreFaturas.do" cmd="item" boxWidth="170" copyMasterTabProperties="true"
			onShow="myOnShow" />
		</adsm:tabGroup>
</adsm:window>