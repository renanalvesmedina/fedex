<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterGerarPreManifestoAction" onPageLoadCallBack="retornoCarregaPagina" >
	<adsm:form action="/carregamento/manterGerarPreManifesto" height="122">
		
		<adsm:hidden property="dhEmissaoManifesto" />
		<adsm:hidden property="filialSessao.idFilial" />
		<adsm:hidden property="filialSessao.sgFilial" />
		<adsm:hidden property="filialSessao.pessoa.nmFantasia" />
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		
		<adsm:combobox property="tpManifesto" label="operacao" 
					   domain="DM_TIPO_MANIFESTO" renderOptions="true"
					   labelWidth="18%" width="34%"
					   onchange="return carregaComboTipoPreManifesto()"/>

		<adsm:hidden property="tpPreManifestoAux" serializable="false"/>
		<adsm:combobox property="tpPreManifesto" label="tipoPreManifesto"
					   optionLabelProperty="description"
					   optionProperty="value" 
					   domain="DM_TIPO_MANIFESTO_ENTREGA"
					   labelWidth="16%" width="30%"
					   onchange="return setaTpPreManifestoAux(this.value)"/>
		
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="18%" width="34%" size="4" 
					 maxLength="3" picker="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			<adsm:lookup dataType="integer" property="controleCarga" 
						 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.carregamento.manterGerarPreManifestoAction.findControleCargaByNrControleByFilialPadrao" 
						 action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="loadDataByNrControleCarga" 
						 onDataLoadCallBack="loadDataByNrControleCarga" 
						 onchange="return checkValueControleCarga(this.value)"
						 maxLength="8" size="8" mask="00000000"
						 popupLabel="pesquisarControleCarga">
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" disable="false" />
 				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/>
 				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/>
 				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" serializable="false" />
		<adsm:textbox dataType="text" property="filialByIdFilialOrigem.sgFilial" 
					  label="preManifesto" labelWidth="16%" width="30%" size="4" disabled="true">
			<adsm:textbox dataType="integer" property="nrPreManifesto" size="8" 
						  maxLength="8" mask="00000000" />
		</adsm:textbox>		
		
		<adsm:lookup dataType="integer" property="controleCarga.rotaIdaVolta"
					 idProperty="idRotaIdaVolta" criteriaProperty="nrRota"
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupRotaIdaVolta"  
					 action="/municipios/consultarRotas"
					 label="rotaViagem" labelWidth="18%" width="34%"
					 size="4" maxLength="4" exactMatch="false"					 
					 cellStyle="vertical-align:bottom;" mask="0000" cmd="idaVolta" disabled="true">			
			<adsm:propertyMapping relatedProperty="controleCarga.rotaIdaVolta.rota.dsRota" modelProperty="rota.dsRota"/>
			<adsm:textbox dataType="text" property="controleCarga.rotaIdaVolta.rota.dsRota" size="30" 
						  cellStyle="vertical-align:bottom;" disabled="true" serializable="true"/>			
		</adsm:lookup>		
        
		<adsm:lookup dataType="integer" property="controleCarga.rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupRotaColetaEntrega" 
					 action="/municipios/manterRotaColetaEntrega"
					 label="rotaColetaEntrega" labelWidth="16%" width="30%" size="5" 
					 maxLength="5" exactMatch="true" disabled="true">
			<adsm:propertyMapping criteriaProperty="filialSessao.idFilial" modelProperty="filial.idFilial" disable="true"/>
			<adsm:propertyMapping criteriaProperty="filialSessao.sgFilial" modelProperty="filial.sgFilial" disable="true"/>
			<adsm:propertyMapping criteriaProperty="filialSessao.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" disable="true"/>
        	<adsm:propertyMapping relatedProperty="controleCarga.rotaColetaEntrega.dsRota" modelProperty="dsRota" inlineQuery="false"/>
	        <adsm:textbox dataType="text" property="controleCarga.rotaColetaEntrega.dsRota" size="28" disabled="true" serializable="false"/>
        </adsm:lookup>
		
		<adsm:lookup dataType="text" property="filialByIdFilialDestino" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
				 	 service="lms.carregamento.manterGerarPreManifestoAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="destino" size="4" maxLength="3" labelWidth="18%" width="34%" disabled="true">
	        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" 
						  size="30" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:hidden property="idPostoAvancado" />
		<adsm:textbox dataType="text" property="dsPostoAvancado" label="postoAvancado" 
					  labelWidth="16%" width="30%" disabled="true" serializable="false"/>

		<adsm:hidden property="solicitacaoRetirada.filialRetirada.pessoa.nmFantasia" serializable="false"/> 
		<adsm:lookup dataType="text" property="solicitacaoRetirada.filialRetirada" 
					 idProperty="idFilial" criteriaProperty="sgFilial"
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais"
					 onchange="return sgFilialSolicitacaoRetiradaOnChangeHandler();" 
					 onDataLoadCallBack="disableNrSolicitacaoRetirada"				 
					 label="solicitacaoRetirada" labelWidth="18%" width="82%" 
					 size="4" maxLength="3" picker="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="solicitacaoRetirada.filialRetirada.pessoa.nmFantasia"/>
			<adsm:lookup dataType="integer" property="solicitacaoRetirada" idProperty="idSolicitacaoRetirada"
						 criteriaProperty="nrSolicitacaoRetirada" 
						 service="lms.carregamento.manterGerarPreManifestoAction.findLookupSolicitacaoRetirada"
						 action="/sim/registrarSolicitacoesRetirada" cmd="list" 
						 onDataLoadCallBack="loadDataByNrSolicitacaoRetirada"
						 onPopupSetValue="loadDataByNrSolicitacaoRetirada"
						 size="8" maxLength="8" mask="00000000">						 
				<adsm:propertyMapping modelProperty="filial.idFilial" />
 				<adsm:propertyMapping criteriaProperty="solicitacaoRetirada.filialRetirada.idFilial" modelProperty="filialRetirada.idFilial"/>
 				<adsm:propertyMapping criteriaProperty="solicitacaoRetirada.filialRetirada.sgFilial" modelProperty="filialRetirada.sgFilial"/>
 				<adsm:propertyMapping criteriaProperty="solicitacaoRetirada.filialRetirada.pessoa.nmFantasia" modelProperty="filialRetirada.pessoa.nmFantasia"/> 				
				<adsm:propertyMapping criteriaProperty="filialSessao.idFilial" modelProperty="filialRetirada.idFilial" disable="true" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filialSessao.sgFilial" modelProperty="filialRetirada.sgFilial" disable="true" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filialSessao.pessoa.nmFantasia" modelProperty="filialRetirada.pessoa.nmFantasia" disable="true" inlineQuery="false"/>
 				<adsm:propertyMapping modelProperty="idFilialRetirada" relatedProperty="solicitacaoRetirada.filialRetirada.idFilial" blankFill="false"/>
 				<adsm:propertyMapping modelProperty="sgFilialRetirada" relatedProperty="solicitacaoRetirada.filialRetirada.sgFilial" blankFill="false"/>
 				<adsm:propertyMapping modelProperty="nmFantasiaRetirada" relatedProperty="solicitacaoRetirada.filialRetirada.pessoa.nmFantasia" blankFill="false"/> 				
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:hidden property="cliente.tpSituacao" value="A" />
		<adsm:lookup label="consignatario" 
					 idProperty="idCliente" 
					 property="cliente" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupCliente"
					 action="/vendas/manterDadosIdentificacao" 
					 dataType="text" size="18" maxLength="18" 
					 labelWidth="18%" width="82%" disabled="true">
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="findButton" caption="consultar" buttonType="findButton" disabled="false" onclick="consultarDados(this.form)"/>
			<adsm:button id="cleanButton" caption="limpar" disabled="false" onclick="limpaTela()"/>			
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idManifesto" property="manifesto" selectionMode="check" autoSearch="true"
			   unique="true" scrollBars="both" gridHeight="165" onDataLoadCallBack="onDataLoadCallBack"
			   service="lms.carregamento.manterGerarPreManifestoAction.findPaginatedManifesto"
			   rowCountService="lms.carregamento.manterGerarPreManifestoAction.getRowCountManifesto">
			   
		<adsm:gridColumnGroup separatorType="MANIFESTO">
    		<adsm:gridColumn title="preManifesto" property="sgFilialOrigem" width="30"/>
    		<adsm:gridColumn property="nrPreManifesto" title="" dataType="integer" width="70" mask="00000000"/>
        </adsm:gridColumnGroup>
		<adsm:gridColumn title="dataHoraGeracao" property="dhGeracaoPreManifesto" dataType="JTDateTimeZone" width="150" align="center"/>		
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
    		<adsm:gridColumn title="controleCarga" property="sgFilialOrigemControleCarga" width="50"/>
    		<adsm:gridColumn property="nrControleCarga" title="" dataType="integer" width="90" mask="00000000"/>
        </adsm:gridColumnGroup>		
		<adsm:gridColumn title="rotaViagem" property="dsRotaIdaVolta" width="150" dataType="text"/>
		<adsm:gridColumn title="rotaColetaEntrega" property="dsRotaColetaEntrega" width="150" dataType="text"/>
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="70" dataType="text"/>		
		<adsm:gridColumn title="tipo" property="tpManifesto" width="100" isDomain="true"/>
		<adsm:gridColumn title="modal" property="tpModal" width="100" isDomain="true"/>
		<adsm:gridColumn title="status" property="tpStatusManifesto" width="150" isDomain="true"/>
		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">

	/**
	 * Carrega dados do usuario
	 */
	function retornoCarregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		if (error == undefined) {
			var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.getDadosSessao", "carregaDadosSessao");
	    	xmit({serviceDataObjects:[sdo]});
		}
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var dadosSessao;
	function carregaDadosSessao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		dadosSessao = data;
		carregaDadosNaTela();
	}


	function initWindow(eventObj) {
		setDisabled("cleanButton", false);
		var tabGroup = getTabGroup(this.document);
		if (tabGroup != undefined) {
			tabGroup.setDisabledTab("documentos", false);
		}
		
		if (getElementValue("controleCarga.idControleCarga") == "" && 
				getElementValue("solicitacaoRetirada.idSolicitacaoRetirada") == "" && 
				getElementValue("nrPreManifesto") == "") {
			this.document.getElementById("tpManifesto").required = "true";
			this.document.getElementById("tpPreManifesto").required = "true";
		}
		
		if(getElementValue("controleCarga.filialByIdFilialOrigem.idFilial") == "") {
			setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
		}
		
		if(getElementValue("solicitacaoRetirada.filialRetirada.idFilial") == "") {
			setDisabled(document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada"), true, undefined, true);
		}		
	}

	/**
	 * Carrega os dados basicos da tela
	 */
	function carregaDadosNaTela() {			
		setElementValue("filialSessao.idFilial", dadosSessao.filialSessao.idFilial);
		setElementValue("filialSessao.sgFilial", dadosSessao.filialSessao.sgFilial);
		setElementValue("filialSessao.pessoa.nmFantasia", dadosSessao.filialSessao.pessoa.nmFantasia);
		setElementValue("idPostoAvancado", dadosSessao.postoAvancado.idPostoAvancado);
		setElementValue("dsPostoAvancado", dadosSessao.postoAvancado.dsPostoAvancado);
		
		setElementValue("filialByIdFilialOrigem.idFilial", dadosSessao.filialSessao.idFilial);
		setElementValue("filialByIdFilialOrigem.sgFilial", dadosSessao.filialSessao.sgFilial);		
	}

	/**
	 * Controla o objeto de controle carga
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") == "") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			resetView();
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}
	
	function disableNrControleCarga_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data.length==0) 
			disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	/**
	 * Desabilita o campo numero do controle de carga
	 */
	function disableNrControleCarga(disable) {
		document.getElementById("controleCarga.nrControleCarga").disabled = disable;
	}
	
	/**
	 * Chama a consulta de 'findControleCargaByNrControleByFilialPadrao' a partir de um dos dados retornados 
	 * da lookup
	 */
	function loadDataByNrControleCarga(data) {	
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.findControleCargaByNrControleByFilialPadrao", "loadDataByNrControleCarga", data);
    	xmit({serviceDataObjects:[sdo]}); 
    	setDisabled("controleCarga.nrControleCarga", false);   	
	}
	
	/**
	 * Carrega os dados da tela de descarregarVeiculos apartir dos dados retornados da 
	 * consulta de 'findControleCargaByNrControleByFilial'
	 */
	function loadDataByNrControleCarga_cb(data, error){
		if (error != undefined) {
			alert(error);
			return false;
		}
		controleCarga_nrControleCarga_exactMatch_cb(data);
	
		//Verifica se este objeto e nulo
		if (data[0]!=undefined) {
			setElementValue("controleCarga.idControleCarga", data[0].idControleCarga);
			setElementValue("controleCarga.nrControleCarga", data[0].nrControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data[0].filialByIdFilialOrigem.idFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
			
			// Formata o campo de nrControlecarga
			format(document.getElementById("controleCarga.nrControleCarga"));
		} else {
			resetView();
			setFocus(document.getElementById("controleCarga.idControleCarga"));
		}
	}			
	
	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueControleCarga(valor) {		
		controleCarga_nrControleCargaOnChangeHandler();
		if (valor=="") {			
			var idFilial = getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
            var sgFilial = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");
            resetView();
            setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
		}		
		return true;
	}
	
	/**
	 * Reseta a tela deixa
	 */
	function resetView(){
		resetValue(this.document);
		carregaDadosNaTela();
	}	

	/**
	 * Carrega combo de Tipo de Pré-Manifesto.
	 */
	 function carregaComboTipoPreManifesto() {
	 	if (getElementValue("tpManifesto") == "E") {
	 		setDisabled("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", false);
	 		setDisabled("cliente.idCliente", false);
	 		setDisabled("controleCarga.rotaIdaVolta.idRotaIdaVolta", true);
	 		setDisabled("filialByIdFilialDestino.idFilial", true); 		
	 		
	 	} else if (getElementValue("tpManifesto") == "V") {	 	
	 		setDisabled("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", true);
	 		setDisabled("cliente.idCliente", true);
	 		setDisabled("controleCarga.rotaIdaVolta.idRotaIdaVolta", false);
	 		setDisabled("filialByIdFilialDestino.idFilial", false);	 		
	 		
	 	} else {
	 		setDisabled("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", true);
	 		setDisabled("cliente.idCliente", true);
	 		setDisabled("controleCarga.rotaIdaVolta.idRotaIdaVolta", true);
	 		setDisabled("filialByIdFilialDestino.idFilial", true);	
	 	}

    	var tpManifesto = new Array();
	    setNestedBeanPropertyValue(tpManifesto, "tpManifesto", getElementValue("tpManifesto"));
	    var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.findComboTipoPreManifesto", "tpPreManifesto", tpManifesto);
    	xmit({serviceDataObjects:[sdo]});

	    return true;
	}

	
	/**
	 * Seta hidden de Tipo de Pré-Manifesto Auxiliar.
	 */
	function setaTpPreManifestoAux(value) {
		setElementValue("tpPreManifestoAux", value);
		return true;
	}
	
	/**
	 * OnDataLoadCallback da grid.
	 */
	function onDataLoadCallBack_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		setElementValue("tpPreManifesto", getElementValue("tpPreManifestoAux"));
	}
	
	/**
	 * Função que limpa a tela carrega os dados da sessão
	 */
	function limpaTela() {
		cleanButtonScript(this.document);
		manifestoGridDef.resetGrid();
		carregaDadosNaTela();
		setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
		setDisabled(document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada"), true, undefined, true);
 		setDisabled("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", true);
 		setDisabled("cliente.idCliente", true);		
	}
	
	/**
	 * Controla o objeto da solicitação de retirada.
	 */	
	function sgFilialSolicitacaoRetiradaOnChangeHandler() {
		if (getElementValue("solicitacaoRetirada.filialRetirada.sgFilial") == "") {
			disableNrSolicitacaoRetirada(true);
			resetValue("solicitacaoRetirada.idSolicitacaoRetirada");
		} else {
			disableNrSolicitacaoRetirada(false);
		}
		return lookupChange({e:document.forms[0].elements["solicitacaoRetirada.filialRetirada.idFilial"]});
	}
	
	function disableNrSolicitacaoRetirada_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data.length==0) 
			disableNrSolicitacaoRetirada(false);
		return lookupExactMatch({e:document.getElementById("solicitacaoRetirada.filialRetirada.idFilial"), data:data});
	}
	
	/**
	 * Desabilita o campo numero da solicitação de retirada.
	 */
	function disableNrSolicitacaoRetirada(disable) {
		document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada").disabled = disable;
	}
	
	/**
	 * Função da consulta de registros para a grid de manifestos.
	 */
	function consultarDados(form) {
		if (getElementValue("controleCarga.idControleCarga") == "" && 
				getElementValue("solicitacaoRetirada.idSolicitacaoRetirada") == "" && 
				getElementValue("nrPreManifesto") == "") {
			this.document.getElementById("tpManifesto").required = "true";
			this.document.getElementById("tpPreManifesto").required = "true";
		} else {
			this.document.getElementById("tpManifesto").required = "false";
			this.document.getElementById("tpPreManifesto").required = "false";		
		}
	
		if (!validateForm(form)) {
			return false;
		}
		var fb = buildFormBeanFromForm(form, 'LIKE_END'); 
		manifesto_cb(fb);	
	}
	
	/**
	 * Função chamada no onDataLoadCallBack da lookup de Solicitação de Retirada.
	 */
	function loadDataByNrSolicitacaoRetirada_cb(data, error){
		solicitacaoRetirada_nrSolicitacaoRetirada_exactMatch_cb(data);
				
		//Verifica se este objeto e nulo
		if (data[0] != undefined) {			
			setElementValue("solicitacaoRetirada.filialRetirada.idFilial", data[0].filialRetirada.idFilial);
			setElementValue("solicitacaoRetirada.filialRetirada.sgFilial", data[0].filialRetirada.sgFilial);
			setElementValue("solicitacaoRetirada.filialRetirada.pessoa.nmFantasia", data[0].filialRetirada.pessoa.nmFantasia);	
		}
	}		
	
	/**
	 * Função chamada ao clicar no registro da pop-up de Solicitação de Retirada.
	 */
	function loadDataByNrSolicitacaoRetirada(data) {
		disableNrSolicitacaoRetirada(false);
	}	
	
</script>