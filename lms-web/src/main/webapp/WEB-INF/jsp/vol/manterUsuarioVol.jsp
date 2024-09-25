<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterUsuarioVol" type="main" >
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq"   src="/vol/manterUsuarioVol" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vol/manterUsuarioVol" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>