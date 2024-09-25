<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterGerarPreManifestoAction" onPageLoadCallBack="retornoCarregaPagina" >
	<adsm:form action="/carregamento/manterGerarPreManifesto" idProperty="idManifesto" onDataLoadCallBack="onDataLoadCallback">
		
		<adsm:hidden property="origem" />
		<adsm:hidden property="sgFilialNrPreManifesto" />
		<adsm:hidden property="manifesto.controleTrecho.idControleTrecho" />
		<adsm:hidden property="filialSessao.idFilial" />
		<adsm:hidden property="filialSessao.sgFilial" />
		<adsm:hidden property="filialSessao.pessoa.nmFantasia" />
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>		
				
		<adsm:hidden property="filialByIdFilialOrigem.idFilial"/>
		<adsm:textbox dataType="text" property="filialByIdFilialOrigem.sgFilial" 
					  label="preManifesto" labelWidth="18%" width="36%" size="4" maxLength="3" 
					  disabled="true" serializable="false">
			<adsm:textbox dataType="integer" property="nrPreManifesto" size="8" 
						  maxLength="8" mask="00000000" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox property="dhGeracaoPreManifesto" label="dataHoraGeracao" dataType="JTDateTimeZone" 
					  width="31%" picker="false" disabled="true"/>

		<adsm:combobox property="tpManifesto" label="operacao" 
					   domain="DM_TIPO_MANIFESTO" renderOptions="true"
					   labelWidth="18%" width="36%" required="true"
					   onchange="return carregaComboTipoPreManifesto(true)"/>
		
		<adsm:combobox property="tpPreManifesto" label="tipoPreManifesto"
					   optionLabelProperty="description"
					   optionProperty="value"
					   service="lms.carregamento.manterGerarPreManifestoAction.findComboEmBranco"
					   width="31%" required="true"
					   onchange="return habilitaCamposByTpPreManifesto()"/>

		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" renderOptions="true"
					   labelWidth="18%" width="36%" disabled="true" onchange="return limpaAbaDocumentos()"/>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" width="31%" renderOptions="true"
					   domain="DM_ABRANGENCIA" disabled="true" onchange="return limpaAbaDocumentos()"/>
		
		<adsm:hidden property="controleCarga.tpControleCarga" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="18%" width="82%" size="4" 
					 maxLength="3" picker="false" serializable="false" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			<adsm:lookup dataType="integer" property="controleCarga" 
						 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.carregamento.manterGerarPreManifestoAction.findControleCargaByNrControleByFilial" 
						 action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="loadDataByNrControleCarga" 
						 onDataLoadCallBack="loadDataByNrControleCarga" 
						 onchange="return onControleCargaChange(this)"
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
        
        <adsm:hidden property="controleCarga.rotaIdaVolta.idRotaIdaVolta"/>
        <adsm:textbox label="rotaViagem" dataType="integer" property="controleCarga.rotaIdaVolta.nrRota" 
        			  labelWidth="18%" width="82%" disabled="true" size="4" serializable="false">
	        <adsm:textbox dataType="text" property="controleCarga.rotaIdaVolta.rota.dsRota" size="30" 
	        			  disabled="true" serializable="false"/>
	    </adsm:textbox>
	    
	    <adsm:hidden property="controleCarga.rotaColetaEntrega.idRotaColetaEntrega"/>
        <adsm:textbox label="rotaColetaEntrega" dataType="integer" property="controleCarga.rotaColetaEntrega.nrRota" 
        			  labelWidth="18%" width="82%" disabled="true" size="5" serializable="false">
	        <adsm:textbox dataType="text" property="controleCarga.rotaColetaEntrega.dsRota" size="30" 
	        			  disabled="true" serializable="false"/>
	    </adsm:textbox>

		<adsm:lookup dataType="text" property="filialByIdFilialDestino" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 criteriaSerializable="true"
				 	 service="lms.carregamento.manterGerarPreManifestoAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
				 	 onPopupSetValue="verificaDestino" 
					 onDataLoadCallBack="verificaDestino"
					 label="destino" size="4" maxLength="3" labelWidth="18%" width="36%" disabled="true">
	        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" 
						  size="30" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:hidden property="idPostoAvancado" serializable="false"/>
		<adsm:textbox dataType="text" property="dsPostoAvancado" label="postoAvancado" 
					  width="31%" disabled="true" serializable="false"/>

		<adsm:hidden property="solicitacaoRetirada.filialRetirada.pessoa.nmFantasia" serializable="false"/> 
		<adsm:lookup dataType="text" property="solicitacaoRetirada.filialRetirada" 
					 idProperty="idFilial" criteriaProperty="sgFilial"
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais"
					 onchange="return sgFilialSolicitacaoRetiradaOnChangeHandler();" 
					 onDataLoadCallBack="disableNrSolicitacaoRetirada"				 
					 label="solicitacaoRetirada" labelWidth="18%" width="82%" 
					 size="4" maxLength="3" picker="false" serializable="false" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="solicitacaoRetirada.filialRetirada.pessoa.nmFantasia"/>
			<adsm:lookup dataType="integer" property="solicitacaoRetirada" idProperty="idSolicitacaoRetirada"
						 criteriaProperty="nrSolicitacaoRetirada" 
						 service="lms.carregamento.manterGerarPreManifestoAction.findLookupSolicitacaoRetirada"
						 action="/sim/registrarSolicitacoesRetirada" cmd="list"
						 onDataLoadCallBack="loadDataByNrSolicitacaoRetirada" 
						 onPopupSetValue="loadDataByNrSolicitacaoRetirada"
						 onchange="return limpaDocumentosSolicitacaoRetirada();"
						 size="8" maxLength="8" mask="00000000" disabled="true">						 
				<adsm:propertyMapping modelProperty="filial.idFilial" disable="false" />
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
						  size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:textarea property="obManifesto" label="observacao" maxLength="200" 
					   columns="70" rows="3" labelWidth="18%" width="82%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="visualizar" onclick="imprimeRelatorio()"/>
			<adsm:storeButton id="storeButton" callbackProperty="salvarRegistro" caption="salvar" />			
			<adsm:button caption="novo" id="newButton" buttonType="newButton" onclick="resetView(true)"/>
			<adsm:button caption="excluir" id="removeButton" buttonType="removeButton" onclick="removeManifesto()"/>
		</adsm:buttonBar>
		
		<script>
			var lms_05039 = '<adsm:label key="LMS-05039"/>';
			var lms_05040 = '<adsm:label key="LMS-05040"/>';
			var lms_05042 = '<adsm:label key="LMS-05042"/>';
		</script>		
	</adsm:form>
	
</adsm:window>

<script type="text/javascript">
	
	function retornoCarregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);		
		if (error == undefined) {
			var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.newMaster");
			var sdo2 = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.getDadosSessao", "carregaDadosSessao");
			xmit({serviceDataObjects:[sdo, sdo2]});
		}
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var dadosSessao;
	function carregaDadosSessao_cb(data, error) {
		dadosSessao = data;
		carregaDadosSessaoParaTela();
		
		// Se a tela for chamada pelo botão do Gerar Controle de Carga.	
		// Propriedades com '*' na frente do nome é para identificar que não devem ser desabilitados pelo MasterLink.
		if (getElementValue("origem") != "") {
			var url = new URL(parent.location.href);
			inicializaCampos();
			carregaComboTipoPreManifesto(true);
			setElementValue("origem", url.parameters.origem);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", url.parameters["*controleCarga.filialByIdFilialOrigem.idFilial"]);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", url.parameters["*controleCarga.filialByIdFilialOrigem.sgFilial"]);
			setElementValue("controleCarga.nrControleCarga", url.parameters["*controleCarga.nrControleCarga"]);
						
			//Comentado, pois Camila pediu para desabilitar o nro.
			//disableNrControleCarga(false);
			if (getElementValue("controleCarga.nrControleCarga") != "") {
				setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true);
				setDisabled("controleCarga.idControleCarga", true);
			}

			lookupChange({e:document.getElementById("controleCarga.idControleCarga"), forceChange:true});
		}
	}

	/**
	 * Carrega os dados da sessão da tela
	 */
	function carregaDadosSessaoParaTela() {
		setElementValue("filialSessao.idFilial", dadosSessao.filialSessao.idFilial);
		setElementValue("filialSessao.sgFilial", dadosSessao.filialSessao.sgFilial);
		setElementValue("filialSessao.pessoa.nmFantasia", dadosSessao.filialSessao.pessoa.nmFantasia);	
		setElementValue("idPostoAvancado", dadosSessao.postoAvancado.idPostoAvancado);
		setElementValue("dsPostoAvancado", dadosSessao.postoAvancado.dsPostoAvancado);
	}		
	
	/**
	 * Ao carregar a tela, chama essa função
	 */
	function initWindow(eventObj) {		
		setDisabled("newButton", false);		
		if (eventObj.name == "tab_click" && getElementValue("tpManifesto") == "") {
			setDisabled("tpManifesto", false);
			setDisabled("tpPreManifesto", false);			
			carregaDados();
		}
		
		if (getElementValue("idManifesto") == "") {			
			setDisabled("btnVisualizar", true);
		} else {
			setDisabled("btnVisualizar", false);
		}		
	}
	
	/**
	 * Função que retorna os dados ao clicar em um registro na tab list.
	 */
	function onDataLoadCallback_cb(data, error) {
		inicializaCampos();
		onDataLoad_cb(data, error);		
		carregaComboTipoPreManifesto(false);		
		habilitaCamposByTpPreManifesto();
		carregaDadosSessaoParaTela();
		
		if(getElementValue("idManifesto") != "") {		
			if (getElementValue("tpPreManifesto") == "RV" || data.desabilitaControleCarga != undefined) {
				getTabGroup(this.document).setDisabledTab("documentos", true);
				if (getElementValue("tpPreManifesto") == "RV") {
					setDisabled("removeButton", false);
				} else {
					setDisabled("removeButton", true);
				}
			} else {
				getTabGroup(this.document).setDisabledTab("documentos", false);			
				setDisabled("removeButton", false);
			}
					
			setDisabled("btnVisualizar", false);			
			setDisabled("tpManifesto", true);
			setDisabled("tpPreManifesto", true);
			inicializaCampos();
		} else {
			setDisabled("tpManifesto", false);
			setDisabled("tpPreManifesto", false);
		}
	}

	
	/**
	 * Carrega os dados
	 */
	function carregaDados() {
		carregaDadosSessaoParaTela();
		inicializaCampos();		
	}

	
	/**
	 * Retorno do Salvamento do registro de Pedido Coleta
	 */
	function salvarRegistro_cb(data, erros, errorMsg, eventObj) {
		store_cb(data, erros, errorMsg, eventObj);
		
		if(!erros) {			
			setElementValue("idManifesto", data.idManifesto);
			setElementValue("filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
			setElementValue("filialByIdFilialOrigem.sgFilial", data.filialByIdFilialOrigem.sgFilial);
			setElementValue("nrPreManifesto", setFormat(document.getElementById("nrPreManifesto"), data.nrPreManifesto));
			setElementValue("dhGeracaoPreManifesto", setFormat(document.getElementById("dhGeracaoPreManifesto"), data.dhGeracaoPreManifesto));
			setElementValue("sgFilialNrPreManifesto", data.filialByIdFilialOrigem.sgFilial + " " + 
												setFormat(document.getElementById("nrPreManifesto"), data.nrPreManifesto));			

			setDisabled("btnVisualizar", false);
			setDisabled("tpManifesto", true);
			setDisabled("tpPreManifesto", true);
			
			// Pega a aba 'documentos' para setar os valores nos properties
			var tabGroup = getTabGroup(this.document);
			var tabDet = tabGroup.getTab("documentos");
			var abaDocumentos = tabDet.tabOwnerFrame.window;
			
			abaDocumentos.setDisabled("carregarVeiculo", false);
			
			// Seta o número na barra verde
			setElementValue(tabDet.getElementById("_sgFilialNrPreManifesto"), data.filialByIdFilialOrigem.sgFilial + " " + setFormat(document.getElementById("nrPreManifesto"), data.nrPreManifesto));
		}
		
		validatePCE();		
	}	
	
	//#####################################################
	// Inicio da validacao do pce
	//#####################################################
	
	
	var codigos;
	
	/**
	 * Este get existe decorrente de uma necessidade da popUp de alert.
	 */
	function getCodigos() {
		return codigos;
	}
	
	/**
	 * Captura o idManifesto e gera o alert PCE caso necessario.
	 *
	 * @param methodComplement
	 */
	function validatePCE() {
		var data = new Object();
		data.idManifesto = getElementValue("idManifesto");			
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da validacao. 
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		// Janela de chamada para a tela de pce
		// Apos sua chamada cai na funcao de callBack - alertPCE
		codigos = data.codigos;
		if (codigos.length>0) {
			showModalDialog('vendas/alertaPce.do?cmd=list', window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:353px;');
		}
		setFocusOnNewButton(this.document);
	}
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################
	
	
	/**
	 * Função que remove o registro de manifesto
	 */
	function removeManifesto() {
		removeButtonScript("lms.carregamento.manterGerarPreManifestoAction.removeById", "removeById", "idManifesto", this.document);		
		resetView(true);
	}
	
	function onControleCargaChange(e) {
		if (e.value=='' && getElementValue('tpManifesto')=='E' && getElementValue('tpPreManifesto')=='ED') {
			setDisabled('tpManifesto', false);
			setDisabled('tpPreManifesto', false);
		}
		return controleCarga_nrControleCargaOnChangeHandler();
	}
	
	/**
	 * Controla o objeto de controle carga
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") == "") {
			var tpManifesto = getElementValue("tpManifesto");
			var tpPreManifesto = getElementValue("tpPreManifesto");
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			resetView(true);			
			setElementValue("tpManifesto", tpManifesto);
			habilitaCamposByTpManifesto(true);
			setElementValue("tpPreManifesto", tpPreManifesto);			
			habilitaCamposByTpPreManifesto();
			setFocus("controleCarga.filialByIdFilialOrigem.sgFilial");
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}
	
	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	/**
	 * Desabilita o campo numero do controle de carga
	 */
	function disableNrControleCarga(disable) {
		document.getElementById("controleCarga.nrControleCarga").disabled = disable;
	}
	
	/**
	 * Chama a consulta de 'findControleCargaByNrControleByFilial' a partir de um dos dados retornados 
	 * da lookup
	 */
	function loadDataByNrControleCarga(data) {	
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.findControleCargaByNrControleByFilial", "loadDataByNrControleCarga", data);
    	xmit({serviceDataObjects:[sdo]});    
    	setDisabled("controleCarga.nrControleCarga", false);	
	}
	
	/**
	 * Carrega os dados da tela de descarregarVeiculos apartir dos dados retornados da 
	 * consulta de 'findControleCargaByNrControleByFilial'
	 */
	function loadDataByNrControleCarga_cb(data, error) {
	
		if (!error) {			
			controleCarga_nrControleCarga_exactMatch_cb(data);

			//Verifica se este objeto e nulo
			if (data[0]!=undefined) {		
				limpaAbaDocumentos();
				setElementValue("controleCarga.idControleCarga", data[0].idControleCarga);
				setElementValue("controleCarga.nrControleCarga", data[0].nrControleCarga);
				setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data[0].filialByIdFilialOrigem.idFilial);
				setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
				setElementValue("controleCarga.tpControleCarga", data[0].tpControleCarga.value);
				
				// Formata o campo de nrControlecarga
				format(document.getElementById("controleCarga.nrControleCarga"));
				
				if(getElementValue("tpManifesto") == "V") {
			 		setDisabled("filialByIdFilialDestino.idFilial", false);
			 		resetValue("filialByIdFilialDestino.idFilial");
			 		document.getElementById("filialByIdFilialDestino.sgFilial").required = "true";
			 		setFocus(document.getElementById("filialByIdFilialDestino.sgFilial"));
					if(getElementValue("controleCarga.tpControleCarga") == "V") {
						if (data[0].rotaIdaVolta) {
							setElementValue("controleCarga.rotaIdaVolta.idRotaIdaVolta", data[0].rotaIdaVolta.idRotaIdaVolta);
							setElementValue("controleCarga.rotaIdaVolta.nrRota", data[0].rotaIdaVolta.nrRota);
							setElementValue("controleCarga.rotaIdaVolta.rota.dsRota", data[0].rotaIdaVolta.rota.dsRota);
						}
					} else {
						alert(lms_05039);
						resetValue("controleCarga.filialByIdFilialOrigem.idFilial");
						resetValue("controleCarga.idControleCarga");
						setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
						setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
					}
	    		} else if(getElementValue("tpManifesto") == "E") {
					if(data[0].possuiManifestoEntrega) {
						alert(data[0].possuiManifestoEntrega);
					}    		
					
	    			if(getElementValue("controleCarga.tpControleCarga") == "C") {
						if (data[0].blEntregaDireta=='true') {
							setElementValue('tpPreManifesto', 'ED');
							setDisabled('tpManifesto', true);
							setDisabled('tpPreManifesto', true);
						}
	    			
	    				if (data[0].rotaColetaEntrega) {
			   				setElementValue("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", data[0].rotaColetaEntrega.idRotaColetaEntrega);
							setElementValue("controleCarga.rotaColetaEntrega.nrRota", data[0].rotaColetaEntrega.nrRota);
							setElementValue("controleCarga.rotaColetaEntrega.dsRota", data[0].rotaColetaEntrega.dsRota);
						}
						
		    		} else {
			    		alert(lms_05040);
		    			resetValue("controleCarga.filialByIdFilialOrigem.idFilial");
						resetValue("controleCarga.idControleCarga");
						setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
						setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
		    		}
		    	}	    	
				
			} else {
				resetValue("controleCarga.idControleCarga");
				setFocus(document.getElementById("controleCarga.idControleCarga"));
			}
		} else {
			alert(error);
			resetValue("controleCarga.idControleCarga");
			setFocus(document.getElementById("controleCarga.idControleCarga"));
		}
	}			
	
	/**
	 * Reseta a tela deixa
	 */
	function resetView(boolean) {
		if(boolean == true) { 
			newButtonScript();
			setElementValue("origem", "");
			limpaAbaDocumentos();
			carregaDados();
			setDisabled("btnVisualizar", true);	
			setDisabled("removeButton", true);
			setDisabled("tpManifesto", false);
			setDisabled("tpPreManifesto", false);						
			setFocusOnFirstFocusableField(this.document);
			getTabGroup(this.document).setDisabledTab("documentos", false);
		}
	}
			
	/**
	 * Função que inicializa os campos com valores vazio e atualiza os status de disabled.
	 */
	function inicializaCampos() {
		desabilitaObrigaTpModalTpAbrangencia(true, "false");		
		setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true);
		document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").required = "false";
		setDisabled("controleCarga.idControleCarga", true);
		document.getElementById("controleCarga.nrControleCarga").required = "false";	
		setDisabled("filialByIdFilialDestino.idFilial", true);
		document.getElementById("filialByIdFilialDestino.sgFilial").required = "false";
		setDisabled("solicitacaoRetirada.filialRetirada.idFilial", true);
		setDisabled("solicitacaoRetirada.idSolicitacaoRetirada", true);
		document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada").required = "false";
		setDisabled("cliente.idCliente", true);
		document.getElementById("cliente.pessoa.nrIdentificacao").required = "false";			
	}
			
	
	/**
	 * Carrega combo de Tipo de Pré-Manifesto e habilita campos de acordo com o valor selecionado
	 */
	 function carregaComboTipoPreManifesto(boolean) {
		habilitaCamposByTpManifesto(boolean);
	 	
	 	if(boolean == true) {
		    var tpManifesto = new Array();
		    setNestedBeanPropertyValue(tpManifesto, "tpManifesto", getElementValue("tpManifesto"));
	    	var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.findComboTipoPreManifesto", "tpPreManifesto", tpManifesto);
	    	xmit({serviceDataObjects:[sdo]});
	    }
	    
	    return true;
	}	
	
	/**
	 * Habilita campos de acordo com o valor selecionado na comboBox de Manifesto.
	 */
	 function habilitaCamposByTpManifesto(boolean) {
	  	var tpManifestoValue = getElementValue("tpManifesto");

	 	if(getElementValue("tpManifesto") == "V") {	 			 		
	 		resetView(boolean);
	 		setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", false);
	 		document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").required = "true";
	 		setDisabled("controleCarga.idControleCarga", false);
	 		if(getElementValue("idManifesto") != "") {
	 			setDisabled(document.getElementById("controleCarga.nrControleCarga"), false, undefined, true);
	 		} else {
				setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
			}
	 		document.getElementById("controleCarga.nrControleCarga").required = "true";	
	 	} else if(getElementValue("tpManifesto") == "E") {
	 		resetView(boolean);
	 		getTabGroup(this.document).setDisabledTab("documentos", false);
			setElementValue("filialByIdFilialDestino.idFilial", dadosSessao.filialSessao.idFilial);
			setElementValue("filialByIdFilialDestino.sgFilial", dadosSessao.filialSessao.sgFilial);
			setElementValue("filialByIdFilialDestino.pessoa.nmFantasia", dadosSessao.filialSessao.pessoa.nmFantasia);	 		
	 	}
	 	
		if (getElementValue("idManifesto") == "") {
			document.getElementById("tpModal").required = "false";
			setElementValue("tpModal", "R");			
			document.getElementById("tpAbrangencia").required = "false";
			setElementValue("tpAbrangencia", "N");
		} 	 	
	 	
	 	setElementValue("tpManifesto", tpManifestoValue);
	 }	
	
	/**
	 * Habilita campos de acordo com o valor selecionado na comboBox de Pré Manifesto.
	 */
	 function habilitaCamposByTpPreManifesto() {	 	 	
	 	var tpManifestoValue = getElementValue("tpManifesto");
	 	var tpPreManifestoValue = getElementValue("tpPreManifesto");	 	
	 	if (getElementValue("idManifesto") == "" && getElementValue("origem") == "") {
			resetView(true);
			setElementValue("tpManifesto", tpManifestoValue);		 	
			habilitaCamposByTpManifesto(true);
			setElementValue("tpPreManifesto", tpPreManifestoValue);
		}
		
		setDisabled("obManifesto", false);
	 	
	 	if(getElementValue("tpManifesto") == "V") {		
			desabilitaObrigaTpModalTpAbrangencia(false, "true");
		
			if(getElementValue("tpPreManifesto") == "RV") {
				getTabGroup(this.document).setDisabledTab("documentos", true);				
				setElementValue("tpModal","R");
				setDisabled("tpModal", true);
				setDisabled("tpAbrangencia", false);
				document.getElementById("tpAbrangencia").required = "true";
	 		} else {
		 		getTabGroup(this.document).setDisabledTab("documentos", false);
				setDisabled("tpModal", false);
				setDisabled("tpAbrangencia", false);
				document.getElementById("tpAbrangencia").required = "false";
	 		}

	 	} else if(getElementValue("tpManifesto") == "E") {			
	 		if(getElementValue("tpPreManifesto") == "EN" || getElementValue("tpPreManifesto") == "ED") {
		 		resetValue("solicitacaoRetirada.filialRetirada.idFilial");
			 	resetValue("solicitacaoRetirada.idSolicitacaoRetirada");
	 			resetValue("cliente.idCliente");
	 			inicializaCampos();
		 		setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", false);
		 		document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").required = "true";
		 		setDisabled("controleCarga.idControleCarga", false);
				if(getElementValue("idManifesto") != "") {
		 			setDisabled(document.getElementById("controleCarga.nrControleCarga"), false, undefined, true);
		 		} else {
					setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
				}
 				document.getElementById("controleCarga.nrControleCarga").required = "true";	 			 				
 				desabilitaObrigaTpModalTpAbrangencia(false, "false");
					 			
	 		} else if(getElementValue("tpPreManifesto") == "CR") {
	 			resetValue("cliente.idCliente");
	 			inicializaCampos();
	 			setDisabled("solicitacaoRetirada.filialRetirada.idFilial", false);
	 			setDisabled("solicitacaoRetirada.idSolicitacaoRetirada", false);
		 		if(getElementValue("idManifesto") != "") {
		 			setDisabled(document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada"), false, undefined, true);
		 		} else {
					setDisabled(document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada"), true, undefined, true);
				}
	 			document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada").required = "true";
				desabilitaObrigaTpModalTpAbrangencia(false, "false");
					 			 				 			
	 		} else if(getElementValue("tpPreManifesto") == "PR" || getElementValue("tpPreManifesto") == "EP") {
	 			resetValue("solicitacaoRetirada.filialRetirada.idFilial");
			 	resetValue("solicitacaoRetirada.idSolicitacaoRetirada");
	 			inicializaCampos();
	 			if(getElementValue("tpPreManifesto") == "EP") {
	 		 		setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", false);
	 		 		document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").required = "true";
			 		setDisabled("controleCarga.idControleCarga", false);
					if(getElementValue("idManifesto") != "") {
			 			setDisabled(document.getElementById("controleCarga.nrControleCarga"), false, undefined, true);
			 		} else {
						setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
					}
					document.getElementById("controleCarga.nrControleCarga").required = "true";	
	 			}
	 			setDisabled("cliente.idCliente", false);
	 			document.getElementById("cliente.pessoa.nrIdentificacao").required = "true";
				desabilitaObrigaTpModalTpAbrangencia(false, "false");
				
	 		} else {
	 			inicializaCampos();
	 		}
	 	}
	 	
	 	if (getElementValue("origem") == "controleCarga") {
	 		if (getElementValue("controleCarga.nrControleCarga") != "") {
	 			setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", true);
	 			setDisabled("controleCarga.idControleCarga", true);
	 		}
	 	} 

		setElementValue("tpManifesto", tpManifestoValue);
	 	setElementValue("tpPreManifesto", tpPreManifestoValue);
	 		 		 	
	 	return true;
	 }
	 
	/**
	 * Imprime o relatório
	 */
	 function imprimeRelatorio() {
	 	if(getElementValue("idManifesto") != "" ) {
	     	reportButtonScript('lms.carregamento.manterGerarPreManifestoReportAction', 'openPdf', document.forms[0]);
	    } else {
	    	alert(lms_05042);
	    }
	}
	
	/**
	 * Função chamada ao selecionar um registro na pop-up de Filial Destino.
	 */
	function verificaDestino(data) {
		limpaAbaDocumentos();
		if(getElementValue("tpManifesto") == "V") {	
			if(getElementValue("controleCarga.idControleCarga") != "") {
				validaDestinoParaRotaIdaVolta(data);
			}
		}
	}

	/**
	 * Função chamada no callback da lookup de Filial Destino.
	 */
	function verificaDestino_cb(data, error) {
		filialByIdFilialDestino_sgFilial_exactMatch_cb(data);
	
		//Verifica se este objeto e nulo
		if (data[0] != undefined) {
			limpaAbaDocumentos();
			if(getElementValue("tpManifesto") == "V") {	
				if(getElementValue("controleCarga.idControleCarga") != "") {
					validaDestinoParaRotaIdaVolta(data[0]);
				}
			}					
		}
	}
	
	/**
	 * Função que chama o método que valida se o destino está dentro da 
	 * Rota de Ida e Volta do Controle de Carga.
	 */ 
	function validaDestinoParaRotaIdaVolta(data) {
		var dataForm = new Array();
		setNestedBeanPropertyValue(dataForm, "tpPreManifesto", getElementValue("tpPreManifesto"));
		setNestedBeanPropertyValue(dataForm, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));
	    setNestedBeanPropertyValue(dataForm, "controleCarga.tpControleCarga", getElementValue("controleCarga.tpControleCarga"));
	    setNestedBeanPropertyValue(dataForm, "controleCarga.rotaIdaVolta.idRotaIdaVolta", getElementValue("controleCarga.rotaIdaVolta.idRotaIdaVolta"));
	    setNestedBeanPropertyValue(dataForm, "filialByIdFilialDestino.idFilial", data.idFilial);
	    setNestedBeanPropertyValue(dataForm, "filialByIdFilialDestino.sgFilial", data.sgFilial);
	    
    	var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.validaDestinoParaRotaIdaVolta", "validaDestinoParaRotaIdaVolta", dataForm);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Função de retorno da validaDestinoParaRotaIdaVolta.
	 */
	function validaDestinoParaRotaIdaVolta_cb(data, error) {		
		if (!error) {
			setElementValue("manifesto.controleTrecho.idControleTrecho", data.controleTrecho.idControleTrecho);	
			if (data.confirmar && !window.confirm(data.confirmar)) {				
				setElementValue("manifesto.controleTrecho.idControleTrecho", "");	
				resetValue("filialByIdFilialDestino.idFilial");
				setFocus(document.getElementById("filialByIdFilialDestino.sgFilial"));
			}
		} else {
			alert(error);
			resetValue("filialByIdFilialDestino.idFilial");
			setFocus(document.getElementById("filialByIdFilialDestino.sgFilial"));
		}
	}
	
	/**
	 * Controla o objeto da solicitação de retirada.
	 */	
	function sgFilialSolicitacaoRetiradaOnChangeHandler() {
		if (getElementValue("solicitacaoRetirada.filialRetirada.sgFilial") == "") {
			disableNrSolicitacaoRetirada(true);
			resetValue("solicitacaoRetirada.idSolicitacaoRetirada");
			limpaAbaDocumentos();
		} else {
			disableNrSolicitacaoRetirada(false);
		}
		return lookupChange({e:document.forms[0].elements["solicitacaoRetirada.filialRetirada.idFilial"]});
	}
	
	function disableNrSolicitacaoRetirada_cb(data, error) {
		if (data.length==0) disableNrSolicitacaoRetirada(false);
		return lookupExactMatch({e:document.getElementById("solicitacaoRetirada.filialRetirada.idFilial"), data:data});
	}
	
	/**
	 * Desabilita o campo numero da solicitação de retirada.
	 */
	function disableNrSolicitacaoRetirada(disable) {
		document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada").disabled = disable;
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
		
			var dataMerged = new Array();			
			merge(dataMerged, buildFormBeanFromForm(this.document.forms[0]));
			setNestedBeanPropertyValue(dataMerged, "solicitacaoRetirada.idSolicitacaoRetirada", data[0].idSolicitacaoRetirada);
			setNestedBeanPropertyValue(dataMerged, "solicitacaoRetirada.filialRetirada.idFilial", data[0].filialRetirada.idFilial);
			setNestedBeanPropertyValue(dataMerged, "solicitacaoRetirada.tpSituacao", data[0].tpSituacao.value);

	    	var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.validaSolicitacaoRetiradaAndGetDocumentos", "validaSolicitacaoRetiradaAndGetDocumentos", dataMerged);
   			xmit({serviceDataObjects:[sdo]});
		}
	}	
	
	/**
	 * Função chamada ao clicar no registro da pop-up de Solicitação de Retirada.
	 */
	function loadDataByNrSolicitacaoRetirada(data) {
		disableNrSolicitacaoRetirada(false);
		var dataMerged = new Array();
		merge(dataMerged, buildFormBeanFromForm(this.document.forms[0]));
		setNestedBeanPropertyValue(dataMerged, "solicitacaoRetirada.idSolicitacaoRetirada", data.idSolicitacaoRetirada);
		setNestedBeanPropertyValue(dataMerged, "solicitacaoRetirada.filialRetirada.idFilial", data.idFilialRetirada);
		setNestedBeanPropertyValue(dataMerged, "solicitacaoRetirada.tpSituacao", data.tpSituacao.value);
		
    	var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.validaSolicitacaoRetiradaAndGetDocumentos", "validaSolicitacaoRetiradaAndGetDocumentos", dataMerged);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Função de retorno da validação da solicitação de retirada.
	 */
	function validaSolicitacaoRetiradaAndGetDocumentos_cb(data, error) {
		if (error) {
			alert(error);
			resetValue("solicitacaoRetirada.idSolicitacaoRetirada");
			setFocus(document.getElementById("solicitacaoRetirada.nrSolicitacaoRetirada"));			
		}
	}
	
	/**
	 * Limpa os documentos da Solicitacao de Retirada.
	 */
	function limpaDocumentosSolicitacaoRetirada() {
		limpaAbaDocumentos();		
		return solicitacaoRetirada_nrSolicitacaoRetiradaOnChangeHandler();
	}	
	
	/**
	 * Desabilita ou habilita, e obriga o tpModal e tpAbrangencia conforme parametros.
	 */
	function desabilitaObrigaTpModalTpAbrangencia(disabled, required) {
		setDisabled("tpModal", disabled);
		setDisabled("tpAbrangencia", disabled);
		document.getElementById("tpModal").required = required;
		document.getElementById("tpAbrangencia").required = required;		
	}
	
	/**
	 * Limpa aba de documentos
	 */	
	function limpaAbaDocumentos() {
	    // Deleta um ou mais documento(s) do Pré-Manifesto.
		var mapCriteria = new Array();	   
		setNestedBeanPropertyValue(mapCriteria, "masterId", getElementValue("idManifesto"));
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.removeDocumentosPreManifesto", "", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
</script>
