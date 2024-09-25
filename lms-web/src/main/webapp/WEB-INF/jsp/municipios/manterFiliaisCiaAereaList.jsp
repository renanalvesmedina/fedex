<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	function pageLoadCustom_cb(data,error) {
		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'list'});
		onPageLoad_cb(data,error);
	}
	
	function pageLoadFilialCiaAerea() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.nrIdentificacao")});
	}
</script>
<adsm:window title="consultarFiliaisCiaAerea" service="lms.municipios.filialCiaAereaService" onPageLoadCallBack="pageLoadCustom" onPageLoad="pageLoadFilialCiaAerea">
	<adsm:form action="/municipios/manterFiliaisCiaAerea" idProperty="idFilialCiaAerea">
		<adsm:hidden property="pessoa.tpPessoa" value="J" />	    
	    <adsm:combobox label="ciaAerea" property="empresa.idEmpresa" service="lms.municipios.empresaService.findCiaAerea" 
	    			   optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa" labelWidth="17%" width="33%" onlyActiveValues="false" boxWidth="200" />
	    			   
		<adsm:lookup label="aeroporto" service="lms.municipios.aeroportoService.findLookup" action="municipios/manterAeroportos" 
			 	 	 dataType="text" property="aeroporto" idProperty="idAeroporto" labelWidth="12%" width="8%" size="3" 
			 	 	 maxLength="3" criteriaProperty="sgAeroporto">
 	 	    <adsm:propertyMapping relatedProperty="aeroporto.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox property="aeroporto.pessoa.nmPessoa" dataType="text" width="15%" size="30" maxLength="30" disabled="true"/>	 	    	      
	 	 </adsm:lookup>
		
		<adsm:complement label="identificacao" labelWidth="17%" width="33%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
	    <adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" maxLength="60" size="30" labelWidth="12%" width="33%" />	
		
		<adsm:textbox label="codigoFornecedor" dataType="integer" property="cdFornecedor" size="15" maxLength="10"  labelWidth="17%" width="33%"/>

        <adsm:combobox property="blTaxaTerrestre" label="taxaTerrestre" domain="DM_SIM_NAO" labelWidth="12%" width="33%"/>
		
		<adsm:range label="vigencia" labelWidth="17%" width="33%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
        <adsm:hidden property="empresa.tpSituacao" />
        
        
        <adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="filialCiaAerea"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

    <adsm:grid property="filialCiaAerea" idProperty="idFilialCiaAerea" scrollBars="horizontal" selectionMode="check" gridHeight="200" unique="true" defaultOrder="empresa_pessoa_.nmPessoa, aeroporto_.sgAeroporto, aeroporto_pessoa_.nmPessoa, pessoa_.nmPessoa">
		<adsm:gridColumn title="ciaAerea"      property="empresa.pessoa.nmPessoa"  width="180"/>
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="68" align="left" />
		<adsm:gridColumn title=""              property="pessoa.nrIdentificacaoFormatado" width="100" align="right" />
		<adsm:gridColumn title="razaoSocial"   property="pessoa.nmPessoa" width="180" />
		<adsm:gridColumn title="aeroporto"     property="aeroporto.siglaDescricao"   width="250" />		
		<adsm:gridColumn title="vigenciaInicial"property="dtVigenciaInicial" dataType="JTDate" width="100"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal"    dataType="JTDate" width="86"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>