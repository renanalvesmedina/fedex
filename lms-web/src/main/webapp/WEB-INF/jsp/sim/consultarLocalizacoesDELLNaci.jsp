<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacaoDetalhadaWeb">
		<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" scrollBars="horizontal" rows="10" gridHeight="350">
			<adsm:gridColumn width="100" title="pedido" property="PEDIDO" align="right"/>
			<adsm:gridColumn width="60" title="tipo" property="tipo" align="left"/>
			<adsm:gridColumn width="150" title="documentoServico" property="CTRC" align="right"/>
			<adsm:gridColumn width="100" title="notaFiscal" property="NF" align="left"/>
			<adsm:gridColumn width="100" title="volumes" property="VOLUMES" align="right"/>
			<adsm:gridColumn width="100" title="valorTotal" property="VALOR" align="right" />
			<adsm:gridColumn width="150" title="emissaoDocumentoServico" property="EMISSAO" align="center" />
			<adsm:gridColumn width="125" title="carregamento" property="CARREGAMENTO" align="center" />
			<adsm:gridColumn width="125" title="chegadaDestino" property="CHEGADA" align="center"/>
			<adsm:gridColumn width="125" title="entrega" property="ENTREGA" align="center" />
			<adsm:gridColumn width="100" title="tempoEvento" property="TEMPO" align="right" />
			<adsm:gridColumn width="200" title="localizacao" property="STATUS" />
		</adsm:grid>
		<adsm:buttonBar>
			<adsm:button caption="voltar" /> 
		</adsm:buttonBar>
</adsm:window>