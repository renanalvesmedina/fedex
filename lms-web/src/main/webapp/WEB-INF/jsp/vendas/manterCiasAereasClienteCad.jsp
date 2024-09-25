<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.ciaAereaClienteService">
	<adsm:form action="/vendas/manterCiasAereasCliente" idProperty="idCiaAereaCliente" onDataLoadCallBack="myDataLoad">
	    <adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	
		<adsm:combobox label="ciaAerea" property="empresa.idEmpresa" service="lms.municipios.empresaService.findCiaAerea" optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa" onlyActiveValues="true" boxWidth="200" required="true"/>		
		<adsm:combobox domain="DM_STATUS" label="situacao" property="tpSituacao" required="true"/>				
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