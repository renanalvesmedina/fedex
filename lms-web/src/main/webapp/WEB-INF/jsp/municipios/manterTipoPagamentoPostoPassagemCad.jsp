<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.tipoPagamPostoPassagemService">
	<adsm:form idProperty="idTipoPagamPostoPassagem" action="/municipios/manterTipoPagamentoPostoPassagem">
		<adsm:textbox dataType="text" required="true" property="dsTipoPagamPostoPassagem" label="descricao" maxLength="60" size="60" labelWidth="18%" width="82%"/>
		<adsm:checkbox property="blCartaoPedagio" label="cartaoPedagio" labelWidth="18%" width="82%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="32%" required="true" />
		<adsm:buttonBar>
		    <adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>