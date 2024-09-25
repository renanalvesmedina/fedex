<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterBDMAction">

	<adsm:form action="/contasReceber/manterBDM"
			   idProperty="idItemBaixaDevMerc"
			   service="lms.contasreceber.manterBDMAction.findByIdItemBaixaDevMerc"
			   onDataLoadCallBack="myOnDataLoadCallBack"
	>

		<adsm:masterLink showSaveAll="true" idProperty="idBaixaDevMerc">
           <adsm:masterLinkItem label="filialOrigem" property="siglaNomeFilial"/>
           <adsm:masterLinkItem label="BDM" property="nrBdm"/>
		</adsm:masterLink>

		<adsm:combobox property="devedorDocServFatAntigo.doctoServico.tpDocumentoServico"
			   label="documentoAntigo" 
			   width="35%"
			   service="lms.contasreceber.manterBDMAction.findTipoDocumentoServico"
			   optionProperty="value"
			   defaultValue="CTR"
			   optionLabelProperty="description"
			   onchange="return tpDocumentoServicoOnChange();"> 			

			<adsm:lookup dataType="text"
						 property="devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterFaturasAction.findLookupFilial"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 exactMatch="true"
						 disabled="true"
						 onchange="return filialOnChange();"
						 onDataLoadCallBack="filialOnChange"/>		
										 
			<adsm:lookup dataType="integer"
						 property="devedorDocServFatAntigo"
						 idProperty="idDevedorDocServFat" 
						 criteriaProperty="doctoServico.nrDoctoServico"
						 service="lms.contasreceber.manterBDMAction.findDevedorServDocFat"
						 action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						 cmd="pesq"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 exactMatch="true"
						 mask="00000000"
						 disabled="true"
						 onDataLoadCallBack="findDocNovo"
						 onPopupSetValue="findDocNovo"
						 required="true">
						 <adsm:propertyMapping modelProperty="doctoServico.tpDocumentoServico" criteriaProperty="devedorDocServFatAntigo.doctoServico.tpDocumentoServico"/>
						 <adsm:propertyMapping modelProperty="vlDevido" formProperty="vlDevido"/>
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" criteriaProperty="devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.idFilial"/>						 
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" criteriaProperty="devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="true"/>						 						 
			</adsm:lookup>			 
		</adsm:combobox>

        <adsm:textbox label="valorDocumentoAntigo" property="vlDevido" dataType="decimal" size="10" labelWidth="23%" width="27%" disabled="true" />

		<adsm:hidden property="devedorDocServFat.doctoServico.idDoctoServico"/>

		<adsm:textbox property="devedorDocServFat.doctoServico.tpDocumentoServico"
			   dataType="text"
			   label="documentoNovo" 
			   size="12"
			   maxLength="10"
			   width="11%"
			   disabled="true"/>

		<adsm:textbox property="sgFilialNovo"
			   dataType="text"
			   size="3"
			   maxLength="3"
			   width="5%"
			   disabled="true"/>

		<adsm:textbox dataType="integer"
					 property="nrDevedorDocServFat"
					 size="8" 
					 width="19%"
					 maxLength="8" 
					 serializable="true" 
					 disabled="true"
					 required="true"/>

        <adsm:textbox label="valorDocumentoNovo" property="vlDevidoNovo" dataType="currency" size="10" labelWidth="23%" width="27%" disabled="true" />

        <adsm:section caption="totaisBDM"/>
        
        <adsm:textbox label="quantidadeTotalDocumentos" property="qtdTotal" dataType="integer" size="10" labelWidth="26%" width="24%" disabled="true" />
        <adsm:textbox label="valorTotalBDM" property="vlTotal" dataType="currency" size="10" labelWidth="23%" width="27%" disabled="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="salvarDocumento" id="storeBt" onclick="myStore(this)"/>
			<adsm:newButton caption="novoDocumento"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idItemBaixaDevMerc"
			   property="itemBaixaDevMerc"
			   detailFrameName="documentosServico"
			   autoSearch="false"
			   service="lms.contasreceber.manterBDMAction.findPaginatedItemBaixaDevMerc"
			   rowCountService="lms.contasreceber.manterBDMAction.getRowCountItemBaixaDevMerc"
			   onDataLoadCallBack="gridCallBack"
			   rows="8"
			   onRowClick="returnFalse"
	>

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoAntigo" isDomain="true"
				property="tpDocumentoAntigo" width="30" align="left"/>
			<adsm:gridColumn title="" property="sgFilalAntigo" width="30" />
			<adsm:gridColumn title="" property="nrDoctoServicoAntigo" width="90" />
		</adsm:gridColumnGroup>


		<adsm:gridColumn title="valorDocumentoAntigo" property="vlDevidoAntigo" dataType="currency"/>
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoNovo" isDomain="true"
				property="tpDocumento" width="30" align="left"/>
			<adsm:gridColumn title="" property="sgFilal" width="30" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="90" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="valorDocumentoNovo" property="vlDevido" dataType="currency"/>

		<adsm:buttonBar> 
			<adsm:removeButton caption="excluirDocumento"  service="lms.contasreceber.manterBDMAction.removeByIdsItemBaixaDevMerc"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script>
	getElement("devedorDocServFatAntigo.doctoServico.tpDocumentoServico").required = true;
	getElement("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.sgFilial").required = true;
	/** 
	* Trata o focus quando salvar
	*
	*chamado: pelo botao salvar documento
	*/
	function myStore(elemento){
		var valid = validateForm(elemento.form);
		
		//se o campo nao for preenchido manda o focus para o mesmo
		if ( valid != true   ){
				//setFocusOnFirstFocusableField(document);
		}else{
			storeButtonScript( 'lms.contasreceber.manterBDMAction.saveItemBaixaDevMerc', 'myStoreCallBack',elemento.form );			
		}

	}
	/**
	*	Trata o focus depois de salvar
	*	
	*	chamado por: myStore
	*/
	function myStoreCallBack_cb(data,error){
		store_cb(data,error);
		
		//caso ocorra erro nada eh alterado
		if (error == null) {
			newButtonScript();
		}
		
		setFocusOnFirstFocusableField(document);
		
	}


	document.getElementById("qtdTotal").masterLink = "true";
	document.getElementById("vlTotal").masterLink = "true";

	function gridCallBack_cb(searchFilters){
		findTotais();
	}

	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor(){
	
		resetValue("devedorDocServFatAntigo.idDevedorDocServFat");
		resetValue("devedorDocServFatAntigo.doctoServico.nrDoctoServico");

		if (getElementValue("devedorDocServFatAntigo.doctoServico.tpDocumentoServico") == "") {
			setDisabled("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("devedorDocServFatAntigo.idDevedorDocServFat",true);	
		} else {
			setDisabled("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.idFilial",false);
			habilitaLupa();
			if (getElementValue("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.sgFilial") != ""){
				setDisabled("devedorDocServFatAntigo.idDevedorDocServFat",false);		
			}
		}		

	}	
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("devedorDocServFatAntigo.idDevedorDocServFat", false);
		setDisabled("devedorDocServFatAntigo.doctoServico.nrDoctoServico", true);
	}

	function myOnDataLoadCallBack_cb(data, erro) {
		onDataLoad_cb(data, erro);
		setDisabled("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.idFilial", false);
		setDisabled("devedorDocServFatAntigo.idDevedorDocServFat", false);
	}

	function initWindow(eventObj){

		tpDocumentoServicoOnChange();
		
		setDisabled("storeBt",false);

	}



	/*
	 * On change da combo de Tipo de Documento de Servico.<BR>
	 * Altera lookup de conhecimento
	 * @see changeLookupConhecimento
	 */
	function tpDocumentoServicoOnChange(){
		resetDevedor();	
		resetValue("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.idFilial");
		filialOnChange();						
		setMaskNrDocumento("devedorDocServFatAntigo.doctoServico.nrDoctoServico", getElementValue("devedorDocServFatAntigo.doctoServico.tpDocumentoServico"))	
	}

	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
		resetDevedor();	
		
		var siglaFilial = getElement('devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		var retorno = devedorDocServFatAntigo_doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('devedorDocServFatAntigo_lupa',false);		
		}
		return retorno;
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
			
		if (e != undefined) {
			alert(e);
			return false;
		}
		
		resetDevedor();		
		if (data.length == 1) {			
			setDisabled("devedorDocServFatAntigo.idDevedorDocServFat",false);
			__lookupSetValue({e:getElement("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.idFilial"), data:data[0]});			
			return true;
		} else {
			alert(lookup_noRecordFound);
			setDisabled("devedorDocServFatAntigo.idDevedorDocServFat",false);
			var siglaFilial = getElement('devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.sgFilial')
			siglaFilial.value = "";				
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
       case "NDN":
             obj.mask = "0000000000";
             break;
       case "NFS":
             obj.mask = "000000";
             break;            
       case "NFT":
             obj.mask = "00000000";             
             break;
       }
    }
	
	function findDocNovo(data, error) {
	
		var nrDoc;
		var idDoc;

		if (data[0] == undefined)
			data[0] = data;
		
		try {	
			nrDoc = data[0].doctoServico.nrDoctoServico;
			idDoc = data[0].idDoctoServico;
			
			if (data[0].idDevedorDocServFat != null) {			
				setElementValue("devedorDocServFatAntigo.idDevedorDocServFat",data[0].idDevedorDocServFat);
				setElementValue("devedorDocServFatAntigo.doctoServico.nrDoctoServico",setFormat(getElement("devedorDocServFatAntigo.doctoServico.nrDoctoServico"),data[0].doctoServico.nrDoctoServico));
				setElementValue("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.idFilial",data[0].idFilialOrigem);
				setElementValue("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.sgFilial",data[0].sgFilialOrigem);					
				setDisabled("devedorDocServFatAntigo.idDevedorDocServFat", false);			
			} else {
				habilitaLupa();
			}
		} catch(e) {
			idDoc = data[0].doctoServico.idDoctoServico;
		}
		
		if (idDoc != null && idDoc != "") {
			_serviceDataObjects = new Array();
		
	        addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBDMAction.findDoctoServicoOriginal",
				"setaDocNovo", 
				{idDoctoServico:idDoc}));

	        xmit(false);     	
		}
	
	}
	
	function findDocNovo_cb(data, error) {
		if (error == undefined) {
			var r = devedorDocServFatAntigo_doctoServico_nrDoctoServico_exactMatch_cb(data);
			
			if (data != undefined && data[0] != undefined) {
				if (getElementValue("devedorDocServFatAntigo.doctoServico.nrDoctoServico") != "") {
					findDocNovo(data, error);
				}
			}
			return r;
		}
		return true;	
	}
	
	function setaDocNovo_cb(data, error) {
		if (error != undefined) {
			alert(error);
			setElementValue("devedorDocServFatAntigo.doctoServico.nrDoctoServico", "");
			resetValue("vlDevido");
			if (getElementValue("devedorDocServFatAntigo.doctoServico.filialByIdFilialOrigem.sgFilial") != ""){
				setDisabled("devedorDocServFatAntigo.idDevedorDocServFat",false);		
			}	
			setFocus('devedorDocServFatAntigo.doctoServico.nrDoctoServico');
		} else {
			if (data != undefined) {
				setElementValue("devedorDocServFat.doctoServico.tpDocumentoServico", data[0].tpDocumentoServico.description);
				setElementValue("nrDevedorDocServFat", data[0].nrDoctoServico);
				if (data[0].df[0] == null) {
					setElementValue("vlDevidoNovo", setFormat("vlDevidoNovo", data[0].df.vlDevido));
				} else {
					setElementValue("vlDevidoNovo", setFormat("vlDevidoNovo", data[0].df[0].vlDevido));
				}
				setElementValue("sgFilialNovo", data[0].sgFilial);
			}
		}
	}
	
	function findTotais() {
	
		var remoteCall = {serviceDataObjects:new Array()};
		var dataCall = createServiceDataObject("lms.contasreceber.manterBDMAction.findTotais", 
											   "setaTotais", 
											   {idBaixaDevMerc:getElementValue("masterId")});

		remoteCall.serviceDataObjects.push(dataCall);

		xmit(remoteCall);
	
	}
	
	function setaTotais_cb(data, error) {
		if (error != null) {
			alert(error);
		} else {
			setElementValue("qtdTotal", data.qtd);
			setElementValue("vlTotal", setFormat("vlTotal", data.valor));
		}
	}
	
	function returnFalse(){
		return false;
	}
	
</script>