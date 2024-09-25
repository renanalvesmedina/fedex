<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	/**
	 * Função chamada ao carregar a tela.
	 */
	function carregaPagina() {
		onPageLoad();
		loadParentData();
		setFocusOnFirstFocusableField(this.document);
	}
	
	/**
	 * Função q pega os dados da tela pai para setar nos campos.
	 */
	function loadParentData() {
		var parentWindow = dialogArguments.window.document;
		//carrega os objetos...			
		setElementValue("idManifesto", parentWindow.getElementById("manifesto.idManifesto").value);
		setElementValue("manifesto.nrPreManifesto", getFormattedValue("integer", parentWindow.getElementById("manifesto.nrPreManifesto").value, "00000000", true));
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").value);
		
		document.getElementById("idManifesto").masterLink=true;
		document.getElementById("manifesto.nrPreManifesto").masterLink=true;
		document.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").masterLink=true;
		
		loadManifesto();
	}
	
	function loadManifesto() {
		var data = new Object();
		data.idManifesto = getElementValue("idManifesto");
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoDocumentosManifestoAction.findManifesto", "loadManifesto", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function loadManifesto_cb(data, error) {
		if (error!=undefined) {
			//Solicitacao Retirada...
			setElementValue("solicitacaoRetirada.idSolicitacaoRetirada", data.idSolicitacaoRetirada);
			setElementValue("solicitacaoRetirada.filialRetirada.sgFilial", data.sgFilial);
			setElementValue("solicitacaoRetirada.nrSolicitacaoRetirada", data.nrSolicitacaoRetirada);
			//Consignatario...
			setElementValue("doctoServico.clienteByIdClienteConsignatario.idCliente", data.idConsignatario);
			setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao", data.nrIdentificacaoConsignatario);
			setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa", data.nmConsignatario);
		
			if (data.tpManifesto=="E") {
				setDisabled("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", true);
				setDisabled("doctoServico.filialByIdFilialDestino.idFilial", false);
			} else if (getElementValue("manifesto.tpManifesto") == "V") {
				//Rota...
				setElementValue("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", idRotaColetaEntrega);
				setElementValue("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", idRotaColetaEntrega);
				setElementValue("manifesto.controleCarga.rotaColetaEntrega.nrRota", nrRota);
				setElementValue("manifesto.controleCarga.rotaColetaEntrega.dsRota", dsRota);
				//Destino...
				setElementValue("doctoServico.filialByIdFilialDestino.idFilial", idFilialDestino);
				setElementValue("doctoServico.filialByIdFilialDestino.sgFilial", sgFilialDestino);
				setElementValue("doctoServico.filialByIdFilialDestino.pessoa.nmFantasia", nmFilialDestino);		
				setDisabled("doctoServico.filialByIdFilialDestino.idFilial", true);
			}	
		} 
	}
</script>

<adsm:window title="adicionarDocumentosPreManifesto" service="lms.carregamento.carregarVeiculoDocumentosManifestoAction"
			 onPageLoad="carregaPagina">
	<adsm:form action="/carregamento/carregarVeiculoDocumentosManifestoAutomatico" idProperty="idPreManifestoDocumento">

		<adsm:section caption="adicionarDocumentosPreManifesto" />
		
		<adsm:hidden property="idManifesto"/>
		<adsm:hidden property="manifesto.tpManifesto"/>
		<adsm:hidden property="tpPreManifesto"/>
		<adsm:hidden property="manifesto.tpModal"/>
		<adsm:hidden property="manifesto.tpAbrangencia"/>
		<adsm:hidden property="tabSelecionada" value="terminal"/>
		<adsm:hidden property="controleCarga.rotaColetaEntrega.idRotaColetaEntrega"/>
		<adsm:hidden property="filialByIdFilialDestino.idFilial"/>	
		<adsm:hidden property="filialSessao.idFilial" />
		<adsm:hidden property="filialSessao.sgFilial" />
		<adsm:hidden property="filialSessao.pessoa.nmFantasia" />		
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>	
		
		<adsm:textbox dataType="text" property="manifesto.filialByIdFilialOrigem.sgFilial" 
					  label="preManifesto" labelWidth="18%" width="36%" size="4" maxLength="3" disabled="true"  >
			<adsm:textbox dataType="integer" property="manifesto.nrPreManifesto" size="8" 
						  maxLength="8" mask="00000000" disabled="true" />
		</adsm:textbox>
		
		<adsm:hidden property="solicitacaoRetirada.idSolicitacaoRetirada" />
		<adsm:textbox label="solicitacaoRetirada" dataType="text" property="solicitacaoRetirada.filialRetirada.sgFilial" 
					  size="3" width="31%" disabled="true">
			<adsm:textbox dataType="integer" property="solicitacaoRetirada.nrSolicitacaoRetirada" 
						  size="8" mask="00000000" disabled="true"/>					  
		</adsm:textbox>
		
		<adsm:lookup dataType="integer" property="manifesto.controleCarga.rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
					 service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findLookupRotaColetaEntrega" 
					 action="/municipios/manterRotaColetaEntrega"
					 label="rotaColetaEntrega" labelWidth="18%" width="36%" size="5" maxLength="5" exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="filialSessao.idFilial" modelProperty="filial.idFilial" disable="true"/>
			<adsm:propertyMapping criteriaProperty="filialSessao.sgFilial" modelProperty="filial.sgFilial" disable="true"/>
			<adsm:propertyMapping criteriaProperty="filialSessao.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" disable="true"/>
        	<adsm:propertyMapping relatedProperty="manifesto.controleCarga.rotaColetaEntrega.dsRota" modelProperty="dsRota" inlineQuery="false"/>
	        <adsm:textbox dataType="text" property="manifesto.controleCarga.rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false"/>
        </adsm:lookup>		

		<adsm:hidden property="manifesto.filialByIdFilialDestino.idFilial" />
		<adsm:lookup dataType="text" property="doctoServico.filialByIdFilialDestino" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
				 	 service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findLookupFilial"
				 	 action="/municipios/manterFiliais"
					 label="destino" size="3" maxLength="3" width="31%">
	        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="doctoServico.filialByIdFilialDestino.pessoa.nmFantasia" 
						  size="30" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.idCliente" />
		<adsm:textbox label="consignatario" property="doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao" 
					  dataType="text" size="18" maxLength="18" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:hidden property="doctoServico.clienteByIdClienteRemetente.tpSituacao" value="A" />
		<adsm:lookup label="remetente" 
					 idProperty="idCliente" 
					 property="doctoServico.clienteByIdClienteRemetente" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findLookupCliente"
					 action="/vendas/manterDadosIdentificacao" 
					 dataType="text" size="18" maxLength="18" 
					 labelWidth="18%" width="82%" >
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteRemetente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
		</adsm:lookup>		

		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.tpSituacao" value="A" />
		<adsm:lookup label="destinatario" 
					 idProperty="idCliente" 
					 property="doctoServico.clienteByIdClienteDestinatario" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findLookupCliente"
					 action="/vendas/manterDadosIdentificacao" 
					 dataType="text" size="18" maxLength="18" 
					 labelWidth="18%" width="82%" >
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:hidden property="doctoServico.idDoctoServico" serializable="true" />
		<adsm:combobox label="documentoServico"
					   labelWidth="18%" width="36%" 
					   property="doctoServico.tpDocumentoServico"
					   service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onDataLoadCallBack="tpDocumentoServicoComboBox"
					   defaultValue="CTR"
					   onchange="return changeDocumentWidgetType({
								 documentTypeElement:this,
								 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
								 documentNumberElement:document.getElementById('conhecimento.idDoctoServico'),
								 documentGroup:'SERVICE',
								 actionService:'lms.carregamento.carregarVeiculoDocumentosManifestoAction' });">
								 
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
						 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 size="4" 
						 maxLength="3" 
						 picker="false" 
						 onDataLoadCallBack="enableDoctoServico"
						 onchange="return doctoServicoFilialByIdFilialOrigem_OnChange(this.value);"
						 popupLabel="pesquisarFilial"/>
								   
			<adsm:lookup dataType="integer"
						 property="conhecimento" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrConhecimento" 
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico"						 
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 disabled="true" 
						 mask="00000000" 
						 popupLabel="pesquisarDocumentoServico"/>
		</adsm:combobox>
		
		<adsm:hidden property="awb.tpStatusAwb" value="E"/>
		<adsm:combobox label="AWB" property="ciaFilialMercurio.empresa.idEmpresa" 
					   optionLabelProperty="pessoa.nmPessoa" 
			 		   optionProperty="idEmpresa" 
					   service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findCiaAerea" 
					   boxWidth="120" serializable="false" width="31%">
			<adsm:lookup idProperty="idAwb" property="doctoServico.ctoAwbs"
					 criteriaProperty="nrAwb"
					 action="/expedicao/consultarAWBs"
					 service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findLookupAwb"
					 dataType="text" size="10" maxLength="10" 
					 onPopupSetValue="onPopupSetValueAWBs">
					<adsm:propertyMapping criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" modelProperty="ciaFilialMercurio.empresa.idEmpresa"/>						 						 
					<adsm:propertyMapping criteriaProperty="awb.tpStatusAwb" modelProperty="tpStatusAwb"/>
			</adsm:lookup>
		</adsm:combobox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="consultarButton" onclick="pesquisarRegistrosTab(this.form)"/>
			<adsm:button id="cleanButton" caption="limpar" disabled="false" onclick="limpaTela(this.form)"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="documentosNoTerminalTitulo" id="terminal" src="/carregamento/carregarVeiculoDocumentosManifestoAutomatico" cmd="terminal" boxWidth="150"/>
		<adsm:tab title="documentosEmDescargaTitulo" id="descarga" src="/carregamento/carregarVeiculoDocumentosManifestoAutomatico" cmd="descarga" boxWidth="150"/>
		<adsm:tab title="coletasChegadas"    		 id="chegadas" src="/carregamento/carregarVeiculoDocumentosManifestoAutomatico" cmd="chegadas" boxWidth="150" disabled="true"/>
		<adsm:tab title="coletasExecutadasTitulo"    id="executadas" src="/carregamento/carregarVeiculoDocumentosManifestoAutomatico" cmd="executadas" boxWidth="150" disabled="true"/>
	</adsm:tabGroup>

</adsm:window>

<script type="text/javascript">
	setDisabled("consultarButton", false);
	setDisabled("cleanButton", false);

	/**
	 * Carrega a combobox de tipo de documento e seta o valor default.
	 */
	function tpDocumentoServicoComboBox_cb(data) {
		comboboxLoadOptions({e:document.getElementById("doctoServico.tpDocumentoServico"), data:data});

		if(getElementValue("manifesto.tpAbrangencia") == "I") {
			var comboBox = document.getElementById("doctoServico.tpDocumentoServico");		
			for(var i=0; i < comboBox.length; i++) {		
				if(comboBox.options[i].value == "CRT") {
					comboBox.options[i].selected = "true";					
				}
			}
			
			setDisabled("doctoServico.tpDocumentoServico", true);	
		}
		
		changeDocumentWidgetType({
			 documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"),
			 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
			 documentNumberElement:document.getElementById('conhecimento.idDoctoServico'),
			 documentGroup:'SERVICE',
			 actionService:'lms.carregamento.carregarVeiculoDocumentosManifestoAction' });			
	}
	
	/**
	 * Função q seta o valor para a variável 'tabSelecionada'
	 */
	function setTabSelecionada(tab) {
		setElementValue("tabSelecionada", tab);
	}
	
	/**
	 * Pesquisa os registros da Tab filha selecionada.
	 */
	function pesquisarRegistrosTab(form) {
		callTabFunction(form.document.tabGroup.selectedTab.tabOwnerFrame.parent.document, 
						getElementValue("tabSelecionada"), "povoaGrid");
	}
	
	/**
	 * #############################################################
	 * # Inicio das funções para a tag customizada de DoctoServico #
	 * #############################################################
	 */

	/**
	 * Controla as tags aninhadas para habilitar/desabilitar
	 */
	function enableDoctoServico_cb(data) {
		var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if (r == true) {
	    	setDisabled("conhecimento.idDoctoServico", false);
	      	setFocus(document.getElementById("conhecimento.nrConhecimento"));
	   	}
	}
	
	function retornoDocumentoServico_cb(data) {
		var boolean = conhecimento_nrConhecimento_exactMatch_cb(data);
		
		if (boolean == true) {
			if (data != undefined && data.length > 0) {
				setElementValue("doctoServico.idDoctoServico", data[0].idDoctoServico);				
			}
		}
	}
	
	function doctoServicoFilialByIdFilialOrigem_OnChange(valor) {
		var boolean = changeDocumentWidgetFilial( { 
						filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"),
			      		documentNumberElement:document.getElementById("conhecimento.idDoctoServico") } );
		return boolean;
	}	
		
	/**
	 * ##########################################################
	 * # Fim das funções para a tag customizada de DoctoServico #
	 * ##########################################################
	 */
	 
	
	/**
	 * Função que limpa a tela carrega os dados da sessão
	 */
	function limpaTela(form) {
		var tabSelecionada = getElementValue("tabSelecionada");
		resetValue(this.document);
		setDisabled(document.getElementById("conhecimento.nrConhecimento"), true, undefined, true);
		setElementValue("tabSelecionada", tabSelecionada);
		getDadosTelaPai();		
		
		// Pesquisa os registros da Tab filha selecionada.
		callTabFunction(form.document.tabGroup.selectedTab.tabOwnerFrame.parent.document, 
						getElementValue("tabSelecionada"), "povoaGrid");
		setFocusOnFirstFocusableField(this.document);
	}
	
	/**
	 * Função chamada no onPopupSetValue da consulta de AWB.	 
	 */
	function onPopupSetValueAWBs(data, error) {
		var comboBox = document.getElementById("ciaFilialMercurio.empresa.idEmpresa");
		for(var i=0; i < comboBox.length; i++) {
			if(comboBox.options[i].value == data.ciaFilialMercurio.empresa.idEmpresa) {			
				comboBox.options[i].selected = "true";
			}
		}					
	}	
	
</script>