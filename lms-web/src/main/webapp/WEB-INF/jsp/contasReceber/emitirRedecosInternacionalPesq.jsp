<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/contasReceber/emitirRedecosInternacional">
		<adsm:textbox label="redeco" dataType="text" property="nomeFilial" size="5" maxLength="5" width="8%"/>
		<adsm:lookup action="/contasReceber/manterFaturas"  service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" 
			size="20" maxLength="6" width="35%"/>
	</adsm:form>
	<adsm:buttonBar>
		<adsm:reportViewerButton reportName="contasReceber/emitirRedecosInternacional.jasper"/>
		<adsm:button caption="limpar"/>
	</adsm:buttonBar>

</adsm:window>
