<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterRotasViagemAction" >
	<adsm:form action="/municipios/manterRotasViagem" idProperty="idRotaViagem" height="142" >

		<adsm:hidden property="tpEmpresaMercurioValue" value="M" serializable="false" />

		<adsm:combobox property="tpRota" label="tipoRota" domain="DM_TIPO_ROTA_VIAGEM" labelWidth="18%" width="32%" renderOptions="true"/>
		<adsm:combobox property="tpSistemaRota" label="sistemaRota" domain="DM_SISTEMA_ROTA" labelWidth="18%" width="32%" renderOptions="true"/>

		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupCliente" 
				label="cliente" size="17" maxLength="20" labelWidth="18%" width="82%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:combobox
			label="tipoMeioTransporte"
			property="tipoMeioTransporte.idTipoMeioTransporte"
			optionProperty="idTipoMeioTransporte"
			optionLabelProperty="dsTipoMeioTransporte"
			service="lms.municipios.manterRotasViagemAction.findTipoMeioTransporte"
			labelWidth="18%"
			width="82%"
			boxWidth="180"
			cellStyle="vertical-align:bottom;"
		>
		</adsm:combobox>

		<adsm:textbox dataType="integer" property="rotaIda.nrRota" label="numeroRotaIda"
				labelWidth="18%" width="32%" maxLength="4" size="5" mask="0000" />
		<adsm:textbox dataType="integer" property="rotaVolta.nrRota" label="numeroRotaVolta"
				labelWidth="18%" width="32%" maxLength="4" size="5" mask="0000" />

		<adsm:listbox property="rotaIda.filiaisRota" 
				optionProperty="sgFilial" optionLabelProperty="dsRota"
				label="rotaIda" size="4" labelWidth="18%" width="32%" 
				showOrderControls="false" boxWidth="180" showIndex="false" serializable="true" allowMultiple="true">
			<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					dataType="text" size="3" maxLength="3" 
					service="lms.municipios.manterRotasViagemAction.findFilialLookup" action="/municipios/manterFiliais"
					exactMatch="false" minLengthForAutoPopUpSearch="3">
				<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
				<adsm:propertyMapping relatedProperty="rotaIda.filiaisRota_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>   
		</adsm:listbox>

		<adsm:listbox property="rotaVolta.filiaisRota" 
				optionProperty="sgFilial" optionLabelProperty="dsRota"
				label="rotaVolta" size="4" labelWidth="18%" width="32%" 
				showOrderControls="false" boxWidth="180" showIndex="false" serializable="true" allowMultiple="true">
			<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					dataType="text" size="3" maxLength="3" 
					service="lms.municipios.manterRotasViagemAction.findFilialLookup" action="/municipios/manterFiliais"
					exactMatch="false" minLengthForAutoPopUpSearch="3">
				<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
				<adsm:propertyMapping relatedProperty="rotaVolta.filiaisRota_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>   
		</adsm:listbox>

		<adsm:textbox dataType="JTTime" label="horarioSaidaRotaIda" property="hrSaidaIda"
				size="4" cellStyle="verticalAlignment:bottom" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="JTTime" label="horarioSaidaRotaVolta" property="hrSaidaVolta"
				size="4" cellStyle="verticalAlignment:bottom" labelWidth="18%" width="32%" />

		<adsm:range label="vigencia" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>  

		<adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" defaultValue="S" labelWidth="18%" width="32%" renderOptions="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaViagem" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form> 
	<adsm:grid idProperty="idRotaViagem" property="rotaViagem" unique="true" scrollBars="horizontal" rows="7"
			service="lms.municipios.manterRotasViagemAction.findPaginatedRotaViagem" gridHeight="140"
			rowCountService="lms.municipios.manterRotasViagemAction.getRowCountRotaViagem" > 
		<adsm:gridColumn title="tipoRota" property="tpRota"  width="90"/>
		<adsm:gridColumn title="sistemaRota" property="tpSistemaRota"  width="110"/>
		<adsm:gridColumn title="tipoMeioTransporte" property="dsTipoMeioTransporte" width="150"/>
		<adsm:gridColumn title="rotaIda" property="nrRotaIda" dataType="integer" mask="0000" width="30" />
		<adsm:gridColumn title="" property="dsRotaIda" width="180" />
		<adsm:gridColumn title="rotaVolta" property="nrRotaVolta" dataType="integer" mask="0000" width="30" />
		<adsm:gridColumn title="" property="dsRotaVolta" width="180" />
		<adsm:gridColumn title="horarioSaidaRotaIda" property="hrSaidaIda" dataType="JTTime" width="150"/>
		<adsm:gridColumn title="horarioSaidaRotaVolta" property="hrSaidaVolta" dataType="JTTime" width="165"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="90" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript">
	document.getElementById("tpEmpresaMercurioValue").masterLink = true;

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			setDisabledTabsFilhas(true);
		}
	}

	function setDisabledTabsFilhas(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("ida", disabled);
		tabGroup.setDisabledTab("volta", disabled);
		//tabGroup.setDisabledTab("rota", disabled);
	}
</script>