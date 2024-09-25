<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">

	function onLoadData(id, beanName, unknow){
		var dados = new Object();
		setNestedBeanPropertyValue(dados, "id", id);
		setNestedBeanPropertyValue(dados, "beanName", beanName);

		if (beanName=="manifestoColeta") {
			disableAbaColeta(false);
		} else {
			disableAbaColeta(true);
		}

		var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaAction.findDataFromPage", "onDataLoad", dados);
    	xmit({serviceDataObjects:[sdo]});
	}
	
</script>

<adsm:window service="lms.coleta.manterManifestosColetaAction">
	<adsm:form action="/coleta/manterManifestosColeta" idProperty="idManifestoColeta" >
	
		<adsm:complement label="rotaColetaEntrega" labelWidth="20%" width="80%">
			<adsm:hidden property="rotaColetaEntrega.idRotaColetaEntrega"/>
			<adsm:textbox dataType="integer" property="rotaColetaEntrega.nrRota" maxLength="3" size="5" disabled="true"/>
			<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true"/>
		</adsm:complement>

		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.rnc.abrirRNCAction.findLookupFilialByControleCarga" action="/municipios/manterFiliais" 
				     label="controleCargas" size="3" maxLength="3" labelWidth="20%" width="30%" picker="false" serializable="false" disabled="true">
	 		<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.rnc.abrirRNCAction.findLookupControleCarga" action="/carregamento/manterControleCargas" 
						 size="9" maxLength="8" serializable="true" disabled="true" picker="false" mask="00000000">
			</adsm:lookup>
		</adsm:lookup>
					  
		<adsm:textbox dataType="JTDateTimeZone" property="dhGeracao" label="dataHoraGeracao" disabled="true" labelWidth="20%" width="30%" picker="false"/>

		<adsm:hidden property="tpSituacao" value="A"/>		
		<adsm:lookup dataType="text" property="controleCarga.meioTransporteByIdTransportado" idProperty="idMeioTransporteFake" criteriaProperty="nrFrota"
					 service="lms.contratacaoveiculos.meioTransporteService.findLookup" action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onchange="meioTransporteByIdTransportadoNrFrotaOnChangeHandler()"
					 label="meioTransporte" labelWidth="20%" width="30%" maxLength="6" size="6" serializable="false" disabled="true" picker="false">
					 
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="controleCarga.meioTransporteByIdTransportado.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="controleCarga.meioTransporteByIdTransportado.nrIdentificador"/> 
			<adsm:propertyMapping modelProperty="nrCapacidadeKg" relatedProperty="controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="controleCarga.meioTransporteByIdTransportado.nrIdentificador"/> 
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
			
			<adsm:lookup dataType="text" property="controleCarga.meioTransporteByIdTransportado" idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"
						 service="lms.contratacaoveiculos.meioTransporteService.findLookup" action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="25" picker="false" disabled="true">
						 
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="controleCarga.meioTransporteByIdTransportado.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrCapacidadeKg" relatedProperty="controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg"/> 
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="controleCarga.meioTransporteByIdTransportado.nrFrota"/> 
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
				
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado" idProperty="idMeioTransporteFake" criteriaProperty="nrFrota"
					 service="lms.contratacaoveiculos.meioTransporteService.findLookup" action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onPopupSetValue="semiReboqueOnPopUpValue"
					 onchange="meioTransporteByIdSemiRebocadoNrFrotaOnChangeHandler()"
					 label="semiReboque" labelWidth="20%" width="30%" maxLength="6" size="6" serializable="false" disabled="true" picker="false">
					 
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
			
			<adsm:lookup dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado" idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"
						 service="lms.contratacaoveiculos.meioTransporteService.findLookup" action="/contratacaoVeiculos/manterMeiosTransporte" 
						 onDataLoadCallBack="semiReboqueOnDataLoad"  
						 maxLength="25" size="25" picker="false" disabled="true">
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="controleCarga.meioTransporteByIdSemiRebocado.nrFrota"/> 
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
			</adsm:lookup>
		</adsm:lookup>

		<adsm:lookup dataType="text" property="filial"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.municipios.filialService.findLookupBySgFilial" action="/municipios/manterFiliais"
					 label="manifesto" size="3" maxLength="3" labelWidth="20%" width="30%" picker="false" disabled="true">
			<adsm:textbox dataType="integer" property="nrManifesto" disabled="true" mask="00000000"/>
		</adsm:lookup>
					 
		<adsm:textbox dataType="weight" property="controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg" 
					  label="capacidadeVeiculo" labelWidth="20%" width="30%" unit="kg" disabled="true"/>

		<adsm:textbox dataType="weight" property="controleCarga.psTotalFrota" 
					  label="pesoTotal" labelWidth="20%" width="30%" unit="kg" disabled="true"/>
		
		<adsm:combobox property="controleCarga.moeda.idMoeda" label="valorTotal"
					   service="lms.rnc.abrirRNCAction.findMoeda" 
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   labelWidth="20%" width="80%" disabled="true">
			<adsm:textbox dataType="currency" property="controleCarga.vlTotalFrota" size="18" maxLength="18" disabled="true"/>
		</adsm:combobox>
		
		<adsm:buttonBar> 
			<adsm:storeButton id="botaoSalvar"/>
			<adsm:button caption="excluir" id="botaoRemover" onclick="removeData()"/>
		</adsm:buttonBar>
		<script>
			function lms_02067() {
				return window.confirm("<adsm:label key='LMS-02067'/>");
			}
			function lms_02068() {
				return alert("<adsm:label key='LMS-02068'/>");
			}
		</script>
	</adsm:form>
</adsm:window>

<script>

	function initWindow(eventObj) {
		setFocus(document.getElementById("rotaColetaEntrega.dsRota"));
		if (eventObj.name == "tab_click") {
		
			//Verifica se a aba de coletas esta enabled...
			if (isDisabledAbaColeta()) {
			
				var idManifestoColetaTemp = getElementValue("idManifestoColeta");
				resetValue(this.document);
				setElementValue("idManifestoColeta", idManifestoColetaTemp);
				if (getElementValue("idManifestoColeta")!="") document.getElementById("botaoRemover").disabled = false;
				
				disableAbaColeta(true);
				loadRota();
				setFocus(document.getElementById("controleCarga.meioTransporteByIdTransportado.nrFrota"));
			} else {
				document.getElementById("botaoRemover").disabled = false;
			
				//Carrega a tela de manifesto coleta detalhamentos para buscar alguma atualizacao referente aos 
				//pedidos coletas adicionados na aba de coletas...
				var idManifestoColetaValue = getElementValue("idManifestoColeta");
				var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaAction.findById", "loadDataControleCarga", {idManifestoColeta:idManifestoColetaValue});
    			xmit({serviceDataObjects:[sdo]});
			}
		} else if (eventObj.name == "storeButton") {
			disableAbaColeta(false);
			document.getElementById("botaoRemover").disabled = false;
		} else if (eventObj.name == "removeButton") {
			resetValue(this.document);
			loadRota();
			disableAbaColeta(true);
		} else if (eventObj.name == "gridRow_click") {
			setDisabled("botaoRemover", false);
			resetValue(this.document);
			loadRota();
		}
	}
	
	function loadDataControleCarga_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		if (data.controleCarga.moeda!=undefined) {
			setElementValue("controleCarga.moeda.idMoeda", data.controleCarga.moeda.idMoeda);
			
			setElementValue("controleCarga.vlTotalFrota", setFormat(document.getElementById("controleCarga.vlTotalFrota"), data.controleCarga.vlTotalFrota));
			setElementValue("controleCarga.psTotalFrota", setFormat(document.getElementById("controleCarga.psTotalFrota"), data.controleCarga.psTotalFrota));
			
			//setElementValue("controleCarga.psTotalFrota", data.controleCarga.psTotalFrota);
			//format(document.getElementById("controleCarga.psTotalFrota"));
		}
	}
		
	/**
	 * Carrega a dsRota de rota caso o usuario venha a partir de um clique na aba.
	 */ 
	function loadRota() {
	
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("pesq");
		
		if (tabDet.getFormProperty("rotaColetaEntrega.idRotaColetaEntrega")!=getElementValue("rotaColetaEntrega.idRotaColetaEntrega")) {
			newButtonScript();
			setElementValue("rotaColetaEntrega.idRotaColetaEntrega", tabDet.getFormProperty("rotaColetaEntrega.idRotaColetaEntrega"));
			setElementValue("rotaColetaEntrega.nrRota", tabDet.getFormProperty("rotaColetaEntrega.nrRota"));
			setElementValue("rotaColetaEntrega.dsRota", tabDet.getFormProperty("rotaColetaEntrega.dsRota"));
		}
	}
	
	/**
	 * Habilita ou desabilita a aba de coleta
	 */
	function disableAbaColeta(disable) {
		var tabGroup = getTabGroup(this.document);
		tabGroup._tabsIndex[2].setDisabled(disable);
	}
	
	/**
	 * Remove o manifesto coleta caso seja possivel.
	 */
	function removeData(){		
		if (lms_02067()) { 
			var dataObject = new Object();
			dataObject.idManifestoColeta = getElementValue("idManifestoColeta");
			
			var sdo = createServiceDataObject('lms.coleta.manterManifestosColetaAction.removeById', 'removeByIdManifestoColeta', dataObject); 
			xmit({serviceDataObjects:[sdo]});    	
		}
	}
	
	/**
	 * 	Remove o controle carga caso seja possivel.
	 */
	function removeByIdManifestoColeta_cb(data, error) {
		if (!error) { 
			resetValue(document.getElementById("filial.idFilial"));
			resetValue(document.getElementById("filial.sgFilial"));
			resetValue(document.getElementById("nrManifesto"));
		
			var dataObject = new Object();
			dataObject.idControleCarga = getElementValue("controleCarga.idControleCarga");
			
			var sdo = createServiceDataObject('lms.coleta.manterManifestosColetaAction.removeByIdControleCarga', 'removeByIdControleCarga', dataObject); 
			xmit({serviceDataObjects:[sdo]});    	
		} else {
			alert(error);
		}
	}
	
	/**
	 * Limpa a tela e deixa os dados necessarios.
	 */
	function removeByIdControleCarga_cb(data, error) {
		if (!error) { 
			resetValue(this.document);
			loadRota();
			disableAbaColeta(true);
		} else {
			lms_02068();
		}
	}
	
	/**
	 * Verifica se a aba de coleta esta ou nao desabilitada
	 */
	function isDisabledAbaColeta() {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup._tabsIndex[2].properties.disabled == true) return true;
		return false;
	}
	
	/**
	 * Joga o foco para o botao de salvar
	 */
	function semiReboqueOnPopUpValue(data) {
		setElementValue("controleCarga.meioTransporteByIdSemiRebocado.nrFrota", data.nrFrota);
		setElementValue("controleCarga.meioTransporteByIdSemiRebocado.idMeioTransporteFake", data.idMeioTransporte);
		setElementValue("controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador", data.nrIdentificador);
		setElementValue("controleCarga.meioTransporteByIdSemiRebocado.idMeioTransporte", data.idMeioTransporte);
		setFocusOnButton("storeButton");
		return false;
	}
	
	/**
	 * Joga o foco para o botao de salvar
	 */
	function semiReboqueOnDataLoad_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		controleCarga_meioTransporteByIdSemiRebocado_nrIdentificador_exactMatch_cb(data);
		if (data!=undefined) 
			setFocusOnButton("storeButton");
	}
	
	/**
	 * Seta o foco no botao limpar
	 */
	function setFocusOnButton(buttonType) {
      	var doc = this.document;
      	var button = findButtonByType(buttonType, doc);
      	if (button == null) {
            setFocusOnFirstFocusableField(doc);
      	} else if (isVisible(button)){
            button.focus();
        }
	}
	
	/**
	 * Funcoes para limpar o nrIndentificador dos veiculos
	 */
	function meioTransporteByIdTransportadoNrFrotaOnChangeHandler() {
	 	controleCarga_meioTransporteByIdTransportado_nrFrotaOnChangeHandler();
	 	if (document.getElementById("controleCarga.meioTransporteByIdTransportado.nrFrota").value=="") {
	 		document.getElementById("controleCarga.meioTransporteByIdTransportado.idMeioTransporte").value="";
	 		document.getElementById("controleCarga.meioTransporteByIdTransportado.nrIdentificador").value="";
	 	}
	}
	 	
	function meioTransporteByIdSemiRebocadoNrFrotaOnChangeHandler() {
	 	controleCarga_meioTransporteByIdSemiRebocado_nrFrotaOnChangeHandler();
	 	if (document.getElementById("controleCarga.meioTransporteByIdSemiRebocado.nrFrota").value=="") {
	 		document.getElementById("controleCarga.meioTransporteByIdSemiRebocado.idMeioTransporte").value="";
	 		document.getElementById("controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador").value="";
	 	}
	}
</script>