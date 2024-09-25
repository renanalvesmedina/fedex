<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterUnidadesFederativas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterUnidadesFederativas" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterUnidadesFederativas" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>
