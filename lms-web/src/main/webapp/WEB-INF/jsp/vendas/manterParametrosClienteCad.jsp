<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<%-- Parametro utilizado por rotaPreco.jsp --%>
<% Boolean blActiveValues = true; %>
<adsm:window 
	onPageLoadCallBack="myOnPageLoadCad"
	service="lms.vendas.manterParametrosClienteAction"
>
	<adsm:i18nLabels>
		<adsm:include key="LMS-01024"/>
		<adsm:include key="LMS-30017"/>
		<adsm:include key="LMS-01167"/>
	</adsm:i18nLabels>

	<adsm:form 
		action="/vendas/manterParametrosCliente" 
		idProperty="idParametroCliente"
		onDataLoadCallBack="dlc"
	>
		<script language="javascript" type="text/javascript">
			function myOnPageLoadCad_cb(dados,erros) {
				onPageLoad_cb(dados,erros);
				var u = new URL(parent.location.href);
				var origem = u.parameters["origem"];
				var obj;

				obj = document.getElementById("origem");
				obj.masterLink = "true";

				if(origem=="div") {
					setDisabled("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente",true);
					obj = document.getElementById("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente");
					obj.masterLink = "true";
				} else if(origem=="param") {
					obj = document.getElementById("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente");
					obj.masterLink = "true";
					notifyElementListeners({e:document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente")});
				}
				buscaPaisUsuarioLogado();
				configTabelaPrecoListeners();
			}

			var idPaisUL;
			var nmPaisUL;
			var idZonaUL;
			var dsZonaUL;
			
			function ajustaPaisUsuarioLogado() {
				setElementValue("paisByIdPaisOrigem.idPais", idPaisUL);
				setElementValue("paisByIdPaisOrigem.nmPais", nmPaisUL);
				setElementValue("zonaByIdZonaOrigem.idZona", idZonaUL);
				setElementValue("zonaByIdZonaOrigem.dsZona", dsZonaUL);
				setElementValue("paisByIdPaisDestino.idPais", idPaisUL);
				setElementValue("paisByIdPaisDestino.nmPais", nmPaisUL);
				setElementValue("zonaByIdZonaDestino.idZona", idZonaUL);
				setElementValue("zonaByIdZonaDestino.dsZona", dsZonaUL);
			
				notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
				notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
			}

			function ajustaPaisUsuarioLogado_cb(data, errMsg) {
				if(errMsg!=undefined) {
					alert(errMsg);
					return;
				}
				idPaisUL = getNestedBeanPropertyValue(data, "idPais");
				nmPaisUL = getNestedBeanPropertyValue(data, "nmPais");
				idZonaUL = getNestedBeanPropertyValue(data, "zona.idZona");
				dsZonaUL = getNestedBeanPropertyValue(data, "zona.dsZona");
				ajustaPaisUsuarioLogado();
			}

			function buscaPaisUsuarioLogado() {
				var sdo = createServiceDataObject("lms.vendas.manterParametrosClienteAction.findPaisUsuarioLogado", "ajustaPaisUsuarioLogado", {});
				xmit({serviceDataObjects:[sdo]});
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

		<adsm:hidden property="origem"/>
		<adsm:hidden property="idDivisaoCliente"/>
		<adsm:hidden property="dsDivisaoCliente"/>
		<adsm:hidden property="tpAcesso" value="" />

		<adsm:hidden property="idTabelaDivisaoCliente"/>
		<adsm:hidden property="tipoTabelaPreco"/>
		<adsm:hidden property="nrVersao"/>
		<adsm:hidden property="subtipoTabelaPreco"/>
		<adsm:hidden property="dsDescricao"/>
		<adsm:hidden property="sgMoeda"/>
		<adsm:hidden property="dsSimboloMoeda"/>
		<adsm:hidden property="dsServico"/>

		<!-- LMS-6166 - trazer somente municípios vigentes -->
		<adsm:hidden property="municipioByIdMunicipioOrigem.vigentes" value="S" serializable="false" />
		<adsm:hidden property="municipioByIdMunicipioDestino.vigentes" value="S" serializable="false" />

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
			required="true"
			size="20"
			width="45%"
		>
			<adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" 
				size="30" 
			/>
		</adsm:lookup>
		
		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:hidden property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"/>
		<adsm:combobox 
			property="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente" 
			optionLabelProperty="dsDivisaoCliente" 
			optionProperty="idDivisaoCliente" 
			required="true"
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


		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao" />
		<adsm:combobox 
			label="tabela" 
			labelWidth="16%"
			optionLabelProperty="tabelaPreco.tabelaPrecoStringDescricao" 
			optionProperty="idTabelaDivisaoCliente" 
			property="tabelaDivisaoCliente.idTabelaDivisaoCliente" 
			required="true"
			service="lms.vendas.manterParametrosClienteAction.findTabelaDivisaoClienteCombo" 
			width="45%" 
			boxWidth="330"
		>
			<adsm:propertyMapping
				criteriaProperty="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente" 
				modelProperty="divisaoCliente.idDivisaoCliente" 
			/>
			<adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao" 
				modelProperty="tabelaPreco.tabelaPrecoStringDescricao" 
			/>
			<adsm:propertyMapping
				relatedProperty="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco" 
				modelProperty="tabelaPreco.idTabelaPreco"
			/>
			<%--adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.servico.dsServico" 
				modelProperty="servico.dsServico" 
			/--%>
			<adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo" 
				modelProperty="tabelaPreco.moeda.dsSimbolo" 
			/>
			<adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" 
				modelProperty="tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" 
			/>
			<adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" 
				modelProperty="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" 
			/>
		</adsm:combobox>

		<adsm:hidden 
			property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco" 
		/>
		<adsm:hidden 
			property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" 
		/>
		<adsm:hidden 
			property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" 
		/>
		<input type="hidden" 
			id="idUfMunicipio" 
		/>
		<%-- Include do JSP que contem os campos das rotas de origem e destino --%>
		<%@ include file="../tabelaPrecos/rotaPreco.jsp" %>
		
		<adsm:buttonBar>
			<adsm:button caption="copiarRotaParametros" id="copiar" buttonType="copiar" onclick="copiarRotaParametros()" disabled="false" />
			<adsm:button caption="historicoNegociacoes" id="historico" buttonType="historico" disabled="false" action="/vendas/visualizarHistoricoNegociacoesClientes" cmd="main">

				<adsm:linkProperty src="tabelaDivisaoCliente.divisaoCliente.cliente.idCliente" target="cliente.idCliente"/>
				<adsm:linkProperty src="tabelaDivisaoCliente.idTabelaDivisaoCliente" target="_idTabelaDivisaoCliente"/>
				<adsm:linkProperty src="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente" target="_idDivisaoCliente"/>
				<adsm:linkProperty src="tabelaDivisaoCliente.idTabelaDivisaoCliente" target="tabelaDivisaoCliente.idTabelaDivisaoCliente" disabled="true"/>
				<adsm:linkProperty src="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente" target="divisaoCliente.idDivisaoCliente" disabled="true"/>
				<adsm:linkProperty src="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" target="cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao" target="cliente.pessoa.nrIdentificacao"/>

				<adsm:linkProperty src="zonaByIdZonaOrigem.idZona" target="zonaOrigem.idZona" disabled="false"/>
				<adsm:linkProperty src="zonaByIdZonaOrigem.dsZona" target="zonaOrigem.dsZona" disabled="false"/>
				<adsm:linkProperty src="paisByIdPaisOrigem.idPais" target="paisByIdPaisOrigem.idPais" disabled="false"/>
				<adsm:linkProperty src="paisByIdPaisOrigem.nmPais" target="paisByIdPaisOrigem.nmPais" disabled="false"/>
				<adsm:linkProperty src="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" target="_idUfOrigem" disabled="false"/>
				<adsm:linkProperty src="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" target="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="filialByIdFilialOrigem.idFilial" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="filialByIdFilialOrigem.sgFilial" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.pessoa.nmFantasia" target="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="false"/>
				<adsm:linkProperty src="municipioByIdMunicipioOrigem.municipio.idMunicipio" target="municipioByIdMunicipioOrigem.municipio.idMunicipio" disabled="false"/>
				<adsm:linkProperty src="municipioByIdMunicipioOrigem.municipio.idMunicipio" target="municipioByIdMunicipioOrigem.idMunicipio"/>
				<adsm:linkProperty src="municipioByIdMunicipioOrigem.municipio.nmMunicipio" target="municipioByIdMunicipioOrigem.municipio.nmMunicipio" disabled="false"/>
				<adsm:linkProperty src="aeroportoByIdAeroportoOrigem.idAeroporto" target="aeroportoByIdAeroportoOrigem.idAeroporto" disabled="false"/>
				<adsm:linkProperty src="aeroportoByIdAeroportoOrigem.sgAeroporto" target="aeroportoByIdAeroportoOrigem.sgAeroporto" disabled="false"/>
				<adsm:linkProperty src="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" target="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" />
				<adsm:linkProperty src="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio" target="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio" disabled="false"/>
				<adsm:linkProperty src="grupoRegiaoOrigem.idGrupoRegiao" target="grupoRegiaoOrigem.idGrupoRegiao" disabled="false"/>

				<adsm:linkProperty src="zonaByIdZonaDestino.idZona" target="zonaDestino.idZona" disabled="false"/>
				<adsm:linkProperty src="zonaByIdZonaDestino.dsZona" target="zonaDestino.dsZona" disabled="false"/>
				<adsm:linkProperty src="paisByIdPaisDestino.idPais" target="paisByIdPaisDestino.idPais" disabled="false"/>
				<adsm:linkProperty src="paisByIdPaisDestino.nmPais" target="paisByIdPaisDestino.nmPais" disabled="false"/>
				<adsm:linkProperty src="unidadeFederativaByIdUfDestino.idUnidadeFederativa" target="_idUfDestino" disabled="false"/>
				<adsm:linkProperty src="unidadeFederativaByIdUfDestino.idUnidadeFederativa" target="unidadeFederativaByIdUfDestino.idUnidadeFederativa" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialDestino.idFilial" target="filialByIdFilialDestino.idFilial" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialDestino.sgFilial" target="filialByIdFilialDestino.sgFilial" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialDestino.pessoa.nmFantasia" target="filialByIdFilialDestino.pessoa.nmFantasia"/>
				<adsm:linkProperty src="municipioByIdMunicipioDestino.municipio.idMunicipio" target="municipioByIdMunicipioDestino.municipio.idMunicipio" disabled="false"/>
				<adsm:linkProperty src="municipioByIdMunicipioDestino.municipio.idMunicipio" target="municipioByIdMunicipioDestino.idMunicipio" disabled="false"/>
				<adsm:linkProperty src="municipioByIdMunicipioDestino.municipio.nmMunicipio" target="municipioByIdMunicipioDestino.municipio.nmMunicipio" disabled="false"/>
				<adsm:linkProperty src="aeroportoByIdAeroportoDestino.idAeroporto" target="aeroportoByIdAeroportoDestino.idAeroporto" disabled="false"/>
				<adsm:linkProperty src="aeroportoByIdAeroportoDestino.sgAeroporto" target="aeroportoByIdAeroportoDestino.sgAeroporto" disabled="false"/>
				<adsm:linkProperty src="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" target="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" disabled="false"/>
				<adsm:linkProperty src="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio" target="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio" disabled="false"/>
				<adsm:linkProperty src="grupoRegiaoDestino.idGrupoRegiao" target="grupoRegiaoDestino.idGrupoRegiao" disabled="false"/>

			</adsm:button>
			<adsm:button caption="continuar" disabled="false" id="continua" buttonType="continua" onclick="continuar(this.form.document)" />
			<adsm:button caption="limpar" disabled="false" id="newButton" buttonType="newButton" onclick="limpar(this.document,undefined,{name:'newButton'})" />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript" src="../lib/rotaPreco.js"></script>
<script language="javascript" type="text/javascript">

function copiarRotaParametros() {
	setIdParametroCliente('');
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("gen", true);
	tabGroup.setDisabledTab("tax", true);
	setDisabled("historico",true);
	setDisabled("copiar",true);
	setDisabled("__buttonBar:0.removeButton",true);
}

function setIdParametroCliente(idParametroCliente) {
	setElementValue("idParametroCliente",idParametroCliente);
}

function dlc_cb(dados,erros,errorCode, eventObj) {	
	var idMunicipio = getNestedBeanPropertyValue(dados, "municipioByIdMunicipioOrigem.idMunicipio");
	setElementValue("municipioByIdMunicipioOrigem.municipio.idMunicipio",idMunicipio);
	var nmMunicipio = getNestedBeanPropertyValue(dados, "municipioByIdMunicipioOrigem.nmMunicipio");
	setElementValue("municipioByIdMunicipioOrigem.municipio.nmMunicipio",nmMunicipio);

	idMunicipio = getNestedBeanPropertyValue(dados, "municipioByIdMunicipioDestino.idMunicipio");
	setElementValue("municipioByIdMunicipioDestino.municipio.idMunicipio",idMunicipio);
	nmMunicipio = getNestedBeanPropertyValue(dados, "municipioByIdMunicipioDestino.nmMunicipio");
	setElementValue("municipioByIdMunicipioDestino.municipio.nmMunicipio",nmMunicipio);

	setElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente",getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.idCliente"));
	setElementValue("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente",getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente"));

	setElementValue("tabelaDivisaoCliente.tabelaPreco.idTabelaPreco",getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"));

	myOnDataLoad_cb(dados,erros,errorCode,eventObj); // data load copiado de rotas

	configTabelaPrecoListeners();

	idGrupoRegiaoOrigem = getNestedBeanPropertyValue(dados, "grupoRegiaoOrigem.idGrupoRegiao");
	idGrupoRegiaoDestino = getNestedBeanPropertyValue(dados, "grupoRegiaoDestino.idGrupoRegiao");
	
	onDataLoad_cb(dados,erros,errorCode,eventObj);
	notifyElementListeners({e:document.getElementById("tabelaDivisaoCliente.idTabelaDivisaoCliente")});

	var frame = parent.document.frames["param_iframe"];
	frame.copyValuesFromCad();
	var dados = frame.onDataLoad_cb(dados,erros,errorCode,eventObj);
	frame.verifyIndicadoresTabela();
	disableContinua(true);
}

function limpar(documento, runInitWindowScript, eventObj) {
	newButtonScript(documento, runInitWindowScript, eventObj);
}

function desabilitarAbas() {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("param", true);
	tabGroup.setDisabledTab("gen", true);
	tabGroup.setDisabledTab("tax", true);
}

var continuaDisabled = false;
function disableContinua(flag) {
	continuaDisabled = flag
}

function initWindow(eventObj) {
	if (eventObj.name == "gridRow_click") {
		habilitarAbas();
	} else if (eventObj.name == "newButton") {
		ajustaPaisUsuarioLogado();
		setDisabled("historico",true);
		setDisabled("copiar",true);
		var param_frame = parent.document.frames["param_iframe"];
		param_frame.limpa();
		desabilitarAbas();
	
		var frame = parent.document.frames["pesq_iframe"];
		if(frame.getElementValue("origem") == "div") {
			getDadosDaTabList();
		} else if(frame.getElementValue("origem") == "param") {
			notifyElementListeners({e:document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente")});
		}
		disableContinua(false);
	} else if (eventObj.name == "removeButton") {
		limpar(this.document,undefined,{name:'newButton'});
		disableContinua(false);
	} else if (eventObj.name == "tab_click" ) {
		var idParametroCliente = getElementValue("idParametroCliente");
		if(idParametroCliente!="") {
			setDisabled("__buttonBar:0.removeButton",false);
			disableContinua(true);
		} else {
			setDisabled("historico",true);
			setDisabled("copiar",true);
			disableContinua(continuaDisabled);
		}
		var frame = parent.document.frames["pesq_iframe"];
		if(frame.getElementValue("origem")=="div") {
			getDadosDaTabList();
		} else if(frame.getElementValue("origem")=="param") {
			//notifyElementListeners({e:document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente")});
			//comboboxChange({e:document.getElementById("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente")});
		}
		if(continuaDisabled==false) {
			ajustaPaisUsuarioLogado();
		}
	}
}

function getDadosDaTabList() {
	var frame = parent.document.frames["pesq_iframe"];
	if(frame.getElementValue("origem")=="div")
	var e;
	var o;
	if(frame.getElementValue("idTabelaDivisaoCliente")!="") {
		setElementValue("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao",frame.getElementValue("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"));
		e = document.getElementById("tabelaDivisaoCliente.idTabelaDivisaoCliente");
		if(e.options.length == 1) {
			o = new Option(frame.getElementValue("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"), frame.getElementValue("idTabelaDivisaoCliente"));
			e.options.add(o);
			e.selectedIndex = 1;
			setElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente",frame.getElementValue("idTabelaDivisaoCliente"));
		}
	}

	if(frame.getElementValue("dsSimboloMoeda") != "") {
		setElementValue("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo",frame.getElementValue("sgMoeda")+' '+frame.getElementValue("dsSimboloMoeda"));
	}
	if(frame.getElementValue("idDivisaoCliente")!="" && frame.getElementValue("dsDivisaoCliente")!="") {
		setElementValue("tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente",frame.getElementValue("dsDivisaoCliente"));
		e = document.getElementById("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente");
		if(e.options.length == 1) {
			o = new Option(frame.getElementValue("dsDivisaoCliente"), frame.getElementValue("idDivisaoCliente"));
			e.options.add(o);
			e.selectedIndex = 1;
			setElementValue("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente",frame.getElementValue("idDivisaoCliente"));
		}
	}
}

function getValuesFromCad() {
	var dados = new Array();

	//comboboxChange({e:document.getElementById("unidadeFederativaByIdUfOrigem.idUnidadeFederativa")});
	//comboboxChange({e:document.getElementById("unidadeFederativaByIdUfDestino.idUnidadeFederativa")});
	//comboboxChange({e:document.getElementById("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio")});
	//comboboxChange({e:document.getElementById("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio")});
	//comboboxChange({e:document.getElementById("grupoRegiaoOrigem.idGrupoRegiao")});
	//comboboxChange({e:document.getElementById("grupoRegiaoDestino.idGrupoRegiao")});

	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.idTabelaDivisaoCliente", getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente"));
	setNestedBeanPropertyValue(dados, "zonaByIdZonaOrigem.idZona", getElementValue("zonaByIdZonaOrigem.idZona"));
	setNestedBeanPropertyValue(dados, "paisByIdPaisOrigem.idPais", getElementValue("paisByIdPaisOrigem.idPais"));
	setNestedBeanPropertyValue(dados, "unidadeFederativaByIdUfOrigem.idUnidadeFederativa", getElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
	setNestedBeanPropertyValue(dados, "filialByIdFilialOrigem.idFilial", getElementValue("filialByIdFilialOrigem.idFilial"));
	setNestedBeanPropertyValue(dados, "municipioByIdMunicipioOrigem.idMunicipio", getElementValue("municipioByIdMunicipioOrigem.idMunicipio"));
	setNestedBeanPropertyValue(dados, "aeroportoByIdAeroportoOrigem.idAeroporto", getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto"));
	setNestedBeanPropertyValue(dados, "tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"));
	setNestedBeanPropertyValue(dados, "grupoRegiaoOrigem.idGrupoRegiao", getElementValue("grupoRegiaoOrigem.idGrupoRegiao"));
	setNestedBeanPropertyValue(dados, "grupoRegiaoOrigem.dsGrupoRegiao", getElementValue("grupoRegiaoOrigem.dsGrupoRegiao"));

	setNestedBeanPropertyValue(dados, "zonaByIdZonaDestino.idZona", getElementValue("zonaByIdZonaDestino.idZona"));
	setNestedBeanPropertyValue(dados, "paisByIdPaisDestino.idPais", getElementValue("paisByIdPaisDestino.idPais"));
	setNestedBeanPropertyValue(dados, "unidadeFederativaByIdUfDestino.idUnidadeFederativa", getElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa"));
	setNestedBeanPropertyValue(dados, "filialByIdFilialDestino.idFilial", getElementValue("filialByIdFilialDestino.idFilial"));
	setNestedBeanPropertyValue(dados, "municipioByIdMunicipioDestino.idMunicipio", getElementValue("municipioByIdMunicipioDestino.idMunicipio"));
	setNestedBeanPropertyValue(dados, "aeroportoByIdAeroportoDestino.idAeroporto", getElementValue("aeroportoByIdAeroportoDestino.idAeroporto"));
	setNestedBeanPropertyValue(dados, "tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio", getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"));
	setNestedBeanPropertyValue(dados, "grupoRegiaoDestino.idGrupoRegiao", getElementValue("grupoRegiaoDestino.idGrupoRegiao"));
	setNestedBeanPropertyValue(dados, "grupoRegiaoDestino.dsGrupoRegiao", getElementValue("grupoRegiaoDestino.dsGrupoRegiao"));

	setNestedBeanPropertyValue(dados, "idParametroCliente", getElementValue("idParametroCliente"));
	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo", getElementValue("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo"));

	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao", getElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"));
	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa", getElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"));
	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente", getElementValue("tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"));

	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.idTabelaPreco", getElementValue("tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"));	
	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco", getElementValue("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"));

	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco", getElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"));
	
	setNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao", getElementValue("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"));
	setNestedBeanPropertyValue(dados, "zonaByIdZonaOrigem.dsZona", getElementValue("zonaByIdZonaOrigem.dsZona"));

	setNestedBeanPropertyValue(dados, "paisByIdPaisOrigem.nmPais", getElementValue("paisByIdPaisOrigem.nmPais"));
	setNestedBeanPropertyValue(dados, "unidadeFederativaByIdUfOrigem.siglaDescricao", getElementValue("unidadeFederativaByIdUfOrigem.siglaDescricao"));
	setNestedBeanPropertyValue(dados, "filialByIdFilialOrigem.sgFilial", getElementValue("filialByIdFilialOrigem.sgFilial"));
	setNestedBeanPropertyValue(dados, "filialByIdFilialOrigem.pessoa.nmFantasia", getElementValue("filialByIdFilialOrigem.pessoa.nmFantasia"));
	setNestedBeanPropertyValue(dados, "municipioByIdMunicipioOrigem.municipio.nmMunicipio", getElementValue("municipioByIdMunicipioOrigem.municipio.nmMunicipio"));
	setNestedBeanPropertyValue(dados, "aeroportoByIdAeroportoOrigem.sgAeroporto", getElementValue("aeroportoByIdAeroportoOrigem.sgAeroporto"));
	setNestedBeanPropertyValue(dados, "aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", getElementValue("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"));
	setNestedBeanPropertyValue(dados, "tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio", getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio"));
	setNestedBeanPropertyValue(dados, "zonaByIdZonaDestino.dsZona", getElementValue("zonaByIdZonaDestino.dsZona"));
	setNestedBeanPropertyValue(dados, "paisByIdPaisDestino.nmPais", getElementValue("paisByIdPaisDestino.nmPais"));
	setNestedBeanPropertyValue(dados, "unidadeFederativaByIdUfDestino.siglaDescricao", getElementValue("unidadeFederativaByIdUfDestino.siglaDescricao"));
	setNestedBeanPropertyValue(dados, "filialByIdFilialDestino.sgFilial", getElementValue("filialByIdFilialDestino.sgFilial"));
	setNestedBeanPropertyValue(dados, "filialByIdFilialDestino.pessoa.nmFantasia", getElementValue("filialByIdFilialDestino.pessoa.nmFantasia"));
	setNestedBeanPropertyValue(dados, "municipioByIdMunicipioDestino.municipio.nmMunicipio", getElementValue("municipioByIdMunicipioDestino.municipio.nmMunicipio"));
	setNestedBeanPropertyValue(dados, "aeroportoByIdAeroportoDestino.sgAeroporto", getElementValue("aeroportoByIdAeroportoDestino.sgAeroporto"));
	setNestedBeanPropertyValue(dados, "aeroportoByIdAeroportoDestino.pessoa.nmPessoa", getElementValue("aeroportoByIdAeroportoDestino.pessoa.nmPessoa"));
	setNestedBeanPropertyValue(dados, "tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio", getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio"));
	return dados;
}

function habilitarAbas() {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("param", false);
	tabGroup.setDisabledTab("gen", false);
	tabGroup.setDisabledTab("tax", false);
}

function validateOwners(documento) {
	if(getElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente")==""
		||getElementValue("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente")==""
		||getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente")=="") {
		return false;
	}
	return true;
}

function getForm() {
	return document.forms[0];
}

function validateParametrosCad(documento) {
	return validateTabScript(documento.forms) && validaRotas();
}

function continuar(documento) {
	if(documento == undefined) {
		documento = this.document;
	}
	if(validateParametrosCad(documento) == true) {
		var tabGroup = getTabGroup(documento);
		tabGroup.setDisabledTab("param", false);
		if(continuaDisabled) {
			tabGroup.selectTab("param",{name:"tab_click"});
		} else {
			tabGroup.selectTab("param",{name:"continuar"});			
		}
		disableContinua(true);
		return true;
	}
	return false;
}

/*------------------------------------------
-- COPY&PASTE FROM MANTER ROTAS - BEGIN --
------------------------------------------*/

	/*
	* Data load do form.
	*/
	function myOnDataLoad_cb(dados, erros, errorCode, eventObj) {
		var idUFOrigem = getNestedBeanPropertyValue(dados, "unidadeFederativaByIdUfOrigem.idUnidadeFederativa");
		setElementValue("_idUfOrigem", idUFOrigem);
		var idUFDestino = getNestedBeanPropertyValue(dados, "unidadeFederativaByIdUfDestino.idUnidadeFederativa");
		setElementValue("_idUfDestino", idUFDestino);
	}



</script>
