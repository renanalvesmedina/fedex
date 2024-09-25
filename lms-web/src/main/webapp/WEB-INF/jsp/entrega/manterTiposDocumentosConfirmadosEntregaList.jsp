<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposDocumentosConfirmadosEntrega" service="lms.entrega.manterTiposDocumentosConfirmadosEntregaAction">
	<adsm:form action="/entrega/manterTiposDocumentosConfirmadosEntrega">
		<adsm:textbox dataType="text" property="dsTipoDocumentoEntrega" label="descricao" size="60" maxLength="60" labelWidth="25%" width="75%" required="false" />
		<adsm:combobox property="tpDocumentoCobranca" domain="DM_TIPO_DOCUMENTO_COBRANCA" label="tipoDocumentoCobranca" prototypeValue="Boleto|Recibo"  labelWidth="25%" width="75%" required="false" />				
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="TipoDocumentoEntrega"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idTipoDocumentoEntrega" selectionMode="check" property="TipoDocumentoEntrega" defaultOrder="dsTipoDocumentoEntrega"  unique="true" rows="12"  >
		<adsm:gridColumn  title="descricao" property="dsTipoDocumentoEntrega" />
		<adsm:gridColumn width="20%" title="tipoDocumentoCobranca" property="tpDocumentoCobranca.description" />
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao.description" align="left" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
