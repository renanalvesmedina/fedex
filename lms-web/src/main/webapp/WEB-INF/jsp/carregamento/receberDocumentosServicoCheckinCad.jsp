<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">

	function pageLoad() {	
	
		document.getElementById("idFilial").masterLink = "true";
		document.getElementById("sgFilial").masterLink = "true";
		document.getElementById("nmFantasiaFilial").masterLink = "true";
		
    	var data = new Array();
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.getBasicData", "loadBasicData", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function loadBasicData_cb(data, error) {
		document.getElementById("idFilial").value = data.idFilial;
		document.getElementById("sgFilial").value = data.sgFilial;
		document.getElementById("nmFantasiaFilial").value = data.nmFantasiaFilial;
		onPageLoad();
	}
</script>

<adsm:window title="receberDocumentosServicoCheckin" service="lms.carregamento.receberDocumentosServicoCheckinAction" onPageLoad="pageLoad">
<script language="javascript" src="../lib/ctrcVerifierServiceDocumentWidgetDefinitions.js"></script>
	<adsm:form action="/carregamento/receberDocumentosServicoCheckin" idProperty="idDoctoServico" id="formDoctoServicoCheckInCad">

		<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia"/>
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" width="85%"
					   service="lms.carregamento.receberDocumentosServicoCheckinAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="onChangeTpDocumentoServico();"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true" popupLabel="pesquisarFilial"
						 action=""
						 size="3" maxLength="3" picker="false" onDataLoadCallBack="enableDoctoServico"
						 onchange="return changeDocumentWidgetFilial({
						     documentTypeElement:document.getElementById('doctoServico.tpDocumentoServico'), 
						     documentGroup:'SERVICE',
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
							 verifierDigitElement:document.getElementById('doctoServico2.idDoctoServico')
							 });"
						 />
			
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						 service="" 
						 action="" required="true"
						 onPopupSetValue="popupRetornoDocumentoServico"
						 onDataLoadCallBack="retornoDocumentoServico"
						 popupLabel="pesquisarDocumentoServico"
						 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" >

			<adsm:lookup dataType="integer"
						 property="doctoServico2"
						 idProperty="idDoctoServico" criteriaProperty="dvConhecimento"
						 service=""
						 action="" popupLabel="pesquisarDocumentoServico"
						 onPopupSetValue="popupRetornoDocumentoServico"
						 onDataLoadCallBack="retornoDvDocumentoServico"
						 onchange="return changeDocumentWidgetVerifierDigit({verifierDigitElement:document.getElementById('doctoServico2.idDoctoServico'),
						 										 documentNumberCriteriaElement:document.getElementById('doctoServico.nrDoctoServico')});"
						 size="1" maxLength="1" serializable="true" mask="0" style="display:none" pickerStyle="display:none"/>
			</adsm:lookup>						 
						 
		</adsm:combobox>
		
		<adsm:lookup label="preManifesto"
				 width="75%" size="3" maxLength="3" 
				 picker="false" serializable="false"
    			 dataType="text" 
    			 property="manifesto.filialByIdFilialOrigem"
    			 idProperty="idFilial" 
    	         criteriaProperty="sgFilial" 
		         service="lms.carregamento.receberDocumentosServicoCheckinAction.findLookupFilial"
		         action="/municipios/manterFiliais"
		         popupLabel="pesquisarFilial"
			     onchange="return sgFilialOnChangeHandler(this.value);" 
			     >
			<adsm:lookup dataType="integer"
			    		 property="manifesto" 
			             idProperty="idManifesto" 
			             criteriaProperty="nrPreManifesto"
				         service="lms.carregamento.receberDocumentosServicoCheckinAction.findLookupPreManifesto" 
				         action="carregamento/manterGerarPreManifesto" 
				         cmd="list"
				         popupLabel="pesquisarPreManifesto"
				         onDataLoadCallBack="loadManifesto"
				         onPopupSetValue="loadManifestoPopUp"
				         disabled="false"
				         mask="00000000">				
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="manifesto.filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="manifesto.filialByIdFilialOrigem.sgFilial"/>
			</adsm:lookup>
		</adsm:lookup>

		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="sgFilial" serializable="false"/>
		<adsm:hidden property="nmFantasiaFilial" serializable="false"/>
		<adsm:hidden property="vigentes" value="S"/>
		<adsm:hidden property="update"/>

		<adsm:lookup label="rota" width="85%"
					 mask="0000"
		        	 size="4" 
		        	 maxLength="4" 
					 exactMatch="true"
					 dataType="integer" 
					 property="rotaIdaVolta" 
					 idProperty="idRotaIdaVolta" 
					 criteriaProperty="nrRota"
					 onPopupSetValue="loadRotaIdaVoltaPopUp"
					 onchange="return nrRotaOnChangeHandler(this.value);"
					 onDataLoadCallBack="loadRotaIdaVolta"
					 service="lms.carregamento.receberDocumentosServicoCheckinAction.findLookupRotaIdaVolta"
					 action="/municipios/consultarRotas"
					 cmd="idaVolta">
 		    <adsm:propertyMapping modelProperty="rota.dsRota" relatedProperty="rotaIdaVolta.rota.dsRota" />
 		    
 		    <adsm:propertyMapping modelProperty="filialOrigem.idFilial" criteriaProperty="idFilial" disable="false"/>
 		    <adsm:propertyMapping modelProperty="filialOrigem.sgFilial" criteriaProperty="sgFilial" disable="false"/>
 		    <adsm:propertyMapping modelProperty="filialOrigem.nmFilial" criteriaProperty="nmFantasiaFilial"/>
 		    
 		    <adsm:propertyMapping modelProperty="vigentes" criteriaProperty="vigentes" />
 		    
        	<adsm:textbox dataType="text" property="rotaIdaVolta.rota.dsRota" disabled="true" size="30" serializable="false"/>
        </adsm:lookup>
         
        <adsm:textbox dataType="JTTime" property="hrSaida" label="horaSaida" disabled="true" serializable="false"/>
        
		<script>
			function lms_05081(alertNumber) {
				alert("<adsm:label key='LMS-05081'/>");
			}
			function lms_05082(alertNumber) {
				alert("<adsm:label key='LMS-05082'/>");
			}
		</script>
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="preManifesto"	id="btnPreManifesto" action="/carregamento/manterGerarPreManifesto" cmd="main">
				<adsm:linkProperty src="manifesto.filialByIdFilialOrigem.idFilial" target="filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="manifesto.filialByIdFilialOrigem.sgFilial" target="filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="manifesto.nrPreManifesto" target="nrPreManifesto"/>
				<adsm:linkProperty src="rotaIdaVolta.idRotaIdaVolta" target="rotaIdaVolta.idRotaIdaVolta"/>
				<adsm:linkProperty src="rotaIdaVolta.nrRota" target="rotaIdaVolta.nrRota"/>
				<adsm:linkProperty src="rotaIdaVolta.rota.dsRota" target="rotaIdaVolta.rota.dsRota"/>
			</adsm:button>
			<adsm:button caption="salvar" id="btnSalvar" onclick="return validateStore();"/>
			<adsm:button caption="limpar" id="btnLimpar" buttonType="newButton" onclick="limpaTela()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function initWindow(eventObj) {
		setDisabled("btnPreManifesto", false);
		setDisabled("btnSalvar", false);
		
		if (eventObj.name=="tab_click"){
			setDisabled("doctoServico.tpDocumentoServico", false);
			setDisabled("manifesto.filialByIdFilialOrigem.idFilial", false);
			setDisabled("rotaIdaVolta.idRotaIdaVolta", false);
			setFocusOnFirstFocusableField(document);
		
		} else if(eventObj.name=="gridRow_click") {
			setDisabled("manifesto.idManifesto", false);
		}
	}
	
	/**
	 * Substitui a funcao padrao de 'onDataLoad'. Decorrente da necessidade de
	 * se ter como parametros para esta tela dois ids (idDoctoServico e idManifesto).
	 *
	 * @param idDoctoServico
	 * @param idManifesto
	 */
	function onDetailDataLoad(idDoctoServico, idManifesto) {
	
		var data = new Object();
		data.idDoctoServico = idDoctoServico;
		data.idManifesto = idManifesto;
		
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.findById", "onDetailDataLoad", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da rotina de 'onDetailDataLoad'.
	 *
	 * @param data
	 * @param error
	 */
	function onDetailDataLoad_cb(data, error) {
	
		if (data._exception==undefined) {
			setElementValue("doctoServico.tpDocumentoServico", data.tpDoctoServico);
			onChangeTpDocumentoServico();
			setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", data.idFilialDoctoServico);
			setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", data.sgFilialDoctoServico);
			setElementValue("doctoServico.idDoctoServico", data.idDoctoServico);
			setElementValue("doctoServico.nrDoctoServico", setFormat(document.getElementById("doctoServico.nrDoctoServico"), data.nrDoctoServico));
			setElementValue("doctoServico2.dvConhecimento", data.dvConhecimento);
			
			setDisabled("doctoServico.tpDocumentoServico", true);
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.filialByIdFilialOrigem.sgFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);
			setDisabled("doctoServico.nrDoctoServico", true);
						
			setElementValue("manifesto.filialByIdFilialOrigem.idFilial", data.idFilialManifesto);
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.sgFilialManifesto);
			setElementValue("manifesto.idManifesto", data.idManifesto);
			setElementValue("manifesto.nrPreManifesto", setFormat(document.getElementById("manifesto.nrPreManifesto"), data.nrPreManifesto));
			
			//Verifica se o doctoServico nao esta associado tanto a uma rota quanto a um preManifesto...
			var disable = false;
			if ((data.idManifesto!=undefined) || (data.idRotaIdaVolta!=undefined)) disable = true;
			setDisabled("manifesto.filialByIdFilialOrigem.idFilial", disable);
			setDisabled("manifesto.filialByIdFilialOrigem.sgFilial", disable);
			setDisabled("manifesto.idManifesto", disable);
				
			setElementValue("rotaIdaVolta.idRotaIdaVolta", data.idRotaIdaVolta);
			setElementValue("rotaIdaVolta.nrRota", data.nrRota);
			setElementValue("rotaIdaVolta.rota.dsRota", data.dsRota);	
				
			//Verifica se o doctoServico nao esta associado a um preManifesto...
			disable = false;
			if (data.idManifesto!=undefined) disable = true			
			setDisabled("rotaIdaVolta.idRotaIdaVolta", disable);
			setDisabled("rotaIdaVolta.nrRota", disable);			
			
			setElementValue("hrSaida", setFormat(document.getElementById("hrSaida"), data.hrSaida));
			
			//Flag...
			setElementValue("update", "true");
			setFocusOnFirstFocusableField(document);
		} else {
			alert(data._exception._message);
		}
	}

	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	function findDocumentos() {
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.findSumDoctoServicoByIdManifestoAndIdRotaIdaVolta", "findDocumentos", buildFormBeanFromForm(this.document.forms[0]));
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findDocumentos_cb(data, error) {		
		if (error!=undefined) {
			alert(error);
		} else if (data.valorTotal!=undefined) {		
		
			var valorTotal = data.sgMoeda + " " + data.dsSimbolo + " " + setFormat(document.getElementById("pesoTotal"), data.pesoTotal)
		
			setElementValue("volumeTotal", data.volumeTotal);
			setElementValue("pesoTotal", valorTotal);
			setElementValue("valorTotal", setFormat(document.getElementById("valorTotal"), data.valorTotal));
			
			findButtonScript('doctoServico', document.getElementById("formDocumentosServicoCheckin"));
		}
	}
	
	/**
	 * Faz a validacao dos dados a serem persistidos.
	 */
	function validateStore() {
		
		if ((validateTabScript(document.getElementById("formDoctoServicoCheckInCad")))==false) return false;
		
		if (getElementValue("update")!="true") {
			if ((getElementValue("rotaIdaVolta.idRotaIdaVolta")=="") && (getElementValue("manifesto.idManifesto")=="")) {
				lms_05082();
				if(getElementValue("manifesto.filialByIdFilialOrigem.idFilial")!=""){
					setFocus(document.getElementById("manifesto.nrPreManifesto"));
				} else {
					setFocus(document.getElementById("manifesto.filialByIdFilialOrigem.sgFilial"));
				}
				return false;
			}
		}

		var data = new Object();
		data.idDoctoServico = getElementValue("doctoServico.idDoctoServico");
		data.idManifesto = getElementValue("manifesto.idManifesto");
		data.idRotaIdaVolta = getElementValue("rotaIdaVolta.idRotaIdaVolta");
		
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.validateDocumentosServicoCheckin", "validateStore", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da funcao de validacao.
	 *
	 * @param data
	 * @param error
	 */
	function validateStore_cb(data, error) {
		if (error!=undefined) {
			if ((data._exception._key=="LMS-05085") || (data._exception._key=="LMS-05086")) {
				if (confirm(error)) {
					doStore();
				}
			} else {
				alert(error);
			}
			setFocus(document.getElementById("doctoServico.tpDocumentoServico"));
			return false;
		} else {
			doStore()
		}
	}
	
	/**
	 * 
	 *
	 */
	function doStore() {
		
		var data = new Object();
		
		//Este update e utilizado como flag para determinar que este registro esta sofrendo uma alteracao...
		if (getElementValue("update")!="") {
			data.update = getElementValue("update");
		} else {
			data.update = "false";
		}
		
		data.idDoctoServico = getElementValue("doctoServico.idDoctoServico");
		data.idManifesto = getElementValue("manifesto.idManifesto");
		data.idRotaIdaVolta = getElementValue("rotaIdaVolta.idRotaIdaVolta");
		
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.storeDocumentosServicoCheckin", "doStore", data);
		xmit({serviceDataObjects:[sdo]});
		
	}
	
	/**
	 * Callback do metodo store.
	 *
	 * @param data
	 * @param error
	 */
	function doStore_cb(data, error) {
		if (error!=undefined) {
			alert(error);
		} else {
			setElementValue("update", "true");
			showSuccessMessage();
			
		}
	}
	
	/**
	 * javaScripts para a 'tag documents'
	 */
	function enableDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}
	
	function onChangeTpDocumentoServico() {
		var result = changeDocumentWidgetType({
						   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"), 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						   documentNumberCriteriaElement:document.getElementById('doctoServico.nrDoctoServico'),
						   verifierDigitElement:document.getElementById('doctoServico2.idDoctoServico'),
						   documentGroup:'SERVICE',
						   actionService:'lms.carregamento.receberDocumentosServicoCheckinAction'
					   });
		return result;
	}
	
	/**
	 * Callback da doctoServico. Realiza uma pesquisa em cima do doctoServico capturado.
	 *
	 * @param data - dados retornados da pesquisa.
	 */
	function retornoDocumentoServico_cb(data) {
		var r = doctoServico_nrDoctoServico_exactMatch_cb(data);
		
		if (data.length==1) {
			validateDoctoServico(data[0].idDoctoServico);
		}
		if (getElementValue("doctoServico.tpDocumentoServico") == "CTR" && r == true) {
			//Verificar codigo, pois pode ser q nunca caia nesssa condicao.
	    	setDisabled("doctoServico2.idDoctoServico", false);
	    	setFocus(document.getElementById("doctoServico2.dvConhecimento"));
	   	}
		
	}
	
	function retornoDvDocumentoServico_cb(data) {
		var r = doctoServico2_dvConhecimento_exactMatch_cb(data);
		
		if (data.length==1) {
			validateDoctoServico(data[0].idDoctoServico);
		}
		if (getElementValue("doctoServico.tpDocumentoServico") == "CTR" && r == true) {
	    	setDisabled("doctoServico2.idDoctoServico", false);
	    	setFocus(document.getElementById("doctoServico2.dvConhecimento"));
	   	}
		
	}

	
	/**
	 * Callback da popup de doctoServico. Realiza uma pesquisa em cima do doctoServico capturado.
	 *
	 * @param data - dados retornados da pesquisa.
	 */
	function popupRetornoDocumentoServico(data){
		validateDoctoServico(data.idDoctoServico);
	}
	
	/**
	 * Valida o doctoServico selecionado.
	 *
	 * @param idDoctoServico
	 */
	function validateDoctoServico(idDoctoServico) {
		var data = new Object();
		data.idDoctoServico = idDoctoServico;
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.validateDoctoServico", "validateDoctoServico", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateDoctoServico_cb(data, error) {
		if (data._exception==undefined) {
			onDetailDataLoad(data.idDoctoServico, "");
		} else {
			alert(data._exception._message);
			resetValue("doctoServico.idDoctoServico");
			resetValue("doctoServico2.idDoctoServico");
			resetValue("doctoServico.nrDoctoServico");
			setFocus("doctoServico.nrDoctoServico", true, false, false);
		}
	}

	/**
	 * Controla o objeto de pre manifesto
	 */	
	function sgFilialOnChangeHandler(valor) {
		if (valor=="") {
			setDisabled(document.getElementById("manifesto.nrPreManifesto"), true);
			resetValue("manifesto.idManifesto");
		} else {
			setDisabled(document.getElementById("manifesto.nrPreManifesto"), false);
		}
		return manifesto_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	function nrRotaOnChangeHandler(valor){
		if (valor=="") {
			limpaPreManifesto();
		}
		return rotaIdaVolta_nrRotaOnChangeHandler();
	}
	
	function disableNrPreManifesto_cb(data, error) {
	
		var result = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	
		if (data.length==0) {
			setDisabled(document.getElementById("manifesto.nrPreManifesto"), false);
		} else {
			limpaRota();
		}
		
		return result;
	}
	
	function loadManifesto_cb(data, error) {
		if (error){
			alert(error);
		} else {
			if (data[0]!=undefined) {
				if (data[0].tpManifesto.value!="V") {
					lms_05081();
					resetValue("manifesto.nrPreManifesto");
					return false;
				}
			}
			
			var result = manifesto_nrPreManifesto_exactMatch_cb(data);
			if(result == true){
				limpaRota();
			}
			return result;
		}
	}
	
	function loadManifestoPopUp(data) {
		limpaRota();
		
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.sgFilialOrigem);

		if (data.tpManifesto.value!="V") {
			lms_05081();
			resetValue("manifesto.nrPreManifesto");
			return false;
		}
	}
	
	/*
	 * Controles do objeto de 'rotaIdaVolta'
	 */
	
	
	/**
	 * rotaIdaVolta callBack
	 *
	 * @param data
	 * @param error
	 */
	function loadRotaIdaVolta_cb(data, error){
		if (error){
			alert(error);
			return false;
		}
		var result = rotaIdaVolta_nrRota_exactMatch_cb(data);
		if (result==true) {
			if (data[0]){
				limpaPreManifesto();
				findManifestoByRotaIdaVolta(data[0].idRotaIdaVolta);
				findHrSaidaRota(data[0].idRotaIdaVolta);
			}
		}
	}
	
	function loadRotaIdaVoltaPopUp(data) {
		var idRotaIdaVolta = data.idRotaIdaVolta;
		findManifestoByRotaIdaVolta(idRotaIdaVolta);
		findHrSaidaRota(idRotaIdaVolta);
		limpaPreManifesto();
	}
	
	/**
	 * Busca por um manifesto a partir da rotaIdaVolta informada...
	 */
	function findManifestoByRotaIdaVolta(idRotaIdaVolta) {
		var data = new Object();
		data.idRotaIdaVolta = idRotaIdaVolta;
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.findManifestoRota", "findManifestoByRotaIdaVolta", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Callback da funcao de findManifestoByRotaIdaVolta...
	 *
	 * @param data
	 * @param error
	 */
	function findManifestoByRotaIdaVolta_cb(data, error) {
		if (error){
			alert(error);
		} else if (data.idManifesto) {
			//setDisabled(document.getElementById("manifesto.idManifesto"), false);
			setElementValue("manifesto.filialByIdFilialOrigem.idFilial", data.idFilial);
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.sgFilial);
			setElementValue("manifesto.idManifesto", data.idManifesto);
			setElementValue("manifesto.nrPreManifesto", setFormat(document.getElementById("manifesto.nrPreManifesto"), data.nrPreManifesto));
			setDisabled(document.getElementById("manifesto.filialByIdFilialOrigem.idFilial"), true);
			setDisabled(document.getElementById("manifesto.filialByIdFilialOrigem.sgFilial"), true);
		}
	}
	
	/**
	 * Realiza a pesquisa da hora de saida da rota.
	 *
	 * @param idRotaIdaVolta
	 */
	function findHrSaidaRota(idRotaIdaVolta) {
	
		var data = new Object();
		data.idRotaIdaVolta = idRotaIdaVolta;
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.findHrSaidaRota", "findHrSaidaRota", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * CallBack da busca de hora.
	 *
	 * @param
	 */
	function findHrSaidaRota_cb(data, error) {
		if (data.hrSaida!=undefined) {
			setElementValue("hrSaida", setFormat(document.getElementById("hrSaida"), data.hrSaida));
		}
	}
	
	/**
	 * Função para limpar a tela.
	 */
	function limpaTela() {
   	    resetValue(this.document);
   	    setDisabled("doctoServico.tpDocumentoServico", false);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true); //Desabilita inclusive a lupa
		setDisabled("doctoServico2.idDoctoServico", true);
		limpaPreManifesto();
		limpaRota();
		setFocusOnFirstFocusableField();
	}
	
	function limpaPreManifesto(){
		resetValue(document.getElementById("manifesto.idManifesto"));
		resetValue(document.getElementById("manifesto.filialByIdFilialOrigem.idFilial"));
		resetValue(document.getElementById("hrSaida"));
		setDisabled(document.getElementById("manifesto.nrPreManifesto"), true);
		setDisabled(document.getElementById("manifesto.filialByIdFilialOrigem.idFilial"), false);
	}
	
	function limpaRota(){
		resetValue("rotaIdaVolta.idRotaIdaVolta");
		setDisabled(document.getElementById("rotaIdaVolta.idRotaIdaVolta"), false);
	}
</script>