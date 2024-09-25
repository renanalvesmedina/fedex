<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	/*
	 * Remove as criteriaProperty da lookup.
	 */
	function myOnPageLoad(){
		onPageLoad();
		
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterConhecimentosSeremTransferidosAction.findInitialValue", "initPage", null)); 
		xmit(false);
		
		var url = new URL(location.href);
		if ((url.parameters != undefined) && (url.parameters["mode"] == "lookup")) {
			document.getElementById('btnFechar').property = ".closeButton";
			setDisabled("btnFechar",false);
		} else {
			getElement("btnFechar").style.visibility = "hidden";
		}								
	}	
</script>
<adsm:window service="lms.contasreceber.manterConhecimentosSeremTransferidosAction" onPageLoad="myOnPageLoad" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterConhecimentosSeremTransferidos" 
			service="lms.contasreceber.manterConhecimentosSeremTransferidosAction">

		<adsm:hidden property="filialByIdFilialOrigem.idFilial"/>
		<adsm:textbox dataType="text" property="filialByIdFilialOrigem.sgFilial" size="3" width="85%" label="filialOrigem" 
			serializable="false" disabled="true" labelWidth="15%">				
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" 
			size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>	
		
		<adsm:hidden property="tpSituacaoDevedorDocServFatValido" serializable="true" value="0"/>

		<adsm:combobox property="devedorDocServFat.doctoServico.tpDocumentoServico"
			   label="documentoServico" 
			   width="35%"
			   service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findTipoDocumentoServico"
			   optionProperty="value" 
			   optionLabelProperty="description"
			   serializable="true"
			   onchange="return tpDocumentoServicoOnChange();"> 			

			<adsm:lookup dataType="text"
						 property="devedorDocServFat.doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findLookupFilial"
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 exactMatch="true"
						 onchange="return filialOnChange();"
						 onDataLoadCallBack="filialOnChange"/>		
										 
			<adsm:lookup dataType="integer"
						 property="devedorDocServFat"
						 idProperty="idDevedorDocServFat" 
						 criteriaProperty="doctoServico.nrDoctoServico"
						 service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findDevedorServDocFatNoMessage"
						 action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						 cmd="pesq"
						 size="11" 
						 maxLength="10" 
						 serializable="true" 
						 disabled="true" 
						 exactMatch="true"
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadDocumento"
						 popupLabel="pesquisarDocumentoServico"
						 mask="0000000000">
						 <adsm:propertyMapping modelProperty="tpSituacaoDevedorDocServFatValido" criteriaProperty="tpSituacaoDevedorDocServFatValido"/>
						 <adsm:propertyMapping modelProperty="doctoServico.tpDocumentoServico" criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico"/>						 						 
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"/>
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="true"/>						 
						 <adsm:propertyMapping modelProperty="idDoctoServico" relatedProperty="devedorDocServFat.doctoServico.idDoctoServico"/>						 
			</adsm:lookup>			 
		</adsm:combobox>
		
		<adsm:hidden property="devedorDocServFat.doctoServico.idDoctoServico"/>
		
        <adsm:lookup label="filialDestino" property="filialByIdFilialDestino" 
        	service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findLookupFilial" 
        	action="/municipios/manterFiliais" idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="3" 
        	maxLength="3" labelWidth="15%" width="35%">
            <adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"
	            modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" 
				size="30" disabled="true" serializable="false"/>
        </adsm:lookup>		

		<adsm:lookup label="responsavelAnterior" width="85%" size="20" maxLength="20"  serializable="true"
					 service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="devedorDocServFat.cliente" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="false"
					 disabled="false">
			<adsm:propertyMapping relatedProperty="devedorDocServFat.cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="devedorDocServFat.cliente.pessoa.nmPessoa" size="50" maxLength="50" 
						  disabled="true" serializable="true"/>
		</adsm:lookup>	
		
		<adsm:lookup label="novoResponsavel" width="85%" size="20" maxLength="20"  serializable="true"
					 service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="cliente" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="false"
					 disabled="false">
			<adsm:propertyMapping relatedProperty="cliente.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.nmPessoa" size="50" maxLength="50" 
						  disabled="true" serializable="true"/>
		</adsm:lookup>			
		
		<adsm:combobox service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findMotivoTransferencia" 
		optionLabelProperty="dsMotivoTransferencia" 
		optionProperty="idMotivoTransferencia" property="motivoTransferencia.idMotivoTransferencia" 
		label="motivo" boxWidth="240"/>
		
		<adsm:combobox property="tpOrigem" label="origem" domain="DM_ORIGEM"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="agendaTransferencia"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="agendaTransferencia" idProperty="idAgendaTransferencia" width="850" scrollBars="horizontal" 
	rows="9">	
		<adsm:gridColumn width="30" title="documentoServico" isDomain="true" property="tpDocumentoServico"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="" property="sgFilialOrigem"/>	
			<adsm:gridColumn width="100" title="" property="nrDocumentoServico" />
		</adsm:gridColumnGroup>

		
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="0" title="filialDestino" property="sgFilialDestino"/>
			<adsm:gridColumn width="150" title="" property="nmFantasiaFilialDestino"/>	
		</adsm:gridColumnGroup>
	
		<adsm:gridColumn width="90" title="responsavelAnterior" property="nrIdentificacaoClienteOrigem" align="right"/>		
		<adsm:gridColumn width="150" title="" property="nmPessoaClienteOrigem"/>				
		<adsm:gridColumn width="90" title="novoResponsavel" property="nrIdentificacaoClienteDestino" align="right"/>
		<adsm:gridColumn width="150" title="" property="nmPessoaClienteDestino"/>	
		<adsm:gridColumn width="90" title="divisao" property="dsDivisaoCliente"/>	
		
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>	
		
	</adsm:grid>
</adsm:window>
<script src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script>

	getElement('tpSituacaoDevedorDocServFatValido').masterLink = 'true';

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

	var constFilial	= new Object(4);

	function myOnPageLoad_cb(d,e,o,x){
		onPageLoad_cb(d,e,o,x);
		loadFilial();
	}
	
	function initWindow(eventObj) {
	
		// Caso o evento não seja tab_click
		if(eventObj.name != "tab_click"){
			loadFilial();
	    	tpDocumentoServicoOnChange();	    	
		}
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
	 * Monta a constante que tem a filial da sessão
	 */
	function initPage_cb(d,e,o,x){
		if (d != null) {	
			constFilial.idFilial = d.idFilial;
			constFilial.sgFilial = d.sgFilial;
			constFilial.nmFantasia = d.nmFantasia;							
			constFilial.dataAtual = d.dataAtual;			
		}
	}	
	
	function loadFilial(){
		if (constFilial.idFilial != undefined){
			setElementValue("filialByIdFilialOrigem.idFilial", constFilial.idFilial);
			setElementValue("filialByIdFilialOrigem.sgFilial", constFilial.sgFilial);
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", constFilial.nmFantasia);	
		} else {	
			setTimeout("loadFilial()",1000);
		}		
	}



	/*
	 * On change da combo de Tipo de Documento de Serivo.<BR>
	 * Altera lookup de conhecimento
	 * @see changeLookupConhecimento
	 */
	function tpDocumentoServicoOnChange(){
	
		resetDevedor();	
		resetValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");
		filialOnChange();						
		setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"));
	
	}
	
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
			return true;
		} else {
			alert(lookup_noRecordFound);
		}			
	}			
	
	function myOnPopupSetValue(data, dialogWindow){
		if (data.idDevedorDocServFat != null) {			
			setElementValue("devedorDocServFat.doctoServico.idDoctoServico",data.idDevedorDocServFat);
			setElementValue("devedorDocServFat.doctoServico.nrDoctoServico",setFormat(getElement("devedorDocServFat.doctoServico.nrDoctoServico"),data.doctoServico.nrDoctoServico));
			setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",data.idFilialOrigem);
			setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial",data.sgFilialOrigem);	
			
			setDisabled("devedorDocServFat.idDevedorDocServFat", false);			
		} else {
			habilitaLupa();
		}
		
		return false;
	}
	
	function myOnDataLoadDocumento_cb(d,e){
		devedorDocServFat_doctoServico_nrDoctoServico_exactMatch_cb(d,e);
		if (e == undefined) {
			if ((d.length == 1)){
				myOnPopupSetValue(d[0], undefined);
			}			
		}
	}
</script>
