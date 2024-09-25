<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterPrestadoresServico" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/carregamento/manterPrestadoresServico" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterPrestadoresServico" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
