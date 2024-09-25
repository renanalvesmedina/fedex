<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterDescontosAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/manterDescontos" onDataLoadCallBack="myOnDataLoadCallBack">
	
		<adsm:hidden property="flagFretePrincipal" serializable="false"/>
	
		<adsm:lookup label="filialCobranca" labelWidth="20%" width="80%"
		             property="filialCobranca"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.contasreceber.manterDescontosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             required="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="filialCobranca.pessoa.nmFantasia" 
        						  modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filialCobranca.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
	
		<adsm:combobox property="devedorDocServFat.doctoServico.tpDocumentoServico"
					   label="documentoServico" 
					   labelWidth="20%" 
					   width="80%"
					   service="lms.contasreceber.manterDescontosAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return onChangeTpDocumentoServico();"> 			

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
						 onchange="return filialOnChange();"
						 disabled="true"/>		
										 
			<adsm:lookup dataType="integer"
						 property="devedorDocServFat"
						 idProperty="idDevedorDocServFat" 
						 criteriaProperty="doctoServico.nrDoctoServico"
						 service="lms.contasreceber.manterDescontosAction.findDevedorServDocFat"
						 action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						 cmd="pesq"
						 size="11" 
						 maxLength="10" 
						 serializable="true" 
						 exactMatch="true"
						 disabled="true"
						 mask="0000000000"
						 onchange="return doctoServicoOnChange()"
						 onDataLoadCallBack="retornoDocumentoServico"
						 onPopupSetValue="myOnPopupSetValue"
						 popupLabel="pesquisarDocumentoServico">
						 <adsm:propertyMapping modelProperty="doctoServico.tpDocumentoServico" criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico"/>
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"/>						 
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="true"/>						 						 
			</adsm:lookup>			 
		</adsm:combobox>
		
		<adsm:hidden property="tpDocumentoServicoHidden"  serializable="true"/>	
		<adsm:hidden property="tpDocumentoHidden" />
		<adsm:lookup label="clienteResponsavel"
				     labelWidth="20%" 
					 width="80%"
					 service="lms.contasreceber.manterDescontosAction.findLookupClienteResponsavel" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="devedorDocServFat.cliente.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="devedorDocServFat.cliente.pessoa.nmPessoa" disabled="true" size="76"/>	
   		</adsm:lookup>
		
		<adsm:combobox label="situacaoAprovacao"					   
					   labelWidth="20%" 
					   width="80%"					   
					   property="situacaoAprovacao"
					   domain="DM_STATUS_WORKFLOW"
					   boxWidth="150" />
					   
		<adsm:combobox label="setorCausadorAbatimento"
					   labelWidth="20%"
					   width="80%"
					   property="setorCausadorAbatimento"
					   domain="DM_SETOR_CAUSADOR"
					   boxWidth="150" onchange="onChangeSetorCausadorAbatimento();" />
					   
		<adsm:combobox label="motivoDesconto"
					   labelWidth="20%" 
					   width="80%"		
					   boxWidth="383"
					   service="lms.contasreceber.manterDescontosAction.findComboMotivoDesconto" 
					   optionLabelProperty="dsMotivoDesconto"
					   optionProperty="idMotivoDesconto" 
					   property="motivoDesconto.idMotivoDesconto"/>					   
	   
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="desconto"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idDesconto" property="desconto" gridHeight="200" rows="10" 
	           onSelectRow="verificaFlagFretePrincipal"
	           onSelectAll="verificaFlagFretePrincipal">
	
		<adsm:gridColumn width=" " title="clienteResponsavel" property="clienteResponsavel"/>
		
		<adsm:gridColumn width="35" title="documentoServico" property="tpDocumentoServico"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="" property="sgFilialOrigem"/>	
			<adsm:gridColumn width="130" title="" property="nrDocumentoServico" />
		</adsm:gridColumnGroup>		
		
		<adsm:gridColumn width="180"  title="motivo" 			property="motivo"/>
		<adsm:gridColumn width="180" title="situacaoAprovacao" 	property="tpSituacaoAprovacao"/>
		
		<adsm:buttonBar>
			<adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>
	</adsm:grid>	
	
</adsm:window>
<script src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script>

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
	}	
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("devedorDocServFat.idDevedorDocServFat", false);
		setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", true);
	}

	function filialOnChange() {
	
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
	
	function limpaDadosDevedorDoctoServico(item){
		resetValue('devedorDocServFat.idDevedorDocServFat');									
		
		if( item != 1 && item != 2 ){
			resetValue('devedorDocServFat.doctoServico.tpDocumentoServico');
		}
		
		resetValue('devedorDocServFat.doctoServico.nrDoctoServico');
		
		if( item != 2 ){
			resetValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial');
			resetValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial');		    
		}

	}
	
	/**
	*	Seta os dados do devedor e do documento de serviço no retorno da lookup
	*
	*/
	function myOnPopupSetValue(data){
	
		//limpaDadosDevedorDoctoServico(0);
		
		if( data.idDevedorDocServFat != null ){
			setElementValue('devedorDocServFat.doctoServico.tpDocumentoServico',              data.tpDocumentoServico.value);
			setElementValue('devedorDocServFat.doctoServico.nrDoctoServico',              	  data.nrDoctoServico);
			setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial', data.idFilialOrigem);
			setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial', data.sgFilialOrigem);		    
			setDisabled('devedorDocServFat.idDevedorDocServFat', false);
		} else {
			habilitaLupa();
		}
	
	}
	
	/**
	 * Quando o "Número do documento" for informado 
	 */
	function retornoDocumentoServico_cb(dados) {
	
		var retorno = devedorDocServFat_doctoServico_nrDoctoServico_exactMatch_cb(dados);
		
		if( retorno == true ){

			if (dados != undefined && dados.length > 0) {		
			
				if( dados.length == 1 ){
					data = dados[0];
				}

				setElementValue('devedorDocServFat.doctoServico.tpDocumentoServico',              data.tpDocumentoServico.value);
				format('devedorDocServFat.doctoServico.nrDoctoServico');
				setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial', data.idFilialOrigem);
				setElementValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial', data.sgFilialOrigem);
			} 		
		}
	}
	
	
	/**
	*	Após a busca padrão do combo de Tipo de Documento de Serviço seta a descrição do tipo no campo hidden tpDocumentoHidden
	*
	*/
	function onChangeTpDocumentoServico() {
	
		resetDevedor();
		
		resetValue('devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial');	
		filialOnChange();
		setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"));

		var comboTpDocumentoServico = document.getElementById("devedorDocServFat.doctoServico.tpDocumentoServico");		
		var indice = comboTpDocumentoServico.selectedIndex;
		
		if( indice != 0 ){
		
			setElementValue('tpDocumentoHidden', comboTpDocumentoServico.options[comboTpDocumentoServico.selectedIndex].text);
			
		}

	}			
	
	function myOnPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data,erro);
		
		var veioDeOutraTela = document.getElementById("devedorDocServFat.doctoServico.tpDocumentoServico").masterLink;
		
		if( veioDeOutraTela == undefined || veioDeOutraTela == "false"){
			buscaFilialUsuario();
			onChangeTpDocumentoServico();
		} else {
			verificaFlagFretePrincipal();
		}
		var url = new URL(parent.location.href);
		console.log(url);
		console.log(url.parameters);
		console.log(url.parameters["tpDoctoServico"]);
		if (url.parameters["tpDoctoServico"]) {
			setElementValue("devedorDocServFat.doctoServico.tpDocumentoServico", url.parameters["tpDoctoServico"]);
			setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial", url.parameters["sgFilial"]);
			setElementValue("devedorDocServFat.doctoServico.nrDoctoServico", url.parameters["nrDoctoServico"]);
			setDisabled('btnExcluir',true);
		}
		
	}
	
	/**
	* Função utilizada quando a Manter Descontos é chamada pela tela 
	* SIM\Consultas e Relatórios\Consultar Localizações de Mercadoria, aba Frete, botão Descontos
	* COMPORTAMENTO ESPECÍFICO
	*/
	function verificaFlagFretePrincipal(){
		if( getElementValue('flagFretePrincipal') != undefined && getElementValue('flagFretePrincipal') == 'true' ){
			setDisabled('btnExcluir',true);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	*	Busca a filial do usuário logado
	*/
	function buscaFilialUsuario(){
		
		var dados = new Array();
        
        var sdo = createServiceDataObject("lms.contasreceber.manterDescontosAction.findFilialUsuario",
                                          "retornoFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
        		
	}
	
	/**
	*	Retorno da busca da filial do usuário.
	*   Seta a filial da seção na lookup de filial
	*/
	function retornoFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(getElement('filialCobranca.sgFilial'));
			return false;
		}
		
		fillFormWithFormBeanData(0, data);				
		setFocusOnFirstFocusableField(document);			
		
	}
	
	function myOnDataLoadCallBack_cb(data,erro){
		onDataLoad_cb(data,erro);
		verificaFlagFretePrincipal();
	}
	
	/**
	*	Método chamado ao se alterar o documento de serviço atual
	*	Ativa a busca de dados do documento e verificação de permissões do usuário sobre os mesmos
	*/	
	function doctoServicoOnChange(){
		return devedorDocServFat_doctoServico_nrDoctoServicoOnChangeHandler();		       	
	}
	
	function initWindow(eventObj){
		buscaFilialUsuario();
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
	/**
	* Onchange do combo tpSetorCausadorAbatimento
	*/
	function onChangeSetorCausadorAbatimento(){
		var idSetorCausadorAbatimento = getElementValue("setorCausadorAbatimento");
		findMotivoDescontoByTpMotivoDesconto(idSetorCausadorAbatimento)
	}
	
	function findMotivoDescontoByTpMotivoDesconto(idSetorCausadorAbatimento){
		var data = new Array();	   		
		setNestedBeanPropertyValue(data, "idSetorCausadorAbatimento", idSetorCausadorAbatimento);
		var sdo = createServiceDataObject("lms.contasreceber.manterMotivosDescontosAction.findMotivoDescontoByTpMotivoDesconto","findMotivoDescontoByTpMotivoDesconto", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findMotivoDescontoByTpMotivoDesconto_cb(data, error){
		if (error != undefined) {
			alert(error);
		}
		comboboxLoadOptions({e:document.getElementById("motivoDesconto.idMotivoDesconto"), data:data});		
	}	
</script>