<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterMoedasAction">
	<adsm:form action="/configuracoes/manterMoedas" idProperty="idMoeda">

		<adsm:textbox
			label="descricao"
			property="dsMoeda"
			width="30%"
			labelWidth="20%"
			dataType="text"
			size="30"
			maxLength="60"
			required="true"
		/>
		<adsm:textbox
			label="sigla"
			width="30%"
			labelWidth="20%"
			dataType="text"
			property="sgMoeda"
			size="3"
			maxLength="3"
			required="true"
		/>

		<adsm:combobox
			label="situacao"
			property="tpSituacao"
			width="30%"
			labelWidth="20%"
			domain="DM_STATUS"
			required="true"
		/>
		<adsm:textbox
			label="simbolo"
			property="dsSimbolo"
			width="30%"
			labelWidth="20%"
			dataType="text"
			size="5"
			maxLength="5"
			required="true"
		/>

		<!-- campos usados para compor DS_VALOR_EXTENSO -->
		<adsm:textbox
			label="nmIntSing"
			property="nmInteiroSingular"
			onchange="trocaPontoVirgula(this);"
			width="30%"
			labelWidth="20%"
			required="true"
			dataType="text"
			size="20"
			maxLength="20"
		/>
		<adsm:textbox
			label="nmIntpl"
			property="nmInteiroPlural"
			onchange="trocaPontoVirgula(this);"
			width="30%"
			labelWidth="20%"
			required="true"
			dataType="text"
			size="20"
			maxLength="20"
		/>
		<adsm:textbox
			label="nmDecSing"
			property="nmDecimalSingular"
			onchange="trocaPontoVirgula(this);"
			width="30%"
			labelWidth="20%"
			required="true"
			dataType="text"
			size="20"
			maxLength="20"
		/>
		<adsm:textbox
			onchange="trocaPontoVirgula(this);"
			width="30%"
			labelWidth="20%"
			required="true"
			dataType="text"
			property="nmDecimalPlural"
			label="nmDecPl"
			size="20"
			maxLength="20"
		/>

		<adsm:textbox
			label="codigoIsoNumerico"
			width="30%"
			labelWidth="20%"
			dataType="integer"
			property="nrIsoCode"
			size="3"
			maxLength="3"
			required="true"
		/>

		<adsm:buttonBar>
			<adsm:button caption="paises" action="/configuracoes/manterMoedasPaises" cmd="main">
				<adsm:linkProperty src="idMoeda" target="moeda.idMoeda" />
				<adsm:linkProperty src="dsMoeda" target="moeda.dsMoeda" />
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	/**
	Function que troca ';' por ''
	*/
	function trocaPontoVirgula(obj){
		var i =0;
		while( i < obj.size ){
			obj.value = obj.value.replace(";","");
			i++;
		}
	}
</script>