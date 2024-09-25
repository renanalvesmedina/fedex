<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/carregamento/manterEquipes" idProperty="idIntegranteEquipe" 
			   service="lms.carregamento.manterEquipesAction.findByIdIntegranteEquipeTerceiro" 
			   onDataLoadCallBack="loadForm">
	
		<adsm:masterLink showSaveAll="true" idProperty="idEquipe">
			<adsm:masterLinkItem property="filial.sgFilial" label="filial" itemWidth="20" />
			<adsm:masterLinkItem property="setor.idSetor" label="setor" itemWidth="30" />
			<adsm:masterLinkItem property="dsEquipe" label="descricaoEquipe" itemWidth="50"/>
			<adsm:hidden property="tpIntegrante" value="T"/>			
		</adsm:masterLink>

		<adsm:hidden property="tpIntegrante" value="T" />
		<adsm:hidden property="pessoa.tpPessoa" value="F" />
        <adsm:hidden property="tpSituacao" value="A"/>
		<adsm:hidden property="pessoa.tpIdentificacao" />

		<adsm:lookup dataType="text" 
					 property="prestadorServico" 
					 idProperty="idPrestadorServico" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.carregamento.manterEquipesAction.findLookupIntegrante" 
					 action="/carregamento/manterPrestadoresServico"
					 label="integrante" size="20" maxLength="20" width="85%" required="true">
			<adsm:propertyMapping modelProperty="pessoa.tpPessoa" criteriaProperty="pessoa.tpPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="prestadorServico.pessoa.nmPessoa" disable="false" inlineQuery="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao" criteriaProperty="pessoa.tpIdentificacao" disable="false"/>	
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="prestadorServico.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao.value" relatedProperty="pessoa.tpIdentificacao" />	
			<adsm:propertyMapping modelProperty="pessoa.idPessoa" relatedProperty="prestadorServico.idPrestadorServico" />				
			<adsm:textbox dataType="text" property="prestadorServico.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" />
		</adsm:lookup>


		<adsm:combobox label="cargo" 
					    property="cargoOperacional.idCargoOperacional"
					    service="lms.carregamento.manterEquipesAction.findListCargo" 
					    optionProperty="idCargoOperacional" 
					    optionLabelProperty="dsCargo"
					    onlyActiveValues="true"					    					    
					    required="true" width="85%">	
			<adsm:propertyMapping relatedProperty="cargoOperacional.dsCargo" modelProperty="dsCargo" />
		</adsm:combobox>
		<adsm:hidden property="cargoOperacional.dsCargo" />

		<adsm:lookup property="empresa" dataType="text"
					 idProperty="idEmpresa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.municipios.empresaService.findLookup"
					 action="/municipios/manterEmpresas" 
					 label="empresa" size="18" maxLength="20" labelWidth="15%" width="85%"
					 serializable="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="empresa.pessoa.nmPessoa" disable="false" />
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>	

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarTerceiro" service="lms.carregamento.manterEquipesAction.saveIntegranteEquipeTerceiro" />
			<adsm:newButton caption="limpar"/>			
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 	property="equipe"
			   	idProperty="idIntegranteEquipe" detailFrameName="terceiros"
			   	autoSearch="false" rows="9" defaultOrder="idIntegranteEquipe:asc"
				selectionMode="check" showPagging="true" showGotoBox="true"
				service="lms.carregamento.manterEquipesAction.findPaginatedIntegranteEquipeTerceiro"
				rowCountService="lms.carregamento.manterEquipesAction.getRowCountIntegranteEquipeTerceiro"
				unique="true" >
		<adsm:gridColumn title="identificacao"	property="pessoa.nrIdentificacaoFormatado" width="18%" align="right" />
		<adsm:gridColumn title="nome" 			property="pessoa.nmPessoa" width="32%" />
		<adsm:gridColumn title="cargo" 			property="cargoOperacional.dsCargo" width="25%" />
		<adsm:gridColumn title="empresa" 		property="empresa.pessoa.nmPessoa" width="25%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirTerceiro" service="lms.carregamento.manterEquipesAction.removeByIdsIntegranteEquipe" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	
	/**
	 * callback do form 
	 *
	 * @param data
	 * @param error
	 */
	function loadForm_cb(data, error) {
		onDataLoad_cb(data, error);
		setElementValue("prestadorServico.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
		setElementValue("prestadorServico.idPrestadorServico", data.pessoa.idPessoa);		
		setElementValue("prestadorServico.pessoa.nmPessoa", data.pessoa.nmPessoa);		
		if (data.empresa){
			setElementValue("empresa.pessoa.nrIdentificacao", data.empresa.pessoa.nrIdentificacaoFormatado);
			setElementValue("empresa.idEmpresa", data.empresa.idEmpresa);		
			setElementValue("empresa.pessoa.nmPessoa", data.empresa.pessoa.nmPessoa);		
		}
	}

</script>