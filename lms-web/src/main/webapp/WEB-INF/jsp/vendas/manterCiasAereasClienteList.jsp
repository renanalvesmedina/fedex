<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.ciaAereaClienteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterCiasAereasCliente">
	    <adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	
		<adsm:combobox label="ciaAerea" property="empresa.idEmpresa" service="lms.municipios.empresaService.findCiaAerea" optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa" onlyActiveValues="false" boxWidth="200"/>
		<adsm:combobox domain="DM_STATUS" label="situacao" property="tpSituacao"/>			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ciaAereaCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idCiaAereaCliente" property="ciaAereaCliente" defaultOrder="empresa_pessoa_.nmPessoa" gridHeight="200" unique="true" onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow" rows="13">
		<adsm:gridColumn title="ciaAerea" property="empresa.pessoa.nmPessoa" width="80%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="20%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.ciaAereaClienteService.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){		
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";

	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){

		var permissao = document.getElementById("permissao");
		
		if( permissao.value != "true" ){
			setDisabled("removeButton",true);
			return false;
		}
	}
	
	/**
	* Esta função deve executar exatamente a mesma tarefa que a função myOnSelectRow.
	*/
	function myOnSelectAll(){
		return myOnSelectRow();
	}

</script>
