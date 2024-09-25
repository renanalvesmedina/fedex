<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/entrega/emitirDocumentosSemRegistroRecebimento">
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="filial" size="6" maxLength="50" labelWidth="30%" width="11%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="nomeFilial" size="18" maxLength="50" disabled="true" width="59%" />
		<adsm:range label="periodoFechamentoManifesto" labelWidth="30%" width="70%" required="true" >
			<adsm:textbox dataType="date" property="periodoInicial"/> 
			<adsm:textbox dataType="date" property="periodoFinal"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/entrega/emitirDocumentosSemRegistroRecebimento.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>