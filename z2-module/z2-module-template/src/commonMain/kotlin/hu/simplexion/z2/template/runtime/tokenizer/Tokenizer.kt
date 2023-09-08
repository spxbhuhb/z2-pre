package hu.simplexion.z2.template.runtime.tokenizer

class Tokenizer(
    input: String
) {
    companion object {
        const val BRACE_OPEN = '{'
        const val BRACE_CLOSE = '}'
        const val PARENTHESIS_OPEN = '('
        const val PARENTHESIS_CLOSE = ')'
        const val BACKSLASH = '\\'
        const val APOSTROPHE = '\''
    }

    val tokens = mutableListOf<Token>()

    val characters = input.toCharArray()

    var characterPosition = 0
    val characterEnd = characters.size

    var buffer = CharArray(1000)
    var bufferPosition = 0
    val bufferEnd = buffer.size

    init {
        tokenize()
    }

    fun tokenize() {
        while (characterPosition < characterEnd) {

            val char = characters[characterPosition]

            when {
                char == BRACE_OPEN -> token(TokenType.BraceOpen, "{")
                char == BRACE_CLOSE -> token(TokenType.BraceClose, "}")
                char == PARENTHESIS_OPEN -> token(TokenType.ParenthesesOpen, "(")
                char == PARENTHESIS_CLOSE -> token(TokenType.ParenthesesClose, ")")
                char == APOSTROPHE -> apostrophe()
                char == BACKSLASH -> escape()
                char.isWhitespace() -> whitespace(char)
                else -> append(char)
            }
        }
        emptyBuffer()
    }

    fun ahead(offset: Int): Char? =
        if (characterPosition + offset < characterEnd) characters[characterPosition + offset] else null

    fun append(char: Char) {
        characterPosition ++
        buffer[bufferPosition ++] = char
    }

    fun emptyBuffer(type : TokenType = TokenType.Text) {
        if (bufferPosition > 0) {
            tokens += Token(
                type,
                characterPosition - bufferPosition,
                characterPosition,
                buffer.concatToString(0, bufferPosition)
            )
            bufferPosition = 0
        }
    }

    fun whitespace(char : Char) {
        emptyBuffer()
        append(char)
        while (ahead(0)?.isWhitespace() == true) {
            append(characters[characterPosition])
        }
        emptyBuffer(TokenType.Whitespace)
    }

    fun token(tokenType: TokenType, text: String) {
        emptyBuffer()
        tokens += Token(tokenType, characterPosition, characterPosition + 1, text)
        characterPosition ++
    }

    fun apostrophe() {
        if (ahead(1) != APOSTROPHE || ahead(2) != APOSTROPHE) {
            append(APOSTROPHE)
        } else {
            emptyBuffer()
            tokens += Token(TokenType.TripleSingleQuote, characterPosition, characterPosition + 3, "'''")
            characterPosition += 3
        }
    }

    fun escape() {
        val char = ahead(1) ?: BACKSLASH

        when (char) {
            BRACE_OPEN,
            BRACE_CLOSE,
            PARENTHESIS_OPEN,
            PARENTHESIS_CLOSE,
            BACKSLASH,
            APOSTROPHE -> {
                characterPosition ++ // step over the escaping backslash
                append(char)
            }

            else -> append(BACKSLASH)
        }
    }

}