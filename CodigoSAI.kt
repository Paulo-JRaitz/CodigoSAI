class CodigoSAI private constructor(val codigo: String, val errorMessage: String? = null) : Comparable<CodigoSAI>, Cloneable {

  val barcodeType: String
      get() = if (isValid) codigo.substring(0, 2) else ""

  val digitoMatrizFilial: String
      get() = if (isValid) codigo.substring(2, 3) else ""

  val codigoAlgodoeira: String
      get() = if (isValid) codigo.substring(3, 12) else ""

  val numeroPrensa: String
      get() = if (isValid) codigo.substring(12, 13) else ""

  val numeroFardo: String
      get() = if (isValid) codigo.substring(13, 19) else ""

  val digitoVerificador: String
      get() = if (isValid) codigo.substring(19, 20) else ""

  val isValid: Boolean
      get() = errorMessage == null

  fun copy(numeroFardo: Int) = if (isValid) {
      if (numeroFardo < 0) CodigoSAI("", "O número do fardo não pode ser menor que zero.") else copy(numeroFardo.toString())
  } else EMPTY

  fun copy(numeroFardo: String): CodigoSAI {
      if (!isValid) return EMPTY

      val digits = numeroFardo.mapNotNull { if (it in '0'..'9') it else null }.joinToString("")

      val numero = when {
          digits.isEmpty() -> return CodigoSAI("", "O número do fardo é obrigatório.")
          digits.length <= 6 -> "%06d".format(digits.toInt())
          else -> return CodigoSAI("", "O número do fardo deve ter no máximo 6 dígitos (%s).".format(digits))
      }
      if (numero.toInt() == 0) {
          return CodigoSAI("", "O número do fardo não pode ser igual a zero.")
      }

      val codigoSAI = "%s%s%s%s%s".format(barcodeType, digitoMatrizFilial, codigoAlgodoeira, numeroPrensa, numero)
      val dv = checksum(codigoSAI)
      return fromString("%s%s".format(codigoSAI, dv))
  }

  public override fun clone() = fromString(codigo)

  override fun toString() = codigo

  override fun equals(other: Any?) = this.codigo == (other as? CodigoSAI)?.codigo && this.errorMessage == (other as? CodigoSAI)?.errorMessage

  override fun hashCode() = this.codigo.hashCode() + (errorMessage ?: "").hashCode()

  override fun compareTo(other: CodigoSAI) = numeroFardo.compareTo(other.numeroFardo)

  operator fun plus(other: Int) = if (this.isValid) copy(this.numeroFardo.toInt() + other) else throw Exception("Cannot plus a invalid CodigoSAI")

  operator fun minus(other: Int) = if (this.isValid) copy(this.numeroFardo.toInt() - other) else throw Exception("Cannot minuts a invalid CodigoSAI")

  companion object {

      val EMPTY = CodigoSAI("", "")

      fun fromString(codigo: String): CodigoSAI {
          val digits = StringBuilder(codigo.mapNotNull { if (it in '0'..'9') it else null }.joinToString(""))

          if (digits.length != 20) {
              return CodigoSAI("", "O código SAI deve conter 20 dígitos.")
          }
          if (! digits.startsWith("00")) {
              return CodigoSAI("", "O código SAI deve iniciar com \"00\" -> $digits")
          }
          val dv = checksum(codigo)
          if (dv != digits.last().toString()) {
              return CodigoSAI("", "Dígito verificador '%s' difere do calculado '%s' (%s)".format(digits.last().toString(), dv, codigo))
          }

          return CodigoSAI(digits.toString())
      }

      fun isValid(codigo: String) = fromString(codigo).isValid

      fun checksum(codigo: String): String? {
          val digits = codigo.mapNotNull { if (it in '0'..'9') it.toString().toInt() else null }

          if (digits.size < 19) return null

          val slice = digits.subList(0, 19)

          var multiplicador = 3
          val sum = slice.reduce { acc, i -> multiplicador = if (multiplicador == 3) 1 else 3; acc + (i * multiplicador) }

          val multiplo = if (sum % 10 == 0) sum else {
              {
                  var acc = 0 
                  while (true) {
                      if ((sum + ++acc) % 10 == 0) {
                          break
                      }
                  }
                  sum + acc
              }()
          }

          return (multiplo - sum).toString()
      }

  }

}

fun main() {
  val codigoAlgodoeira = "789854719"
  val codigoPrensa = "4"
  val numeroFardo = "100000"
  val dv = CodigoSAI.checksum("000${codigoAlgodoeira}${codigoPrensa}${numeroFardo}")
  val codigoSAI = CodigoSAI.fromString("000${codigoAlgodoeira}${codigoPrensa}${numeroFardo}${dv}")

  (11000..14000).forEach {
      println(codigoSAI + it)
  }

}