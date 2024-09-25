<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>
	<adsm:form action="/configuracoes/manterObservacoesICMS">
		<adsm:textbox dataType="text" property="ie" size="80" width="85%" label="ie" disabled="true"/>
         <adsm:range label="vigencia" labelWidth="29%" width="71%" >
	         <adsm:textbox dataType="JTDateTimeZone" property="dataVigenciaInicial" picker="true" />
	         <adsm:textbox dataType="JTDateTimeZone" property="dataVigenciaFinal" picker="true"/>
         </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true" >
		<adsm:gridColumn title="vigencia" property="dataVigencia" width="100%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
