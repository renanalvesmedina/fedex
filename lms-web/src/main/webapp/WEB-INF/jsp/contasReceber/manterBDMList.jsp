<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterBDMAction">

	<adsm:form action="/contasReceber/manterBDM">

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialEmissora" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialOrigem" 
					 size="3" 
					 maxLength="3" 
					 width="35%"
					 labelWidth="15%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialEmissora.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialEmissora.pessoa.nmFantasia" size="30" maxLength="30" disabled="true" serializable="true"/>
		</adsm:lookup>	

        <adsm:textbox label="numero" property="nrBdm" dataType="integer" size="10" labelWidth="20%" width="30%" />

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialDestino" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialDestino" 
					 size="3" 
					 maxLength="3" 
					 width="35%"
					 labelWidth="15%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="30" maxLength="30" disabled="true" serializable="true"/>
		</adsm:lookup>	

        <adsm:range label="dataEmissao" labelWidth="20%" width="30%">
			<adsm:textbox property="dtEmissaoInicial" dataType="JTDate"/>
			<adsm:textbox property="dtEmissaoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS_BDM" labelWidth="15%" width="35%"/>

		<adsm:lookup label="responsavelFrete"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupClientes" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 width="85%"
					 serializable="true"
					 labelWidth="15%"
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="50" maxLength="50" />
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="baixaDevMerc"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid selectionMode="none" 
  			   idProperty="idBaixaDevMerc"
			   property="baixaDevMerc"
			   service="lms.contasreceber.manterBDMAction.findPaginated"
			   rowCountService="lms.contasreceber.manterBDMAction.getRowCount"
			   rows="10"
	>	
		<adsm:gridColumn title="filialOrigem" property="filialEmissora" width="11%" dataType="text"/>
		<adsm:gridColumn title="numero" property="nrBdm" width="10%" dataType="integer"/>
		<adsm:gridColumn title="filialDestino" property="filialDestino" width="11%" dataType="text"/>
		<adsm:gridColumn title="dataEmissao" property="dtEmissao" width="14%" dataType="JTDate"/>
        <adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%" dataType="text"/>
        <adsm:gridColumn title="cliente" property="cliente" width="26%" />
        
        <adsm:buttonBar>
        </adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>

	function initWindow(eventObj){
		if( eventObj.name == "tab_load" || eventObj.name == "cleanButton_click") {
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBDMAction.findFilialUsuarioLogado",
				"setFilialUsuario", 
				new Array()));
	        xmit(false);
		}
	}

	function setFilialUsuario_cb(data, error) {
		if (data != null) {
			setElementValue("filialEmissora.idFilial", data.idFilial);
			setElementValue("filialEmissora.sgFilial", data.sgFilial);
			setElementValue("filialEmissora.pessoa.nmFantasia", data.pessoa.nmFantasia);
		}
	}

</script>