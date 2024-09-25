<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.manterPrazoVencimentoDivisaoAction"
	onPageLoadCallBack="myPageLoad">

	<adsm:form
		action="/vendas/manterPrazoVencimentoDivisao">

		<adsm:complement
			label="cliente"
			labelWidth="17%"
			width="83%"
			separator="branco">

		<adsm:hidden
			property="idFilial"
			serializable="false"/>

		<adsm:hidden
			property="permissao"
			serializable="false"/>

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"
				size="20"/>

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false"
				size="30"/>
		</adsm:complement>

		<adsm:hidden
			property="divisaoCliente.idDivisaoCliente"/>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="divisao"
			labelWidth="17%"
			maxLength="60"
			property="divisaoCliente.dsDivisaoCliente"
			serializable="false"
			size="51"
			width="40%"/>

		<adsm:combobox
			domain="DM_MODAL"
			label="modal"
			labelWidth="13%"
			onchange="buscaServicosList();"
			property="tpModal"
			width="30%"/>

		<adsm:combobox
			domain="DM_ABRANGENCIA"
			label="abrangencia"
			labelWidth="17%"
			onchange="buscaServicosList();"
			property="tpAbrangencia"
			width="40%"/>

		<adsm:combobox
			boxWidth="215"
			label="servico"
			labelWidth="13%"
			onlyActiveValues="false"
			optionLabelProperty="dsServico"
			optionProperty="idServico"
			property="servico.idServico"
			service="lms.configuracoes.servicoService.find"
			width="30%">

			<adsm:propertyMapping
				modelProperty="tpAbrangencia"
				relatedProperty="tpAbrangencia"/>

			<adsm:propertyMapping
				modelProperty="tpModal"
				relatedProperty="tpModal"/>

		</adsm:combobox>

		<adsm:combobox
			domain="DM_TIPO_FRETE"
			label="tipoFrete"
			labelWidth="17%"
			property="tpFrete"
			width="83%"/>

		<adsm:buttonBar 
			freeLayout="true">

			<adsm:findButton
				callbackProperty="prazoVencimento"/>

			<adsm:button
				caption="limpar"
				id="limpar"
				buttonType="resetButton"
				onclick="myResetButtonScript()"
				disabled="false"/>

		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
		idProperty="idPrazoVencimento"
		gridHeight="200"
		property="prazoVencimento"
		onSelectRow="permissaoExcluir"
		onSelectAll="permissaoExcluir"
		unique="true">

		<adsm:gridColumn
			title="modal"
			property="tpModal"
			width="12%"
			isDomain="true"/>

		<adsm:gridColumn
			property="tpAbrangencia"
			title="abrangencia"
			width="14%"
			isDomain="true"/>

		<adsm:gridColumn
			property="servico.dsServico"
			title="servico"
			width="48%"/>

		<adsm:gridColumn
			property="tpFrete"
			title="tipoFrete"
			width="14%"
			isDomain="true"/>

		<adsm:gridColumn
			property="nrPrazoPagamento"
			title="prazoPagamento"
			width="12%"
			dataType="integer"/>


		<adsm:buttonBar>
			<adsm:removeButton
				id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function onShowListTab(){
		tab_onShow();
		buscaServicosList();
	}
	/************************************************************\
	*
	\************************************************************/
	function myResetButtonScript(){
		cleanButtonScript(undefined,undefined,undefined);
		buscaServicosList();
	}
	/************************************************************\
	*
	\************************************************************/
	function buscaServicosList(){
		var modal = getElementValue("tpModal");
		var abrangencia = getElementValue("tpAbrangencia");
	    var sdo = createServiceDataObject("lms.configuracoes.servicoService.find", "servico.idServico", {tpModal:modal,tpAbrangencia:abrangencia});
	   	xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
 	function permissaoExcluir(data){
		if (getElement("permissao").value!="true") {
			setDisabled("removeButton", true);
			return false;
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function myPageLoad_cb(){
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdd = createServiceDataObject("lms.vendas.manterPrazoVencimentoDivisaoAction.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdd]});
	}
	/************************************************************\
	*
	\************************************************************/
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
	/************************************************************\
	*
	\************************************************************/
	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";
</script>