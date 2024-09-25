<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>

	<adsm:form action="/contasReceber/emitirDemonstrativoDescontosFretes">
	
		<adsm:hidden property="sgFilial"/>

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="75%"
					 labelWidth="25%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="20" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>


		<adsm:textbox label="numeroDemonstrativo" property="nrDemonstrativoDesconto" dataType="integer" size="10" maxLength="10" labelWidth="25%" width="75%" />

 		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="25%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>			
			<adsm:reportViewerButton service="lms.contasreceber.emitirDemonstrativoDescontosFretesAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>