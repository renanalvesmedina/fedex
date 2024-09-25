<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.causaAcaoCorretivaService">
	<adsm:form action="/rnc/manterAcoesCorretivasCausasNaoConformidade" idProperty="idCausaAcaoCorretiva" >
		<adsm:combobox property="causaNaoConformidade.idCausaNaoConformidade" label="causaNaoConformidade" service="lms.rnc.causaNaoConformidadeService.find" optionProperty="idCausaNaoConformidade" optionLabelProperty="dsCausaNaoConformidade" labelWidth="20%" width="80%"/>
		<adsm:combobox property="acaoCorretiva.idAcaoCorretiva" label="acaoCorretiva" service="lms.rnc.acaoCorretivaService.findOrderByAcaoCorretiva"  optionProperty="idAcaoCorretiva" optionLabelProperty="dsAcaoCorretiva" labelWidth="20%" width="80%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="causasAcoesCorretivas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="causasAcoesCorretivas" idProperty="idCausaAcaoCorretiva" defaultOrder="acaoCorretiva_.dsAcaoCorretiva:asc" selectionMode="check" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn property="causaNaoConformidade.dsCausaNaoConformidade" title="causaNaoConformidade" width="50%" />
		<adsm:gridColumn property="acaoCorretiva.dsAcaoCorretiva" title="acaoCorretiva" width="50%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
