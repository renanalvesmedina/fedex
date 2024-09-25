<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.causaNaoConformidadeService" >
	<adsm:form action="/rnc/manterCausasNaoConformidade" idProperty="idCausaNaoConformidade" >
		<adsm:textbox dataType="text" property="dsCausaNaoConformidade" label="descricao" size="60" maxLength="50" width="85%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:button caption="acoesCorretivas" action="/rnc/manterAcoesCorretivasCausasNaoConformidade" cmd="main">
				<adsm:linkProperty src="idCausaNaoConformidade, dsCausaNaoConformidade" target="causaNaoConformidade.idCausaNaoConformidade" disabled="true" />
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>