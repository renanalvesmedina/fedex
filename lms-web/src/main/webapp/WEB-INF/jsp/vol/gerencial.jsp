<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="gerencial" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="filtros"  		 id="filtro" onHide="tabHideFiltros" src="/vol/gerencial" cmd="filtro"/>
		<adsm:tab title="acompanhamento" id="list"	onShow="tabShowAcompanhamento" onHide="tabHideAcompanhamento" src="/vol/gerencial" cmd="list"/>
		<adsm:tab title="detalhamento" 	 id="det" 	onShow="tabShowDetalhamento" onHide="tabHideDetalhamento" src="/vol/gerencial" cmd="det" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
