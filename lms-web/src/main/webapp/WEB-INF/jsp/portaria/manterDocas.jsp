<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDocas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/portaria/manterDocas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/portaria/manterDocas" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  