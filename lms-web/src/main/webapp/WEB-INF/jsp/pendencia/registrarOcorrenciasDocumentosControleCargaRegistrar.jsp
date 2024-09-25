<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	function pageLoad(data, error){
		setRowVisibility("cdOcorrenciaBloqueio");	
		onPageLoad(data, error);
	}

	/**
	 * Carrega página.
	 */	
	function carregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
	    var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosControleCargaRegistrarAction.getDadosSessao", "buscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	var dadosSessao;
	function buscarDadosSessao_cb(data, error) {
		dadosSessao = data;
		setaDadosSessao();
		
		// Pega parâmetros da tela pai.
		windowPai = window.dialogArguments.window;
		var tabGroup = getTabGroup(windowPai.document);
		var tabDet = tabGroup.getTab("ocorrencia");
		
		var idControleCarga = tabDet.getFormProperty("controleCarga.idControleCarga");		
		var sgFilial = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial");		
		var nrControleCarga = tabDet.getFormProperty("controleCarga.nrControleCarga");		
		var tpControleCarga = tabDet.getFormProperty("controleCarga.tpControleCarga.description");
		setElementValue("controleCarga.idControleCarga", idControleCarga);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("controleCarga.nrControleCarga", nrControleCarga);
		setElementValue("controleCarga.tpControleCarga", tpControleCarga);
		
		var idDoctoServico = tabDet.getFormProperty("doctoServico.idDoctoServico");
		setElementValue("doctoServico.idDoctoServico", idDoctoServico);
		
		var dataGrid = windowPai.manifestoGridDef.getDataRowById(windowPai.manifestoGridDef.getSelectedIds().ids[0]);		

		if(dataGrid.blBloqueado == "true") {
			setElementValue("ocorrenciaPendencia.tpOcorrencia", "L");
			setRowVisibility("cdOcorrenciaBloqueio","show");
			setElementValue("blPermiteOcorParaManif", "");
			document.getElementById("spanlbl_ocorrenciaPendencia").innerText = getLabelOcorrenciaLiberacao();
			
			//Buscar aqui a ocorrencia de bloqueio para mostrar no campo readonly
			var dataBean = new Array();
			merge(dataBean, buildFormBeanFromForm(document.forms[0]));
			merge(dataBean, selectedIds());
		    var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosControleCargaRegistrarAction.findOcorrenciaBloqueioFromOcorrenciaDoctoServicoEmAberto", "findOcorrencia", dataBean);
			xmit({serviceDataObjects:[sdo]});

		} else {
			setElementValue("ocorrenciaPendencia.tpOcorrencia", "B");
			setRowVisibility("cdOcorrenciaBloqueio");
			document.getElementById("spanlbl_ocorrenciaPendencia").innerText = getLabelOcorrenciaBloqueio();
		}
	}
	
	function findOcorrencia_cb(data, error){
		if (error){
			alert(error);
		} else {
			//Caso tenha uma ocorrência de bloqueio...
			if(data.idOcorrenciaBloqueio){
				setElementValue("idOcorrenciaBloqueio", data.idOcorrenciaBloqueio);
				setElementValue("cdOcorrenciaBloqueio", data.cdOcorrenciaBloqueio);
				setElementValue("dsOcorrenciaBloqueio", data.dsOcorrenciaBloqueio);
				//setRowVisibility("cdOcorrenciaBloqueio", "show");
				//document.getElementById("spanlbl_ocorrenciaPendencia").innerText = getLabelOcorrenciaLiberacao();
			}
		}
	}
</script>

<adsm:window title="registrarOcorrencia" service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaRegistrarAction"
		onPageLoad="pageLoad" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/pendencia/registrarOcorrenciasDocumentosControleCarga">
	
		<adsm:hidden property="doctoServico.idDoctoServico"/>
		<adsm:section caption="registrarOcorrencia" />
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					  label="controleCargas" labelWidth="21%" width="29%" size="3" maxLength="3" 
					  disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="controleCarga.nrControleCarga" maxLength="8" 
						  size="8" mask="00000000" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:textbox label="tipo" property="controleCarga.tpControleCarga" dataType="text" 
					  labelWidth="21%" width="27%" size="15" disabled="true"/>

		<adsm:hidden property="idOcorrenciaBloqueio"/>					  
		<adsm:textbox label="ocorrenciaBloqueio" property="cdOcorrenciaBloqueio" labelWidth="21%" width="52%" size="4" dataType="text" disabled="true">
			<adsm:textbox property="dsOcorrenciaBloqueio" dataType="text" size="50" disabled="true"/>
		</adsm:textbox>
 		<adsm:combobox width="25%" disabled="true"  property="ocorrenciaPendencia.tpOcorrencia" domain="DM_TIPO_OCORRENCIA_PENDENCIA" style="visibility:hidden" renderOptions="true" />

		<adsm:hidden property="blMatriz" value="true"/>
		<adsm:hidden property="blPermiteOcorParaManif" value="S" serializable="false"/>
		<adsm:hidden property="ocorrenciaPendencia.blApreensao" />
		<adsm:hidden property="ocorrenciaPendencia.evento.idEvento"/>
		<adsm:lookup property="ocorrenciaPendencia" idProperty="idOcorrenciaPendencia" 
					 criteriaProperty="cdOcorrencia"
					 service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaRegistrarAction.findLookupOcorrenciaPendencia"
					 action="/pendencia/registrarOcorrenciasDocumentosServico" cmd="ocorrenciaLookup"
					 onPopupSetValue="verificaOcorrenciaPendenciaLookup"
					 onDataLoadCallBack="verificaOcorrenciaPendenciaLookup"
					 onchange="return ocorrenciaPendenciaCdOcorrencia_OnChange(this.value)" 
					 popupLabel="pesquisarOcorrenciaPendencia"
					 label="ocorrenciaBloqueio" dataType="integer" size="4" maxLength="3"
					 labelWidth="21%" width="79%" criteriaSerializable="true" required="true" >
			<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico"/>
			<adsm:propertyMapping criteriaProperty="ocorrenciaPendencia.tpOcorrencia" modelProperty="tpOcorrencia"/>
			<adsm:propertyMapping criteriaProperty="blMatriz" modelProperty="blMatriz"/>
			<adsm:propertyMapping criteriaProperty="idOcorrenciaBloqueio" modelProperty="idOcorrenciaBloqueio"/>
			<adsm:propertyMapping criteriaProperty="blPermiteOcorParaManif" modelProperty="blPermiteOcorParaManif" />
			<adsm:propertyMapping modelProperty="dsOcorrencia" relatedProperty="ocorrenciaPendencia.dsOcorrencia"/>
			<adsm:propertyMapping modelProperty="blApreensao" relatedProperty="ocorrenciaPendencia.blApreensao"/>
			<adsm:propertyMapping modelProperty="evento.idEvento" relatedProperty="ocorrenciaPendencia.evento.idEvento"/>
			<adsm:textbox property="ocorrenciaPendencia.dsOcorrencia" dataType="text" 
						  size="50" maxLength="60" disabled="true" />
		</adsm:lookup>

		<adsm:textbox property="dhOcorrencia" label="dataOcorrencia" dataType="JTDateTimeZone"
					  labelWidth="21%" width="79%" disabled="true" picker="false"/>

		<adsm:label key="espacoBranco" width="100%" style="border:none"/>					  
		<adsm:section caption="comunicadoApreensao" />

		<adsm:textbox property="numeroTermoApreensao" label="numeroTermoApreensao" dataType="integer" maxLength="20" size="20"
					 labelWidth="21%" width="79%" disabled="true"/>

		<adsm:textbox property="dtTermoApreensao" label="dataTermoApreensao" dataType="JTDate" 
					  labelWidth="21%" width="79%" disabled="true"/>

		<adsm:combobox property="moeda.idMoeda" label="valorMulta"
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaRegistrarAction.findMoeda" 
					   labelWidth="21%" width="12%" boxWidth="85" disabled="true"/>
		<adsm:textbox property="valorMulta" dataType="currency" width="67%"
					  mask="#,###,###,###,###,##0.00" size="16" maxLength="18" disabled="true"/>

		<adsm:textarea property="motivoAlegado" label="motivoAlegado" maxLength="200" 
					   columns="85" rows="3" labelWidth="21%" width="79%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button caption="registrar" id="registrar" onclick="registrarOcorrencia(this.form)" disabled="false"/>
			<adsm:button caption="fechar" id="btnFechar" onclick="fecharJanela()" disabled="false"/>
		</adsm:buttonBar>
		
		<script>
			function getLabelOcorrenciaBloqueio(){
				var labelOcorrenciaBloqueio = '<adsm:label key="ocorrenciaBloqueio"/>';
				return labelOcorrenciaBloqueio;
			}
			
			function getLabelOcorrenciaLiberacao(){
				var labelOcorrenciaLiberacao = '<adsm:label key="ocorrenciaLiberacao"/>';
				return labelOcorrenciaLiberacao;
			}
		</script>
	</adsm:form>
</adsm:window>

<script type="text/javascript">

	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		// Seta o label para o campo valor de mercadoria
		document.getElementById("valorMulta").label = document.getElementById("moeda.idMoeda").label;
		document.getElementById("moeda.idMoeda").label = "Moeda do valor da multa";
	}
	
	/**
	 * Seta os Dados referente ao usuário da sessão.
	 */
	function setaDadosSessao() {
		setElementValue("moeda.idMoeda", dadosSessao.idMoedaSessao);
	}	

	// Função onDataLoadCallBack da lookup de Ocorrência
	function verificaOcorrenciaPendenciaLookup_cb(data, error) {
		var boolean = ocorrenciaPendencia_cdOcorrencia_exactMatch_cb(data);		
		if(boolean == true) {
			validaOcorrenciaPendencia(data[0]);
		}
	}
	
	// Função onPopupValue da lookup de Ocorrência
	function verificaOcorrenciaPendenciaLookup(data) {
		validaOcorrenciaPendencia(data);
	}
	
	// Função de retorno da validação da ocorrência de pendência.
	function validaOcorrenciaPendencia(data) {
		setElementValue("ocorrenciaPendencia.blApreensao", data.blApreensao);
		if (data.blApreensao == "true") {
			habilitaComunicadoApreensao(true);
		} else {
			resetComunicadoApreensao();
		}
	}
	
	function resetComunicadoApreensao(){
		resetValue("numeroTermoApreensao");
		resetValue("dtTermoApreensao");
		resetValue("moeda.idMoeda");
		resetValue("valorMulta");
		resetValue("motivoAlegado");
		habilitaComunicadoApreensao(false);
		setaDadosSessao();
	}
	
	function validaComunicadoApreensao(){
		if ((getElementValue("numeroTermoApreensao") != "")
			|| (getElementValue("dtTermoApreensao") != "")
			|| (getElementValue("valorMulta") != "")
			|| (getElementValue("motivoAlegado") != "")){
				document.getElementById("numeroTermoApreensao").required = "true";
				document.getElementById("dtTermoApreensao").required = "true";
				document.getElementById("moeda.idMoeda").required = "true";
				document.getElementById("valorMulta").required = "true";
				document.getElementById("motivoAlegado").required = "true";	
		} else {
			document.getElementById("numeroTermoApreensao").required = "false";
			document.getElementById("dtTermoApreensao").required = "false";
			document.getElementById("moeda.idMoeda").required = "false";
			document.getElementById("valorMulta").required = "false";
			document.getElementById("motivoAlegado").required = "false";	
		}
	}

	/**
	 * Função que habilita / desabilita a sessão de Comunicação de Apreensão.
	 */	
	function habilitaComunicadoApreensao(boolean) {
		if(boolean == true) {
			setDisabled("numeroTermoApreensao", false);
			setDisabled("dtTermoApreensao", false);
			setDisabled("moeda.idMoeda", false);
			setDisabled("valorMulta", false);
			setDisabled("motivoAlegado", false);
			setFocus("numeroTermoApreensao");
		} else {
			setDisabled("numeroTermoApreensao", true);
			setDisabled("dtTermoApreensao", true);
			setDisabled("moeda.idMoeda", true);
			setDisabled("valorMulta", true);
			setDisabled("motivoAlegado", true);
		}
	}
	
	// Função que limpa a lookup de ocorrencia e desabilita os campos de Comunicado Apreensão.
	function ocorrenciaPendenciaCdOcorrencia_OnChange(valor) {
		var boolean = ocorrenciaPendencia_cdOcorrenciaOnChangeHandler();
		if (boolean==true && valor == "") {
       	    resetValue("ocorrenciaPendencia.idOcorrenciaPendencia");
			resetComunicadoApreensao();				
		}	
		return boolean;		
	}
	
	/**
	 * Função que registra as ocorrencias.
	 */
	function registrarOcorrencia(form) {		
		validaComunicadoApreensao();
		if(validateForm(form)) {
			//var windowPai = window.dialogArguments.window;		
			var dataBean = new Array();

			merge(dataBean, buildFormBeanFromForm(form));
			merge(dataBean, selectedIds());

			//storeButtonScript('lms.pendencia.registrarOcorrenciasDocumentosServicoAction.store', 'registrarOcorrencia', dataBean);		
		    var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosControleCargaRegistrarAction.store", "registrarOcorrencia", dataBean);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	/**
	 * Função de callback do store
	 */
	function registrarOcorrencia_cb(data, error) {
		if(!error) {
			setElementValue("dhOcorrencia", setFormat(document.getElementById("dhOcorrencia"), data.dataHora));
			setDisabledForm(document.forms[0], true);			
			showSuccessMessage();
			setDisabledForm(document.forms[0], true);
			setDisabled(document.getElementById("btnFechar"), false);
			setFocus('btnFechar', false);
		} else {
			alert(error);
		}
	}
	
	function fecharJanela(){
		self.close();
	}

/**
 * Função para pegar os verdadeiros ids dos registros selecionados.
 */
function selectedIds() 
{
	var windowPai = window.dialogArguments.window;
	var gridFormElems = windowPai.document.getElementById("manifesto.form").elements;
	var dataGrid;
	// array que armazena os ids das linhas que dever?o ser removidas da camada de dados.
	var selectedIds = new Array();
	for (var j = 0; j < gridFormElems.length; j++) 
	{
		if ((gridFormElems[j].type == "checkbox") || (gridFormElems[j].type == "radio")) {
			if ((gridFormElems[j].name.indexOf(".idCustom")>0) || (gridFormElems[j].type == "radio")) {
				if (gridFormElems[j].checked) {
					if (gridFormElems[j].value != "undefined" && gridFormElems[j].value != "null") {
						dataGrid = windowPai.manifestoGridDef.getDataRowById(gridFormElems[j].value);
						selectedIds.push(dataGrid.idManifesto);
					}
				}
			}
		}
	}
	var selMap = new Object();
	selMap["ids"] = selectedIds;
	return selMap;
}
</script>