package Hotel

import kotlin.system.exitProcess
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter

/*oque falta
    um sistema inteiro para cadastrar hospede aqui no Hotel.kt
    exibir o mapa de quartos em grade 4x5 igual a atividade 4.2-10 pede
    criar data class pra tudo que pede na 4.2-9?
*/

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

//lista mutavel de quartos livres e ocupados

val quartosLivres = mutableListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)
val quartosOcupados = mutableListOf<Int>()

//data class da função reserva de quarto (*PARA FAZER TALVEZ, AINDA NAO ENTENDI O OUTRO PROGRAMA 'CadastroHospedesDataClass'*)

//lista mutavel de hospedes
data class Hospede(
    var nome: String,
    var idade: String,
    )

var hospedes = mutableListOf<Hospede>()
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

    print("\nEscolha uma opção:")
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

fun cadastrarQuartos() {

    var rodandoQuartos = true


    while (rodandoQuartos) {

        print("\nRESERVA\n")

        print("\nInforme o valor da diária: ")
        valor = readln().toInt()

        print("Informe a quantidade de diárias (1-30): ")
        diasDiaria = readln().toInt()

        if (diasDiaria <= 0) {
            print("Valor inválido $nomeUsuario\n")
            print("Voltando para o início\n")
            rodandoQuartos = false
        }
        else if (diasDiaria >= 31) {
            print("Comando inválido $nomeUsuario\n")
            print("Não é possível fazer uma reserva maior que 30 dias.\n")
            print("Voltando para o início\n")
            rodandoQuartos = false
        }
        else if (diasDiaria >= 1 && diasDiaria <= 30) {

            //nome do hóspede
            print("Qual o nome completo do hóspede: ")
            nomeHospede = readln()


            //tipo de quarto
            print("Taxa de serviço\n")
            print("Quarto Standard: 10%\n")
            print("Quarto Executivo: 13%\n")
            print("Quarto Luxo: 16%\n")
            print("Tipo de quarto (S/E/L): ")
            val tipoQuarto = readln().uppercase()

            when(tipoQuarto){
                "S" -> {
                    fatorTipo = 1.0
                    print("Quarto selecionado: Quarto Standard\n")
                    print("Escolha um quarto (1-9): ")
                    numQuarto = readln().toInt()
                    while (numQuarto<= 0 || numQuarto >= 10){
                        print("Este quarto não faz parte da classe Standard\n")
                        print("Escolha outro quarto dentro da classe Standard")
                        numQuarto = readln().toInt()
                    }
                }
                "E" -> {
                    fatorTipo = 1.35
                    print("Quarto selecionado: Quarto Executivo\n")
                    print("Escolha um quarto (10-16): ")
                    numQuarto = readln().toInt()
                    while (numQuarto<= 9 || numQuarto >= 17){
                        print("Este quarto não faz parte da classe Executivo\n")
                        print("Escolha outro quarto dentro da classe Executivo")
                        numQuarto = readln().toInt()
                    }
                }
                "L" -> {
                    fatorTipo = 1.65
                    print("Quarto selecionado: Quarto Luxo\n")
                    print("Escolha um quarto (17-20): ")
                    numQuarto = readln().toInt()
                    while (numQuarto<= 16 || numQuarto >= 21){
                        print("Este quarto não faz parte da classe Luxo")
                        print("Escolha outro quarto dentro da classe Luxo")
                        numQuarto = readln().toInt()
                    }
                }
            }

            if (numQuarto in quartosLivres){
                //calcular tudo
                val subtotal = (diasDiaria * valor) * fatorTipo
                val taxaServico = (subtotal * 10)/100
                val totalFinal = subtotal + taxaServico
                println("\nResumo:")
                println("Hóspede: $nomeHospede")
                println("Quarto: $numQuarto")
                println("Subtotal: R$ %.2f".format(subtotal))
                println("Taxa de serviço (10%%): R$ %.2f".format(taxaServico))
                println("Total: R$ %.2f".format(totalFinal))

            }

            else if (numQuarto in quartosOcupados){
                print("Quarto ocupado no momento, escolha outro por favor\n")
                print("Quartos livres: ")
                for (quarto in quartosLivres){
                    print("Quarto $quarto\n")
                }
                numQuarto = readln().toInt()
            }
            //numero do quarto (1 a 20)
        }

        print("\n$nomeUsuario, deseja confirmar reserva(S/N): ")
        var continuarQuarto = readln().uppercase()

        while (continuarQuarto != "S" && continuarQuarto != "N"){
            print("Comando inválido!\n")
            print("Digite um comando válido: ")
            continuarQuarto = readln().uppercase()
        }

        when (continuarQuarto){
            "S" -> {
                print("\nReserva efetuada com sucesso.")

                //cadastrar hóspede


                //ocupando quarto
                quartosLivres.remove(numQuarto)
                quartosOcupados.add(numQuarto)
            }
            "N"->{
                println("\nReserva não efetuada. Voltando para o início...")
            }
        }
    }
}
/* funcao de exibir quartos
fun exibirMapaQuartos() {
    println("\n--- MAPA DE QUARTOS ---")
    for (linha in 0..3) {
        for (coluna in 1..5) {
            val numeroQuarto = (linha * 5) + coluna
            val status = if (quartosLivres.contains(numeroQuarto)) "L" else "O"

            // Formata com zero à esquerda (ex: 01, 02) para a grade ficar alinhada
            val numeroFormatado = numeroQuarto.toString().padStart(2, '0')
            print("[ $numeroFormatado: $status ]\t")
        }
        println() // Quebra a linha após 5 quartos
    }
    println("-----------------------\n")
}
*/
fun cadastrarHospedes() {
    var rodandoHospedes = true
    while (rodandoHospedes) {
        print("\nEscolha uma opção:")
        print("1-Cadastrar\n")
        print("2-Pesquisar por nome exato\n")
        print("3-Pesquisar por prefixo\n")
        print("4-Listar ordenado (A-Z)\n")
        print("5-Atualizar cadastro\n")
        print("Remover cadastro\n")
        print("7-Sair\n")
        val escolha = readln()

        when (escolha) {
            "1" -> {
                if (hospedes.size == 15) {
                    print("Máximo de cadastros atingido\n")
                } else {
                    print("Nome do hóspede: ")
                    val hospedeNome = readln().uppercase()
                    print("Idade do hóspede: ")
                    val hospedeIdade = readln()
                    val jaExiste = hospedes.any { it.nome.equals(hospedeNome, ignoreCase = true) }
                    if (jaExiste) {
                        print("Hóspede já cadastrado")
                    } else {
                        val novoHospede = Hospede(nome = hospedeNome, idade = hospedeIdade)
                        hospedes.add(novoHospede)
                        print("\n|Nome: $hospedeNome\n")
                        print("|Idade: $hospedeIdade\n")
                        print("Operação realizada com sucesso\n")
                    }
                }
            }

            "2" -> {
                print("Qual o nome do hóspede que deseja buscar: ")
                buscar = readln().uppercase().trim()


                val hospedeEncontrado = hospedes.find { it.nome == buscar || it.idade == buscar }

                if (hospedeEncontrado != null) {
                    println("Hóspede encontrado!")
                    println("Nome: ${hospedeEncontrado.nome}")
                    println("Idade: ${hospedeEncontrado.idade}")
                } else {
                    print("Hóspede não encontrado!")
                }
            }

            "3" -> {
                print("Qual o nome do hóspede que deseja buscar: ")
                buscar = readln().uppercase().trim()

                val resultadoPesquisa = hospedes.filter {
                    it.nome.startsWith(buscar, ignoreCase = true)
                }

                if (resultadoPesquisa.isNotEmpty()) {
                    print("Hóspedes ncontrados: ${resultadoPesquisa.size}\n")
                    for (hospede in resultadoPesquisa) {
                        print("-Nome: ${hospede.nome} | Idade: ${hospede.idade}\n")
                    }
                }
            }

            "4" -> {
                if (hospedes.isEmpty()) {
                    println("\nNenhum hóspede cadastrado para listar.\n")
                } else {
                    print("\nLista de Hóspedes (A-Z)\n")
                    val hospedesOrdenados = hospedes.sortedBy { it.nome }
                    for (hospede in hospedesOrdenados) {
                        println("Nome: ${hospede.nome} | Idade: ${hospede.idade}")
                    }
                }
            }

            "5" -> {
                print("Digite o nome exato do hóspede que deseja atualizar: ")
                val nomeBusca = readln().trim()

                // Busca a referência do objeto na lista
                val hospedeEncontrado = hospedes.find { it.nome.equals(nomeBusca, ignoreCase = true) }

                if (hospedeEncontrado != null) {
                    println("Hóspede encontrado: ${hospedeEncontrado.nome}, Idade atual: ${hospedeEncontrado.idade}")

                    print("Digite a nova idade (ou pressione Enter para manter a mesma): ")
                    val entradaIdade = readln().trim()

                    // Atualiza a idade apenas se o usuário digitou um número válido
                    val novaIdade = entradaIdade
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
                print("Listando hóspedes...\n")
                //contato é hospede, mas para não confundir coloquei contato
                for (contato in hospedes) {
                    println("Nome:${contato.nome} | Número: ${contato.idade}")
                }
                print("Lista de hóspedes encerrada\n")
                print("Nome do hóspede que deseja remover: ")
                val remover = readln().uppercase()
                val hospedeEncontrado = hospedes.find { it.nome == remover || it.idade == remover }

                if (hospedeEncontrado != null) {
                    println("Contato ${hospedeEncontrado.nome} encontrado!")
                    print("Deseja remover contato (S/N): ")
                    var removerMesmo = readln().uppercase()
                    while (removerMesmo != "S" && removerMesmo != "N") {
                        println("Comando inválido")
                        print("Por favor, digite um comando válido: ")
                        removerMesmo = readln().uppercase()
                    }
                    when (removerMesmo) {
                        "S" -> {
                            hospedes.remove(hospedeEncontrado)
                            println("Hóspede ${hospedeEncontrado.nome} removido!")
                        }

                        "N" -> {
                            println("Hóspede ${hospedeEncontrado.nome} não removido!")
                        }
                    }
                }
            }

            "7" -> {
                print("Saindo do programa Cadastrar Hóspedes...")
                rodandoHospedes = false
            }

       }
    }
}

fun eventos() {

}

fun arCondicionado(){

}

fun AbastecimentoDeAutomoveis() {

}

fun erro(){
    println("Por favor, informe um número entre 1 e 7.")
    escolhaHotel = readln().toIntOrNull()

}

fun sairDoHotel() {
    print("Você deseja sair? (S/N): ")
    val resposta = readln().trim().uppercase()

    if (resposta == "S" || resposta == "SIM") {
        println("Muito obrigado e até logo $nomeUsuario!")
    } else {
        inicio()
    }
}

fun bloqueioSistema() {
    println("Encerrando sistema...")
    exitProcess(0)
}