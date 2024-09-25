<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterDevedoresDocumentosServicoAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterDevedoresDocumentosServico" id="formListagem" onDataLoadCallBack="myOnDataLoadCallBackFormPrincipal">
	
		<adsm:hidden property="devedorDocServFat.doctoServico.idDoctoServico" serializable="true"/>
		<adsm:hidden property="nrDoctoServico" serializable="true"/>
		<adsm:hidden property="tpDocumentoHidden"/>			
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-36039"/>
			<adsm:include key="LMS-36048"/>			
			<adsm:include key="LMS-36049"/>	
			<adsm:include key="LMS-02067"/>					
		</adsm:i18nLabels>
		
		<adsm:hidden property="tpModal" serializable="false"/>
		<adsm:hidden property="tpAbrangencia" serializable="false"/>

		<adsm:combobox property="devedorDocServFat.doctoServico.tpDocumentoServico"
					   label="documentoServico" 
					   labelWidth="18%" 
					   width="82%"
					   service="lms.contasreceber.manterDevedoresDocumentosServicoAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   defaultValue="CTR"
					   onchange="onChangeTpDocumentoServico();">

			<adsm:lookup dataType="text"
						 property="devedorDocServFat.doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterDescontosAction.findLookupFilial"
						 action="/municipios/manterFiliais"
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 popupLabel="pesquisarFilial"
						 exactMatch="true"
						 onDataLoadCallBack="enableDevedorDocServFatDoctoServico"
						 onchange="filialOnChange();"
						 disabled="false"/>
										 
			<adsm:hidden property="doctoServico.idDoctoServico"/>
			<adsm:hidden property="tpSituacaoDevedorDocServFatValido" value="0" serializable="true"/>
										
			<adsm:lookup dataType="integer"
						 property="devedorDocServFat"
						 idProperty="idDevedorDocServFat" 
						 criteriaProperty="doctoServico.nrDoctoServico"
						 service="lms.contasreceber.manterDevedoresDocumentosServicoAction.findDevedorServDocFat"
						 action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						 cmd="pesq"
						 size="11" 
						 maxLength="10" 
						 serializable="true" 
						 exactMatch="true"
						 disabled="false"
						 required="true"
						 mask="0000000000"
						 popupLabel="pesquisarDocumentoServico"
						 onchange="devedorOnChange();"
						 onDataLoadCallBack="retornoDocumentoServico"
						 onPopupSetValue="retornoPopupDocumentoServico"
						 >
						 <adsm:propertyMapping modelProperty="tpSituacaoDevedorDocServFatValido" criteriaProperty="tpSituacaoDevedorDocServFatValido"/>
						 <adsm:propertyMapping modelProperty="doctoServico.tpDocumentoServico" criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico"/>
						 <adsm:propertyMapping modelProperty="doctoServico.idDoctoServico" criteriaProperty="devedorDocServFat.doctoServico.idDoctoServico"/>
						 <adsm:propertyMapping modelProperty="idDoctoServico" relatedProperty="devedorDocServFat.doctoServico.idDoctoServico"/>
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"/>						 
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="true"/>
						 <adsm:propertyMapping modelProperty="tpModal.value" relatedProperty="tpModal"/>						 						 
						 <adsm:propertyMapping modelProperty="tpAbrangencia.value" relatedProperty="tpAbrangencia"/>						 						 
			</adsm:lookup>			 
		</adsm:combobox>
		 
		<adsm:hidden property="origem" serializable="true" value="P"/><!-- P de Pesquisa ou S de Salvar-->

		<adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="findButton" caption="consultar" onclick="myOnClicDevedorDocServFat()" disabled="false"/>
			<adsm:button id="btnLimpar" caption="limpar" onclick="myLimparOnClick(this)" disabled="false"/>
		</adsm:buttonBar>
		
	</adsm:form>	

	<adsm:grid idProperty="idDevedorDocServFat"			    
	           property="devedorDocServFat" 
	           gridHeight="50" 
	           selectionMode="radio" 
	           showGotoBox="false" 
	           service="lms.contasreceber.manterDevedoresDocumentosServicoAction.findDevedores"
	           rows="5"
	           showTotalPageCount="false" 
	           showPagging="false" 
	           disableMarkAll="true"
	           onDataLoadCallBack="myOnDataLoad"
	           detailFrameName="pesq"
	           onRowClick="returnFalse();"
	           onRowDblClick="returnFalse();"
	           onSelectRow="validaGrid">
	           
		<adsm:gridColumn width=" " title="devedor" property="devedor" />
		<adsm:gridColumn width="200" title="divisao" property="dsDivisaoCliente" />
		
		<adsm:gridColumn width="60" title="valor" property="moeda" dataType="text"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="90" title="" property="valor" dataType="currency"/>
		</adsm:gridColumnGroup>		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="trocar"  id="btnTrocar"  onclick="trocarOnClick()"  disabled="false"/>
			<adsm:button caption="incluir" id="btnIncluir" onclick="incluirOnClick()"/>
			<adsm:button caption="excluir" id="btnExcluir" onclick="myRemove(this)" disabled="false"/>
		</adsm:buttonBar>
	</adsm:grid>

	<adsm:form action="/contasReceber/manterDevedoresDocumentosServico" id="formDetalhamento" idProperty="idDevedorDocServFat" onDataLoadCallBack="myOnDataLoadCallBackFormCad">
	
		<adsm:hidden property="idDoctoServico" 	serializable="true"/>
		<adsm:hidden property="vlDoctoServico" 	serializable="true"/>		
		<adsm:hidden property="operacao" 		serializable="true"/>
		<adsm:hidden property="idFilialOrigem" 	serializable="true"/>
		<adsm:hidden property="tipoDocumento" 	serializable="true"/>
		<adsm:hidden property="rowCount" 		serializable="true"/>
		
		<adsm:hidden property="idDevedorAnterior"/>
		<adsm:textbox label="devedorAnterior" dataType="text" property="nrIdentificacaoDevedorAnteriorFormatado" disabled="true" size="20" maxLength="20" width="80%" labelWidth="20%" serializable="false">
			<adsm:textbox dataType="text" property="nmPessoaDevedorAnterior" disabled="true" size="60" serializable="false"/>
		</adsm:textbox>
		
		<adsm:hidden property="idDivisaoClienteAnterior" serializable="true"/>
		<adsm:textbox dataType="text" property="dsDivisaoClienteAnterior" label="divisaoResponsavelAnterior" width="80%" labelWidth="20%"
			size="60" serializable="false" disabled="true"/>	
		
		<adsm:hidden property="idMoeda" serializable="true"/>
		<adsm:textbox label="valor" dataType="text" size="10" disabled="true" property="siglaSimbolo" labelWidth="20%" width="80%" serializable="false">
			<adsm:textbox property="valor" dataType="currency" size="20" onchange="verificaValor(this)" disabled="true"/>
		</adsm:textbox>	
		
		<adsm:lookup label="novoDevedor" 
				     labelWidth="20%"
					 service="lms.contasreceber.manterDevedoresDocumentosServicoAction.findClienteByLookup" 
					 dataType="text"
					 property="novoDevedor" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 onDataLoadCallBack="novoDevedor"
					 onPopupSetValue="novoDevedorPopUpSetValue"
					 onchange="return novoDevedorOnChange();"
					 exactMatch="false" 
					 size="20"
					 maxLength="20" 
					 disabled="true"
					 width="85%" 
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="nomeNovoDevedor" />
			<adsm:textbox dataType="text" property="nomeNovoDevedor" disabled="true" size="73" />
	    </adsm:lookup>
	    
	    <adsm:hidden property="descricaoDivisaoCliente"/>	
		<adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisaoNovoResponsavel" 
					   autoLoad="false"
					   disabled="true"
        			   service="lms.contasreceber.manterDevedoresDocumentosServicoAction.findComboDivisaoCliente" 
					   optionLabelProperty="dsDivisaoCliente"				
					   width="80%" 
					   labelWidth="20%"
					   boxWidth="150"
					   optionProperty="idDivisaoCliente">
					   
					  <adsm:propertyMapping 
							relatedProperty="descricaoDivisaoCliente" 
							modelProperty="dsDivisaoCliente"/>
		</adsm:combobox>

		<adsm:combobox label="motivoTransferencia"
					   labelWidth="20%"
	 				   width="80%"
	 				   disabled="true"
					   property="motivoTransferencia.idMotivoTransferencia" 
					   optionProperty="idMotivoTransferencia"
					   optionLabelProperty="dsMotivoTransferencia" 
					   service="lms.contasreceber.manterDevedoresDocumentosServicoAction.findMotivosTransferenciaByCombo" 
					   boxWidth="531"/>		   
			
		<adsm:textarea label="observacao" 
					   labelWidth="20%"
					   width="80%" 
					   columns="102" 
					   disabled="true"
					   rows="2" 
					   maxLength="500"
					   property="observacaoRecebimento"/>
					   
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="btnSalvar" disabled="true" onclick="myStore(this)" buttonType="storeButton"/>
		</adsm:buttonBar>
		
		<script>
			
			var lms36039 = i18NLabel.getLabel('LMS-36039');
			var lms36048 = i18NLabel.getLabel('LMS-36048');
			var lms36049 = i18NLabel.getLabel('LMS-36049');
			
		</script>
	
	</adsm:form>

</adsm:window>
<script src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script>
	document.getElementById("tpSituacaoDevedorDocServFatValido").masterLink = "true";
	setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", true);
	var posSalvar = false;
	
	var operacao = null;
	var TROCAR  = 0;
	var INCLUIR = 1;
	var EXCLUIR = 2;
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		setDisabled("btnSalvar", true);
	}
	
	/** OnClick do botão consultar */
	function myOnClicDevedorDocServFat(){
		limpaCamposForm2();
		findButtonScript("devedorDocServFat", document.forms[0]);
		setDisabled("novoDevedor.idCliente", true);
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		setDisabled("motivoTransferencia.idMotivoTransferencia", true);
		setDisabled("observacaoRecebimento", true);		
	}
	
	/*
	 * On Change da lookup de Filial 
	 */
	function resetDevedor(){
	
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
		
		// Desabilita os campos do novo responsável
		setDisabled("novoDevedor.idCliente", true);
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		setDisabled("motivoTransferencia.idMotivoTransferencia", true);
		setDisabled("observacaoRecebimento", true);

	}	
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("devedorDocServFat.idDevedorDocServFat", false);
		setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", true);
	}

	function initWindow(evento){	
		setDisabled("btnLimpar",false);
		
		if( evento.name != 'removeButton_grid' ){
			setFocusOnFirstFocusableField(document);
		}
		
	}

	/**
	*	Após a busca padrão do combo de Tipo de Documento de Serviço seta a descrição do tipo no campo hidden tpDocumentoHidden
	*
	*/
	function onChangeTpDocumentoServico() {
	
		var comboTpDocumentoServico = document.getElementById("devedorDocServFat.doctoServico.tpDocumentoServico");
		setElementValue('tpDocumentoHidden', comboTpDocumentoServico.options[comboTpDocumentoServico.selectedIndex].text);
		limpaValoresEPesquisa();
		
		resetValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");
		resetValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial");
		
		resetDevedor();					
		
		setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"))	

	}
	
	/**
	*	Limpa os dados da grid e do form2
	*/
	function limpaValoresEPesquisa(){
		devedorDocServFatGridDef.resetGrid();
		limpaCamposForm2();
		setDisabled('btnTrocar',true);
		setDisabled('btnIncluir',true);
		setDisabled('btnExcluir',true);				
		setDisabled('btnLimpar',false);				
		document.forms[2].document.getElementById('valor').disabled = 'true';	
	}
	
   /**
	*	Busca a filial origem e habilita a lookup de documento serviço
	*
	*/	
	function enableDevedorDocServFatDoctoServico_cb(data) {
	
	   var r = devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);

	   limpaValoresEPesquisa();	   
	   
	   resetDevedor();
	   
	   if (r == true) {
	      setFocus(document.getElementById("devedorDocServFat.doctoServico.nrDoctoServico"));
	   } else {
		  setFocus(document.getElementById("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial"));
	   }
	   
	}
	 
	function devedorOnChange(){
		limpaValoresEPesquisa();
		return devedorDocServFat_doctoServico_nrDoctoServicoOnChangeHandler();
	}
	
	function filialOnChange() {
	
		resetDevedor();	
		limpaValoresEPesquisa();
		
		var siglaFilial = getElement('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();		
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('devedorDocServFat_lupa',false);		
		}
		
		if( (siglaAnterior == siglaFilial.value) && retorno == false ){
			getElement('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial').previousValue = '';
		}
			
		return retorno;
	}
	
	
	/**
	*	Regra 2.4 - Botão Excluir deve estar habilitado somente quando o tipo de documento de serviço for 'CRT'
	*				e houverem mais de 1 registro de devedores para o documento em questão
	*/
	function verificaQuantidadeDeDevedores(){
	
		if( getElementValue('devedorDocServFat.doctoServico.idDoctoServico') != "" ){
		
			var dados = new Array();
	
	        setNestedBeanPropertyValue(dados, "devedorDocServFat.doctoServico.idDoctoServico", getElementValue('devedorDocServFat.doctoServico.idDoctoServico'));
	        setNestedBeanPropertyValue(dados, "doctoServico.tpDocumentoServico", getElementValue('devedorDocServFat.doctoServico.tpDocumentoServico'));
	        setNestedBeanPropertyValue(dados, "devedorDocServFat.filialByIdFilialOrigem.idFilial", getElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial'));        
	         
	        var sdo = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.getRowCount",
	                                          "callBackVerificaQuantidadeDevedores",
	                                          dados);
	        xmit({serviceDataObjects:[sdo]});
	        
		}else{
			validateTabScript(this.document.forms[0]);
		}
		
	}
	
	/**
	*	Regra 2.4 - Retorno
	*
	*/
	function callBackVerificaQuantidadeDevedores_cb(data,erro){
	
		var rows = devedorDocServFatGridDef.gridState.data.length;
		setDisabled('btnTrocar', (rows < 0));
	
		posSalvar = false;
	
		var tpDocumento    = document.forms[0].document.getElementById("tpDocumentoHidden").value;
	
		if( data != undefined ){ 
		
			if( data._value > 1 && tpDocumento == 'CRT' ){
				document.forms[2].document.getElementById('btnExcluir').disabled = false;
			} else {
				document.forms[2].document.getElementById('btnExcluir').disabled = true;
			}
		
		} else {
			document.forms[2].document.getElementById('btnExcluir').disabled = true;
		}
		
		if( !getElementValue('origem') == 'P'){
			document.getElementById("btnLimpar").focus();
		}
		
		if( getElementValue('devedorDocServFat.doctoServico.tpDocumentoServico') == 'CRT' && rows == 1 ){
			setDisabled('btnIncluir', false);
		} else {
			setDisabled('btnIncluir', true);
		}
	
	}
	
	/**
	*	Regra 2.2
	*	Se o tipo de documento for CRT colocar o foco no campo VALOR caso contrário no campo Novo Devedor
	*/
	function trocarOnClick(){
	
		operacao = TROCAR;
		
		var dados = new Array();
		
		var ids = devedorDocServFatGridDef.getSelectedIds();
		
		if( ids == null || ids == undefined || ids.ids == '' ){
			return false;
		}
		
        setNestedBeanPropertyValue(dados, "idDevedorDocServFat", ids.ids[0]);
         
        var sdo = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.findById",
                                          "myOnDataLoadCallBackFormCad",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
        
		setDisabled("novoDevedor.idCliente", false);
		//setDisabled("divisaoCliente.idDivisaoCliente", false);
		setDisabled("motivoTransferencia.idMotivoTransferencia", false);
		setDisabled("observacaoRecebimento", false);
	}
	
	/**
	*	Método de retorno da carga dos dados na form 2
	*
	*/
	function myOnDataLoadCallBackFormCad_cb(data,erro){
	
		onDataLoad_cb(data,erro);
		
		var nrDevedores = devedorDocServFatGridDef.gridState["rowCount"];
		
		fillFormWithFormBeanData(2, data);
		document.forms[0].document.getElementById('btnTrocar').disabled = false;
		
		var tpDocumento = document.getElementById('devedorDocServFat.doctoServico.tpDocumentoServico').options[document.getElementById('devedorDocServFat.doctoServico.tpDocumentoServico').selectedIndex].value;
	
		if( tpDocumento == 'CRT' && nrDevedores != 1 ){
			setFocus(document.forms[2].document.getElementById('valor'),'true');
		} else {
			if( nrDevedores == 1 ){
				setDisabled('valor','true');
			}			
			document.forms[2].document.getElementById("novoDevedor.pessoa.nrIdentificacao").focus();
		}
		
	}
	
	/**
	*	Regra 2.3
	*	Ao clicar posicinar no campo valor (habilitado somente para documentos CRT)
	*/
	function incluirOnClick(){
	
		limpaCamposForm2();
		var tpDocumento    = document.forms[0].document.getElementById("tpDocumentoHidden").value;
		
		var nrDevedores = devedorDocServFatGridDef.gridState["rowCount"];
		
		if( tpDocumento == 'CRT' && (nrDevedores <= 2 && nrDevedores >= 0) ){
		
			if( nrDevedores == 2 ){
				alert(lms36049);
				return false;
			} else if( nrDevedores == 1 ){
			
				if( devedorDocServFatGridDef.gridState.data.length > 0 ){
					setElementValue('siglaSimbolo',devedorDocServFatGridDef.gridState.data[0].moeda);
					setElementValue('idMoeda',devedorDocServFatGridDef.gridState.data[0].idMoeda);			
				}
		
				setDisabled('valor',false);
				document.forms[2].document.getElementById("valor").focus();		
				document.forms[2].document.getElementById('btnSalvar').disabled = false;
				operacao = INCLUIR;
			} else {
				return false;
			}
			
		} else {
			
			alert(lms36039);
			return false;
		
		}
		
	}
	
	/**
	*	Regra 2.5
	*	Valor não deve ser maior do que o valor do documento de serviço
	*/
	function verificaValor(elemento){
		
		var idDoctoServico  = document.forms[0].document.getElementById('devedorDocServFat.doctoServico.idDoctoServico').value;
		var valor      		= getElementValue('valor');
		
		var dados = new Array();
		
        setNestedBeanPropertyValue(dados, "valor", valor);
        setNestedBeanPropertyValue(dados, "idDoctoServico", idDoctoServico);        
         
        var sdo = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.validateValor",
                                          "validacaoValor",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
		
	}	
	
	/**
	*	Regra 2.5 Retorno
	*	Apresenta a mensagem LMS-36024 caso o valor do campo VALOR for maior que o valor do documento de serviço
	*/	
	function validacaoValor_cb(data,erro){
	
		if( erro != undefined ){
			alert(erro);
			setFocus(document.forms[2].document.getElementById('valor'));
			return false;
		}
	
	}
	
	/**
	*	Método que salva um registro de Devedor
	*/	
	function myStore(elemento){
	
		setElementValue('origem','S');
	
		if( operacao == TROCAR || operacao == INCLUIR ){
			document.forms[2].document.getElementById('valor').required = 'true';			
			document.forms[2].document.getElementById('novoDevedor.idCliente').required = 'true';			
			document.forms[2].document.getElementById('divisaoCliente.idDivisaoCliente').required = 'true';			
			document.forms[2].document.getElementById('motivoTransferencia.idMotivoTransferencia').required = 'true';			

			
			
			var valor = document.forms[2].document.getElementById('valor');
			var novoDevedor = document.forms[2].document.getElementById('novoDevedor.idCliente');
			var numeroNovoDevedor = document.forms[2].document.getElementById("novoDevedor.pessoa.nrIdentificacao");
			var novaDivisaoCliente = document.forms[2].document.getElementById("divisaoCliente.idDivisaoCliente");
			var motivoTransferencia = document.forms[2].document.getElementById('motivoTransferencia.idMotivoTransferencia');			
			
			if( valor.value == '' ){
				alert(getMessage(erRequired, new Array(valor.label)));
				setFocus(valor);
				return false;
			}
			
			if( novoDevedor.value == '' ){
				alert(getMessage(erRequired, new Array(novoDevedor.label)));
				setFocus(numeroNovoDevedor);
				return false;
			}
			
			if( novaDivisaoCliente.value == '' && getElement("divisaoCliente.idDivisaoCliente").options.length > 1){
				alert(getMessage(erRequired, new Array(novaDivisaoCliente.label)));
				setFocus(novaDivisaoCliente);
				return false;
			}
			
			if( motivoTransferencia.value == '' ){
				alert(getMessage(erRequired, new Array(motivoTransferencia.label)));
				setFocus(motivoTransferencia);
				return false;
			}
	
		}
		
		document.forms[2].document.getElementById('operacao').value = operacao;
		document.forms[2].document.getElementById('rowCount').value = devedorDocServFatGridDef.gridState["rowCount"];
		
		var	idDoctoServico      = getElementValue('idDoctoServico');
		var idFilialOrigem      = getElementValue('idFilialOrigem');
		var idDevedorDocServFat = getElementValue('idDevedorDocServFat');
		var valor				= getElementValue('valor');
		var idNovoDevedor		= getElementValue('novoDevedor.idCliente');
		var tipoDocumento       = getElementValue('tipoDocumento');
		
		if( idDoctoServico == null || idDoctoServico == '' ){
			setElementValue('idDoctoServico',document.forms[0].document.getElementById('devedorDocServFat.doctoServico.idDoctoServico').value);
		}
		
		if( idFilialOrigem == null || idFilialOrigem == '' ){
			setElementValue('idFilialOrigem',document.forms[0].document.getElementById('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial').value);
		}		
		
		if( tipoDocumento == null || tipoDocumento == '' ){
			setElementValue('tipoDocumento',document.forms[0].document.getElementById('devedorDocServFat.doctoServico.tpDocumentoServico').value);
		}
	
		storeButtonScript('lms.contasreceber.manterDevedoresDocumentosServicoAction.store', 'myStoreCallBack', elemento.form);	
	
	}
	
	/**
	*	Retorno da store.
	*	Após o store do devedor a lista de devedores deve ser atualizada
	*/
	function myStoreCallBack_cb(data, errorMsg, errorKey, showErrorAlert, eventObj){
	
		posSalvar = true;
		
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
		
		var dados = new Array();

        setNestedBeanPropertyValue(dados, "devedorDocServFat.doctoServico.idDoctoServico", getElementValue('devedorDocServFat.doctoServico.idDoctoServico'));
        setNestedBeanPropertyValue(dados, "devedorDocServFat.doctoServico.tpDocumentoServico", getElementValue('devedorDocServFat.doctoServico.tpDocumentoServico'));
        setNestedBeanPropertyValue(dados, "devedorDocServFat.filialByIdFilialOrigem.idFilial", getElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial'));        
        setNestedBeanPropertyValue(dados, "buscaSimples", true);                
		
		devedorDocServFatGridDef.executeSearch(dados);		
		limpaCamposForm2();		
		setDisabled('valor',false);
		setDisabled("novoDevedor.idCliente", true);
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		setDisabled("motivoTransferencia.idMotivoTransferencia", true);
		setDisabled("observacaoRecebimento", true);		
		
	}
	
	function validateTab(){
		return validateTabScript(document.forms[0]);
	}
	
	/**
	*	Método ativa ao clicar no botão Excluir
	*	Chama o método RemoveById passando vários parâmetros para posteriores verificações (permissões, etc...)
	*/
	function myRemove(elemento){
	
		var tpDocumento = document.getElementById('devedorDocServFat.doctoServico.tpDocumentoServico').options[document.getElementById('devedorDocServFat.doctoServico.tpDocumentoServico').selectedIndex].value;
		var nrDevedores = devedorDocServFatGridDef.gridState["rowCount"];
		
		//Regra 2.4
		if( tpDocumento != 'CRT' ){
			alert(lms36039);
			return false;
		}

		//Regra 2.4		
		if( nrDevedores == 1 ){
			alert(lms36048);
			return false;
		}
	
		documento = elemento.document;
		
		var ids = devedorDocServFatGridDef.getSelectedIds();
		
		var idDevedor = ids.ids[0];
		
		if( nrDevedores > 1 && (idDevedor != null && idDevedor != '') ){

			if (window.confirm(i18NLabel.getLabel('LMS-02067'))) { 
				
				var dados = new Array();        
	
		        setNestedBeanPropertyValue(dados, "idDevedorDocServFat", idDevedor);
		        
		        var sdo = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.removeById",
		                                         "devedorDocServFatGridDef#removeByIds",
		                                          dados);
		        xmit({serviceDataObjects:[sdo]});
		        
			}
			
		} 
	
	}	
	
	/**
	*	Método ativado ao clicar no botão Incluir
	*
	*/
	function limpaCamposForm2(){
		
		resetValue('idDoctoServico');
		resetValue('vlDoctoServico');
		resetValue('operacao');
		resetValue('idFilialOrigem');
		resetValue('tipoDocumento');				
		resetValue('rowCount');
		resetComboDivisao();
		resetValue('dsDivisaoClienteAnterior');
		resetValue('idDivisaoClienteAnterior');
		resetValue('idDevedorAnterior');				
		resetValue('nrIdentificacaoDevedorAnteriorFormatado');
		resetValue('nmPessoaDevedorAnterior');
		resetValue('siglaSimbolo');
		resetValue('idMoeda');
		resetValue('valor');
		resetValue('novoDevedor.idCliente');								
		resetValue('novoDevedor.pessoa.nrIdentificacao');
		resetValue('nomeNovoDevedor');								
		resetValue('motivoTransferencia.idMotivoTransferencia');
		resetValue('observacaoRecebimento');								

		setDisabled('btnSalvar',true);
								
	}
	
	function returnFalse(){
		return false;
	}
	
	function myOnDataLoad_cb(data, error) {
		onDataLoad_cb(data[0], error);		
		document.forms[2].document.getElementById('btnSalvar').disabled = true; 
		var rows = devedorDocServFatGridDef.gridState.data.length;
		
		if( getElementValue('devedorDocServFat.doctoServico.tpDocumentoServico') == 'CRT' && rows == 1 ){
			setDisabled('btnIncluir', false);
		} else {
			setDisabled('btnIncluir', true);
		}
	}
	
	function validaGrid(id) {
		verificaQuantidadeDeDevedores();
	}
	
	function myLimparOnClick(elemento){
		cleanButtonScript(elemento.document);
		resetDevedor();
	    limpaValoresEPesquisa();
	    setDisabled('btnExcluir',true);
	    posSalvar = false;
	}
	
	function myOnDataLoadCallBackFormPrincipal(data,erro){
		onDataLoad_cb(data,erro);
		setElementValue('origem','S');
	}
	
	function verificaTipoDocumentoSelecionado(data){
		var idDoctoServico = getElementValue("devedorDocServFat.doctoServico.idDoctoServico");
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.validateMonitoramentoEletronicoAutorizado", "verificaTipoDocumentoSelecionado", {idDoctoServico:idDoctoServico, data:data});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
		
		return false;
	}
	
	function verificaTipoDocumentoSelecionado_cb(data, error){
		if (error) {
			alert(error);
			resetValue('doctoServico.idDoctoServico');
			resetValue('devedorDocServFat.doctoServico.idDoctoServico');
			resetValue('devedorDocServFat.doctoServico.nrDoctoServico');
		} else {
			var idDoctoServico = getElementValue("devedorDocServFat.doctoServico.idDoctoServico");
			var tpDocumento    = document.forms[0].document.getElementById("tpDocumentoHidden").value;
		
			if (data != undefined && data.length > 0) {			
			
				setElementValue('devedorDocServFat.doctoServico.idDoctoServico', idDoctoServico);		
				setElementValue('nrDoctoServico', data[0].nrDoctoServico);
	
				if( tpDocumento == 'CRT' ){
					//Regra  2.5
					document.forms[2].document.getElementById("valor").disabled = false;
					
				} else {
				
					//Regra 2.5
					document.forms[2].document.getElementById("valor").disabled = true;
				}
				
			} else {
			
				limpaValoresEPesquisa();	
			
				if( idDoctoServico == '' ){
					setFocus(document.getElementById("devedorDocServFat.doctoServico.nrDoctoServico"));
				}
				//Regra 2.5
				document.forms[2].document.getElementById("valor").disabled = true;
							
			}
		}
	}
	
	/**
	 * Quando o "Número do documento" for informado 
	 */
	function retornoDocumentoServico_cb(data) {
	
		var retorno = devedorDocServFat_doctoServico_nrDoctoServico_exactMatch_cb(data);
		verificaTipoDocumentoSelecionado(data);		

	}
	
	function retornoPopupDocumentoServico(data){
		if (data!=undefined){
			setElementValue('devedorDocServFat.doctoServico.tpDocumentoServico',data.tpDocumentoServico.value);
			setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial',data.sgFilialOrigem);
			setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial',data.idFilialOrigem);			
			setElementValue("devedorDocServFat.doctoServico.idDoctoServico",data.idDoctoServico);
			setElementValue("devedorDocServFat.doctoServico.nrDoctoServico",data.nrDoctoServico);  			                 
			setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", false);					
		}		
		var obj = new Array();
		obj.push(data);
		verificaTipoDocumentoSelecionado(obj);
		return true;
	}
	
	/** Busca as divisões do cliente */
	function findComboDivisaoCliente(idCliente){
	
		var data = new Array();	   
		
		setNestedBeanPropertyValue(data, "idCliente", idCliente);
		setNestedBeanPropertyValue(data, "descricaoDivisaoCliente", "A");
		setNestedBeanPropertyValue(data, "tpModal", document.forms[0].document.getElementById("tpModal").value);
		setNestedBeanPropertyValue(data, "tpAbrangencia", document.forms[0].document.getElementById("tpAbrangencia").value);
		
		var sdo = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.findComboDivisaoCliente",
			"findComboDivisaoCliente", data);
		xmit({serviceDataObjects:[sdo]});

	}
	
	/** 
	  * CallBack da função findComboDivisaoCliente 
	  */
	function findComboDivisaoCliente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		divisaoCliente_idDivisaoCliente_cb(data);
		
		if(getElement("divisaoCliente.idDivisaoCliente").options.length > 1){
			document.forms[2].document.getElementById("divisaoCliente.idDivisaoCliente").required = "true";
			setDisabled("divisaoCliente.idDivisaoCliente", false);
			
			if(getElement("divisaoCliente.idDivisaoCliente").options.length == 2){
				getElement("divisaoCliente.idDivisaoCliente").options[1].selected = true;
			} else {
				getElement("divisaoCliente.idDivisaoCliente").focus();
			}			
		}else{
			resetComboDivisao();
		}
	}
	
	// CallBack da lookup de novo cliente
	function novoDevedor_cb(data, error){
		
		if(error != undefined)
			alert(error);
		
		if(novoDevedor_pessoa_nrIdentificacao_exactMatch_cb(data)){
			findComboDivisaoCliente(getElementValue("novoDevedor.idCliente"));
		}
	}
	
	// PopUpSetValue da lookup novo responsavel
	function novoDevedorPopUpSetValue(data, error){
		findComboDivisaoCliente(data.idCliente);
	}
	
	// OnChange da lookup de novoDevedor
	function novoDevedorOnChange(){
		var retorno = novoDevedor_pessoa_nrIdentificacaoOnChangeHandler();	
		
		if(getElementValue("novoDevedor.pessoa.nrIdentificacao") == "" && getElementValue("nomeNovoDevedor") == "")
			resetComboDivisao();
			
		return retorno;	
	}
	
	// Reseta a combo de divisao
	function resetComboDivisao(){
		
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		getElement("divisaoCliente.idDivisaoCliente").required = "false";
		getElement("divisaoCliente.idDivisaoCliente").length = 1;
		getElement("divisaoCliente.idDivisaoCliente").selecttedIndex = 0;
		
	}
	
</script>