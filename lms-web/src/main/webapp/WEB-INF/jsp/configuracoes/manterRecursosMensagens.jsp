<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterRecursosMensagens" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterRecursosMensagens" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterRecursosMensagens" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>