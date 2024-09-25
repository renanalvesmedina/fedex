<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/carregamento/emitirEstoqueDispositivoUnitilizacao">
		
		<adsm:lookup property="filial.id" label="filial" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" >
			<adsm:propertyMapping modelProperty="filial.id" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>		

		<adsm:lookup label="empresaProprietaria" property="empresaProprietaria" action="/vendas/manterDadosIdentificacao" dataType="integer" service="" labelWidth="18%" width="82%" size="18" maxLength="18">
			<adsm:textbox property="dsEmpresaProprietaria" dataType="text"  disabled="true"/>
		</adsm:lookup> 

		<adsm:combobox property="tipoDispositivo" label="tipoDispositivo" labelWidth="18%" width="82%" optionLabelProperty="tipoServico" optionProperty="1" service=""/>

		<adsm:range label="periodoMesAno" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="monthYear" property="dataInicial" picker="true" />
			<adsm:textbox dataType="monthYear" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="carregamento/emitirEstoqueDispositivoUnitilizacao.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>