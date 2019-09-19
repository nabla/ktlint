package nablarules

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType.CALL_EXPRESSION
import com.pinterest.ktlint.core.ast.ElementType.DOT_QUALIFIED_EXPRESSION
import com.pinterest.ktlint.core.ast.ElementType.VALUE_ARGUMENT
import com.pinterest.ktlint.core.ast.children
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

class NoErrorWithoutStackTraceRule : Rule("no-error-without-stack-trace") {

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == DOT_QUALIFIED_EXPRESSION) {
            val callExpression = node.lastChildNode

            if (callExpression.elementType == CALL_EXPRESSION) {
                val referenceExpression = callExpression.firstChildNode
                val identifier = referenceExpression.firstChildNode

                if (identifier.text == "error") {
                    val valueArgumentList = callExpression.lastChildNode
                    val valueArguments = valueArgumentList.children().filter {
                        it.elementType == VALUE_ARGUMENT
                    }.toList()

                    if (valueArguments.size == 1) {
                        emit(node.startOffset, "logger.error with only one argument", false)
                    }
                }
            }
        }
    }
}
