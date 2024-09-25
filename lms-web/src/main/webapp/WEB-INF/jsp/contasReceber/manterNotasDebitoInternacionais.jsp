<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterNotasDebitoInternacionais" type="main">
		<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterNotasDebitoInternacionais.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterNotasDebitoInternacionais.do" cmd="cad"/>
			<adsm:tab title="crts" id="documentoServico" src="/contasReceber/manterNotasDebitoInternacionais.do" cmd="item" 
			masterTabId="cad" copyMasterTabProperties="true" onShow="myOnShow"/>
		</adsm:tabGroup>
</adsm:window>