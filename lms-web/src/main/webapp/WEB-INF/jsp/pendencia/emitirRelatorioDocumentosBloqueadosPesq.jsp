<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction">
	<adsm:form action="/pendencia/emitirRelatorioDocumentosBloqueados">

		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>		

		<adsm:range label="periodoBloqueio" labelWidth="19%" width="81%" maxInterval="31" required="true">
			<adsm:textbox property="periodoInicial" dataType="JTDate"/>
			<adsm:textbox property="periodoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:lookup property="filialOrigem" idProperty="idFilial"
					 criteriaProperty="sgFilial" 
				 	 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="filialOrigem" dataType="text" size="4" maxLength="3" 
					 labelWidth="19%" width="81%" criteriaSerializable="true"
					 onPopupSetValue="desabilitaDoctoServico"
					 onDataLoadCallBack="desabilitaDoctoServico"
					 onchange="return desabilitaDoctoServico_OnChange(this.value)">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialOrigem.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" 
						  size="50" maxLength="60" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup property="filialDestino" idProperty="idFilial"
					 criteriaProperty="sgFilial" 
				 	 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="filialDestino" dataType="text" size="4" maxLength="3" 
					 labelWidth="19%" width="81%" criteriaSerializable="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" 
						  size="50" maxLength="60" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup property="filialBloqueio" idProperty="idFilial"
					 criteriaProperty="sgFilial" 
				 	 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="filialBloqueio" dataType="text" size="4" maxLength="3" 
					 labelWidth="19%" width="81%" criteriaSerializable="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialBloqueio.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialBloqueio.pessoa.nmFantasia" 
						  size="50" maxLength="60" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup property="localizacaoMercadoria" idProperty="idLocalizacaoMercadoria" 
					 criteriaProperty="cdLocalizacaoMercadoria" 
					 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupLocalizacaoMercadoria" 
					 action="/sim/manterLocalizacoesMercadoria"
					 label="localizacaoMercadoria" dataType="integer" size="4" maxLength="3" 
					 labelWidth="19%" width="81%" minLengthForAutoPopUpSearch="1" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="localizacaoMercadoria.dsLocalizacaoMercadoria" modelProperty="dsLocalizacaoMercadoria" />
			<adsm:textbox property="localizacaoMercadoria.dsLocalizacaoMercadoria" dataType="text" 
						  size="50" maxLength="60" disabled="true"/>
		</adsm:lookup>

		<adsm:hidden property="flagBloqueio" serializable="false" value="B"/>
		<adsm:lookup property="ocorrenciaBloqueio" idProperty="idOcorrenciaPendencia" 
					 criteriaProperty="cdOcorrencia"
					 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupOcorrenciaPendencia"
					 action="/pendencia/manterOcorrenciasPendencia" cmd="list" 
					 label="ocorrenciaBloqueio" dataType="integer" size="4" maxLength="3"
					 labelWidth="19%" width="81%">			
			<adsm:propertyMapping criteriaProperty="flagBloqueio" modelProperty="tpOcorrencia"/>
			<adsm:propertyMapping modelProperty="dsOcorrencia" relatedProperty="ocorrenciaBloqueio.dsOcorrencia"/>			
			<adsm:textbox property="ocorrenciaBloqueio.dsOcorrencia" dataType="text" 
						  size="50" maxLength="60" disabled="true" />			
		</adsm:lookup>

		<adsm:hidden property="idDoctoServico" serializable="true" />
		<adsm:combobox label="documentoServico"
					   labelWidth="19%" width="81%"
					   property="doctoServico.tpDocumentoServico"
					   service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return doctoServicoTpDocumentoServico_OnChange(this.value)">
								 
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
						 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false"
						 popupLabel="pesquisarFilial"
						 onDataLoadCallBack="enableDoctoServico"
						 onchange="return doctoServicoFilialByIdFilialOrigem_OnChange(this.value);"
						 criteriaSerializable="true"/>
								   
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico"		
						 onchange="return doctoServicoNrDoctoServico_OnChange(this.value)"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 onPopupSetValue="retornoPopupDoctoServico"
						 disabled="true" 
						 popupLabel="pesquisarDocumentoServico"
						 mask="00000000" 
						 criteriaSerializable="true"/>
		</adsm:combobox>

		<adsm:range label="emissaoDocumento" labelWidth="19%" width="81%">
			<adsm:textbox property="emissaoDocumentoInicial" dataType="JTDate"/>
			<adsm:textbox property="emissaoDocumentoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:combobox label="tipoFrete" property="tpFrete" domain="DM_TIPO_FRETE" labelWidth="19%" width="81%" renderOptions="true"/>

		<adsm:textbox property="nrNotaFiscal" label="notaFiscal" dataType="integer" size="8" maxLength="6"
					  labelWidth="19%" width="81%" />

		<adsm:hidden property="clienteRemetente.tpSituacao" value="A" serializable="false"/>
 		<adsm:lookup property="clienteRemetente" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 label="remetente" dataType="text"
					 size="20" maxLength="20" labelWidth="19%" width="81%">					 
			<adsm:propertyMapping criteriaProperty="clienteRemetente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="clienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="clienteRemetente.pessoa.nmPessoa" 
						  size="50" maxLength="60" disabled="true" />			  
		</adsm:lookup>

		<adsm:hidden property="clienteDestinatario.tpSituacao" value="A" serializable="false"/>
 		<adsm:lookup property="clienteDestinatario" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 label="destinatario" dataType="text"
					 size="20" maxLength="20" labelWidth="19%" width="81%">					 
			<adsm:propertyMapping criteriaProperty="clienteDestinatario.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="clienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="clienteDestinatario.pessoa.nmPessoa" 
						  size="50" maxLength="60" disabled="true" />			  
		</adsm:lookup>

		<adsm:hidden property="municipioRemetente.tpSituacao" value="A" serializable="false"/>
		<adsm:lookup label="municipioRemetente" 
					 idProperty="idMunicipio" 
					 property="municipioRemetente" 
					 criteriaProperty="nmMunicipio"
					 action="/municipios/manterMunicipios" exactMatch="false" 
					 minLengthForAutoPopUpSearch="3"
					 maxLength="60"
					 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupMunicipio"
					 dataType="text" size="35" labelWidth="19%" width="41%" criteriaSerializable="true">			
			<adsm:propertyMapping criteriaProperty="municipioRemetente.tpSituacao" modelProperty="tpSituacao" />
		</adsm:lookup>

		<adsm:hidden property="municipioDestinatario.tpSituacao" value="A" serializable="false"/>
		<adsm:lookup label="municipioDestinatario" 
					 idProperty="idMunicipio" 
					 property="municipioDestinatario" 
					 criteriaProperty="nmMunicipio"
					 action="/municipios/manterMunicipios" exactMatch="false" 
					 minLengthForAutoPopUpSearch="3"
					 maxLength="60"
					 service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction.findLookupMunicipio"
					 dataType="text" size="35" labelWidth="19%" width="41%" criteriaSerializable="true">			
			<adsm:propertyMapping criteriaProperty="municipioDestinatario.tpSituacao" modelProperty="tpSituacao" />
		</adsm:lookup>

		<adsm:range label="diasEmBloqueio" labelWidth="19%" width="81%" >
			<adsm:textbox property="diasBloqueioInicial" dataType="integer" size="3" maxLength="3" onchange="return diasBloqueioInicial_OnChange()"/>
			<adsm:textbox property="diasBloqueioFinal" dataType="integer" size="3" maxLength="3" onchange="return diasBloqueioFinal_OnChange()"/>
		</adsm:range> 

		<adsm:checkbox property="mostrarOcorrencias" label="mostrarOcorrencias" labelWidth="19%" width="81%"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.pendencia.emitirRelatorioDocumentosBloqueadosAction"/>
			<adsm:button caption="limpar" id="cleanButton" onclick="limpaRegistro()"/>
		</adsm:buttonBar>
		
	</adsm:form>
		
</adsm:window>

<script type="text/javascript">
	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		setDisabled("cleanButton", false);
	}

	function diasBloqueioInicial_OnChange() {
		if (getElementValue("diasBloqueioInicial") != "0" && getElementValue("diasBloqueioFinal") == "") {
			setElementValue("diasBloqueioFinal", getElementValue("diasBloqueioInicial"));
		} else if (getElementValue("diasBloqueioInicial") == "0" && getElementValue("diasBloqueioFinal") == "") {
			setElementValue("diasBloqueioInicial", "");			
		} else if (getElementValue("diasBloqueioInicial") == "" && getElementValue("diasBloqueioFinal") != "") {
			setElementValue("diasBloqueioInicial", "0");			
		} 
		
		return true;
	}

	function diasBloqueioFinal_OnChange() {
		if (getElementValue("diasBloqueioFinal") == "" && getElementValue("diasBloqueioInicial") != "0") {
			setElementValue("diasBloqueioFinal", getElementValue("diasBloqueioInicial"));
		} else if (getElementValue("diasBloqueioFinal") == "" && getElementValue("diasBloqueioInicial") == "0") {
			setElementValue("diasBloqueioInicial", "");
		} else if (getElementValue("diasBloqueioInicial") == "") {
			setElementValue("diasBloqueioInicial", "0");
		} 
		
		return true;
	}
	
	function desabilitaDoctoServico(data) {
		setDisabled("doctoServico.tpDocumentoServico", true);
	}
	
	function desabilitaDoctoServico_cb(data) {
		var boolean = filialOrigem_sgFilial_exactMatch_cb(data);
		if (boolean && data[0].sgFilial != "") {
			setDisabled("doctoServico.tpDocumentoServico", true);
		} 
	}
	
	function desabilitaDoctoServico_OnChange(valor) {
		if (valor == "") {
			setDisabled("doctoServico.tpDocumentoServico", false);
		}
		return filialOrigem_sgFilialOnChangeHandler();
	}
	
	/**
	 * Limpa a tela e carrega os dados necessarios novamente.
	 */
	function limpaRegistro() {
		resetValue(this.document);
		setDisabled("filialOrigem.idFilial", false);
		setDisabled("doctoServico.tpDocumentoServico", false);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);		
		setFocus(document.getElementById("periodoInicial"));
	}		
	

	/**
	 * #############################################################
	 * # Inicio das funções para a tag customizada de DoctoServico #
	 * #############################################################
	 */
	
	function doctoServicoTpDocumentoServico_OnChange(valor) {
		var boolean = changeDocumentWidgetType({
								 documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"),
								 filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"),
								 documentNumberElement:document.getElementById("doctoServico.idDoctoServico"),
								 documentGroup:"SERVICE",
								 actionService:"lms.pendencia.emitirRelatorioDocumentosBloqueadosAction" });
		
		if (valor != "") {
			setDisabled("filialOrigem.idFilial", true);
		} else {
			setDisabled("filialOrigem.idFilial", false);
		}
		return boolean;
		
	}

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
		if (boolean == true) {
			if (data != undefined && data.length > 0) {
				setElementValue("doctoServico.idDoctoServico", data[0].idDoctoServico);
				//Campo hidden
				setElementValue("idDoctoServico", data[0].idDoctoServico);
			}
		}
	}	
	
	function doctoServicoFilialByIdFilialOrigem_OnChange(valor) {
		var boolean = changeDocumentWidgetFilial( { 
											filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"),
								      		documentNumberElement:document.getElementById("doctoServico.idDoctoServico") } );
		
		if (valor == "") {
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
		}		
		return boolean;		
	}	
	
	function doctoServicoNrDoctoServico_OnChange(valor) {
		var r = doctoServico_nrDoctoServicoOnChangeHandler();
		
		if (valor == "") {
			resetValue("doctoServico.idDoctoServico");
			resetValue("idDoctoServico");
		}		
		return r;		
	}
	
	function retornoPopupDoctoServico(data){
		if (data.idDoctoServico){
			setElementValue("idDoctoServico", data.idDoctoServico);
			setElementValue("doctoServico.idDoctoServico", data.idDoctoServico);
		}
	}	
		
	/**
	 * ##########################################################
	 * # Fim das funções para a tag customizada de DoctoServico #
	 * ##########################################################
	 */
	 
</script>