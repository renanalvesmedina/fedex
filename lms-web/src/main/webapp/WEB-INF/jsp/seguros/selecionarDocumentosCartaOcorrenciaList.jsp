<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	/**
	 * Carrega dados e features da tela.
	 */
	function loadData(){
		loadDataFromParent();
		onPageLoad();
		disableButtons(false);
	}

	/**
	 * Carrega os dados da tela parente.
	 */
	function loadDataFromParent(){
		var parentWindow = dialogArguments.window.document;
		
		setElementValue("processoSinistro.idProcessoSinistro", parentWindow.getElementById("processoSinistro.idProcessoSinistro").value);
		setElementValue("nrProcessoSinistro", parentWindow.getElementById("nrProcessoSinistro").value);
		format("nrProcessoSinistro");
	}

	/**
	 * Desabilitar os botoes da tela.
	 *
	 * @param disable
	 */
	function disableButtons(disable) {
		setDisabled("buscar", disable);
		setDisabled("limpar", disable);
		setDisabled("selecionarDocumentos", disable);
		setDisabled("fecharDocumentos", disable);
	}
	
</script>

<adsm:window title="selecionarDocumentosCartaOcorrencia" service="lms.seguros.selecionarDocumentosCartaOcorrenciaAction" onPageLoad="loadData" >
	<adsm:form action="/seguros/selecionarDocumentosCartaOcorrencia" height="165" id="selecionarDocumentosForm">
		<adsm:section caption="selecionarDocumentosCartaOcorrencia"/>
		
			<adsm:hidden property="processoSinistro.idProcessoSinistro"/>
		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />


		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" labelWidth="15%" width="35%" serializable="true" 
					   service="lms.seguros.selecionarDocumentosCartaOcorrenciaAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
						   documentGroup:'MANIFESTO',
						   actionService:'lms.seguros.selecionarDocumentosCartaOcorrenciaAction'
						   });" >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 popupLabel="pesquisarFilial"
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false" 
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
						 onchange="return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	}); "
						 />

			<adsm:lookup dataType="integer" 
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service="" popupLabel="pesquisarManifesto"
						 action="" 
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" />
		</adsm:combobox>
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" />
		<adsm:hidden property="manifesto.tpManifestoEntrega" value="EN" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />

		
		<adsm:lookup dataType="text" property="doctoServico.clienteByIdClienteRemetente" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" service="lms.seguros.selecionarDocumentosCartaOcorrenciaAction.findClienteLookup" 
					 label="remetente" width="85%" maxLength="20" size="20">
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="doctoServico.clienteByIdClienteDestinatario" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao" service="lms.seguros.selecionarDocumentosCartaOcorrenciaAction.findClienteLookup" 
					 label="destinatario" width="85%" maxLength="20" size="20">
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:hidden property="tpSituacao" value="A"/>



		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" width="85%"
					   service="lms.seguros.selecionarDocumentosCartaOcorrenciaAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						   documentGroup:'SERVICE',
						   actionService:'lms.seguros.selecionarDocumentosCartaOcorrenciaAction'
					   });"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 popupLabel="pesquisarFilial"
						 
						 action=""
						 size="3" maxLength="3" picker="false" onDataLoadCallBack="enableNaoConformidadeDoctoServico"
						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
							 });"
						 />
			
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						 service=""
						 action="" popupLabel="pesquisarDocumentoServico"
						 onDataLoadCallBack="retornoDocumentoServico"
						 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />
		</adsm:combobox>

		<adsm:combobox property="tpPrejuizo" domain="DM_TIPO_PREJUIZO" label="prejuizo" width="85%" renderOptions="true"/>

		<adsm:checkbox property="blEnviado" label="somenteNaoEnviados"/>
		<adsm:checkbox property="blEnviado2" label="somenteNaoEnviados"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton id="buscar" callbackProperty="sinistroDoctoServico" />
			<adsm:button caption="limpar" id="limpar" onclick="resetView();"/>
		</adsm:buttonBar>
		<script>
			function lms_22012() {
				alert("<adsm:label key='LMS-22012'/>");
			}
		</script>
		
	</adsm:form>
	
	<adsm:grid property="sinistroDoctoServico" idProperty="idSinistroDoctoServico" 
			   gridHeight="122" title="documentosSelecionados" maxRows="6" rows="6"
			   onRowClick="disableGridClick" scrollBars="horizontal">
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
		<adsm:gridColumn property="dhCarta" title="dataHoraCarta" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="dhEnvio" title="dataHoraEnvio" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="tipoCarta" title="tipoCarta" width="100" />
		<adsm:buttonBar>
			<adsm:button id="selecionarDocumentos" caption="selecionar" onclick="selectDocuments();"/>
			<adsm:button id="fecharDocumentos" caption="fechar" onclick="returnToParent();"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script><!--

	function initWindow(eventObj) {
		if (eventObj.name == "tab_load") {
		} else if (eventObj.name == "cleanButton_click") {
			setFocus(document.getElementById("controleCarga.idControleCarga"));
			disableButtons(false);
		}
	}
	
	//##################################
    // Comportamentos apartir de objetos
	//##################################
	

	/************************************ INICIO - MANIFESTO ************************************/

	/**
	 * Limpa os dados informados na tag manifesto
	 */
	function resetaTagManifesto() {
		resetValue("manifesto.tpManifesto");
		resetValue("manifesto.filialByIdFilialOrigem.idFilial");
		resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
		setDisabled('manifesto.tpManifesto', false);
		setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
		setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', true);
	}
	
	function enableManifestoManifestoViagemNacioal_cb(data) {
	   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
	      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	   }
	}
	
	/************************************ FIM - MANIFESTO ***********************************/

	
	/**
	 * javaScripts para a 'tag documents'
	 */
	function enableNaoConformidadeDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}
	
	function retornoDocumentoServico_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
	}
	
	/**
	 * Captura os ids selecionados na tela.
	 */
	function selectDocuments(){
	    var ids = sinistroDoctoServicoGridDef.getSelectedIds();
	    if (ids.ids.length >0){   
		    dialogArguments.window.setGridSelectedIds(ids);
       	    self.close();
       	}else{
       	    lms_22012();
       	}
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	 
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
	
		
	/**
	 * Reseta a tela
	 */
	function resetView(){
		var idProcessoSinistro = getElementValue("processoSinistro.idProcessoSinistro");
		var nrProcessoSinistro = getElementValue("nrProcessoSinistro");
		
		resetValue(this.document);
		
		setElementValue("processoSinistro.idProcessoSinistro", idProcessoSinistro);
		setElementValue("nrProcessoSinistro", nrProcessoSinistro);
		
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);
		setDisabled("manifesto.filialByIdFilialOrigem.idFilial", true);
		setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", true);
	}
--></script>
