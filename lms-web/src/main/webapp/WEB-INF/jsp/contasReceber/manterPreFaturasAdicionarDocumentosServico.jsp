<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterPreFaturasAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterPreFaturas" service="lms.contasreceber.manterPreFaturasAction"
	 id="documentos.form">
	    <adsm:range label="notasFiscais" labelWidth="20%" width="30%">
			<adsm:textbox dataType="integer" property="notaInicial" size="10" mask="00000000"/>
	    	<adsm:textbox dataType="integer" property="notaFinal"   size="10" mask="00000000"/>
		</adsm:range>
	
	    <adsm:range label="documentosServico" labelWidth="20%" width="30%">
			<adsm:textbox dataType="integer" property="doctoServicoInicial" size="10" mask="00000000"/>
	    	<adsm:textbox dataType="integer" property="doctoServicoFinal"   size="10" mask="00000000"/>
		</adsm:range>	
	
		<adsm:hidden property="idCliente"/>
		<adsm:hidden property="idFatura"/>				
		<adsm:hidden property="idMoeda"/>
		<adsm:hidden property="tpModal"/>
		<adsm:hidden property="tpAbrangencia"/>
		<adsm:hidden property="idServico"/>	
		<adsm:hidden property="tpSituacaoFatura" />
		<adsm:hidden property="idDivisaoCliente" />

		<adsm:textbox label="emissaoAte" labelWidth="20%" width="30%" dataType="JTDate" property="dtEmissaoFinal" size="10"/>

		<adsm:combobox property="tpFrete" label="tipoFrete" labelWidth="20%" width="30%" domain="DM_TIPO_FRETE"/>

		<adsm:hidden property="nrDocumento" serializable="true"/>
		<adsm:hidden property="tpDocumentoHidden"/>
		<adsm:hidden property="tpDocumentoServicoHidden"/>

		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" 
					   labelWidth="20%" width="80%"
					   service="lms.contasreceber.manterPreFaturasAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return onChangeTpDocumentoServico();"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 popupLabel="pesquisarFilial"
						 picker="false" 
						 exactMatch="true"
						 onDataLoadCallBack="enableDevedorDocServFatDoctoServico"						 
						 onchange="return onChangeFilial();">										 
						 <adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
			</adsm:lookup>
			
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico"
						 popupLabel="pesquisarDocumentoServico"
						 service=""
						 action=""
						 size="12" 
						 maxLength="8" 
						 serializable="true" 
						 disabled="true" 
						 mask="00000000">
			</adsm:lookup>			 
		</adsm:combobox>
		
	    <adsm:i18nLabels>
			<adsm:include key="LMS-36008"/>
			<adsm:include key="LMS-36226"/>			
	    </adsm:i18nLabels>		

        <adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="findButton" caption="consultar" onclick="myFindButton();" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idDevedorDocServFat" property="devedorDocServFat" gridHeight="300"
		service="lms.contasreceber.manterPreFaturasAction.findPaginatedDoctoServico" rows="14"
		rowCountService="lms.contasreceber.manterPreFaturasAction.getRowCountDoctoServico"
		onSelectRow="myOnSelectRow" onRowClick="rowClick" onSelectAll="myOnSelectAll">
     	<adsm:gridColumn title="notaFiscal" property="nrNotaFiscal" width="100" dataType="integer" mask="00000000"/>
  		<adsm:gridColumn width="35" title="documentoServico" property="tpDocumentoServico"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="70" title="" property="sgFilial"/>	
			<adsm:gridColumn width="70" title="" property="nrDoctoServico"/>
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="emissaoAte" property="dtEmissao" dataType="JTDate" width="105" />
		<adsm:gridColumn title="valorMercadoria" property="vlMercadoria" dataType="currency" width="130"/>
		<adsm:gridColumn title="valorFrete" property="vlTotalDocServico" dataType="currency" width="130"/>
		<adsm:gridColumn title="tipoFrete" property="tpFrete"/>
		<adsm:buttonBar>
            <adsm:button caption="adicionar"  id="adicionar" onclick="return adicionarScript();" buttonType="removeButton" disabled="false" />
  		    <adsm:button caption="fechar" id="fechar" disabled="false" onclick="javascript:window.close();"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script>
	
	var objFiltros = new Object();
	
	function initWindow(e){
		setDisabled("adicionar", true);
		setDisabled("fechar", false);
		
		if (e.name = "cleanButton_click"){
			setData();
		}
	}
	
	function myOnSelectAll(){
		var estado = getElement("devedorDocServFat.chkSelectAll").checked;
		var form = document.getElementById("devedorDocServFat.form");
		var elem = form.elements;
		var idx = 0;
	
		disablePagging(!estado);
	
		for (var j = 0; j < elem.length; j++){
			if (elem[j].id.indexOf(".idDevedorDocServFat")>0) {				
				var retorno = true;
				
				elem[j].checked = estado;
				
				if (estado == true){
					retorno = validateDocumento(elem[j].parentElement.parentElement, devedorDocServFatGridDef.gridState.data[elem[j].parentElement.parentElement.rowIndex]);
				}
				
				if (retorno == false){
					for (var k = 0; k <= j; k++){
						elem[k].checked = false;
					}
					getElement("devedorDocServFat.chkSelectAll").checked = true;
					disablePagging(true);
					return false;
				}
			}
		}
		
		return true;
	}
	
	function myOnSelectRow(rowRef){
		var dataGrid = devedorDocServFatGridDef.gridState.data[rowRef.rowIndex];
		var blHabilitar = rowRef.children[0].children[0].checked == false && devedorDocServFatGridDef.getSelectedIds().ids[0] == undefined ;

		//disablePagging(blHabilitar);
		validateDocumento(rowRef, dataGrid);
	}
	
	function disablePagging(blHabilitar){
		if (blHabilitar && devedorDocServFatGridDef.getPageCount() > 1){
			var resultSetPage = new Object();
			resultSetPage["list"] = new Array();
			resultSetPage["currentPage"] = devedorDocServFatGridDef.gridState["currentPage"];
			resultSetPage["hasNextPage"] = devedorDocServFatGridDef.gridState["hasNextPage"];
			resultSetPage["hasPriorPage"] = devedorDocServFatGridDef.gridState["hasPriorPage"];
			resultSetPage["firstPage"] = devedorDocServFatGridDef.gridState["firstPage"];
			resultSetPage["lastPage"] = devedorDocServFatGridDef.gridState["lastPage"];
			devedorDocServFatGridDef._refreshPageControls(resultSetPage);
			setDisabled("devedorDocServFat.gotoPageNumber", false);
			setDisabled("devedorDocServFat.gotoPageNumberPicker", false); 
		} else {
			devedorDocServFatGridDef.changePagingControlsState(true, true, true, true);
			setDisabled("devedorDocServFat.gotoPageNumber", true);
			setDisabled("devedorDocServFat.gotoPageNumberPicker", true); 
		}
	}
	
	function validateDocumento(rowRef, dataGrid){
		if (rowRef.children[0].children[0].checked == false && devedorDocServFatGridDef.getSelectedIds().ids[0] == undefined && objFiltros.novaFatura == true){
			objFiltros = new Object();
		} else if (objFiltros.tpDocumentoServico == undefined){
			objFiltros.tpDocumentoServico = dataGrid.tpDocumentoServico.substr(0,3);
			objFiltros.idMoeda = dataGrid.idMoeda;
			objFiltros.idServico = dataGrid.idServico;
			objFiltros.tpModal = dataGrid.tpModal;
			objFiltros.tpAbrangencia = dataGrid.tpAbrangencia;
			objFiltros.idCliente = dataGrid.idCliente;
			objFiltros.tpFrete = dataGrid.tpFrete;
			objFiltros.idDivisaoCliente = dataGrid.idDivisaoCliente;
			objFiltros.novaFatura = true;
		} else if (rowRef.children[0].children[0].checked == true && devedorDocServFatGridDef.getSelectedIds().ids[0] != undefined) {
			 if (objFiltros.tpDocumentoServico != dataGrid.tpDocumentoServico.substr(0,3) ||
				objFiltros.idMoeda != dataGrid.idMoeda ||
				objFiltros.idServico != dataGrid.idServico ||
				objFiltros.tpModal != dataGrid.tpModal ||
				objFiltros.tpAbrangencia != dataGrid.tpAbrangencia ||
				objFiltros.idCliente != dataGrid.idCliente ||
				objFiltros.idCliente != dataGrid.idCliente ||
				objFiltros.idDivisaoCliente != dataGrid.idDivisaoCliente){
				rowRef.firstChild.firstChild.checked = false;
				alertI18nMessage("LMS-36008");
				return false;
			}
		}
		return true;
	}

	/**
	*	Após a busca padrão do combo de Tipo de Documento de Serviço seta a descrição do tipo no campo hidden tpDocumentoHidden
	*
	*/
	function onChangeTpDocumentoServico() {
		var comboTpDocumentoServico = document.getElementById("doctoServico.tpDocumentoServico");
		setElementValue('tpDocumentoHidden', comboTpDocumentoServico.options[comboTpDocumentoServico.selectedIndex].text);
		return changeDocumentWidgetType({
									   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"),
									   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
									   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
										 parentQualifier:'doctoServico',
										 documentGroup:'DOCTOSERVICE',
									   actionService:'lms.contasreceber.pesquisarDevedorDocServFatLookUpAction'
					   });		
	}
	
	function onChangeFilial(){
		return changeDocumentWidgetFilial({
										 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
										 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')									 
										 });	
	}

	function myFindButton(){
		if ((getElementValue("notaInicial") == "" || getElementValue("notaFinal") == "") && (getElementValue("doctoServicoInicial") == "" || getElementValue("doctoServicoFinal") == "") && getElementValue("doctoServico.idDoctoServico") == ""){
			alertI18nMessage("LMS-36226");
		} else {
			findButtonScript('devedorDocServFat', document.forms[0]);
		}
	}
	
		/**
	*	Busca a filial origem e habilita a lookup de documento serviço
	*
	*/	
	function enableDevedorDocServFatDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}

	function adicionarScript(){
		if (devedorDocServFatGridDef.getSelectedIds().ids[0] == undefined) {
			return;
		}
		
		var dataAcao = buildFormBeanFromForm(document.getElementById("documentos.form"));
		var dataGrid = devedorDocServFatGridDef.getSelectedIds();
		var data = new Array();			

		merge(data,dataAcao);
		merge(data,dataGrid);
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterPreFaturasAction.storeItemFatura", "adicionarScript", data)); 
		xmit(true); // deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
	}
	
	function adicionarScript_cb(d,e,c,x){
		onDataLoad_cb(d,e,c,x);
		
		if (e == undefined) {
			var dataGrid = devedorDocServFatGridDef.findById(devedorDocServFatGridDef.getSelectedIds().ids[0]);		
			var blRemontarFiltros = !(dialogArguments.window.itemFaturaGridDef.getRowCount() > 0);
			
			dialogArguments.window.itemFaturaGridDef.executeSearch(null, true);
			dialogArguments.window.populaSomatorios();
			dialogArguments.window.setValueTelaCad(dataGrid.tpModal, dataGrid.tpAbrangencia, dataGrid.idDivisaoCliente);
			
			if (blRemontarFiltros){
				objFiltros.tpDocumentoServico = dataGrid.tpDocumentoServico.substr(0,3);
				objFiltros.idMoeda = dataGrid.idMoeda;
				objFiltros.idServico = dataGrid.idServico;
				objFiltros.tpModal = dataGrid.tpModal;
				objFiltros.tpAbrangencia = dataGrid.tpAbrangencia;
				objFiltros.tpFrete = dataGrid.tpFrete;
				objFiltros.idDivisaoCliente = dataGrid.idDivisaoCliente;

				
				setElementValue("idMoeda", objFiltros.idMoeda);
				setElementValue("tpModal", objFiltros.tpModal);
				setElementValue("tpAbrangencia", objFiltros.tpAbrangencia);
				setElementValue("idServico", objFiltros.idServico);
				setElementValue("idDivisaoCliente", objFiltros.idDivisaoCliente);
				
				if (dataGrid.tpFrete != undefined && dataGrid.tpFrete != ""){
					setElementValue("tpFrete", objFiltros.tpFrete.substr(0,1));
				}
				setElementValue("doctoServico.tpDocumentoServico", objFiltros.tpDocumentoServico);
			}
			
			getTab(dialogArguments.window.document).setChanged(true);
			myFindButton();
		}
	}
	
	
	
	function myOnPageLoad_cb(d,e){
		onPageLoad_cb(d,e);

		setData();
		
		setMasterLink(document, true);
		
		if (getElementValue("doctoServico.tpDocumentoServico") != ""){
			getElement("doctoServico.tpDocumentoServico").onchange();
		}
	}
	
	function setData(){
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.findDataAtual", "setData")); 
		xmit(false);
	}
	
	function setData_cb(d,e,c){
		if (d != null){
			setElementValue("dtEmissaoFinal", setFormat(getElement("dtEmissaoFinal"), d._value));
		}
	}		

	function rowClick(){
		return false;
	}		
	
	setDisabled("adicionar", true);
</script>