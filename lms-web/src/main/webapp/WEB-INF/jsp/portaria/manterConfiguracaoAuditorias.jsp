<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterConfiguracaoAuditorias" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="portaria/manterConfiguracaoAuditorias" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="portaria/manterConfiguracaoAuditorias" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>