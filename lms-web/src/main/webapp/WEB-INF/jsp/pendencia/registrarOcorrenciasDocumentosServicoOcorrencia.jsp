<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
	function pageLoad(data, error){
		setRowVisibility("cdOcorrenciaBloqueio");	
		onPageLoad(data, error);
	}

	/**
	 * Carrega página.
	 */	
	function carregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		setDisabled("newButton", false);
		document.getElementById("doctoServico.tpDocumentoServico").required = "true";
		document.getElementById("doctoServico.filialByIdFilialOrigem.sgFilial").required = "true";
		// Seta o label para o campo valor de mercadoria
		document.getElementById("valorMulta").label = document.getElementById("moeda.idMoeda").label;
		document.getElementById("moeda.idMoeda").label = document.getElementById("moeda.idMoeda").label;
	    var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosServicoAction.getDadosSessao", "buscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});	
	}
	
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	var dadosSessao;
	function buscarDadosSessao_cb(data, error) {
		dadosSessao = data;
		setaDadosSessao();
		setElementValue("doctoServico.tpDocumentoServico", "CTR");
		doctoServicoTpDocumentoServico_OnChange("CTR");
	}
	
		/**
	 * Seta os Dados referente ao usuário da sessão.
	 */
	function setaDadosSessao() {
		setElementValue("moeda.idMoeda", dadosSessao.idMoedaSessao);
		setElementValue("blMatriz", dadosSessao.blMatriz);
	}	
	
</script>
<adsm:window service="lms.pendencia.registrarOcorrenciasDocumentosServicoAction" onPageLoad="pageLoad" onPageLoadCallBack="carregaPagina">

	<adsm:form action="/pendencia/registrarOcorrenciasDocumentosServico" height="370" >
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>

		<adsm:hidden property="origem" value="regOcorrDoctoServico"/>

		<adsm:hidden property="blBloqueado"/>

		<adsm:combobox label="documentoServico"
					   labelWidth="23%" width="77%"
					   property="doctoServico.tpDocumentoServico"
					   service="lms.pendencia.registrarOcorrenciasDocumentosServicoAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="doctoServicoTpDocumentoServico_OnChange(this.value);">
								 
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
						 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 popupLabel="pesquisarFilial"
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 onDataLoadCallBack="enableDoctoServico"
						 onchange="return doctoServicoFilialByIdFilialOrigem_OnChange(this.value);"/>
								   
			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service=""
						 action=""
						 popupLabel="pesquisarDocumentoServico"
						 onDataLoadCallBack="retornoDocumentoServico"
						 onPopupSetValue="retornoPopupDocumentoServico"
						 onchange="return doctoServicoNrDoctoServico_OnChange(this.value)"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 disabled="true" 
						 required="true"
						 mask="00000000" />
		</adsm:combobox>

		<adsm:hidden property="idOcorrenciaBloqueio"/>
		<adsm:textbox label="ocorrenciaBloqueio" property="cdOcorrenciaBloqueio" labelWidth="23%" width="52%" size="4" dataType="text" disabled="true">
			<adsm:textbox property="dsOcorrenciaBloqueio" dataType="text" size="50" disabled="true"/>
		</adsm:textbox>
 		<adsm:combobox width="25%" disabled="true"  property="ocorrenciaPendencia.tpOcorrencia" domain="DM_TIPO_OCORRENCIA_PENDENCIA" style="visibility:hidden" renderOptions="true"/>
				
		<adsm:hidden property="ocorrenciaPendencia.blExigeRnc"/>
		<adsm:hidden property="ocorrenciaPendencia.blApreensao"/>
		<adsm:hidden property="ocorrenciaPendencia.evento.idEvento"/>
		<adsm:hidden property="blMatriz"/>
		<adsm:lookup property="ocorrenciaPendencia" idProperty="idOcorrenciaPendencia" 
					 criteriaProperty="cdOcorrencia"
					 service="lms.pendencia.registrarOcorrenciasDocumentosServicoAction.findLookupOcorrenciaPendencia"
					 action="/pendencia/registrarOcorrenciasDocumentosServico" cmd="ocorrenciaLookup"
					 onPopupSetValue="verificaOcorrenciaPendencia"
					 onDataLoadCallBack="verificaOcorrenciaPendencia"
					 onchange="return ocorrenciaPendenciaCdOcorrencia_OnChange(this.value)" 
 					 popupLabel="pesquisarOcorrenciaPendencia"
					 label="ocorrenciaBloqueio" dataType="integer" size="4" maxLength="3"
					 labelWidth="23%" width="77%" criteriaSerializable="true" disabled="true" required="true" >					 
			<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico"/>
			<adsm:propertyMapping criteriaProperty="idOcorrenciaBloqueio" modelProperty="idOcorrenciaBloqueio"/>
			<adsm:propertyMapping criteriaProperty="ocorrenciaPendencia.tpOcorrencia" modelProperty="tpOcorrencia"/>
			<adsm:propertyMapping criteriaProperty="blMatriz" modelProperty="blMatriz"/>
			<adsm:propertyMapping modelProperty="dsOcorrencia" relatedProperty="ocorrenciaPendencia.dsOcorrencia"/>
			<adsm:propertyMapping modelProperty="evento.idEvento" relatedProperty="ocorrenciaPendencia.evento.idEvento"/>
			<adsm:propertyMapping modelProperty="blApreensao" relatedProperty="ocorrenciaPendencia.blApreensao"/>
			<adsm:propertyMapping modelProperty="blExigeRnc" relatedProperty="ocorrenciaPendencia.blExigeRnc"/>
			<adsm:textbox property="ocorrenciaPendencia.dsOcorrencia" dataType="text" 
						  size="50" maxLength="60" disabled="true" />			
		</adsm:lookup>
		
		<adsm:textbox property="dhOcorrencia" label="dataOcorrencia" dataType="JTDateTimeZone"
					  labelWidth="23%" width="27%" disabled="true" picker="false"/>

		<adsm:hidden property="naoConformidade.filial.idFilial" />
		<adsm:hidden property="naoConformidade.idNaoConformidade" />		
		<adsm:textbox label="rnc" property="naoConformidade.filial.sgFilial" dataType="text"  
					  disabled="true" size="3" maxLength="3" labelWidth="23%" width="77%">
			<adsm:textbox property="naoConformidade.nrNaoConformidade" dataType="integer"  
						  size="8" maxLength="8" mask="00000000" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="processoSinistro.idProcessoSinistro"/>	
		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="23%" width="77%" disabled="true" />	

		<!-- Retirar o espaço ao criar o código para armazenamento. -->
		<adsm:label key="espacoBranco" width="100%" style="border:none"/>

		<adsm:section caption="comunicadoApreensao"/>

		<adsm:textbox property="numeroTermoApreensao" label="numeroTermoApreensao" dataType="integer" maxLength="20" size="20" 
					  width="77%" labelWidth="23%" disabled="true"/>

		<adsm:textbox property="dtTermoApreensao" label="dataTermoApreensao" dataType="JTDate" 
					  width="77%" labelWidth="23%" disabled="true"/>

		<adsm:combobox property="moeda.idMoeda" label="valorMulta"
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   service="lms.pendencia.registrarOcorrenciasDocumentosServicoAction.findMoeda" 
					   labelWidth="23%" width="77%" boxWidth="85" disabled="true">
			<adsm:textbox property="valorMulta" dataType="currency" mask="#,###,###,###,###,##0.00" size="16" maxLength="18" disabled="true"/>
		</adsm:combobox>

		<adsm:textarea property="motivoAlegado" label="motivoAlegado" maxLength="200" 
					   columns="85" rows="3" labelWidth="23%" width="77%" disabled="true"/>
					   
		<adsm:buttonBar>
			<adsm:button caption="comunicadoApreensao" id="comunicadoApreensao" action="/pendencia/emitirComunicadoApreensaoCliente" cmd="main">
				<adsm:linkProperty src="origem" target="origem"/>
				<adsm:linkProperty src="doctoServico.idDoctoServico" target="ocorrenciaDoctoServico.doctoServico.idDoctoServico"/>
				<adsm:linkProperty src="doctoServico.tpDocumentoServico" target="ocorrenciaDoctoServico.doctoServico.tpDocumentoServico"/>
				<adsm:linkProperty src="doctoServico.filialByIdFilialOrigem.idFilial" target="ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="doctoServico.filialByIdFilialOrigem.sgFilial" target="ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="doctoServico.nrDoctoServico" target="ocorrenciaDoctoServico.doctoServico.nrDoctoServico"/>
			</adsm:button>
			<adsm:button buttonType="storeButton" caption="salvar" id="storeButton" onclick="storeButtonClick(this.form)" />
			<adsm:button caption="limpar" id="newButton" buttonType="newButton" onclick="newButtonClick()"/>
		</adsm:buttonBar>

		<script>
			function getLabelOcorrenciaBloqueio(){
				var labelOcorrenciaBloqueio = '<adsm:label key="ocorrenciaBloqueio"/>';
				return labelOcorrenciaBloqueio;
			}
			
			function getLabelOcorrenciaLiberacao(){
				var labelOcorrenciaLiberacao = '<adsm:label key="ocorrenciaLiberacao"/>';
				return labelOcorrenciaLiberacao;
			}
			
			function lms_17052() {
				return confirm("<adsm:label key='LMS-17052'/>");
			}
		</script>
		
	</adsm:form>
	
</adsm:window>

<script	type="text/javascript">
	
	/**
	 * Função para limpar a tela.
	 */
	function newButtonClick() {
   	    resetValue(this.document);
		document.getElementById("spanlbl_ocorrenciaPendencia").innerText = getLabelOcorrenciaBloqueio();
		setRowVisibility("cdOcorrenciaBloqueio");	   	    
   	    setDisabled("doctoServico.tpDocumentoServico", false);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true); //Desabilita inclusive a lupa
		setElementValue("doctoServico.tpDocumentoServico", "CTR");
		doctoServicoTpDocumentoServico_OnChange("CTR");
   	    setDisabled("ocorrenciaPendencia.idOcorrenciaPendencia", true);
   	    habilitaComunicadoApreensao(false);
   	    setDisabled("storeButton", false);
   		setFocus("doctoServico.tpDocumentoServico");	
		setaDadosSessao();
	}
	
	/**
	 * Função onDataLoadCallBack da lookup de Ocorrência
	 */
	function verificaOcorrenciaPendencia_cb(data, error) {
		var boolean = ocorrenciaPendencia_cdOcorrencia_exactMatch_cb(data);		
		if(boolean == true) {
			// Valida o Ocorrência Pendencia.
			verificaOcorrenciaPendencia(data[0])
		}
	}
	
	// Função onPopupValue da lookup de Ocorrência
	function verificaOcorrenciaPendencia(data) {
		// Valida o Ocorrência Pendencia.
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "idOcorrenciaBloqueio", getElementValue("idOcorrenciaBloqueio"));
	   	setNestedBeanPropertyValue(mapCriteria, "doctoServico.idDoctoServico", getElementValue("doctoServico.idDoctoServico"));
	   	setNestedBeanPropertyValue(mapCriteria, "naoConformidade.idNaoConformidade", getElementValue("naoConformidade.idNaoConformidade"));
	   	setNestedBeanPropertyValue(mapCriteria, "ocorrenciaPendencia.idOcorrenciaPendencia", data.idOcorrenciaPendencia);
	   	setNestedBeanPropertyValue(mapCriteria, "ocorrenciaPendencia.tpOcorrencia", getElementValue("ocorrenciaPendencia.tpOcorrencia"));
	   	setNestedBeanPropertyValue(mapCriteria, "ocorrenciaPendencia.evento.idEvento", data.evento.idEvento);
	   	setNestedBeanPropertyValue(mapCriteria, "ocorrenciaPendencia.blExigeRnc", data.blExigeRnc);
	   	var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosServicoAction.validaOcorrenciaPendencia", 
										  "validaOcorrenciaPendencia", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	// Função de retorno da validação da ocorrência de pendência.
	function validaOcorrenciaPendencia_cb(data, error) {
		if(!error) {
			if(data.existsDocumentoAdicional && !lms_17052()){
				resetOcorrenciaPendencia();
				setFocus("ocorrenciaPendencia.cdOcorrencia");
				return;
			}
			if (getElementValue("ocorrenciaPendencia.blApreensao") == "true") {
				habilitaComunicadoApreensao(true);
			} else {
				habilitaComunicadoApreensao(false);
			}
		} else {
			alert(error);
			resetOcorrenciaPendencia();
			setFocus("ocorrenciaPendencia.cdOcorrencia");
		}
	}	
	
	// Função que limpa a lookup de ocorrencia e desabilita os campos de Comunicado Apreensão.
	function ocorrenciaPendenciaCdOcorrencia_OnChange(valor) {
		var boolean = ocorrenciaPendencia_cdOcorrenciaOnChangeHandler();
		if (boolean ==true && valor == "") {
       	    resetValue("ocorrenciaPendencia.idOcorrenciaPendencia");
       	    resetComunicadoApreensao();
		}		
		return boolean;		
	}		
	
	/**
	 * Função que habilita / desabilita a sessão de Comunicação de Apreensão.
	 */	
	function habilitaComunicadoApreensao(boolean) {
		if(boolean == true) {
			setDisabled("numeroTermoApreensao", false);
			setDisabled("dtTermoApreensao", false);
			setDisabled("moeda.idMoeda", false);
			setDisabled("valorMulta", false);
			setDisabled("motivoAlegado", false);
		} else {
			setDisabled("numeroTermoApreensao", true);
			setDisabled("dtTermoApreensao", true);
			setDisabled("moeda.idMoeda", true);
			setDisabled("valorMulta", true);
			setDisabled("motivoAlegado", true);
		}
	}
		
	function validaComunicadoApreensao(){
		if ((getElementValue("numeroTermoApreensao") != "")
			|| (getElementValue("dtTermoApreensao") != "")
			|| (getElementValue("valorMulta") != "")
			|| (getElementValue("motivoAlegado") != "")){
				document.getElementById("numeroTermoApreensao").required = "true";
				document.getElementById("dtTermoApreensao").required = "true";
				document.getElementById("moeda.idMoeda").required = "true";
				document.getElementById("valorMulta").required = "true";
				document.getElementById("motivoAlegado").required = "true";	
		} else {
			document.getElementById("numeroTermoApreensao").required = "false";
			document.getElementById("dtTermoApreensao").required = "false";
			document.getElementById("moeda.idMoeda").required = "false";
			document.getElementById("valorMulta").required = "false";
			document.getElementById("motivoAlegado").required = "false";	
		}
	}
	
	function storeButtonClick(formOcorrenciasDocumentosServico){
		validaComunicadoApreensao();
		storeButtonScript('lms.pendencia.registrarOcorrenciasDocumentosServicoAction.store', 'storeCallBack', formOcorrenciasDocumentosServico);
	}

	function storeCallBack_cb(data, error, key){
		if (!error){
			setElementValue("dhOcorrencia", setFormat(document.getElementById("dhOcorrencia"), data.dataHora));
			setDisabledForm(document.forms[0], true);
			setDisabled("newButton", false);
			showSuccessMessage();
			setFocus('newButton', false);
		} else {
			alert(error);
			if (key == 'LMS-17031' || key == 'LMS-17032') {
				newButtonClick();
			}
		}
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

	function retornoPopupDocumentoServico(data) {
		setDisabled(document.getElementById("doctoServico.nrDoctoServico"), false, undefined, true);
		setElementValue("doctoServico.idDoctoServico", data.idDoctoServico);
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
		setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", data.filialByIdFilialOrigem.sgFilial);
		validaDoctoServico(data);
	}
	
	function retornoDocumentoServico_cb(data, error) {		
		var boolean = doctoServico_nrDoctoServico_exactMatch_cb(data);
		if (boolean == true) {
			if (data && data.length>0){
				setElementValue("doctoServico.idDoctoServico", data[0].idDoctoServico);
				setElementValue("blBloqueado", data[0].blBloqueado);
				validaDoctoServico(data[0]);
			} 
		}
		return boolean;
	}
	
	/**
	 * Realiza pesquisa para os dados necessarios da tela.
	 * Esta função não é obrigatória.
	 */
	function validaDoctoServico(data) {
		// Valida o Documento de Serviço.
		var mapCriteria = new Array();	   
	   	setNestedBeanPropertyValue(mapCriteria, "idDoctoServico", getElementValue("doctoServico.idDoctoServico"));		
		var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosServicoAction.validaDoctoServico", 
										  "validaDoctoServico", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	/**
	 * Retorno da validação do DoctoServico.
	 * Em caso válido, busca os dados de NaoConformidade e Processo Sinistro
	 * a partir do ID do DoctoServico
	 */
	function validaDoctoServico_cb(data, error) {
		resetCamposDependentesDoDoctoServico();		
		if(!error){
			setDisabled("ocorrenciaPendencia.idOcorrenciaPendencia", false);
			setFocus(document.getElementById("ocorrenciaPendencia.cdOcorrencia"));
			setaDadosNaoConformidadeProcessoSinistro(data, error);
		} else {
			alert(error);
			resetValue("doctoServico.nrDoctoServico");
			setFocus(document.getElementById("doctoServico.nrDoctoServico"));
		}
	}	
		
	/**
	 * Retorno da pesquisa dos dados da tela.
	 * Esta função não é obrigatória. 
	 */
	function setaDadosNaoConformidadeProcessoSinistro(data, error) {
		setElementValue("ocorrenciaPendencia.tpOcorrencia", "B");
		
		//Caso tenha uma ocorrência de bloqueio...
		if(data.idOcorrenciaBloqueio){
			setElementValue("idOcorrenciaBloqueio", data.idOcorrenciaBloqueio);
			setElementValue("cdOcorrenciaBloqueio", data.cdOcorrenciaBloqueio);
			setElementValue("dsOcorrenciaBloqueio", data.dsOcorrenciaBloqueio);
			setElementValue("ocorrenciaPendencia.tpOcorrencia", "L");
			setRowVisibility("cdOcorrenciaBloqueio", "show");
			document.getElementById("spanlbl_ocorrenciaPendencia").innerText = getLabelOcorrenciaLiberacao();
		}	
		//Caso tenha uma Não Conformidade...
		if(data.idNaoConformidade) {			
			setElementValue("naoConformidade.idNaoConformidade", data.idNaoConformidade);
			setElementValue("naoConformidade.filial.idFilial", data.idFilial);
			setElementValue("naoConformidade.filial.sgFilial", data.sgFilial);
			setElementValue("naoConformidade.nrNaoConformidade", setFormat(document.getElementById("naoConformidade.nrNaoConformidade"), data.nrNaoConformidade));
		}

		//Caso tenha um Processo de Sinistro...
		if(data.idProcessoSinistro) {			
			setElementValue("processoSinistro.idProcessoSinistro", data.idProcessoSinistro);
			setElementValue("nrProcessoSinistro", setFormat(document.getElementById("nrProcessoSinistro"), data.nrProcessoSinistro));
		}
	}
	
	function resetCamposDependentesDoDoctoServico(){
		document.getElementById("spanlbl_ocorrenciaPendencia").innerText = getLabelOcorrenciaBloqueio();
		setRowVisibility("cdOcorrenciaBloqueio");	
		resetValue("idOcorrenciaBloqueio");
		resetValue("cdOcorrenciaBloqueio");
		resetValue("dsOcorrenciaBloqueio");
		resetValue("ocorrenciaPendencia.tpOcorrencia");
		resetValue("dhOcorrencia");
		resetValue("naoConformidade.filial.idFilial");
		resetValue("naoConformidade.filial.sgFilial");
		resetValue("naoConformidade.idNaoConformidade");
		resetValue("naoConformidade.nrNaoConformidade");
		resetValue("processoSinistro.idProcessoSinistro");
		resetValue("nrProcessoSinistro");
		resetComunicadoApreensao();
	}
	
	function resetComunicadoApreensao(){
		resetValue("numeroTermoApreensao");
		resetValue("dtTermoApreensao");
		resetValue("moeda.idMoeda");
		resetValue("valorMulta");
		resetValue("motivoAlegado");
		habilitaComunicadoApreensao(false);
		setaDadosSessao();
	}
	
	function resetOcorrenciaPendencia(){
		resetValue("ocorrenciaPendencia.idOcorrenciaPendencia");
		resetComunicadoApreensao();
	}

	function doctoServicoTpDocumentoServico_OnChange(valor){
		changeDocumentWidgetType({
								 documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"),
								 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
								 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
								 documentGroup:'SERVICE',
								 actionService:'lms.pendencia.registrarOcorrenciasDocumentosServicoAction' });

		doctoServicoFilialByIdFilialOrigem_OnChange("");						
		if (getElementValue('doctoServico.tpDocumentoServico')=='') {
			setDisabled('doctoServico.idDoctoServico', true);
			setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', true);
		}else{
			setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', false);
		}
	}
	
	function doctoServicoFilialByIdFilialOrigem_OnChange(valor) {
		var boolean = changeDocumentWidgetFilial( { 
											filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"),
								      		documentNumberElement:document.getElementById("doctoServico.idDoctoServico") } );
		if (boolean ==true && valor == ""){
			doctoServicoNrDoctoServico_OnChange("");								      		
		}
		return boolean;		
	}
	
	function doctoServicoNrDoctoServico_OnChange(valor) {
		
		var r = doctoServico_nrDoctoServicoOnChangeHandler();
		
		if (r ==true && valor == "") {
			resetOcorrenciaPendencia();
			setDisabled("ocorrenciaPendencia.idOcorrenciaPendencia", true);
			resetCamposDependentesDoDoctoServico();
		}		
				
		return r;		
	}	
		
	/**
	 * ##########################################################
	 * # Fim das funções para a tag customizada de DoctoServico #
	 * ##########################################################
	 */

</script>