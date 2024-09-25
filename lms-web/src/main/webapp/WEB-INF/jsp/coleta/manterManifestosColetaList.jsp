<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
    	var data = new Array();
		var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaAction.getDataUsuario", "loadDataUsuario", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var dataUsuario;
	function loadDataUsuario_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		dataUsuario = data;
		fillDataUsuario();
		onPageLoad();
	}
</script>

<adsm:window service="lms.coleta.manterManifestosColetaAction" onPageLoad="loadDataObjects">
	<adsm:form action="/coleta/manterManifestosColeta">	
	
		<adsm:hidden property="filial.idFilial" serializable="false"/>
		<adsm:hidden property="filial.sgFilial" serializable="false"/>
		<adsm:hidden property="filial.pessoa.nmFantasia" serializable="false"/>
		<adsm:hidden property="nrManifesto" serializable="false"/>
		<adsm:lookup dataType="integer" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
					 onDataLoadCallBack="checkRota" onPopupSetValue="checkRotaFromPopUp" onchange="return nrRotaOnChangeHandler();"
			 		 service="lms.coleta.manterManifestosColetaAction.findLookupByFilialUsuario" action="/municipios/manterRotaColetaEntrega"
			  		 label="rotaColetaEntrega" labelWidth="20%" width="80%" maxLength="3" size="5" required="true">
			 <adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
			 <adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filial.idFilial"/>
			 <adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filial.sgFilial"/>
			 <adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia"/>
			 <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" maxLength="30" size="30"/>
		</adsm:lookup>
					 
		<adsm:buttonBar freeLayout="true">
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="manifestoColetas" idProperty="id" scrollBars="horizontal" gridHeight="260" rows="13" 
			   defaultOrder="controleCarga_.nrControleCarga:asc" onRowClick="rowClickHandler">
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="sgFilialOrigem" width="30" title="controleCargas"/>
			<adsm:gridColumn property="nrControleCarga" width="90" title="" dataType="integer" mask="00000000" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="dhGeracao" width="140" title="geracao" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="nrFrotaTransportado" width="40" title="meioTransporte"/>
		<adsm:gridColumn property="nrIdentificadorTransportado" width="80" title=""/>
		<adsm:gridColumn property="nrFrotaSemiRebocado" width="40" title="semiReboque"/>
		<adsm:gridColumn property="nrIdentificadorSemiRebocado" width="80" title=""/>
		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn property="sgFilial" width="30" title="manifesto"/>
			<adsm:gridColumn property="nrManifesto" width="70" mask="00000000" dataType="integer" title="" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="psTotalFrota" width="100" title="pesoTotal" unit="kg" dataType="decimal" mask="###,###,##0.000" align="right"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" title="valorTotal" width="30"/>
			<adsm:gridColumn property="dsSimbolo" width="30" title="" dataType="text"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlTotalFrota" width="70" title="" dataType="currency" align="right"/>
		<adsm:gridColumn property="nrCapacidadeKg" width="175" title="capacidadeVeiculo" unit="kg" dataType="decimal" mask="###,###,##0.000" align="right"/>
		<adsm:buttonBar> 
			<adsm:button id="botaoRemover" onclick="removeDados()" caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			if (getElementValue("rotaColetaEntrega.dsRota")=="") {
				setDisableTabs(true, null);
			} else {
				setDisabled("botaoRemover", false);
			}
			setDisableTabs(null, true);
		} else if (eventObj.name == "cleanButton_click"){
			fillDataUsuario();
			manifestoColetasGridDef.resetGrid();
			setDisableTabs(true, true);
		}
	}
	
	/**
	 * Remove os itens da grid selecionados
	 */
	function removeDados() {
		var idsMap = manifestoColetasGridDef.getSelectedIds(); 
		
		if (idsMap.ids.length>0) { 
			if (window.confirm(erExcluir)) {
				var itemsRemove = new Array();
				for (j=0; idsMap.ids.length>j; j++) {
					var id = idsMap.ids[j];
					for (i=0; i<manifestoColetasGridDef.gridState.data.length; i++) {
						if (manifestoColetasGridDef.gridState.data[i].id==id){
							if (manifestoColetasGridDef.gridState.data[i]!=undefined) {
								var ids = new Object();
								ids.idManifestoColeta = manifestoColetasGridDef.gridState.data[i].idManifestoColeta;
								ids.idControleCarga = manifestoColetasGridDef.gridState.data[i].idControleCarga;
								itemsRemove[i] = ids;
								break;
							}
						}
					}
				} 
		
				var remoteCall = {serviceDataObjects:new Array()}; 
			  	remoteCall.serviceDataObjects.push(createServiceDataObject("lms.coleta.manterManifestosColetaAction.removeByIds", "removeDados", {ids:itemsRemove})); 
			  	xmit(remoteCall); 		
			}
		} else {
			alert(erSemRegistro);
		}
	}
	
	function removeDados_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		showSuccessMessage();
		manifestoColetasGridDef.executeLastSearch(true);
	}
	
	/**
	 * Habilita a aba de detalhe 
	 */
	function rowClickHandler(id){
				
		var dadosControleCarga;
		for (i=0; i<manifestoColetasGridDef.gridState.data.length; i++) {
			if (manifestoColetasGridDef.gridState.data[i].id==id){
				dadosControleCarga = manifestoColetasGridDef.gridState.data[i];
			}
		}
		
		var dados = new Object();
		if (dadosControleCarga!=undefined) {
			if (dadosControleCarga.idManifestoColeta!=undefined) {
				setNestedBeanPropertyValue(dados, "idManifestoColeta", dadosControleCarga.idManifestoColeta);
				setNestedBeanPropertyValue(dados, "beanName", "manifestoColeta");
			} else {
				setNestedBeanPropertyValue(dados, "idControleCarga", dadosControleCarga.idControleCarga);
				setNestedBeanPropertyValue(dados, "beanName", "controleCarga");
			}
		}
		
		manifestoColetasGridDef.detailGridRow("onLoadData", dados);
		
		return false;
	}
	
	/**
	 * Verifica a mudanca do objeto de rota
	 */
	function nrRotaOnChangeHandler() {
		
		if (getElementValue("rotaColetaEntrega.nrRota")=="") {
			setDisableTabs(true, null);
			manifestoColetasGridDef.resetGrid();
		}
		
		setDisableTabs(null, true);
		
		return lookupChange({e:document.forms[0].elements["rotaColetaEntrega.idRotaColetaEntrega"]});
	}
	
	/**
	 * Controle para o objeto de 'Rota'
	 */		
	function checkRota_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		if (data.length==0) {
			setDisableTabs(true, null);
		} else {
			setDisableTabs(false, null);
			if (data!=undefined) {
				doManifestosColetaSearch(data);
				setDisabled("botaoRemover", false);
			}
		}
		
		return lookupExactMatch({e:document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), data:data});
	}
	
	/**
	 * Verifica a rota que retornou da popup
	 */ 
	function checkRotaFromPopUp(data) {
		setDisableTabs(false, null);
		if (data!=undefined) doManifestosColetaSearch(data);
	}
	
	/**
	 * Realiza a consulta de manifestos coletas existentes para esta rota.
	 */
	function doManifestosColetaSearch(data){
		if (data.length>0) {
			setElementValue("rotaColetaEntrega.idRotaColetaEntrega", data[0].idRotaColetaEntrega);
			setElementValue("rotaColetaEntrega.nrRota", data[0].nrRota);
			setElementValue("rotaColetaEntrega.dsRota", data[0].dsRota);
		} else if (data.idRotaColetaEntrega!=undefined) {
			setElementValue("rotaColetaEntrega.idRotaColetaEntrega", data.idRotaColetaEntrega);
			setElementValue("rotaColetaEntrega.nrRota", data.nrRota);
			setElementValue("rotaColetaEntrega.dsRota", data.dsRota);
		}
		
		if (getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="") findButtonScript('manifestoColetas', document.forms[0]);
	}
	
	/**
	 * Desabilita as abas de detalhamento e coletas
	 *
	 * @param disable1 aba detalhamento
	 * @param disable2 aba colheta
	 */
	function setDisableTabs(disableTab1, disableTab2) {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup!=null) {
			if (disableTab1!=null) tabGroup._tabsIndex[1].setDisabled(disableTab1);
			if (disableTab2!=null) tabGroup._tabsIndex[2].setDisabled(disableTab2);
		}
	}
	
	/**
	 * Carrega os dados da filial do usuario logado para um campo hidden na tela
	 */
	function fillDataUsuario(){
		setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
		setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
	}
</script> 
