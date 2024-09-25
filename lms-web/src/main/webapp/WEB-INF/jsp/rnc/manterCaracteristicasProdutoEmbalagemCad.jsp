<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.caracteristicaProdutoService">
	<adsm:form action="/rnc/manterCaracteristicasProdutoEmbalagem" idProperty="idCaracteristicaProduto">
		<adsm:textbox dataType="text" property="dsCaracteristicaProduto" label="descricao" required="true"  maxLength="100" size="50" width="85%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" width="85%" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
