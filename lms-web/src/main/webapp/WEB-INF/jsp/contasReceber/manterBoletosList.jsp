<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterBoletosAction" onPageLoadCallBack="myOnPageLoad"> 
	<adsm:form action="/contasReceber/manterBoletos" service="lms.contasreceber.manterBoletosAction">
		<adsm:i18nLabels>
			<adsm:include key="LMS-36219"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="idProcessoWorkflow"/>
		
		<adsm:textbox label="numeroBoleto" property="nrBoleto" maxLength="13" dataType="text" labelWidth="20%" width="30%"/>
		<adsm:textbox label="sequencialFilial" property="nrSequenciaFilial" maxLength="10" dataType="integer" width="30%" labelWidth="20%"/>
		
		<adsm:combobox property="documento.tpDocumento"
			   label="documentoServico" 
			   width="30%"
			   labelWidth="20%"
			   service="lms.contasreceber.manterBoletosAction.findComboTpDocumento"
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
						 size="11" 
						 maxLength="10" 
						 serializable="true" 
						 disabled="true" 
						 exactMatch="true"
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadDocumento"
						 popupLabel="pesquisarDocumentoServico"
						 mask="0000000000">
			</adsm:lookup>			 
		</adsm:combobox>
		
		<adsm:hidden property="documento.filial.pessoa.nmFantasia"/>
		
		<adsm:combobox 
			service="lms.contasreceber.manterBoletosAction.findComboCedentes" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			label="banco"
			boxWidth="180"
			labelWidth="20%" width="30%"> 
		</adsm:combobox>
		
		<adsm:lookup property="fatura.filialByIdFilialCobranca" idProperty="idFilial" criteriaProperty="sgFilial" 
			service="lms.contasreceber.manterBoletosAction.findLookupFilial" dataType="text"  label="filialCobranca" size="3" 
			action="/municipios/manterFiliais" width="9%" labelWidth="20%"
			minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px" maxLength="3" required="false">
			<adsm:propertyMapping relatedProperty="fatura.filialByIdFilialCobranca.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
					<adsm:textbox dataType="text" property="fatura.filialByIdFilialCobranca.pessoa.nmFantasia" width="26%" size="30" serializable="false" disabled="true"/>
		</adsm:lookup>		

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true"  
			idProperty="idCliente" 
			label="cliente" 
			maxLength="20" 
			property="fatura.cliente" 
			service="lms.contasreceber.manterBoletosAction.findLookupCliente" 
			size="20"
			labelWidth="20%" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="fatura.cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="fatura.cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
				
		</adsm:lookup>

		<adsm:combobox label="situacao" property="tpSituacaoBoleto" domain="DM_STATUS_BOLETO" labelWidth="20%" width="30%" />
		
	    <adsm:range label="dataEmissao" labelWidth="20%" width="30%" maxInterval="31">
			<adsm:textbox property="dtEmissaoInicial" dataType="JTDate" width="15%"/>
			<adsm:textbox property="dtEmissaoFinal" dataType="JTDate" width="15%"/>
		</adsm:range>
		<adsm:range label="dataVencimento" labelWidth="20%" width="30%" maxInterval="31">
			<adsm:textbox property="dtVencimentoInicial" dataType="JTDate" width="15%"/>
			<adsm:textbox property="dtVencimentoFinal" dataType="JTDate" width="20%"/>
		</adsm:range>
		<adsm:range label="liquidacao" labelWidth="20%" width="30%" maxInterval="31">
			<adsm:textbox property="fatura.dtLiquidacaoInicial" dataType="JTDate" width="15%"/>
			<adsm:textbox property="fatura.dtLiquidacaoFinal" dataType="JTDate" width="20%"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				buttonType="findButton"
				caption="consultar" 
				id="__buttonBar:0.findButton" 
				disabled="false" 
				onclick="return myFindButtonScript('boleto', this.form);"/>
			<adsm:button buttonType="cleanButton" caption="limpar" onclick="myCleanButtonScript();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idBoleto" 
				property="boleto" 
				rows="8" 
				gridHeight="150" 
				selectionMode="none" 
				service="lms.contasreceber.manterBoletosAction.findPaginatedBoleto"
				rowCountService="lms.contasreceber.manterBoletosAction.getRowCountBoleto" >
		
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="25" title="fatura" property="fatura.filialByIdFilial.sgFilial" dataType="text"/>			
			<adsm:gridColumn width="60" title="" property="fatura.nrFatura" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		
		
				
		<adsm:gridColumn width="10%" title="emissao" property="dtEmissao" dataType="JTDate"/>
		<adsm:gridColumn width="10%" title="vencimento" property="dtVencimento" dataType="JTDate"/>
		<adsm:gridColumn width="12%" title="situacao" property="tpSituacaoBoleto" isDomain="true"/>
		<adsm:gridColumn width="10%" title="filialCobranca" property="fatura.filialByIdFilialCobradora.sgFilial"/>
		<adsm:gridColumnGroup customSeparator=" - ">	
			<adsm:gridColumn width="90" title="cliente" property="fatura.cliente.pessoa.nrIdentificacao"/>
			<adsm:gridColumn width="140" title="" property="fatura.cliente.pessoa.nmPessoa"/>		
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="" title="valorBoletoReais" property="vlTotal" dataType="currency"/>
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script>
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
		
		if (getElementValue("documento.tpDocumento") == "FAT"){
			document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterBoletosAction.findFatura";
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
			document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterBoletosAction.findDevedorServDocFat";
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
		
		resetValue("documento.filial.idFilial");
		filialOnChange();		
		setMaskNrDocumento("documento.nrDocumento", getElementValue("documento.tpDocumento"));
				
	}

	function initWindow(eventObj){
		if (eventObj.name == "tab_click") {
			if (getElementValue("documento.tpDocumento") == "") {
				setDisabled("documento.filial.idFilial", true);
				setDisabled("documento.idDocumento", true);
			}else {
				setDisabled("documento.filial.idFilial", false);
				setDisabled("documento.idDocumento", false);
			}
		} else {
			resetDevedor();
		}
		findFilialUsuario();
	}
	
	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor(){
	
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
	
	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
		resetDevedor();
		
		var siglaFilial = getElement('documento.filial.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = documento_filial_sgFilialOnChangeHandler();
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('documento_lupa',false);		
		}
		
		return retorno;
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
		resetDevedor();
		if (data.length == 1) {			
			setDisabled("documento.idDocumento",false);
			__lookupSetValue({e:getElement("documento.filial.idFilial"), data:data[0]});
			return true;
		} else {
			alert(lookup_noRecordFound);
		}			
	}
	
	function myCleanButtonScript(){
		cleanButtonScript(this.document, true);
		resetDevedor();
	}
		
	function myOnPopupSetValue(data, dialogWindow){
		if (data.idFatura != null) {
			setElementValue("documento.idDocumento",data.idFatura);
			setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.nrFatura));
			setElementValue("documento.filial.idFilial",data.filialByIdFilial.idFilial);
			setElementValue("documento.filial.sgFilial",data.filialByIdFilial.sgFilial);
			
			setDisabled("documento.idDocumento", false);
		} else if (data.idDevedorDocServFat != null) {			
			setElementValue("documento.idDocumento",data.idDevedorDocServFat);
			setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.nrDoctoServico));
			setElementValue("documento.filial.idFilial",data.idFilialOrigem);
			setElementValue("documento.filial.sgFilial",data.sgFilialOrigem);	
			
			setDisabled("documento.idDocumento", false);			
		} else {
			habilitaLupa();
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
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);		
		findFilialUsuario();
	}
 
	function myFindButtonScript(callback, form){
		if (!validateForm(document.forms[0])){
			return false;
		}
		
		if ((getElementValue("nrBoleto") == "") &&
			(getElementValue("documento.idDocumento") == "") &&
			(getElementValue("fatura.cliente.idCliente") == "") &&
			(getElementValue("fatura.filialByIdFilialCobranca.idFilial") == "" || getElementValue("dtEmissaoInicial") == "" || getElementValue("dtEmissaoFinal") == "") &&
			(getElementValue("fatura.filialByIdFilialCobranca.idFilial") == "" || getElementValue("dtVencimentoInicial") == "" || getElementValue("dtVencimentoFinal") == "") &&
			(getElementValue("fatura.filialByIdFilialCobranca.idFilial") == "" || getElementValue("fatura.dtLiquidacaoInicial") == "" || getElementValue("fatura.dtLiquidacaoFinal") == "")
			){
			alertI18nMessage("LMS-36219");
			return false;
		}
		
		return findButtonScript(callback, form);
	}
	
	/**
	*	Busca a filial do usuário logado
	*/
	function findFilialUsuario(){
		if (getElement("fatura.filialByIdFilialCobranca.idFilial").masterLink == undefined){
			var dados = new Array();
	        
	        var sdo = createServiceDataObject("lms.contasreceber.manterBoletosAction.findFilialUsuario",
	                                          "findFilialUsuario",
	                                          dados);
	        xmit({serviceDataObjects:[sdo]});
        }	
	}
	
	/**
	*	Retorno da busca da filial do usuário.
	*   Seta a filial da seção na lookup de filial
	*/
	function findFilialUsuario_cb(data,erro){
		if (erro == undefined){
			setElementValue("fatura.filialByIdFilialCobranca.idFilial", data.idFilial);
			setElementValue("fatura.filialByIdFilialCobranca.sgFilial", data.sgFilial);
			setElementValue("fatura.filialByIdFilialCobranca.pessoa.nmFantasia", data.nmFantasia);
		}			
	}
	
	document.getElementById("nrSequenciaFilial").style.textAlign = "right";

</script>