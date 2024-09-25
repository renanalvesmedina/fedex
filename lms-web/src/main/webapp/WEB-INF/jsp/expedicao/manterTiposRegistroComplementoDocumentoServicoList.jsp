<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction">
	<adsm:form action="/expedicao/manterTiposRegistroComplementoDocumentoServico" 
		idProperty="idTipoRegistroComplemento">
		<adsm:textbox maxLength="60" dataType="text" 
			property="dsTipoRegistroComplemento" label="descricao" size="33"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoRegistroComplemento"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="tipoRegistroComplemento" idProperty="idTipoRegistroComplemento" 
		gridHeight="200" unique="true" defaultOrder="dsTipoRegistroComplemento" rows="13">
		<adsm:gridColumn title="descricao" property="dsTipoRegistroComplemento" width="100%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
