<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.tabelaprecos.manterTabelasPrecoAction">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-30054" />
	</adsm:i18nLabels>

	<adsm:form
		action="/tabelaPrecos/manterTabelasPreco"
		idProperty="idTabelaPreco">

		<adsm:hidden
			property="tabelaPrecoString"
			serializable="false"/>
			
		<adsm:hidden
			property="tpProposta"
			serializable="false"/>
		
		<adsm:hidden
			property="blTabelaFob" />

		<adsm:combobox
			onchange="carregaSubTipos(this);"
			label="tipo"
			property="tipoTabelaPreco.idTipoTabelaPreco"
			service="lms.tabelaprecos.tipoTabelaPrecoService.findTipoTabelaPrecoAtivo"
			optionProperty="idTipoTabelaPreco"
			optionLabelProperty="tpTipoTabelaPrecoNrVersao">
			
			<adsm:propertyMapping
				relatedProperty="empresa"
				modelProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"/>

		</adsm:combobox>

		<adsm:combobox
			autoLoad="false"
			label="subTipo"
			property="subtipoTabelaPreco.idSubtipoTabelaPreco"
			service="lms.tabelaprecos.subtipoTabelaPrecoService.findByTpTipoTabelaPreco"
			optionProperty="idSubtipoTabelaPreco"
			optionLabelProperty="tpSubtipoTabelaPreco">
			
		</adsm:combobox>
		
		<adsm:combobox
			label="categoria"
			domain="DM_CATEGORIA_TABELA"
			property="tpCategoria" />
		
		<adsm:combobox
			label="tipoServico"
			domain="DM_TIPO_SERVICO_TABELA"
			property="tpServico" />	
		
		<adsm:textbox
			label="empresa"
			serializable="false"
			property="empresa"
			dataType="text"
			disabled="true"
			size="30"
			maxLength="60"/>

		<adsm:combobox
			label="efetivada"
			property="blEfetivada"
			domain="DM_SIM_NAO"/>

		<adsm:textbox
			label="vigencia"
			dataType="JTDate"
			property="dtVigencia"/>

		<adsm:buttonBar
			freeLayout="true">

			<adsm:findButton
				callbackProperty="tabelaPrecos"/>
			<adsm:resetButton/>

		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		property="tabelaPrecos"
		defaultOrder="tipoTabelaPreco_.tpTipoTabelaPreco, tipoTabelaPreco_.nrVersao, subtipoTabelaPreco_.tpSubtipoTabelaPreco, tipoTabelaPreco_empresaByIdEmpresaCadastrada_pessoa_.nmPessoa"
		idProperty="idTabelaPreco"
		gridHeight="200"
		rows="10"
		unique="true"
		scrollBars="horizontal"
		onRowClick="rowClickTabelaPreco">

		<adsm:gridColumn
			title="tipo"
			property="tipoTabelaPreco.tpTipoTabelaPrecoNrVersao"
			width="50"/>

		<adsm:gridColumn
			title="subTipo"
			property="subtipoTabelaPreco.tpSubtipoTabelaPreco"
			width="50"/>

		<adsm:gridColumn
			title="empresa"
			property="tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa"
			width="200"/>

		<adsm:gridColumn
			title="descricao"
			property="dsDescricao"
			width="300"/>

		<adsm:gridColumn
			title="efetivada"
			property="blEfetivada"
			width="100"
			renderMode="image-check"/>

		<adsm:gridColumn
			title="vigenciaInicial"
			dataType="JTDate"
			property="dtVigenciaInicial"
			width="100"/>

		<adsm:gridColumn
			title="vigenciaFinal"
			dataType="JTDate"
			property="dtVigenciaFinal"
			width="100"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	
	function carregaSubTipos(e){
		if(e.selectedIndex > 0) {
			var v = getNestedBeanPropertyValue(e.data, ":" + (e.selectedIndex - 1) + ".tpTipoTabelaPreco.value");
			var sdo = createServiceDataObject("lms.tabelaprecos.subtipoTabelaPrecoService.findByTpTipoTabelaPreco", "subtipoTabelaPreco.idSubtipoTabelaPreco", {tpTipoTabelaPreco:v, blTabelaFob: getElementValue("blTabelaFob")});
			xmit({serviceDataObjects:[sdo]});
		} else {
			var combo = document.getElementById("subtipoTabelaPreco.idSubtipoTabelaPreco");
			for(var i = combo.options.length; i >= 1; i--) {
				combo.options.remove(i);
			}
			combo.selectedIndex = 0
		}
		comboboxChange({e:e});
	}
	
	function disableTabParcela(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("parcela", disabled);
	}
	
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "tab_click"){
			disableTabParcela(true);
		} else if(event == "cleanButton_click"){
			limpaComboSubtipos();
		} 
	}

	function limpaComboSubtipos(){
		var combo = getElement("subtipoTabelaPreco.idSubtipoTabelaPreco");
		combo.options.length = 1;
		combo.selectedIndex = 0;
	}
	
	function rowClickTabelaPreco(pkValue) {
		var url = new URL(location.href);
		if ((url.parameters != undefined) && (url.parameters["mode"] == "lookup")) {
			if (getElementValue("tpProposta") == "P") {
				var rowData = tabelaPrecosGridDef.findById(pkValue);
				var tpTipoTabelaPreco = rowData.tipoTabelaPreco.tpTipoTabelaPreco.value;
				return validaTabelaPreco(tpTipoTabelaPreco);
			}
		}
		return true;
	}
	
	function validaTabelaPreco(tpTipoTabelaPreco) {
		if (tpTipoTabelaPreco == "C") {
			alertI18nMessage("LMS-30054");
			return false;
		}
		return true;
	}
</script>