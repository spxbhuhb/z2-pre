package hu.simplexion.z2.template.runtime.tokenizer

data class Token(
    val type : TokenType,
    val startOffset : Int,
    val endOffset : Int,
    val text : String
)