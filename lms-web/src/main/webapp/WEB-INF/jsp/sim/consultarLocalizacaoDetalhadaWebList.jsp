<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacaoDetalhadaWeb">
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" rows="16" unique="true" scrollBars="horizontal" >
		<adsm:gridColumn width="100" title="servico" property="servico" align="left" />
		<adsm:gridColumn width="100" title="notaFiscal" property="NOTA" align="left" />
		<adsm:gridColumn width="150" title="tipoDocumentoServico" property="tp" />
		<adsm:gridColumn width="150" title="documentoServico" property="DOCUMENTO" />
		<adsm:gridColumn width="40"  title="identificacaoRemetente" property="tipoIdRemetente" />
		<adsm:gridColumn width="150" title="" property="idRemetente" align="right"/>
		<adsm:gridColumn width="180" title="remetente" property="remetente"/>	
		<adsm:gridColumn width="40"  title="identificacaoDestinatario" property="tipoIdDestinatario" />
		<adsm:gridColumn width="150" title="" property="idDestinatario" align="right"/>
		<adsm:gridColumn width="180" title="destinatario" property="destinatario"/>	
		<adsm:gridColumn width="100" title="dataEntrega" property="ENTREGA" align="center"/>
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="voltar" /> 
	</adsm:buttonBar>
</adsm:window>