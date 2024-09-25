<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterGruposFrotas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vol/manterGruposFrotas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vol/manterGruposFrotas" cmd="cad"/>
		<adsm:tab title="meiosTransporte" id="frotas" onShow="tabShowFrotas" src="/vol/manterGruposFrotas" cmd="frotas" masterTabId="cad" copyMasterTabProperties="true"/>
		<adsm:tab title="usuarios" id="usuarios"  src="/vol/manterGruposFrotas" cmd="usuarios" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
