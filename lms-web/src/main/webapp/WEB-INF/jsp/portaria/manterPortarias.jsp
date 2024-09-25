<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPortarias" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/portaria/manterPortarias" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/portaria/manterPortarias" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  