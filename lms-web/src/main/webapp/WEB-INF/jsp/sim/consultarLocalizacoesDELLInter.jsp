<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacaoDetalhadaWeb">
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" rows="10" scrollBars="horizontal" gridHeight="350">
		<adsm:gridColumn width="100" title="pedido" property="PEDIDO" align="right"/>
		<adsm:gridColumn width="140" title="pedidoInternacional" property="PEDIDOI" align="right"/>
		<adsm:gridColumn width="60" title="tipo" property="tipo" align="left"/>
		<adsm:gridColumn width="150" title="documentoServico" property="CRT" align="right"/>
		<adsm:gridColumn width="100" title="volumes" property="BULTOS" align="right"/>
		<adsm:gridColumn width="100" title="valorExportacao" property="VALORE" align="right"/>

		<adsm:gridColumn width="125" title="saidaDell" property="SAIDAD" align="center"/>
		<adsm:gridColumn width="160" title="emissaoDocumentoServico" property="EMISSAO" align="center"/>
		<adsm:gridColumn width="125" title="chegadaUruguaiana" property="CHEGADAU" align="center"/>
		<adsm:gridColumn width="125" title="saidaUruguaiana" property="SAIDAU" align="center"/>


		<adsm:gridColumn width="125" title="liberadaPll" property="LIBERADAPL" align="center"/>
		<adsm:gridColumn width="125" title="chegadaLoPrimo" property="CHEGADAP" align="center"/>
		<adsm:gridColumn width="125" title="liberadaLoPrimo" property="LIBERADALP" align="center"/>
		<adsm:gridColumn width="125" title="saidaLoPrimo" property="SAIDALP" align="center"/>


		<adsm:gridColumn width="125" title="chegadaAndreani" property="CHEGADAA" align="center"/>
		<adsm:gridColumn width="125" title="autorizacaoDell" property="AUTORIZACAO" align="center" />
		<adsm:gridColumn width="125" title="entregaFinal" property="ENTREGA" align="center" />
		<adsm:gridColumn width="100" title="tempoEvento" property="TEMPO" align="right"/>
		<adsm:gridColumn width="100" title="localizacao" property="LOCALIZACAO" align="left"/>
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="voltar" /> 
	</adsm:buttonBar>
</adsm:window>