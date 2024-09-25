<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterReciboBrasilAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterReciboBrasil" service="lms.contasreceber.manterReciboBrasilAction.findByIdFaturaRecibo"
	idProperty="idFaturaRecibo" onDataLoadCallBack="myOnDataLoad">
		<adsm:masterLink idProperty="idRecibo" showSaveAll="true">
			<adsm:masterLinkItem property="filialByIdFilialEmissora.sgFilial" label="filialCobranca" />
			<adsm:masterLinkItem property="nrRecibo" label="numeroRecibo" />
		</adsm:masterLink>
		
		
		<adsm:combobox property="documento.tpDocumento"
			   label="documentoServico" 
			   width="80%"
			   labelWidth="20%"
			   service="lms.contasreceber.manterBoletosAction.findTipoDocumentoServico"
			   optionProperty="value"
			   optionLabelProperty="description"
			   serializable="true"
			   onchange="return tpDocumentoServicoOnChange();">

			<adsm:lookup dataType="text"
						 property="documento.filial"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterBoletosAction.findLookupFilial"
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3"
						 picker="false" 
						 onchange="return filialOnChange();"
						 onDataLoadCallBack="filialOnChange"						 
						 exactMatch="true">		
					<adsm:propertyMapping relatedProperty="documento.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>							 
			</adsm:lookup>
			<adsm:lookup dataType="integer"
						 property="documento"
						 idProperty="idDocumento" 
						 criteriaProperty="nrDocumento"
						 service="lms.contasreceber.manterBoletosAction.findDocumento"
						 action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						 cmd="pesq"
						 size="10" 
						 maxLength="10" 
						 serializable="true" 
						 disabled="true" 
						 exactMatch="true"
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadDocumento"
						 onchange="return onChangeDocumento();"
						 popupLabel="pesquisarDocumentoServico"
						 required="true">
			</adsm:lookup>			 
		</adsm:combobox>	
		
		<adsm:hidden property="documento.filial.pessoa.nmFantasia"/>	
		
        <adsm:textbox dataType="currency" label="valorDocumento" property="vlDocumento" size="10" labelWidth="20%" maxLength="18" disabled="true" width="30%"/>
        <adsm:textbox dataType="currency" label="valorDesconto" property="vlDesconto" size="10" labelWidth="20%" maxLength="18" disabled="true" width="30%"/>
        <adsm:textbox dataType="currency" label="jurosCalculado" property="vlJuros" size="10" labelWidth="20%" maxLength="18" disabled="true" width="30%"/>
        <adsm:textbox dataType="currency" label="jurosRecebidos" property="vlJuroRecebido" size="10" labelWidth="20%" maxLength="18" width="30%" required="true" minValue="0"/>        
        <adsm:textbox dataType="currency" label="valorRecebido" property="vlCobrado" size="10" labelWidth="20%" maxLength="18" disabled="true" width="30%"/>

        <adsm:section caption="totaisRecibo" />
		<adsm:textbox dataType="text" property="qtDocumentos" label="quantidadeDocumentos" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox dataType="currency" property="vlTotalCobrado" label="valorTotalCobrado" labelWidth="20%" width="30%" disabled="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarDocumento" service="lms.contasreceber.manterReciboBrasilAction.storeFaturaRecibo"/>
			<adsm:newButton caption="novoDocumento" onclick="return myNewButtonScript();"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idFaturaRecibo" property="faturaRecibo" 
		unique="true" autoSearch="false" rows="5" showGotoBox="true"
		showPagging="true" detailFrameName="faturas"
		service="lms.contasreceber.manterReciboBrasilAction.findPaginatedFaturaRecibo"
		rowCountService="lms.contasreceber.manterReciboBrasilAction.getRowCountFaturaRecibo">
		
		<adsm:gridColumn title="documento" property="tpDocumento" width="30"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">			
			<adsm:gridColumn title="" property="sgFilial" dataType="text" width="60"/>		
			<adsm:gridColumn title="" property="nrDocumento" dataType="text" width="60"/>
		</adsm:gridColumnGroup>		

		
		<adsm:gridColumn title="valorDocumento" property="vlDocumento" dataType="currency" width="100"/>
		<adsm:gridColumn title="valorDesconto" property="vlDesconto" dataType="currency" width="100"/>		
		<adsm:gridColumn title="jurosCalculado" property="vlJuros" dataType="currency" width="100"/>				
		<adsm:gridColumn title="jurosRecebidos" property="vlJuroRecebido" dataType="currency" width="100"/>						
		<adsm:gridColumn title="valorRecebido" property="vlCobrado" dataType="currency"/>		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDocumento" service="lms.contasreceber.manterReciboBrasilAction.removeByIdsFaturaRecibo"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	document.getElementById("qtDocumentos").masterLink = "true";
	document.getElementById("vlTotalCobrado").masterLink = "true";
	var dominioDocumento = new Object();

	/*
	 *
	 *
	 * CÓDIGO PARA A LOOKUP DE DOCUMENTO DE SERVICO/FATURA
	 *
	 *
	 *
	 */
	
	/*
	 * On change da combo de Tipo de Documento de Serivo.<BR>
	 * Altera lookup de conhecimento
	 * @see changeLookupConhecimento
	 */
	function tpDocumentoServicoOnChange(){
		resetDevedor();	
		setLookupAction();
		resetValue("documento.filial.idFilial");
		filialOnChange();		
		setMaskNrDocumento("documento.nrDocumento", getElementValue("documento.tpDocumento"));
	}
	
	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor(){
		resetDadosDocumento();
		resetValue("documento.idDocumento");
		resetValue("documento.nrDocumento");
		
		if (getElementValue("documento.tpDocumento") == "") {
			setDisabled("documento.filial.idFilial",true);
			setDisabled("documento.idDocumento",true);	
		} else {
			setDisabled("documento.filial.idFilial",false);
			habilitaLupa();
			if (getElementValue("documento.filial.sgFilial") != ""){
				setDisabled("documento.idDocumento",false);		
			}
		}			
	}
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("documento.idDocumento", false);
		setDisabled("documento.nrDocumento", true);
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
		case "FAT":
			obj.mask = "0000000000";			
			break;			
		}
	}	
	
	
		
	
	function setLookupAction(){
		if (getElementValue("documento.tpDocumento") == "FAT"){
			document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterReciboBrasilAction.findFatura";
			document.getElementById("documento.idDocumento").cmd = "list";
			document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/manterFaturas.do";
			
			document.getElementById("documento.idDocumento").propertyMappings = [  
			{ modelProperty:"nrFatura", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false }, 
			{ modelProperty:"nrFatura", relatedProperty:"documento.nrDocumento", blankFill:true }, 
			{ modelProperty:"filialByIdFilial.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
			{ modelProperty:"filialByIdFilial.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
			{ modelProperty:"filialByIdFilial.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
			{ modelProperty:"filialByIdFilial.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },
			{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", criteriaProperty:"documento.filial.pessoa.nmFantasia", inlineQuery:true, disable:true },
			{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }			
			];			
						
		} else {
			document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterReciboBrasilAction.findDevedorServDocFat";
			document.getElementById("documento.idDocumento").cmd = "pesq";
			document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/pesquisarDevedorDocServFatLookUp.do";
			
			document.getElementById("documento.idDocumento").propertyMappings = [  
			{ modelProperty:"doctoServico.nrDoctoServico", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false }, 
			{ modelProperty:"doctoServico.nrDoctoServico", relatedProperty:"documento.nrDocumento", blankFill:true }, 
			{ modelProperty:"doctoServico.tpDocumentoServico", criteriaProperty:"documento.tpDocumento", inlineQuery:true, disable:true }, 
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },			
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }						
			];
		}	
	}

	
	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
		resetDevedor();
		
		return documento_filial_sgFilialOnChangeHandler();
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
		resetDevedor();
		if (data.length == 1) {		
			setDisabled("documento.idDocumento",false);
			//setMaskNrDocumento("documento.nrDocumento", getElementValue("documento.tpDocumento"))					
	
			__lookupSetValue({e:getElement("documento.filial.idFilial"), data:data[0]});
			//setElementValue("documento.filial.pessoa.nmFantasia",data[0].pessoa.nmFantasia);
			return true;
		} else {
			habilitaLupa();
			alert(lookup_noRecordFound);
			return false;
		}			
	}
	

	
	function onChangeDocumento(){
		if (getElementValue("documento.nrDocumento") == ""){
			resetDevedor();
		}
		return documento_nrDocumentoOnChangeHandler();
	}	
	
	
	function myOnPopupSetValue(data, dialogWindow){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject("lms.contasreceber.manterReciboBrasilAction.validateMonitoramentoEletronicoAutorizado", "myOnPopupSetValue", {data:data});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
		
		return false;
	}
		
	function myOnPopupSetValue_cb(data, error) {
		if (error) {
			alert(error);
		} else {
			if (data.data.idFatura != null) {			
				setElementValue("documento.idDocumento",data.data.idFatura);
				setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.data.nrFatura));
				setElementValue("documento.filial.idFilial",data.data.filialByIdFilial.idFilial);
				setElementValue("documento.filial.sgFilial",data.data.filialByIdFilial.sgFilial);
				setElementValue("documento.filial.pessoa.nmFantasia",data.data.filialByIdFilial.pessoa.nmFantasia);
				setDisabled("documento.idDocumento", false);
			}
	
			if (data.data.idDevedorDocServFat != null) {			
				setElementValue("documento.idDocumento",data.data.idDevedorDocServFat);
				setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.data.nrDoctoServico));
				setElementValue("documento.filial.idFilial",data.data.idFilialOrigem);
				setElementValue("documento.filial.sgFilial",data.data.sgFilialOrigem);	
				setElementValue("documento.filial.pessoa.nmFantasia",data.data.nmFantasia);
				setDisabled("documento.idDocumento", false);
			}
			
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterReciboBrasilAction.findDadosFaturaRecibo", "onDataLoad",
				{
				tpDocumento:getElementValue("documento.tpDocumento"),
				idDocumento:getElementValue("documento.idDocumento")
				}));
			xmit(false);
		}
		return false;
	}		
	
	function myOnDataLoadDocumento_cb(d,e){
		documento_nrDocumento_exactMatch_cb(d,e);
		if (e == undefined) {
			if ((d.length == 1)){
				myOnPopupSetValue(d[0], undefined);
			}			
		}
	}
	
	function resetDadosDocumento(){
		resetValue("vlDocumento");
		resetValue("vlDesconto");
		resetValue("vlJuros");
		resetValue("vlCobrado");
	}


	/*
	 *
	 *
	 *
	 * CÓDIGO DA TELA
	 *
	 *
	 *
	 *
	 */
	 
	function myOnPageLoad_cb(d,e,c,x){		
		/*_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterReciboBrasilAction.findDominioDocumento", "findDominioDocumento"));
		xmit(false);*/

		onPageLoad_cb(d,e,c,x);
	}
	
	function myOnDataLoad_cb(d,e,c,x){
		if (e==undefined){
			setMaskNrDocumento("documento.nrDocumento", d.documento.tpDocumento);
		}
		onDataLoad_cb(d,e,c,x);		
		setDisabled("documento.filial.idFilial", false);
		setDisabled("documento.idDocumento", false);
		setLookupAction();
		populaSomatorios();
	}
	
	function initWindow(eventObj){
		if(eventObj.name == "tab_click" || eventObj.name == "storeItemButton" || eventObj.name == "removeButton_grid"){
		    populaSomatorios();
		    		       
		}
		if (eventObj.name == "storeItemButton"){
			tpDocumentoServicoOnChange();
			resetDevedor(); 
		}
		if (eventObj.name == "newItemButton_click"){
			tpDocumentoServicoOnChange();
			resetDevedor();
		}
		
	    if (eventObj.name == "tab_click") {
	    	if (eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
	    		getTabGroup(this.document).getTab("cad").tabOwnerFrame.initPage();
	    	}
	    	tpDocumentoServicoOnChange();
	    }			
	}
	
	function myNewButtonScript(){
		newButtonScript();
		populaSomatorios();
	}
	
	function populaSomatorios(){
	     var remoteCall = {serviceDataObjects:new Array()};
	     var dataCall = createServiceDataObject("lms.contasreceber.manterReciboBrasilAction.findSomatorios", "populaSomatorios", 
	           {
	                 idRecibo:getElementValue("masterId")
	           }
	     );
	     remoteCall.serviceDataObjects.push(dataCall);
	     xmit(remoteCall);  
		
	}
	
	/** Função (callBack) para popular os dados dos somatórios */
	
	function populaSomatorios_cb(data, error, erromsg){
		if (error != undefined) {
			alert(error+'');
			return;
		}
		
		setElementValue(getElement("vlTotalCobrado"), setFormat(getElement("vlTotalCobrado"), data.vlTotalCobrado));	
	    setElementValue("qtDocumentos",data.qtDocumentos);
	    	    
	}	
</script>