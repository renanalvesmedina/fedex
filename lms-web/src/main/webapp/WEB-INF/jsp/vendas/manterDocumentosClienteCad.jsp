<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.documentoClienteService">
	<adsm:form action="/vendas/manterDocumentosCliente" idProperty="idDocumentoCliente" onDataLoadCallBack="myDataLoad">
	
		<adsm:hidden property="idFilial" serializable="false"/>
	
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" required="true"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" required="true"/>		
		<adsm:combobox property="tipoDocumentoEntrega.idTipoDocumentoEntrega" label="tipoDocumento" 
				optionLabelProperty="dsTipoDocumentoEntregaTpDocumentoCobranca" 
				optionProperty="idTipoDocumentoEntrega" service="lms.entrega.tipoDocumentoEntregaService.find" 
				required="true" onlyActiveValues="true" style="width:250"/>
		<adsm:checkbox label="faturaVinculada" property="blFaturaVinculada"/>
		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindow(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

</script>