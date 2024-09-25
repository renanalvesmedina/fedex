<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.indicadorFinanceiroService">
	<adsm:form action="/configuracoes/manterIndicadoresFinanceiros" idProperty="idIndicadorFinanceiro">					 
	
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
					 size="30"/>			 					 
					 
		<adsm:textbox dataType="text" property="nmIndicadorFinanceiro" label="nome" size="30" maxLength="60"/>
		<adsm:textbox dataType="text" property="sgIndicadorFinanceiro" label="sigla" size="10" maxLength="10"/>
		
		<adsm:combobox property="tpIndicadorFinanceiro" label="tipo" domain="DM_TIPO_IND_FINANCEIRO"/>
		<adsm:combobox property="frequencia.idFrequencia" label="frequencia" 
					   service="lms.configuracoes.frequenciaService.find" 
					   optionLabelProperty="dsFrequencia" optionProperty="idFrequencia" boxWidth="190"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="indicadorFinanceiro"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid gridHeight="200" idProperty="idIndicadorFinanceiro" property="indicadorFinanceiro" rows="12" defaultOrder="pais_.nmPais,nmIndicadorFinanceiro">
		<adsm:gridColumn width="20%" title="pais" property="pais.nmPais"/>
		<adsm:gridColumn width="20%" title="nome" property="nmIndicadorFinanceiro" />
		<adsm:gridColumn width="10%" title="sigla" property="sgIndicadorFinanceiro"/>
		<adsm:gridColumn width="20%" title="tipo" property="tpIndicadorFinanceiro" isDomain="true"/>
		<adsm:gridColumn width="20%" title="frequencia" property="frequencia.dsFrequencia"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
