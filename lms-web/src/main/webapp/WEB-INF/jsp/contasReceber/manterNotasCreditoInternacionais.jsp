<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterNotasCreditoInternacionais" type="main">
		<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterNotasCreditoInternacionais.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterNotasCreditoInternacionais.do" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>