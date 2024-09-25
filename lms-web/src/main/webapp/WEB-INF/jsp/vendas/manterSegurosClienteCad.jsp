<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.seguroClienteService" >
	<adsm:form action="/vendas/manterSegurosCliente" idProperty="idSeguroCliente" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-01172"/>
        </adsm:i18nLabels>		

		<!-- LMS-6148 -->
		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="nrIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterSegurosClienteAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="13%"
			maxLength="20"
			required="true"
			size="20"
			width="87%"
		>
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:propertyMapping 
				relatedProperty="cliente.idCliente" 
				modelProperty="idCliente" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				size="80"
				maxLength="60" 
			/>
		</adsm:lookup>

		<adsm:combobox
			property="tpModal"
			label="modal"
			domain="DM_MODAL"
			required="true"
			onlyActiveValues="true"
			onchange="validateCombos();"
			labelWidth="13%"
			width="32%"
		/>

		<adsm:combobox
			property="tpAbrangencia"
			domain="DM_ABRANGENCIA"
			label="abrangencia"
			required="true"
			onlyActiveValues="true"
			onchange="validateCombos();"
			labelWidth="10%"
			width="45%"
		/>
		
		<adsm:combobox
			property="tipoSeguro.idTipoSeguro"
			label="tipoSeguro"
			autoLoad="false"
			style="width:100px"
			optionLabelProperty="sgTipo"
			optionProperty="idTipoSeguro"
			onlyActiveValues="true"
			required="true"
			labelWidth="13%"
			width="50%"
		/>

		<!-- LMS-6148 -->
		<adsm:combobox 
			label="seguradora"
			property="seguradora.idSeguradora"
			optionProperty="seguradora.pessoa.idPessoa"
			optionLabelProperty="seguradora.pessoa.nmPessoa"
			service="lms.vendas.manterSegurosClienteAction.findSeguradoraOrderByNmPessoa"
			required="true"
			labelWidth="13%" 
			width="33%" 
		/>	
		
		<!-- LMS-6148 -->
		<adsm:combobox 
			label="reguladora"
			property="reguladoraSeguro.idReguladora"
			optionProperty="pessoa.idPessoa"
			optionLabelProperty="pessoa.nmPessoa"
			service="lms.vendas.manterSegurosClienteAction.findReguladoraOrderByNmPessoa"
			labelWidth="9%"
			width="35%" 
		/>	

		<adsm:textbox
			dataType="text"
			property="dsApolice"
			label="apolice"
			maxLength="60"
			labelWidth="13%"
			width="18%"
		/>
		
		<!-- LMS-6148 -->
		<adsm:checkbox 
			label="emEmissao" 
			property="blEmEmissao"
			labelWidth="8%"
			width="7%"
		/>

		<adsm:complement label="valorLimite" width="40%" labelWidth="9%" required="true" separator="branco">
			<adsm:combobox
				property="moeda.idMoeda"
				boxWidth="85"
				service="lms.vendas.manterSegurosClienteAction.findMoeda"
				onlyActiveValues="true"
				optionLabelProperty="descricao"
				optionProperty="idMoeda">
			</adsm:combobox>
			<adsm:textbox
				dataType="currency"
				property="vlLimite"
				size="22"
				maxLength="18" />
		</adsm:complement>

		<adsm:range label="vigencia" required="true" labelWidth="13%" width="35%">
			<adsm:textbox
				dataType="JTDate"
				property="dtVigenciaInicial"/>
			<adsm:textbox
				dataType="JTDate"
				property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:textbox 
			label="taxa" 
			mask="##0.000" 
			required="false" 
			unit="percent" 
			dataType="decimal" 
			property="percTaxa" 
			size="15" 
			disabled="false" 
			maxLength="6" 
			labelWidth="7%" 
			width="40%" 
			serializable="true" 
			onchange="return verificaPerc();"
		/>
		
		<!-- LMS-6148 -->
		<adsm:lookup 
			label="municipioOrigem" 
			dataType="text" 
			property="municipioOrigem" 
			idProperty="idMunicipio" 
			criteriaProperty="nmMunicipio"
			service="lms.vendas.manterSegurosClienteAction.findLookupMunicipio"
			action="/municipios/manterMunicipios" 
			size="30" 
			maxLength="60" 
			width="28%"
			labelWidth="13%" 
			exactMatch="false" 
			minLengthForAutoPopUpSearch="3" 
			required="false"
		>
			<adsm:propertyMapping 
				modelProperty="unidadeFederativa.sgUnidadeFederativa" 
				relatedProperty="municipioOrigem.unidadeFederativa.sgUnidadeFederativa" 
			/>
			
			<adsm:textbox 
				label="uf"
				dataType="text" 
				disabled="true" 
				property="municipioOrigem.unidadeFederativa.sgUnidadeFederativa" 
				width="32%"
				labelWidth="4%" 
				size="2"
				maxLength="2" 
			/>
		</adsm:lookup>	
		
		<!-- LMS-6148 -->	
		<adsm:lookup 
			label="municipioDestino" 
			dataType="text" 
			property="municipioDestino" 
			idProperty="idMunicipio" 
			criteriaProperty="nmMunicipio"
			service="lms.vendas.manterSegurosClienteAction.findLookupMunicipio"
			action="/municipios/manterMunicipios" 
			size="30" 
			maxLength="60" 
			width="28%"
			labelWidth="13%" 
			exactMatch="false" 
			minLengthForAutoPopUpSearch="3" 
			required="false"
		>
			<adsm:propertyMapping 
				modelProperty="unidadeFederativa.sgUnidadeFederativa" 
				relatedProperty="municipioDestino.unidadeFederativa.sgUnidadeFederativa" 
			/>
			
			<adsm:textbox 
				label="uf"
				dataType="text" 
				disabled="true" 
				property="municipioDestino.unidadeFederativa.sgUnidadeFederativa" 
				width="8%"
				labelWidth="4%" 
				size="2"
				maxLength="2" 
			/>
		</adsm:lookup>	
			
		<!-- LMS-6148 -->
		<adsm:textarea 
			label="coberturas" 
			property="dsCobertura" 
			maxLength="1500" 
			columns="120" 
			rows="3" 
			labelWidth="13%" 
			width="87%" 
		/>
		
		<!-- LMS-6148 -->
		<adsm:textarea 
			label="mercadoria" 
			property="dsMercadoria" 
			maxLength="1500" 
			columns="120" 
			rows="3" 
			labelWidth="13%" 
			width="87%" 
		/>
		
		<!-- LMS-6148 -->
		<adsm:textarea 
			label="embalagem" 
			property="dsEmbalagem" 
			maxLength="1500" 
			columns="120" 
			rows="3" 
			labelWidth="13%" 
			width="87%" 
		/>
		
		<adsm:textbox
			dataType="file"
			property="dcCartaIsencao"
			blobColumnName="DC_CARTA_ISENCAO"
			tableName="SEGURO_CLIENTE"
			primaryKeyColumnName="ID_SEGURO_CLIENTE"
			primaryKeyValueProperty="idSeguroCliente"
			label="cartaIsencao"
			size="40"
			labelWidth="13%"
			width="87%"
		/>
		
		<!-- LMS-6148 -->
		<adsm:complement label="comercialResponsavel" width="45%" labelWidth="16%" separator="branco">
			<adsm:textbox
				dataType="text"
				property="nrMatriculaComercialResponsavel"
				size="15"
				maxLength="20"
				disabled="true" 
			/>
			<adsm:textbox
				dataType="text" 
				property="nmUsuarioComercialResponsavel"
				size="24"
				maxLength="45"
				disabled="true" 
			/>
		</adsm:complement>
		
		<!-- LMS-6148 -->
		<adsm:textbox 
			label="dataEnvioUltimoAviso"
			dataType="JTDateTimeZone" 
			picker="false"
			property="dhEnvioAviso"
			width="32%" 
			labelWidth="16%"
			disabled="true"
		/>
		
		<!-- LMS-6148 -->
		<adsm:hidden property="usuarioAviso.idUsuario"/>
		<adsm:complement label="usuarioAvisado" width="38%" labelWidth="13%" separator="branco">
			<adsm:textbox
				dataType="text"
				property="usuarioAviso.nrMatricula"
				size="15"
				maxLength="20"
				disabled="true" 
			/>
			<adsm:textbox
				dataType="text" 
				property="usuarioAviso.nmUsuario"
				size="24"
				maxLength="45"
				disabled="true" 
			/>
		</adsm:complement>
		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:button
				buttonType="newButton"
				id="btnLimpar"
				caption="limpar"
				disabled="false"
				onclick="resetForm(this.document)"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/*
	valida se a porcentagem informada é maior que zero ou menor ou igual a 100
	*/
	function verificaPerc(){
		var pcTaxa = getElementValue("percTaxa");
		if(pcTaxa != ''){
			if(pcTaxa <= 0 || pcTaxa > 100){
				alertI18nMessage("LMS-01172");
				return false;
			}else{
				return true;
			}
		}
		return true;
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
		findPromotorClientes();
		habilitaCampoCliente();
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindow(){
		setDisabled(document.getElementById("cliente.nrIdentificacao"), false);
		document.getElementById("cliente.pessoa.nrIdentificacao").focus();
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("btnLimpar", true);
			setDisabled("removeButton", true);
		}
		habilitaCampoCliente();
	}
	 
	function habilitaCampoCliente() {
		if(getElementValue("cliente.pessoa.nrIdentificacao") != null && getElementValue("cliente.pessoa.nrIdentificacao") != "") {
			setDisabled(document.getElementById("cliente.nrIdentificacao"), true);
			document.getElementById("tpModal").focus();
		}
	}	
	
	/* */
	function findPromotorClientes() {
		var data = new Array();
		
		setNestedBeanPropertyValue(data, "id", getElementValue("idSeguroCliente")); 
		
		var sdo = createServiceDataObject("lms.vendas.seguroClienteService.findPromotorClientes", "promotorClientes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function promotorClientes_cb(data, error) {
		if (error != undefined) {
			alert(error);
		}
		else {
			if (data.length > 0) {
				setElementValue("nrMatriculaComercialResponsavel", getNestedBeanPropertyValue(data[0], "nrMatricula"));
				setElementValue("nmUsuarioComercialResponsavel", getNestedBeanPropertyValue(data[0], "nmUsuario"));
			}
		}
	}
	
	/** Função que valida se as combos de modal e abrangência estão preenchidas, para preencher a combo de tipo de seguro */
	function validateCombos(){
		document.getElementById("tipoSeguro.idTipoSeguro").length = 1;
		if(getElementValue("tpModal") != '' &&
				getElementValue("tpAbrangencia") != ''){
			var sgModal = getElementValue("tpModal");
			var sgAbrangencia = getElementValue("tpAbrangencia");
			var data = new Array();

			/** Insere os dois campos com suas chaves e valores no Array que posteriormente será transformado em um Map pela XMLBroker */
			setNestedBeanPropertyValue(data, "sgModal", sgModal);
			setNestedBeanPropertyValue(data, "sgAbrangencia", sgAbrangencia);
			setNestedBeanPropertyValue(data, "tpSituacao", "A");

			/** Invoca o método da service, especifica a função JS que será executada no callBack e passa os dados para serem usados como filtro */
			var sdo = createServiceDataObject("lms.seguros.tipoSeguroService.findComboByTipoSeguro", "validateCombos", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	/** Função que popula a combo tipo seguro no callBack */
	function validateCombos_cb(data, error, erromsg){
		if(error == null){
			/** Função gerada pelo framework para popular o combo */
			tipoSeguro_idTipoSeguro_cb(data);	
		}
	}

	/** Função responsável por limpar os campos da tela e colocar o focus no primeiro campo */
	function resetForm(doc){
		/** Reseta todos campos da tela */
		newButtonScript(doc, true, {name:'newButton_click'});

		/** Retira todos optioms da Select deixando apenas o primeiro */
		document.getElementById("tipoSeguro.idTipoSeguro").length = 1;

		/** Coloca o focus no primeiro campo da tela */
		setFocusOnFirstFocusableField(doc);

		/** Habilita o botão limpar */
		setDisabled("btnLimpar", false);
	}
</script>