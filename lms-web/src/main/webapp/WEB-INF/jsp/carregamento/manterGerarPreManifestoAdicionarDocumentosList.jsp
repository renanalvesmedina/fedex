<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	/**
	 * Função chamada ao carregar a tela.
	 */
	function carregaPagina() { 
		onPageLoad();
		getDadosTelaPai();
		setFocusOnFirstFocusableField(this.document);
	}
	
	/**
	 * Função q pega os dados da tela pai para setar nos campos.
	 */
	function getDadosTelaPai() {
		
		getFromParameter();
		if (isFromCarregamento()!=true) {
		
			var doc;
			if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {
				doc = window.dialogArguments.window.document;
			} else {
			   doc = document;
			}		
		
			var tabGroup = getTabGroup(doc);
			
			// Pega parâmetros da tela DOCUMENTOS.
			var tabDet = tabGroup.getTab("documentos");
			var masterId = tabDet.getFormProperty("masterId");
			setElementValue("masterId", masterId);
			
			// Pega parâmetros da tela CAD.
			var tabDet = tabGroup.getTab("cad");
					
			var sgFilial = tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial");
			var nrPreManifesto = tabDet.getFormProperty("nrPreManifesto");
			var tpManifesto = tabDet.getFormProperty("tpManifesto");
			var tpPreManifesto = tabDet.getFormProperty("tpPreManifesto");
			var tpModal = tabDet.getFormProperty("tpModal");
			var tpAbrangencia = tabDet.getFormProperty("tpAbrangencia");
			var idRotaColetaEntrega = tabDet.getFormProperty("controleCarga.rotaColetaEntrega.idRotaColetaEntrega");
			var nrRota = tabDet.getFormProperty("controleCarga.rotaColetaEntrega.nrRota");
			var dsRota = tabDet.getFormProperty("controleCarga.rotaColetaEntrega.dsRota");	
			var idFilialDestino = tabDet.getFormProperty("filialByIdFilialDestino.idFilial");
			var sgFilialDestino = tabDet.getFormProperty("filialByIdFilialDestino.sgFilial");
			var nmFilialDestino = tabDet.getFormProperty("filialByIdFilialDestino.pessoa.nmFantasia");	
			var idSolicitacaoRetirada = tabDet.getFormProperty("solicitacaoRetirada.idSolicitacaoRetirada");
			var sgFilialSolicitacaoRetirada = tabDet.getFormProperty("solicitacaoRetirada.filialRetirada.sgFilial");
			var nrSolicitacaoRetirada = tabDet.getFormProperty("solicitacaoRetirada.nrSolicitacaoRetirada");
			var idConsignatario = tabDet.getFormProperty("cliente.idCliente");
			var nrIdentConsignatario = tabDet.getFormProperty("cliente.pessoa.nrIdentificacao");
			var nmConsignatario = tabDet.getFormProperty("cliente.pessoa.nmPessoa");
			var idFilialSessao = tabDet.getFormProperty("filialSessao.idFilial");
			var sgFilialSessao = tabDet.getFormProperty("filialSessao.sgFilial");
			var nmFantasiaFilialSessao = tabDet.getFormProperty("filialSessao.pessoa.nmFantasia");
			
			
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", sgFilial);
			setElementValue("manifesto.nrPreManifesto", nrPreManifesto);	
			setElementValue("manifesto.tpManifesto", tpManifesto);
			setElementValue("manifesto.tpPreManifesto", tpPreManifesto);	
			setElementValue("manifesto.tpModal", tpModal);
			setElementValue("manifesto.tpAbrangencia", tpAbrangencia);	
			setElementValue("filialByIdFilialDestino.idFilial", idFilialDestino);		
			setElementValue("manifesto.filialByIdFilialDestino.idFilial", idFilialDestino);
			setElementValue("solicitacaoRetirada.idSolicitacaoRetirada", idSolicitacaoRetirada);
			setElementValue("solicitacaoRetirada.filialRetirada.sgFilial", sgFilialSolicitacaoRetirada);
			setElementValue("solicitacaoRetirada.nrSolicitacaoRetirada", nrSolicitacaoRetirada);
			setElementValue("doctoServico.clienteByIdClienteConsignatario.idCliente", idConsignatario);
			setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao", nrIdentConsignatario);
			setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa", nmConsignatario);
			setElementValue("filialSessao.idFilial", idFilialSessao);
			setElementValue("filialSessao.sgFilial", sgFilialSessao);
			setElementValue("filialSessao.pessoa.nmFantasia", nmFantasiaFilialSessao);		
			
			if(getElementValue("manifesto.tpManifesto") == "E") {
				
				if(getElementValue("manifesto.tpPreManifesto") != "PR" && getElementValue("manifesto.tpPreManifesto") != "EP") {
					setElementValue("doctoServico.filialByIdFilialDestino.idFilial", idFilialDestino);
					setElementValue("doctoServico.filialByIdFilialDestino.sgFilial", sgFilialDestino);
					setElementValue("doctoServico.filialByIdFilialDestino.pessoa.nmFantasia", nmFilialDestino);		
					setDisabled("doctoServico.filialByIdFilialDestino.idFilial", true);
								
					setElementValue("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", idRotaColetaEntrega);
					setElementValue("manifesto.controleCarga.rotaColetaEntrega.nrRota", nrRota);
					setElementValue("manifesto.controleCarga.rotaColetaEntrega.dsRota", dsRota);
				}
			} else if(getElementValue("manifesto.tpManifesto") == "V") {
				setDisabled("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", true);
				setDisabled("doctoServico.filialByIdFilialDestino.idFilial", false);
			}	
		} else {
			//Carrega os dados do manifesto atraves de pesquisa.
			var parentWindow = dialogArguments.window.document;
			setElementValue("masterId", parentWindow.getElementById("manifesto.idManifesto").value);
			var data = new Object();
			data.idManifesto = getElementValue("masterId");
			var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.newMaster", "newMaster", data);
	    	xmit({serviceDataObjects:[sdo]});
		}
		
	}
	
	function newMaster_cb(data, error) {
		loadManifesto();
	}
	
	function loadManifesto() {
		var data = new Object();
		data.idManifesto = getElementValue("masterId");
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.findManifesto", "loadManifesto", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function loadManifesto_cb(data, error) {
		if (data != undefined) {
			//Solicitacao Retirada...
			if (data.solicitacaoRetirada){
				setElementValue("solicitacaoRetirada.idSolicitacaoRetirada", data.solicitacaoRetirada.idSolicitacaoRetirada);
				setElementValue("solicitacaoRetirada.filialRetirada.sgFilial", datasolicitacaoRetirada.filialRetirada.sgFilial);
				setElementValue("solicitacaoRetirada.nrSolicitacaoRetirada", data.nrSolicitacaoRetirada.nrSolicitacaoRetirada);
			}
			//Consignatario...
			if (data.consignatario){
				setElementValue("doctoServico.clienteByIdClienteConsignatario.idCliente", data.consignatario.idConsignatario);
				setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao", data.consignatario.nrIdentificacaoConsignatario);
				setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa", data.consignatario.nmConsignatario);
			}

			setElementValue("masterId", data.manifesto.idManifesto);
			setElementValue("manifesto.nrPreManifesto", getFormattedValue("integer", data.manifesto.nrPreManifesto, "00000000", true));
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.manifesto.filialByIdFilialOrigem.sgFilial);
			setElementValue("manifesto.filialByIdFilialDestino.idFilial", data.manifesto.filialByIdFilialDestino.idFilial);
			setElementValue("manifesto.tpManifesto", data.manifesto.tpManifesto);
			setElementValue("manifesto.tpAbrangencia", data.manifesto.tpAbrangencia);
			setElementValue("manifesto.tpPreManifesto", data.manifesto.tpPreManifesto);
			setElementValue("manifesto.tpModal", data.manifesto.tpModal);		
			
			document.getElementById("masterId").masterLink=true;
			document.getElementById("manifesto.nrPreManifesto").masterLink=true;
			document.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").masterLink=true;
			document.getElementById("manifesto.filialByIdFilialDestino.idFilial").masterLink=true;				
			document.getElementById("manifesto.tpManifesto").masterLink=true;
			document.getElementById("manifesto.tpAbrangencia").masterLink=true;
			document.getElementById("manifesto.tpPreManifesto").masterLink=true;

		
			if (data.tpManifesto == "E") {
							
				//Rota...
				//Verificar necessidade da linha seguinte
				setElementValue("controleCarga.rotaColetaEntrega.idRotaColetaEntrega", data.idRotaColetaEntrega);
				
				if(data.tpPreManifesto != "PR" && data.tpPreManifesto != "EP") {
					setElementValue("filialByIdFilialDestino.idFilial", data.filialSessao.idFilialDestino);		
					setElementValue("manifesto.filialByIdFilialDestino.idFilial", data.filialSessao.idFilialDestino);				
					setElementValue("doctoServico.filialByIdFilialDestino.idFilial", data.filialSessao.idFilialDestino);
					setElementValue("doctoServico.filialByIdFilialDestino.sgFilial", data.filialSessao.sgFilialDestino);
					setElementValue("doctoServico.filialByIdFilialDestino.pessoa.nmFantasia", data.filialSessao.nmFilialDestino);		
					setDisabled("doctoServico.filialByIdFilialDestino.idFilial", true);
					
					if (data.rotaColetaEntrega){			
						setElementValue("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", data.rotaColetaEntrega.idRotaColetaEntrega);
						setElementValue("manifesto.controleCarga.rotaColetaEntrega.nrRota", data.rotaColetaEntrega.nrRota);
						setElementValue("manifesto.controleCarga.rotaColetaEntrega.dsRota", data.rotaColetaEntrega.dsRota);
					}
				}
			} else if (getElementValue("manifesto.tpManifesto") == "V") {
				setDisabled("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", true);
				setDisabled("doctoServico.filialByIdFilialDestino.idFilial", false);			
			}	
		} 
	}
	
	/**
	 * Seta o hiden from 
	 */
	function getFromParameter() {
		var url = new URL(parent.location.href);
		var from = url.parameters["from"];
	
		if (from!=undefined)  {
			setElementValue("from", from);
		} else {
			setElementValue("from", "");
		}
	}
	
	/**
	 * Verifica se a tela esta sendo chamada da tela de carregamento.
	 */
	function isFromCarregamento() {
		if (getElementValue("from")=="carregamento") {
			return true;
		}
		return false;
	}
</script>

<adsm:window title="adicionarDocumentosPreManifesto" service="lms.carregamento.manterGerarPreManifestoAction"
			 onPageLoad="carregaPagina">
	<adsm:form action="/carregamento/manterGerarPreManifestoAdicionarDocumentos" idProperty="idPreManifestoDocumento" id="popUpListForm">

		<adsm:section caption="adicionarDocumentosPreManifesto" />
		
		<adsm:hidden property="masterId"/>
		<adsm:hidden property="manifesto.tpManifesto"/>
		<adsm:hidden property="manifesto.tpPreManifesto"/>
		<adsm:hidden property="manifesto.tpModal"/>
		<adsm:hidden property="manifesto.tpAbrangencia"/>
		<adsm:hidden property="tabSelecionada" value="terminal"/>
		<adsm:hidden property="controleCarga.rotaColetaEntrega.idRotaColetaEntrega"/>
		<adsm:hidden property="filialByIdFilialDestino.idFilial"/>
		<adsm:hidden property="filialSessao.idFilial"/>
		<adsm:hidden property="filialSessao.sgFilial"/>
		<adsm:hidden property="filialSessao.pessoa.nmFantasia"/>		
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>	
		<adsm:hidden property="from"/> 
		
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
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupRotaColetaEntrega" 
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
				 	 service="lms.carregamento.manterGerarPreManifestoAction.findLookupFilial"
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
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupCliente"
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
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupCliente"
					 action="/vendas/manterDadosIdentificacao" 
					 dataType="text" size="18" maxLength="18" 
					 labelWidth="18%" width="82%" >
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:combobox label="documentoServico"
					   labelWidth="18%" width="36%" 
					   property="doctoServico.tpDocumentoServico"
					   service="lms.carregamento.manterGerarPreManifestoAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onDataLoadCallBack="tpDocumentoServicoComboBox"
					   onchange="return changeDocumentWidgetType({
								 documentTypeElement:this,
								 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
								 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
								 documentGroup:'SERVICE',
								 actionService:'lms.carregamento.manterGerarPreManifestoAction' });">
								 
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
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
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
					   service="lms.carregamento.manterGerarPreManifestoAction.findCiaAerea" 
					   boxWidth="120" serializable="false" width="31%">
			<adsm:lookup idProperty="idAwb" property="doctoServico.ctoAwbs"
					 criteriaProperty="nrAwb"
					 action="/expedicao/consultarAWBs"
					 service="lms.carregamento.manterGerarPreManifestoAction.findLookupAwb"
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
		<adsm:tab title="documentosNoTerminalTitulo" id="terminal" src="/carregamento/manterGerarPreManifestoAdicionarDocumentos" cmd="terminal" boxWidth="150"/>
		<adsm:tab title="documentosEmDescargaTitulo" id="descarga" src="/carregamento/manterGerarPreManifestoAdicionarDocumentos" cmd="descarga" boxWidth="150"/>
		<adsm:tab title="coletasChegadas"    		 id="chegadas" src="/carregamento/manterGerarPreManifestoAdicionarDocumentos" cmd="chegadas" boxWidth="150" disabled="true"/>
		<adsm:tab title="coletasExecutadasTitulo"    id="executadas" src="/carregamento/manterGerarPreManifestoAdicionarDocumentos" cmd="executadas" boxWidth="150" disabled="true"/>
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
			 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
			 documentGroup:'SERVICE',
			 actionService:'lms.carregamento.manterGerarPreManifestoAction' });			
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
	    	setDisabled("doctoServico.idDoctoServico", false);
	      	setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   	}
	}
	
	function retornoDocumentoServico_cb(data) {
		var boolean = doctoServico_nrDoctoServico_exactMatch_cb(data);
	}
	
	function doctoServicoFilialByIdFilialOrigem_OnChange(valor) {
		var boolean = changeDocumentWidgetFilial( { 
						filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"),
			      		documentNumberElement:document.getElementById("doctoServico.idDoctoServico") } );
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
		setDisabled(document.getElementById("doctoServico.nrDoctoServico"), true, undefined, true);
		setElementValue("tabSelecionada", tabSelecionada);
		getDadosTelaPai();		
		
		// Pesquisa os registros da Tab filha selecionada.
		callTabFunction(form.document.tabGroup.selectedTab.tabOwnerFrame.parent.document, 
						getElementValue("tabSelecionada"), "resetGrid");
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