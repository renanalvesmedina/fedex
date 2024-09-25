<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.tipoLiberacaoEmbarqueService">
	<adsm:form action="/expedicao/manterTiposLiberacaoEmbarque" idProperty="idTipoLiberacaoEmbarque">
		<adsm:textbox label="liberacaoEmbarque2" property="dsTipoLiberacaoEmbarque" dataType="text" required="true" maxLength="60" size="40" labelWidth="17%" width="53%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" labelWidth="10%" width="20%"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>