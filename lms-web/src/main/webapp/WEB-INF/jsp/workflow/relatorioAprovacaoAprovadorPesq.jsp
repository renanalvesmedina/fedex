<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/workflow/relatoriaAprovacaoAprovador">
		<adsm:lookup action="/workflow/manterFuncionario" dataType="text" service="" property="funcionario" label="funcionario"/>
		<adsm:combobox property="status" prototypeValue="Aprovado|Cancelado" optionLabelProperty="Status" optionProperty="0" service="" label="status"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="workflow/relatorioAprovacaoAprovadorPesq.rpt"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>