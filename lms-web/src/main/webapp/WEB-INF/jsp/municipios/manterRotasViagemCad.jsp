<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';
		return colunas;
	}
</script>
<adsm:window service="lms.municipios.manterRotasViagemAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-29057"/>
		<adsm:include key="LMS-29193"/>
	</adsm:i18nLabels>

	<adsm:form action="/municipios/manterRotasViagem" idProperty="idRotaViagem" height="385"
			service="lms.municipios.manterRotasViagemAction.findByIdRotaViagem"
			onDataLoadCallBack="rotaViagemLoad" newService="lms.municipios.manterRotasViagemAction.newMaster" >

	<td colspan="100" >

	<div id="principal" style="display:block;border:none;">
	<script>
		document.write(geraColunas());
	</script>
		<adsm:hidden property="versao" />
		<adsm:hidden property="tpEmpresaMercurioValue" value="M" serializable="false" />

		<adsm:combobox property="tpRota" label="tipoRota" domain="DM_TIPO_ROTA_VIAGEM"
				onchange="tpRotaViagemChange(this);" required="true" labelWidth="18%" width="32%" />
		<adsm:combobox property="tpSistemaRota" label="sistemaRota" domain="DM_SISTEMA_ROTA" 
				onchange="tpSistemaRotaChange(this);"
				labelWidth="18%" width="32%" required="true" />
		<adsm:hidden property="dsTpRota" />

		<adsm:hidden property="cliente.tpSituacao" value="A" serializable="false" />

		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupCliente" 
				label="cliente" size="17" maxLength="20" labelWidth="18%" width="82%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" required="true" >
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />

			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:combobox
			label="tipoMeioTransporte"
			property="tipoMeioTransporte.idTipoMeioTransporte"
			optionProperty="idTipoMeioTransporte"
			optionLabelProperty="dsTipoMeioTransporte"
			service="lms.municipios.manterRotasViagemAction.findTipoMeioTransporte"
			required="true"
			onchange="onChangeTipoMT(this);"
			onlyActiveValues="true"
			labelWidth="18%"
			width="82%"
			boxWidth="180"
			cellStyle="vertical-align:bottom;">
		</adsm:combobox>

		<adsm:range label="vigencia" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:hidden property="acaoVigenciaAtual" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

	</table>
	</div>
<%----------------------------------------------------------------------------------------------%>
<%--ROTA IDA------------------------------------------------------------------------------------%>
<%----------------------------------------------------------------------------------------------%>	
	<div id="agrupIda" style="display:none;border:none;">
	<script>
		document.write(geraColunas());
	</script>
		<adsm:hidden property="rotaIda.idRotaIdaVolta" />
		<adsm:hidden property="rotaIda.versao" />
		<adsm:hidden property="rotaIda.tpRotaIdaVolta" value="I" />

		<adsm:section caption="rotaIda" />
		<adsm:listbox
			label="rotaIda"
			property="rotaIda.filiaisRota" 
			optionProperty="sgFilial"
			optionLabelProperty="dsRota"
			size="4"
			labelWidth="18%"
			width="82%"
			required="true"
			onContentChange="rotaIdaContentChange"
			showOrderControls="false"
			boxWidth="180"
			showIndex="false"
			serializable="true"
			allowMultiple="true"
		>
			<adsm:lookup
				property="filial"
				idProperty="idFilial"
				criteriaProperty="sgFilial" 
				dataType="text"
				size="3"
				maxLength="3" 
				service="lms.municipios.manterRotasViagemAction.findFilialLookup"
				action="/municipios/manterFiliais"
				exactMatch="false"
				minLengthForAutoPopUpSearch="3"
			>
				<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa"/>
				<adsm:propertyMapping relatedProperty="rotaIda.filiaisRota_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>
		</adsm:listbox>
		<adsm:hidden property="rotaIda.dsRota" serializable="false" />

		<adsm:textbox dataType="integer" property="rotaIda.nrRota" label="numeroRota"
				labelWidth="18%" width="32%" disabled="true" size="5" mask="0000" />
		<adsm:textbox dataType="decimal" label="distancia" property="rotaIda.nrDistancia" onchange="calcFreteKm('Ida');"
				maxLength="6" size="18" required="true" unit="km2" labelWidth="18%" width="32%" mask="###,###" />

		<adsm:combobox
			label="moeda"
			property="rotaIda.moedaPais.idMoedaPais"
			optionProperty="idMoedaPais"
			optionLabelProperty="moeda.siglaSimbolo"
			service="lms.municipios.manterRotasViagemAction.findMoedaByPais"
			autoLoad="true"
			onchange="onChangeMoeda(this);"
			labelWidth="18%"
			width="32%"
			boxWidth="85"
			required="true"
			onlyActiveValues="true"
		> 
			<adsm:propertyMapping relatedProperty="rotaIda.moedaPais.moeda.dsSimbolo" modelProperty="moeda.siglaSimbolo"/>
		</adsm:combobox>
		<adsm:hidden property="rotaIda.moedaPais.moeda.dsSimbolo"/>

		<adsm:textbox dataType="currency" property="rotaIda.vlFreteKm"
				label="freteKm" maxLength="18" size="22" disabled="true" mask="###,###,###,###,##0.000" labelWidth="18%" width="32%" required="true" />
		<adsm:textbox dataType="currency" property="rotaIda.vlFreteCarreteiro" style="text-align:right;"
				label="freteCarreteiro" size="18" disabled="true" mask="#,###,###,###,###,##0.00" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="currency" property="rotaIda.vlPremio"
				label="premioCumprimento" maxLength="18" size="22" mask="#,###,###,###,###,##0.00" labelWidth="18%" width="32%" />

		<adsm:textbox dataType="currency" property="rotaIda.vlPedagio" label="valorPostoPassagem" serializable="false"
				labelWidth="18%" width="32%" size="22" 
				mask="###,###,###,###,##0.00" cellStyle="vertical-align:bottom;" disabled="true" />
		<adsm:textbox dataType="text" property="rotaIda.nrTempoViagem" label="tempoViagem" serializable="false"
				labelWidth="18%" width="32%" size="6" unit="h" cellStyle="vertical-align:bottom;" disabled="true"/>

		<adsm:textarea property="rotaIda.obItinerario" label="itinerario" maxLength="500" rows="3" columns="75" labelWidth="18%" width="82%" />
		<adsm:textarea property="rotaIda.obRotaIdaVolta" label="observacao" maxLength="500" rows="3" columns="75" labelWidth="18%" width="82%" />

	</table>	
	</div>
<%----------------------------------------------------------------------------------------------%>
<%--ROTA EVENTUAL-------------------------------------------------------------------------------%>
<%----------------------------------------------------------------------------------------------%>
	<%--div id="agrupEventual" style="display:none;border:none;">
	<script>
		document.write(geraColunas());
	</script>
		<adsm:hidden property="rotaEvent.idRotaIdaVolta" />
		<adsm:hidden property="rotaEvent.versao" />
		<adsm:hidden property="rotaEvent.tpRotaIdaVolta" value="E" />

		<adsm:section caption="rota" />
		<adsm:listbox property="rotaEvent.filiaisRota"
				optionProperty="sgFilial" optionLabelProperty="dsRota"
				label="rota" size="4" labelWidth="18%" width="82%" required="true" onContentChange="rotaEventContentChange"
				showOrderControls="false" boxWidth="180" showIndex="false" serializable="true">
			<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					dataType="text" size="3" maxLength="3"
					service="lms.municipios.manterRotaAction.findFilialLookup" action="/municipios/manterFiliais"
					exactMatch="false" minLengthForAutoPopUpSearch="3">
				<adsm:propertyMapping relatedProperty="rotaEvent.filiaisRota_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>
		</adsm:listbox>
		<adsm:hidden property="rotaEvent.dsRota" serializable="false" />

		<adsm:textbox dataType="integer" label="distancia" property="rotaEvent.nrDistancia"
				maxLength="6" size="18" required="true" unit="km2" labelWidth="18%" width="82%" mask="##,###" />

		<adsm:combobox property="rotaEvent.moedaPais.idMoedaPais" autoLoad="true"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo"
				service="lms.municipios.manterRotasViagemAction.findMoedaByPais"
				label="moeda" labelWidth="18%" width="32%" boxWidth="85" required="true" > 
			<adsm:propertyMapping relatedProperty="rotaEvent.moedaPais.moeda.dsSimbolo" modelProperty="moeda.siglaSimbolo"/>
		</adsm:combobox>
		<adsm:hidden property="rotaEvent.moedaPais.moeda.dsSimbolo"/>

		<adsm:textbox dataType="integer" property="rotaEvent.nrTempoViagem" label="tempoViagem" serializable="false"
			labelWidth="18%" width="32%" size="18" cellStyle="vertical-align:bottom;" disabled="true"/>

		<adsm:textarea property="rotaEvent.obItinerario" label="itinerario" maxLength="500" rows="3" columns="75" labelWidth="18%" width="82%" />
		<adsm:textarea property="rotaEvent.obRotaIdaVolta" label="observacao" maxLength="500" rows="3" columns="75" labelWidth="18%" width="82%" />

	</table>
	</div--%>
<%----------------------------------------------------------------------------------------------%>
<%--ROTA VOLTA----------------------------------------------------------------------------------%>
<%----------------------------------------------------------------------------------------------%>
	<div id="agrupVolta" style="display:none;border:none;">
	<script>
		document.write(geraColunas());
	</script>
		<adsm:hidden property="rotaVolta.idRotaIdaVolta" />
		<adsm:hidden property="rotaVolta.versao" />
		<adsm:hidden property="rotaVolta.tpRotaIdaVolta" value="V" />

		<adsm:section caption="rotaVolta" />

		<adsm:listbox
			label="rotaVolta"
			property="rotaVolta.filiaisRota" 
			optionProperty="sgFilial"
			optionLabelProperty="dsRota"
			size="4"
			labelWidth="18%"
			width="82%"
			onContentChange="rotaVoltaContentChange"
			showOrderControls="false"
			boxWidth="180"
			showIndex="false"
			serializable="true"
			required="true"
			allowMultiple="true"
		>
			<adsm:lookup
				property="filial"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				dataType="text"
				size="3"
				maxLength="3"
				service="lms.municipios.manterRotasViagemAction.findFilialLookup"
				action="/municipios/manterFiliais"
				exactMatch="false"
				minLengthForAutoPopUpSearch="3"
			>
				<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
				<adsm:propertyMapping relatedProperty="rotaVolta.filiaisRota_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>
		</adsm:listbox>
		<adsm:hidden property="rotaVolta.dsRota" serializable="false" />

		<adsm:textbox dataType="integer" property="rotaVolta.nrRota" label="numeroRota"
				labelWidth="18%" width="32%" disabled="true" size="5" mask="0000" />
		<adsm:textbox dataType="decimal" label="distancia" property="rotaVolta.nrDistancia" onchange="calcFreteKm('Volta');"
				maxLength="6" size="18" unit="km2" labelWidth="18%" width="32%" mask="###,###" required="true" >
			<%--span id="nrDistanciaVolta_req"><font size="2" color="red">*</font></span--%>
		</adsm:textbox>

		<adsm:combobox
			label="moeda"
			property="rotaVolta.moedaPais.idMoedaPais"
			optionProperty="idMoedaPais"
			optionLabelProperty="moeda.siglaSimbolo"
			service="lms.municipios.manterRotasViagemAction.findMoedaByPais"
			autoLoad="true"
			onchange="onChangeMoeda(this);"
			labelWidth="18%"
			width="32%"
			boxWidth="85"
			required="true"
			onlyActiveValues="true"
		> 
			<adsm:propertyMapping relatedProperty="rotaVolta.moedaPais.moeda.dsSimbolo" modelProperty="moeda.siglaSimbolo"/>
			<%--span id="moedaVolta_req"><font size="2" color="red">*</font></span--%>
		</adsm:combobox>
		<adsm:hidden property="rotaVolta.moedaPais.moeda.dsSimbolo"/>

		<adsm:textbox dataType="currency" property="rotaVolta.vlFreteKm"
				label="freteKm" maxLength="18" size="22" disabled="true" mask="###,###,###,###,##0.000" labelWidth="18%" width="32%" required="true" >
			<%-- span id="vlFreteKmVolta_req"><font size="2" color="red">*</font></span--%>
		</adsm:textbox>
		<adsm:textbox dataType="currency" property="rotaVolta.vlFreteCarreteiro" style="text-align:right;"
				label="freteCarreteiro" size="18" disabled="true" mask="#,###,###,###,###,##0.00" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="currency" property="rotaVolta.vlPremio"
				label="premioCumprimento" maxLength="18" size="22" mask="#,###,###,###,###,##0.00" labelWidth="18%" width="32%" >
		</adsm:textbox>

		<adsm:textbox dataType="currency" property="rotaVolta.vlPedagio" label="valorPostoPassagem" serializable="false"
				labelWidth="18%" width="32%" size="22" cellStyle="vertical-align:bottom;" 
				mask="###,###,###,###,##0.00" disabled="true"/>
		<adsm:textbox dataType="text" property="rotaVolta.nrTempoViagem" label="tempoViagem" serializable="false"
				labelWidth="18%" width="32%" size="6" unit="h" cellStyle="vertical-align:bottom;" disabled="true"/>

		<adsm:textarea property="rotaVolta.obItinerario" label="itinerario" maxLength="500" rows="3" columns="75" labelWidth="18%" width="82%" />
		<adsm:textarea property="rotaVolta.obRotaIdaVolta" label="observacao" maxLength="500" rows="3" columns="75" labelWidth="18%" width="82%" />

	</table>
	</div>	

		<adsm:buttonBar>
			<adsm:button id="motoristas" caption="motoristas" boxWidth="120" onclick="openMotoristas();" />
			<adsm:button id="servicos" caption="servicos" onclick="openServicos();" />
			<adsm:button id="meiosTransporte" caption="meiosTransporte" onclick="openMeiosTransporte();" />
			<adsm:button caption="alterarRota" id="alterarRota"
				onclick="showModalDialog('municipios/manterRotasViagem.do?cmd=altRota',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:230px;dialogHeight:100px;');"
			/>
			<adsm:storeButton id="storeButton" service="lms.municipios.manterRotasViagemAction.storeValidation" callbackProperty="storeValidation" />
			<adsm:newButton id="newButton" />
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>

	</td></tr></table>

	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">

	document.getElementById("rotaVolta.filiaisRota_filial.sgFilial").onfocus = rotaVoltaFocus;
	document.getElementById("rotaIda.filiaisRota_filial.sgFilial").onblur = rotaIdaBlur;
	document.getElementById("rotaVolta.filiaisRota_filial.sgFilial").onblur = rotaVoltaBlur;

	var idMoedaPaisPadrao = -1;

	/**
	 * PageLoad customizado.
	 */
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		findMoedaPadrao();
	}

	/**
	 * Encontra moeda padrão do país.
	 */
	function findMoedaPadrao() {
		var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.findMoedaPadrao",
				"findMoedaPadrao",undefined);
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Callback da função que encontra moeda padrão do país.
	 */
	function findMoedaPadrao_cb(data,error) {
		if (data != undefined)
			idMoedaPaisPadrao = getNestedBeanPropertyValue(data, "idMoedaPais");
	}

	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("rotaIda.filiaisRota_nmFilial",true);
		setDisabled("rotaVolta.filiaisRota_nmFilial",true);
		setDisabled("rotaIda.vlFreteKm",true);
		setDisabled("rotaIda.vlFreteCarreteiro",true);
		setDisabled("rotaIda.vlPedagio",true);
		setDisabled("rotaIda.nrTempoViagem",true);
		setDisabled("rotaIda.nrRota",true);
		setDisabled("rotaVolta.vlFreteKm",true);
		setDisabled("rotaVolta.vlFreteCarreteiro",true);
		setDisabled("rotaVolta.vlPedagio",true);
		setDisabled("rotaVolta.nrTempoViagem",true);
		setDisabled("rotaVolta.nrRota",true);
		setDisabled("removeButton",true);
		setDisabled("servicos",true);
		setDisabled("meiosTransporte",true);
		setDisabled("motoristas",true);
		setDisabled("alterarRota",true);
	}

	/**
	 * Habilitar campos se o registro estiver vigente.
	 */
	function habilitarCampos() {
		setDisabled("dtVigenciaFinal",false);
		setDisabled("newButton",false);
		setDisabled("storeButton",false);
		setDisabled("servicos",false);
		setDisabled("meiosTransporte",false);
		setDisabled("motoristas",false);
		setDisabled("alterarRota",false);
	}

	function rotaViagemLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		if (data != undefined) {

                        var tpRota = getNestedBeanPropertyValue(data,"tpRota.value");
			visibilidadeByRota(tpRota);

			var dsRotaIda = getNestedBeanPropertyValue(data, "rotaIda.dsRota");
			setDisabledTabIda(dsRotaIda == "" || dsRotaIda == undefined);
			var dsRotaVolta = getNestedBeanPropertyValue(data, "rotaVolta.dsRota");
			setDisabledTabVolta(dsRotaVolta == "" || dsRotaVolta == undefined);

			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			estadoTelaAcaoVigencia(acaoVigenciaAtual,"dataLoad");

			visibilidadeBySistema(getNestedBeanPropertyValue(data, "tpSistemaRota.value"));
                        
                        if(tpRota && tpRota.length>0){
                            setDisabled("tpSistemaRota", true);
		}
                        
	}
	}

	var acaoVigenciaAtualGlobal = 0;
	function estadoTelaAcaoVigencia(acaoVigenciaAtual,which) {
		acaoVigenciaAtualGlobal = acaoVigenciaAtual;
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("removeButton", false);
			setDisabled("servicos", false);
			setDisabled("meiosTransporte", false);
			setDisabled("motoristas", false);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			habilitarCampos();
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document, true);
			setDisabled("newButton", false);
			setDisabled("storeButton", true);
			setDisabled("removeButton", true);
			setDisabled("servicos", false);
			setDisabled("meiosTransporte", false);
			setDisabled("motoristas", false);
		} else if(acaoVigenciaAtual == 3) {
			//setDisabled(document, true);

			//Habilita os botões
			habilitarCampos();
			
			setDisabled(document, false);
			
			//Desabilita os campos do cabeçalho
			setDisabled("tpSistemaRota",true);
			setDisabled("tipoMeioTransporte.idTipoMeioTransporte", true);
			setDisabled("dtVigenciaInicial",true);
			
			//Habilita data final
			setDisabled("dtVigenciaFinal",false);
			
			//E desabilita alguns campos
			setDisabled("rotaIda.filiaisRota", true);
			setDisabled("rotaIda.filiaisRota_nmFilial",true);
			setDisabled("rotaIda.filiaisRota_filial.sgFilial", true);
			setDisabled("rotaIda.filiaisRota_filial_picker",true);
			setDisabled("rotaIda.nrRota",true);
			setDisabled("rotaIda.vlFreteKm",true);
			setDisabled("rotaIda.vlFreteCarreteiro",true);
			setDisabled("rotaIda.vlPedagio",true);
						
			setDisabled("rotaVolta.filiaisRota", true);
			setDisabled("rotaVolta.filiaisRota_nmFilial",true);
			setDisabled("rotaVolta.filiaisRota_filial.sgFilial", true);
			setDisabled("rotaVolta.filiaisRota_filial_picker",true);
			setDisabled("rotaVolta.nrRota",true);
			setDisabled("rotaVolta.vlFreteKm",true);
			setDisabled("rotaVolta.vlFreteCarreteiro",true);
			setDisabled("rotaVolta.vlPedagio",true);
		}

		setDisabled("tpRota", true);

		if (which == "dataLoad") {
			if (acaoVigenciaAtual == 2)
				setFocusOnNewButton();
			else
				setFocus("dtVigenciaFinal");
		} else {
			setFocusOnNewButton();
		}
	}

	function inicializaValoresMoedas() {
		setElementValue("rotaIda.moedaPais.idMoedaPais", idMoedaPaisPadrao);
		setElementValue("rotaVolta.moedaPais.idMoedaPais", idMoedaPaisPadrao);
		//setElementValue("rotaEvent.moedaPais.idMoedaPais", idMoedaPaisPadrao);
	}

	/**
	 * tratamento dos eventos da initWindow para <tab_click>, 
	 * <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	 */
	function initWindow(eventObj) {
		if (eventObj.name != "storeButton" &&
				!(eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq")) {
			showRotaIda(false);
			showRotaVolta(false);
			//showRotaEvent(false);
			setVisibilityTipoMTAndTpSistemaRota(false);
			document.getElementById("motoristas").style.visibility = "hidden";
			setVisibilityCliente(false);
			setDisabledTabIda(true);
			setDisabledTabVolta(true);
			//setDisabledTabRota(true);
		}

		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton" &&
				!(eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq")) {
			estadoNovo();
			inicializaValoresMoedas();
			// Por problemas de sincronização, devemos setar o foco no primeiro campo forçadamente.
			setFocusOnFirstFocusableField();
		} 
		// O framework tenta desabilitar o botão Alterar Rota quando não deveria!!! Mas eu não deixo! HAHAHA
		else if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq" &&
				acaoVigenciaAtualGlobal != 1)
			setDisabled("alterarRota",true);
	}

	function tpRotaViagemChange(elem) {
		comboboxChange({e:elem});
		setElementValue("dsTpRota", document.getElementById("tpRota").options[document.getElementById("tpRota").selectedIndex].text);
		visibilidadeByRota(elem.value);
	}

	function tpSistemaRotaChange(elem) {
		comboboxChange({e:elem});
		visibilidadeBySistema(elem.value);
	}

	function visibilidadeBySistema(value) {
		if (value == "B" || value == "V") {
			showRotaVolta(true);
			setDisabledTabVolta(getElementValue("rotaVolta.dsRota") == "");
			if(isBlank(getElementValue("rotaVolta.nrRota"))) {
				setElementValue("rotaVolta.moedaPais.idMoedaPais", idMoedaPaisPadrao);
			}
		} else {
			showRotaVolta(false);
			setDisabledTabVolta(true);
		}
	}

	function visibilidadeByRota(tipo) {
		setVisibilityTipoMTAndTpSistemaRota(false);
		setVisibilityCliente(false);
		document.getElementById("motoristas").style.visibility = "hidden";
		showRotaIda(false);
		setDisabledTabIda(true);
		setDisabledTabVolta(true);

		if (tipo == "EX" || tipo == "EC") {
			setVisibilityTipoMTAndTpSistemaRota(true);
			document.getElementById("motoristas").style.visibility = "visible";
			showRotaIda(true);

			var dsRotaIda = getElementValue("rotaIda.dsRota");
			setDisabledTabIda(dsRotaIda == "" || dsRotaIda == undefined);
			var dsRotaVolta = getElementValue("rotaVolta.dsRota");
			setDisabledTabVolta(dsRotaVolta == "" || dsRotaVolta == undefined);
		}

		if (tipo == "EC") {
			setVisibilityCliente(true);
		}
	}

	function showRotaIda(valueBoolean) {
		var value = valueBoolean ? "" : "none";
		var valueSrt = valueBoolean ? "true" : "false";

		document.getElementById("agrupIda").style.display = value;

		document.getElementById("rotaIda.filiaisRota").required = valueSrt;
		document.getElementById("rotaIda.nrDistancia").required = valueSrt;
		document.getElementById("rotaIda.moedaPais.idMoedaPais").required = valueSrt;
		document.getElementById("rotaIda.vlFreteKm").required = valueSrt;

		document.getElementById("rotaIda.idRotaIdaVolta").serializable = valueBoolean;
		document.getElementById("rotaIda.tpRotaIdaVolta").serializable = valueBoolean;
		document.getElementById("rotaIda.filiaisRota").serializable = valueBoolean;
		document.getElementById("rotaIda.nrDistancia").serializable = valueBoolean;
		document.getElementById("rotaIda.moedaPais.idMoedaPais").serializable = valueBoolean;
		document.getElementById("rotaIda.moedaPais.moeda.dsSimbolo").serializable = valueBoolean;
		document.getElementById("rotaIda.vlFreteKm").serializable = valueBoolean;
		document.getElementById("rotaIda.vlPremio").serializable = valueBoolean;
		document.getElementById("rotaIda.obItinerario").serializable = valueBoolean;
		document.getElementById("rotaIda.obRotaIdaVolta").serializable = valueBoolean;

		setDisabledTabIda(getElementValue("rotaIda.dsRota") == "");
	}

	function showRotaVolta(valueBoolean) {
		var value = valueBoolean ? "" : "none";
		var valueSrt = valueBoolean ? "true" : "false";

		document.getElementById("agrupVolta").style.display = value;
		// RECOLOCADO:
		/* RETIRADO POIS ROTA VOLTA NÃO É MAIS OBRIGATORIO!!!! e OS CAMPOS SERÃO SERIALIZAVEIS SOMENTE SE A ROTA FOR INFORMADA!*/
		document.getElementById("rotaVolta.filiaisRota").required = valueSrt;
		document.getElementById("rotaVolta.nrDistancia").required = valueSrt;
		document.getElementById("rotaVolta.moedaPais.idMoedaPais").required = valueSrt;
		document.getElementById("rotaVolta.vlFreteKm").required = valueSrt;

		document.getElementById("rotaVolta.idRotaIdaVolta").serializable = valueBoolean;
		document.getElementById("rotaVolta.tpRotaIdaVolta").serializable = valueBoolean;
		document.getElementById("rotaVolta.filiaisRota").serializable = valueBoolean;
		document.getElementById("rotaVolta.nrDistancia").serializable = valueBoolean;
		document.getElementById("rotaVolta.moedaPais.idMoedaPais").serializable = valueBoolean;
		document.getElementById("rotaVolta.moedaPais.moeda.dsSimbolo").serializable = valueBoolean;
		document.getElementById("rotaVolta.vlFreteKm").serializable = valueBoolean;
		document.getElementById("rotaVolta.vlPremio").serializable = valueBoolean;
		document.getElementById("rotaVolta.obItinerario").serializable = valueBoolean;
		document.getElementById("rotaVolta.obRotaIdaVolta").serializable = valueBoolean;

		setDisabledTabVolta(getElementValue("rotaVolta.dsRota") == "");
	}

	function storeValidation_cb(data,error,key) {	
		if (error == undefined) {
			return afterStore_cb(data,error,key);
		}

		if ((error != undefined) && (key != "LMS-29063")) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}

		var resultConfirm = confirm(error);
		if (resultConfirm)
			storeButtonScript('lms.municipios.manterRotasViagemAction.storeAtribuiServicos',
					'afterStore', document.getElementById('form_idRotaViagem'));
		else if (!resultConfirm)
			storeButtonScript('lms.municipios.manterRotasViagemAction.storeNaoAtribuiServicos',
					'afterStore', document.getElementById('form_idRotaViagem'));
	}

	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);

		if(error == undefined) {
			// LMS-3494 - Após o salvamento deve-se desabilitar todos os campos da tela, para impedir que se faça alterações no registro sem editá-lo pelo sistema.
			// Por isso seto 1 na acaoVigenciaAtual, para que as demais abas fiquem desabilitadas também.
			document.getElementById("acaoVigenciaAtual").value= 1;
			setDisabled(document, true);
			
		} else {
			var tabGroup = getTabGroup(this.document);
			if (key == "LMS-29064") 
				tabGroup.selectTab('ida',{name:'tab_click'});
			else if (key == "LMS-29065")
				tabGroup.selectTab('volta',{name:'tab_click'});
		}		
	}

	
//#################################################################################################################################################
// Funções dos botões pois linkProperty's são dinâmicos.
	function buildLinkPropertiesQueryString_mt(tipoRota) {
		var qs = "";

			qs += "&rotaViagem.idRotaViagem=" + getElementValue("idRotaViagem");
			qs += "&rotaIda.dsRota=" + getElementValue("rotaIda.dsRota");
			qs += "&rotaVolta.dsRota=" + getElementValue("rotaVolta.dsRota");
			qs += "&tipoMeioTransporte.idTipoMeioTransporte=" + getElementValue("tipoMeioTransporte.idTipoMeioTransporte");
			qs += "&rotaIda.nrRota=" + getElementValue("rotaIda.nrRota");
			qs += "&rotaVolta.nrRota=" + getElementValue("rotaVolta.nrRota");
		return qs;
	}

	function buildLinkPropertiesQueryString_srv(tipoRota) {
		var qs = "";
			qs += "&rotaViagem.idRotaViagem=" + getElementValue("idRotaViagem");
			qs += "&rotaViagem.versao=" + getElementValue("versao");
			qs += "&rotaViagem.tipoRota=" + getElementValue("dsTpRota");
			qs += "&rotaIda.dsRota=" + getElementValue("rotaIda.dsRota");
			qs += "&rotaVolta.dsRota=" + getElementValue("rotaVolta.dsRota");
			qs += "&rotaIda.nrRota=" + getElementValue("rotaIda.nrRota");
			qs += "&rotaVolta.nrRota=" + getElementValue("rotaVolta.nrRota");
		return qs;
	}

	function buildLinkPropertiesQueryString_mot(tipoRota) {
		var qs = "";
			qs += "&rotaViagem.idRotaViagem=" + getElementValue("idRotaViagem");
			qs += "&rotaIda.dsRota=" + getElementValue("rotaIda.dsRota");
			qs += "&rotaVolta.dsRota=" + getElementValue("rotaVolta.dsRota");
			qs += "&rotaIda.nrRota=" + getElementValue("rotaIda.nrRota");
			qs += "&rotaVolta.nrRota=" + getElementValue("rotaVolta.nrRota");
		return qs;
	}

	function openMeiosTransporte() {
			parent.parent.redirectPage('municipios/manterMeiosTransporteRotaExpressa.do?cmd=main' + buildLinkPropertiesQueryString_mt("EX"));
	}

	function openServicos() {
			parent.parent.redirectPage('municipios/manterServicosRotaViagem.do?cmd=main' + buildLinkPropertiesQueryString_srv("EX"));
	}

	function openMotoristas() {
			parent.parent.redirectPage('municipios/manterMotoristasRotaViagem.do?cmd=main' + buildLinkPropertiesQueryString_mot("EX"));
	}

//##################################################################################################################################################
	// funções que manipular o clicar dos botões das lists:
	function rotaIdaContentChange(event) {
		return manipulaRotasChange(document.getElementById("rotaIda.filiaisRota"),
				document.getElementById("rotaIda.filiaisRota_filial.idFilial"),
				document.getElementById("rotaIda.filiaisRota_filial.sgFilial"),
				"rotaIda",
				event);
	}

	function rotaVoltaContentChange(event) {
		return manipulaRotasChange(document.getElementById("rotaVolta.filiaisRota"),
				document.getElementById("rotaVolta.filiaisRota_filial.idFilial"),
				document.getElementById("rotaVolta.filiaisRota_filial.sgFilial"),
				"rotaVolta",
				event);
	}


	// variáveis globais necessárias no callback;
	var blElemListLengthForManipulaRotas;
	var tipoRotaForManipulaRotas;
	var TREAT_MODIFY = "true";
	var TREAT_DELETE = "true";
	function manipulaRotasChange(elemList,elemIdLookup,elemLookup,tipoRota,event) {
		if ((event.name == "modifyButton_click" && (elemIdLookup.value == undefined || elemIdLookup.value == "")) ||
			(event.name == "deleteButton_click" && (elemList.selectedIndex == -1 || elemList.selectedIndex == undefined))) {
			setFocus(elemLookup);
			TREAT_MODIFY = "true";
			TREAT_DELETE = "true";
			return true;
		}

		if (event.name == "cleanButton_afterClick") {
			var listBox = document.getElementById(tipoRota+".filiaisRota");
			listBox.selectedIndex = -1;
		} else if (event.name == "modifyButton_click" && elemList.length == 0) {
			if (tipoRota == "rotaIda")
				setDisabledTabIda(false);
			else if (tipoRota == "rotaVolta")
				setDisabledTabVolta(false);
		} else if (event.name == "modifyButton_click") {
			var listBox = document.getElementById(tipoRota+".filiaisRota");
			if (listBox.selectedIndex != undefined && listBox.selectedIndex != -1 && TREAT_MODIFY == "true") {
				tipoRotaForManipulaRotas = tipoRota;
				validateExistenciaTrechos(tipoRota,event.name);
				return false;
			}
		} else if (event.name == "deleteButton_click" && TREAT_DELETE == "true") {
			blElemListLengthForManipulaRotas = (elemList.length == 1);
			tipoRotaForManipulaRotas = tipoRota;

			validateExistenciaTrechos(tipoRota,event.name);
			return false;
		}

		TREAT_MODIFY = "true";
		TREAT_DELETE = "true";

		setFocus(elemLookup);
		return true;
	}

	function validateExistenciaTrechos(tpRota,evento) {
		var data = new Array();
		var tpRotaValue;
		if (tpRota == "rotaIda")
			tpRotaValue = 'I';
		else if (tpRota == "rotaVolta")
			tpRotaValue = 'V';

		setNestedBeanPropertyValue(data,"tpRota",tpRotaValue);
		setNestedBeanPropertyValue(data,"idRotaViagem",getElementValue("idRotaViagem"));
		setNestedBeanPropertyValue(data,"idRotaIdaVolta",getElementValue(tpRota+".idRotaIdaVolta"));
		var listBox = document.getElementById(tpRota+".filiaisRota");
		setNestedBeanPropertyValue(data,"idFilial",listBox[listBox.selectedIndex].data.filial.idFilial);

		var strCallback = "";
		if (evento == "modifyButton_click") {
			strCallback = "validateExistenciaTrechosModify";
		} else if (evento == "deleteButton_click") {
			setNestedBeanPropertyValue(data,"nrOrdem",listBox.selectedIndex + 1);
			strCallback = "validateExistenciaTrechosDelete";
		}
		var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.validateExistenciaTrechos",
				strCallback,data);
		xmit({serviceDataObjects:[sdo]});
	}

	function validateExistenciaTrechosModify_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		TREAT_MODIFY = "false";
		if (tipoRotaForManipulaRotas == "rotaIda")
			rotaIda_filiaisRotaListboxDef.insertOrUpdateOption();
		else if (tipoRotaForManipulaRotas == "rotaVolta")
			rotaVolta_filiaisRotaListboxDef.insertOrUpdateOption();
	}

	function validateExistenciaTrechosDelete_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (blElemListLengthForManipulaRotas) {
			if (tipoRotaForManipulaRotas == "rotaIda")
				setDisabledTabIda(true);
			else if (tipoRotaForManipulaRotas == "rotaVolta")
				setDisabledTabVolta(true);
		}
		TREAT_DELETE = "false";
		if (tipoRotaForManipulaRotas == "rotaIda")
			rotaIda_filiaisRotaListboxDef.deleteOption();
		else if (tipoRotaForManipulaRotas == "rotaVolta")
			rotaVolta_filiaisRotaListboxDef.deleteOption();
	}

//#################################################################################################################################################	
// As funções abaixo pesquisam rotas viagem semelhantes para popular campo distância e itinerário.
	var numeroAnteriorFiliaisIda = -1;
	var numeroAnteriorFiliaisVolta = -1;
	var numeroAnteriorFiliaisEvent = -1;

	/*
	 * Evento chamado as campo rota Ida perder o foco.
	 */
	function rotaIdaBlur() {
		var elemLength = document.getElementById("rotaIda.filiaisRota").options.length;
		var elemLengthOld = numeroAnteriorFiliaisIda;
		numeroAnteriorFiliaisIda = elemLength;
		if (elemLength != elemLengthOld) {
			findDadosByRota("Ida");
		}		
	}

	function rotaVoltaBlur() {
		var elemLength = document.getElementById("rotaVolta.filiaisRota").options.length;
		var elemLengthOld = numeroAnteriorFiliaisVolta;
		numeroAnteriorFiliaisVolta = elemLength;
		if (elemLength != elemLengthOld) {
			findDadosByRota("Volta");
		}		
	}

	/*
	 * Gera String com siglas de filiais a partir de uma listbox.
	 */
	function getStringFiliaisByList(elem) {
		if (elem.options.length > 0) {
			var i = 0;
			var strFiliais = "";
			var filial = "";
			for (i = 0 ; i < elem.options.length ; i++) {
				filial = getNestedBeanPropertyValue(elem.options[i].data,"filial.sgFilial");
				strFiliais += (strFiliais == "") ? (filial) : ("-"+filial);
			}
			return strFiliais;
		}
		return "";
	}

	/**
	 * xmit que consultará a existência de uma rota viagem semelhante.
	 */
	function findDadosByRota(tipo) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idRotaViagem", getElementValue("idRotaViagem"));
		if (tipo != "Event")
			setNestedBeanPropertyValue(data,"vlFreteKm",getElementValue("rota" + tipo + ".vlFreteKm"));

		// COMPORTAMENTO INSERIDO AQUI POIS CENTRALIZA TODOS CHANGEs DE ROTAS!!!
		var strFiliais = getStringFiliaisByList(document.getElementById("rota" + tipo + ".filiaisRota"));
		setNestedBeanPropertyValue(data,"filiaisRota",strFiliais);
		var listFiliaisRota = getElementValue("rota" + tipo + ".filiaisRota");
		setNestedBeanPropertyValue(data,"listFiliaisRota",listFiliaisRota);
		setElementValue("rota" + tipo + ".dsRota", strFiliais);

		findValorPegadio(tipo);

		findDistancia(tipo, data);
	}

	function findDistancia(tipo, data) {
		var tpRota = "rota" + tipo;
		var listFiliaisRota = data.listFiliaisRota;
		var run = false;

		if (listFiliaisRota != undefined && listFiliaisRota != null) {
			if (listFiliaisRota.length > 1) {
				var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.findDistanciaByRota", "findDistanciaRota" + tipo, data);
				xmit({serviceDataObjects:[sdo]});
				run = true;
			}
		}
		if(!run) {
			resetValue(tpRota + ".nrDistancia");
			resetValue(tpRota + ".obItinerario");
		}
	}

	function findDistanciaRotaIda_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		findDistancia_cb("Ida", data);
	}

	function findDistanciaRotaVolta_cb(data,error) {
		if(error != undefined) {
			alert(error);
			return false;
		}
		findDistancia_cb("Volta", data);
	}

	function findDistancia_cb(tipo, data) {
		var tpRota = "rota" + tipo;
		if (data != undefined) {
			setElementValue(tpRota + ".nrDistancia", getNestedBeanPropertyValue(data, "nrDistancia"));
			format(getElement(tpRota + ".nrDistancia"));
			setElementValue(tpRota + ".obItinerario", getNestedBeanPropertyValue(data, "obItinerario"));
		} else {
			resetValue(tpRota + ".nrDistancia");
			resetValue(tpRota + ".obItinerario");
		}
		calcFreteKm(tipo);
	}

	// As 3 funções abaixo são responsáveis por habilitar e desabilitar as abas filhas.
	function setDisabledTabIda(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("ida", disabled);
	}

	function setDisabledTabVolta(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("volta", disabled);

		// Ao desabilitar a tab volta, os campos de rota volta não são mais serializáveis.
		// Isto foi implementado aqui pois é uma correção!!!

		var valueBoolean = !disabled;
		var reqValue = (valueBoolean == true) ? "visible" : "hidden";

		document.getElementById("rotaVolta.idRotaIdaVolta").serializable = valueBoolean;
		document.getElementById("rotaVolta.tpRotaIdaVolta").serializable = valueBoolean;
		document.getElementById("rotaVolta.filiaisRota").serializable = valueBoolean;
		document.getElementById("rotaVolta.nrDistancia").serializable = valueBoolean;
		document.getElementById("rotaVolta.moedaPais.idMoedaPais").serializable = valueBoolean;
		document.getElementById("rotaVolta.moedaPais.moeda.dsSimbolo").serializable = valueBoolean;
		document.getElementById("rotaVolta.vlFreteKm").serializable = valueBoolean;
		document.getElementById("rotaVolta.vlPremio").serializable = valueBoolean;
		document.getElementById("rotaVolta.obItinerario").serializable = valueBoolean;
		document.getElementById("rotaVolta.obRotaIdaVolta").serializable = valueBoolean;
	}

	/*
	 * Copia dados da listBox rota Ida para Volta após questionar cópia.
	 */
	function rotaVoltaFocus() {
		var elemSrc = document.getElementById("rotaIda.filiaisRota");
		var elemTrg = document.getElementById("rotaVolta.filiaisRota");
		if (elemSrc.length > 1 && elemTrg.length == 0) {
			if (confirmI18nMessage("LMS-29057")) {
				while (elemTrg.length > 0) {
					elemTrg.options[0].data = null;
					elemTrg.options[0] = null;
				}

				var i = 0;
				for (i = elemSrc.length; i > 0 ; i--) {
					elemTrg.options[elemTrg.length] = new Option(
										elemSrc.options[i-1].text,
										elemSrc.options[i-1].value);
					elemTrg.options[elemTrg.length-1].data = elemSrc.options[i-1].data;
				}

				var strFiliais = getStringFiliaisByList(document.getElementById("rotaVolta.filiaisRota"));
				setElementValue("rotaVolta.dsRota", strFiliais);
				setDisabledTabVolta(strFiliais == "");
				findDadosByRota("Volta");
				setFocus("rotaVolta.nrDistancia");
			}
		}
	}
	
	/*
	 * Xmit para SIMPLESMENTE multiplicar dois valores.
	 */
	function getFreteKm(tipo,vlFreteCarreteiro,nrDistancia) {
		var data = new Array();
		setNestedBeanPropertyValue(data,"nrDistancia",nrDistancia);
		setNestedBeanPropertyValue(data,"vlFreteCarreteiro",vlFreteCarreteiro);

		var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.getFreteKm",
				"getFreteKm" + tipo,data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Chamado no onChange dos campos nrDistancia
	 */
	function calcFreteKm(tipo) {
		var vlFreteCarreteiro = getElementValue("rota" + tipo + ".vlFreteCarreteiro");
		var nrDistancia = getElementValue("rota" + tipo + ".nrDistancia")
		if (vlFreteCarreteiro != "" && nrDistancia != "")
			getFreteKm(tipo,vlFreteCarreteiro,nrDistancia);
		else
			setElementValue("rota" + tipo + ".vlFreteKm","");
	}

	// Cada tipo de rotaIdaVolta possui um cb para o cálculo do frete Km.
	function getFreteKmIda_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			setElementValue("rotaIda.vlFreteKm",getNestedBeanPropertyValue(data,"vlFreteKm"));
		}
	}

	function getFreteKmVolta_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			setElementValue("rotaVolta.vlFreteKm",getNestedBeanPropertyValue(data,"vlFreteKm"));
		}
	}

	/**
	 * Mostra/esconde os campos:
	 *		tipo de meio de transporte
	 *		sistema da rota
	 * devido ao fato que eles devem ser mostrados apenas na rota expressa.
	 */
	function setVisibilityTipoMTAndTpSistemaRota(value) {
		setRowVisibility("tipoMeioTransporte.idTipoMeioTransporte", value);
		setVisibility("tpSistemaRota", value);

		var valueStr = value ? "true" : "false";

		document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").required = valueStr;
		document.getElementById("tpSistemaRota").required = valueStr;
	}

	/**
	 * Mostra/esconde os campos:
	 *		tipo de meio de transporte
	 *		sistema da rota
	 * devido ao fato que eles devem ser mostrados apenas na rota expressa.
	 */
	function setVisibilityCliente(value) {
		setRowVisibility("cliente.idCliente", value);
		var valueStr = value ? "true" : "false";
		document.getElementById("cliente.pessoa.nrIdentificacao").required = valueStr;
	}

	var isCalledByTipoMTCombo = false;	
	function onChangeTipoMT(elem) {
		comboboxChange({e:elem});
		findValorPegadio("Ida");
		isCalledByTipoMTCombo = true;
	}

	function onChangeMoeda(elem) {
		comboboxChange({e:elem});
		if (elem.id == "rotaIda.moedaPais.idMoedaPais")
			findValorPegadio("Ida");
		else if (elem.id == "rotaVolta.moedaPais.idMoedaPais")
			findValorPegadio("Volta");
	}

	//Encontra o valor do pedágio para a rota selecionada!
	function findValorPegadio(tipo) {
		var tpRota = "rota" + tipo;
		var data = new Array();
		var tpMeioTransporte = getElementValue("tipoMeioTransporte.idTipoMeioTransporte");
		var idMoedaPais = getElementValue(tpRota + ".moedaPais.idMoedaPais");
		var dsRota = getElementValue(tpRota + ".dsRota");
		var run = false;

		if (tpMeioTransporte != undefined && tpMeioTransporte != "" && idMoedaPais != undefined && idMoedaPais != "" && dsRota != "") {
			setNestedBeanPropertyValue(data, "idTipoMeioTransporte", tpMeioTransporte);
			setNestedBeanPropertyValue(data, "idMoedaPais", idMoedaPais);

			var listFiliaisRota = getElementValue(tpRota + ".filiaisRota");
			setNestedBeanPropertyValue(data, "listFiliaisRota", listFiliaisRota);

			if (listFiliaisRota != undefined && listFiliaisRota != null) {
				if (listFiliaisRota.length > 1) {
					var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.findValorPedagio",
							"findValorPegadioRota" + tipo, data);
					xmit({serviceDataObjects:[sdo]});
					run = true;
				}
			}
		}
		if(!run) {
			resetValue(tpRota + ".vlPedagio");
		}
	}

	function findValorPegadioRotaIda_cb(data, error, key) {
		findValorPegadio_cb("rotaIda", data, error, key);
		if (isCalledByTipoMTCombo == true) {
			isCalledByTipoMTCombo = false;
			findValorPegadio("Volta");
		}			
	}

	function findValorPegadioRotaVolta_cb(data, error, key) {
		findValorPegadio_cb("rotaVolta", data, error, key);
	}

	function findValorPegadio_cb(tpRota, data, error, key) {
		if (error != undefined) {
			alert(error);
			if(key == "LMS-27047") {
				resetValue(tpRota + ".moedaPais.idMoedaPais");
				resetValue(tpRota + ".vlPedagio");
			}
			return false;
		}
		if (data != undefined) {
			var vlPedagio = getNestedBeanPropertyValue(data, "vlPedagio");
			setElementValue(tpRota + ".vlPedagio", vlPedagio);
		}
	}


</script>