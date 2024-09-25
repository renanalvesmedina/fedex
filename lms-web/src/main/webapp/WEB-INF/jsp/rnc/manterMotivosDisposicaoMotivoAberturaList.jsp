<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.manterMotivosDisposicaoMotivoAberturaAction">
	<adsm:form action="/rnc/manterMotivosDisposicaoMotivoAbertura" idProperty="idMotAberturaMotDisposicao">
		<adsm:combobox property="motivoAberturaNc.idMotivoAberturaNc" label="motivoAberturaNaoConformidade" service="lms.rnc.motivoAberturaNcService.findOrderByDsMotivoAbertura"  optionLabelProperty="dsMotivoAbertura" optionProperty="idMotivoAberturaNc" labelWidth="32%" width="68%" />
		<adsm:combobox property="motivoDisposicao.idMotivoDisposicao" label="motivoDisposicao" service="lms.rnc.motivoDisposicaoService.findOrderByDsMotivo"  optionLabelProperty="dsMotivo" optionProperty="idMotivoDisposicao" labelWidth="32%" width="68%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivosAberturaMotivosDisposicao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motivosAberturaMotivosDisposicao" idProperty="idMotAberturaMotDisposicao" defaultOrder="motivoAberturaNc_.dsMotivoAbertura:asc,motivoDisposicao_.dsMotivo:asc" gridHeight="200" unique="true" rows="13"
			service="lms.rnc.manterMotivosDisposicaoMotivoAberturaAction.findPaginatedMotDispMotAbert" rowCountService="lms.rnc.manterMotivosDisposicaoMotivoAberturaAction.getRowCount">
		<adsm:gridColumn property="motivoAberturaNc.dsMotivoAbertura" title="motivoAberturaNaoConformidade" width="50%"/>
		<adsm:gridColumn property="motivoDisposicao.dsMotivo" title="motivoDisposicao" width="50%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
