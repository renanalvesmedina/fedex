<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.causaAcaoCorretivaService">
	<adsm:form action="/rnc/manterAcoesCorretivasCausasNaoConformidade" idProperty="idCausaAcaoCorretiva">
		<adsm:combobox property="causaNaoConformidade.idCausaNaoConformidade" label="causaNaoConformidade" service="lms.rnc.causaNaoConformidadeService.find" onlyActiveValues="true" optionProperty="idCausaNaoConformidade" optionLabelProperty="dsCausaNaoConformidade" labelWidth="20%" width="80%" required="true"/>
		<adsm:combobox property="acaoCorretiva.idAcaoCorretiva" label="acaoCorretiva" service="lms.rnc.acaoCorretivaService.findOrderByAcaoCorretiva" onlyActiveValues="true" optionProperty="idAcaoCorretiva" optionLabelProperty="dsAcaoCorretiva" labelWidth="20%" width="80%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>