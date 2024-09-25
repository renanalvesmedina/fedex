<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">

	function validaWorkFlowInicioDescarga(idControleCarga){
		var paramsMap = new Array();
		setNestedBeanPropertyValue(paramsMap, "idControleCarga", idControleCarga);
		var sdo = createServiceDataObject("lms.recepcaodescarga.iniciarDescargaAction.validateWorkFlowInicioDescarga", "validaWorkFlowInicioDescarga", paramsMap);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validaWorkFlowInicioDescarga_cb(data, error) {
		setDisabled("iniciar", false);
		if(error) {
			alert(error);
			setDisabled("iniciar", true);
		}
	}

	function findInformacoesIniciais(doc) {
		var idControleCarga = getElementValue("controleCarga.idControleCarga");
		var tpControleCarga = getElementValue("controleCarga.tpControleCarga");

		// se o controle de carga for de coleta/entrega
		// deve buscar a última equipe aberta para aquele 
		if (idControleCarga && tpControleCarga=='C') {
			var data = new Array();
			data.idControleCarga = idControleCarga;
			var sdo = createServiceDataObject("lms.recepcaodescarga.iniciarDescargaAction.findInformacoesIniciais", "findInformacoesIniciais", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function findInformacoesIniciais_cb(data, error) {
		if (error) {
			alert(error);			
		} else if (data.idEquipe){
			// id da equipe operacao (que nao foi fechada) que deu origem à 
			// sugestão de equipe, caso 
			setElementValue('idOpenEquipeOperacao', data.idOpenEquipeOperacao);
			setElementValue('equipe.idEquipe', data.idEquipe);		
			setElementValue('equipe.dsEquipe', data.dsEquipe);			
			setIdEquipeIntegrantes(data.idEquipe);
			equipeOnChange();
		}
	}


	function carregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		var sdo = createServiceDataObject("lms.recepcaodescarga.iniciarDescargaAction.newMaster", "newMaster");
		xmit({serviceDataObjects:[sdo]});
	
		// Rotina que pega a referência da tela pai para usar parametros ou chamar funções
		// que serão usadas na tela filho.
		var doc;
		if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {
			doc = window.dialogArguments.window.document;
		} else {
		   doc = document;
		}
		
		// Pega parâmetros da tela pai.
		var tabGroup = getTabGroup(doc);
		var tabDet = tabGroup.getTab("recepcaoDescarga");
		var idCarregamentoDescarga = tabDet.getFormProperty("idCarregamentoDescarga");
		var idFilialControleCarga = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.idFilial");
		var sgFilialControleCarga = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial");
		var idControleCarga = tabDet.getFormProperty("controleCarga.idControleCarga");
		var tpControleCarga = tabDet.getFormProperty("controleCarga.tpControleCarga");
		var nrControleCarga = tabDet.getFormProperty("controleCarga.nrControleCarga");		
		var idFilial = tabDet.getFormProperty("idFilial");
		var sgFilial = tabDet.getFormProperty("sgFilial");
		var nmFilial = tabDet.getFormProperty("pessoa.nmFantasia");
		var sgPostoAvancado = tabDet.getFormProperty("sgFilialPostoAvancado");
		var nmPostoAvancado = tabDet.getFormProperty("nmPessoaPostoAvancado");		
		
		setElementValue("idCarregamentoDescarga", idCarregamentoDescarga);
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilialControleCarga);
		setElementValue("controleCarga.idControleCarga", idControleCarga);
		setElementValue("controleCarga.tpControleCarga", tpControleCarga);
		setElementValue("controleCarga.nrControleCarga", nrControleCarga);			
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilialControleCarga);
		setElementValue("idFilial", idFilial);
		setElementValue("sgFilial", sgFilial);
		setElementValue("nmFilial", nmFilial);
		setElementValue("sgPostoAvancado", sgPostoAvancado);
		setElementValue("nmPostoAvancado", nmPostoAvancado);

		findInformacoesIniciais(doc);
		validaWorkFlowInicioDescarga(idControleCarga);

	}	
	
	/**
	 * CallBack do Master na sessão.
	 */
	function newMaster_cb(data, error) {
		// Pega parâmetros da tela pai.
		var tabGroup = getTabGroup(window.dialogArguments.window.document);
		var tabDet = tabGroup.getTab("recepcaoDescarga");
		var mapArray = new Array();
		setNestedBeanPropertyValue(mapArray, "idFilial", tabDet.getFormProperty("idFilial"));
		setNestedBeanPropertyValue(mapArray, "idControleCarga", tabDet.getFormProperty("controleCarga.idControleCarga"));
		var sdo = createServiceDataObject("lms.recepcaodescarga.iniciarDescargaAction.getBoxFromCarregamentoDescarga", "boxFromCarregamentoDescarga", mapArray);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * CallBack do getBoxFromCarregamentoDescarga.
	 */
	function boxFromCarregamentoDescarga_cb(data, error) {
		if(data.idBox) {
			setElementValue("box.idBox", data.idBox);
			setDisabled("box.idBox", true);
		}else {
			setDisabled("box.idBox", false);
		}
		
		var tpControleCarga = getElementValue("controleCarga.tpControleCarga");
		var tabGroup = getTabGroup(window.dialogArguments.window.document);
		var tabDet = tabGroup.getTab("recepcaoDescarga");

		if (tpControleCarga=='V') {
			setDisabled("nrQuilometragem", true);
			this.document.getElementById("nrQuilometragem").required = "false";	
			setDisabled("blVirouHodometro", true);
			this.document.getElementById("blVirouHodometro").required = "false";
			
		}else if(tabDet.getFormProperty("blInformaKmPortaria") == "false") {
			setDisabled("nrQuilometragem", false);
			this.document.getElementById("nrQuilometragem").required = "true";
			setDisabled("blVirouHodometro", false);
			this.document.getElementById("blVirouHodometro").required = "true";
			
		} else {
			var mapArray = new Array();
			setNestedBeanPropertyValue(mapArray, "idFilial", tabDet.getFormProperty("idFilial"));
			setNestedBeanPropertyValue(mapArray, "idControleCarga", tabDet.getFormProperty("controleCarga.idControleCarga"));
			var sdo2 = createServiceDataObject("lms.recepcaodescarga.iniciarDescargaAction.getControleQuilometragem", "controleQuilometragem", mapArray);
			xmit({serviceDataObjects:[sdo2]});
		}		
	}
	
	// Callback do Controle Quilometragem
	function controleQuilometragem_cb(data, error) {
		if(!error) {
			setElementValue("nrQuilometragem", data.nrQuilometragem);
			setDisabled("nrQuilometragem", true);
			
			setElementValue("blVirouHodometro", data.blVirouHodometro);
			setDisabled("blVirouHodometro", true);
		} else {
			alert(error);
			setDisabled("nrQuilometragem", false);
			this.document.getElementById("nrQuilometragem").required = "true";	
			setFocus(this.document.getElementById("nrQuilometragem"));
			
			setDisabled("blVirouHodometro", false);
			this.document.getElementById("blVirouHodometro").required = "true";	
		}
	}
</script>

<adsm:window service="lms.recepcaodescarga.iniciarDescargaAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/recepcaoDescarga/iniciarDescarga" idProperty="idEquipeOperacao">
		
		<adsm:hidden property="idCarregamentoDescarga" />
		
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial" />
		<adsm:hidden property="controleCarga.idControleCarga" />		
		<adsm:hidden property="controleCarga.tpControleCarga" />		
		<adsm:textbox label="controleCarga" property="controleCarga.filialByIdFilialOrigem.sgFilial" dataType="text"  
					  disabled="true" size="3" maxLength="3" labelWidth="20%" width="80%">
			<adsm:textbox property="controleCarga.nrControleCarga" dataType="text" disabled="true" 
						  size="8" maxLength="8" />
		</adsm:textbox>

		<adsm:hidden property="idFilial" />
		<adsm:textbox label="filial" property="sgFilial" dataType="text"  
					  disabled="true" size="3" maxLength="3" labelWidth="20%" width="80%">
			<adsm:textbox property="nmFilial" dataType="text" disabled="true" 
						  size="50" maxLength="50" />
		</adsm:textbox>
		
		<adsm:textbox label="postoAvancado" property="sgPostoAvancado" dataType="text"  
					  disabled="true" size="3" maxLength="3" labelWidth="20%" width="80%">
			<adsm:textbox property="nmPostoAvancado" dataType="text" disabled="true" 
						  size="50" maxLength="50" />
		</adsm:textbox>
		
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:hidden property="idOpenEquipeOperacao"/>
		<adsm:lookup property="equipe" dataType="text" idProperty="idEquipe" criteriaProperty="dsEquipe"
					 action="/carregamento/manterEquipes" service="lms.recepcaodescarga.iniciarDescargaAction.findLookupEquipe" 
					 onDataLoadCallBack="loadEquipes" onPopupSetValue="loadEquipesPopUp" onchange="return equipeOnChange();"
					 label="equipe" labelWidth="20%" width="80%" maxLength="50" size="50" required="true"
					 minLengthForAutoPopUpSearch="3" exactMatch="false">
			<adsm:propertyMapping modelProperty="idEquipe" relatedProperty="equipe.idEquipe"/>					 
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
		</adsm:lookup>
		
		<adsm:combobox property="box.idBox" optionProperty="idBox" optionLabelProperty="dsBox" 
					   service="lms.recepcaodescarga.iniciarDescargaAction.findBox" 
					   label="box" labelWidth="20%" width="80%" onlyActiveValues="true" />
					   
		<adsm:textbox label="quilometragemRetorno" property="nrQuilometragem" dataType="integer" 
					  size="8" maxLength="6" labelWidth="20%" width="12%" disabled="true"/>
		<adsm:checkbox label="virouHodometro" property="blVirouHodometro" />
		
		<adsm:checkbox label="semLacre" property="semLacre" labelWidth="20%" width="80%"
					   onclick="desabilitaAbaLacre()"/>
		<adsm:label key="espacoBranco" width="20%" style="border:none"/>
        <adsm:range width="80%">
			<adsm:textarea property="obSemLacre" maxLength="200" rows="3" columns="57"/>
        </adsm:range>
        
        <adsm:textarea label="observacaoQuilometragem" property="obControleQuilometragem" maxLength="200" 
        			   rows="3" columns="57" labelWidth="20%" width="80%"/>
		       			  
		<adsm:buttonBar>
			<adsm:storeButton id="iniciar" caption="iniciarDescarga" callbackProperty="iniciarDescarga" />
			<adsm:button id="closeButton" caption="fechar" onclick="self.close();"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	
	function initWindow(eventObj) {
		setDisabled("closeButton", false);
		document.getElementById("obSemLacre").readOnly="true";
	}
	
	/**
	 * Função que desabilita a aba de Lacres e habilita a textarea 
	 * para observação, ou vice-versa.
	 */
	function desabilitaAbaLacre() {
		var tabGroup = getTabGroup(this.document);
		if(getElementValue("semLacre")) {						
			setDisabled("obSemLacre", false);
			tabGroup.setDisabledTab("lacres", true);
		} else {			
			setElementValue("obSemLacre", "");
			setDisabled("obSemLacre", true);
			tabGroup.setDisabledTab("lacres", false);
		}
	}

	/**
	 * Funcao de retorno do objeto de iniciar descarga
	 */
	function iniciarDescarga_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField(document);
		} else {
			window.close();
		}
	}
	
	/**
	 * Função de onChange da lookup de Equipes.
	 */
	function equipeOnChange() {
		if (getElementValue("equipe.dsEquipe")=="") {
			removeIntegrantes(0);
			disableIntegrantes(true);
		} else {
			disableIntegrantes(false);
		}
		return equipe_dsEquipeOnChangeHandler();
	}
	
	/**
	 * Tratamento para o callback da lookup de Equipes.
	 * @param data
	 * @param error
	 */
	function loadEquipes_cb(data, error) {
		if (data[0]!=undefined) {
			disableIntegrantes(false);
			setElementValue("equipe.idEquipe", data[0].idEquipe);
			removeIntegrantes(data[0].idEquipe);
			setIdEquipeIntegrantes(data[0].idEquipe);
		} else {	
			disableIntegrantes(true);
		}
		
		return lookupExactMatch({e:document.getElementById("equipe.idEquipe"), data:data, callBack:"loadEquipesLikeMatch"});
	}
	
	/**
	 * Tratamento para o callback LikeMatch da lookup de Equipes.
	 *
	 * @param data
	 * @param error
	 */
	function loadEquipesLikeMatch_cb(data, error) {
		if (data[0]!=undefined) {
			disableIntegrantes(false);
			setElementValue("equipe.idEquipe", data[0].idEquipe);
			removeIntegrantes(data[0].idEquipe); 
			setIdEquipeIntegrantes(data[0].idEquipe);
		} else {	
			disableIntegrantes(true);
		}
		
		return equipe_dsEquipe_likeEndMatch_cb(data);
	}	
	
	/**
	 * Tratamento para o callback da lookup de equipes quando a mesma vem da popup
	 * @param data
	 * @param error
	 */
	function loadEquipesPopUp(data, error) {
		if (data.dsEquipe!=undefined) {
			disableIntegrantes(false);
			removeIntegrantes(data.idEquipe);
			setIdEquipeIntegrantes(data.idEquipe);
		}
	}
	
	/**
	 * Remove todos os filhos casoo id desta tela seja igual ao do filho
	 */
	function removeIntegrantes(idEquipe){
		var tabGroup = getTabGroup(this.document);
		var abaIntegrantes = tabGroup.getTab("integrantes");
		
		if (abaIntegrantes.getElementById("idEquipe").value != idEquipe){
			var sdo = createServiceDataObject("lms.recepcaodescarga.iniciarDescargaAction.newMaster");
			xmit({serviceDataObjects:[sdo]});
		}	
		
	}
	
	/**
	 * Seta o id equipe existente na masterLinK 
	 */ 
	function setIdEquipeIntegrantes(idEquipe) {	
		var tabGroup = getTabGroup(this.document);
		var abaIntegrantes = tabGroup.getTab("integrantes");		
		abaIntegrantes.getElementById("idEquipe").value = idEquipe;
	}

	/**
	 * Desabilita a aba de integrantes
	 */
	function disableIntegrantes(disableTab) {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup!=null) {
			tabGroup.setDisabledTab('integrantes', disableTab);	
		}
	}
	
</script>
