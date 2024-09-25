<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarServicosFilial" service="lms.municipios.filialServicoService">
	   <adsm:form action="/municipios/manterServicosFilial" idProperty="idFilialServico">

		   <adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" property="filial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" width="9%" action="/municipios/manterFiliais" idProperty="idFilial" style="width:45px">
	                <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
	        </adsm:lookup>
	        <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" width="26%"/>
	        
		   <adsm:combobox property="servico.idServico" label="servico"  service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" boxWidth="200"/>
		   
	       <adsm:range label="vigencia" width="45%">
	             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
	             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
	        </adsm:range>
	        
			<adsm:buttonBar freeLayout="true">
				<adsm:findButton callbackProperty="filialServico"/>
				<adsm:resetButton/>
			</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idFilialServico" property="filialServico" selectionMode="check" gridHeight="200" unique="true" rows="13" defaultOrder="filial_.sgFilial,servico_.dsServico,dtVigenciaInicial">
	    <adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="filial.sgFilial" width="150"/>
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="100"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="servico" property="servico.dsServico" width="250"/>	
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="100" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="100" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
