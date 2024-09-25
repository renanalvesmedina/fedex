<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTiposEquipamento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 	   id="pesq"    src="/vol/manterTiposEquipamento" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"		src="/vol/manterTiposEquipamento" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
