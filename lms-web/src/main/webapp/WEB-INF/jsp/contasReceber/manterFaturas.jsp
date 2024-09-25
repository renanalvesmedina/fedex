 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterFaturas" type="main">
		<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterFaturas.do" cmd="list" onShow="myOnShow"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterFaturas.do" cmd="cad"/>
			<adsm:tab title="documentosServicoTitulo" masterTabId="cad" id="documentoServico" 
			src="/contasReceber/manterFaturas.do" cmd="item" boxWidth="170" copyMasterTabProperties="true"
			onShow="myOnShow"/>
			<adsm:tab title="anexos" id="anexos" src="/contasReceber/manterFaturas.do" masterTabId="cad" copyMasterTabProperties="true" cmd="anexos"/>
		</adsm:tabGroup>
</adsm:window>