<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.eventoClienteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterEventosCliente">
	    <adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox
			label="cliente"
			property="cliente.pessoa.nrIdentificacao"
			dataType="text"
			size="20"
			maxLength="20"
			labelWidth="15%"
			width="80%"
			disabled="true"
			serializable="false">
			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				size="60"
				maxLength="60"
				disabled="true"
				serializable="false"/>
		</adsm:textbox>

		<adsm:combobox
			property="evento.idEvento"
			optionLabelProperty="comboText"
			optionProperty="idEvento"
			service="lms.vendas.eventoClienteService.findEventoCombo"
			boxWidth="445"
			label="evento"
			width="90%" />
		<adsm:textbox
			maxLength="60"
			size="70"
			dataType="text"
			property="dsEventoCliente"
			label="descricao"
			width="90%"/>
		<adsm:combobox
			domain="DM_STATUS"
			label="situacao"
			property="tpSituacao"/>		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="eventoCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>                                                                           
	<adsm:grid
		idProperty="idEventoCliente"
		property="eventoCliente"
		defaultOrder="evento_.cdEvento,dsEventoCliente"
		gridHeight="200"
		unique="true"
		onSelectAll="myOnSelectAll"
		onSelectRow="myOnSelectRow">
		<adsm:gridColumn title="evento" property="dsEvento" width="30%" />
		<adsm:gridColumn title="descricao" property="dsEventoCliente" width="55%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="15%" isDomain="true"/>
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
		var sdo = createServiceDataObject("lms.vendas.eventoClienteService.validatePermissao", "validarPermissoes", data);
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

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";
</script>