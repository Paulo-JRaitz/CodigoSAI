let codigoAlgodoeira = '789868744';
let numeroPrensa = '1';
let quantidadeFardinho = 1000;
async function pad(str, length) {
  const resto = length - String(str).length;
  return '0'.repeat(resto > 0 ? resto : '0') + str;
}
async function GeraCodigoSAI(codigoAlgodoeira, numeroPrensa, numeroFardo) {
  let identificacaoTipoCodigo = '00';
  let identificacaoMatrizFilia = '0';
  let digitoVerificador = '';
  let codigosai = identificacaoTipoCodigo;
  codigosai += identificacaoMatrizFilia;
  codigosai += codigoAlgodoeira;
  codigosai += numeroPrensa;
  codigosai += numeroFardo;
  let acumulado = 0;
  for (var i = 0; i < 19; i++) {
    let numeralCodigoSAI = codigosai.substring(i, i + 1);
    let resto = i % 2;
    let multiplicador = resto == 0 ? 3 : 1;
    acumulado += numeralCodigoSAI * multiplicador;
  }
  let metaValidacao = acumulado % 10;
  digitoVerificador = metaValidacao != 0 ? 10 - metaValidacao : 0;
  codigosai += digitoVerificador.toString();
  console.log(codigosai);
}
for (var i = 0; i < quantidadeFardinho; i++) {
  let codigoFardino = 1 + i;
  codigoFardino = await pad(codigoFardino.toString(), 6);
  await GeraCodigoSAI(codigoAlgodoeira, numeroPrensa, codigoFardino);
}
