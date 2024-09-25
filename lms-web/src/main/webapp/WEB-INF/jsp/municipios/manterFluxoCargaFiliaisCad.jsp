<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterFluxoCargaFiliaisAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-29024"/>
		<adsm:include key="LMS-29025"/>
		<adsm:include key="LMS-29026"/>
	</adsm:i18nLabels>

	<adsm:form action="/municipios/manterFluxoCargaFiliais" idProperty="idFluxoFilial"
		onDataLoadCallBack="fluxoFilialLoad">
		
		<adsm:hidden property="dsFluxoFilial" value=""/>
		<adsm:hidden property="nrPrazoTotal" value=""/>
		<adsm:hidden property="blClonar" value="false"/>
		<adsm:hidden property="tpSituacao" serializable="false"/> 
		<adsm:hidden property="tpModal" serializable="false"/>
		<adsm:combobox
			label="servico"
			property="servico.idServico"
			optionProperty="idServico"
			optionLabelProperty="dsServico"
			service="lms.configuracoes.servicoService.find"
			onlyActiveValues="true"
			labelWidth="17%"
			width="83%"
			boxWidth="231"
			onchange="onChangeServico()"/>
			
		<adsm:hidden property="tpEmpresa" value="M" serializable="false"/>
		<adsm:lookup
			label="filialOrigem"
			idProperty="idFilial"
			property="filialByIdFilialOrigem"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilial"
			action="/municipios/manterFiliais"
			onchange="return onChangeFilialOrigem();"
			onDataLoadCallBack="onLoadFilialOrigem"
			onPopupSetValue="onSetValueFilialOrigem"
			afterPopupSetValue="afterSetValueFilial"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="83%"
			exactMatch="true"
			required="true"
			criteriaSerializable="true">
			
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="tpAcesso" value="A" serializable="false"/>	
		<adsm:hidden property="tpFilialDestino" value="" serializable="false"/>		
		<adsm:lookup
			label="filialDestino"
			idProperty="idFilial"
			property="filialByIdFilialDestino"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilial"
			action="/municipios/manterFiliais"
			onchange="return onChangeFilialDestino();"
			onDataLoadCallBack="onLoadFilialDestino"
			onPopupSetValue="onSetValueFilialDestino"
			afterPopupSetValue="afterSetValueFilial"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%"
			exactMatch="true"
			required="true"
			criteriaSerializable="true">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping modelProperty="empresa.pessoa.nmPessoa" relatedProperty="empresaDestino" disable="false"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="tpFilialDestino" modelProperty="empresa.tpEmpresa.value"/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia"
					size="30" maxLength="50" disabled="true" serializable="true" />
		</adsm:lookup>

		<adsm:textbox
			label="empresaDestino"
			property="empresaDestino"
			dataType="text"
			disabled="true"
			labelWidth="17%"
			width="33%"
			size="30"
			serializable="false"/>

		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:lookup
			label="filialReembarcadora"
			idProperty="idFilial"
			property="filialByIdFilialReembarcadora"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilial"
			action="/municipios/manterFiliais"
			onchange="return filialReemChange(this);"
			onDataLoadCallBack="filialReembLoad"
			onPopupSetValue="filialReembPopup"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="83%"
			cellStyle="vertical-align:middle"
			exactMatch="true">
			<adsm:propertyMapping relatedProperty="filialByIdFilialReembarcadora.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox
				dataType="text"
				property="filialByIdFilialReembarcadora.pessoa.nmFantasia"
				size="30"
				maxLength="50"
				disabled="true"
				serializable="true"
				cellStyle="vertical-align:middle"
			/>
		</adsm:lookup>

		<adsm:hidden property="tpEmpresaParceira" value="P" serializable="false"/>
		<adsm:lookup
			label="filialParceira"
			property="filialByIdFilialParceira"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilialParceiraVigenteEm"
			action="/municipios/manterFiliais"
			onchange="return filialParceiraChange(this);"
			onDataLoadCallBack="filialParceiraLoad"
			onPopupSetValue="filialParceiraPopup"			
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%"
			exactMatch="true">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresaParceira" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping modelProperty="empresa.pessoa.nmPessoa" relatedProperty="empresaParceira" disable="false"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialParceira.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialParceira.pessoa.nmFantasia"
					size="30" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:textbox
			label="empresaParceira"
			property="empresaParceira"
			dataType="text"
			disabled="true"
			labelWidth="17%"
			width="33%"
			size="30"
			serializable="false"/>		

		<adsm:listbox
			label="fluxoReembarque"
			property="fluxoReembarque"
			optionProperty=""
			optionLabelProperty="fluxo"
			size="3"
			serializable="false"
			labelWidth="17%"
			width="83%"
			boxWidth="231"/>

		<adsm:textbox dataType="integer" property="nrDistancia" label="distancia"
				size="4" maxLength="6" labelWidth="17%" width="83%" required="true" unit="km2" />	

		<adsm:textbox dataType="integer" property="nrPrazoView" label="tempo"
				size="4" maxLength="3" required="true" labelWidth="17%" width="83%" unit="h" />

		<adsm:multicheckbox 
			texts="dom|seg|ter|qua|qui|sex|sab|"
			property="blDomingo|blSegunda|blTerca|blQuarta|blQuinta|blSexta|blSabado|"
			align="top" label="dias2" width="83%" labelWidth="17%"
		/>

		<adsm:textbox
			dataType="integer" property="nrGrauDificuldade" label="grauDificuldade"
			maxLength="6" size="6" labelWidth="17%" width="83%" unit="km2"
		/>

		<adsm:checkbox property="blPorto" label="portoEntreFiliais" width="83%" labelWidth="17%" />
		
		<adsm:checkbox property="blFluxoSubcontratacao" label="possuiSubcontratacao" width="33%" labelWidth="17%" 
			serializable="true" />

		
		<adsm:lookup label="empresaSubcontratada" dataType="text" property="empresaSubcontratada" idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado"
					 service="lms.indenizacoes.consultarReciboIndenizacaoAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas" 
					 exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="17%" width="83%" disabled="true">
     		<adsm:propertyMapping relatedProperty="empresaSubcontratada.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:textbox dataType="text" property="empresaSubcontratada.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
 
		<adsm:buttonBar>
			<adsm:button id="botaoClonar" caption="clonar" onclick="onclickClonar()" disabled="true"/>
			<adsm:storeButton id="botaoSalvar" callbackProperty="afterStore"/>
			<adsm:newButton id="__botaoNovo"/>
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript"><!--

	document.getElementById("dtVigenciaInicial").onblur = desabilitarFilialReembarcadora;
    var manipulaPorto = true;
    
    
    document.getElementById("blFluxoSubcontratacao").onclick =blFluxoSubcontratacaoChange;
    
    function blFluxoSubcontratacaoChange(){
    	var habilitaSubcontratacao = getElementValue("blFluxoSubcontratacao");
    	setDisabled("empresaSubcontratada.idPessoa", !habilitaSubcontratacao);
    	getElement("empresaSubcontratada.idPessoa").required = habilitaSubcontratacao;
    	if(!habilitaSubcontratacao){
    		resetValue("empresaSubcontratada.idPessoa");
    	}
    }

	function onclickClonar(){
		var sdo = createServiceDataObject("lms.municipios.manterFluxoCargaFiliaisAction.getDadosSessao", "onclickClonar");
		xmit({serviceDataObjects:[sdo]});	
	}

	function onclickClonar_cb(data, error){
		setElementValue("blClonar", "true");
		setDisabled("dtVigenciaInicial",false);
		setElementValue("dtVigenciaInicial", setFormat(document.getElementById("dtVigenciaInicial"), data.dataAtual));
		setDisabled("dtVigenciaFinal",false);
		setElementValue("dtVigenciaFinal", "");
		setDisabled("filialByIdFilialReembarcadora.idFilial",false);
		setDisabled("nrDistancia",false);
		setDisabled("nrPrazoView",false);
		setDisabled("blDomingo",false);
		setDisabled("blSegunda",false);
		setDisabled("blTerca",false);
		setDisabled("blQuarta",false);
		setDisabled("blQuinta",false);
		setDisabled("blSexta",false);
		setDisabled("blSabado",false);
		setDisabled("blFluxoSubcontratacao",false);
		setDisabled("nrGrauDificuldade",true);
		setDisabled("botaoClonar",true);
		setDisabled("botaoSalvar",false);
		blFluxoSubcontratacaoChange();
	}
	
	function desabilitarFilialReembarcadora(){
		if(getElementValue("dtVigenciaInicial") != ""){
			setDisabled("filialByIdFilialReembarcadora.idFilial", false);
		}else{
			resetValue("filialByIdFilialReembarcadora.idFilial");
			limpaFluxoReembarque();			
			setDisabled("filialByIdFilialReembarcadora.idFilial", true);
		}			
	}

	/**
	* Retorna estado dos campos como foram carregados na página.
	*/
	function estadoNovo() {
		setDisabled(document, false);
		setDisabled("empresaDestino", true);
		setDisabled("empresaParceira", true);
		setDisabled("botaoExcluir", true);
		setDisabled("filialByIdFilialOrigem.pessoa.nmFantasia", true);
		setDisabled("filialByIdFilialDestino.pessoa.nmFantasia", true);
		setDisabled("filialByIdFilialReembarcadora.pessoa.nmFantasia", true);
		setDisabled("filialByIdFilialParceira.pessoa.nmFantasia", true);
		setDisabled("fluxoReembarque", true);
		manipulaPorto = false;
		setDisabled("blPorto", true);
		setFocus(document.getElementById("servico.idServico"));
		blFluxoSubcontratacaoChange();
	}

	/**
	* Ao carregar os dados, é preenchido a listbox fluxo de reembarque.
	* Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	*/
	function fluxoFilialLoad_cb(data, exception, errorCode) {
		onDataLoad_cb(data,exception);
		if (errorCode == "LMS-29155") {
			newButtonScript(document, true, {name:'newItemButton_click'});
		} else if (data != undefined) {
			comportamentoDetalhe(data);
		    comportamentoPortoEntreFiliais(document.getElementById("tpSituacao").value,document.getElementById("tpModal").value);
		}
		
	}

	//funcao chamada no onchange da combo, ao selecionar um servico, a filial reembarcadora é excluida.
	//esta funcao também dispara um evento de controle para habilitar/desabilitar o checkbox blPorto
	function onChangeServico() {
		buscaDadosUltimoFluxo();
		buscaDadosServico();
		limpaFilial("Reembarcadora");
		limpaFluxoReembarque();
		//tratarServicoRodoviario();
	}
	
	function comportamentoPortoEntreFiliais(tpSituacao,tpModal){
		if (manipulaPorto == true) {
			
			setDisabled("blPorto", false);
			
			if(tpSituacao!='' && tpModal != '') {
				setElementValue("tpSituacao", tpSituacao);
				setElementValue("tpModal", tpModal);
				
				if (tpSituacao == 'A' && tpModal != 'R'){
					setDisabled("blPorto", true);
					resetValue("blPorto");
				}
			}
		}
	}

	function afterStore_cb(data,exception,key){
		store_cb(data, exception, key);
		if (!exception) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
			setElementValue("blClonar","false");
		}
	}

	function comportamentoDetalhe(data) {
		
		var idFilialParceira = getNestedBeanPropertyValue(data, "filialByIdFilialParceira.idFilial");
		//acaoVigenciaAtual = 0 se antes vigência, 1 se vigente, 2 se depois da vigência.
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");

		var cnpjSubcontratada = getNestedBeanPropertyValue(data,"empresaSubcontratada.pessoa.nrIdentificacao");
		if (cnpjSubcontratada){
			setElementValue("empresaSubcontratada.pessoa.nrIdentificacao",cnpjSubcontratada);
		}
		
		setDisabled(document,true);
		if (acaoVigenciaAtual == 0) {
			setDisabledFiliais(true);
			//setDisabled("filialByIdFilialReembarcadora.idFilial", false);
			var tpFilialDestino = getNestedBeanPropertyValue(data, "tpFilialDestino");
			if (tpFilialDestino == null || tpFilialDestino==undefined){
				tpFilialDestino = getElementValue("tpFilialDestino");
			}
			if (tpFilialDestino == 'P'){
				//setDisabled("filialByIdFilialParceira.idFilial", false);
			}
			setDisabled("botaoExcluir", false);
			setDisabled("__botaoNovo",false);
			setDisabled("__buttonBar:0.storeButton",false);
			setDisabled("botaoClonar", false);
			setDisabled("dtVigenciaFinal",false);
			setFocus("dtVigenciaFinal");
		} else if (acaoVigenciaAtual == 1) {
			setDisabled("__botaoNovo",false);
			setDisabled("__buttonBar:0.storeButton",false);
			setDisabled("botaoClonar", false);
			setDisabled("botaoExcluir",true);
			setDisabled("dtVigenciaFinal",false);
			setFocus("dtVigenciaFinal");
		} else if (acaoVigenciaAtual == 2) {
			setDisabled("__botaoNovo",true);
			setFocusOnNewButton();
		}
	}

	/**
	* tratamento dos eventos da initWindow para <tab_click>, 
	* <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	*/
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" || eventObj.name != "storeButton")
			estadoNovo(eventObj);

			var state = (eventObj.name == "gridRow_click" || eventObj.name == "storeButton");
			setDisabledFiliais(state);
	
			/*Ao entrar na tela deve ser desabilitado a filial reembarcadora*/
			if (eventObj.name == "tab_click" || eventObj.name == "newButton_click"){
				setDisabled("filialByIdFilialReembarcadora.idFilial", true);
				setDisabled("filialByIdFilialParceira.idFilial", true);
				manipulaPorto = true;
				if (getElementValue("idFluxoFilial")){
				//desabilitado temporariamente.
					alert('entrou');
					setDisabled("botaoClonar",false);
			}else{
				setDisabled("botaoClonar",true);
			}
		}
		blFluxoSubcontratacaoChange();
	}

	function setDisabledFiliais(state) {
		setDisabled("servico.idServico", state);
		setDisabled("filialByIdFilialOrigem.idFilial", state);
		setDisabled("filialByIdFilialDestino.idFilial", state);
		setDisabled("filialByIdFilialReembarcadora.idFilial", state);
		setDisabled("filialByIdFilialParceira.idFilial", state);
	}

	/**
	* retorna true quando a filial for igual à alguma no array.
	*/
	function validaFilial(idFilial,filiais) {
		if (filiais.length > 0 ) {
			for (i = 0 ; i < filiais.length ; i++)
				if (idFilial == getElementValue(filiais[i]))
					return true;
		}
		return false;		
	}

	/**
	* retorna true quando a filial ORIGEM for igual à alguma no array.
	*/
	function validaFilialOrigem(idFilial, dialogWindow) {
		var filiais = new Array(1);
		filiais[0] = "filialByIdFilialReembarcadora.idFilial";
		if (validaFilial(idFilial, filiais)) {
			if(dialogWindow != undefined) {
				dialogWindow.close();
			}
			alertI18nMessage("LMS-29026");
			return true;
		}
		return false;
	}

	function onLoadFilialOrigem_cb(data,exception) {
		if (data != undefined ) {
			var idFilial = getNestedBeanPropertyValue(data,":0.idFilial");
			if (validaFilialOrigem(idFilial)) {
				resetValue("filialByIdFilialOrigem.sgFilial");
				setFocus("filialByIdFilialOrigem.sgFilial");
				return false;
			}
			var result = filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
			buscaDadosUltimoFluxo();
			return result;
		}
		return true;
	}

	function onSetValueFilialOrigem(data, dialogWindow) {
		if (data != undefined) {
			var idFilial = getNestedBeanPropertyValue(data,":idFilial");
			var flag = validaFilialOrigem(idFilial,dialogWindow);
			if(flag == true)
				setFocus("filialByIdFilialOrigem.sgFilial");
			return (!flag);
		}
		return true;
	}

	function onChangeFilialOrigem() {
		limpaDadosUltimoFluxo();
		return filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}

	function afterSetValueFilial() {
		buscaDadosUltimoFluxo();
	}

	/**
	* retorna true quando a filial DESTINO for igual à alguma no array.
	* retorna false quando a filial REEMBARCADORA for vazia
	*/
	function validaFilialDestino(idFilial,dialogWindow) {
		var filiais = new Array(1);
		filiais[0] = "filialByIdFilialReembarcadora.idFilial";
		if (validaFilial(idFilial,filiais)) {
			if(dialogWindow != undefined) {
				dialogWindow.close();
			}
			alertI18nMessage("LMS-29025");
			return true;
		} else if (idFilial != undefined)
			fluxoByFilialDestino(idFilial);
		return false;
	}

	function onLoadFilialDestino_cb(data,exception) {
		if (data != undefined) {
			var idFilial = getNestedBeanPropertyValue(data,":0.idFilial");
			if (validaFilialDestino(idFilial)) {
				resetValue("filialByIdFilialDestino.sgFilial");
				setFocus("filialByIdFilialDestino.sgFilial");
				return false;
			}
			var result = filialByIdFilialDestino_sgFilial_exactMatch_cb(data);
			buscaDadosUltimoFluxo();

			if(data.length == 1) {
				executaComportamentoFiliais(getElementValue("filialByIdFilialReembarcadora.idFilial"),
						idFilial, getElementValue("tpFilialDestino"));
			}
			
			return result;
		}
		fluxoReembarqueListboxDef.cleanRelateds();
		return true;
	}

	function onSetValueFilialDestino(data,dialogWindow) {
		if (data != undefined) {
			setElementValue("tpFilialDestino", getNestedBeanPropertyValue(data,":empresa.tpEmpresa.value"));
			limpaFluxoReembarque();
			var idFilial = getNestedBeanPropertyValue(data,":idFilial");
			var flag = validaFilialDestino(idFilial,dialogWindow);
			if(flag == true) {
				setFocus("filialByIdFilialDestino.sgFilial");
			} else {
				executaComportamentoFiliais(getElementValue("filialByIdFilialReembarcadora.idFilial"),
						idFilial, getElementValue("tpFilialDestino"));
			}
			return (!flag);
		}
		fluxoReembarqueListboxDef.cleanRelateds();
		return true;
	}

	function onChangeFilialDestino() {
		limpaDadosUltimoFluxo();
		var retorno = filialByIdFilialDestino_sgFilialOnChangeHandler();
		var idFilialDestino = getElementValue("filialByIdFilialDestino.idFilial");
		if(idFilialDestino == "") {
			limpaFluxoReembarque();
		}
		var sgFilialDestino = getElementValue("filialByIdFilialDestino.sgFilial");
		if (sgFilialDestino == ""){
			executaComportamentoFiliais(getElementValue("filialByIdFilialReembarcadora.idFilial"),
					idFilialDestino, getElementValue("tpFilialDestino"));
			setDisabled("filialByIdFilialParceira.idFilial", true);
	}
		return retorno;
	}

	/**
	* retorna true quando a filial REEMBARCADORA for igual à alguma no array.
	* retorna false quando a filial DESTINO for vazia
	*/
	function validaFilialReemb(idFilial,dialogWindow) {
		var filiais = new Array(2);
		filiais[0] = "filialByIdFilialOrigem.idFilial";
		filiais[1] = "filialByIdFilialDestino.idFilial";
		if (validaFilial(idFilial,filiais)) {
			if(dialogWindow != undefined) {
				dialogWindow.close();
			}
			alertI18nMessage("LMS-29024");
			return true;
		} else if (idFilial != undefined)
			fluxoByFilialReemb(idFilial);
		return false;
	}

	function filialReembLoad_cb(data,exception) {
		if (data != undefined ) {
			if(data.length == 1) {			
				var idFilial = getNestedBeanPropertyValue(data,":0.idFilial");
			}
			if (validaFilialReemb(idFilial)) {
				resetValue("filialByIdFilialReembarcadora.sgFilial");
				setFocus("filialByIdFilialReembarcadora.sgFilial");
				return false;
		} else {
				var retorno = filialByIdFilialReembarcadora_sgFilial_exactMatch_cb(data);
				if (retorno) {
					//CQPRO00022947
					executaComportamentoFiliais(idFilial,
							getElementValue("filialByIdFilialDestino.idFilial"), getElementValue("tpFilialDestino"));
					limpaFilial("Parceira");
					//setDisabled("filialByIdFilialParceira.idFilial", true);
					setFocus("nrDistancia");
					//FIM CQPRO00022947
				}
				return retorno;
			}
		} else {
			fluxoReembarqueListboxDef.cleanRelateds();
			return true;		
		}
	}

	function filialReembPopup(data,dialogWindow) {
		if (data != undefined) {
			limpaFluxoReembarque();
			var idFilial = getNestedBeanPropertyValue(data,":idFilial");
			var flag = validaFilialReemb(idFilial,dialogWindow);
			if(flag == true) {
				setFocus("filialByIdFilialReembarcadora.sgFilial");
			} else {
				//CQPRO00022947
				executaComportamentoFiliais(idFilial,
						getElementValue("filialByIdFilialDestino.idFilial"), getElementValue("tpFilialDestino"));

				limpaFilial("Parceira");
				//setDisabled("filialByIdFilialParceira.idFilial", true);
				setFocus("nrDistancia");
				//FIM CQPRO00022947
			}
			return (!flag);
		} else {
			fluxoReembarqueListboxDef.cleanRelateds();
			return true;
		}
	}

	function filialReemChange(eThis){
		if (eThis.value == "") {
			//setDisabled("filialByIdFilialParceira.idFilial", false);
			limpaFluxoReembarque();
		}
		return filialByIdFilialReembarcadora_sgFilialOnChangeHandler();
	}

	function filialParceiraLoad_cb(data,exception) {
		if (data != undefined ) {
			var retorno = filialByIdFilialParceira_sgFilial_exactMatch_cb(data);
			if (retorno) {
				//CQPRO00022947
				limpaFilial("Reembarcadora");
				limpaFluxoReembarque();
		        // Coloca a filial parceira
		        // dentro do listBox de fluxoReembarque                
		        var opt = document.createElement("option");		
		        document.getElementById("fluxoReembarque").options.add(opt);        // Assign text and value to Option object
		        opt.text = document.getElementById("filialByIdFilialParceira.sgFilial").value + 
		        				" - " + document.getElementById("filialByIdFilialParceira.pessoa.nmFantasia").value;
		        opt.value = document.getElementById("filialByIdFilialParceira.idFilial").value;
        
				setFocus("nrDistancia");
				//FIM CQPRO00022947
			}
			return retorno;
		}
		return true;		
	}

	function filialParceiraPopup(data,dialogWindow) {
		if (data != undefined) {
			//CQPRO00022947
			limpaFilial("Reembarcadora");		
			limpaFluxoReembarque();
			setFocus("nrDistancia");
			//FIM CQPRO00022947		
			return true;
		}
		return true;
	}
	
	function filialParceiraChange(eThis){
		limpaDadosUltimoFluxo();
		if (eThis.value == "") {
			limpaFluxoReembarque();
		}
		return filialByIdFilialParceira_sgFilialOnChangeHandler();
	}
	
	function limpaFluxoReembarque() {
		while(document.getElementById("fluxoReembarque").length != 0)
			document.getElementById("fluxoReembarque")[0] = null;
	}

	function buscaDadosUltimoFluxo() {
		var idFilialOrigem = getElementValue("filialByIdFilialOrigem.idFilial");
		var idFilialDestino = getElementValue("filialByIdFilialDestino.idFilial");
		var idServico = getElementValue("servico.idServico");

		//Somente busca dados se as filiais estiverem preenchidas
		if(idFilialOrigem && idFilialDestino) {
			var data = new Array();
			setNestedBeanPropertyValue(data, "filialByIdFilialOrigem.idFilial", idFilialOrigem);
			setNestedBeanPropertyValue(data, "filialByIdFilialDestino.idFilial", idFilialDestino);
			setNestedBeanPropertyValue(data, "servico.idServico", idServico);

			var sdo = createServiceDataObject("lms.municipios.manterFluxoCargaFiliaisAction.findDadosUltimoFluxo", "findDadosUltimoFluxo", data);
			xmit({serviceDataObjects:[sdo]});
		}
		limpaDadosUltimoFluxo();
	}

	function findDadosUltimoFluxo_cb(data, error) {
		if(error) {
			alert(error);
			return false;
		}
		setElementValue("nrDistancia", getNestedBeanPropertyValue(data, "nrDistancia"));
		setElementValue("nrPrazoView", getNestedBeanPropertyValue(data, "nrPrazoView"));
		setElementValue("nrGrauDificuldade", getNestedBeanPropertyValue(data, "nrGrauDificuldade"));

	}
	
	function buscaDadosServico() {
		
		var idServico = getElementValue("servico.idServico");

		//Somente busca dados se o serviço estiver preenchido
		if(idServico) {
			var data = new Array();
			setNestedBeanPropertyValue(data, "servico.idServico", idServico);

			var sdo = createServiceDataObject("lms.municipios.manterFluxoCargaFiliaisAction.findDadosServico", "findDadosServico", data);
			xmit({serviceDataObjects:[sdo]});
			
			/* var tpModal = getNestedBeanPropertyValue(data,"tpModal");
			var tpAbrangencia = getNestedBeanPropertyValue(data,"tpAbrangencia"); */
			
			
		}

		limpaDadosServico();
	}

	function findDadosServico_cb(data, error) {
		if(error) {
			alert(error);
			return false;
		}
		
		setElementValue("tpSituacao", getNestedBeanPropertyValue(data, "tpSituacao"));
		setElementValue("tpModal", getNestedBeanPropertyValue(data, "tpModal"));
				
		comportamentoPortoEntreFiliais(document.getElementById("tpSituacao").value,document.getElementById("tpModal").value);
		
		var stsBlPorto = getNestedBeanPropertyValue(data,"stsBlPorto");
		
		if(stsBlPorto=='S') {
				
			setDisabled("blPorto",false);
				
		} else {
				
			setElementValue("blPorto",false);
				
			setDisabled("blPorto",true);
			
		}
		
		
		
	}
	
	function limpaDadosServico() {
		resetValue("tpSituacao");
		resetValue("tpModal");
	}
	
	function limpaDadosUltimoFluxo() {
		resetValue("nrDistancia");
		resetValue("nrPrazoView");
		resetValue("nrGrauDificuldade");
	}

	function fluxoByFilialDestino(id) {
		if (getElementValue("filialByIdFilialReembarcadora.idFilial") != "")
			callFindFluxoReembarqueDest(id);
		else
			return true;
	}

	function fluxoByFilialReemb(id) {
		if (getElementValue("filialByIdFilialDestino.idFilial") != "")
			callFindFluxoReembarqueReemb(id);
		else
			return true;
	}

	/**
	* xmit para consultar fluxo de reembarque.
	*/
	function callFindFluxoReembarqueDest(id) {
		var data = new Array();

		setNestedBeanPropertyValue(data, "idFluxoFilial", getElementValue("idFluxoFilial"));
		setNestedBeanPropertyValue(data, "idFilialDestino", id);
		setNestedBeanPropertyValue(data, "idFilialReembarcadora", getElementValue("filialByIdFilialReembarcadora.idFilial"));
		setNestedBeanPropertyValue(data, "idFilialParceira", getElementValue("filialByIdFilialParceira.idFilial"));
		setNestedBeanPropertyValue(data, "idServico", getElementValue("servico.idServico"));
		setNestedBeanPropertyValue(data, "which", "dest");
		setNestedBeanPropertyValue(data, "dtVigenciaInicial", getElementValue("dtVigenciaInicial"));
		setNestedBeanPropertyValue(data, "dtVigenciaFinal", getElementValue("dtVigenciaFinal"));

		var sdo = createServiceDataObject("lms.municipios.manterFluxoCargaFiliaisAction.findFluxoReembarque", "findFluxoReembarqueDest", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findFluxoReembarqueDest_cb(data,exception) {
		if (exception != null) {
			alert(exception);
			limpaFilial("Destino");
			fluxoReembarqueListboxDef.cleanRelateds();
			setFocus("filialByIdFilialDestino.sgFilial");
		} else {
			limpaFluxoReembarque();
			fluxoReembarque_cb(data);
			setFocus("filialByIdFilialReembarcadora.sgFilial");
		}
	}

	/**
	* xmit para consultar fluxo de reembarque.
	*/
	function callFindFluxoReembarqueReemb(id) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFluxoFilial", getElementValue("idFluxoFilial"));
		setNestedBeanPropertyValue(data, "idFilialDestino", getElementValue("filialByIdFilialDestino.idFilial"));
		setNestedBeanPropertyValue(data, "idFilialReembarcadora", id);
		setNestedBeanPropertyValue(data, "idServico", getElementValue("servico.idServico"));
		setNestedBeanPropertyValue(data, "which", "reemb");
		setNestedBeanPropertyValue(data, "dtVigenciaInicial", getElementValue("dtVigenciaInicial"));
		setNestedBeanPropertyValue(data, "dtVigenciaFinal", getElementValue("dtVigenciaFinal"));

		var sdo = createServiceDataObject("lms.municipios.manterFluxoCargaFiliaisAction.findFluxoReembarque", "findFluxoReembarqueReemb", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findFluxoReembarqueReemb_cb(data,exception) {
		if (exception != null) {
			alert(exception);
			limpaFilial("Reembarcadora");
			fluxoReembarqueListboxDef.cleanRelateds();
			setFocus(document.getElementById("filialByIdFilialReembarcadora.sgFilial"));
		} else {
			limpaFluxoReembarque();
			fluxoReembarque_cb(data);
			setFocus(document.getElementById("fluxoReembarque"));
		}
	}

	/**
	* xmit para consultar fluxo de reembarque.
	*/
	function callFindFluxoReembarqueParc(id) {
		var data = new Array();

		setNestedBeanPropertyValue(data, "idFluxoFilial", getElementValue("idFluxoFilial"));
		setNestedBeanPropertyValue(data, "idFilialDestino", getElementValue("filialByIdFilialDestino.idFilial"));
		setNestedBeanPropertyValue(data, "idFilialReembarcadora", getElementValue("filialByIdFilialReembarcadora.idFilial"));
		setNestedBeanPropertyValue(data, "idFilialParceira", id);
		setNestedBeanPropertyValue(data, "idServico", getElementValue("servico.idServico"));
		setNestedBeanPropertyValue(data, "which", "parc");
		setNestedBeanPropertyValue(data, "dtVigenciaInicial", getElementValue("dtVigenciaInicial"));
		setNestedBeanPropertyValue(data, "dtVigenciaFinal", getElementValue("dtVigenciaFinal"));
		var sdo = createServiceDataObject("lms.municipios.manterFluxoCargaFiliaisAction.findFluxoReembarque", "findFluxoReembarqueParc", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findFluxoReembarqueParc_cb(data,exception) {
		if (exception != null) {
			alert(exception);
			limpaFilial("Parceira");
			fluxoReembarqueListboxDef.cleanRelateds();
			setFocus("filialByIdFilialParceira.sgFilial");
		} else {
			limpaFluxoReembarque();
			fluxoReembarque_cb(data);
			setFocus("filialByIdFilialParceira.sgFilial");
		}
	}

	function limpaFilial(tipo) {
		var filial = "filialByIdFilial" + tipo;
		resetValue(filial + ".idFilial");
		resetValue(filial + ".sgFilial");
		resetValue(filial + ".pessoa.nmFantasia");
	}

	//CQPRO00022947
	function executaComportamentoFiliais(idFilialReembarcadora, idFilialDestino, tpFilialDestino){
		if ( (idFilialReembarcadora == "" || idFilialReembarcadora == undefined)
				&& (idFilialDestino != "" && idFilialDestino != undefined)
				&& (tpFilialDestino == "P")) {
			//habilita filial parceira
			setDisabled("filialByIdFilialParceira.idFilial", false);
		} else {
			//desabilita filial parceira
			limpaFilial("Parceira");
		}
	}
</script>