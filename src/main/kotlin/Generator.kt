import kotlin.random.Random

/*
 * Гуглим "kotlin grammar" и получаем следующую упрощенную картину синтаксиса о:
 * expression               ::= disjunction
 * disjunction              ::= conjunction ('||' conjunction)*
 * conjunction              ::= equality ('&&' equality)*
 * equality                 ::= comparison (equalityOperator comparison)*
 * comparison               ::= additiveExpression (comparisonOperator additiveExpression)*
 * additiveExpression       ::= multiplicativeExpression (additiveOperator multiplicativeExpression)*
 * multiplicativeExpression ::= primaryExpression (multiplicativeOperator primaryExpression)*
 * primaryExpression        ::= '(' expression ')' | variable | constant
 */


object Generator {
    private const val maxDepth = 10
    private var initialDepth: Int = 0

    fun generateArithmeticExpression(): StringBuilder {
        initialDepth = Thread.currentThread().stackTrace.size
        return generateExpression()
    }

    // expression ::= disjunction
    private fun generateExpression(): StringBuilder {
        val expression = StringBuilder()
        expression.append(generateDisjunction())
        return expression
    }

    // disjunction ::= conjunction ('||' conjunction)*
    private fun generateDisjunction(): StringBuilder {
        val conjunction = generateConjunction()

        while (decideToContinue(0.3)) {
            conjunction.append(" || ")
            conjunction.append(generateConjunction())
        }

        return conjunction
    }

    // conjunction ::= equality ('&&' equality)*
    private fun generateConjunction(): StringBuilder {
        val equality = generateEquality()

        while (decideToContinue(0.3)) {
            equality.append(" && ")
            equality.append(generateEquality())
        }

        return equality
    }

    // equality ::= comparison (equalityOperator comparison)*
    private fun generateEquality(): StringBuilder {
        val comparison = generateComparison()

        while (decideToContinue(0.3)) {
            comparison.append(listOf(" == ", " != ").random())
            comparison.append(generateComparison())
        }

        return comparison
    }

    // comparison ::= additiveExpression (comparisonOperator additiveExpression)*
    private fun generateComparison(): StringBuilder {
        val additiveExpression = generateAdditiveExpression()

        while (decideToContinue(0.3)) {
            additiveExpression.append(listOf(" < ", " > ", " <= ", " >= ").random())
            additiveExpression.append(generateAdditiveExpression())
        }

        return additiveExpression
    }

    // additiveExpression ::= multiplicativeExpression (additiveOperator multiplicativeExpression)*
    private fun generateAdditiveExpression(): StringBuilder {
        val multiplicativeExpression = generateMultiplicativeExpression()

        while (decideToContinue(0.3)) {
            multiplicativeExpression.append(listOf(" + ", " - ").random())
            multiplicativeExpression.append(generateMultiplicativeExpression())
        }

        return multiplicativeExpression
    }

    // multiplicativeExpression ::= primaryExpression (multiplicativeOperator primaryExpression)*
    private fun generateMultiplicativeExpression(): StringBuilder {
        val primaryExpression = generatePrimaryExpression()

        while (decideToContinue(0.3)) {
            primaryExpression.append(listOf(" * ", " / ").random())
            primaryExpression.append(generatePrimaryExpression())
        }

        return primaryExpression
    }

    // primaryExpression ::= '(' expression ')' | variable | constant
    private fun generatePrimaryExpression(): StringBuilder {
        val unaryExpression = java.lang.StringBuilder()

        when (Random.nextInt(0, 18)) {
            in 0..2 -> {
                unaryExpression.append("(")
                unaryExpression.append(generateExpression())
                unaryExpression.append(")")
            }
            in 3..11 -> unaryExpression.append(generateVariable())
            in 11..17 -> unaryExpression.append(generateConstant())
        }

        return unaryExpression
    }

    private fun generateVariable(): StringBuilder {
        return StringBuilder(('a'..'z').toList().random().toString())
    }

    private fun generateConstant(): StringBuilder {
        return StringBuilder(Random.nextInt(0, 10001).toString())
    }

    private fun decideToContinue(probability: Double): Boolean =
          Random.nextInt(0, 100) / 100.0 < probability
                  && Thread.currentThread().stackTrace.size - initialDepth < maxDepth
}