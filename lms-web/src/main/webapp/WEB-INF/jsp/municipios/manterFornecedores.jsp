<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFornecedores" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterFornecedores" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterFornecedores" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  