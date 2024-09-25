<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.seguros.manterProcessosSinistroAction">
	<adsm:form action="/seguros/manterProcessosSinistro" idProperty="idSinistroDoctoServico">
	
		<adsm:masterLink idProperty="idProcessoSinistro" showSaveAll="true" >
			<adsm:masterLinkItem property="nrProcessoSinistro" label="numeroProcesso" />
		</adsm:masterLink>
		
		<adsm:hidden property="nrProcessoSinistro"  serializable="false"/> 
		<adsm:hidden property="doctoServico.vlMercadoria"  serializable="false" />
		
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" labelWidth="15%" width="35%"
					   service="lms.seguros.manterProcessosSinistroAction.findTpDoctoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						   parentQualifier:'',
						   documentGroup:'SERVICE',
						   actionService:'lms.seguros.manterProcessosSinistroAction'
					   });"
					   > 

			<adsm:lookup dataType="text"
					 property="doctoServico.filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial"
					 service=""
					 disabled="true"
					 action=""
					 size="3" maxLength="3" picker="false" onDataLoadCallBack="enableDoctoServico"
					 onchange="return changeDocumentWidgetFilial({
						 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						 });"
					 />

			<adsm:lookup dataType="integer"
					 property="doctoServico"
					 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
					 service=""
					 action=""
					 required="true"
					 onDataLoadCallBack="retornoDocumentoServico" popupLabel="pesquisarDocumentoServico"
					 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />

		</adsm:combobox>
		
		<adsm:combobox property="tpPrejuizo" label="tipoPrejuizo" labelWidth="12%" width="15%" 
			domain="DM_TIPO_PREJUIZO" required="true" renderOptions="true" defaultValue="S" onchange="tpPrejuizo_Onchange();"/>
		
		<adsm:textbox property="vlPrejuizo" label="valorPrejuizo" labelWidth="15%" width="35%" dataType="currency"
		size="10" maxLength="16" mask="###,###,###,###,##0.00" required="true" value="0,00" disabled="true"
		onblur="validateValorPrejuizo();"
		/>
		
		<adsm:combobox property="blPrejuizoProprio" label="prejuizoProprio" labelWidth="12%" width="15%" 
			domain="DM_SIM_NAO" required="true" renderOptions="true" defaultValue="N" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="button" caption="selecionarVariosDocumentos" onclick="exibirPopup();"/>
			<adsm:button buttonType="button" caption="storeGrid" onclick="storeEditGridScript();"/>
			<adsm:button buttonType="button" caption="salvarItem" onclick="validateDocumento();"/>
			<adsm:button buttonType="resetButton" id="btnLimpar" caption="limpar" disabled="false" onclick="resetForm(this.document)"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-22038"/>
		</adsm:i18nLabels>
		
	</adsm:form>
	
	<adsm:grid property="sinistroDoctoServico" 
				idProperty="idSinistroDoctoServico" 
				service="lms.seguros.manterProcessosSinistroAction.findPaginatedDocumentos" 
				rowCountService="lms.seguros.manterProcessosSinistroAction.getRowCountDocumentos" 
				selectionMode="true"
				detailFrameName="documentos"
				scrollBars="horizontal"
				autoSearch="false"
				onDataLoadCallBack="documentos_OnDataLoadCallBack"
				onPopulateRow="documentos_OnPopulateRow"
				onRowClick="documentos_OnClick"
				onPopulateRow="documentos_OnPopulateRow"
				gridHeight="175"
				defaultOrder="doctoServico.tpDocumentoServico:asc, doctoServico.filialByIdFilialOrigem.sgFilial:asc, doctoServico.nrDoctoServico:asc"
				rows="6">

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn property="doctoServico.tpDocumentoServico" isDomain="true" title="documentoServico" dataType="text"  width="40"/>
			<adsm:gridColumn property="doctoServico.filialByIdFilialOrigem.sgFilial" dataType="text" title="" width="40" />
			<adsm:gridColumn property="doctoServico.nrDoctoServico" dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="destino" property="doctoServico.filialByIdFilialDestino.sgFilial" width="80"/>
		
		<adsm:gridColumn title="dataEmissao" property="doctoServico.dhEmissao" width="130" align="center" dataType="JTDateTimeZone"/>
		
		<adsm:editColumn title="tipoPrejuizo" property="tpPrejuizo" value="tpPrejuizo" field="combobox" disabled="true" align="center" width="100" domain="DM_TIPO_PREJUIZO"/>
		
		<adsm:editColumn title="valorPrejuizo" property="vlPrejuizo" field="textbox" width="120" dataType="currency"/>
		
		<adsm:editColumn title="prejuizoProprio" property="blPrejuizoProprio" value="blPrejuizoProprio" field="combobox" disabled="true" align="center" width="85" domain="DM_SIM_NAO"/>
		
		<adsm:gridColumn title="valorMercadoria" property="doctoServico.vlMercadoria" width="100" dataType="currency"/>
		
		<adsm:editColumn title="hidden" property="vlMercadoriaHidden" dataType="text" field="hidden" width="" />
		
		<adsm:gridColumn title="vol." property="doctoServico.qtVolumes" width="80" dataType="integer"/>
		
		<adsm:gridColumn title="pesoKG" property="peso" width="80" dataType="decimal" align="right" unit="kg" mask="###,###,###,##0.000"/>
		
		<adsm:gridColumn title="remetente" property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" width="150"/>
		
		<adsm:gridColumn title="destinatario" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" width="150"/>
		
		<adsm:gridColumn title="devedor" property="doctoServico.devedorDocServs.cliente.pessoa.nmPessoa" width="150"/>
		
		<adsm:gridColumn title="dataEmissaoRim" property="doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.dtEmissao" width="130" align="center" dataType="JTDate"/>
		
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.filial.sgFilial" title="rim" dataType="text"  width="30"/>
			<adsm:gridColumn property="doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.nrReciboIndenizacao" dataType="text" title="" width="30" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="situacaoRim" property="doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.tpStatusIndenizacao" width="80" isDomain="true"/>
		
		<adsm:gridColumn title="valorRIM" property="doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.vlIndenizacao" width="80" dataType="currency"/>
		
		<adsm:gridColumn title="dataPagamento" property="doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.dtPagamentoEfetuado" width="100" align="center" dataType="JTDate"/>
		
		<adsm:gridColumn title="dataCartaComunicado" property="dataCartaComunicado" width="130" align="center" dataType="text"/>
		
		<adsm:gridColumn title="dataCartaAtualizacao" property="dataCartaAtualizacao" width="130" align="center" dataType="text"/>
		
		<adsm:gridColumn title="dataCartaEmissaoRim" property="dataCartaEmissaoRim" width="130" align="center" dataType="text"/>
				
		<adsm:buttonBar>
			<adsm:button caption="excluirItem" buttonType="removeButton" onclick="removeItem();"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");
	var idProcessoSinistro;
	var gridSelectedIds = null;
	
	function initWindow(evento) {
		idProcessoSinistro = tabGroup.getTab("cad").getElementById("idProcessoSinistro").value
		
		if (evento.name == "newButton_click" || evento.name=="tab_click" ) {
			if (evento.name == "newButton_click" || (evento.name=="tab_click" && evento.src.tabGroup.oldSelectedTab.properties.id=="pesq")) { 
				newMaster();
				resetForm(this.document);
			}
		}
	}
	
	// Salvar item - INÍCIO
	
	function validateDocumento(ids, isPopup) {
		var vlPrejuizo;
		var tpPrejuizo;

		if(!isPopup) {
			// Valida os campos obrigatórios antes da validação do documento de serviço
			if (!validateForm(this.document.forms[0])) {
				return false;
			}
			
			if(ids == undefined) {
				ids = new Array(getElementValue("doctoServico.idDoctoServico"));
			}
			
			vlPrejuizo = getElementValue("vlPrejuizo");
			tpPrejuizo = getElementValue("tpPrejuizo");
			isPopup = false;
		} else {
			vlPrejuizo = 0;
			tpPrejuizo = "S"; // SEM PREJUÍZO como padrão 
		}
		
		/* Caso não seja informado o parâmetro 'idDoctoServico', 
		* atribui o valor do documento de serviço do form
		*/

		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.validateDocumentoServico", "validateDocumento", 
			{idProcessoSinistro:idProcessoSinistro, idsDoctoServico:ids, vlPrejuizo:vlPrejuizo, tpPrejuizo:tpPrejuizo, isPopup:isPopup });
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateDocumento_cb(data, error) {
		
		if(error!=undefined) {
			alert(error);
		} else {
			for(var i = 0; i < data.messages.length; i++) {
				if(!confirm(data.messages[i])) {
					return false;
				}
			}
			
			if(data.isPopup == "true") {
				var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.storeDocumentosPopup", "storeDocumentosPopup", 
					{idProcessoSinistro:idProcessoSinistro, idsDoctoServico:data.idsDoctoServico });
			
				xmit({serviceDataObjects:[sdo]});
			} else {
				storeButtonScript("lms.seguros.manterProcessosSinistroAction.storeDocumento", "storeItem", this.document.forms[0]);
			}
		}
	}
	
	function storeItem_cb(data, error, eventObj){
		if(error == undefined){
			sinistroDoctoServicoGridDef.executeSearch({idProcessoSinistro:idProcessoSinistro}, true);
			resetForm(this.document);
			getTabGroup(this.document).getTab("documentos").itemTabChanged = false;
			tabGroup.getTab("documentos").changed = true;
		}else{
			alert(error);
		}
	}
	
	function storeDocumentosPopup_cb(data, error, eventObj){
		if(error == undefined){
			sinistroDoctoServicoGridDef.executeSearch({idProcessoSinistro:idProcessoSinistro}, true);
			getTabGroup(this.document).getTab("documentos").itemTabChanged = false;
		}else{
			alert(error);
		}
	}
	
	// Salvar item - FIM
	
	// Excluir item- INÍCIO
	
	function removeItem(){
		var mapCriteria = new Array();	   
		setNestedBeanPropertyValue(mapCriteria, "ids", sinistroDoctoServicoGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.removeByIdsDocumento", "removeItem", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function removeItem_cb(data, error, eventObj){
		if(error == undefined){
			sinistroDoctoServicoGridDef.executeSearch({idProcessoSinistro:idProcessoSinistro}, true);
		}else{
			alert(error);	
		}
	}
	
	// Excluir item- FIM
	
	// Eventos da grid - INÍCIO
	
	function documentos_OnClick(id) {
		return false;
	}
	
	function documentos_OnPopulateRow(tr, data) {
	
		var id = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".id");
		var objTipoPrejuizo = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".tpPrejuizo");
		var	valorPrejuizo = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".vlPrejuizo");
		var	valorMercadoria = document.getElementById("sinistroDoctoServico:"+tr.rowIndex+".vlMercadoriaHidden");		
	
		// Registro estiver persistido
		if(id.value > 0) {
	
			objTipoPrejuizo.onchange = function(){
	
				if (objTipoPrejuizo.value=="S"){
					setDisabled(valorPrejuizo, true);
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, "0"));
				} else if (objTipoPrejuizo.value=="P"){
					setDisabled(valorPrejuizo, false);
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, "0"));
					setFocus(valorPrejuizo);
				} else if (objTipoPrejuizo.value=="T"){
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, valorMercadoria.value));
					setDisabled(valorPrejuizo, true);
				}
			};
			
			valorPrejuizo.onblur = function(){
				
				if(getElementValue(valorPrejuizo)==""){
					setElementValue(valorPrejuizo, setFormat(valorPrejuizo, "0"));
				} else {
					if (objTipoPrejuizo.value=="P" &&
						parseFloat(valorPrejuizo.value.replace('.', '').replace(',','.')) > 
						(valorMercadoria.value == "" ? 0 : parseFloat(valorMercadoria.value)) || parseFloat(valorPrejuizo.value.replace('.', '').replace(',','.')) <= 0){
						setElementValue(valorPrejuizo, setFormat(valorPrejuizo, "0"));
						alert("LMS-22038 - " + i18NLabel.getLabel("LMS-22038"));
					}
				}
			};
		}
	}
	
	function documentos_OnDataLoadCallBack_cb(data, error) {
			
		var gridDef = document.getElementById("sinistroDoctoServico.dataTable").gridDefinition;
		var id;
		var objTipoPrejuizo;
		var objValorPrejuizo;
		
		for(var i = 0; i < gridDef.currentRowCount; i++) {
			
			id = document.getElementById("sinistroDoctoServico:"+i+".id");
			objTipoPrejuizo = document.getElementById("sinistroDoctoServico:"+i+".tpPrejuizo");
			valorPrejuizo = document.getElementById("sinistroDoctoServico:"+i+".vlPrejuizo");

			if(id.value > 0) {
				if (objTipoPrejuizo.value=="S"){
					setDisabled(valorPrejuizo, true);
				} else if (objTipoPrejuizo.value=="P"){
					setDisabled(valorPrejuizo, false);
				} else if (objTipoPrejuizo.value=="T"){
					setDisabled(valorPrejuizo, true);
				}
			}  else {
				setDisabled(objTipoPrejuizo, true);
				setDisabled(valorPrejuizo, true);
			}
		}
	}
	
	// Eventos da grid - FIM
	
	// Salvar alterações da grade - INÍCIO
	
	function storeEditGridScript() {
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // não deseja que o alert seja exibido, a função store_cb irá mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject('lms.seguros.manterProcessosSinistroAction.storeGridDocumentos', 'storeGridDocumentos', editGridFormBean(document.forms[1], undefined));
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
		return true;
	}

	function storeGridDocumentos_cb(data, error) {
		if(error != undefined){
			alert(error);
			return;
		}
		// Limpa a flag que indica se houve alterações nos dados grid editavel
		sinistroDoctoServicoGridDef.gridState.fieldsChanged = false;
		
		// Limpa a flag que indica se houve alterações nos dados da tab
		getTabGroup(this.document).getTab("documentos").changed = false
		
		showSuccessMessage();
	}
	
	// Salvar alterações da grade - FIM
	
	// Selecionar vários documentos - INÍCIO
	
	function exibirPopup(){
		showModalDialog("/seguros/manterProcessosSinistroDocumentosVariosDocumentos.do?cmd=list" ,
		 window, "unardorned:no;scroll:yes;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:480px;");
	}
	
	function setGridSelectedIds(ids) {
		if (ids != null) {
			// Chama rotina de validacao
			validateDocumento(ids.ids, true);
		}
	}

	function getGridSelectedIds() {
		return this.gridSelectedIds;
	}

	function isNotExistent(id) {
		for (var i = 0; gridSelectedIds.ids.length > i; i++) {
			if (gridSelectedIds.ids[i] == id) {
				return false;
			}
		}
		return true;
	}
	
	// Selecionar vários documentos - FIM
	
	// Combobox Documento de Serviço - INÍCIO
	
	function enableDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}
	
	function retornoDocumentoServico_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
		
		if(data != undefined && data[0] != undefined) {	
			setElementValue("doctoServico.vlMercadoria", data[0].vlMercadoria);
			
			if(getElementValue("tpPrejuizo") == "T") {
				setElementValue("vlPrejuizo", setFormat(valorPrejuizo, getElementValue("doctoServico.vlMercadoria")));
				setDisabled("vlPrejuizo", true);
			}
		}
	}
	
	// Combobox Documento de Serviço - FIM
	
	// Combobox Tipo de prejuízo - INÍCIO
	
	function tpPrejuizo_Onchange() {
		if(getElementValue("tpPrejuizo") == "S") {
			setElementValue("vlPrejuizo", "0,00");
			setDisabled("vlPrejuizo", true);
		} else if(getElementValue("tpPrejuizo") == "T") {
			if(getElementValue("doctoServico.vlMercadoria") != "" && 
				getElementValue("doctoServico.nrDoctoServico") != "") {
				setElementValue("vlPrejuizo", setFormat(valorPrejuizo, getElementValue("doctoServico.vlMercadoria")));
			} else {
				resetValue("vlPrejuizo");
			}
			setDisabled("vlPrejuizo", true);
		} else {
			setDisabled("vlPrejuizo", false);
		}
	}
	
	function validateValorPrejuizo() {
		if(getElementValue("tpPrejuizo") == "P" || getElementValue("tpPrejuizo") == "T") {
			if(parseFloat(getElementValue("vlPrejuizo")) > 
			(getElementValue("doctoServico.vlMercadoria") == "" ? 0 : parseFloat(getElementValue("doctoServico.vlMercadoria"))) ||
				getElementValue("vlPrejuizo") <= 0) {
				alert("LMS-22038 - " + i18NLabel.getLabel("LMS-22038"));
				resetValue("vlPrejuizo");
			}
		}
	}
	
	// Combobox Tipo de prejuízo - FIM
	
	// Limpeza dos campos - INÍCIO
	
	function newMaster() {
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.newMaster");
		xmit({serviceDataObjects:[sdo]});
	}
	
	function resetForm(doc){
		/** Reseta todos campos da tela */
		resetValue("doctoServico.tpDocumentoServico");
		resetValue("doctoServico.filialByIdFilialOrigem.sgFilial");
		resetValue("doctoServico.nrDoctoServico");
		resetValue("doctoServico.vlMercadoria");
		resetValue("tpPrejuizo");
		resetValue("vlPrejuizo");
		
		/** Coloca o focus no primeiro campo da tela */
		setFocusOnFirstFocusableField(doc);
		
		/** Habilita/Desabilita os campos */
		setDisabled("btnLimpar", false);
		setDisabled("doctoServico.filialByIdFilialOrigem.sgFilial", true);
		setDisabled("doctoServico.nrDoctoServico", true);
		setDisabled("vlPrejuizo", true);
	}
	
	// Limpeza dos campos - FIM
</script>