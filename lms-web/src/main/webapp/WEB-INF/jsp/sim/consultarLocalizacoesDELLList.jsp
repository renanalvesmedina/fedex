<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacaoDetalhadaWeb" height="420">
		<adsm:combobox optionLabelProperty="" optionProperty="" property="TIPO" service="" label="tipoDocumento" prototypeValue="CTRC|CRT|NFS|MDA|RRE" labelWidth="19%" width="31%"/>

		<adsm:textbox label="documentoServico" labelWidth="19%" width="8%" size="6" maxLength="3" dataType="text" property="filialOrigem" />
        <adsm:textbox width="10%" size="10" dataType="text" property="numeroDocumento"/>
        <adsm:label key="hifen" width="2%" style="border:none;text-align:center"/>
        <adsm:lookup action="" dataType="text" property="numeroDocumentoCompl" service="" width="11%" size="5" style="width: 15px;"/>

		<adsm:combobox optionLabelProperty="" optionProperty="" property="EVENTO" service="" label="evento"  labelWidth="19%" width="81%"/>
		<adsm:combobox optionLabelProperty="" optionProperty="" prototypeValue="Rodoviário|Aéreo|Ferroviário|Marítimo" property="MODAL" service="" label="modal" labelWidth="19%" width="31%"/>
		<adsm:combobox optionLabelProperty="" prototypeValue="Nacional|Internacional" optionProperty="" property="ABRANGENCIA" service="" label="abrangencia" labelWidth="19%" width="31%"/>

		<adsm:range label="periodo" labelWidth="19%" width="81%" required="true">
			<adsm:textbox dataType="date" property="PERIODOI"/>
			<adsm:textbox dataType="date" property="PERIODOF"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   