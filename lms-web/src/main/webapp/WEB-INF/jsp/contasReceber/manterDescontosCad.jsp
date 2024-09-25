<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterDescontosAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/manterDescontos" idProperty="idDesconto" onDataLoadCallBack="myOnDataLoadCallBack">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-02067"/>
			<adsm:include key="documentoServico"/>
			<adsm:include key="valorDocumento"/>
		</adsm:i18nLabels>
	
		<adsm:hidden property="flagFretePrincipal" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="descontoDsConcatenado" serializable="false"/>
		<adsm:hidden property="vlMinimoDocumentoDesconto" serializable="false"/>
		<adsm:hidden property="blDesabilitar" />


		<adsm:hidden property="devedorDocServFat.filial.idFilial" serializable="true"/>
		<adsm:textbox label="filialCobranca" labelWidth="20%" width="80%" property="devedorDocServFat.filial.sgFilial" disabled="true" dataType="text" size="3">
			<adsm:textbox property="devedorDocServFat.filial.pessoa.nmFantasia" dataType="text"  size="50" disabled="true"/>
		</adsm:textbox>
		
		<adsm:combobox property="devedorDocServFat.doctoServico.tpDocumentoServico"
					   label="documentoServico" 
					   labelWidth="20%" 
					   width="80%"
					   service="lms.contasreceber.manterDescontosAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   defaultValue="CTR"
					   onchange="limpaDadosClienteEValores();
					   	 		 return onChangeTpDocumentoServico();"> 			

			<adsm:lookup dataType="text"
						 property="devedorDocServFat.doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterDescontosAction.findLookupFilial"
						 action="/municipios/manterFiliais" 
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 exactMatch="true"
						 popupLabel="pesquisarFilial"
						 onchange="limpaDadosClienteEValores();
						 		   return filialOnChange();" />		
										 
			<adsm:lookup dataType="integer"
						 property="devedorDocServFat"
						 idProperty="idDevedorDocServFat" 
						 criteriaProperty="doctoServico.nrDoctoServico"
						 service="lms.contasreceber.manterDescontosAction.findDevedorServDocFat"
						 action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						 cmd="pesq"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 exactMatch="true"
						 mask="00000000"
						 onchange="return doctoServicoOnChange()"
						 onDataLoadCallBack="retornoDocumentoServico"
						 onPopupSetValue="myOnPopupSetValue"
						 popupLabel="pesquisarDocumentoServico"
						 required="true">
						 <adsm:propertyMapping modelProperty="doctoServico.tpDocumentoServico" criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico"/>
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"/>
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="true"/>
						 <adsm:propertyMapping modelProperty="tpSituacaoDevedorDocServFatValido" criteriaProperty="tpSituacaoDevedorDocServFatValido"/>
						 <adsm:propertyMapping modelProperty="vlDevido" relatedProperty="devedorDocServFat.vlDevido"/>
						 <adsm:propertyMapping modelProperty="idFilial" relatedProperty="devedorDocServFat.filial.idFilial"/>
						 <adsm:propertyMapping modelProperty="sgFilial" relatedProperty="devedorDocServFat.filial.sgFilial"/>
						 <adsm:propertyMapping modelProperty="nmFantasia" relatedProperty="devedorDocServFat.filial.pessoa.nmFantasia"/>
						 <adsm:propertyMapping modelProperty="idCliente" relatedProperty="devedorDocServFat.cliente.idCliente"/>
						 <adsm:propertyMapping modelProperty="nrIdentificacaoResponsavelAnterior" relatedProperty="devedorDocServFat.cliente.pessoa.nrIdentificacao"/>
						 <adsm:propertyMapping modelProperty="nmResponsavelAnterior" relatedProperty="devedorDocServFat.cliente.pessoa.nmPessoa"/>
						 <adsm:propertyMapping modelProperty="idMoeda" relatedProperty="devedorDocServFat.doctoServico.moeda.idMoeda"/>
						 <adsm:propertyMapping modelProperty="idServico" relatedProperty="devedorDocServFat.doctoServico.servico.idServico"/>
						 <adsm:propertyMapping modelProperty="tpModal.value" relatedProperty="devedorDocServFat.doctoServico.servico.tpModal"/>
						 <adsm:propertyMapping modelProperty="tpAbrangencia.value" relatedProperty="devedorDocServFat.doctoServico.servico.tpAbrangencia"/>
						 <adsm:propertyMapping modelProperty="idDoctoServico" relatedProperty="devedorDocServFat.doctoServico.idDoctoServico"/>
			</adsm:lookup>			 
		</adsm:combobox>
		
		<adsm:hidden property="nrDocumento" serializable="true"/>
		<adsm:hidden property="tpDocumentoHidden"/>
		<adsm:hidden property="tpDocumentoServicoHidden"/>
		<adsm:hidden property="tpSituacaoDevedorDocServFatValido" serializable="true" value="0"/>
		
		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>
					   
		<adsm:hidden property="devedorDocServFat.cliente.idCliente" serializable="true"/>
		<adsm:textbox label="clienteResponsavel" 
					  property="devedorDocServFat.cliente.pessoa.nrIdentificacao" 					  
					  dataType="text" 
					  size="20" 
					  disabled="true"
					  labelWidth="20%"
					  width="80%">
			<adsm:textbox property="devedorDocServFat.cliente.pessoa.nmPessoa" dataType="text" size="76" disabled="true"/>
		</adsm:textbox>			   		
					  
		<adsm:combobox label="situacaoAprovacao"					   
					   labelWidth="20%" 
					   width="30%"					   
					   property="tpSituacaoAprovacao"
					   domain="DM_STATUS_WORKFLOW"
					   disabled="true"/>			  
					   
		<adsm:textbox label="valorDesconto" 
					  dataType="currency" 
					  property="vlDesconto" 
					  labelWidth="20%" 
					  width="30%"
					  size="21"
					  maxLength="18"
					  onchange="return valorDescontoOnChange()"					  
					  required="true"/>
			
		<adsm:hidden property="devedorDocServFat.doctoServico.moeda.idMoeda" serializable="true"/>
		<adsm:textbox label="valorDocumento" 
					  labelWidth="20%"
					  width="30%"
					  dataType="text" 
					  property="devedorDocServFat.doctoServico.moeda.siglaSimbolo" 
					  disabled="true" 
					  size="10" 
					  maxLength="10">
			<adsm:textbox dataType="currency" 
						  property="devedorDocServFat.vlDevido" 						  
						  disabled="true"/>
		</adsm:textbox>	
		
		<adsm:textbox label="percentualDesconto" 
					  labelWidth="20%" 
					  width="30%"
					  property="percentualDesconto" 
					  dataType="percent" 
					  disabled="true"
					  maxLength="18" 
					  size="21"/>
			 
		<adsm:textarea label="observacao" labelWidth="20%" width="80%" columns="100" rows="3" maxLength="255" property="obDesconto"/>
		
		<adsm:textarea label="acaoCorretiva" labelWidth="20%" width="80%" columns="100" rows="3" maxLength="500" property="obAcaoCorretiva" />
		
		<adsm:combobox label="setorCausadorAbatimento"
			   labelWidth="20%"
			   width="80%"
			   property="setorCausadorAbatimento"
			   domain="DM_SETOR_CAUSADOR"
			   boxWidth="150" 
			   required="true" onchange="findComboMotivoDesconto();" />

		<adsm:combobox label="motivoDesconto"
					   labelWidth="20%" 
					   width="80%"	
   					   boxWidth="383"	
   					   onlyActiveValues="true" 
					   optionLabelProperty="dsMotivoDesconto"
					   optionProperty="idMotivoDesconto" 
					   property="motivoDesconto.idMotivoDesconto"
					   required="true">
					   
				   <adsm:propertyMapping criteriaProperty="idDesconto" 
										 modelProperty="desconto.idDesconto"/>
				   <adsm:propertyMapping criteriaProperty="tpSituacao" 
										 modelProperty="tpSituacao"/>				
		</adsm:combobox>
		
		
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.idServico" serializable="true"/>		
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.tpModal" serializable="true"/>
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.tpAbrangencia" serializable="true"/>		
		<adsm:hidden property="devedorDocServFat.doctoServico.idDoctoServico" serializable="true"/>
		<adsm:hidden property="idPendencia" serializable="true"/>
		<adsm:hidden property="idMotivoDesconto" serializable="false"/>			
		
		<script> 
			var labelDocumento = i18NLabel.getLabel('documentoServico');
			var lableValorDocumento = i18NLabel.getLabel('valorDocumento');
		</script>
		
		<adsm:buttonBar>
			<adsm:button caption="historicoAprovacao" id="btnHistoricoAprovacao" disabled="true" 
						 onclick="showHistoricoAprovacao();" />
			<adsm:button caption="salvar" onclick="myStore(this)" disabled="false" buttonType="storeButton" id="storeButton"/>
			<adsm:newButton id="btnNovo"/>
			<adsm:button caption="excluir" id="btnRemover" onclick="myRemove(this)" disabled="true" buttonType="removeButton"/>
		</adsm:buttonBar>
		
	</adsm:form>

</adsm:window>
<script>	

	document.getElementById("tpSituacaoDevedorDocServFatValido").masterLink = "true";
	document.getElementById('flagFretePrincipal').masterLink = 'true';
	document.getElementById("tpSituacao").masterLink = "true";
	getElement("devedorDocServFat.doctoServico.tpDocumentoServico").required = true;
	getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial").required = true

	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor(){

		if( getElementValue('flagFretePrincipal') != 'true' ){
	
			resetValue("devedorDocServFat.idDevedorDocServFat");
			resetValue("devedorDocServFat.doctoServico.nrDoctoServico");
	
			if (getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico") == "") {
				setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",true);
				setDisabled("devedorDocServFat.idDevedorDocServFat",true);	
			} else {
				setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",false);
				habilitaLupa();
				if (getElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial") != ""){
					setDisabled("devedorDocServFat.idDevedorDocServFat",false);		
				}
			}		
			
		}

	}	
	
	/*
		Habilita a lupa de documento de servi�o mesmo com o n�mero do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("devedorDocServFat.idDevedorDocServFat", false);
		setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", true);
	}

	/**
	*	Ao carregar os dados nos devidos campos da tela salva dados do documento de servi�o 
	*   em campos hidden com sufixo OLD para verifica��o de mudan�as no registro
	*	Habilita/desabilita o bot�o Excluir e o bot�o Fluxo de Aprova��o
	*/	
	function myOnDataLoadCallBack_cb(data, erro, errorCode, eventObj){
		onDataLoad_cb(data,erro, errorCode, eventObj);
		setElementValue("idMotivoDesconto", data.motivoDesconto.idMotivoDesconto);
		
		if( getElementValue("idDesconto") != "" ){
			setDisabled("btnRemover",false);
		} else {
			setDisabled("btnRemover",true);
		}
		
		if( getElementValue("idPendencia") != "" ){
			setDisabled("btnHistoricoAprovacao",false);
		}
		
		if (getElementValue('idProcessoWorkflow') != "" || verificaFlagFretePrincipal() ){
		    desabilitaBotoes(true);            
		}

		if (erro == undefined) {
			if (data.blDesabilitar == "true"){
				desabilitaTela(true);
			} else {
				desabilitaTela(false);
			}
		}
		
		findComboMotivoDesconto();

		// seta descri��o na aba de anexos
		setElementValue("descontoDsConcatenado", 
				getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico") + " - " + 
				getElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial") + " - " +
				getElementValue("devedorDocServFat.doctoServico.nrDoctoServico")
				 );

		valorDescontoOnChange();

		if( getElementValue("idPendencia") == "" ){
			setDisabled("btnHistoricoAprovacao",true);
	}
	}
	
	function desabilitaTela(blDesabilita){
		desabilitaBotoes(blDesabilita);
		desabilitaCampos(blDesabilita);
	}
	
	function desabilitaBotoes(blDesabilita){
		setDisabled("btnHistoricoAprovacao",blDesabilita);
	    setDisabled("storeButton",blDesabilita);
	    setDisabled("btnNovo",blDesabilita);
		setDisabled("btnRemover",blDesabilita);
	}

	function desabilitaCampos(blDesabilita){
		setDisabled("vlDesconto",blDesabilita);
	    setDisabled("obDesconto",blDesabilita);
	    setDisabled("motivoDesconto.idMotivoDesconto",blDesabilita);
	    setDisabled("setorCausadorAbatimento",blDesabilita);
	    setDisabled("obAcaoCorretiva",blDesabilita);
	}

	/**
	*	M�todo ativa ao clicar no bot�o Excluir
	*	Chama o m�todo RemoveById passando v�rios par�metros para posteriores verifica��es (permiss�es, etc...)
	*/
	function myRemove(elemento){
	
		documento = elemento.document;

		// TODO: i18n da mensagem
		if (window.confirm(i18NLabel.getLabel('LMS-02067'))) { 
			var dados = new Array();        

	        setNestedBeanPropertyValue(dados, "idDesconto", getElementValue('idDesconto'));
	        setNestedBeanPropertyValue(dados, "tpSituacaoAprovacao", getElementValue('tpSituacaoAprovacao'));
	        setNestedBeanPropertyValue(dados, "idPendencia", getElementValue('idPendencia'));
	        setNestedBeanPropertyValue(dados, "idDoctoServico", getElementValue('devedorDocServFat.doctoServico.idDoctoServico'));
	        setNestedBeanPropertyValue(dados, "idFilial", getElementValue('devedorDocServFat.filial.idFilial'));
         
	        var sdo = createServiceDataObject("lms.contasreceber.manterDescontosAction.removeById",
	                                          "removeById",
	                                          dados);
	        xmit({serviceDataObjects:[sdo]});
		}
		statusKey=false;		
	
	}
	
	/**
	*	M�todo chamado ao iniciar a tela : Basicamente seta o tipo de Situacao da Aprova��o para Em aprova��o ('E')	
	*   quando for newButton_click e removeButton
	*/	
	function initWindow(eventObj){		
    	if( eventObj.name == 'storeButton' || eventObj.name == 'gridRow_click' ){
			desabilitaDoctoServico(true);
		} else {
		    if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq"){
				if (getElementValue("blDesabilitar") == "true"){
					desabilitaTela(true);
				} else {
					desabilitaTela(false);
				}
	    		return;
		    }
		    addServiceDataObject(createServiceDataObject("lms.contasreceber.manterDescontosAction.clearSessionItens",
                    null,
                    null));
    		xmit(false);
		    
			setElementValue("tpSituacaoAprovacao","E");//Em aprova��o
			setDisabled(document.getElementById("tpSituacaoAprovacao"), true);
			
			desabilitaDoctoServico(false);					
			
			desabilitaTodosOsCampos(false);

			onChangeTpDocumentoServico();
			
			verificaFlagFretePrincipal(true);
			
			findComboMotivoDesconto();
			
			setFocusOnFirstFocusableField(document);
			}
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterDescontosAction.findValorMinimoDocumentoDesconto", "setValorMinimo", null)); 
		xmit(false);
		}
		
	function findComboMotivoDesconto(){
		var dados = new Array();        
		setNestedBeanPropertyValue(dados, "tpSituacao", getElementValue("tpSituacao"));
        setNestedBeanPropertyValue(dados, "desconto.idDesconto", getElementValue("idDesconto"));
		        
		_serviceDataObjects = new Array();
		findComboMotivoDescontoDefault(dados); 
		xmit(false);	      
	}
	
	function findComboMotivoDescontoDefault(dados){
		setNestedBeanPropertyValue(dados, "tpSituacao", getElementValue("tpSituacao"));
        setNestedBeanPropertyValue(dados, "desconto.idDesconto", getElementValue("idDesconto"));
        setNestedBeanPropertyValue(dados, "idSetorCausadorAbatimento", getElementValue("setorCausadorAbatimento"));
        

        addServiceDataObject(createServiceDataObject("lms.contasreceber.manterMotivosDescontosAction.findMotivoDescontoByTpMotivoDesconto",
			                                         "retornoMotivoDesconto",
			                                         dados));
	}
	
	/**
	*	Habilita/Desabilita tag Documento de servi�o
	*/
	function desabilitaDoctoServico(desabilitar){
	
		if( desabilitar == true || desabilitar == false ){
	
			setDisabled(getElement('devedorDocServFat.doctoServico.tpDocumentoServico'),desabilitar);			
			
			if( desabilitar == true ){			
				setDisabled(getElement('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial'),desabilitar);
				setDisabled(getElement('devedorDocServFat.idDevedorDocServFat'),desabilitar);
			}
			
		}
	
	}

	/**
	*	M�todo chamado ao carregar a p�gina. Busca os dados para a combo motivos do desconto
	*   Se esta tela for chamada pelo workflow � realizada conjuntamente a busca dos dados da combo de motivos de desconto os 
	*   demais dados referentes ao desconto
	*/
	function myOnPageLoadCallBack_cb(data, erro){
	
		onPageLoad_cb(data,erro);
		
		var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
		if ( idProcessoWorkflow != ""){
			onDataLoad(idProcessoWorkflow);   
		    getTabGroup(this.document).selectTab("cad");   
		    getTabGroup(this.document).getTab("pesq").setDisabled(true);
		}
		
		setElementValue("tpSituacaoAprovacao","E");//Em aprova��o
		setDisabled(document.getElementById("tpSituacaoAprovacao"), true);
        
        
        var dados = new Array();        
        _serviceDataObjects = new Array();
        
		findComboMotivoDescontoDefault(dados);
		
		if( idProcessoWorkflow != undefined && idProcessoWorkflow != '' ){
			dados = new Array();        
			
			setNestedBeanPropertyValue(dados, "idDesconto", idProcessoWorkflow);    
			
	        addServiceDataObject(createServiceDataObject("lms.contasreceber.manterDescontosAction.findById",
	                                                     "retornoFindById",
	                                                     dados));
		}
		
		valorDescontoOnChange();
		
		xmit(false);	      
	}
	
	/**
	*	Fun��o utilizada para setar o valor correto do motivo desconto quando esta tela � chamada pelo workflow
	*   OBS: Este m�todo n�o � utilizado para a combo de motivo desconto a qual est� utilizando o retorno padr�o
	*/
	function retornoMotivoDesconto_cb(data,erro){
		comboboxLoadOptions({e:document.getElementById("motivoDesconto.idMotivoDesconto"), data:data});
		setElementValue('motivoDesconto.idMotivoDesconto',getElementValue('idMotivoDesconto'));
	}
	
	/**
	*	Retorno da busca do desconto pelo id do processo Workflow
	*
	*/
	function retornoFindById_cb(data, error){
	
		if( error != undefined ){
			alert(error);
			return false;		
		}
		
		if ( data != undefined ){								
			fillFormWithFormBeanData(0, data);		
		}
		
		setFocusOnFirstFocusableField(document);
	
	}
	
	/**
	*	M�todo chamado ao clicar no bot�o Salvar. Salva um desconto
	*
	*/	
	function myStore(elemento){
	
		document.getElementById("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial").label = labelDocumento;
		
		if( getElementValue("vlDesconto") != "" && getElementValue("devedorDocServFat.vlDevido") == "" ){
			document.getElementById("devedorDocServFat.vlDevido").label = lableValorDocumento;
			document.getElementById("devedorDocServFat.vlDevido").required = 'true';
		}
	
		storeButtonScript('lms.contasreceber.manterDescontosAction.store', 'myStoreCallBack', elemento.form);
		
	}
	
	/**
	*	M�todo de retorno do store de Desconto. Store_cb padr�o e desabilita o bot�o Fluxo de aprovacao
	*
	*/	
	function myStoreCallBack_cb(data, erro){	
		if( erro == undefined ){
			setElementValue("idMotivoDesconto", data.motivoDesconto.idMotivoDesconto);
		}
		
		store_cb(data, erro);	
		
		if( erro == undefined ){
			if(data.idPendencia && data.idPendencia != null && data.idPendencia != "null"){
			setDisabled("btnHistoricoAprovacao",false);
		}
	}
	}
	
	
	/**
	*	M�todo chamado ao se alterar o documento de servi�o atual
	*	Ativa a busca de dados do documento e verifica��o de permiss�es do usu�rio sobre os mesmos
	*/	
	function doctoServicoOnChange(){
	
		var doctoServico = getElementValue("devedorDocServFat.doctoServico.nrDoctoServico");
	
		if( doctoServico == "" || doctoServico == undefined ){		
			limpaDadosClienteEValores();		
		}	
		
		return devedorDocServFat_doctoServico_nrDoctoServicoOnChangeHandler();
	
	}
	
	/**
	*	Seta os dados do devedor e do documento de servi�o no retorno da lookup
	*
	*/
	function myOnPopupSetValue(data){	
		if (data != undefined) {		

			setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial', data.idFilialOrigem);
			setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial', data.sgFilialOrigem);	
			setElementValue('devedorDocServFat.doctoServico.moeda.siglaSimbolo', data.sgMoeda + " " + data.dsSimbolo);	
			setDisabled("devedorDocServFat.idDevedorDocServFat", false);
			verificaDoctoEmitidoBloqueado(data,"retornoValidateDoctoEmitidoBloqueado");				
		}	
		return verificaDoctoEmitidoBloqueado(data,"setDadosPopUpDoctoServico");						
	}

	/**
	*	Seta dados de retorno da pop up de DoctoServico
	*/
	function setDadosPopUpDoctoServico_cb(data,error){
		if( error != undefined ){	
			alert(error);
			setFocus(getElement('devedorDocServFat.doctoServico.nrDoctoServico'));
			limpaDadosClienteEValores();
			return false;
		}

		format('devedorDocServFat.doctoServico.nrDoctoServico');
		verificaPermissaoDocumentoUsuario('retornoPermissao');
	}
	
	/**
	 * Quando o "N�mero do documento" for informado 
	 */
	function retornoDocumentoServico_cb(dados) {
	
		var retorno = devedorDocServFat_doctoServico_nrDoctoServico_exactMatch_cb(dados);

		if( retorno == true ){
		
			if (dados != undefined && dados.length > 0) {		
			
				if( dados.length == 1 ){
					data = dados[0];
				}

				setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial', data.idFilialOrigem);
				setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial', data.sgFilialOrigem);	
				setElementValue('devedorDocServFat.doctoServico.moeda.siglaSimbolo', data.sgMoeda + " " + data.dsSimbolo);	

				verificaDoctoEmitidoBloqueado(data,"retornoValidateDoctoEmitidoBloqueado");				
			}

		}
	}
	
	/**
	*	Verifica se o documento selecionado est� 'Emitido' ou 'Bloqueado'
	*/
	function verificaDoctoEmitidoBloqueado(dados,retorno){
	
        var sdo = createServiceDataObject("lms.contasreceber.manterDescontosAction.validateDoctoEmitidoBloqueado",
                                          retorno,
                                          dados);
        xmit({serviceDataObjects:[sdo]});	
        
	}
	
	/**
	*	Retorno da valida��o do Situa��o do documento
	*/
	function retornoValidateDoctoEmitidoBloqueado_cb(data,error){
		
		if( error != undefined ){	
			alert(error);
			setFocus(getElement('devedorDocServFat.doctoServico.nrDoctoServico'));	
			limpaDadosClienteEValores();
			return false;
		}
		
		if (data != undefined ) {		
			fillFormWithFormBeanData(0, data);
			verificaPermissaoDocumentoUsuario('retornoPermissao');
		} else {		
			limpaDadosClienteEValores();		
		}
		
	}
	
	/**
     * Verifica se o usu�rio logado possui permiss�es de acesso ao documento de servi�o
     */
	function verificaPermissaoDocumentoUsuario(retorno){
	
		var idDoctoServico = getElementValue("devedorDocServFat.doctoServico.idDoctoServico");
		var idFilial       = getElementValue("devedorDocServFat.filial.idFilial");
         
        var dados = new Array();        

        setNestedBeanPropertyValue(dados, "idDoctoServico", idDoctoServico);
        setNestedBeanPropertyValue(dados, "idFilial", idFilial);
        
        var sdo = createServiceDataObject("lms.contasreceber.manterDescontosAction.validatePermissaoDocumentoUsuario",
                                          retorno,
                                          dados);
        xmit({serviceDataObjects:[sdo]});	
	
	}
	
	/**
	*	Retorno da busca de permiss�es do usuario sobre o documento de servi�o atual
	*	Busca dados do cliente e do valor do documento se o usu�rio possui permiss�es de acesso ao mesmo
	*/	
	function retornoPermissao_cb(dados, erro){
	
		if( erro != undefined ){
			alert(erro);
			desabilitaTodosOsCampos(true);
			setFocus(document.getElementById("devedorDocServFat.doctoServico.nrDoctoServico"));
			getElement("devedorDocServFat.doctoServico.nrDoctoServico").previousValue = '';
			limpaDadosClienteEValores();		
			return false;
		} else {
			desabilitaTodosOsCampos(false);
		}
		
		if( dados._value == 'true' ){		
			buscaDadosClienteEValores();			
		} else {		
			limpaDadosClienteEValores();		
		}		
	
	}

	/**
	*	Habilita/Desabilita todos os campos da tela, e tamb�m o campo doctoServico independentemente dos outros campos
	*/	
	function desabilitaTodosOsCampos(desabilitar){
		
		setDisabled(getElement('motivoDesconto.idMotivoDesconto'),desabilitar);
		setDisabled(getElement('vlDesconto'),desabilitar);
		setDisabled(getElement('obDesconto'),desabilitar);
		
		
	}
	
	/**
     * Busca os dados do respons�vel e o valor do documento
     * 
     */
	function buscaDadosClienteEValores(){

        
        var sdo = createServiceDataObject("lms.contasreceber.manterDescontosAction.findDadosClienteEValores",
                                          "retornoDadosCliente",
                                          {idDevedorDocServFat:getElementValue("devedorDocServFat.idDevedorDocServFat")});
        xmit({serviceDataObjects:[sdo]});	
	
	}
	
	/**
	*	Retorno da busca dos dados do cliente e do valor do documento
	*
	*/	
	function retornoDadosCliente_cb(dados, erro){
	
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById("devedorDocServFat.doctoServico.nrDoctoServico"));
			limpaDadosClienteEValores();
			return false;
		}
		
		if( dados.devedorDocServFat != undefined ){
			fillFormWithFormBeanData(0, dados);
		} else {		
			limpaDadosClienteEValores();		
		}
		
	}
	
	function limpaDadosDevedorDoctoServico(item){
		resetValue('devedorDocServFat.idDevedorDocServFat');					
		resetValue('devedorDocServFat.doctoServico.idDoctoServico');					
		
		if( item != 1 && item != 2 ){
			resetValue('devedorDocServFat.doctoServico.tpDocumentoServico');
		}
		
		resetValue('devedorDocServFat.doctoServico.nrDoctoServico');
		
		if( item != 2 ){
			resetValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial');
			resetValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial');		    
		}
		
		resetValue('devedorDocServFat.filial.idFilial');	
	}
	
	/**
	*	Limpa dados relacionados ao documento de servi�o atual
	*
	*/	
	function limpaDadosClienteEValores(){	
	
		resetValue("devedorDocServFat.cliente.pessoa.nrIdentificacao");						
		resetValue("devedorDocServFat.cliente.pessoa.nmPessoa");						
		resetValue("devedorDocServFat.cliente.idCliente");						
		resetValue("devedorDocServFat.doctoServico.moeda.idMoeda");						
		resetValue("devedorDocServFat.doctoServico.moeda.siglaSimbolo");						
		resetValue("devedorDocServFat.vlDevido");		
		resetValue("devedorDocServFat.doctoServico.servico.tpModal");
		resetValue("devedorDocServFat.doctoServico.servico.tpAbrangencia");
		resetValue("devedorDocServFat.filial.idFilial");
		resetValue("devedorDocServFat.filial.sgFilial");
		resetValue("devedorDocServFat.filial.pessoa.nmFantasia");		
		resetValue("percentualDesconto");
		resetValue("vlDesconto");				
		
	}	
	
	/**
	*	Ap�s a busca padr�o do combo de Tipo de Documento de Servi�o seta a descri��o do tipo no campo hidden tpDocumentoHidden
	*
	*/
	function onChangeTpDocumentoServico() {
	
		if( getElementValue('flagFretePrincipal') != 'true' ){
	
			resetDevedor();
			
			resetValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial');			
			filialOnChange();
			setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"));
			
		}
		
		var comboTpDocumentoServico = document.getElementById("devedorDocServFat.doctoServico.tpDocumentoServico");		
		var indice = comboTpDocumentoServico.selectedIndex;
		
		if( indice != 0 ){
			setElementValue('tpDocumentoHidden', comboTpDocumentoServico.options[comboTpDocumentoServico.selectedIndex].text);
		}

	}	
	
	/**
	*	M�todo chamado ao se alterar o valor do desconto.	
	*   Chama o calculo de representa��o do desconto em porcentagem 
	*/	
	function valorDescontoOnChange(){
	
		var vlDocumento  = getElementValue("devedorDocServFat.vlDevido");
		var vlDesconto   = document.getElementById("vlDesconto");
		
		var valorDoDesconto = getElementValue("vlDesconto"); 
		
		if( valorDoDesconto != undefined && valorDoDesconto != "" && valorDoDesconto <= 0.00 ){
			var array = new Array();
			array.push(vlDesconto.label);
			array.push(0);
			alert(getMessage(erForaLimites, array));
			setFocus(vlDesconto);
			return false
		}
		
		if( valorDoDesconto != "" ){
			if(parseFloat(vlDesconto.value) >= parseFloat(getElementValue("vlMinimoDocumentoDesconto"))  ){
				document.getElementById("obAcaoCorretiva").required = "true"; 
			} else {
				document.getElementById("obAcaoCorretiva").required = "false";
			}
		         
	        var dados = new Array();        
	        
	        setNestedBeanPropertyValue(dados, "vlDocumento", vlDocumento);
	        setNestedBeanPropertyValue(dados, "vlDesconto", valorDoDesconto);
	        
	        var sdo = createServiceDataObject("lms.contasreceber.manterDescontosAction.validateValores",
	                                          "retornoValidateValores",
	                                          dados);
	        xmit({serviceDataObjects:[sdo]});
	        
	    }	        
	
	}
	
	/**
	*	Retorno da busca dos valores em porcentagem
	*
	*/	
	function retornoValidateValores_cb(dados, erro){
	
		if( erro != undefined ){
			alert(erro);
			setFocus("vlDesconto");
			return false;
		} 
		
		if( dados.percentualDesconto != undefined ){
			fillFormWithFormBeanData(0, dados);
			setFocus("obDesconto");		
		} else {		
			setElementValue("percentualDesconto","");		
		}
	
	}
	
	//---------------------	
		
	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
	
		resetDevedor();	
		
		var siglaFilial = getElement('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();		
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('devedorDocServFat_lupa',false);		
		}
			
		return retorno;
		
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
		resetDevedor();
		if (data.length == 1) {			
			setDisabled("devedorDocServFat.idDevedorDocServFat",false);			
			__lookupSetValue({e:getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"), data:data[0]});
 	        setFocus(document.getElementById("devedorDocServFat.doctoServico.nrDoctoServico"));
			return true;
		} else {
			alert(lookup_noRecordFound);			
		}		
	}
	
	function setMaskNrDocumento(element, type){
       var obj;
       obj = getElement(element);
       
       switch(type) {
       case "CRT":
             obj.mask = "000000";
             break;
       case "CTR":
             obj.mask = "00000000";
             break;
       case "NFS":
             obj.mask = "00000000";
             break;
       }
    }
	
	/**
	*	Busca a filial origem e habilita a lookup de documento servi�o
	*
	*/	
	function enableDevedorDocServFatDoctoServico_cb(data) {
	   var r = devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("devedorDocServFat.idDevedorDocServFat", false);
	      setFocus(document.getElementById("devedorDocServFat.doctoServico.nrDoctoServico"));
	   }
	}
	
	/**
	* Fun��o utilizada quando a Manter Descontos � chamada pela tela 
	* SIM\Consultas e Relat�rios\Consultar Localiza��es de Mercadoria, aba Frete, bot�o Descontos
	* COMPORTAMENTO ESPEC�FICO
	*/
	function verificaFlagFretePrincipal(x){
		var retorno = null;
		if( getElementValue('flagFretePrincipal') != undefined && getElementValue('flagFretePrincipal') == 'true' ){
			if( x != undefined && (x == false || x == true) ){
				desabilitaBotoes(x);
			}
			retorno = true;
		} else {
			retorno = false;
		}
		return retorno;
	}
	
	//Se idPendencia for nulo n�o faz as chamadas
	function showHistoricoAprovacao(){
		if (getElementValue('idPendencia') != "") {
				showModalDialog(
						'questionamentoFaturas/consultarHistoricoQuestionamentoFaturas.do?cmd=list&idQuestionamentoFatura=' + getElementValue('idPendencia'),
							window,
							'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
		} else {
			alert("N�o h� registro a apresentar.");
	}
	}

	/*
	 * guarda o valor minimo de desconto para nao precisar preencher a acao corretiva.
	 */
	function setValorMinimo_cb(data, erro){
		if (data == undefined) {
			return;
		}
		if( erro != undefined ){
			alert(erro);
		}
		
		setElementValue("vlMinimoDocumentoDesconto", data._value);
	}
</script>
