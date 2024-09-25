<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCheckList" type="main"> 
	<adsm:tabGroup selectedTab="0" >
		<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterCheckList" cmd="list"  />
		<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterCheckList" cmd="cad" onShow="findInfUsuarioLogado" />
	</adsm:tabGroup>
</adsm:window>