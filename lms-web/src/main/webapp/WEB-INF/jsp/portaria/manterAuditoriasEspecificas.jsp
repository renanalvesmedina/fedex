<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAuditoriasEspecificas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/portaria/manterAuditoriasEspecificas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/portaria/manterAuditoriasEspecificas" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
