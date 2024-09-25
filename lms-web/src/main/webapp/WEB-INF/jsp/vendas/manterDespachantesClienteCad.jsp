<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.clienteDespachanteService">
	<adsm:i18nLabels>
		<adsm:include key="requiredField"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterDespachantesCliente" idProperty="idClienteDespachante" 
		service="lms.vendas.clienteDespachanteService.findByIdSpecificTela"
		onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:hidden property="despachante.pessoa.idPessoa"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>
		<adsm:lookup property="despachante" idProperty="idDespachante" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="despachante" action="vendas/manterDespachantes" 
			service="lms.vendas.manterDespachantesClienteAction.findLookupDespachantes" dataType="text" 
			size="20" maxLength="20" width="19%" onDataLoadCallBack="despachante_pessoa_nrIdentificacao"
			onPopupSetValue="myOnPopupSetValue"
			required="true">
			<adsm:propertyMapping
				criteriaProperty="tpSituacaoAtivo"
				modelProperty="tpSituacao"/>

			<adsm:propertyMapping 
				modelProperty="pessoa.nmPessoa"
				formProperty="despachante.pessoa.nmPessoa"/>
			<adsm:propertyMapping 
				modelProperty="tpTelefone.description"
				formProperty="tpTelefone.description"/>
			<adsm:propertyMapping 
				modelProperty="tpUso.description"
				formProperty="tpUso.description"/>
			<adsm:propertyMapping 
				modelProperty="nrDdi"
				formProperty="nrDdi"/>
			<adsm:propertyMapping 
				modelProperty="nrDdd"
				formProperty="nrDdd"/>
			<adsm:propertyMapping 
				modelProperty="nrTelefone"
				formProperty="nrTelefone"/>

			<adsm:textbox
				dataType="text"
				property="despachante.pessoa.nmPessoa"
				width="66%"
				disabled="true"
				size="30"/>
		</adsm:lookup>

		<adsm:combobox
			domain="DM_LOCAL_DESPACHO"
			label="local"
			property="tpLocal"
			required="true"
			onlyActiveValues="true"/>

		<adsm:textbox property="tpTelefone.description" label="tipoTelefone" dataType="text" size="30" disabled="true" serializable="false"/>
		<adsm:textbox property="tpUso.description" label="usoTelefone" dataType="text" size="30" disabled="true" serializable="false"/>
        <adsm:textbox label="ddi" dataType="integer" property="nrDdi" maxLength="5" size="5" disabled="true" serializable="false"/>
		<adsm:complement label="numero" width="35%">
            <adsm:textbox dataType="integer" property="nrDdd" maxLength="5" size="5" disabled="true" serializable="false"/>
        	<adsm:textbox dataType="text" property="nrTelefone" maxLength="10" size="10" disabled="true" serializable="false"/>
        </adsm:complement>

		<adsm:combobox domain="DM_STATUS" label="situacao" property="tpSituacao" required="true" onlyActiveValues="true"/>
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
		if (error == undefined){
			try {
				if (data.despachante.pessoa.idPessoa != undefined){
					buscarTelefone(data.despachante.pessoa.idPessoa);
				}	
			}catch(e){}		
		} else {
			alert(error+'');
		}
		initWindow();
	}

	function myOnPopupSetValue(data){
		if (data.pessoa.idPessoa!=undefined){
			buscarTelefone(data.pessoa.idPessoa);
		}
	}

	function despachante_pessoa_nrIdentificacao_cb(data){
		if (data.length == 1){
			buscarTelefone(data[0].pessoa.idPessoa);
		}
		return lookupExactMatch({e:document.getElementById("despachante.idDespachante"), data:data});
	}
	
	/**
	 * Busca o telefone padrão do despachante e popula os campos de telefone na tela.
	 * @param Long idPessoa
	 */
	function buscarTelefone(idPessoa){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject("lms.configuracoes.telefoneEnderecoService.findTelefoneEnderecoPadraoTela", "buscarTelefone", {idPessoa:idPessoa});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
		return true;	
	}
	
	function buscarTelefone_cb(data, error){
		if (error == null){
			fillFormWithFormBeanData(document.getElementById(mainForm).tabIndex, data);			
		} else {
			alert(error+'');
		}
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

	function validateTab(){
		if(getElementValue("despachante.idDespachante") == "") {
			alertRequiredField("despachante.idDespachante"); 
			setFocusOnFirstFocusableField();
			return false;
		}
		return validateTabScript(document.forms);
	}
</script>