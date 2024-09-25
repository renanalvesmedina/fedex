<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.indicadorFinanceiroService">
	<adsm:form action="/configuracoes/manterIndicadoresFinanceiros" idProperty="idIndicadorFinanceiro">
		
		<adsm:hidden property="tpSituacaoPais" value="A"/>
		<adsm:lookup service="lms.municipios.paisService.findLookup" 
					 action="/municipios/manterPaises"
					 property="pais" 
					 label="pais"
					 idProperty="idPais"
					 criteriaProperty="nmPais"					 
					 dataType="text" 
					 maxLength="60"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 size="30"
					 required="true">
					 
					 <adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoPais"/>
					 
		</adsm:lookup>	
		<adsm:textbox dataType="text" property="nmIndicadorFinanceiro" label="nome" size="30" maxLength="60" required="true"/>
		<adsm:textbox dataType="text" property="sgIndicadorFinanceiro" label="sigla" size="10" maxLength="10" required="true"/>
		
		<adsm:combobox property="tpIndicadorFinanceiro" label="tipo" domain="DM_TIPO_IND_FINANCEIRO" required="true"/>
		
		<adsm:combobox property="frequencia.idFrequencia" 
		               label="frequencia" 
					   service="lms.configuracoes.frequenciaService.findAtivas" 
					   optionLabelProperty="dsFrequencia" 
					   optionProperty="idFrequencia" 
					   required="true" 
					   boxWidth="190" 
					   autoLoad="true"/>
			
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true" onlyActiveValues="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="cotacaoIndicadoresFinanceiros" action="/configuracoes/manterCotacaoIndicadoresFinanceiros" cmd="main" boxWidth="240">
				<adsm:linkProperty src="pais.idPais" target="indicadorFinanceiro.pais.idPais"/>
				<adsm:linkProperty src="pais.nmPais" target="indicadorFinanceiro.pais.nmPais"/>
				<adsm:linkProperty src="idIndicadorFinanceiro" target="idIndicadorFinanceiroTmp"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>