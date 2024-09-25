<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTerminais" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/portaria/manterTerminais" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/portaria/manterTerminais" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  