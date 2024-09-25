<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterConhecimentosSeremTransferidos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterConhecimentosSeremTransferidos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterConhecimentosSeremTransferidos" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
