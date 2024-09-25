<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarMunicipios" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/consultarMunicipios" cmd="list" height="440" />
			<adsm:tab title="consultarMunicipiosInf" id="cad" src="/municipios/consultarMunicipios" cmd="inf" disabled="true" height="440" boxWidth="120" copyMasterTabProperties="true"/>
			<adsm:tab title="atendimento" id="aten" src="/municipios/consultarMunicipios" cmd="aten" disabled="true" relatedIdTab="cad" relatedSourceProperty="idMunicipio" onShow="selecionaAbaList"/>
			<adsm:tab title="feriados" id="feriados" src="/municipios/consultarMunicipios" cmd="feriados" height="440" disabled="true" onShow="detalhamentoAba"/>
		</adsm:tabGroup>
</adsm:window>