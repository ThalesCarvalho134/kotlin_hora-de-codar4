package Hotel

// data class representa um objeto de dados e já fornece equals(), toString() e copy().
// Cada objeto Hospede guarda as informações de uma pessoa cadastrada no hotel.
data class Hospedeq(
    val nome: String,
    // Valor padrão mantém o cadastro simples quando a idade não for informada.
    val idade: Int = 0
)

// Um Quarto tem número e tipo fixos. Já o hóspede pode mudar ao ocupar ou liberar o quarto.
data class Quarto(
    val numero: Int, // de 1 a 20
    val tipo: String, // Luxo, Padrão, Master
    var hospede: Hospede? = null // Informar o hóspede, mas caso não haja um, o valor será nulo.
) {
    // Propriedade calculada: verifica se há um hóspede associado ao quarto.
    val estaOcupado: Boolean
        get() = hospede != null
}

/**
 * Exemplo simples para praticar a manipulação de objetos Hospede e Quarto.
 */
fun CadastroHospedesDataClass() {
    // A lista pode receber novos objetos Hospede durante a execução.
    val hospedes = mutableListOf<Hospede>()
    // A lista representa os quartos disponíveis no hotel.
    val quartos = (1..20).map { numero -> Quarto(numero, "Padrão") }.toMutableList()
    // Repete o menu até que a pessoa escolha voltar.
    while (true) {
        println(
            """
            |
            |[Cadastro de Hóspedes]
            |1. Cadastrar hóspede
            |2. Pesquisar hóspede
            |3. Hospedar em um quarto
            |4. Liberar quarto
            |5. Listar hóspedes e quartos
            |6. Voltar
            |Opção:""".trimMargin()
        )

        // toIntOrNull evita uma exceção quando a entrada não for um número.
        val escolha = readln().toIntOrNull()

        when (escolha) {
            1 -> cadastrarHospede(hospedes)
            2 -> pesquisarHospede(hospedes)
            3 -> hospedarEmQuarto(hospedes, quartos)
            4 -> liberarQuarto(quartos)
            5 -> listarHospedesEQuartos(hospedes, quartos)
            6 -> return // nothing
            else -> println("Informe uma opção entre 1 e 6.")
        }
    }
}
fun listarHospedes(hospedes: MutableList<Hospede>) {
}

fun cadastrarHospede(hospedes: MutableList<Hospede>) {
    // Regra do exercício: no máximo 20 objetos Hospede na lista.
    if (hospedes.size == 20) {
        println("Máximo de cadastros atingido.")
        return
    }

    print("Nome do hóspede: ")
    val nome = readln().trim()

    if (nome.isBlank()) {
        println("O nome não pode ficar vazio.")
    } else if (hospedes.any { it.nome.equals(nome, ignoreCase = true) }) {
        println("Hóspede já cadastrado.")
    } else {
        // Cria o objeto Hospede e o adiciona à lista.
        hospedes.add(Hospede(nome))
        println("Hóspede cadastrado com sucesso.")
    }
}

fun pesquisarHospede(hospedes: List<Hospede>) {
    print("Nome do hóspede: ")
    val nome = readln().trim()
    // find devolve o objeto encontrado ou null se ele não existir.
    val hospede = hospedes.find { it.nome.equals(nome, ignoreCase = true) }

    print(hospede?.let { "Hóspede ${it.nome} foi encontrado." } ?: "Hóspede não encontrado.\n")
}

fun hospedarEmQuarto(hospedes: List<Hospede>, quartos: List<Quarto>) {
    if (hospedes.isEmpty()) {
        print("Cadastre um hóspede antes de escolher um quarto.\n")
        return
    }

    print("Nome do hóspede: ")
    val nome = readln().trim()
    val hospede = hospedes.find { it.nome.equals(nome, ignoreCase = true) }

    if (hospede == null) {
        println("Hóspede não encontrado.")
        return
    }

    print("Número do quarto (1 a 20): ")
    val numero = readln().toIntOrNull()
    // Encontramos o objeto Quarto pelo número digitado.
    val quarto = quartos.find { it.numero == numero }

    when {
        quarto == null -> println("Quarto inválido.")
        quarto.estaOcupado -> println("Quarto já está ocupado.")
        quartos.any { it.hospede == hospede } -> println("Este hóspede já está em um quarto.")
        else -> {
            // A ocupação é registrada ao guardar o Hospede dentro do Quarto.
            quarto.hospede = hospede
            println("${hospede.nome} foi hospedado(a) no quarto ${quarto.numero}.")
        }
    }
}

fun liberarQuarto(quartos: List<Quarto>) {
    print("Número do quarto a liberar: ")
    val numero = readln().toIntOrNull()
    val quarto = quartos.find { it.numero == numero }

    when {
        quarto == null -> println("Quarto inválido.")
        !quarto.estaOcupado -> println("O quarto ${quarto.numero} já está livre.")
        else -> {
            // null remove a associação e torna o quarto livre novamente.
            quarto.hospede = null
            println("Quarto ${quarto.numero} liberado com sucesso.")
        }
    }
}

fun listarHospedesEQuartos(hospedes: List<Hospede>, quartos: List<Quarto>) {
    println("\nHóspedes cadastrados:")
    if (hospedes.isEmpty()) println("Nenhum hóspede cadastrado.")
    hospedes.forEachIndexed { indice, hospede -> println("${indice + 1}. ${hospede.nome}") }

    println("\nQuartos:")
    quartos.forEach { quarto ->
        // ?: mostra "Livre" caso a propriedade hospede seja null.
        val status = quarto.hospede?.nome ?: "Livre"
        println("Quarto ${quarto.numero}: $status")
    }
}