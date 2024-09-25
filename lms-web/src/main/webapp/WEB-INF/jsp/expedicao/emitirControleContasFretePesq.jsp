<script type="text/javascript">
<!--
	var idFilialUsuario;
	var dsFilialUsuario;
	var sgFilialUsuario;
	var nmFantasiaFilialUsuario;
	var impressoraUsuario;

	function filialOnChange() {
		var idFilial = getElementValue("filial.idFilial");
		var sdo = createServiceDataObject("lms.expedicao.emitirControleContasFreteAction.findFilialById", "filialOnChange", {filial:{idFilial:idFilial}});
		xmit({serviceDataObjects:[sdo]}); 
	}

	function filialOnChange_cb(dados, errorMsg){
		if (errorMsg) {
			alert(errorMsg);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		if(dados != undefined) {
			setElementValue("filial.idFilial", dados.idFilial);
			setElementValue("filial.dsFilial", dados.dsFilial);
			setElementValue("filial.sgFilial", dados.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dados.nmFantasia);
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
			return true;
		}
	}

	function filialDataLoad_cb(data) {
		if(data) {
			filial_idFilial_cb(data);
			if(!idFilialUsuario) {
				getFilialUsuario();
			}
		}
	}

	function impressoraDataLoad_cb(data) {
		if(data) {
			impressora_idImpressora_cb(data);
			if(!impressoraUsuario) {
				getImpressoraUsuario();
			} else {
				setImpressoraPadrao();
			}
		}
	}

	function setFilialPadrao() {
		setElementValue("filial.idFilial", idFilialUsuario);
		setElementValue("filial.dsFilial", dsFilialUsuario);
		setElementValue("filial.sgFilial", sgFilialUsuario);
		setElementValue("filial.pessoa.nmFantasia", nmFantasiaFilialUsuario);
		notifyElementListeners({e:document.getElementById("filial.idFilial")});
	}

	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.expedicao.emitirControleContasFreteAction.findFilialUsuarioLogado", "getFilialUsuario");
		xmit({serviceDataObjects:[sdo]}); 
	}
	function getFilialUsuario_cb(data, error) {
		if (error) {
			alert(error);
			return false;
		}
		idFilialUsuario = data.idFilial;
		dsFilialUsuario = data.dsFilial;
		sgFilialUsuario = data.sgFilial;
		nmFantasiaFilialUsuario = data.nmFantasia;
		setFilialPadrao();
	}

	function getImpressoraUsuario() {
		var sdo = createServiceDataObject("lms.expedicao.emitirControleContasFreteAction.findImpressoraUsuarioLogado", "getImpressoraUsuario", {userip:''});
		xmit({serviceDataObjects:[sdo]}); 
	}
	function getImpressoraUsuario_cb(dados, errorMsg) {
		if (errorMsg) {
			alert(errorMsg);
			return false;
		}
		if(dados != undefined) {
			impressoraUsuario = dados;
			setImpressoraPadrao();
			return true;
		}
	}

	function setImpressoraPadrao() {
		if(getElementValue("filial.idFilial") == idFilialUsuario) {
			setElementValue("impressora.idImpressora", impressoraUsuario.idImpressora);
			setElementValue("impressora.nmImpressora", impressoraUsuario.dsCheckIn);
		}
	}
	
	function initWindow(eventObj) {
	//alert("eventObj.name="+eventObj.name);
		if (eventObj.name == "cleanButton_click"){
			setFilialPadrao();
		}
	}
//-->
</script>

<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/expedicao/emitirControleContasFrete">

		<adsm:hidden property="userip"/>
		<adsm:combobox 
			property="filial.idFilial" 
			optionLabelProperty="dsFilial" 
			optionProperty="idFilial" 
			onlyActiveValues="true"
			service="lms.expedicao.emitirControleContasFreteAction.findFiliaisUsuario"
			label="filial" 
			boxWidth="200"
			width="38%"
			labelWidth="12%"
			onchange="filialOnChange();"
			onDataLoadCallBack="filialDataLoad"
		>
			<adsm:propertyMapping relatedProperty="filial.dsFilial" modelProperty="dsFilial"/>
		</adsm:combobox>
		<adsm:hidden property="filial.dsFilial"/>
		<adsm:hidden property="filial.sgFilial"/>
		<adsm:hidden property="filial.pessoa.nmFantasia"/>

		<adsm:combobox
			property="impressora.idImpressora"
			optionLabelProperty="dsCheckIn" 
			optionProperty="idImpressora" 
			onlyActiveValues="true"
			service="lms.expedicao.emitirControleContasFreteAction.findImpressoraLookup"
			label="impressora" 
			onDataLoadCallBack="impressoraDataLoad"
			boxWidth="200"
		>
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="idFilial"/>
			<adsm:propertyMapping relatedProperty="impressora.nmImpressora" modelProperty="dsCheckIn"/>
		</adsm:combobox>
		<adsm:hidden property="impressoraIdUsuario"/>
		<adsm:hidden property="impressora.nmImpressora"/>

		<adsm:range 
			label="periodo" 
			required="true" 
			labelWidth="12%" 
			width="38%"
		>
			<adsm:textbox 
				dataType="JTDateTimeZone"
				property="dataInicial" 
				required="true" 
				smallerThan="dataFinal"
			/>
			<adsm:textbox 
				dataType="JTDateTimeZone" 
				property="dataFinal"
				biggerThan="dataInicial"
			/>
		</adsm:range>

		<adsm:lookup 
			property="funcionario" 
			idProperty="idUsuario" 
			criteriaProperty="nrMatricula"
			serializable="false" 
			service="lms.expedicao.emitirControleContasFreteAction.findLookupUsuarioFuncionario" 
			dataType="text" 
			label="funcionario" 
			size="10" 
			maxLength="15" 
			action="/configuracoes/consultarFuncionariosView"
			exactMatch="true" 
		>
			<adsm:propertyMapping 
				relatedProperty="funcionario.codPessoa.nome" 
				modelProperty="nmUsuario"
			/>
			<adsm:propertyMapping 
				relatedProperty="usuario.idUsuario" 
				modelProperty="idUsuario"
			/>

			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>

			<adsm:textbox 
				property="funcionario.codPessoa.nome" 
				dataType="text" 
				size="25" 
				disabled="true" 
			/>
		</adsm:lookup>
		<adsm:hidden property="usuario.idUsuario"/>

		<adsm:lookup 
			label="cliente"
			property="clienteByIdCliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.emitirControleContasFreteAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			size="20"
			maxLength="20"
			labelWidth="12%"
			width="75%"
			exactMatch="true"
		>
			<adsm:propertyMapping
				relatedProperty="clienteByIdCliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa"
			/>
			<adsm:propertyMapping
				relatedProperty="clienteByIdCliente.tpCliente"
				modelProperty="tpCliente.value"
			/>
			<adsm:textbox
				dataType="text"
				property="clienteByIdCliente.pessoa.nmPessoa"
				size="25"
				disabled="true"
			/>
			<adsm:hidden property="clienteByIdCliente.tpCliente" />
		</adsm:lookup>

		<adsm:buttonBar>
			<!-- expedicao/emitirControleContasFrete.jasper -->
			<adsm:reportViewerButton service="lms.expedicao.emitirControleContasFreteAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
