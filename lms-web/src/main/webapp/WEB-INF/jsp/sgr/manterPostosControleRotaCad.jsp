<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.rotaPostoControleService">
	<adsm:form action="/sgr/manterPostosControleRota" idProperty="idRota" 
	service="lms.sgr.rotaPostoControleService.findRotaPostoControleByRota" onDataLoadCallBack="loadData">
	
		<adsm:lookup dataType="text" property="rota" idProperty="idRota" criteriaProperty="dsRota"
					 service="lms.municipios.rotaService.findLookup" action="/municipios/manterPostosPassagemRotasViagem" cmd="list" 
					 onDataLoadCallBack="rota" onchange="return rotaChangeHandler();" onPopupSetValue="checkPostosControle"
		 			 label="rota" labelWidth="15%" width="85%" size="30" maxLength="60" exactMatch="true" minLengthForAutoPopUpSearch="3"/>

		<adsm:section caption="postosControleOrdemPassagem"/>
	
		<adsm:listbox property="rotaPostosControle" optionProperty="idRotaPostoControle" optionLabelProperty="postoControle.dsPostoControle" orderProperty="nrOrdem" 
					  label="postosControle" size="6" boxWidth="600" labelWidth="15%" width="85%" showOrderControls="true" serializable="true"/>
					  
		<adsm:buttonBar>
			<adsm:storeButton service="lms.sgr.rotaPostoControleService.storeListOrder" id="buttonSalvar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	
	function initWindow(eventObj) {
		var event = eventObj.name;
		if (event == "removeButton" || event == "newButton_click") {
			desabilitaTabPostosControle(true);
			document.getElementById('buttonSalvar').disabled = isDisabledStoreButton();
		} else if (event == "gridRow_click" || eventObj.name == "storeButton") { 
			desabilitaTabPostosControle(false);
			document.getElementById('buttonSalvar').disabled = isDisabledStoreButton();
		} else if (event == "tab_click"){
			if (getElementValue('idRota')!='') {
				loadView();
			} else {
				document.getElementById('buttonSalvar').disabled = isDisabledStoreButton();
			}
		}
	}
	
	function loadView() {
		resetValue("rotaPostosControle");
		var beanValue = getElementValue('idRota');
		var sdo = createServiceDataObject("lms.sgr.rotaPostoControleService.findRotaPostoControleByRota", "loadData", {idRota:beanValue});
    	xmit({serviceDataObjects:[sdo]});
	}

	function loadData_cb(data, error) {
		setElementValue("idRota", getNestedBeanPropertyValue(data, "rota.idRota"));
		setElementValue("rota.idRota", getNestedBeanPropertyValue(data, "rota.idRota"));
		setElementValue("rota.dsRota", getNestedBeanPropertyValue(data, "rota.dsRota"));
		rotaPostosControle_cb(data, error);
		document.getElementById('buttonSalvar').disabled = isDisabledStoreButton();
	}

	function desabilitaTabPostosControle(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("postosControle", disabled);
	}
	
	/**
	 * Verifica se existem postos de controle para esta rota
	 * e por sua vez carrega a list de posto de controles
	 */
	function checkPostosControle(data) {
		setElementValue("idRota", data.idRota);
		setElementValue("rota.idRota", data.idRota);
		setElementValue("rota.dsRota", data.dsRota);
		desabilitaTabPostosControle(false);
		loadView();
	}
	
	/**
	 * Controla o recarregamento da tela via o objeto de rota
	 */
	function rotaChangeHandler() {
		if (document.getElementById("rota.dsRota").value=="") {
			resetValue("rotaPostosControle");
			desabilitaTabPostosControle(true);
		}
		
		document.getElementById('buttonSalvar').disabled = isDisabledStoreButton();
		
		return rota_dsRotaOnChangeHandler();
	}
	 
	function rota_cb(data, error) {
		var result = lookupExactMatch({e:document.getElementById("rota.idRota"), data:data, callBack:"rotaLike"});
		reLoadPage(data);
		return result;
	}
	
	function rotaLike_cb(data, error) {
		var result = rota_dsRota_likeEndMatch_cb(data);
		reLoadPage(data);
		return result;
	}
	
	function reLoadPage(data) {
		resetValue("rotaPostosControle");
		if (data[0]!=undefined) {		
   			setElementValue("idRota", getNestedBeanPropertyValue(data[0], "idRota"));
			loadView();
			desabilitaTabPostosControle(false);
		} else {
			desabilitaTabPostosControle(true);
		}
		document.getElementById('buttonSalvar').disabled = isDisabledStoreButton();
	}
	
	/**
	 * Verifica se a list de rotaPostosControle contem algum elemento
	 * caso nao exista retorna true.
	 */
	function isDisabledStoreButton(){
		if (document.getElementById("rotaPostosControle").length>0) return false;
		return true;
	}
</script>