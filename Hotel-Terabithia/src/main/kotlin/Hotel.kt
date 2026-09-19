package Hotel

import kotlin.system.exitProcess
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import kotlin.text.compareTo

/*oque falta
    um sistema inteiro para cadastrar hospede aqui no Hotel.kt
    criar data class pra tudo que pede na 4.2-9?
*/

//para organizar depois
val listaHospedes = mutableListOf<Hospede>()
val listaQuartos = (1..20).map { numero ->
    val tipo = when (numero){
        in 1..9 -> "Standard"
        in 10..16 -> "Executivo"
        else ->"Luxo"
    }
    Quarto(numero, tipo)
}.toMutableList()

//variavel while para o menu continuar funcionando
var rodandoMenu = true

val nomeHotel =  "Grand Horizon"

//autenticação
var nomeUsuario = ""
var senha = ""
val senhaCorreta = "2678"
var tentaitvas = 3
var autenticado = false

//reserva de quartos
var escolhaHotel: Int? = null
var valor = 0
var diasDiaria = 0
var fatorTipo = 0.0
var nomeHospede = ""
var numQuarto = 0

var buscar = ""

fun main() {
    inicio()
}

fun inicio() {
    print("\nBem vindo ao $nomeHotel Hotel!\n")
    auth()
    //colocar nome de usuario e senha
}

fun auth() {
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
                3 -> eventos()
                4 -> arCondicionado()
                5 -> AbastecimentoDeAutomoveis()
                6 -> CadastroHospedesDataClass()
                7 -> sairDoHotel()
                else -> erro()
            }
        }
}

fun cadastrarQuartos() {
    var rodandoQuartos = true

    while (rodandoQuartos) {
        println("\n--- RESERVA DE QUARTO ---")

        print("Informe o valor da diária: R$ ")
        valor = readln().toIntOrNull() ?: 0

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

        val quartoSelecionado = listaQuartos.find { it.numero == numQuarto }

        if (quartoSelecionado == null) {
            println("Quarto não encontrado.")
            continue
        }

        if (quartoSelecionado.hospede != null) {
            println("O quarto $numQuarto já está OCUPADO! Por favor, recomece a reserva e escolha outro quarto.")
            continue
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

//funcao de exibir quartos
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
    var rodandoHospedes = true
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
                if (listaHospedes.size == 20) {
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
                    for (hospede in hospedesOrdenados) {
                        println("Nome: ${hospede.nome} | Idade: ${hospede.idade}")
                    }
                }
            }

            "5" -> {
                print("Digite o nome exato do hóspede que deseja atualizar: ")
                val nomeBusca = readln().trim()

                val hospedeEncontrado = listaHospedes.find { it.nome.equals(nomeBusca, ignoreCase = true) }

                if (hospedeEncontrado != null) {
                    println("Hóspede encontrado: ${hospedeEncontrado.nome}, Idade atual: ${hospedeEncontrado.idade}")
                    print("Digite a nova idade: ")
                    val novaIdade = readln().toIntOrNull()

                    if (novaIdade != null) {
                        hospedeEncontrado.idade = novaIdade
                        println("Cadastro de ${hospedeEncontrado.nome} atualizado com sucesso!")
                    } else {
                        println("Idade inválida. A idade não foi alterada.")
                    }
                } else {
                    println("Hóspede não encontrado.")
                }
            }

            "6" -> {
                if (listaHospedes.isEmpty()) {
                    println("Nenhum hóspede cadastrado para remover.")
                } else {
                    println("Listando hóspedes:")
                    for (hospede in listaHospedes) {
                        println("Nome: ${hospede.nome} | Idade: ${hospede.idade}")
                    }
                    print("\nNome do hóspede que deseja remover: ")
                    val remover = readln().trim()
                    val hospedeEncontrado = listaHospedes.find { it.nome.equals(remover, ignoreCase = true) }

                    if (hospedeEncontrado != null) {
                        print("Deseja mesmo remover ${hospedeEncontrado.nome}? (S/N): ")
                        val confirmar = readln().uppercase().trim()
                        if (confirmar == "S") {
                            listaHospedes.remove(hospedeEncontrado)
                            println("Hóspede removido com sucesso!")
                        } else {
                            println("Remoção cancelada.")
                        }
                    } else {
                        println("Hóspede não encontrado.")
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

fun eventos() {
    // 6.1 Parte A — Capacidade e seleção de auditório
    print("Informe o número de convidados: ")
    val convidados = readln().toIntOrNull() ?: -1

// Validação: menor que zero ou maior que 350
    if (convidados <= 0 || convidados > 350) {
        println("Número de convidados inválido.")
        return // Volta para o menu principal sem fechar o sistema
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
        return
    }

    print("Informe o horário inicial do evento (0 a 23): ")
    val horaInicio = readln().toIntOrNull() ?: -1

    print("Informe a duração do evento em horas (1 a 12): ")
    val duracao = readln().toIntOrNull() ?: -1

// Validação da Duração (1 a 12h)
    if (duracao !in 1..12) {
        println("Duração inválida. O evento deve durar de 1 a 12 horas.")
        return
    }

    val horaFim = horaInicio + duracao

// Validação se o horário final respeita a janela do dia
    if (dia in diasUteis && (horaInicio < 7 || horaFim > 23)) {
        println("Auditório indisponível. Para dias úteis, o evento deve iniciar a partir das 07h e encerrar até às 23h.")
        return
    } else if (dia in fimDeSemana && (horaInicio < 7 || horaFim > 15)) {
        println("Auditório indisponível. Para fins de semana, o evento deve iniciar a partir das 07h e encerrar até às 15h.")
        return
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
    } else {
        println("\nReserva não efetuada.")
    }
}

fun arCondicionado(){

}

fun AbastecimentoDeAutomoveis() {

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
        inicio()
    }
}

fun bloqueioSistema() {
    println("Encerrando sistema...")
    exitProcess(0)
}