package Hotel

import kotlin.system.exitProcess

// Apenas o que realmente precisa ser global:
//Hóspedes
val listaHospedes = mutableListOf<Hospede>()
val listaQuartos = (1..20).map { numero ->
    val tipo = when (numero){
        in 1..9 -> "Standard"
        in 10..16 -> "Executivo"
        else -> "Luxo"
    }
    Quarto(numero, tipo)
}.toMutableList()

//Variáveis usadas em mais de uma função
var rodandoMenu = true
val nomeHotel = "Grand Horizon"
var nomeUsuario = ""

fun main() {
    inicio()
}

fun inicio() {
    print("\nBem vindo ao $nomeHotel Hotel!\n")
    auth()
    //colocar nome de usuario e senha
}

fun auth() {
    //Variáveis locais
    var senha = ""
    val senhaCorreta = "2678"
    var tentaitvas = 3
    var autenticado = false

    //Começo do código
    print("Digite seu nome de usuário: ")
    nomeUsuario = readln().uppercase()

    while (tentaitvas > 0) {
        print("Digite a senha: ")
        senha = readln()

        if (senha == senhaCorreta) {
            autenticado = true
            break
        } else {
            tentaitvas--
            if (tentaitvas > 0) {
                print("Senha incorreta, $tentaitvas tentativa(s) restante.\n")
            }
        }
    }

        if (autenticado) {
            print("\nBem vindo ao $nomeHotel Hotel, $nomeUsuario. É um imenso prazer ter você por aqui!\n")
            menu()
        } else {
            print("Número de tentativas excedido.\n")
            bloqueioSistema()
            exitProcess(0)
        }
}

fun menu(){
    //Variáveis locais
    var escolhaHotel: Int? = null
    var receitaEventos = 0.0

    //Começo do código
        while (rodandoMenu) {

            print("\nEscolha uma opção:\n")
            print("1-Reservas de Quartos\n")
            print("2-Cadastro de Hóspedes\n")
            print("3-Eventos\n")
            print("4-Ar-Condicionado\n")
            print("5-Abastecimento\n")
            print("6-Relatórios Operacionais\n")
            print("7-Sair\n")


            // A varival escolha armazena a opção escolhida pelo usuário.
            // uma variavel local é utilizada apenas dentro da função inicio().
            escolhaHotel = readln().toIntOrNull()
            when (escolhaHotel) {
                1 -> cadastrarQuartos()
                2 -> cadastrarHospedes()
                3 -> receitaEventos += eventos()
                4 -> arCondicionado()
                5 -> AbastecimentoDeAutomoveis()
                6 -> relatoriosOperacionais(receitaEventos)
                7 -> sairDoHotel()
                else -> erro()
            }
        }
}

fun cadastrarQuartos() {
    //Variáveis locais
    var valor = 0
    var diasDiaria = 0
    var fatorTipo = 0.0
    var nomeHospede = ""
    var numQuarto = 0
    var rodandoQuartos = true

    //Começo do código
    while (rodandoQuartos) {
        println("\n--- RESERVA DE QUARTO ---")

        print("Informe o valor da diária: R$ ")
        valor = readln().toIntOrNull() ?: 0
        if (valor <= 0){
            print("Valor inválido, $nomeUsuario. Voltando para o menu principal...")
            rodandoQuartos = false
            continue
        }

        print("Informe a quantidade de diárias (1-30): ")
        diasDiaria = readln().toIntOrNull() ?: 0

        if (diasDiaria <= 0) {
            println("Valor inválido, $nomeUsuario. Voltando para o menu principal...")
            rodandoQuartos = false
            continue
        } else if (diasDiaria > 30) {
            println("Não é possível fazer uma reserva superior a 30 dias.")
            println("Voltando para o menu principal...")
            rodandoQuartos = false
            continue
        }

        print("Qual o nome completo do hóspede: ")
        nomeHospede = readln().trim()

        // Localiza ou cria o hóspede na lista de cadastros
        var hospedeReserva = listaHospedes.find { it.nome.equals(nomeHospede, ignoreCase = true) }
        if (hospedeReserva == null) {
            hospedeReserva = Hospede(nome = nomeHospede)
            listaHospedes.add(hospedeReserva)
        }

        println("\nTipos de quarto:")
        println("S - Standard (Multiplicador 1.0)")
        println("E - Executivo (Multiplicador 1.35)")
        println("L - Luxo (Multiplicador 1.65)")
        print("Opção de quarto (S/E/L): ")
        val tipoQuarto = readln().uppercase().trim()

        val (faixaMin, faixaMax) = when (tipoQuarto) {
            "S" -> {
                fatorTipo = 1.0
                Pair(1, 9)
            }
            "E" -> {
                fatorTipo = 1.35
                Pair(10, 16)
            }
            "L" -> {
                fatorTipo = 1.65
                Pair(17, 20)
            }
            else -> {
                println("Tipo de quarto inválido. Cancelando reserva...")
                rodandoQuartos = false
                continue
            }
        }

        exibirMapaQuartos()
        print("Escolha um quarto ($faixaMin-$faixaMax): ")
        numQuarto = readln().toIntOrNull() ?: 0

        while (numQuarto !in faixaMin..faixaMax) {
            println("Este número não faz parte do tipo de quarto selecionado.")
            print("Escolha outro quarto ($faixaMin-$faixaMax): ")
            numQuarto = readln().toIntOrNull() ?: 0
        }

        var quartoSelecionado = listaQuartos.find { it.numero == numQuarto }

        // Enquanto o quarto não existir, for de outra categoria OU estiver ocupado:
        while (quartoSelecionado == null || numQuarto !in faixaMin..faixaMax || quartoSelecionado.hospede != null) {

            if (numQuarto !in faixaMin..faixaMax) {
                println("Número inválido para este tipo de quarto ($faixaMin-$faixaMax).")
            } else {
                println("O quarto $numQuarto já está OCUPADO! Por favor, escolha outro quarto.")
            }

            exibirMapaQuartos()
            print("Escolha outro quarto ($faixaMin-$faixaMax): ")
            numQuarto = readln().toIntOrNull() ?: 0

            // ATUALIZA o quarto selecionado com o novo número digitado!
            quartoSelecionado = listaQuartos.find { it.numero == numQuarto }
        }

        // Cálculos do valor da diária
        val subtotal = (diasDiaria * valor) * fatorTipo
        val taxaServico = subtotal * 0.10
        val totalFinal = subtotal + taxaServico

        println("\n--- RESUMO DA RESERVA ---")
        println("Hóspede: ${hospedeReserva.nome}")
        println("Quarto: $numQuarto (${quartoSelecionado.tipo})")
        println("Diárias: $diasDiaria dia(s)")
        println("Subtotal: R$ %.2f".format(subtotal))
        println("Taxa de serviço (10%%): R$ %.2f".format(taxaServico))
        println("Total Final: R$ %.2f".format(totalFinal))

        print("\n$nomeUsuario, deseja confirmar a reserva? (S/N): ")
        var continuarQuarto = readln().uppercase().trim()

        while (continuarQuarto != "S" && continuarQuarto != "N") {
            print("Opção inválida! Digite S para Sim ou N para Não: ")
            continuarQuarto = readln().uppercase().trim()
        }

        if (continuarQuarto == "S") {
            quartoSelecionado.hospede = hospedeReserva
            println("\nReserva efetuada com sucesso!")
            exibirMapaQuartos()
        } else {
            println("\nReserva cancelada.")
        }

        rodandoQuartos = false
    }
}

//função de exibir quartos
fun exibirMapaQuartos() {
    println("\n--- MAPA DE QUARTOS ---")
    for (i in listaQuartos.indices) {
        val quarto = listaQuartos[i]
        val status = if (quarto.hospede == null) "L" else "O"
        val numeroFormatado = quarto.numero.toString().padStart(2, '0')
        print("[ $numeroFormatado: $status ]\t")

        // Quebra a linha a cada 5 quartos na tela
        if ((i + 1) % 5 == 0) {
            println()
        }
    }
    println("-----------------------\n")
}

fun cadastrarHospedes() {
    //Variáveis locais
    var buscar = ""
    var rodandoHospedes = true

    //Começo do código
    while (rodandoHospedes) {
        print("\n--- CADASTRO DE HÓSPEDES ---")
        print("\nEscolha uma opção:\n")
        print("1-Cadastrar\n")
        print("2-Pesquisar por nome exato\n")
        print("3-Pesquisar por prefixo\n")
        print("4-Listar ordenado (A-Z)\n")
        print("5-Atualizar cadastro\n")
        print("6-Remover cadastro\n")
        print("7-Sair\n")
        print("Opção: ")
        val escolha = readln()

        when (escolha) {
            "1" -> {
                if (listaHospedes.size >= 15) {
                    println("Máximo de cadastros atingido.")
                } else {
                    print("Nome do hóspede: ")
                    val hospedeNome = readln().trim()
                    print("Idade do hóspede: ")
                    val hospedeIdade = readln().toIntOrNull() ?: 0

                    if (hospedeNome.isBlank()) {
                        println("O nome não pode ficar vazio.")
                    } else if (listaHospedes.any { it.nome.equals(hospedeNome, ignoreCase = true) }) {
                        println("Hóspede já cadastrado.")
                    } else {
                        val novoHospede = Hospede(nome = hospedeNome, idade = hospedeIdade)
                        listaHospedes.add(novoHospede)
                        println("\n|Nome: $hospedeNome")
                        println("|Idade: $hospedeIdade")
                        println("Operação realizada com sucesso!")
                    }
                }
            }

            "2" -> {
                print("Qual o nome do hóspede que deseja buscar: ")
                buscar = readln().trim()

                val hospedeEncontrado = listaHospedes.find { it.nome.equals(buscar, ignoreCase = true) }

                if (hospedeEncontrado != null) {
                    println("Hóspede encontrado!")
                    println("Nome: ${hospedeEncontrado.nome} | Idade: ${hospedeEncontrado.idade}")
                } else {
                    println("Hóspede não encontrado!")
                }
            }

            "3" -> {
                print("Qual o prefixo/início do nome: ")
                buscar = readln().trim()

                val resultadoPesquisa = listaHospedes.filter {
                    it.nome.startsWith(buscar, ignoreCase = true)
                }

                if (resultadoPesquisa.isNotEmpty()) {
                    println("Hóspedes encontrados: ${resultadoPesquisa.size}")
                    for (hospede in resultadoPesquisa) {
                        println("- Nome: ${hospede.nome} | Idade: ${hospede.idade}")
                    }
                } else {
                    println("Nenhum hóspede encontrado com esse início.")
                }
            }

            "4" -> {
                if (listaHospedes.isEmpty()) {
                    println("\nNenhum hóspede cadastrado para listar.\n")
                } else {
                    println("\nLista de Hóspedes (A-Z):")
                    val hospedesOrdenados = listaHospedes.sortedBy { it.nome }
                    hospedesOrdenados.forEachIndexed { indice, hospede ->
                        println("${indice + 1}. ${hospede.nome} | Idade: ${hospede.idade} | Cadastro: ${hospede.dataHoraCadastro}")
                    }

                }
            }

            "5" -> {
                if (listaHospedes.isEmpty()) {
                    println("Nenhum hóspede cadastrado para atualizar.")
                } else {
                    println("\n--- Selecione o hóspede que deseja atualizar ---")
                    listaHospedes.forEachIndexed { indice, hospede ->
                        println("${indice + 1} - ${hospede.nome} (Idade: ${hospede.idade})")
                    }

                    print("\nDigite o número do hóspede: ")
                    val opcao = readln().toIntOrNull()

                    if (opcao != null && opcao in 1..listaHospedes.size) {
                        val hospedeEncontrado = listaHospedes[opcao - 1]
                        println("Hóspede selecionado: ${hospedeEncontrado.nome}, Idade atual: ${hospedeEncontrado.idade}")

                        print("Digite a nova idade: ")
                        val novaIdade = readln().toIntOrNull()

                        if (novaIdade != null && novaIdade >= 0) {
                            hospedeEncontrado.idade = novaIdade
                            println("Cadastro de ${hospedeEncontrado.nome} atualizado com sucesso!")
                        } else {
                            println("Idade inválida. A idade não foi alterada.")
                        }
                    } else {
                        println("Opção inválida. Escolha um número da lista.")
                    }
                }
            }

            "6" -> {
                if (listaHospedes.isEmpty()) {
                    println("Nenhum hóspede cadastrado para remover.")
                } else {
                    println("Listando hóspedes:")
                    listaHospedes.forEachIndexed { indice, hospede ->
                        println("${indice + 1}. ${hospede.nome} | Idade: ${hospede.idade}")
                    }

                    print("\nDigite o número do hóspede que deseja remover: ")
                    val opcao = readln().toIntOrNull()

                    if (opcao != null && opcao in 1..listaHospedes.size) {
                        val hospedeEncontrado = listaHospedes[opcao - 1]

                        print("Deseja mesmo remover ${hospedeEncontrado.nome}? (S/N): ")
                        val confirmar = readln().uppercase().trim()

                        if (confirmar == "S") {
                            listaHospedes.remove(hospedeEncontrado)
                            println("Hóspede removido com sucesso!")
                        } else {
                            println("Remoção cancelada.")
                        }
                    } else {
                        println("Opção inválida. Escolha um número da lista.")
                    }
                }
            }

            "7" -> {
                println("Saindo do cadastro de hóspedes...")
                rodandoHospedes = false
            }

            else -> println("Opção inválida.")
        }
    }
}

fun eventos(): Double {
    // 6.1 Parte A — Capacidade e seleção de auditório
    print("Informe o número de convidados: ")
    val convidados = readln().toIntOrNull() ?: -1

// Validação: menor que zero ou maior que 350
    if (convidados <= 0 || convidados > 350) {
        println("Número de convidados inválido.")
        return 0.0 // Volta para o menu principal sem fechar o sistema
    }

    println("Número de convidados válido.")

    val auditorio: String

    if (convidados <= 220) {
        auditorio = "Laranja"
        if (convidados > 150) {
            val cadeirasAdicionais = convidados - 150
            println("Auditório selecionado: $auditorio ($cadeirasAdicionais cadeiras adicionais)")
        } else {
            println("Auditório selecionado: $auditorio")
        }
    } else {
        auditorio = "Colorado"
        println("Auditório selecionado: $auditorio")
    }

    // 6.2 Parte B — Agenda e disponibilidade (com Tabela de Horários)

    print("Informe o dia da semana (ex: segunda, terca, sabado): ")
    val dia = readln().lowercase().trim()

    val diasUteis = listOf("segunda", "terca", "terça", "quarta", "quinta", "sexta")
    val fimDeSemana = listOf("sabado", "sábado", "domingo")

// Exibição da Tabela Visual de Horários
    if (dia in diasUteis) {
        println("\n=======================================================")
        println(" TABELA DE HORÁRIOS DISPONÍVEIS - SEGUNDA A SEXTA (07h às 23h)")
        println("=======================================================")
        println("[07h] [08h] [09h] [10h] [11h] [12h] [13h] [14h]")
        println("[15h] [16h] [17h] [18h] [19h] [20h] [21h] [22h] [23h]")
        println("=======================================================\n")
    } else if (dia in fimDeSemana) {
        println("\n=======================================================")
        println(" TABELA DE HORÁRIOS DISPONÍVEIS - SÁBADO E DOMINGO (07h às 15h)")
        println("=======================================================")
        println("[07h] [08h] [09h] [10h] [11h] [12h] [13h] [14h] [15h]")
        println("=======================================================\n")
    } else {
        println("Dia da semana inválido.")
        return 0.0
    }

    print("Informe o horário inicial do evento (0 a 23): ")
    val horaInicio = readln().toIntOrNull() ?: -1

    print("Informe a duração do evento em horas (1 a 12): ")
    val duracao = readln().toIntOrNull() ?: -1

// Validação da Duração (1 a 12h)
    if (duracao !in 1..12) {
        println("Duração inválida. O evento deve durar de 1 a 12 horas.")
        return 0.0
    }

    val horaFim = horaInicio + duracao

// Validação se o horário final respeita a janela do dia
    if (dia in diasUteis && (horaInicio < 7 || horaFim > 23)) {
        println("Auditório indisponível. Para dias úteis, o evento deve iniciar a partir das 07h e encerrar até às 23h.")
        return 0.0
    } else if (dia in fimDeSemana && (horaInicio < 7 || horaFim > 15)) {
        println("Auditório indisponível. Para fins de semana, o evento deve iniciar a partir das 07h e encerrar até às 15h.")
        return 0.0
    }

    print("Qual o nome da empresa contratante: ")
    val nomeEmpresa = readln().trim()
    println("Auditório reservado para $nomeEmpresa: $dia às ${horaInicio}hs.")

    // 6.3 Parte C — Equipe de garçons
    val garconsBase = Math.ceil(convidados / 12.0).toInt()
    val garconsReforco = duracao / 2
    val totalGarcons = garconsBase + garconsReforco
    val custoGarcons = totalGarcons * duracao * 10.50

    println("\nSão necessários $totalGarcons garçons.")
    println("Custo total com garçons: R$ %.2f".format(custoGarcons))

    // 6.4 Parte D — Serviço de Buffet

    val quantidadeCafe = convidados * 0.2
    val custoCafe = quantidadeCafe * 0.80

    val quantidadeAgua = convidados * 0.5
    val custoAgua = quantidadeAgua * 0.40

    val quantidadeSalgados = convidados * 7
    val custoSalgados = quantidadeSalgados * 0.34

    val custoBuffet = custoCafe + custoAgua + custoSalgados

    println("\nO evento precisará de:")
    println("- %.1f litros de café".format(quantidadeCafe))
    println("- %.1f litros de água".format(quantidadeAgua))
    println("- $quantidadeSalgados salgados")
    println("Custo total do buffet: R$ %.2f".format(custoBuffet))

    // 6.5 Parte E — Relatório e Confirmação
    val custoTotal = custoGarcons + custoBuffet

    println("\n==========================================")
    println("          RELATÓRIO DO EVENTO             ")
    println("==========================================")
    println("Auditório reservado: $auditorio")
    println("Empresa contratante: $nomeEmpresa")
    println("Dia: $dia | Horário: ${horaInicio}h às ${horaFim}h")
    println("Duração: ${duracao}h")
    println("Quantidade de convidados: $convidados")
    println("Garçons: $totalGarcons | Custo dos garçons: R$ %.2f".format(custoGarcons))
    println("Consumo do buffet:")
    println("  - Água: %.1f L".format(quantidadeAgua))
    println("  - Café: %.1f L".format(quantidadeCafe))
    println("  - Salgados: $quantidadeSalgados un")
    println("Custo do buffet: R$ %.2f".format(custoBuffet))
    println("------------------------------------------")
    println("CUSTO TOTAL DO EVENTO: R$ %.2f".format(custoTotal))
    println("==========================================")

    print("\nGostaria de efetuar a reserva? (S/N): ")
    var resposta = readln().uppercase().trim()

    while (resposta != "S" && resposta != "N") {
        println("Opção inválida. Digite S para Sim ou N para Não.")
        print("Gostaria de efetuar a reserva? (S/N): ")
        resposta = readln().uppercase().trim()
    }

    if (resposta == "S") {
        println("\nReserva efetuada com sucesso.")
        return custoTotal
    } else {
        println("\nReserva não efetuada.")
        return 0.0
    }

}

fun arCondicionado() {
    var empresaMaisBarata = ""
    var menorValor = Double.MAX_VALUE
    var maiorValor = Double.MIN_VALUE
    var empresaMaisCara = ""
    var continuar = true

    while (continuar) {
        print("Nome da Empresa: ")
        val nomeEmpresa = readln()

        print("Valor por aparelho: ")
        val valorPorAparelho = readln().toDoubleOrNull() ?: 0.0

        print("Quantidade de aparelhos: ")
        val qtdAparelho = readln().toIntOrNull() ?: 0

        print("Porcentagem do desconto: ")
        val porcentagemDesconto = readln().toDoubleOrNull() ?: 0.0

        print("Quantidade minima para desconto: ")
        val qtdMinima = readln().toIntOrNull() ?: 0

        print("Valor do deslocamento: ")
        val deslocamento = readln().toDoubleOrNull() ?: 0.0

        val valorCheio = qtdAparelho * valorPorAparelho
        // Cálculo do valorTotal tratando o desconto e mantendo a variável acessível fora do if
        val valorTotal = if (qtdAparelho >= qtdMinima) {
            val desconto = (valorCheio * porcentagemDesconto) / 100
            (valorCheio + deslocamento) - desconto
        } else {
            valorCheio + deslocamento
        }
        //maior valor
        if (valorTotal > maiorValor) {
            maiorValor = valorTotal
            empresaMaisCara = nomeEmpresa
        }
        //menor valor
        if (valorTotal < menorValor) {
            menorValor = valorTotal
            empresaMaisBarata = nomeEmpresa
        }

        println("|Total a pagar: R$ %.2f".format(valorTotal))



        print("Deseja continuar (S/N): ")
        var respostaContinuar = readln().uppercase()

        // Validação da entrada S/N
        while (respostaContinuar != "S" && respostaContinuar != "N") {
            print("Comando invalido. Digite S ou N: ")
            respostaContinuar = readln().uppercase()
        }

        // Atualização da variável de controle do loop
        when (respostaContinuar) {
            "S" -> continuar = true
            "N" -> continuar = false
        }
    }
    val diferencaPercentual = (maiorValor - menorValor) / menorValor * 100

    println("\n--- Resultados ---")
    println("O orçamento de menor valor é o da $empresaMaisBarata por R$ %.2f".format(menorValor))
    println("O orçamento de maior valor é o da $empresaMaisCara por R$ %.2f".format(maiorValor))
    println("A diferença percentual entre eles é de %.2f%%".format(diferencaPercentual))
}

fun AbastecimentoDeAutomoveis() {
    println("\n[Abastecimento]")

    // Leitura dos preços
    print("Qual o preço do álcool no Wayne Oil: R$ ")
    val alcoolWayne = readln().replace(",", ".").toDoubleOrNull() ?: 0.0
    print("Qual o preço da gasolina no Wayne Oil: R$ ")
    val gasolinaWayne = readln().replace(",", ".").toDoubleOrNull() ?: 0.0

    print("Qual o preço do álcool no Stark Petrol: R$ ")
    val alcoolStark = readln().replace(",", ".").toDoubleOrNull() ?: 0.0
    print("Qual o preço da gasolina no Stark Petrol: R$ ")
    val gasolinaStark = readln().replace(",", ".").toDoubleOrNull() ?: 0.0

    // Regra dos 70% (Álcool só compensa se for <= 70% do preço da gasolina)
    val opcaoWayneStr = if (alcoolWayne > 0 && alcoolWayne <= gasolinaWayne * 0.70) "Álcool" else "Gasolina"
    val precoWayne = if (opcaoWayneStr == "Álcool") alcoolWayne else gasolinaWayne
    val totalWayne = precoWayne * 42

    val opcaoStarkStr = if (alcoolStark > 0 && alcoolStark <= gasolinaStark * 0.70) "Álcool" else "Gasolina"
    val precoStark = if (opcaoStarkStr == "Álcool") alcoolStark else gasolinaStark
    val totalStark = precoStark * 42

    // Exibição conforme o exemplo do enunciado
    println("\nWayne Oil: melhor opção = $opcaoWayneStr | Total (42L) = R$ %.2f".format(totalWayne))
    println("Stark Petrol: melhor opção = $opcaoStarkStr | Total (42L) = R$ %.2f".format(totalStark))

    // Recomendação Final
    if (totalWayne < totalStark) {
        println("\n$nomeUsuario, é mais barato abastecer com ${opcaoWayneStr.lowercase()} no posto Wayne Oil.")
    } else if (totalStark < totalWayne) {
        println("\n$nomeUsuario, é mais barato abastecer com ${opcaoStarkStr.lowercase()} no posto Stark Petrol.")
    } else {
        println("\n$nomeUsuario, ambos os postos têm o mesmo valor total.")
    }
}

fun relatoriosOperacionais(receitaEventos: Double){
    println("\n==========================================")
    println("      RELATÓRIO OPERACIONAL DO HOTEL      ")
    println("==========================================")

    // Total de hóspedes
    println("Total de hóspedes cadastrados: ${listaHospedes.size}")
    if (listaHospedes.isNotEmpty()) {
        println("\nLista de Hóspedes:")
        listaHospedes.forEachIndexed { index, hospede ->
            println("  ${index + 1}. $hospede")
        }
    } else {
        println("Nenhum hóspede cadastrado até o momento.")
    }

    println("------------------------------------------")
    // Receita de Eventos
    println("Receita Total de Eventos: R$ %.2f".format(receitaEventos))
    println("==========================================\n")
}

fun erro(){
    println("Por favor, informe um número entre 1 e 7.")
}

fun sairDoHotel() {
    print("Você deseja sair? (S/N): ")
    val resposta = readln().trim().uppercase()

    if (resposta == "S" || resposta == "SIM") {
        println("Muito obrigado e até logo $nomeUsuario!")
        rodandoMenu = false
    } else {
        println("Voltando ao menu principal...")
    }
}

fun bloqueioSistema() {
    println("Encerrando sistema...")
    exitProcess(0)
}