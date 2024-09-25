<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/carregamento/emitirEficienciaEmbarqueReembarque">
	
		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>	

		<adsm:lookup dataType="text" property="rota" label="rota" action="/municipios/consultarRotasViagem" service="" width="85%" />
		
		<adsm:lookup property="remetente.id" label="remetente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" width="85%" >
			<adsm:propertyMapping modelProperty="remetente.id" formProperty="nomeRemetente"/>
			<adsm:textbox dataType="text" property="nomeRemetente" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>		

		<adsm:buttonBar>
			<adsm:reportViewerButton service="carregamento/emitirEficienciaEmbarqueReembarque.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>