<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterLocalizacoesMercadoriaAction">
	<adsm:form action="/sim/manterLocalizacoesMercadoria" idProperty="idLocalizacaoMercadoria">
    	<adsm:textbox label="codigo" property="cdLocalizacaoMercadoria" dataType="integer" maxValue="999" maxLength="3" minValue="0" width="79%" labelWidth="21%" size="5" required="true"/>
		<adsm:textbox label="localizacaoDeMercadoria" property="dsLocalizacaoMercadoria" dataType="text" maxLength="60" width="34%" labelWidth="21%" size="40" required="true"/>
		<adsm:lookup dataType="text" property="faseProcesso" idProperty="idFaseProcesso" criteriaProperty="dsFase"
					 service="lms.sim.faseProcessoService.findLookup" action="/sim/manterFaseProcesso" required="true"
					 label="faseProcesso" maxLength="50" width="25%" size="20%" exactMatch="false" minLengthForAutoPopUpSearch="4"/>
		<adsm:checkbox property="blArmazenagem" label="armazenagem" labelWidth="21%" width="9%" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="8%" width="62%" required="true" />

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
    </adsm:form>  
</adsm:window>