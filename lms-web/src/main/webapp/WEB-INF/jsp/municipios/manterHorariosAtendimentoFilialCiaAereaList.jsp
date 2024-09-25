<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.atendimFilialCiaAereaService">
	<adsm:form action="/municipios/manterHorariosAtendimentoFilialCiaAerea" idProperty="idAtendimeFilialCiaAerea">
	
		<adsm:lookup 
		            service="lms.municipios.filialCiaAereaService.findLookup" 
		            dataType="text" 
		            property="filialCiaAerea" 
		            idProperty="idFilialCiaAerea"
					criteriaProperty="pessoa.nrIdentificacao" 
					relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					label="filialCiaAerea2" 
					size="20" maxLength="20" width="47%" 
					action="/municipios/manterFiliaisCiaAerea" labelWidth="13%">
	         <adsm:propertyMapping relatedProperty="filialCiaAerea.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="filialCiaAerea.pessoa.nmPessoa" size="30" maxLength="60" disabled="true" required="false" serializable="false"/>		
	    </adsm:lookup>
	    
			
		<adsm:combobox label="ciaAerea" labelWidth="13%" width="23%" property="filialCiaAerea.empresa.idEmpresa" service="lms.municipios.empresaService.findCiaAerea" 
	    			   optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa" onlyActiveValues="false" boxWidth="180" />
	    		 
		<adsm:lookup label="aeroporto" service="lms.municipios.aeroportoService.findLookup" action="municipios/manterAeroportos" 
			 	 	 dataType="text" property="filialCiaAerea.aeroporto" idProperty="idAeroporto" labelWidth="13%" width="8%" size="3" 
			 	 	 maxLength="3" criteriaProperty="sgAeroporto">
 	 	    <adsm:propertyMapping relatedProperty="filialCiaAerea.aeroporto.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
	 	 </adsm:lookup>
	 	 
	 	 <adsm:textbox property="filialCiaAerea.aeroporto.pessoa.nmPessoa" dataType="text" width="50%" size="47" maxLength="30" disabled="true"/>	 	    	      

		<adsm:range label="vigencia" labelWidth="13%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
 
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="atendimFilialCiaArea" />
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idAtendimFilialCiaAerea" property="atendimFilialCiaArea" 
				defaultOrder="filialCiaAerea_empresa_pessoa_.nmPessoa, filialCiaAerea_aeroporto_pessoa_.nmPessoa, filialCiaAerea_pessoa_.nmPessoa, dtVigenciaInicial, dtVigenciaFinal" 
				scrollBars="horizontal" rows="11" gridHeight="220" unique="true">
	    
	    <adsm:gridColumn title="identificacao" property="filialCiaAerea.pessoa.tpIdentificacao" width="60" isDomain="true" />
	    <adsm:gridColumn title="" property="filialCiaAerea.pessoa.nrIdentificacaoFormatado" width="100" align="right"/>
		<adsm:gridColumn title="filialCiaAerea2" property="filialCiaAerea.pessoa.nmPessoa" width="200" />
		<adsm:gridColumn title="ciaAerea" property="filialCiaAerea.empresa.pessoa.nmPessoa" width="200" />
		<adsm:gridColumn title="aeroporto" property="filialCiaAerea.aeroporto.siglaDescricao" width="200" />

		<adsm:gridColumn title="dom" property="blDomingo" renderMode="image-check" width="40"/>
		<adsm:gridColumn title="seg" property="blSegunda" renderMode="image-check" width="40"/>
		<adsm:gridColumn title="ter" property="blTerca"   renderMode="image-check" width="40"/> 
		<adsm:gridColumn title="qua" property="blQuarta"  renderMode="image-check" width="40"/>
		<adsm:gridColumn title="qui" property="blQuinta"  renderMode="image-check" width="40"/>
		<adsm:gridColumn title="sex" property="blSexta"   renderMode="image-check" width="40"/>
		<adsm:gridColumn title="sab" property="blSabado"  renderMode="image-check" width="40"/>

		<adsm:gridColumn title="horarioInicial" property="hrAtendimentoInicial" dataType="JTTime" width="90" />
		<adsm:gridColumn title="horarioFinal" property="hrAtendimentoFinal" dataType="JTTime" width="80" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="80" />

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>