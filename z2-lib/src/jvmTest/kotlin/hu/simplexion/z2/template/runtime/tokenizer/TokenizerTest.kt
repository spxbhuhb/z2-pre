package hu.simplexion.z2.template.runtime.tokenizer

import kotlin.test.Test
import kotlin.test.assertEquals

class TokenizerTest {

    @Test
    fun textOnly() {
        val tokenizer = Tokenizer("hello \t\n world")
        assertEquals(
            listOf(
                Token(TokenType.Text, 0, 5, "hello"),
                Token(TokenType.Whitespace, 5, 9, " \t\n "),
                Token(TokenType.Text, 9, 14, "world")
            ),
            tokenizer.tokens
        )
    }

    @Test
    fun braceOpenMiddle() {
        val tokenizer = Tokenizer("hello { world")
        assertEquals(
            listOf(
                Token(TokenType.Text, 0, 5, "hello"),
                Token(TokenType.Whitespace, 5, 6, " "),
                Token(TokenType.BraceOpen, 6, 7, "{"),
                Token(TokenType.Whitespace, 7, 8, " "),
                Token(TokenType.Text, 8, 13, "world"),
            ),
            tokenizer.tokens
        )
    }

    //@Test
    fun braceRealistic() {
        val tokenizer = Tokenizer("hello val(param){world}")
        assertEquals(
            listOf(
                Token(TokenType.Text, 0, 6, "hello "),
                Token(TokenType.Whitespace, 6, 7, " "),
                Token(TokenType.Text, 7, 10, "val"),
                Token(TokenType.ParenthesesOpen, 10, 11, "("),
                Token(TokenType.Text, 11, 16, "param"),
                Token(TokenType.ParenthesesClose, 16, 17, ")"),
                Token(TokenType.BraceOpen, 17, 18, "{"),
                Token(TokenType.Text, 18, 23, "world"),
                Token(TokenType.BraceClose, 24, 25, "}")
            ),
            tokenizer.tokens
        )
    }
}