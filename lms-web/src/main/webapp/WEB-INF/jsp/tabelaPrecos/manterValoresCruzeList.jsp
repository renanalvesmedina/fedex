<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.manterValoresCruzeAction">
	<adsm:form action="/tabelaPrecos/manterValoresCruze">
		

		<adsm:textbox dataType="JTDate" property="dataReferencia" label="dataReferencia"/>

		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridControle" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idValorCruze" property="gridControle" 
			   service="lms.tabelaprecos.manterValoresCruzeAction.findPaginated"
			   rowCountService="lms.tabelaprecos.manterValoresCruzeAction.getRowCount"
			   rows="14">
			   
		<adsm:gridColumn property="nrFaixaInicialPeso" title="faixaInicialPeso" dataType="decimal" mask="##,##0.000"/>
		<adsm:gridColumn property="nrFaixaFinalPeso" title="faixaFinalPeso" dataType="decimal" mask="##,##0.000"/>
		
		<adsm:gridColumnGroup customSeparator=" ">
              <adsm:gridColumn property="sgMoeda" dataType="text" title="valorCruze" width="30"/>
              <adsm:gridColumn property="dsSimbolo" dataType="text" title="" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" dataType="currency" property="vlCruze" width="80" align="right"/>
		
		<adsm:gridColumn property="dtVigenciaInicial" title="vigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="vigenciaFinal" dataType="JTDate"/>

		<adsm:buttonBar>
			<adsm:removeButton service="lms.tabelaprecos.manterValoresCruzeAction.removeByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

