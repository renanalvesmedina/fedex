<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.atendimentoFilialService" >
	<adsm:form action="/municipios/manterHorariosAtendimentoFilial" idProperty="idAtendimentoFilial" >
		<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" property="filial" idProperty="idFilial"
				criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				action="/municipios/manterFiliais" labelWidth="17%" width="8%" exactMatch="true" >
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
	    </adsm:lookup>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30"
				maxLength="50" disabled="true" width="75%" serializable="false" />	
		
		<adsm:combobox label="tipoFuncionamento" property="tpAtendimento" domain="DM_TIPO_ATENDIMENTO" labelWidth="17%" width="83%" />

		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="atendimentoFilial" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="atendimentoFilial" idProperty="idAtendimentoFilial" rows="11" unique="true"
			defaultOrder="filial_.sgFilial,tpAtendimento,dtVigenciaInicial,hrAtendimentoInicial" >
		<adsm:gridColumn title="filial" property="filial.sgFilial" width="5%" />
		<adsm:gridColumn title="tipoFuncionamento" property="tpAtendimento" isDomain="true" />

		<adsm:gridColumn title="dom" property="blDomingo" width="4%" renderMode="image-check" />
		<adsm:gridColumn title="seg" property="blSegunda" width="4%" renderMode="image-check" />
		<adsm:gridColumn title="ter" property="blTerca" width="4%" renderMode="image-check" />
		<adsm:gridColumn title="qua" property="blQuarta" width="4%" renderMode="image-check" />
		<adsm:gridColumn title="qui" property="blQuinta" width="4%" renderMode="image-check" />
		<adsm:gridColumn title="sex" property="blSexta" width="4%" renderMode="image-check" />
		<adsm:gridColumn title="sab" property="blSabado" width="4%" renderMode="image-check" />

		<adsm:gridColumn title="horarioInicial" property="hrAtendimentoInicial" dataType="JTTime" width="10%" />
		<adsm:gridColumn title="horarioFinal" property="hrAtendimentoFinal" dataType="JTTime" width="10%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="10%" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="10%" />

		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>