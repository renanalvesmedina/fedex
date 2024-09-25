<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
	
		document.getElementById("idFilial").masterLink = "true";
		document.getElementById("sgFilial").masterLink = "true";
		document.getElementById("nmFantasiaFilial").masterLink = "true";
		document.getElementById("manifesto.nrPreManifesto").disabled=true;
	
    	var data = new Array();
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.getBasicData", "loadBasicData", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	function loadBasicData_cb(data, error) {
		
		document.getElementById("idFilial").value = data.idFilial;
		document.getElementById("sgFilial").value = data.sgFilial;
		document.getElementById("nmFantasiaFilial").value = data.nmFantasiaFilial;
		
		onPageLoad();
	}
</script>

<adsm:window service="lms.carregamento.receberDocumentosServicoCheckinAction" onPageLoad="loadDataObjects">
<script language="javascript" src="../lib/ctrcVerifierServiceDocumentWidgetDefinitions.js"></script>
	<adsm:form action="/carregamento/receberDocumentosServicoCheckin" id="formDocumentosServicoCheckin">
		
		<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia"/>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" width="85%"
					   service="lms.carregamento.receberDocumentosServicoCheckinAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="onChangeTpDocumentoServico();"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial"
						 service="" popupLabel="pesquisarFilial"
						 disabled="true"
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
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico"
						 popupLabel="pesquisarDocumentoServico"
						 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />
						 
			<adsm:lookup dataType="integer"
						 property="doctoServico2"
						 idProperty="idDoctoServico" criteriaProperty="dvConhecimento"
						 service=""
						 action=""
						 onchange="return changeDocumentWidgetVerifierDigit({verifierDigitElement:document.getElementById('doctoServico2.idDoctoServico'),
						 										 documentNumberCriteriaElement:document.getElementById('doctoServico.nrDoctoServico')});"
						 size="1" maxLength="1" serializable="true" mask="0" style="display:none" pickerStyle="display:none"/>

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
			     onchange="return sgFilialOnChangeHandler(this.value);"
			     popupLabel="pesquisarFilial"
			     >
			<adsm:lookup dataType="integer"
			    		 property="manifesto" 
			             idProperty="idManifesto" 
			             criteriaProperty="nrPreManifesto"
				         service="lms.carregamento.receberDocumentosServicoCheckinAction.findLookupPreManifesto" 
				         action="carregamento/manterGerarPreManifesto" 
				         cmd="list"
				         popupLabel="pesquisarPreManifesto"
				         onPopupSetValue="habilitaNrPreManifesto"
				         afterPopupSetValue="loadManifestoPopUp"
				         onDataLoadCallBack="disableNrPreManifesto"
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
	
		<adsm:lookup label="rota" width="85%"
					 mask="0000"
		        	 size="4" 
		        	 maxLength="4" 
					 exactMatch="true"
					 dataType="integer" 
					 property="rotaIdaVolta"  
					 idProperty="idRotaIdaVolta"  
					 criteriaProperty="nrRota"
					 onDataLoadCallBack="loadRotaIdaVolta"
					 onPopupSetValue="loadRotaIdaVoltaPopUp"
					 service="lms.carregamento.receberDocumentosServicoCheckinAction.findLookupRotaIdaVolta"
					 action="/municipios/consultarRotas"
					 cmd="idaVolta">
 		    <adsm:propertyMapping modelProperty="rota.dsRota" relatedProperty="rotaIdaVolta.rota.dsRota" />
 		    
 		    <adsm:propertyMapping modelProperty="filialOrigem.idFilial" criteriaProperty="idFilial" disable="false"/>
 		    <adsm:propertyMapping modelProperty="filialOrigem.sgFilial" criteriaProperty="sgFilial" disable="false"/>
 		    <adsm:propertyMapping modelProperty="filialOrigem.nmFilial" criteriaProperty="nmFantasiaFilial" />
 		    
 		    <adsm:propertyMapping modelProperty="vigentes" criteriaProperty="vigentes" />
 		    
        	<adsm:textbox dataType="text" property="rotaIdaVolta.rota.dsRota" disabled="true" size="30" serializable="false"/>
        </adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="btnConsultar" caption="consultar" onclick="return findDocumentos();"/>
			<adsm:button caption="limpar" id="btnLimpar" buttonType="resetButton" onclick="limpaTela()"/>
		</adsm:buttonBar>
		<script>
			function lms_05096() {
				alert("<adsm:label key='LMS-05096'/>");
			}
		</script>
	</adsm:form>

	<adsm:grid idProperty="idDoctoServico" property="doctoServico" selectionMode="none" unique="false"
			           service="lms.carregamento.receberDocumentosServicoCheckinAction.findPaginatedCheckin" gridHeight="200"
			   rowCountService="lms.carregamento.receberDocumentosServicoCheckinAction.getRowCountCheckin" onRowClick="loadDetail">
		<adsm:gridColumn title="rotaViagem" property="nrRota" width="35" mask="0000" dataType="integer"/>
		<adsm:gridColumnGroup customSeparator=" ">	
			<adsm:gridColumn title="" property="dsRota" width="65" />
			<adsm:gridColumn title="" property="hrSaida" width="10" />
		<adsm:gridColumn title="" property="horario" width="70" align="center" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn property="sgFilialManifesto " dataType="text" title="preManifesto" width="30"/>
			<adsm:gridColumn property="nrPreManifesto" dataType="integer" title="" width="70" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="documentoServico" 	property="tpDoctoServico" isDomain="true" width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialDoctoServico" width="30" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" dataType="text" title="valorMercadoria" width="30"/>
			<adsm:gridColumn property="dsSimboloMoeda" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlMercadoria" dataType="currency" title="" width="70"/>
		<adsm:gridColumn property="qtVolumes" title="volumes" width="80" align="right" />
		<adsm:gridColumn property="psReal" title="peso" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
	
	<adsm:form action="/carregamento/receberDocumentosServicoCheckin">
		<adsm:textbox dataType="currency" property="valorTotal" label="valorTotal" width="18%" disabled="true" mask="###.###.###.###,00"/>
		<adsm:textbox dataType="decimal" property="volumeTotal" label="totalVolumes" width="18%" disabled="true"/>
		<adsm:textbox dataType="decimal" property="pesoTotal" label="totalPeso" width="18%" disabled="true" mask="###.###.###.###,000" unit="kg"/>
	</adsm:form>
</adsm:window>

<script>

	function initWindow(eventObj) {
		setDisabled("btnConsultar", false);
		if (eventObj.name == "tab_click") {
			setFocus(document.getElementById("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico"));
		} else if (eventObj.name == "cleanButton_click") {
			setDisabled("manifesto.nrPreManifesto", true);
			doctoServicoGridDef.resetGrid();
		}
	}
	
	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	function findDocumentos() {
		if ((document.getElementById("doctoServico.idDoctoServico").value=="") && 
			(document.getElementById("manifesto.idManifesto").value=="") && 
			(document.getElementById("rotaIdaVolta.idRotaIdaVolta").value=="")) {
			lms_05096();
			setFocusOnFirstFocusableField();
			return false
		}
	
		var sdo = createServiceDataObject("lms.carregamento.receberDocumentosServicoCheckinAction.findSumDoctoServicoByIdManifestoAndIdRotaIdaVolta", "findDocumentos", buildFormBeanFromForm(this.document.forms[0]));
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findDocumentos_cb(data, error) {		
		if (error) {
			alert(error);
		}
		setElementValue("volumeTotal", data.volumeTotal);
		setElementValue("pesoTotal", data.pesoTotal);
		setElementValue("valorTotal", data.valorTotal);
		
		return findButtonScript('doctoServico', document.getElementById("formDocumentosServicoCheckin"));
	}
	
	/**
	 * javaScripts para a 'tag documents'
	 */
	function enableDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.nrDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}
	
	function retornoDocumentoServico_cb(data) {
	   var r = doctoServico_nrDoctoServico_exactMatch_cb(data);
	   if (getElementValue("doctoServico.tpDocumentoServico") == "CTR" && r == true) {
	      setDisabled("doctoServico2.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico2.dvConhecimento"));
	   }
	}
	
	//VERIFICAR SE ESTA FUNCIONANDO
	function retornoDvDocumentoServico_cb(data) {
	   var r = doctoServico2_dvConhecimento_exactMatch_cb(data);
	   if (getElementValue("doctoServico.tpDocumentoServico") == "CTR" && r == true) {
	      setDisabled("doctoServico2.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico2.dvConhecimento"));
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
	
	function disableNrPreManifesto_cb(data, error) {
	
		var result = manifesto_nrPreManifesto_exactMatch_cb(data);
	
		if (data.length==0) {
			setDisabled(document.getElementById("manifesto.nrPreManifesto"), false);
		} else {
			limpaRota();
		}
		return result;
	}
	
	function loadManifestoPopUp(data) {
		limpaRota();
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.sgFilialOrigem);
	}
	
	function habilitaNrPreManifesto(data, error){
		setDisabled(document.getElementById("manifesto.nrPreManifesto"), false);
	}
	
	/*
	 * rotaIdaVolta callBack
	 */
	 
	function loadRotaIdaVolta_cb(data, error){
		var result = rotaIdaVolta_nrRota_exactMatch_cb(data);
		if (result==true){	
			limpaPreManifesto();
			
			//Rota ida volta...
			setElementValue("rotaIdaVolta.idRotaIdaVolta", data[0].idRotaIdaVolta);
			setElementValue("rotaIdaVolta.nrRota", data[0].nrRota);
			setElementValue("rotaIdaVolta.rota.dsRota", data[0].rota.dsRota);
		}
	}
	
	function loadRotaIdaVoltaPopUp(data) {
		limpaPreManifesto();
	}
	
	/**
	 * Carrega a tela de details a partir do clique na grid.
	 *
	 * @param id
	 */
	function loadDetail(id) {
		var data = new Object();
		data.idDoctoServico = id;
		
		for (i=0; i<doctoServicoGridDef.gridState.data.length; i++) {
			var rowData = doctoServicoGridDef.gridState.data[i];	
			if (rowData.idDoctoServico==id){    				
				data.idManifesto = rowData.idManifesto;
				break;
			}
		}
		
		doctoServicoGridDef.detailGridRow("onDetailDataLoad", data);
		return false;
	}
	
	/**
	 * Função para limpar a tela.
	 */
	function limpaTela() {
   	    resetValue(this.document);
   	    setDisabled("doctoServico.tpDocumentoServico", false);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true); //Desabilita inclusive a lupa
		setDisabled("doctoServico.nrDoctoServico", true);
		setDisabled("doctoServico2.idDoctoServico", true);
		setDisabled("manifesto.nrPreManifesto", true);
		setFocusOnFirstFocusableField();
	}
	
	function limpaPreManifesto(){
		resetValue(document.getElementById("manifesto.idManifesto"));
		resetValue(document.getElementById("manifesto.filialByIdFilialOrigem.idFilial"));
		setDisabled(document.getElementById("manifesto.nrPreManifesto"), true);
	}
	
	function limpaRota(){
		resetValue("rotaIdaVolta.idRotaIdaVolta");
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
</script>
