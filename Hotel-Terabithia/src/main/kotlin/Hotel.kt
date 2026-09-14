package Hotel

/*oque falta
    cadastrar hospede aqui no Hotel.kt
    senha para login
    exibir o mapa de quartos em grade 4x5 igual a atividade 4.2-10 pede
    criar data class pra tudo que pede na 4.2-9?
*/

val nomeHotel =  "Grand Horizon"

var nomeUsuario = ""
var senha = ""

var escolhaHotel: Int? = null

var valor = 0
var diasDiaria = 0
var fatorTipo = 0.0

var nomeHospede = ""
var numQuarto = 0

//lista mutavel de quartos livres e ocupados

val quartosLivres = mutableListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)
val quartosOcupados = mutableListOf<Int>()

//data class da função reserva de quarto



fun main() {
    inicio()
}

fun inicio() {
    print("\nBem vindo ao $nomeHotel Hotel!\n")

    //colocar nome de usuario e senha

    print("Digite seu nome de usuário: ")
    nomeUsuario = readln()

    print("Senha: ")
    senha = readln()

    /*if (senha != "2678"){
        print("Senha incorreta, tente novamente")
        senha = readln()
    }
    if (senha != "2678"){
        print("Senha incorreta, tente novamente")
        senha = readln()
    }
    if (senha != "2678"){
        println("Senha incorreta. ")
        println("Número de tentativas excedido.")
        bloqueioSistema()
    }*/

    //refazer com while ou for

    if (senha == "2678"){
        println("")
        println("Bem vindo ao $nomeHotel Hotel, $nomeUsuario. É um imenso prazer ter você por aqui!\n")
    }


    println("Escolha uma opção:")
    println("1-Reservas de Quartos")
    println("2-Cadastro de Hóspedes")
    println("3-Eventos")
    println("4-Ar-Condicionado")
    println("5-Abastecimento ")
    println("6-Relatórios Operacionais")
    println("7-Sair")


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
                    while (numQuarto<= 0 && numQuarto >= 10){
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
                    while (numQuarto<= 9 && numQuarto >= 17){
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
                    while (numQuarto<= 16 && numQuarto >= 21){
                        print("Este quarto não faz parte da classe Luxo")
                        print("Escolha outro quarto dentro da classe Luxo")
                        numQuarto = readln().toInt()
                    }
                }
            }

            if (numQuarto in quartosLivres){
                //calcular tudo
                print("Subtotal = $valor x $diasDiaria x $tipoQuarto\n")
                val subtotal = (diasDiaria * valor) * fatorTipo
                print("Subtotal = $subtotal\n")
                val taxaServico = (subtotal * 10)/100
                print("Taxa de serviço: 10% do subtotal = $taxaServico\n")
                print("\nTotal final: ${subtotal + taxaServico}\n")

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
            println("Comando inválido!")
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
    println("Você deseja sair?")
    val confirma = readln().toBoolean()
    if (confirma) {
        println("Muito obrigado e até logo $nomeUsuario!")
    } else {
        inicio()
    }
}

fun bloqueioSistema() {
    println("Encerrando sistema...")
}