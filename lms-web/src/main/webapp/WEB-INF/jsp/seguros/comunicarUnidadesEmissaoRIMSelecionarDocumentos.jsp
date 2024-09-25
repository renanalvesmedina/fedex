<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function pageLoad(){
		loadDataFromParent();
		onPageLoad();
		disableButtons(false);
	}
	
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
	}

	function loadDataFromParent(){
		var parentWindow = dialogArguments.window.document;
		setElementValue("processoSinistro.idProcessoSinistro", parentWindow.getElementById("processoSinistro.idProcessoSinistro").value);
		setElementValue("processoSinistro.nrProcessoSinistro", parentWindow.getElementById("processoSinistro.nrProcessoSinistro").value);
	}

	function disableButtons(disable) {
		setDisabled("buscar", disable);
		setDisabled("limpar", disable);
		setDisabled("selecionarDocumentos", disable);
		setDisabled("fechar", disable);
	}
	
</script>

<adsm:window title="selecionarDocumentos" service="lms.seguros.comunicarUnidadesEmissaoRIMAction" onPageLoad="pageLoad"
			onPageLoadCallBack="pageLoad">
	<adsm:form action="/seguros/comunicarUnidadesEmissaoRIM" height="145" id="selecionarDocumentosForm">
		<adsm:section caption="selecionarDocumentosComunicarUnidadesEmissaoRim"/>
		
			<adsm:hidden property="processoSinistro.idProcessoSinistro"/>
		<adsm:textbox label="numeroProcesso" property="processoSinistro.nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />

		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" labelWidth="15%" width="85%" serializable="false" 
					   service="lms.seguros.comunicarUnidadesEmissaoRIMAction.findTipoManifesto" 
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
							   		documentTypeElement:this, 
							   		filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
							   		documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
							   		documentGroup:'MANIFESTO',
							   		actionService:'lms.seguros.comunicarUnidadesEmissaoRIMAction'
							   		});" >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false"
						 popupLabel="pesquisarFilial"
						 onDataLoadCallBack="filialManifestoCallback"
						 onchange="return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	});" >						 	
			</adsm:lookup>

			<adsm:lookup dataType="integer" 
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" popupLabel="pesquisarManifesto"
						 afterPopupSetValue="manifestoAfterPopupSetValue"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" >						 
			</adsm:lookup>
		</adsm:combobox>
		<adsm:hidden property="manifesto.tpStatusManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" />
		<adsm:hidden property="manifesto.tpManifestoEntrega" value="EN" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />

		
		<adsm:lookup dataType="text" property="doctoServico.clienteByIdClienteRemetente" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" service="lms.seguros.comunicarUnidadesEmissaoRIMAction.findClienteLookup" 
					 label="remetente" width="85%" maxLength="20" size="20">
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="doctoServico.clienteByIdClienteDestinatario" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" service="lms.seguros.comunicarUnidadesEmissaoRIMAction.findClienteLookup" 
					 label="destinatario" width="85%" maxLength="20" size="20">
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:hidden property="tpSituacao" value="A"/>
		
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   service="lms.seguros.comunicarUnidadesEmissaoRIMAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   label="documentoServico" labelWidth="15%" width="85%"
					   required="false"
					   onchange="return onTpDoctoServicoChange(this);"
					   >

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action=""
						 popupLabel="pesquisarFilial" 
						 onDataLoadCallBack="filialDoctoServico"
						 size="3" maxLength="3" picker="false" disabled="true"
 						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						  }); "/>

			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 mask="00000000"
						 popupLabel="pesquisarDocumentoServico"
						 size="10" disabled="true" />

		</adsm:combobox>
		

		<adsm:combobox property="tpPrejuizo" optionProperty="value" optionLabelProperty="description" 
					   service="lms.seguros.comunicarUnidadesEmissaoRIMAction.findTipoPrejuizo" label="prejuizo" width="85%"/>

		<adsm:checkbox property="blEnviado" label="somenteNaoEnviados"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="buscar" onclick="findDocumentoServico();"/>
			<adsm:button caption="limpar" id="limpar" onclick="resetView();"/>
		</adsm:buttonBar>
		<script>
			function lms_22011() {
				alert("<adsm:label key='LMS-22011'/>");
			}
			function lms_22012() {
				alert("<adsm:label key='LMS-22012'/>");
			}
			function lms_22021() {
				alert("<adsm:label key='LMS-22021'/>");
			}
			
			
		</script>
	</adsm:form>
	
	<adsm:grid property="sinistroDoctoServico" idProperty="idSinistroDoctoServico" 
			   service="lms.seguros.comunicarUnidadesEmissaoRIMAction.findPaginatedSelecionarDocumentos"
			   rowCountService="lms.seguros.comunicarUnidadesEmissaoRIMAction.getRowCountSelecionarDocumentos"
			   gridHeight="180" unique="true" title="documentosSelecionados" onRowClick="disableGridClick"
			   showPagging="false" scrollBars="both" >
		<adsm:gridColumn title="documentoServico" 	property="tpDoctoServico" isDomain="true" width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilial" width="30" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>     
		<adsm:gridColumn property="sgFilialDestino" title="destino" width="70" />
		<adsm:gridColumn property="nmPessoaClienteRemetente" title="remetente" width="150" />
		<adsm:gridColumn property="nmPessoaClienteDestinatario" title="destinatario" width="150" />
		<adsm:gridColumn property="qtVolumes" title="volumes" width="80" align="right" />
		<adsm:gridColumn property="psReal" title="peso" width="100" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" dataType="text" title="valorMercadoria" width="30"/>
			<adsm:gridColumn property="dsSimboloMoeda" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlMercadoria" dataType="currency" title="" width="70"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoedaPrejuizo" dataType="text" title="prejuizo" width="30"/>
			<adsm:gridColumn property="dsSimboloMoedaPrejuizo" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlPrejuizo" title="" dataType="currency" width="70"/>
		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn property="sgFilialManifesto " dataType="text" title="manifesto" width="30"/>
			<adsm:gridColumn property="nrPreManifesto" dataType="integer" title="" width="70" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="dhGeracaoFilialRim" title="dataHoraCarta" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="dhEnvioEmailFilialRim" title="dataHoraEnvio" width="150" dataType="JTDateTimeZone"/>
		<adsm:buttonBar>
			<adsm:button id="selecionarDocumentos" caption="selecionar" onclick="selectDocuments()"/>
			<adsm:button id="fechar" caption="fechar" buttonType="closeButton" onclick="returnToParent()"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>

	function findDocumentoServico() {	
		findButtonScript('sinistroDoctoServico', document.getElementById("selecionarDocumentosForm"));
	}
	
	function selectDocuments(){
		window.returnValue = sinistroDoctoServicoGridDef.getSelectedIds();
		if (sinistroDoctoServicoGridDef.getSelectedIds().ids.length==0) {
			lms_22021();
		} else {
			returnToParent();
		}
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	function returnToParent(){
		self.close();
	}
	 
	function disableGridClick(){
		return false;
	}
	
	function resetView(){
		var idProcessoSinistro = getElementValue("processoSinistro.idProcessoSinistro");
		var nrProcessoSinistro = getElementValue("processoSinistro.nrProcessoSinistro");
		
		resetValue(this.document);
		
		setElementValue("processoSinistro.idProcessoSinistro", idProcessoSinistro);
		setElementValue("processoSinistro.nrProcessoSinistro", nrProcessoSinistro);
		limparTagManifesto();
		setFocusOnFirstFocusableField(document);
	}
	
	//##################################
	// Funções da lookup de Manifesto
	//##################################
	
	function limparTagManifesto() {
		resetValue("manifesto.tpManifesto");
		resetValue("manifesto.filialByIdFilialOrigem.idFilial");
		resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
		desabilitaTagManifesto(true);
	}
	
	function desabilitaTagManifesto(valor) {
		if (valor == true) {
			setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
			setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', true);
		}
	}
	
	function filialManifestoCallback_cb(data) {
	   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
	      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	   }
	}
	
	function manifestoAfterPopupSetValue(data) {
		setDisabled('manifesto.manifestoViagemNacional.nrManifestoOrigem', false);
	}

	//##################################
	// Funções da lookup de Manifesto
	//##################################
	function onDoctoServicoChange() {
		return doctoServico_nrDoctoServicoOnChangeHandler();
	}

	function onTpDoctoServicoChange(elemento) {
   		var r = changeDocumentWidgetType({
 		   		documentTypeElement:elemento, 
		   		filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
		   		documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
		   		documentGroup:'SERVICE',
		   		actionService:'lms.seguros.comunicarUnidadesEmissaoRIMAction',
		   		parentQualifier:''
		   	});
		   	
	   return r;
	}
		
	function filialDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("conhecimento.nrConhecimento"));
	   }
	}
	
</script>