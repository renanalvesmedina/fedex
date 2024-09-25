<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterRelacaoDocumentosDepositos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterRelacaoDocumentosDepositos.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterRelacaoDocumentosDepositos.do" cmd="cad"/>
			<adsm:tab title="documentos" masterTabId="cad" id="item" 
					onShow="myOnShow" src="/contasReceber/manterRelacaoDocumentosDepositos.do" 
			cmd="item" boxWidth="170" copyMasterTabProperties="true" />
		</adsm:tabGroup>
</adsm:window>