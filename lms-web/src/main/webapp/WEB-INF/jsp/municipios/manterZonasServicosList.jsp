<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.zonaServicoService" >
	<adsm:form idProperty="idZonaServico" action="/municipios/manterZonasServicos" >
		<adsm:hidden property="zona.tpSituacao"/>
		
		<adsm:combobox property="zona.idZona" label="zona" service="lms.municipios.zonaService.find" optionLabelProperty="dsZona" optionProperty="idZona" boxWidth="200"/>
		<adsm:combobox property="servico.idServico" label="servico" service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" boxWidth="200"/>
		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="zonaServico"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idZonaServico" rows="13" property="zonaServico" selectionMode="check" unique="true" defaultOrder="zona_.dsZona, servico_.dsServico, dtVigenciaInicial, dtVigenciaFinal">
		<adsm:gridColumn title="zona" property="zona.dsZona" width="20%" />
		<adsm:gridColumn title="situacao" property="zona.tpSituacao" isDomain="true" width="10%" />
		<adsm:gridColumn title="servico" property="servico.dsServico" width="30%" />
		<adsm:gridColumn title="situacao" property="servico.tpSituacao" isDomain="true" width="10%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="15%" dataType="JTDate"  />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="15%" dataType="JTDate"  />

		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>