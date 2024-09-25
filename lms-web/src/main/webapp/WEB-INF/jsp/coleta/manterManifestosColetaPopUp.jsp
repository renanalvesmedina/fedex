<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.coleta.manterManifestosColetaPopUpAction" onPageLoadCallBack="loadDataObjects">
	<adsm:form action="/coleta/manterManifestosColeta">	
		
		<adsm:hidden property="filial.idFilial" serializable="false"/>
		<adsm:textbox property="filial.sgFilial" dataType="text" label="filial" disabled="true"  size="3" maxLength="3" 
					  width="80%" labelWidth="20%">
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" maxLength="50" size="50" disabled="true"/>
		</adsm:textbox>
		
		<adsm:hidden property="nrManifesto" serializable="false"/>
		<adsm:lookup dataType="integer" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
					 onDataLoadCallBack="checkRota" onPopupSetValue="checkRotaFromPopUp" onchange="return nrRotaOnChangeHandler();"
			 		 service="lms.coleta.manterManifestosColetaPopUpAction.findLookupByFilialUsuario" action="/municipios/manterRotaColetaEntrega"
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


	<adsm:grid property="manifestoColetas" idProperty="id" scrollBars="horizontal" gridHeight="230" rows="13" 
			   defaultOrder="controleCarga_.nrControleCarga:asc">
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
			<adsm:gridColumn property="filial.sgFilial" width="30" title="manifesto"/>
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
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click"){
			setDisabled("botaoFechar", false);
			fillDataUsuario();
			manifestoColetasGridDef.resetGrid();
		}
	}

	var dataUsuario = new Array();

	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		onPageLoad_cb(data, error);
		if (getElementValue("filial.idFilial") != "") {
			setNestedBeanPropertyValue(dataUsuario, "filial.idFilial", getElementValue("filial.idFilial"));
			setNestedBeanPropertyValue(dataUsuario, "filial.sgFilial", getElementValue("filial.sgFilial"));
			setNestedBeanPropertyValue(dataUsuario, "filial.pessoa.nmFantasia", getElementValue("filial.pessoa.nmFantasia"));
		}
		else {
			var sdo = createServiceDataObject("lms.coleta.manterManifestosColetaPopUpAction.getDataUsuario", "loadDataUsuario");
	    	xmit({serviceDataObjects:[sdo]});
	    }
	}

	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	function loadDataUsuario_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		dataUsuario = data;
		fillDataUsuario();
	}

	/**
	 * Verifica a mudanca do objeto de rota
	 */
	function nrRotaOnChangeHandler() {
		var r = lookupChange({e:document.forms[0].elements["rotaColetaEntrega.idRotaColetaEntrega"]});
		if (getElementValue("rotaColetaEntrega.nrRota")=="") {
			manifestoColetasGridDef.resetGrid();
		}
		return r;
	}
	
	/**
	 * Controle para o objeto de 'Rota'
	 */		
	function checkRota_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}	
		var r = lookupExactMatch({e:document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"), data:data});
		if (r == true) {
			doManifestosColetaSearch(data);
		}
		return r;
	}
	
	/**
	 * Verifica a rota que retornou da popup
	 */ 
	function checkRotaFromPopUp(data) {
		doManifestosColetaSearch(data);
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

		if (getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="")
			findButtonScript('manifestoColetas', document.forms[0]);
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
