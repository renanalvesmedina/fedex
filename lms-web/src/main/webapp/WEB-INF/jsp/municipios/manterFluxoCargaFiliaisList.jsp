<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.fluxoFilialService" >
	<adsm:form action="/municipios/manterFluxoCargaFiliais" idProperty="idFluxoFilial" >
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/> 
			<adsm:include key="filialOrigem"/>
			<adsm:include key="filialDestino"/>
			<adsm:include key="filialReembarcadora"/>
			<adsm:include key="filialParceira"/>
		</adsm:i18nLabels>
		<adsm:combobox
			label="servico"
			property="servico.idServico"
			optionProperty="idServico"
			optionLabelProperty="dsServico"
			service="lms.configuracoes.servicoService.find"
			labelWidth="17%"
			width="83%"
			boxWidth="231"
		/>

		<adsm:hidden property="tpEmpresa" value="M" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="false"/>
		<adsm:lookup
			label="filialOrigem"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilial"
			action="/municipios/manterFiliais"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%"
			exactMatch="true"
		>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia"
					size="30" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:lookup
			label="filialDestino"
			property="filialByIdFilialDestino"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilial"
			action="/municipios/manterFiliais"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%"
			exactMatch="true"
		>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia"
					size="30" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:lookup
			label="filialReembarcadora"
			property="filialByIdFilialReembarcadora"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilial"
			action="/municipios/manterFiliais"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%" width="33%"
			exactMatch="true"
		>
			<adsm:propertyMapping relatedProperty="filialByIdFilialReembarcadora.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filialByIdFilialReembarcadora.pessoa.nmFantasia"
					size="30" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:hidden property="tpEmpresaParceira" value="P" serializable="false"/>
		<adsm:lookup
			label="filialParceira"
			property="filialByIdFilialParceira"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFluxoCargaFiliaisAction.findLookupFilialParceira"
			action="/municipios/manterFiliais"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%"
			exactMatch="true"
		>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresaParceira" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialParceira.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialParceira.pessoa.nmFantasia"
					size="30" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="17%" width="83%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="fluxoFilial"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="fluxoFilial" idProperty="idFluxoFilial" rows="10" scrollBars="horizontal" gridHeight="200">
		<adsm:gridColumn title="servico" property="servico.dsServico" width="150"/>

		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialOrigem" property="filialByIdFilialOrigem.sgFilial" width="90"/>
			<adsm:gridColumn title="" property="filialByIdFilialOrigem.pessoa.nmFantasia" width="90"/>
		</adsm:gridColumnGroup> 

		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialDestino" property="filialByIdFilialDestino.sgFilial" width="90"/>
			<adsm:gridColumn title="" property="filialByIdFilialDestino.pessoa.nmFantasia" width="90"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialReembarcadora" property="filialByIdFilialReembarcadora.sgFilial" width="90"/>
			<adsm:gridColumn title="" property="filialByIdFilialReembarcadora.pessoa.nmFantasia" width="90"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialParceira" property="filialByIdFilialParceira.sgFilial" width="90"/>
			<adsm:gridColumn title="" property="filialByIdFilialParceira.pessoa.nmFantasia" width="90"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="distancia" property="nrDistancia" width="100" unit="km2" dataType="integer"/>
		<adsm:gridColumn title="tempo" property="nrPrazoView" width="70" unit="h" dataType="integer"/>

		<adsm:gridColumn title="dom" property="blDomingo" width="30" renderMode="image-check"/>
		<adsm:gridColumn title="seg" property="blSegunda" width="30" renderMode="image-check"/>
		<adsm:gridColumn title="ter" property="blTerca" width="30" renderMode="image-check"/>
		<adsm:gridColumn title="qua" property="blQuarta" width="30" renderMode="image-check"/>
		<adsm:gridColumn title="qui" property="blQuinta" width="30" renderMode="image-check"/>
		<adsm:gridColumn title="sex" property="blSexta" width="30" renderMode="image-check"/>
		<adsm:gridColumn title="sab" property="blSabado" width="30" renderMode="image-check"/>

		<adsm:gridColumn title="grauDificuldade" property="nrGrauDificuldade" width="150" unit="km2" dataType="integer"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="80" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("filialByIdFilialOrigem.idFilial") != ""
				|| getElementValue("filialByIdFilialDestino.idFilial") != ""
				|| getElementValue("filialByIdFilialReembarcadora.idFilial") != ""
				|| getElementValue("filialByIdFilialParceira.idFilial") != ""
			) {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013") 
						+ i18NLabel.getLabel("filialOrigem") + ', ' 
						+ i18NLabel.getLabel("filialDestino") + ', ' 
						+ i18NLabel.getLabel("filialReembarcadora") + ', '
						+ i18NLabel.getLabel("filialParceira") + '.');
			}
		}
		return false;
	}
</script>
