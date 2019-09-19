package nablarules

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType.DOT_QUALIFIED_EXPRESSION
import com.pinterest.ktlint.core.ast.ElementType.IMPORT_DIRECTIVE
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

class ForbiddenImports : Rule("forbidden-imports") {

    private val forbiddenImports = mapOf(
        "org.jetbrains.exposed.sql.transactions.transaction" to "ace.db.exposed.transaction"
    )

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == IMPORT_DIRECTIVE) {
            val dotQualifiedExpression = node.lastChildNode

            if (dotQualifiedExpression.elementType == DOT_QUALIFIED_EXPRESSION) {
                val text = dotQualifiedExpression.text

                if (forbiddenImports.containsKey(text)) {
                    emit(node.startOffset, "$text -> ${forbiddenImports[text]}", false)
                }
            }
        }
    }
}
