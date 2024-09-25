<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	var dadosUsuario;
	function pageLoad(){
		onPageLoad();
		document.getElementById("vlTarifaSpot").required = "true";
		var sdo = createServiceDataObject("lms.tabelaprecos.tarifaSpotService.findUsuarioLogado", "populaCampos");
		xmit({serviceDataObjects:[sdo]});
	}

	function populaCampos_cb(dados, error){
		if(error != undefined) {
			alert(error);
			return;
		}
		setElementValue("nrUtilizacoes", 0);
		dadosUsuario = dados;
		if(dadosUsuario) {
			populaUsuarioLogado(dadosUsuario);
		}
	}
//-->
</script>
<adsm:window onPageLoad="pageLoad" service="lms.tabelaprecos.tarifaSpotService">
	<adsm:form action="/tabelaPrecos/manterTarifasSpot" idProperty="idTarifaSpot"
		service="lms.tabelaprecos.tarifaSpotService.findByIdMap">
	
		<adsm:hidden property="usuarioByIdUsuarioSolicitante.idUsuario"/>
		<adsm:hidden property="empresa.tpEmpresa" value="C" serializable="false"/>
		<adsm:hidden property="tpSituacao" value="A"/>
	
		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.tabelaprecos.manterTarifasSpotAction.findLookupFilial"
			action="/municipios/manterFiliais"
			onchange="return lookup_filial_onchange();"
			onDataLoadCallBack="lookup_filial"
			onPopupSetValue="verificaFilial"
			required="true"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="20%"
			width="80%"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="funcionarioSolicitante"
			property="funcionario" 
			idProperty="idUsuario" 
			criteriaProperty="nrMatricula" 
			service="lms.tabelaprecos.manterTarifasSpotAction.findLookupUsuarioFuncionario" 
			action="/configuracoes/consultarFuncionariosView"
			serializable="false" 
			disabled="true" 
			required="true"
			dataType="text"
			size="15"
			maxLength="20" 
			labelWidth="20%"
			width="16%" 
			exactMatch="true" 
		>
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioSolicitante.idUsuario" modelProperty="idUsuario"/>
			<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" serializable="false" size="30" width="64%" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="ciaAerea"
			property="empresa"
			idProperty="idEmpresa"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.municipios.empresaService.findLookup"
			action="/municipios/manterEmpresas"
			popupLabel="pesqCiaAerea"
			dataType="text"
			size="18"
			maxLength="18"
			labelWidth="20%"
			width="18%"
			required="true"
		>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="tpEmpresa"/>
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" serializable="false" size="30" width="60%" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="aeroportoDeOrigem" 
			property="aeroportoByIdAeroportoOrigem"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			service="lms.tabelaprecos.manterTarifasSpotAction.findLookupAeroporto"
			action="/municipios/manterAeroportos"
			required="true"
			width="80%"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="20%"
		>
			<adsm:propertyMapping criteriaProperty="tpSituacao"
				modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"
				serializable="false" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="aeroportoDeDestino"
			property="aeroportoByIdAeroportoDestino"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto" width="80%"
			service="lms.tabelaprecos.manterTarifasSpotAction.findLookupAeroporto"
			action="/municipios/manterAeroportos"
			required="true"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="20%"
		>
			<adsm:propertyMapping criteriaProperty="tpSituacao"
				modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" 
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				serializable="false" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox
			label="valorTarifa"
			property="moeda.idMoeda"
			optionProperty="idMoeda"
			optionLabelProperty="dsSimbolo"
			service="lms.configuracoes.moedaPaisService.findMoedaByPaisUsuarioLogado"
			required="true"
			labelWidth="20%"
			width="80%"
			boxWidth="85"
			onDataLoadCallBack="verificaMoedaPais"
		>
			<adsm:textbox dataType="currency" maxLength="18" property="vlTarifaSpot" size="10"/>
		</adsm:combobox>
		
		<adsm:textbox dataType="integer" minValue="1" maxLength="2" property="nrPossibilidades" label="numeroLiberacoes" labelWidth="20%" width="40%" size="5" required="true"/>
		<adsm:textbox dataType="integer" maxLength="2" property="nrUtilizacoes" label="numeroUtilizacoes" disabled="true" 
			labelWidth="20%" width="20%" size="5"/>

		<adsm:complement label="funcionarioLiberacao" labelWidth="20%" width="40%">
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioLiberador.funcionario.chapa"
					 size="12" serializable="false" disabled="true"/>
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioLiberador.funcionario.codPessoa.nome"
					 size="30" serializable="false" disabled="true"/>
		</adsm:complement>
	 	<adsm:textbox dataType="JTDate" serializable="false" picker="false" property="dtLiberacao" label="dataLiberacao" disabled="true" labelWidth="20%" width="20%"/>
		<adsm:textbox dataType="integer" serializable="false" property="dsSenha" label="codigoLiberacao" labelWidth="20%" width="30%" disabled="true" size="8"/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	
	var moedaDefault;
	var moedas;
	
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" 
			&& eventObj.name != "storeButton") {
			setElementValue("nrUtilizacoes", 0);
			disabledLookupFuncionario(true);
			if(moedas) {
				moeda_idMoeda_cb(moedas);
				if(moedaDefault)
					setElementValue("moeda.idMoeda", moedaDefault);
			}
			if(dadosUsuario)
				populaUsuarioLogado(dadosUsuario);
		} else {
			setDisabled("funcionario.idUsuario", false);
		}
	}

	function populaUsuarioLogado(dados){
		var chapa = getNestedBeanPropertyValue(dados, "chapa");
		var nome = getNestedBeanPropertyValue(dados, "nmUsuario");
		setElementValue("usuarioByIdUsuarioLiberador.funcionario.chapa", chapa);
		setElementValue("usuarioByIdUsuarioLiberador.funcionario.codPessoa.nome", nome);
	}

	function disabledLookupFuncionario(disabled) {
		setDisabled("funcionario.idUsuario", disabled);
		resetValue("funcionario.idUsuario");
	}

	function lookup_filial_cb(dados, erros) {
		verificaFilial(dados[0]);
		return filial_sgFilial_exactMatch_cb(dados);
	}

	function verificaFilial(dados){
		if(dados) {
			var v = getNestedBeanPropertyValue(dados, "idFilial");
			if(v != undefined){
				disabledLookupFuncionario(false);
			}
		} else {
			disabledLookupFuncionario(true);
		}
		return true;
	}

	function lookup_filial_onchange(){
		var filial = getElementValue("filial.sgFilial");
		if (filial == "" || filial == undefined) {
			disabledLookupFuncionario(true);
		} 
		return filial_sgFilialOnChangeHandler();
	}
	
	function verificaMoedaPais_cb(dados) {
		moeda_idMoeda_cb(dados);
		moedas = dados;
		if(dados) {
			for(var i = 0; i < dados.length; i++) {
				var indUtil = getNestedBeanPropertyValue(dados[i], "blIndicadorMaisUtilizada");
				if(indUtil == true || indUtil == 'true'){
					moedaDefault = getNestedBeanPropertyValue(dados[i], "idMoeda");
					setElementValue("moeda.idMoeda", moedaDefault);
					return;
				}
			}
		}
		
	}

</script>