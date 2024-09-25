<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.centralizadoraFaturamentoService">
	<adsm:form action="/configuracoes/manterCentralizadoraFaturamento">
	 	<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" 
	 		property="filialByIdFilialCentralizadora" 
	 		criteriaProperty="sgFilial"
	 		idProperty="idFilial"
	 		label="filialCentralizadora" 
	 		size="3" maxLength="3" 
	 		action="/municipios/manterFiliais" labelWidth="15%" width="8%">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="nomeFilialCentralizadora"/>
			<adsm:textbox dataType="text" size="30" maxLength="30" property="nomeFilialCentralizadora" disabled="true" width="27%" serializable="false"/>
		</adsm:lookup>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" labelWidth="15%"/>
	 	<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" 
	 		property="filialByIdFilialCentralizada" 
	 		criteriaProperty="sgFilial"
	 		idProperty="idFilial"
	 		label="filialCentralizada" 
	 		size="3" maxLength="3" 
	 		action="/municipios/manterFiliais" labelWidth="15%" width="8%">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="nomeFilialCentralizada"/>
			<adsm:textbox dataType="text" size="30" maxLength="30" property="nomeFilialCentralizada" disabled="true" width="27%" serializable="false"/>
		</adsm:lookup>			
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="15%"/>	
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="centralizadoraFaturamento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
	</adsm:form>
	<adsm:grid idProperty="idCentralizadoraFaturamento" property="centralizadoraFaturamento" defaultOrder="filialByIdFilialCentralizadora_pessoa_.nmPessoa, filialByIdFilialCentralizada_pessoa_.nmPessoa, tpModal, tpAbrangencia" rows="13" gridHeight="200">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="50" title="filialCentralizadora" property="filialByIdFilialCentralizadora.sgFilial"/>
			<adsm:gridColumn width="120" title="" property="filialByIdFilialCentralizadora.pessoa.nmFantasia"/>	
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="50" title="filialCentralizada" property="filialByIdFilialCentralizada.sgFilial"/>
			<adsm:gridColumn width="120" title="" property="filialByIdFilialCentralizada.pessoa.nmFantasia"/>	
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>	
	</adsm:grid>
</adsm:window>