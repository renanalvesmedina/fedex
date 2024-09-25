<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterRegiaoGeograficas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq"    src="/municipios/manterRegiaoGeograficas" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterRegiaoGeograficas" cmd="cad"  />
		</adsm:tabGroup>
</adsm:window>
