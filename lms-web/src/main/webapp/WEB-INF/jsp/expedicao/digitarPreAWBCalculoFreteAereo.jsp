<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarPreAWBCalculoFreteAction" onPageLoad="myOnPageLoad">
	<adsm:section caption="calculoFreteAereoTitulo" width="85%" />

	<adsm:grid
		idProperty="idCalculoFrete"
		property="freteList"
		unique="false"
		showPagging="false"
		selectionMode="radio"
		gridHeight="80"
		width="603"
		scrollBars="vertical"
		disableMarkAll="true"
		autoSearch="false"
		showTotalPageCount="false"
		service="lms.expedicao.digitarPreAWBCalculoFreteAction.calculaFreteAwb"
		onDataLoadCallBack="freteListDataLoadCallBack"
		onRowClick="freteRowClick">

		<adsm:gridColumn
			title="ciaAerea"
			property="nmPessoa"
			width="153"/>

		<adsm:gridColumn
			title="tabela"
			property="dsTabela"
			width="200"/>

		<adsm:gridColumn
			dataType="currency"
			title="valorFrete"
			property="vlTotal"
			width="150"
			align="right"
			unit="reais"/>

		<adsm:gridColumn 
			title="tarifaSpot" 
			property="blTarifaSpot" 
			renderMode="image-check"
			width="100"/>		
	</adsm:grid>

	<adsm:form 
		id="calculoForm"
		action="/expedicao/digitarPreAWBCalculoFreteAereo">

		<adsm:label key="espacoBranco" style="border:none;" width="100%" />
		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:hidden property="dsJustificativaPrejuizo" />

		<adsm:textbox 
			dataType="text" 
			property="nrVooPrevisto" 
			required="true"
			label="numeroVooPrevisto" 
			maxLength="60"
			size="10" 
			labelWidth="16%" 
			width="31%"
			disabled="true" />

		<adsm:textbox 
			dataType="JTDateTimeZone" 
			property="dhSaida" 
			required="true"
			label="dataHoraDeSaida" 
			smallerThan="dhChegada"
			labelWidth="19%"
			width="33%"
			disabled="true" />

		<adsm:label key="branco" style="border:none;" width="1%" />

		<%-----------------------------%>
		<%-- AEROPORTO ESCALA LOOKUP --%>
		<%-----------------------------%>
		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.digitarPreAWBCalculoFreteAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoEscala"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="via"
			size="3"
			maxLength="3"
			labelWidth="16%"
			width="31%"
			disabled="true">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoEscala.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoEscala.pessoa.nmPessoa" 
				serializable="false"
				size="20"
				maxLength="60"
				disabled="true"/>

		</adsm:lookup>

		<adsm:textbox 
			dataType="JTDateTimeZone" 
			property="dhChegada" 
			required="true"
			label="dataHoraDeChegada" 
			biggerThan="dhSaida"
			labelWidth="19%" 
			width="33%"
			disabled="true" />

		<adsm:buttonBar freeLayout="false">
			<adsm:button 
				id="btnCancelar"
				caption="cancelar"
				disabled="false"
				onclick="self.close();"/>

			<adsm:button 
				id="btnSalvar"
				caption="salvar"
				onclick="salvar();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
var _argumentsData;

function myOnPageLoad() {
	onPageLoad();

	// aeroportoEscala
	setElementValue("aeroportoByIdAeroportoEscala.idAeroporto", dialogArguments.getElementValue("aeroportoByIdAeroportoEscala.idAeroporto"));
	setElementValue("aeroportoByIdAeroportoEscala.sgAeroporto", dialogArguments.getElementValue("aeroportoByIdAeroportoEscala.sgAeroporto"));
	setElementValue("aeroportoByIdAeroportoEscala.pessoa.nmPessoa", dialogArguments.getElementValue("aeroportoByIdAeroportoEscala.pessoa.nmPessoa"));

	setElementValue("nrVooPrevisto", dialogArguments.getElementValue("dsVooPrevisto"));
	setElementValue("dhSaida", setFormat("dhSaida", dialogArguments.getElementValue("dhPrevistaSaida")));
	setElementValue("dhChegada", setFormat("dhChegada", dialogArguments.getElementValue("dhPrevistaChegada")));

	setDisabled("btnCancelar", false);

	populaGrid();
}

function populaGrid() {
	/*
	var idAeroportoOrigem = dialogArguments.getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto");
	var idAeroportoDestino = dialogArguments.getElementValue("aeroportoByIdAeroportoDestino.idAeroporto");
	var idProdutoEspecifico = dialogArguments.getElementValue("produtoEspecifico.idProdutoEspecifico");
	var idExpedidor = dialogArguments.getElementValue("clienteByIdClienteRemetente.idCliente");
	var psRealInformado = stringToNumber(dialogArguments.getElementValue("psReal"));
	var psCubadoInformado = stringToNumber(dialogArguments.getElementValue("psCubado"));
	var tpFrete = dialogArguments.getElementValue("tpFrete");
	var dsSenha = dialogArguments.getElementValue("tarifaSpot.dsSenha");
	var idTarifaSpot = dialogArguments.getElementValue("tarifaSpot.idTarifaSpot");

	_argumentsData = {
		idAeroportoOrigem : idAeroportoOrigem,
		idAeroportoDestino : idAeroportoDestino,
		idProdutoEspecifico : idProdutoEspecifico,
		idExpedidor : idExpedidor,
		psRealInformado : psRealInformado,
		psCubadoInformado : psCubadoInformado,
		tpFrete : tpFrete,
		tarifaSpot : {
			dsSenha : dsSenha,
			idTarifaSpot : idTarifaSpot
		}
	}
	*/
	_argumentsData = {};
	freteListGridDef.executeSearch(_argumentsData, null, null, true);
}

function changeFieldsStatus(status) {
	setDisabled("nrVooPrevisto", status);
	setDisabled("dhSaida", status);
	setDisabled("aeroportoByIdAeroportoEscala.idAeroporto", status);
	setDisabled("dhChegada", status);
	setDisabled("btnSalvar", status);
}

function freteListDataLoadCallBack_cb(data, error) {
	if(error != undefined) {
		alert(error);
		self.close();
	}
	if(data != undefined && data.length > 0) {
		var radioArray = document.getElementsByName("freteList:select-radio");
		radioArray[0].checked = true;
		changeFieldsStatus(false);
		setFocus("nrVooPrevisto", false);
	}
}

function salvar() {
	var calculoForm = getElement("calculoForm");
	var valid = validateTabScript(calculoForm);
	if(valid == true) {
		var selectedId = freteListGridDef.getSelectedIds().ids[0];
		var gridData = freteListGridDef.findById(selectedId);		
		
		var data = {
			idCalculoFrete : selectedId,
			idAeroportoEscala : getElementValue("aeroportoByIdAeroportoEscala.idAeroporto"),
			dhChegada : getElementValue("dhChegada"),
			dhSaida : getElementValue("dhSaida"),
			nrVooPrevisto : getElementValue("nrVooPrevisto"),
			idTabelaPreco : gridData.idTabelaPreco, 
			dsJustificativaPrejuizo : getElementValue("dsJustificativaPrejuizo")
		}
		//var openerData = buildFormBeanFromForm(dialogArguments.document.forms[0]);

		//merge(data, openerData);

		var service = "lms.expedicao.digitarPreAWBAction.store";
		var sdo = createServiceDataObject(service, "store", data);
		xmit({serviceDataObjects:[sdo]});
	}
}

function store_cb(data, error, key) {
	if(error != undefined) {
		if(key == "LMS-04513"){
			alert(error);
			showJustificarAwbPrejuizo();
			if(getElementValue("dsJustificativaPrejuizo") != null && getElementValue("dsJustificativaPrejuizo") != ""){
				salvar();
			}
		} else {
			alert(error);
		}
		return;
	}
	
	setElementValue("dsJustificativaPrejuizo", "");
	var siglaDescricao = "";
	if(getElementValue("aeroportoByIdAeroportoEscala.idAeroporto") != "") {
		siglaDescricao = getElementValue("aeroportoByIdAeroportoEscala.sgAeroporto") + " - " + getElementValue("aeroportoByIdAeroportoEscala.pessoa.nmPessoa");
	}

	var selectedId = freteListGridDef.getSelectedIds().ids[0];
	var gridData = freteListGridDef.findById(selectedId);

	dialogArguments.setElementValue("ciaFilialMercurio.empresa.pessoa.nmPessoa", gridData.nmPessoa);
	dialogArguments.setElementValue("vlFrete", setFormat(dialogArguments.getElement("vlFrete"), ""+gridData.vlTotal));
	dialogArguments.setElementValue("dsVooPrevisto", getElementValue("nrVooPrevisto"));
	dialogArguments.setElementValue("dhPrevistaSaida", getElement("dhSaida").value);
	dialogArguments.setElementValue("aeroportoByIdAeroportoEscala.siglaDescricao", siglaDescricao);

	dialogArguments.setElementValue("aeroportoByIdAeroportoEscala.idAeroporto", getElementValue("aeroportoByIdAeroportoEscala.idAeroporto"));
	dialogArguments.setElementValue("aeroportoByIdAeroportoEscala.sgAeroporto", getElementValue("aeroportoByIdAeroportoEscala.sgAeroporto"));
	dialogArguments.setElementValue("aeroportoByIdAeroportoEscala.pessoa.nmPessoa", getElementValue("aeroportoByIdAeroportoEscala.pessoa.nmPessoa"));
	
	dialogArguments.setElementValue("clienteByIdClienteTomador.idCliente", data.idClienteTomador);
	dialogArguments.setElementValue("clienteByIdClienteTomador.pessoa.nrIdentificacao", data.nrIdentificacaoTomador);
	dialogArguments.setElementValue("nrCcTomadorServico", data.nrCcTomadorServico);

	dialogArguments.setElementValue("dhPrevistaChegada", getElement("dhChegada").value);
	//dialogArguments.setDisabled("botao.emitir", false);
	//dialogArguments.setDisabled("botao.calcularFrete", true);

	dialogArguments.setElementValue("awb.idAwb", data.idAwb);
	dialogArguments.setElementValue("dhDigitacao", setFormat(dialogArguments.getElement("dhDigitacao"), ""+data.dhDigitacao));

	self.close();
}

function showJustificarAwbPrejuizo() {
	showModalDialog('expedicao/justificarInclusaoAWBPrejuizo.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:490px;dialogHeight:160px;');
}

function freteRowClick(rowRef) {
	return false;
}
</script>