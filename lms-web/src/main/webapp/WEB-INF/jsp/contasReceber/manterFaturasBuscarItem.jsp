<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas"
		service="lms.contasreceber.manterFaturasAction.findByIdItemFatura"
		idProperty="idItemFatura">

		<adsm:combobox
			property="devedorDocServFat.doctoServico.tpDocumentoServico"
			label="documentoServico" width="35%"
			service="lms.contasreceber.manterFaturasAction.findTipoDocumentoServico"
			optionProperty="value" optionLabelProperty="description" defaultValue="CTR"
			onchange="return tpDocumentoServicoOnChange();">

			<adsm:lookup dataType="text"
				property="devedorDocServFat.doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="lms.contasreceber.manterFaturasAction.findLookupFilial"
				disabled="true" action="" size="3" maxLength="3" picker="false"
				exactMatch="true" onchange="return filialOnChange();"
				popupLabel="pesquisarFilial"
				onDataLoadCallBack="filialOnChange" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
				relatedProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
			<adsm:lookup dataType="integer" property="devedorDocServFat"
				idProperty="idDevedorDocServFat"
				criteriaProperty="doctoServico.nrDoctoServico"
				service="lms.contasreceber.manterFaturasAction.findDevedorServDocFat"
				action="/contasReceber/pesquisarDevedorDocServFatLookUp" cmd="pesq"
				popupLabel="pesquisarDocumentoServico"
				onPopupSetValue="doctoServico_nrDoctoServico_popUpSetValue"
				size="10" maxLength="8" serializable="true" disabled="true"
				exactMatch="true"
				required="true">
				<adsm:propertyMapping
					modelProperty="doctoServico.tpDocumentoServico"
					criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico" />
				<adsm:propertyMapping
					modelProperty="doctoServico.filialByIdFilialOrigem.idFilial"
					criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping
					modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial"
					criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial"
					inlineQuery="true" />
										
				<adsm:propertyMapping modelProperty="cliente.idCliente"
					criteriaProperty="idCliente" />
				<adsm:propertyMapping modelProperty="cliente.pessoa.nrIdentificacao"
					criteriaProperty="nrCliente" />		
				<adsm:propertyMapping modelProperty="cliente.pessoa.nmPessoa"
					criteriaProperty="nmCliente" />							

				<adsm:propertyMapping modelProperty="idMoeda"
					criteriaProperty="devedorDocServFat.doctoServico.moeda.idMoeda" />
				<adsm:propertyMapping modelProperty="idServico"
					criteriaProperty="devedorDocServFat.doctoServico.servico.idServico" />
				<adsm:propertyMapping modelProperty="tpModal"
					criteriaProperty="devedorDocServFat.doctoServico.servico.tpModal" />
				<adsm:propertyMapping modelProperty="tpAbrangencia"
					criteriaProperty="devedorDocServFat.doctoServico.servico.tpAbrangencia" />
					
			</adsm:lookup>
		</adsm:combobox>
		<adsm:hidden property="idCliente" />
		<adsm:hidden property="nrCliente" />
		<adsm:hidden property="nmCliente" />		
		
		<adsm:hidden property="devedorDocServFat.doctoServico.moeda.idMoeda" />
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.idServico" />
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.tpModal" />
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.tpAbrangencia" />		
		
		
		<adsm:buttonBar>
			<adsm:button caption="localizar" disabled="false" onclick="buscarItemFatura();"/>
			<adsm:button caption="cancelar" onclick="self.close();"  disabled="false"/>
		</adsm:buttonBar>		
		
	</adsm:form>
</adsm:window>
<script>

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
			setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"))					
	
			__lookupSetValue({e:getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"), data:data[0]});
			return true;
		} else {
			alert(lookup_noRecordFound);
		}		
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
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("devedorDocServFat.idDevedorDocServFat", false);
		setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", true);
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
	
	function buscarItemFatura(){ 
		if (validateForm(document.forms[0])){
			dialogArguments.window.findNavigatedItemFatura(getElementValue("devedorDocServFat.idDevedorDocServFat"));
			window.close();
		}
	}
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		tpDocumentoServicoOnChange();
		getElement("devedorDocServFat.doctoServico.tpDocumentoServico").required = true;
		getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial").required = true;
	}
	
	function doctoServico_nrDoctoServico_popUpSetValue(data, error){
		__lookupSetValue({e:getElement("devedorDocServFat.idDevedorDocServFat"), data:data});
		setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", false);
		setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial", data.idFilialOrigem);
		setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial", data.sgFilialOrigem);
	}
</script>