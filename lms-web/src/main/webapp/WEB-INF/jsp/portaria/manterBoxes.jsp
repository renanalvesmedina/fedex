<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterBoxes" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/portaria/manterBoxes" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/portaria/manterBoxes" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  