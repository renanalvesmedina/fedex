<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
	
		document.getElementById("reset").disabled = false;
		document.getElementById("controleCarga.nrControleCarga").disabled = true;
	
    	var data = new Array();
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.getBasicData", "loadBasicData", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var basicData;
	function loadBasicData_cb(data, error) {
		basicData = data;
		fillBasicData();
		onPageLoad();
	}
</script>

<adsm:window service="lms.carregamento.cancelarFimCarregamentoAction" onPageLoad="loadDataObjects">
	<adsm:form action="/carregamento/cancelarFimCarregamento" idProperty="idCarregamentoDescarga" id="formCad">
		
		<adsm:hidden property="idFilial"/>
		<adsm:textbox dataType="text" property="sgFilial" label="filial" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoa" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="idPostoAvancado"/>
		<adsm:textbox dataType="text" property="sgFilialPostoAvancado" 
					  label="postoAvancado" maxLength="3" size="3" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoaPostoAvancado" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />				
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.cancelarFimCarregamentoAction.findLookupBySgFilial" action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" onDataLoadCallBack="disableNrControleCarga"
					 popupLabel="pesquisarFilial"
					 label="controleCargas" labelWidth="18%" width="32%" size="3" maxLength="3" picker="false" serializable="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.carregamento.cancelarFimCarregamentoAction.findCarregamentoDescargaByNrControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="loadDataByNrControleCarga" onDataLoadCallBack="loadDataByNrControleCarga" onchange="return checkValueControleCarga(this.value)"
						 popupLabel="pesquisarControleCarga"
						 maxLength="8" size="8" mask="00000000" required="true">
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"/>
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial"/>
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
 				 <adsm:propertyMapping modelProperty="tpControleCarga" criteriaProperty="tpControleCarga" disable="true" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:textbox property="tpControleCarga" dataType="text"
					   label="tipo" labelWidth="18%" width="32%" disabled="true"/> 

		<adsm:textbox property="tpStatusControleCarga" dataType="text"
					   label="status" labelWidth="18%" width="32%" size="36" disabled="true"/>
		
		<adsm:textbox property="dhEvento" dataType="JTDateTimeZone" 
					  label="chegadaPortaria" labelWidth="18%" width="32%" disabled="true"/>

		<adsm:textbox property="nrFrotaTransporte" dataType="text" 
					  label="meioTransporte" maxLength="6" size="6" labelWidth="18%" width="32%" disabled="true">
			<adsm:textbox property="nrIdentificadorTransporte" 
						  dataType="text" maxLength="25" size="25" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox property="nrFrotaSemiReboque" dataType="text" 
					  label="semiReboque" size="6" maxLength="6" labelWidth="18%" width="32%" disabled="true">
			<adsm:textbox property="nrIdentificadorSemiReboque" 
						  dataType="text" maxLength="25" size="25" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox property="dhInicioOperacao" dataType="JTDateTimeZone" label="inicioCarregamento" labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:textbox property="dhFimOperacao" dataType="JTDateTimeZone" label="fimCarregamento" labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:hidden property="inicioCarregamento" value="false"/>
		<adsm:hidden property="tpControleCargaValue"/> 
		
		<adsm:textarea property="obCancelamento" 
				       label="observacao" labelWidth="18%" width="82%"
					   maxLength="200" columns="70" rows="4" required="true" />
		
		<script>
			function lms_05129() {
				var msg = "<adsm:label key='LMS-05129'/>";
				return msg;
			}
		</script>
		<adsm:buttonBar>
			<adsm:button id="btnSalvar" caption="cancelarFimCarregamentoLabel" onclick="salvar()" disabled="false"/>
			<adsm:button id="reset" caption="limpar" onclick="btnLimpar_click()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function initWindow(eventObj) {
		document.getElementById("obCancelamento").readOnly = true;
		if (eventObj.name == "tab_click") {
			setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
			disableButtons(false);
		} 
	}
	
	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	/**
	 * Persiste o registro.
	 *
	 * @param form
	 */
	function salvar() {
	
		if ((validateTabScript(document.getElementById("formCad")))==false) return false;
		
		if (confirm(lms_05129())) {
			storeButtonScript('lms.carregamento.cancelarFimCarregamentoAction.generateCancelarFimCarregamento', 'salvar', document.getElementById("formCad"));
		}
	}
	
	function salvar_cb(data, error) {
		if (error!=undefined) {
			alert(error);
			setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"), true);
		} else if (data.idManifestoEletronicoCancelado) {
			//LMS-3544
			verificaCancelamentoMdfe(data);
		} else {
			showSuccessMessage();
			resetView();
			setFocus(document.getElementById("btnLimpar"), true, true);
		}
	}
	
	function verificaCancelamentoMdfe(data) {
		var dataEnviar = new Object();
		dataEnviar.idManifestoEletronicoCancelado = data.idManifestoEletronicoCancelado;
		dataEnviar.dhEncerramento = data.dhEncerramento;
		showMessageMDFe();
		var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.verificaCancelamentoMdfe", "verificaCancelamentoMdfe", dataEnviar);
	 	xmit({serviceDataObjects:[sdo]});
	}
	
	function verificaCancelamentoMdfe_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			hideMessageMDFe();
			return false;
		}
		
		if(data.limiteEspera) {
			setTimeout(function() {
					unblockUI();
					verificaCancelamentoMdfe(data);
					return false;
				},
				data.limiteEspera*1000);
		} else {
			/* Libera a tela e some com o loader que tinha sido setado por tempo ilimitado. */
			hideMessageMDFe();
			showSuccessMessage();
			resetView();
			setFocus(document.getElementById("btnLimpar"), true, true);
		}
	}
	
	/* 	Exibe loader e bloqueia tela por tempo ilimitado. O mesmo só será removido na função 'hideMessageMDFe'. 
	Implementado dessa forma para garantir que tela não fique liberada nos intervalos das chamadas, e para que loader seja exibido durante toda a espera do processo de autorização da MDFe. */
	function showMessageMDFe(){
		blockUI();
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe == null || messageMDFe == undefined){
			showSystemMessage('processando', null, true);
			var messageSign = doc.getElementById("message.div");
			var messageMDFe = messageSign;
			messageMDFe.id = 'messageMDFe.div';
			doc.body.appendChild(messageMDFe);
		}
	}
	
	function hideMessageMDFe(){
		unblockUI();
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe != null && messageMDFe != undefined){
			doc.body.removeChild(messageMDFe)
		}		
	}
	
	/**
	 * Controla o objeto de controle carga
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			resetView();
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}
	
	function disableNrControleCarga_cb(data, error) {
		if (data.length==0){
			disableNrControleCarga(false);
		}
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	/**
	 * Carrega os dados da tela de carregarVeiculos apartir dos dados retornados da 
	 * consulta de 'findCarregamentoDescargaByNrControleCarga'
	 */
	function loadDataByNrControleCarga_cb(data, error){
		var result = controleCarga_nrControleCarga_exactMatch_cb(data);
		//Verifica se este objeto e nulo
		if (data[0]!=undefined) {
			setElementValue("idCarregamentoDescarga", data[0].idCarregamentoDescarga);
			setElementValue("controleCarga.idControleCarga", data[0].idControleCarga);
			setElementValue("controleCarga.nrControleCarga", data[0].nrControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data[0].idFilialControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data[0].sgFilialControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", data[0].nmFantasia);
			setElementValue("sgFilialPostoAvancado", data[0].sgFilialPostoAvancado);
			setElementValue("nmPessoaPostoAvancado", data[0].nmPessoaPostoAvancado);
			document.getElementById("tpControleCarga").value = data[0].tpControleCarga.description;
			document.getElementById("tpControleCargaValue").value = data[0].tpControleCarga.value;
			document.getElementById("tpStatusControleCarga").value = data[0].tpStatusControleCarga.description;
			setElementValue("dhEvento", setFormat(document.getElementById("dhEvento"), data[0].dhEvento));
			setElementValue("nrFrotaTransporte", data[0].nrFrotaTransporte);
			setElementValue("nrIdentificadorTransporte", data[0].nrIdentificadorTransporte);
			setElementValue("nrFrotaSemiReboque", data[0].nrFrotaSemiReboque);
			setElementValue("nrIdentificadorSemiReboque", data[0].nrIdentificadorSemiReboque);
			
			if (data[0].dhInicioOperacao!=undefined) {
				setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), data[0].dhInicioOperacao));
			}
			
			if (data[0].dhFimOperacao!=undefined){
				setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), data[0].dhFimOperacao));
			}
			
			//Formata o campo de nrControleCarga
			format(document.getElementById("controleCarga.nrControleCarga"));
			
			disableButtons(false);
			document.getElementById("obCancelamento").readOnly = false;
			setFocus(document.getElementById("obCancelamento"));
		} else {
			var idFilial = document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").value;
			var sgFilial = document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value;
			
			resetView();
			
			document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").value = idFilial;
			document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value = sgFilial;
			
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
		}
		return result;
	}
	
	function enableNrControleCarga(data){
		disableNrControleCarga(false);
	}
	
	function disableNrControleCarga(disable) {
		document.getElementById("controleCarga.nrControleCarga").disabled = disable;
	}
	
	/**
	 * Chama a consulta de 'findCarregamentoDescargaByNrControleCarga' a partir de um dos dados retornados 
	 * da lookup
	 */
	function loadDataByNrControleCarga(rowValues) {
		var data = new Array();
		
		data.nrControleCarga = rowValues.nrControleCarga;
		setNestedBeanPropertyValue(data, "filialByIdFilialOrigem.idFilial", rowValues.filialByIdFilialOrigem.idFilial);
		
		var sdo = createServiceDataObject("lms.carregamento.cancelarFimCarregamentoAction.findCarregamentoDescargaByNrControleCarga", "loadDataByNrControleCarga", data);
    	xmit({serviceDataObjects:[sdo]});
    	
	}
	
	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueControleCarga(valor) {
		var result = controleCarga_nrControleCargaOnChangeHandler();
		if (valor=="") {
			var idFilial = getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
			var sgFilial = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");
			resetView();
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
		}
		
		return result;
	}

	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Carrega os dados basicos da tela
	 */
	function fillBasicData() {
		setElementValue("idFilial", basicData.filial.idFilial);
		setElementValue("sgFilial", basicData.filial.sgFilial);
		setElementValue("nmPessoa", basicData.filial.pessoa.nmFantasia);
	}
	
	function btnLimpar_click(){
		resetView();
		setDisabled("controleCarga.nrControleCarga", true);
		setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
	}
	
	/**
	 * Reseta a tela deixa
	 */
	function resetView(){
		resetValue(this.document);
		disableButtons(true);
		document.getElementById("obCancelamento").readOnly = true;
		fillBasicData();
	}
	
	/**
	 * Controla a propriedade de disable dos botoes da tela
	 */
	function disableButtons(disable) {	
		setDisabled("btnSalvar", disable);
	}
	
	/**
	 * Re-carrega os dados da tela de carregamento
	 */
	function reloadDataCarregamento() {
		
		var controleCarga = new Array();
		setNestedBeanPropertyValue(controleCarga, "filialByIdFilialOrigem.idFilial", getElementValue("controleCarga.filialByIdFilialOrigem.idFilial"));
		setNestedBeanPropertyValue(controleCarga, "nrControleCarga", getElementValue("controleCarga.nrControleCarga"));
		
		var sdo = createServiceDataObject("lms.carregamento.cancelarFimCarregamentoAction.findCarregamentoDescargaByNrControleCarga", "loadDataByNrControleCarga", controleCarga);
	    xmit({serviceDataObjects:[sdo]});
	}	
		
</script>