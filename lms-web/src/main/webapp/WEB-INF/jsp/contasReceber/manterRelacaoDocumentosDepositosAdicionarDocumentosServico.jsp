<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterRelacaoDocumentosDepositosAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterRelacaoDocumentosDepositos"
				service="lms.contasreceber.manterRelacaoDocumentosDepositosAction"
				id="documentos.form">
		<adsm:section caption="adicionarDocumento" style="width:770px"/>
		<tr>
		<td>
		<br>
		</td>
		</tr>
		<adsm:hidden property="masterId"/>				
		<adsm:hidden property="idMoeda"/>			
		<adsm:hidden property="idCliente"/>
		<adsm:hidden property="tpDocumentoAnterior"/>
		<adsm:combobox
			property="tpDocumento"
			label="tipoDocumento"
			domain="DM_TIPO_DOCUMENTO_DEPOSITO" required="true" width="35%"/>
		
		<adsm:range label="numeroDocumento" labelWidth="20%" width="30%">
			<adsm:textbox dataType="integer" property="nrDocumentoInicial" size="10" mask="00000000"/>
	    	<adsm:textbox dataType="integer" property="nrDocumentoFinal"   size="10" mask="00000000"/>
		</adsm:range>	

		<adsm:range label="dataEmissao" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" size="10"
				maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" size="10"
				maxLength="20" />
		</adsm:range>

        <adsm:buttonBar freeLayout="true">
			<adsm:button 
				buttonType="findButton"
				caption="consultar" 
				id="__buttonBar:0.findButton" 
				disabled="false" 
				onclick="return myFindButtonScript('documento', this.form);"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	    <adsm:i18nLabels>
			<adsm:include key="LMS-36043"/>
			<adsm:include key="LMS-36229"/>
			<adsm:include key="LMS-36095"/>	
			<adsm:include key="LMS-36010"/>		
	    </adsm:i18nLabels>

	</adsm:form>

	<adsm:grid idProperty="idDocumento" property="documento" gridHeight="320" 
		onRowClick="returnNull"
		rowCountService="lms.contasreceber.manterRelacaoDocumentosDepositosAction.getRowCountDocumento"
		service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.findPaginatedDocumento" rows="15" 
		onSelectAll="myOnSelectAllRows" onSelectRow="myOnSelectRow" onPopulateRow="myOnPopulateRow">
		<adsm:gridColumn title="tipoDocumento" property="tpDocumento" width="250"/>
		<adsm:gridColumn title="documento" property="nrDocumento" dataType="integer" width="110"/>
		<adsm:gridColumn title="dataEmissao" property="dtEmissao" dataType="JTDate"/>
		 
		<adsm:gridColumn title="valorDocumento" property="siglaSimbolo" width="40" dataType="text"/>			
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">			
			<adsm:gridColumn title="" property="vlDocumento" dataType="currency" width="80"/>
		</adsm:gridColumnGroup>		
		
		<adsm:buttonBar>
            <adsm:button caption="adicionar" id="adicionar" onclick="return adicionarScript();" buttonType="removeButton" disabled="true" />
  		    <adsm:button buttonType="closeButton" caption="fechar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	var idMoedaTmp = null;
	var dominioDocumento = new Object();

	var dataForm = undefined;

	function adicionarScript(){
		if (documentoGridDef.getSelectedIds().ids[0] == undefined) {
			return;
		}
		fillFormWithFormBeanData("documentos.form", dataForm);
		
		var dataAcao = dataForm;//buildFormBeanFromForm(document.getElementById("documentos.form"));
		var dataGrid = documentoGridDef.getSelectedIds();
		var data = new Array();			

		merge(data,dataAcao);
		merge(data,dataGrid);
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.saveItemDepositoCcorrente", "adicionarScript", data)); 
		xmit(true); // deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
	}
	
	function adicionarScript_cb(d,e,c,x){
		onDataLoad_cb(d,e,c,x);
		
		if (e == undefined) {		
			var dataGrid = documentoGridDef.findById(documentoGridDef.getSelectedIds().ids[0]);		
			
			var blRemontarFiltros = !(dialogArguments.window.itemDepositoCcorrenteGridDef.getRowCount() > 0);

			dialogArguments.window.itemDepositoCcorrenteGridDef.executeSearch(null, true);
			dialogArguments.window.populaSomatorios();
			
			if (blRemontarFiltros){
				setElementValue("idMoeda", dataGrid.idMoeda);
			}
			
			myFindButtonScript("documento", document.forms[0])
		}
	}
	
	function myOnSelectAllRows(todos){
		var estado = getElement("documento.chkSelectAll").checked;
		var form = document.getElementById("documento.form");
		var elem = form.elements;
		var idx = 0;
		
		disablePagging(!estado);
	
		for (var j = 0; j < elem.length; j++){
			if (elem[j].id.indexOf(".idDocumento")>0) {				
				var retornoValidateDocumento = true;
				var retornoValidateTpAprovacao = true;
				
				elem[j].checked = estado;
				
				if (estado == true){
					retornoValidateDocumento = validateDocumento(elem[j].parentElement.parentElement);
					retornoValidateTpAprovacao = validateTpAprovacaoDocumento(elem[j].parentElement.parentElement);
				}
				
				if (retornoValidateDocumento == false || retornoValidateTpAprovacao == false){
					for (var k = 0; k <= j; k++){
						elem[k].checked = false;
					}
					getElement("documento.chkSelectAll").checked = true;
					disablePagging(true);
					return false;
				}
			}
		}
		
		return true;
	}
	
	function myOnSelectRow(rowRef){
		var retornoValidateDocumento = true;
		var retornoValidateTpAprovacao = true;
		var dataGrid = documentoGridDef.gridState.data[rowRef.rowIndex];
		var blHabilitar = rowRef.children[0].children[0].checked == false && documentoGridDef.getSelectedIds().ids[0] == undefined ;
		disablePagging(blHabilitar);
		
		retornoValidateDocumento = validateDocumento(rowRef);
		retornoValidateTpAprovacao = validateTpAprovacaoDocumento(rowRef);
		if (retornoValidateDocumento == false || retornoValidateTpAprovacao == false){
			rowRef.children[0].children[0].checked = false;
		}
		
		return retornoValidateDocumento && retornoValidateTpAprovacao;
	}
	
	function validateDocumento(rowRef){
		var dataGrid = documentoGridDef.gridState.data[rowRef.rowIndex];		
		var idMoedaAnterior = getElementValue("idMoeda");
		var idMoedaAtual    = dataGrid.idMoeda;
		var idTemp = null;
		
		if ( idMoedaAnterior != ""){
			idTemp = idMoedaAnterior;
		} else if( idMoedaTmp != null ){
			idTemp = idMoedaTmp;
		} else {
			idMoedaTmp = idMoedaAtual;
			idTemp     = idMoedaAtual;
		}
		
		//Se o idMoeda do novo registro é diferente ao padrão, mandar exception
		if (idMoedaAtual != idTemp ){
			rowRef.firstChild.firstChild.checked = false;
			alert(i18NLabel.getLabel("LMS-36043"));
			var chkAll = getElement('documento.chkSelectAll');
			chkAll.checked = false; 
			return false;
		} else {
			if( rowRef.firstChild.firstChild.checked == false){
				idMoedaTmp = null; 
			}
		}
		
		return true;	
	}
	
	/**
	  * Valida a situação de aprovação do documento.
	  */
	function validateTpAprovacaoDocumento(rowRef){
		var dataGrid = documentoGridDef.gridState.data[rowRef.rowIndex];
		// Caso o documento esteja em aprovado ou não esteja em workflow.
		if (dataGrid.tpSituacaoAprovacao != "" && dataGrid.tpSituacaoAprovacao != "A") {
			// Caso seja fatura.
			if (dataGrid.tpDocumento == "FAT") {
				alert(i18NLabel.getLabel("LMS-36095"));
			// Caso seja outros tipos de documentos.
			} else {
				alert(i18NLabel.getLabel("LMS-36010"));
			}	
			return false;	
		}
		return true;
	}
	 
	function disablePagging(blHabilitar){
		if (blHabilitar && documentoGridDef.getPageCount() > 1){
			var resultSetPage = new Object();
			resultSetPage["list"] = new Array();
			resultSetPage["currentPage"] = documentoGridDef.gridState["currentPage"];
			resultSetPage["hasNextPage"] = documentoGridDef.gridState["hasNextPage"];
			resultSetPage["hasPriorPage"] = documentoGridDef.gridState["hasPriorPage"];
			resultSetPage["firstPage"] = documentoGridDef.gridState["firstPage"];
			resultSetPage["lastPage"] = documentoGridDef.gridState["lastPage"];
			documentoGridDef._refreshPageControls(resultSetPage);
			setDisabled("documento.gotoPageNumber", false);
			setDisabled("documento.gotoPageNumberPicker", false); 
		} else {
			documentoGridDef.changePagingControlsState(true, true, true, true);
			setDisabled("documento.gotoPageNumber", true);
			setDisabled("documento.gotoPageNumberPicker", true); 
		}
	}	
	
	function myOnPageLoad_cb(d,e,c,x){
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.findDominioDocumento", "findDominioDocumento")); 		
		xmit(false);
			
		onPageLoad_cb(d,e,c,x);				
	}	
	
	function returnNull(){
		return false;
	}	
	
	function findDominioDocumento_cb(d,e,c,x){
		if (d != undefined) {
			dominioDocumento.crt = d.CRT;
			dominioDocumento.ctr = d.CTR;
			dominioDocumento.ndn = d.NDN;
			dominioDocumento.nfs = d.NFS;
			dominioDocumento.nft = d.NFT;
			dominioDocumento.fat = d.FAT;
		}
	}
	
	function myOnPopulateRow(row, d){
		if (d.tpDocumento == "CTRC") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.ctr;
		}	
		if (d.tpDocumento == "CRT") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.crt;
		}
		if (d.tpDocumento == "NDN") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.ndn;
		}
		if (d.tpDocumento == "NFS") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.nfs;
		}
		if (d.tpDocumento == "NFT") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.nft;
		}
		if (d.tpDocumento == "FAT") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.fat;
		}
	}
	
	function myFindButtonScript(callback, form){
		if (!validateForm(document.forms[0])){
			return false;
		}
		
		if ((getElementValue("dtEmissaoInicial") == "" || getElementValue("dtEmissaoFinal") == "") &&
			(getElementValue("nrDocumentoInicial") == "" || getElementValue("nrDocumentoFinal") == "")
			){
			alertI18nMessage("LMS-36229");
			return false;
		}
		
		dataForm = buildFormBeanFromForm(document.getElementById("documentos.form"));
		
		return findButtonScript(callback, form);
	}
	
	setMasterLink(document, true);
</script>