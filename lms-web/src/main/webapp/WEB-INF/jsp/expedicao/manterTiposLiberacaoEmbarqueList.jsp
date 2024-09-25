<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.tipoLiberacaoEmbarqueService">
	<adsm:form action="/expedicao/manterTiposLiberacaoEmbarque">
		<adsm:textbox label="liberacaoEmbarque2" property="dsTipoLiberacaoEmbarque" dataType="text" maxLength="60" size="40" labelWidth="17%" width="53%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="10%" width="20%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoLiberacaoEmbarque"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="tipoLiberacaoEmbarque" idProperty="idTipoLiberacaoEmbarque" defaultOrder="dsTipoLiberacaoEmbarque" gridHeight="200" unique="true" rows="14">
		<adsm:gridColumn title="liberacaoEmbarque2" property="dsTipoLiberacaoEmbarque" width="90%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>