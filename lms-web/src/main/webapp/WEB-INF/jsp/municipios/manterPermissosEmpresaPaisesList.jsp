<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarPermissosEmpresaPaises" service="lms.municipios.permissoEmpresaPaisService">
	<adsm:form action="/municipios/manterPermissosEmpresaPaises" idProperty="idPermissoEmpresaPais">
		
		<adsm:lookup service="lms.municipios.empresaService.findLookup" dataType="text" property="empresa" 
					idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" label="empresa" exactMatch="true"
					action="/municipios/manterEmpresas" size="20" labelWidth="20%" width="19%" minLengthForAutoPopUpSearch="5">
                  <adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" serializable="false" size="30" disabled="true" width="61%"/>

		<adsm:lookup service="lms.municipios.paisService.findLookup" dataType="text" property="paisByIdPaisOrigem" criteriaProperty="nmPais" idProperty="idPais" label="paisOrigemDestino" action="/municipios/manterPaises" size="30" maxLength="50" labelWidth="20%" width="30%" exactMatch="false" minLengthForAutoPopUpSearch="3"/>

		<adsm:lookup service="lms.municipios.paisService.findLookup" dataType="text" property="paisByIdPaisDestino" criteriaProperty="nmPais" idProperty="idPais" label="paisOrigemDestino" action="/municipios/manterPaises" size="30" maxLength="50" labelWidth="20%" width="30%" exactMatch="false" minLengthForAutoPopUpSearch="3"/>

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="permissoEmpresaPais"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idPermissoEmpresaPais"  property="permissoEmpresaPais" selectionMode="check" defaultOrder="empresa_pessoa_.nmPessoa,paisByIdPaisOrigem_.nmPais,paisByIdPaisDestino_.nmPais,dtVigenciaInicial" gridHeight="200" unique="true" rows="11">
		<adsm:gridColumn title="empresa" property="empresa.pessoa.nmPessoa" width="20%"/>
		<adsm:gridColumn title="paisOrigemDestino" property="paisByIdPaisOrigem.nmPais" width="15%"/>
		<adsm:gridColumn title="paisOrigemDestino" property="paisByIdPaisDestino.nmPais" width="15%"/>
		<adsm:gridColumn title="numeroPermisso" property="nrPermisso" width="13%" align="right"/>
		<adsm:gridColumn title="numeroPermissoMIC" property="nrPermissoMic" width="13%" align="right"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="12%" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="12%" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
		
	function permissos_pageLoad(){
		onPageLoad();
		estadoNovo();
	}

</script>