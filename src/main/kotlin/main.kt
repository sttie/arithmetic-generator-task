fun main() {
    var choice: String?
    do {
        print("Generate new expression (y/n)? ")
        choice = readLine()

        if (choice == "y")
            println(Generator.generateArithmeticExpression())
        else if (choice != "y" && choice != "n")
            println("Enter y or n!")
    } while (choice != "n")
}
