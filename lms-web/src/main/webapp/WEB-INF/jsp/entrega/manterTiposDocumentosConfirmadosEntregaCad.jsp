<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.manterTiposDocumentosConfirmadosEntregaAction">
	<adsm:form action="/entrega/manterTiposDocumentosConfirmadosEntrega" idProperty="idTipoDocumentoEntrega">
		<adsm:textbox dataType="text" property="dsTipoDocumentoEntrega" label="descricao" size="60" maxLength="60" labelWidth="25%" width="75%" required="true" />
		<adsm:combobox property="tpDocumentoCobranca"  domain="DM_TIPO_DOCUMENTO_COBRANCA" label="tipoDocumentoCobranca" prototypeValue="Boleto|Recibo" service="" optionLabelProperty="" optionProperty="" labelWidth="25%" width="75%"  />				
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="25%" width="75%"  required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
