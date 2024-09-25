<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.carregamento.emitirReciboPostoPassagemAction" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form action="/carregamento/emitirReciboPostoPassagem">
	
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem"  
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.emitirReciboPostoPassagemAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="18%" width="82%" size="3" 
					 maxLength="3" picker="false" serializable="false" popupLabel="pesquisarFilial">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>

			<adsm:lookup dataType="integer" property="controleCarga" 
						 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.carregamento.emitirReciboPostoPassagemAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas" cmd="list"
						 afterPopupSetValue="loadDataByNrControleCarga"
						 maxLength="8" size="8" mask="00000000" popupLabel="pesquisarControleCarga" required="true" >
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="true"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="true"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" disable="true"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:hidden property="idCache" serializable="false"/>
		
		<adsm:checkbox property="blMostrarCancelados" label="mostrarCancelados" labelWidth="18%" width="82%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="botaoConsultar" onclick="consultar_OnClick(this.form);" disabled="false" />
			<adsm:button caption="limpar" buttonType="reset" onclick="resetView();"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="reciboPostoPassagem" idProperty="idReciboPostoPassagem" onRowClick="disableGridClick"
			   selectionMode="none" gridHeight="270" rows="14">
		<adsm:gridColumn property="filial.sgFilial" title="filial" width="50"/>
		<adsm:gridColumn property="nrReciboPostoPassagem" title="numeroRecibo" width="100" dataType="integer"/>
		<adsm:gridColumn property="tpStatusRecibo" title="status" isDomain="true" width="70"/>
		<adsm:gridColumn property="dhEmissao" title="dataEmissao" width="105" dataType="JTDateTimeZone"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="moeda.sgMoeda" dataType="text" title="valor" width="20"/>
			<adsm:gridColumn property="moeda.dsSimbolo" title="" width="20"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlBruto" dataType="currency" title="" width="50"/>
		<adsm:gridColumn property="usuario.nmUsuario" title="usuario" width="195"/>
		<adsm:gridColumn property="visualizar" title="visualizar" align="center" image="/images/printer.gif" link="javascript:geraRecibo" linkIdProperty="idReciboPostoPassagem" width="70"/>
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>

<script>
	function initWindow(event) {
		if (event.name == "tab_load") {
			setDisabled("botaoConsultar", false);
			setElementValue('blMostrarCancelados', false);
		}
		setFocusOnFirstFocusableField();
	}


	function retornoCarregaPagina_cb(data, error) {
		onPageLoad_cb(data,error);
		setDisabled("controleCarga.nrControleCarga", true);
	}

	//##################################
    // Comportamentos apartir de objetos
	//##################################
	
	/**
	 * Funcao que faz a chamada do relatorio...
	 *
	 * @param idReciboPostoPassagem
	 */
	function geraRecibo(idReciboPostoPassagem) {
	
		//Deixa o id do Recibo Posto Passagem requerido salvo na tela.
		setElementValue("idCache", idReciboPostoPassagem);
	
		var dataObject = new Object();
		dataObject.idReciboPostoPassagem = idReciboPostoPassagem;
		dataObject.status = getStatusReciboPostoPassagem(idReciboPostoPassagem);
		dataObject._reportCall = true; 	
		
		var sdo = createServiceDataObject('lms.carregamento.emitirReciboPostoPassagemAction.execute',  'openPdfRecibo', dataObject); 
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Restorna o status do recibo posto de passagem (emitido, reemitido ou nulo)...
	 *
	 * @param id do registro selecionado na grid
	 */
	function getStatusReciboPostoPassagem(id) {
		for (i=0; i<reciboPostoPassagemGridDef.gridState.data.length; i++) {
			if (reciboPostoPassagemGridDef.gridState.data[i].idReciboPostoPassagem==id){
				return reciboPostoPassagemGridDef.gridState.data[i].tpStatusRecibo.value;
			}
		}
		return "";
	}
	
	/**
	 * Callback da chamada do relatorio. Responsavel por fazer a atualizacao do 
	 * registro de reciboPostoPassagem
	 *
	 * @param data
	 * @param error
	 */
	function openPdfRecibo_cb(data, error) {
		openReport(data, error);
		
		var dataObject = new Object();
		dataObject.idReciboPostoPassagem = getElementValue("idCache");
		
		var sdo = createServiceDataObject("lms.carregamento.emitirReciboPostoPassagemAction.updateReciboPostoPassagem", "updateRecibo", dataObject);
	    xmit({serviceDataObjects:[sdo]});
	    
	    //Esvazia o elemento de cache...
	    setElementValue("idCache", "");
	}
	
	function updateRecibo_cb(data, error) {
		populateGrid();
		return true;
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
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	function loadDataByNrControleCarga(data) {
		setDisabled("controleCarga.idControleCarga", false);
	}
	
	function enableNrControleCarga(data){
		disableNrControleCarga(false);
	}
	
	function disableNrControleCarga(disable) {
		document.getElementById("controleCarga.nrControleCarga").disabled = disable;
	}
	

	//##################################
    // Comportamentos basicos
	//##################################
	
	/**
	 * Popula a grid
	 */
	function populateGrid() {
		var formData = new Object();
		setNestedBeanPropertyValue(formData, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));
		setNestedBeanPropertyValue(formData, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));
		reciboPostoPassagemGridDef.executeSearch(formData, true);
	}
	
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
	
	/**
	 * Reseta os dados da tela
	 */
	function resetView(){
		cleanButtonScript(this.document);
		reciboPostoPassagemGridDef.resetGrid();
		setDisabled("controleCarga.nrControleCarga", true);
		setDisabled("botaoConsultar", false);
	}
	
	
	function consultar_OnClick(form) {
		if (!validateForm(form)) {
			return false;
		}
		populateGrid();
	}
</script>
