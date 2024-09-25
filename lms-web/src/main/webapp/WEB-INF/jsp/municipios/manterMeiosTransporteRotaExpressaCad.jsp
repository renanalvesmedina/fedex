<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterMeiosTransporteRotaExpressaAction" onPageLoad="pageLoadCustom" >
	<adsm:form action="/municipios/manterMeiosTransporteRotaExpressa" idProperty="idMeioTransporteRotaViagem"
			onDataLoadCallBack="meioTranspLoad" >	
		<adsm:hidden property="rotaViagem.idRotaViagem" value="1" />
		<adsm:hidden property="rotaViagem.versao" value="1" />

		<adsm:textbox dataType="integer" label="rotaIda" property="rotaIda.nrRota" disabled="true" mask="0000" size="4" serializable="false" labelWidth="20%" width="30%">
			<adsm:textbox dataType="text" property="rotaIda.dsRota" disabled="true" size="25" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="integer" label="rotaVolta" property="rotaVolta.nrRota" disabled="true" mask="0000" size="4" serializable="false" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="text" property="rotaVolta.dsRota" disabled="true" size="25" serializable="false"/>
		</adsm:textbox>

		<adsm:hidden property="blChangeServiceTipoMTCombo" value="S" />

		<adsm:hidden property="meioTransporte.tpSituacao" value="A" />
		<adsm:hidden property="isCalledByLookup" value="true" />
		<adsm:hidden property="tipoMeioTransporte.idTipoMeioTransporte" />
		<adsm:lookup
			label="meioTransporte"
			property="meioTransporteRodoviario2"
			idProperty="idMeioTransporte"
			criteriaProperty="meioTransporte.nrFrota"
			service="lms.municipios.manterMeiosTransporteRotaExpressaAction.findLookupMeioTransporteRodoviario"
			action="/contratacaoVeiculos/manterMeiosTransporte"
			picker="false"
			cmd="rodo"
			dataType="text"
			labelWidth="20%"
			width="80%"
			size="8"
			maxLength="6"
			exactMatch="true"
			serializable="false"
			required="true"
		>
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador"/>
			<adsm:propertyMapping criteriaProperty="isCalledByLookup" modelProperty="isCalledByLookup"/>
			<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="meioTransporte.tpSituacao" />
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.nrAnoFabricao" modelProperty="meioTransporte.nrAnoFabricao" />
			<adsm:propertyMapping relatedProperty="meioTransporte.nrCapacidadeM3" modelProperty="meioTransporte.nrCapacidadeM3" />
			<adsm:propertyMapping relatedProperty="meioTransporte.nrCapacidadeKg" modelProperty="meioTransporte.nrCapacidadeKg" />
			<adsm:propertyMapping relatedProperty="meioTransporte.meioTransporteRodoviario.nrRastreador" modelProperty="meioTransporte.meioTransporteRodoviario.nrRastreador" />
			<adsm:propertyMapping relatedProperty="meioTransporte.meioTransporteRodoviario.operadoraMct.pessoa.nmPessoa" modelProperty="meioTransporte.meioTransporteRodoviario.operadoraMct.pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="outrasInfo.dsEndereco" modelProperty="outrasInfo.dsEndereco" />
			<adsm:propertyMapping relatedProperty="outrasInfo.dsBairro" modelProperty="outrasInfo.dsBairro"/>
			<adsm:propertyMapping relatedProperty="outrasInfo.nrCep" modelProperty="outrasInfo.nrCep"/>
			<adsm:propertyMapping relatedProperty="outrasInfo.nrDdd" modelProperty="outrasInfo.nrDdd" />
			<adsm:propertyMapping relatedProperty="outrasInfo.nrTelefone" modelProperty="outrasInfo.nrTelefone" />
			<adsm:propertyMapping relatedProperty="outrasInfo.nmMunicipio" modelProperty="outrasInfo.nmMunicipio" />
			<adsm:propertyMapping relatedProperty="outrasInfo.sgUnidadeFederativa" modelProperty="outrasInfo.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="outrasInfo.dsEmail" modelProperty="outrasInfo.dsEmail" />
			<adsm:propertyMapping relatedProperty="outrasInfo.nrPis" modelProperty="outrasInfo.nrPis" />
			<adsm:propertyMapping relatedProperty="outrasInfo.nrIdentificacaoFormatado" modelProperty="outrasInfo.nrIdentificacaoFormatado" />
			<adsm:propertyMapping relatedProperty="outrasInfo.nmPessoa" modelProperty="outrasInfo.nmPessoa" />
			<adsm:propertyMapping relatedProperty="outrasInfo.tpIdentificacaoPessoa" modelProperty="outrasInfo.tpIdentificacaoPessoa.description" />
			<adsm:propertyMapping relatedProperty="outrasInfo.idPessoa" modelProperty="outrasInfo.idPessoa" />
			<adsm:propertyMapping relatedProperty="outrasInfo.nrAntt" modelProperty="outrasInfo.nrAntt" />
			<adsm:propertyMapping relatedProperty="outrasInfo.nmPais" modelProperty="outrasInfo.nmPais" />		
			<adsm:propertyMapping relatedProperty="outrasInfo.nmContato" modelProperty="outrasInfo.nmContato" />					  				  

			<adsm:lookup
				property="meioTransporteRodoviario"
				idProperty="idMeioTransporte"
				criteriaProperty="meioTransporte.nrIdentificador"
				service="lms.municipios.manterMeiosTransporteRotaExpressaAction.findLookupMeioTransporteRodoviario"
				action="/contratacaoVeiculos/manterMeiosTransporte"
				picker="true"
				cmd="rodo"
				dataType="text"
				minLengthForAutoPopUpSearch="3"
				maxLength="25"
				size="25"
			>
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping criteriaProperty="isCalledByLookup" modelProperty="isCalledByLookup"/>
				<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="meioTransporte.tpSituacao" />
				<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
				<adsm:propertyMapping relatedProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporte.nrAnoFabricao" modelProperty="meioTransporte.nrAnoFabricao" />
				<adsm:propertyMapping relatedProperty="meioTransporte.nrCapacidadeM3" modelProperty="meioTransporte.nrCapacidadeM3" />
				<adsm:propertyMapping relatedProperty="meioTransporte.nrCapacidadeKg" modelProperty="meioTransporte.nrCapacidadeKg" />
				<adsm:propertyMapping relatedProperty="meioTransporte.meioTransporteRodoviario.nrRastreador" modelProperty="meioTransporte.meioTransporteRodoviario.nrRastreador" />
				<adsm:propertyMapping relatedProperty="meioTransporte.meioTransporteRodoviario.operadoraMct.pessoa.nmPessoa" modelProperty="meioTransporte.meioTransporteRodoviario.operadoraMct.pessoa.nmPessoa" />
				<adsm:propertyMapping relatedProperty="outrasInfo.dsEndereco" modelProperty="outrasInfo.dsEndereco" />
				<adsm:propertyMapping relatedProperty="outrasInfo.dsBairro" modelProperty="outrasInfo.dsBairro" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nrCep" modelProperty="outrasInfo.nrCep" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nrDdd" modelProperty="outrasInfo.nrDdd" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nrTelefone" modelProperty="outrasInfo.nrTelefone" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nmMunicipio" modelProperty="outrasInfo.nmMunicipio" />
				<adsm:propertyMapping relatedProperty="outrasInfo.sgUnidadeFederativa" modelProperty="outrasInfo.sgUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="outrasInfo.dsEmail" modelProperty="outrasInfo.dsEmail" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nrPis" modelProperty="outrasInfo.nrPis" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nrIdentificacaoFormatado" modelProperty="outrasInfo.nrIdentificacaoFormatado" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nmPessoa" modelProperty="outrasInfo.nmPessoa" />	
				<adsm:propertyMapping relatedProperty="outrasInfo.tpIdentificacaoPessoa" modelProperty="outrasInfo.tpIdentificacaoPessoa.description" />
				<adsm:propertyMapping relatedProperty="outrasInfo.idPessoa" modelProperty="outrasInfo.idPessoa" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nrAntt" modelProperty="outrasInfo.nrAntt" />
				<adsm:propertyMapping relatedProperty="outrasInfo.nmPais" modelProperty="outrasInfo.nmPais" />	
				<adsm:propertyMapping relatedProperty="outrasInfo.nmContato" modelProperty="outrasInfo.nmContato" />						  				  					  	
			</adsm:lookup>

		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:section caption="informacoesMeioTransporte" />
		<adsm:textbox dataType="text" property="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte"
				label="tipoMeioTransporte" maxLength="50" size="35" labelWidth="20%" width="30%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte"
				label="marca" maxLength="50" size="35" labelWidth="20%" width="30%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte"
				label="modelo" maxLength="30" size="35" labelWidth="20%" width="30%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.nrAnoFabricao"
				label="anoFabricacao" maxLength="4" size="10" labelWidth="20%" width="30%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.nrCapacidadeM3"
				label="capacidade" maxLength="18" unit="m3" size="20" labelWidth="20%" width="30%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.nrCapacidadeKg"
				label="capacidade" maxLength="18" unit="kg" size="20" labelWidth="20%" width="30%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.meioTransporteRodoviario.nrRastreador"
				label="rastreadorNum" maxLength="10" size="10" labelWidth="20%" width="30%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.meioTransporteRodoviario.operadoraMct.pessoa.nmPessoa"
				label="operadorRastreador" maxLength="50" size="35" labelWidth="20%" width="30%" disabled="true" serializable="false" />

		<adsm:section caption="informacoesProprietario" />
		<adsm:hidden property="outrasInfo.tpIdentificacaoPessoa" serializable="true" />
		<adsm:hidden property="outrasInfo.idPessoa" serializable="true" />
		<adsm:textbox dataType="text" property="outrasInfo.nrIdentificacaoFormatado"
				label="cnpj" maxLength="20" size="20" labelWidth="20%" disabled="true" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.nmPessoa"
				label="nome" maxLength="20" size="35" labelWidth="20%" disabled="true" width="30%" serializable="false" />	
		<adsm:textbox dataType="text" property="outrasInfo.nrAntt"
				label="antt" maxLength="11" size="20" disabled="true" labelWidth="20%" width="30%" serializable="false" />	
		<adsm:textbox dataType="text" property="outrasInfo.nrPis"
				label="numeroPis" maxLength="11" size="20" disabled="true" labelWidth="20%" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.dsEndereco"
				label="endereco" maxLength="100" size="35" disabled="true" labelWidth="20%" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.dsBairro"
				label="bairro" maxLength="40" size="35" disabled="true" labelWidth="20%" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.nmMunicipio"
				label="municipio" maxLength="50" size="35" disabled="true" labelWidth="20%" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.nrCep"
				label="cep" maxLength="10" size="10" disabled="true" labelWidth="20%" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.sgUnidadeFederativa"
				label="uf" maxLength="2" size="3" disabled="true" labelWidth="20%" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.nmPais"
				label="pais" maxLength="20" size="35" disabled="true" labelWidth="20%" width="30%" serializable="false" />
		<adsm:textbox dataType="text" property="outrasInfo.nmContato"
				label="contato" maxLength="40" size="35" disabled="true" labelWidth="20%" width="30%" serializable="false" />

		<adsm:complement label="telefone" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="integer" property="outrasInfo.nrDdd" maxLength="5" size="5" disabled="true" serializable="false" />
			<adsm:textbox dataType="integer" property="outrasInfo.nrTelefone" maxLength="10" size="10" disabled="true" serializable="false" />
		</adsm:complement>

		<adsm:textbox dataType="text" property="outrasInfo.dsEmail"
				label="email" maxLength="10" size="20" disabled="true" labelWidth="20%" width="30%" serializable="false" />

		<adsm:hidden property="labelPessoa" serializable="false" />

		<adsm:buttonBar>
			<adsm:button caption="dadosBancarios" id="dadosBancarios" action="configuracoes/manterDadosBancariosPessoa" cmd="main" boxWidth="105">
				<adsm:linkProperty src="outrasInfo.idPessoa" target="pessoa.idPessoa" disabled="true" />
				<adsm:linkProperty src="outrasInfo.tpIdentificacaoPessoa" target="pessoa.tpIdentificacao" disabled="true" />
				<adsm:linkProperty src="outrasInfo.nrIdentificacaoFormatado" target="pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="outrasInfo.nmPessoa" target="pessoa.nmPessoa" disabled="true" />
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>
			<adsm:storeButton id="salvar" callbackProperty="afterStore" />
			<adsm:newButton id="novo" />
			<adsm:removeButton id="excluir" />
		</adsm:buttonBar>
		<script>
			var labelPessoaVar = '<adsm:label key="proprietario"/>';
		</script>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	document.getElementById("labelPessoa").masterLink = "true";
	document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").masterLink = "true";

	function pageLoadCustom() {
		onPageLoad();
		setElementValue("labelPessoa", labelPessoaVar);
	}

	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document, true);
		setDisabled("salvar", false);
		setDisabled("novo", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setDisabled("meioTransporteRodoviario.idMeioTransporte", false);
		setDisabled("meioTransporteRodoviario2.idMeioTransporte", false);
		setFocusOnFirstFocusableField();
	}

	/**
	 * Habilitar campos se o registro estiver vigente.
	 */
	function habilitarCampos(idProprietario) {
		setDisabled("dtVigenciaFinal", false);
		setDisabled("novo", false);
		setDisabled("salvar", false);
		setDisabled("dadosBancarios",(idProprietario == undefined));
	}

	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function meioTranspLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);

		if (data != undefined) {
			var idFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporte.idMeioTransporte");
			var nrFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporte.nrFrota");

			// É necessário preencher via js a segunda lookup de Meio de Transporte.				
			if (idFrota != undefined) {
				setElementValue("meioTransporteRodoviario2.idMeioTransporte",idFrota);
				setElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota",nrFrota);
			}
			comportamentoDetalhe(data);
		}
	}

	/**
	 * Após salvar, deve-se carregar o valor da vigência inicial detalhada.
	 */
	function afterStore_cb(data,exception,key){
		store_cb(data,exception,key);	
		if(exception == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
	}

	function comportamentoDetalhe(data) {
		var idProprietario = getNestedBeanPropertyValue(data, "outrasInfo.idProprietario");
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("dadosBancarios",(idProprietario == undefined));
			setDisabled("excluir",false);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			habilitarCampos(idProprietario);
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("novo",false);
			setDisabled("dadosBancarios",(idProprietario == undefined));
			setFocusOnNewButton();
		}
	}

	/**
	 * Tratamento dos eventos da initWindow.
	 */
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton")
			estadoNovo();
	}

</script>
