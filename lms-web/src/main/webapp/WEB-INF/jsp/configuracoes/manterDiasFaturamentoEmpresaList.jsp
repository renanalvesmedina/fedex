<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.diaFaturamentoEmpresaService">
	<adsm:form action="/configuracoes/manterDiasFaturamentoEmpresa" idProperty="idDiaFaturamentoEmpresa" >
        <adsm:combobox property="tpPeriodicidade" domain="DM_PERIODICIDADE_FATURAMENTO" label="periodicidade"  />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" width="35%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="diasFaturamentoEmpresa"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDiaFaturamentoEmpresa" property="diasFaturamentoEmpresa" selectionMode="check" gridHeight="200" >
        <adsm:gridColumn width="60%" title="periodicidade" property="tpPeriodicidade" isDomain="true"/>
		<adsm:gridColumn width="20%" title="diaFaturamento" property="ddCorteExt" />
		<adsm:gridColumn width="20%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>