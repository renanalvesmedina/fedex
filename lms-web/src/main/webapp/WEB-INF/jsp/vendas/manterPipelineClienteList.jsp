<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
function loadPage() {
	var data = new Array();
	var sdo = createServiceDataObject("lms.vendas.manterPipelineClienteAction.findDadosIniciaisListagem", "findDadosIniciais", data);
	xmit({serviceDataObjects:[sdo]});
}

function loadPage_cb(data, error) {
	onPageLoad_cb(data,error);
	if(error){
		alert(error);
		return false;
	}
}

function findDadosIniciais_cb(data, error) {
	setDisabled("usuarioByIdUsuario.idUsuario", false);
	if(data.isMatriz != 'true') {
		setElementValue("filial.idFilial", data.idFilial);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.nmFantasiaFilial);
		setElementValue("siglaDescricao", data.dsRegional);
	}
	
	if(data.idUsuario) {
		setElementValue("usuarioByIdUsuario.idUsuario", data.idUsuario);
		setElementValue("usuarioByIdUsuario.nrMatricula", data.nrMatricula);
		setElementValue("usuarioByIdUsuario.nmUsuario", data.nmFuncionario);
		setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data.dsFuncao);
		setDisabled("usuarioByIdUsuario.idUsuario", data.isMatriz != 'true');
	}
}

</script>
<adsm:window service="lms.vendas.manterPipelineClienteAction" onPageLoad="loadPage" onPageLoadCallBack="loadPage">
	<adsm:form action="/vendas/manterPipelineCliente" idProperty="idPipelineCliente">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01127"/>
	</adsm:i18nLabels>

	<adsm:hidden property="idMoedaUsuario"/>
	
	<adsm:lookup 
		property="filial" 
		idProperty="idFilial" 
		required="false" 
		criteriaProperty="sgFilial" 
		maxLength="3"
		service="lms.vendas.manterPipelineClienteAction.findLookupFilial" 
		dataType="text" 
		label="filial" size="3"
		action="/municipios/manterFiliais" 
		labelWidth="10%" width="43%" 
		minLengthForAutoPopUpSearch="3"
		exactMatch="false" disabled="false" onDataLoadCallBack="filialCallBack" afterPopupSetValue="filialAfterPopupSetValue">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="siglaDescricao" modelProperty="lastRegional.dsRegional" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
	</adsm:lookup>

	<adsm:hidden property="idRegional" serializable="false" />
	<adsm:textbox
		dataType="text" 
		label="regional"
		property="siglaDescricao" 
		disabled="true"
		size="35"
		maxLength="60"
		width="41%"
		labelWidth="6%"
		serializable="false"
		required="false"
	/>

	<adsm:lookup
		action="/configuracoes/consultarFuncionariosView"
		service="lms.vendas.manterPipelineClienteAction.findLookupFuncionario"
		dataType="text"
		property="usuarioByIdUsuario"
		idProperty="idUsuario"
		criteriaProperty="nrMatricula"
		label="vendedor"
		size="17"
		maxLength="10"
		exactMatch="true"
		width="43%"
		labelWidth="10%"
		onDataLoadCallBack="funcCallBack"
		onPopupSetValue="funcCallBack"
	>
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.nmUsuario"
			modelProperty="nmUsuario"/>
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.vfuncionario.dsFuncao"
			modelProperty="dsFuncao"/>
		<adsm:textbox
			dataType="text" 
			property="usuarioByIdUsuario.nmUsuario" 
			size="35" 
			maxLength="50" 
			disabled="true"
			serializable="false"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="text" 
			label="cargo"
			property="usuarioByIdUsuario.vfuncionario.dsFuncao" 
			maxLength="30" 
			size="35"
			disabled="true" 
			width="41%"
			labelWidth="6%"
			serializable="false"/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			maxLength="18"
			dataType="text"
			width="90%"
			labelWidth="10%">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="false" 
				serializable="true"/>
		</adsm:lookup>

		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" serializable="true" labelWidth="10%" width="90%"/>	

		<adsm:range label="dataInicio" labelWidth="10%" width="15%">
			<adsm:textbox dataType="JTDate" property="dtInicial" smallerThan="dtFinal"/>
			
		</adsm:range>
		<adsm:range label="dataTermino" labelWidth="12%" width="16%">
			<adsm:textbox dataType="JTDate" property="dtFinal" biggerThan="dtInicial"/>
		</adsm:range>

		<adsm:combobox property="tpSituacao" label="tpSituacao" domain="DM_SITUACAO_PIPELINE" serializable="true" labelWidth="10%" width="36%"/>	

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="pipelineCliente" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		idProperty="idPipelineCliente" 
		property="pipelineCliente" 
		unique="true"
		rowCountService="lms.vendas.manterPipelineClienteAction.getRowCountCustom" 
		service="lms.vendas.manterPipelineClienteAction.findPaginatedCustom"
		gridHeight="220"
		rows="9">
		<adsm:gridColumn
			title="funcionario"
			property="usuarioByIdUsuario.nmUsuario" 
			dataType="text"
			width="30%" />
		<adsm:gridColumn
			title="filial" 
			property="filial.sgFilial" 
			dataType="text"
			width="8%" 
			align="center"/>
		<adsm:gridColumn
			title="cliente" 
			property="cliente.pessoa.nmPessoa" 
			width="35%"
			dataType="text" />
		<adsm:gridColumn
			title="dataInicio" 
			property="dtEvento" 
			width="13%"
			dataType="JTDate" 
			align="center" />
		<adsm:gridColumn
			title="ultimaAtualizacao" 
			property="dtUltimaAtualizacao" 
			width="15%"
			dataType="JTDate" 
			align="center" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">

	function initWindow(eventObj) {
		var tabGroup = getTabGroup(this.document);
		if(eventObj.name == "tab_click"){
			tabGroup.getTab("etapas").getElementById("consultou").value = '';
		}
		if(eventObj.name == 'cleanButton_click'){
			resetValue(this.document);
			loadPage();
		}
		
		//desabilita a aba etapas
		tabGroup.setDisabledTab("etapas", true);
		
	}

	function resetFilial() {
		setElementValue("filial.idFilial", '');
		setElementValue("filial.sgFilial", '');
		setElementValue("filial.pessoa.nmFantasia", '');
		setElementValue("siglaDescricao", '');
	}
	
		
	/***** Lookup filial *****/
	function filialCallBack_cb(data){
		if(!filial_sgFilial_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			if(data[0].error) {
				alert(data[0].error);
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].nmFantasia);
			setElementValue("siglaDescricao", data[0].lastRegional.dsRegional);
				return;
			}
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].nmFantasia);
			setElementValue("siglaDescricao", data[0].lastRegional.dsRegional);
		}
		return;
	}
			
	function filialAfterPopupSetValue(data) {
		if(data != undefined) {
			if(data.error) {
				alert(data.error);
				return;
		}
			var sdo = createServiceDataObject("lms.vendas.manterPipelineClienteAction.validaFilialSelecionadaById", "filialAfterPopupSetValue", {'idFilial':data.idFilial});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function filialAfterPopupSetValue_cb(data, error) {
		if(error) {
			alert(error);
			resetFilial();
		return;
	}
	}

	/***** Lookup Funcionario *****/
	function funcCallBack_cb(data){
		if(!usuarioByIdUsuario_nrMatricula_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			
			setElementValue("usuarioByIdUsuario.idUsuario", data[0].idUsuario);
			setElementValue("usuarioByIdUsuario.nrMatricula", data[0].nrMatricula);
			setElementValue("usuarioByIdUsuario.nmUsuario", data[0].nmFuncionario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data[0].dsFuncao);
		}
		return;
	}

	function funcCallBack(data){
		if(data != undefined) {
			setElementValue("usuarioByIdUsuario.nrMatricula", data.nrMatricula);
			setElementValue("usuarioByIdUsuario.idUsuario", data.idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data.nmUsuario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data.dsFuncao);
		}
		return;
	}
</script>