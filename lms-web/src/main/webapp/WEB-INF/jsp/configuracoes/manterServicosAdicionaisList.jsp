<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.servicoAdicionalService">
	<adsm:form action="/configuracoes/manterServicosAdicionais" idProperty="idServicoAdicional">
		<adsm:textbox dataType="text" property="dsServicoAdicional" label="descricao" size="70" maxLength="60" width="85%"/>
        <adsm:combobox label="servicoOficial" property="servicoOficialTributo.idServicoOficialTributo"
        	service="lms.tributos.servicoOficialTributoService.find"
        	optionLabelProperty="nrServicoTributoDsServicoTributo"  optionProperty="idServicoOficialTributo"  
        	boxWidth="630" width="85%" />
		<adsm:textbox label="vigencia" width="85%" dataType="JTDate" property="dtVigencia"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicosAdicionais"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
    </adsm:form>
    <adsm:grid idProperty="idServicoAdicional" property="servicosAdicionais" defaultOrder="dsServicoAdicional,dtVigenciaInicial" selectionMode="check" gridHeight="200" rows="12">
	   <adsm:gridColumn title="descricao" property="dsServicoAdicional" dataType="text" width="70%"  />
	   <adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial"  dataType="JTDate" width="15%" />
       <adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="15%" />
       <adsm:buttonBar> 
	     <adsm:removeButton/>
       </adsm:buttonBar>
    </adsm:grid>
</adsm:window>
