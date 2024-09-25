<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/gerarSimulacaoClienteDigitacaoNFs">
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" gridHeight="380" unique="true" showPaging="false" scrollBars="horizontal">
		<adsm:editColumn property="notaFiscal" title="notaFiscal" field="TextBox"  width="80" />
		<adsm:editColumn property="municipioOrigemId" title="municipioOrigem" field="Lookup" action="/municipios/manterMunicipios"  width="180" />
		<adsm:editColumn property="ufOrigemId" title="ufOrigem" field="ComboBox"  width="60" />
		<adsm:editColumn property="municipioDestinoId" title="municipioDestino" field="Lookup" action="/municipios/manterMunicipios"  width="180" />
		<adsm:editColumn property="ufOrigemId" title="ufDestino" field="ComboBox"  width="60" />
		<adsm:editColumn property="valorMercadoria" title="vlrMerc" unit="reais" field="TextBox"  width="80" />
		<adsm:editColumn property="pesoReal" title="pesoReal" field="TextBox" width="70" unit="kg"/>
		<adsm:editColumn property="pesoCubado" title="pesoCubado" field="TextBox" width="80" unit="kg"/>
		<adsm:editColumn property="qtdeVolumes" title="qtdeVolumes" field="TextBox" width="110" />
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="gravarNFs" />
	</adsm:buttonBar>
 	</adsm:form>
</adsm:window>
