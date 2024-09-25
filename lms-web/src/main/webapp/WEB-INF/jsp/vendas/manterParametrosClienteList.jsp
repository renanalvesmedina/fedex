<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<%-- Parametro utilizado por rotaPreco.jsp --%>
<% Boolean blActiveValues = false; %>

<script language="javascript" type="text/javascript">

function addOptionToComboMasterLink(obj, data) {
	obj.masterLink = "true";
	setDisabled(obj, true);
	comboboxLoadOptions({e:obj, data:data});
	obj.selectedIndex = 1;
}

function myOnPageLoad_cb(dados,erros) {
	onPageLoad_cb(dados, erros);
	var u = new URL(parent.location.href);
	var origem = u.parameters["origem"];
	if(origem == "div") {
		var obj = getElement("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente");
		var data = [];
		data[data.length] = {idDivisaoCliente:getElementValue("idDivisaoCliente"), dsDivisaoCliente:getElementValue("dsDivisaoCliente")};
		addOptionToComboMasterLink(getElement("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente"), data);

		var tabelaLabel = getTabelaLabel();
		obj = getElement("tabelaDivisaoCliente.idTabelaDivisaoCliente");
		data = [];
		data[data.length] = {idTabelaDivisaoCliente:getElementValue("idTabelaDivisaoCliente"), tabelaPreco:{tabelaPrecoStringDescricao:tabelaLabel}};
		addOptionToComboMasterLink(getElement("tabelaDivisaoCliente.idTabelaDivisaoCliente"), data);
		setElementValue("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao", tabelaLabel);
	} else if(origem == "param") {
		notifyElementListeners({e:document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente")});
	}

	configTabelaPrecoListeners();	
}

function onShow() {
	tab_onShow();
	if(getElementValue("origem") == "param") {
		var frame = parent.document.frames["cad_iframe"];
		frame.setElementValue("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente", "");
	}
}

function getTabelaLabel() {
	var tabelaLabel = getElementValue("tipoTabelaPreco");
	if(getElementValue("nrVersao") != "") {
		tabelaLabel += getElementValue("nrVersao");
	}
	if(getElementValue("subtipoTabelaPreco") != "") {
		tabelaLabel += "-" + getElementValue("subtipoTabelaPreco");
	}
	if(getElementValue("dsDescricao") != "") {
		tabelaLabel += " - " + getElementValue("dsDescricao");
	}
	if(getElementValue("dsServico") != "") {
		tabelaLabel += " - " + getElementValue("dsServico");
	}
	return tabelaLabel;
}

function configTabelaPrecoListeners () {	
	if(document.getElementById("grupoRegiaoOrigem.idGrupoRegiao") != null) {		
		document.getElementById("grupoRegiaoOrigem.idGrupoRegiao").propertyMappings = [ 
		{ modelProperty:"unidadeFederativaByIdUf.idUnidadeFederativa", criteriaProperty:"unidadeFederativaByIdUfOrigem.idUnidadeFederativa", inlineQuery:true }, 
		{ modelProperty:"dsGrupoRegiao", relatedProperty:"grupoRegiaoOrigem.dsGrupoRegiao" },
		{ modelProperty:"tabelaDivisaoCliente.idTabelaDivisaoCliente", criteriaProperty:"tabelaDivisaoCliente.idTabelaDivisaoCliente",  inlineQuery:true }
		];
		document.getElementById("grupoRegiaoDestino.idGrupoRegiao").propertyMappings = [ 
		{ modelProperty:"unidadeFederativaByIdUf.idUnidadeFederativa", criteriaProperty:"unidadeFederativaByIdUfDestino.idUnidadeFederativa", inlineQuery:true }, 
		{ modelProperty:"dsGrupoRegiao", relatedProperty:"grupoRegiaoDestino.dsGrupoRegiao" },
		{ modelProperty:"tabelaDivisaoCliente.idTabelaDivisaoCliente", criteriaProperty:"tabelaDivisaoCliente.idTabelaDivisaoCliente" , inlineQuery:true }
		];
		addElementChangeListener({e:document.getElementById("tabelaDivisaoCliente.idTabelaDivisaoCliente"), changeListener: document.getElementById("grupoRegiaoOrigem.idGrupoRegiao")});
		addElementChangeListener({e:document.getElementById("tabelaDivisaoCliente.idTabelaDivisaoCliente"), changeListener: document.getElementById("grupoRegiaoDestino.idGrupoRegiao")});
	}

}
</script>

<adsm:window 
	onPageLoadCallBack="myOnPageLoad"
	service="lms.vendas.manterParametrosClienteAction"
>
	<adsm:i18nLabels>
		<adsm:include key="LMS-01024"/>
		<adsm:include key="LMS-30017"/>
	</adsm:i18nLabels>

	<adsm:form 
		action="/vendas/manterParametrosCliente" 
		height="140"
	>
		<adsm:hidden property="tpAcesso" value="A"/>
		<adsm:hidden property="origem"/>
		<adsm:hidden property="idDivisaoCliente"/>
		<adsm:hidden property="dsDivisaoCliente"/>

		<adsm:hidden property="idTabelaDivisaoCliente"/>
		<adsm:hidden property="tipoTabelaPreco"/>
		<adsm:hidden property="nrVersao"/>
		<adsm:hidden property="subtipoTabelaPreco"/>
		<adsm:hidden property="dsDescricao"/>
		<adsm:hidden property="sgMoeda"/>
		<adsm:hidden property="dsSimboloMoeda"/>
		<adsm:hidden property="dsServico"/>

		<%-------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%-------------------%>
		<adsm:lookup
			label="cliente"
			property="tabelaDivisaoCliente.divisaoCliente.cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterParametrosClienteAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="16%"
			maxLength="20"
			size="20"
			width="45%"
			required="true"
			afterPopupSetValue="afterPopupCliente"
		>
			<adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" 
				serializable="false"
				size="30" 
			/>
		</adsm:lookup>

		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:hidden
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			serializable="false"
		/>
		<adsm:combobox
			property="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente"
			optionProperty="idDivisaoCliente"
			service="lms.vendas.manterParametrosClienteAction.findDivisaoCombo"
			labelWidth="10%"
			width="29%"
			label="divisao"
			boxWidth="140"
		>
			<adsm:propertyMapping
				relatedProperty="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
				modelProperty="dsDivisaoCliente"
			/>
			<adsm:propertyMapping
				criteriaProperty="tabelaDivisaoCliente.divisaoCliente.cliente.idCliente"
				modelProperty="cliente.idCliente"
			/>
		</adsm:combobox>

		<%-------------------%>
		<%-- TABELA COMBO --%>
		<%-------------------%>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao" serializable="false"/>

		<adsm:combobox
			label="tabela"
			labelWidth="16%"
			optionLabelProperty="tabelaPreco.tabelaPrecoStringDescricao"
			optionProperty="idTabelaDivisaoCliente"
			property="tabelaDivisaoCliente.idTabelaDivisaoCliente"
			service="lms.vendas.manterParametrosClienteAction.findTabelaDivisaoClienteCombo"
			width="45%"
			boxWidth="330"
			onchange="tabelaDivisaoClienteOnChange();"
			onDataLoadCallBack="tabelaDivisaoClienteDataLoad"
		>
			<adsm:propertyMapping
				criteriaProperty="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente"
				modelProperty="divisaoCliente.idDivisaoCliente"
			/>
		</adsm:combobox>
		
		<adsm:hidden
			property="idSubtipoTabelaPreco" 
			serializable="false"
		/>
		<adsm:hidden 
			property="idTipoTabelaPreco" 
			serializable="false"
		/>
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" 
			serializable="false"
		/>
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" 
			serializable="false"
		/>
		<input type="hidden" 
			id="idUfMunicipio" 
		/>

		<%-- Include do JSP que contem os campos das rotas de origem e destino --%>
		<%@ include file="../tabelaPrecos/rotaPreco.jsp" %>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton
				callbackProperty="parametrosCliente"
			/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
		idProperty="idParametroCliente" 
		gridHeight="125" 
		property="parametrosCliente" 
		unique="true" 
		scrollBars="horizontal" 
		rows="6"
		showPagging="true"
		showTotalPageCount="true"
		rowCountService="lms.vendas.manterParametrosClienteAction.getRowCount"
		service="lms.vendas.manterParametrosClienteAction.findPaginated"
	>

		<adsm:gridColumn title="numeroProposta" property="proposta.idProposta" width="120" align="right" />
		<adsm:gridColumn title="zonaOrigem" property="zonaByIdZonaOrigem.dsZona" width="150" />
		<adsm:gridColumn title="paisOrigem" property="paisByIdPaisOrigem.nmPais" width="150" />
		<adsm:gridColumn title="ufOrigem2" property="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialOrigem" property="filialByIdFilialOrigem.sgFilial" width="50" />
		<adsm:gridColumn title="municipioOrigem" property="municipioByIdMunicipioOrigem.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoOrigem" property="aeroportoByIdAeroportoOrigem.sgAeroporto" width="80" />
		<adsm:gridColumn title="localizacaoOrigem" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio" width="100" />
		<adsm:gridColumn title="grupoRegiaoOrigem" property="grupoRegiaoOrigem.dsGrupoRegiao" width="150" />

		<adsm:gridColumn title="zonaDestino" property="zonaByIdZonaDestino.dsZona" width="150" />
		<adsm:gridColumn title="paisDestino" property="paisByIdPaisDestino.nmPais" width="150" />
		<adsm:gridColumn title="ufDestino2" property="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialDestino" property="filialByIdFilialDestino.sgFilial" width="50" />
		<adsm:gridColumn title="municipioDestino" property="municipioByIdMunicipioDestino.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoDestino" property="aeroportoByIdAeroportoDestino.sgAeroporto" width="80" />
		<adsm:gridColumn title="localizacaoDestino" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio" width="100" />
		<adsm:gridColumn title="grupoRegiaoDestino" property="grupoRegiaoDestino.dsGrupoRegiao" width="150" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript" src="../lib/rotaPreco.js"></script>
<script language="javascript" type="text/javascript">

document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao").serializable="false";

function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		var frame = parent.document.frames["param_iframe"];
		frame.limpa();
		desabilitarAbas();
		frame = parent.document.frames["cad_iframe"];
		frame.continuaDisabled = false;
	} else if (eventObj.name == "cleanButton_click") {
		var origem = getElementValue("origem");
		if(origem=="div") {
		} else if(origem=="param") {
			notifyElementListeners({e:document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente")});
		}
	}
}

function desabilitarAbas() {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("param", true);
	tabGroup.setDisabledTab("gen", true);
	tabGroup.setDisabledTab("tax", true);
}

function tabelaDivisaoClienteOnChange() {
	setElementValue("idTabelaDivisaoCliente", getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente"));
	if(document.getElementById("grupoRegiaoOrigem.idGrupoRegiao") != null) {		
		comboboxChange({e:document.getElementById("tabelaDivisaoCliente.idTabelaDivisaoCliente")});		
		setElementValue("grupoRegiaoOrigem.idGrupoRegiao","");
		setElementValue("grupoRegiaoDestino.idGrupoRegiao","");
}
}

function tabelaDivisaoClienteDataLoad_cb(data, error) {
	tabelaDivisaoCliente_idTabelaDivisaoCliente_cb(data);
	if (data.length == 1) {
		setElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente", data[0].idTabelaDivisaoCliente);
	}
	notifyElementListeners({e:document.getElementById("tabelaDivisaoCliente.idTabelaDivisaoCliente")});
}

function afterPopupCliente(data) {
	setElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
}

</script>