<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/coleta/emitirRelatorioEstatisticoColetasTipoServico">
		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>
		<adsm:combobox property="modal" label="modal" width="85%" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="Aéreo|Rodoviário" />
		<adsm:combobox property="abrangencia" label="abrangencia" width="85%" optionLabelProperty="label" optionProperty="1" service="" prototypeValue="Nacional|Internacional" />
		<adsm:lookup dataType="text" property="tipoServico" label="tipoServico" action="/configuracoes/manterTiposServicos" cmd="list" service="" width="85%" size="50" />
		<adsm:buttonBar>
			<adsm:reportViewerButton service="coleta/emitirRelatorioEstatisticoColetasTipoServico.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>