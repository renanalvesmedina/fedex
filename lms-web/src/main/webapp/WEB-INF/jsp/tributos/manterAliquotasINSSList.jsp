<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.aliquotaInssPessoaFisicaService">
	<adsm:form action="/tributos/manterAliquotasINSS" >
	    <adsm:range label="vigenciaInicial" labelWidth="22%">
	   	    <adsm:textbox dataType="JTDate" property="dtInicioVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtInicioVigenciaFinal"/>
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="AliquotasInssPessoaFisica"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idAliquotaInssPessoaFisica" 
				property="AliquotasInssPessoaFisica" 
				selectionMode="check" 
				defaultOrder="dtInicioVigencia" 
				rows="13"	
				gridHeight="200">
		<adsm:gridColumn width="20%" title="vigenciaInicial" property="dtInicioVigencia" dataType="JTDate" />
		<adsm:gridColumn width="20%" title="percentualContribuicao" property="pcAliquota" dataType="decimal"/>
		<adsm:gridColumn width="20%" title="salarioBaseContribuicao" property="vlSalarioBase" dataType="currency"/>
		<adsm:gridColumn width="20%" title="valorMaximoRecolher" property="vlMaximoRecolhimento" dataType="currency"/>
		<adsm:gridColumn width="20%" title="percentualBaseCalcReduzida" property="pcBaseCalcReduzida" dataType="decimal"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>