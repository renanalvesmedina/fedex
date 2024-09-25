<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
	/**
	 * Forca a tela a carregar o botao de visual
	 */
	function loadPage() {
		setDisabled('visualizar', false);
		
		var data = new Array();
		var sdo = createServiceDataObject("lms.coleta.consultarColetasAction.findFilialUsuarioLogado", "loadPage", data);
    	xmit({serviceDataObjects:[sdo]});
    
	}
	
	var dataUsuario;
	function loadPage_cb(data, error) {
		dataUsuario = data;
		loadDataFromUsuario(dataUsuario);
		disableNrManifesto(true);
		onPageLoad();
	}
	
	function loadDataFromUsuario(data){
		setElementValue("filialByIdFilialResponsavel.idFilial", data.filial.idFilial);
		setElementValue("filialByIdFilialResponsavel.sgFilial", data.filial.sgFilial);
		setElementValue("filialByIdFilialResponsavel.sgFilialValue", data.filial.sgFilial);
		setElementValue("filialByIdFilialResponsavel.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);
	}
</script>

<adsm:window service="lms.coleta.emitirManifestosColetaSemBaixaAction" onPageLoad="loadPage">
	<adsm:form action="/coleta/emitirManifestosColetaSemBaixa">
		
		<adsm:hidden property="filialByIdFilialResponsavel.sgFilialValue"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" />
		
		<adsm:lookup dataType="text" property="filialByIdFilialResponsavel" idProperty="idFilial" criteriaProperty="sgFilial" 
	   				 service="lms.coleta.emitirManifestosColetaSemBaixaAction.findLookupFilialByUsuario" action="/municipios/manterFiliais" 
	   				 onPopupSetValue="validaAcessoFilial"
					 label="filial" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="false" required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialResponsavel.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="filialByIdFilialResponsavel.sgFilialValue"/>
			<adsm:propertyMapping modelProperty="flagBuscaEmpresaUsuarioLogado" criteriaProperty="flagBuscaEmpresaUsuarioLogado"/>				
			<adsm:textbox dataType="text" property="filialByIdFilialResponsavel.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:hidden property="rotaColetaEntrega.dsRotaValue"/>
		<adsm:lookup dataType="integer" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
					 service="lms.coleta.emitirManifestosColetaSemBaixaAction.findLookupRotaColetaEntrega" action="/municipios/manterRotaColetaEntrega"
					 label="rotaColetaEntrega" labelWidth="18%" width="82%" size="3" maxLength="3" >
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.idFilial" modelProperty="filial.idFilial" />
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.sgFilial" modelProperty="filial.sgFilial" />
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
 		    <adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRotaValue"/>
 		    <adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota"/>
        	<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" disabled="true" size="30" serializable="false"/>
        </adsm:lookup>
		 		
		 
		<adsm:hidden property="manifestoColeta.nrManifestoValue"/>	
		<adsm:lookup dataType="text" property="manifestoColeta.filial" popupLabel="pesquisarFilial" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.coleta.emitirManifestosColetaSemBaixaAction.findLookupFilialByUsuario" action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" onDataLoadCallBack="filialManifestoCallback"
					 label="manifesto" labelWidth="18%" width="82%" size="3" maxLength="3" picker="false" serializable="false">
			<adsm:lookup dataType="integer" property="manifestoColeta" idProperty="idManifestoColeta" criteriaProperty="nrManifesto"
						 action="/coleta/consultarManifestosColeta" popupLabel="pesquisarManifesto" cmd="pesq" service="lms.coleta.emitirManifestosColetaSemBaixaAction.findLookupManifestoColeta" 
						 onDataLoadCallBack="loadNrManifesto" onPopupSetValue="enableNrManifesto"
						 exactMatch="false" size="15" maxLength="8" mask="00000000" > 
				<adsm:propertyMapping criteriaProperty="manifestoColeta.filial.idFilial" modelProperty="filial.idFilial" disable="true" /> <%-- inlineQuery="false" --%>
 		    	<adsm:propertyMapping criteriaProperty="manifestoColeta.filial.sgFilial" modelProperty="filial.sgFilial" disable="true" /> <%-- inlineQuery="false" --%>
 		    	<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="manifestoColeta.filial.idFilial" blankFill="false"/>  
 		    	<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="manifestoColeta.filial.sgFilial" blankFill="false"/>  
				<adsm:propertyMapping modelProperty="nrManifesto" relatedProperty="manifestoColeta.nrManifestoValue"/>
			</adsm:lookup>
		</adsm:lookup>
		 				
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="meioTransporte2.nrFrotaValue"/>
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte" criteriaProperty="nrFrota"
					 service="lms.contratacaoveiculos.meioTransporteService.findLookup" action="/contratacaoVeiculos/manterMeiosTransporte" 
					 label="meioTransporte" labelWidth="18%" width="82%" size="6" serializable="false" maxLength="6" picker="false">
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporte.nrIdentificador" disable="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.idFilial" modelProperty="filial.idFilial" disable="false"/>
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.sgFilial" modelProperty="filial.sgFilial" disable="false"/>
 		    <adsm:propertyMapping criteriaProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" disable="false"/>
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificadorValue" />
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte2.nrFrotaValue" />

			<adsm:hidden property="meioTransporte.nrIdentificadorValue"/>
			<adsm:lookup dataType="text" property="meioTransporte" 
						 idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"
						 action="/contratacaoVeiculos/manterMeiosTransporte" service="lms.contratacaoveiculos.meioTransporteService.findLookup" 
						 picker="true" maxLength="25" size="20" >
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporte2.nrFrota" />
				<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte2.idMeioTransporte"	/>	
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte2.nrFrota" />
				<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporte.nrIdentificadorValue" />
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporte2.nrFrotaValue" />
			</adsm:lookup>
			
		</adsm:lookup>
		
		<adsm:hidden property="servico.dsServico" />
		<adsm:combobox label="servico" property="servico.idServico" renderOptions="true"
					   optionProperty="idServico" optionLabelProperty="dsServico"
					   service="lms.coleta.emitirJustificacaoColetasNaoRealizadasAction.findServico" 
					   labelWidth="18%" width="82%" boxWidth="230"
					   onlyActiveValues="true" onchange="setDsServico();"/>
		<adsm:hidden property="dsTpPedidoColeta" />
		<adsm:combobox label="tipoColeta" property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA" labelWidth="18%" width="82%" renderOptions="true" onchange="setDsTipoColeta();"/>
		
		<adsm:combobox property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO"
		               defaultValue="pdf"
		               label="formatoRelatorio" labelWidth="18%" width="82%" required="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="visualizar" onclick="validar()" disabled="false"/>
			<adsm:button id="cleanButton" caption="limpar" buttonType="cleanButton" disabled="false" onclick="limpaTela()"/>	
		</adsm:buttonBar>
		<script>
			function lms_02013() {
				alert('<adsm:label key="LMS-02013"/>');
			}
		</script>
	</adsm:form>
</adsm:window>

<script>
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click"){
			document.getElementById("visualizar").disabled = false;
			loadDataFromUsuario(dataUsuario);
		} else if (eventObj.name == "tab_load") {
			disableNrManifesto(false);
		}
	}
	
	/**
	 * Verifica se as regras de preenchimento desta tela foram feitos
	 */
	function validar() {
		if(!validateForm(this.document.forms[0])){
			return false;
		}
		
		if ((getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="")||(getElementValue("manifestoColeta.idManifestoColeta")!="")||(getElementValue("meioTransporte2.idMeioTransporte")!="")){
			if ((getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="") &&  (getElementValue("manifestoColeta.idManifestoColeta")!="") || 
				(getElementValue("rotaColetaEntrega.idRotaColetaEntrega")!="") &&  (getElementValue("meioTransporte2.idMeioTransporte")!="") || 
				(getElementValue("manifestoColeta.idManifestoColeta")!="") &&  (getElementValue("meioTransporte2.idMeioTransporte")!="")) {
				lms_02013();
 			} else {
				reportButtonScript('lms.coleta.emitirManifestosColetaSemBaixaAction', 'openPdf', this.document.forms[0]);
			} 
		} else {
			lms_02013();
		}
	}
		
	/**
	 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
	 * Função necessária pois quando é selecionado um item na popup não é chamado
	 * o serviço definido na lookup.
	 */
	 function validaAcessoFilial(data) {
		var criteria = new Array();
	    // Monta um map
	    setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
	    setNestedBeanPropertyValue(criteria, "sgFilial", data.sgFilial);
		
	    var sdo = createServiceDataObject("lms.coleta.emitirManifestosColetaSemBaixaAction.findLookupFilialByUsuario", "validaAcessoFilial", criteria);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Função que trata o retorno da função validaAcessoFilial.
	 */
	function validaAcessoFilial_cb(data, error) {
	
		if (error != undefined) {
			alert(error);
			resetValue(document.getElementById("filialByIdFilialResponsavel.idFilial"));
			resetValue(document.getElementById("filialByIdFilialResponsavel.pessoa.nmFantasia"));
		    setFocus(document.getElementById("filialByIdFilialResponsavel.sgFilial"));
			return false;
		} else {
			filialByIdFilialResponsavel_sgFilial_exactMatch_cb(data, error);
		}
	}
	
	/**
	 * Controla o objeto de manifesto
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("manifestoColeta.filial.sgFilial")=="") {
			disableNrManifesto(true);
			resetValue("manifestoColeta.idManifestoColeta");
		} else {
			disableNrManifesto(false);
		}
		return lookupChange({e:document.forms[0].elements["manifestoColeta.filial.idFilial"]});
	}
	
	function filialManifestoCallback_cb(data, error) {
		if (data.length==0) {
			disableNrManifesto(false);
		}
		return lookupExactMatch({e:document.getElementById("manifestoColeta.filial.idFilial"), data:data});
	}
	
	function loadNrManifesto_cb(data, error) {
		manifestoColeta_nrManifesto_exactMatch_cb(data);
		if (data[0]!=undefined) {
			document.getElementById("manifestoColeta.filial.sgFilial").value=data[0].filial.sgFilial;
		}
	}
	
	function disableNrManifesto(disable) {
		document.getElementById("manifestoColeta.nrManifesto").disabled = disable;
	}
	
	function enableNrManifesto(data){
		if (data.nrManifesto!=undefined) {
			disableNrManifesto(false);
		} else {
			disableNrManifesto(true);
		}
	}
	
	/**
	 * Função que limpa a tela carrega os dados da sessão
	 */
	function limpaTela() {
		cleanButtonScript(this.document);
		disableNrManifesto(true);
	}
	
	function setDsServico(){
		var comboBoxServico = document.getElementById("servico.idServico");
		setElementValue("servico.dsServico", comboBoxServico.options[comboBoxServico.selectedIndex].text);
	}
	
	function setDsTipoColeta(){
		var comboBoxServico = document.getElementById("tpPedidoColeta");
		setElementValue("dsTpPedidoColeta", comboBoxServico.options[comboBoxServico.selectedIndex].text);
	}
		
</script>
