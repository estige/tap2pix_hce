

# [ Tap2Pix NFC Card Emulation Service ]

Este projeto Android implementa um serviço de emulação de cartão NFC (Host-based Card Emulation, HCE), que permite ao dispositivo simular um cartão NFC e responder a comandos APDU (Application Protocol Data Units). A aplicação emula um cartão NDEF, permitindo a interação com leitores NFC compatíveis. O principal uso é fornecer informações como mensagens de texto ou URLs via NFC.

## Prints do Tap2Pix

<a href="#">
 <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource221/v4/07/d9/a5/07d9a577-b007-69d5-a175-8c3c9a5ffc12/Apple_Store_-_2.png/460x0w.webp" />
</a>
<a href="#">
 <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource211/v4/36/19/f5/3619f50c-a5c3-2169-758f-d5caf281ae98/Apple_Store_-_3.png/460x0w.webp" />
</a>
<a href="#">
 <img src="https://is1-ssl.mzstatic.com/image/thumb/PurpleSource221/v4/dd/45/63/dd45638c-5f6b-9345-a50c-9290c9d8a519/Apple_Store_-_4.png/460x0w.webp" />
</a>

## Links dos apps Tap2Pix
<a target="_blank" href="https://play.google.com/store/apps/details?id=org.tap2pix.app&hl=pt_BR">
  <img alt="Get it from the Google Play" src="https://res.cloudinary.com/dunz5zfpt/f_auto,c_limit,w_350,q_auto//site-stone/brands/googlePlay" />
</a>
<a target="_blank" href="https://apps.apple.com/us/app/tap2pix/id6624295820?l=pt-BR">
  <img alt="Get it from the App Store" src="https://res.cloudinary.com/dunz5zfpt/f_auto,c_limit,w_350,q_auto//site-stone/brands/appStore" />
</a>

<br><br>

> [!IMPORTANT]
> Somente o Android possui a funcionadade para a transmição do NFC! Sendo assim, os Vendedores precisam sempre ser Android. Já o Pagador, pode ser Android ou IOS

<br>

> [!NOTE]
>Para a realização do teste, faça o download em um Android do Tap2Pix, coloque o valor a ser cobrado e aproxime de qualquer IPhone ou Android.

> [!NOTE]
>Nesse momento para os testes, somente temos listado 3 bancos, são eles: **Itaú Unibanco, Mercado Pago e Bradesco**.

> [!NOTE]
>O modelo atual para o pagamento com aproximação é com a Jornada COM Redirecionamento. Porém nosso app está preparado para ser um ITP e rodar o JSR quando disponível pelo Banco Central do Brasil e Open Finance Brasil.

<br><br>

<a target="_blank" href="https://www.tap2pix.org">
 <img  src="https://media.licdn.com/dms/image/v2/D4D22AQGPta0yimkvHQ/feedshare-shrink_800/feedshare-shrink_800/0/1724860093979?e=2147483647&v=beta&t=1-0Df40sK7V6i-POBYSBefRLDK6epA9Py5QgwtBv0Kg" />
</a>

## A proposta da Tap2Pix.org
A proposta da Tap2Pix.org destaca-se como um Super App Wallet soberano, 100% tupiniquim, disponível em multiplataformas (iOS e Android - IPhone, Google e Samsung)

Que permite a iniciação automática dos pagamentos pela aproximação do celular via NFC ou através da leitura de QR Code. 

Além disso, é o único que oferece ambas as jornadas de pagamento, com e sem redirecionamento. Permitindo uma gradual adaptação dos usuários. 

O mais impressionante é que tudo isso é conduzido sob a governança e total controle dos associados no consórcio!

## NfcFCardEmulation

NfcFCardEmulation é um serviço Android que utiliza a tecnologia Host-based Card Emulation (HCE) para emular um cartão NFC e transmitir mensagens NDEF, como textos ou URLs, para leitores NFC. Ele é ideal para aplicações que precisam simular cartões NFC e responder a comandos APDU.

Veja mais em: https://developer.android.com/reference/android/nfc/cardemulation/NfcFCardEmulation


> [!IMPORTANT]
> Nenhum dos participantes do Tap2Pix precisará se preocupar com o desenvolvimento dos aplicativos instantâneos para integração com o Tap2Pix. Toda a jornada e adaptação necessária já foi realizada pela equipe do Tap2Pix.

## Padrão do Banco Central para o Pix por Aproximação

O Banco Central do Brasil, por meio do Grupo de Trabalho de Padronização e Requisitos Técnicos, propôs uma padronização para pagamentos com PIX por Aproximação + APP Iniciador de Pagamentos no contexto do Open Finance, utilizando a Jornada Sem Redirecionamento (JSR).

## Especificações do Formato

O fluxo proposto consiste no envio, por meio do comando APDU, de uma URI padronizada por terminais de pagamento (maquininhas, smartphones ou pin pads), seguindo o seguinte formato:

	pix://<hostname>?qr=<uri-encoded-emv-qr-string>&sig=<signature>
 
[![](https://img.shields.io/badge/pix://-red?style=for-the-badge)](#)
> [!WARNING]
> O DEEPLINK NÃO É UTILIZADO PELO TAP2PIX! 
- `“pix://”`: 🟥 Permite o uso do deeplink, que oferece uma camada de segurança fraca. Contudo, qualquer aplicativo poderia utilizar esse deeplink. Quando combinado com o parâmetro `"&sig=<signature>"`, seria possível criptografar o valor do parâmetro `"qr=<uri-encoded-emv-qr-string>"`, aumentando a segurança, mas criando redundância, pois o “copie e cole” do Pix já possui criptografia suficiente.

[![](https://img.shields.io/badge/hostname-blue?style=for-the-badge)](#)
- `“<hostname>?”`: 🟩 Permite o uso do domínio (Universal Link) **`https://tap2pix.app`**, com um certificado RSA, garante a camada de segurança necessária para comprovação de propriedade e execução exclusiva dos aplicativos instantâneos.

[![](https://img.shields.io/badge/qr-blue?style=for-the-badge)](#)
- `“qr=<uri-encoded-emv-qr-string>”`: 🟩 Este é o elemento central da interoperabilidade proposta pelo Banco Central. O QR code precisa estar em formato URI encode, para que os aplicativos bancários possam filtrar e absorver as informações transmitidas via GET. A informação contida no “copie e cole” é essencial para a realização da transação, enquanto as demais informações apenas compõem a personalização dos aplicativos e seus formatos de leitura.


# [ Transmissão do NFC com Tap2Pix ]
## Descrição das Funcionalidades

- **Emulação de cartão NFC (HCE)**: O dispositivo Android atua como um cartão NFC emulado.
- **Resposta a comandos APDU**: O serviço interpreta e responde a comandos APDU, como a seleção de aplicação, container de capacidade e arquivos NDEF.
- **Envio de mensagens NDEF**: Permite o envio de mensagens NDEF contendo texto ou URLs para dispositivos/leitores NFC.
- **Suporte a mensagens dinâmicas**: O serviço pode gerar mensagens NDEF com base em URLs fornecidas via `Intent`.

## Estrutura do Projeto

A principal classe do projeto é `Tap2PixApduService`, que herda de `HostApduService` e implementa a lógica de resposta aos comandos NFC.

### Principais Componentes

- **`onCreate()`**: Inicializa o serviço, preparando uma mensagem NDEF padrão.
- **`onStartCommand()`**: Atualiza a mensagem NDEF com base nos dados recebidos via `Intent`.
- **`processCommandApdu()`**: Lida com os comandos APDU recebidos, respondendo com dados NDEF ou informações do container de capacidade.
- **`onDeactivated()`**: Reseta os estados quando o serviço NFC é desativado.

### Comandos APDU

Os comandos APDU são usados para selecionar diferentes arquivos e responder com as informações correspondentes:

- **Selecionar Aplicação**: Seleciona a aplicação NDEF emulada.
- **Selecionar Container de Capacidade**: Seleciona o arquivo de capacidade NDEF.
- **Selecionar Arquivo NDEF**: Seleciona o arquivo que contém a mensagem NDEF.

### Estrutura da Mensagem NDEF

A mensagem NDEF pode conter:

- **Texto**: Uma string em formato de texto (padrão).
- **URL**: Uma URL passada por `Intent` para emular um redirecionamento.

## Pré-requisitos

- Android Studio
- Um dispositivo Android com suporte a NFC e Host-based Card Emulation (HCE)


<a target="_blank" href="https://www.tap2pix.org">
 <img  src="https://media.licdn.com/dms/image/v2/D4D22AQE7gYVSTeJLcA/feedshare-shrink_800/feedshare-shrink_800/0/1724860093115?e=2147483647&v=beta&t=yoOxtYvfIJPhWMZpC__pqC4znSY_1dqi5zdf8GCYJ_A" />
</a>

# [ Envio da URL do Pix transmitida pelo NFC ] 
## Padronização da URL Tap2Pix

O formato padrão da URL para ser transmitido no Tap2Pix é o seguinte:

	https://tap2pix.app/?qr=encodeURI(copie&cole)

A URL tap2pix.app foi configurada para garantir a segurança da aplicação, **sendo compatível com os aplicativos instantâneos da Apple (App Clip) e Android (Instant App)**. O uso de Universal Links garante que apenas o domínio tap2pix.app inicie esses apps instantâneos, além de estar alinhado à proposta enviada pelo Banco Central do Brasil (BCB).

## Exemplo de Transmissão de URL

Abaixo está um exemplo de como seria a transmissão da URL do Tap2Pix com um QR code estático:

See demo Android/Google: [Instant APP](https://tap2pix.app/?qr=00020101021126360014br.gov.bcb.pix0114%2B552199203898652040000530398654041.235802BR5918DEIVISON%20A%20L%20SERPA6009CABO%20FRIO62070503%2A%2A%2A6304BD9A)

> [!WARNING]
> O Instant APP precisa rodar com o Google Chrome

See demo IOS/Apple: [APP Clip](https://appclip.apple.com/id?p=org.tap2pix.application.clip&qr=00020101021126360014br.gov.bcb.pix0114%2B552199203898652040000530398654041.235802BR5918DEIVISON%20A%20L%20SERPA6009CABO%20FRIO62070503%2A%2A%2A6304BD9A)
 

Essa estrutura segue as diretrizes de segurança e padronização exigidas pelo Banco Central, garantindo a eficácia e segurança do Tap2Pix em diferentes cenários de uso.

# [ Listagem dos APPs na Tap2Pix ]
## Adição dos APPs bancários junto ao TAP2PIX

Para listar seu app no Tap2Pix, é necessário entrar em contato e fornecer os schemes do seu app. Vale lembrar que o Tap2Pix aceitará apps de BaaS e Bancos Sociais. Esses apps não são obrigatórios para o Open Finance na Jornada SEM Redirecionamento, que será lançada em fevereiro de 2025. Contudo, apps de BaaS, Bancos Sociais e de exchanges (optantes pelo OFF Ramp) podem solicitar inclusão via Jornada COM Redirecionamento.

É necessário fornecer os schemes do app junto com o Universal Link. Exemplo:

```json
	"name": "BANCO-A",
	"brand": "https://static.eb.tech/img/BANCO-A.png",
	"universal_link": "https://www.BANCO-A.com.br/",
	"links": {
		"android": "BANCO-A://transferenciapix",
		"ios": "BANCO-A://transferenciapix"
	}
```

## Exibição dos APPs e sua ordenação [ Em evolução ]

O Tap2Pix mapeia os apps instalados nos smartphones dos clientes [ Já desenvolvido para IOS ]. Checamos se os apps estão instalados no Smartphone (IOS e Android) dos clientes. 

E iremos checar as 20 à 30 instituições mais utilizadas no Brasil estão presentes no Smartphone [ Estamos mapeando os universal link e schemes ]. 

Caso não estejam presente no Smartphone, os links dos apps serão removidos da listagem, garantindo que apenas os apps instalados [ A checagem dos 20 a 30 apps ] sejam exibidos.

A ordenação será feita com base nos últimos 3 apps utilizados pelo cliente [ Em desenvolvimento ], armazenando essa informação para que sejam listados primeiro, proporcionando uma experiência mais simples e eficaz.

## Redução de Fricção e Taps Necessários

O Pix já é um sucesso para pagamentos online e de serviços. No entanto, no uso presencial, ele ainda enfrenta obstáculos devido à fricção envolvida.

Em testes de laboratório, observamos que a jornada com QR Code exige de 8 a 12 interações (taps), resultando em um tempo total entre 20 e 50 segundos.

O Tap2Pix busca reduzir essa fricção nos pagamentos presenciais com Pix, proporcionando uma experiência mais simplificada.

• Na proposta do Tap2Pix COM redirecionamento, a jornada varia de 3 a 6 interações. A melhor experiência de UX foi alcançada com 3 taps, e, entre os bancos testados, o Mercado Pago apresentou 4 taps, com tempo de transação variando entre 6 e 15 segundos em média.

> [!TIP]
> O Tap2Pix é compatível com Apple Pay e Instant App Android, mantendo a mesma quantidade de interações nas Jornadas COM e SEM Redirecionamento em ambas as plataformas.

• Na proposta SEM redirecionamento, estimamos que o processo envolverá 2 cliques laterais + 1 ou 2 taps. Considerando que haverá mais um PSP intermediando a transação, acreditamos que a latência do JSR será ligeiramente maior devido às verificações de fraude, levando de 10 a 15 segundos em média.

• A experiência com a Wallet da Apple demonstrou ser extremamente eficiente, exigindo apenas 2 cliques laterais, com tempo médio de 5 a 10 segundos.

Para substituir a Wallet da Apple, outras wallets precisarão de 2 cliques laterais + 1 tap para ativar a leitura do NFC, levando de 10 a 15 segundos. No entanto, para que essas wallets sejam competitivas, é essencial que ofereçam uma UX superior à da Apple, para motivar os clientes a usá-las. Outros pontos identificados foram:

- Necessidade de atualização dos smartphones dos clientes para iOS 18.1 ou superior;
- Competição direta com a Wallet da Apple;
- Necessidade de interação para ativar a leitura do NFC;
- Competição com bandeiras de cartão de crédito;
- Instalação de um novo aplicativo de wallet;
- Limitação no suporte a NFCs de serviços, como cartão vacinal do governo, cartões de embarque, tickets de transporte, passaportes da Disney, entre outros.



## O projeto Tap2Pix

O projeto Tap2Pix busca a democratização do Pix por Aproximação. Além de estar totalmente preparado para a Jornada SEM Redicionamento, ele também mantém a Jornada COM Redirecionamento.

<a target="_blank" href="https://www.tap2pix.org">
 <img  src="https://media.licdn.com/dms/image/v2/D4D22AQHMV3vRaJn-xw/feedshare-shrink_800/feedshare-shrink_800/0/1724860094162?e=2147483647&v=beta&t=g2nItvQPs4DUpKiYZ1CaxYLb06Q7HbObxjRAeDupwJQ" />
</a>

## Benefícios da Proposta do Tap2Pix:

- Disponibilidade do Pix por Aproximação antes do lançamento do JSR com o Open Finance (Fevereiro de 2025)
- A proposta do Tap2Pix não conflita com as bandeiras;
- Não competirá com Apple Pay ou Wallet no duplo clique lateral;
- Sua ativação é automática e instantânea;
- Compatível com iOS 15 e Android 12
- Prevê a jornada COM e SEM redirecionamento;
- Utiliza QR Code, NFC ou Link para ativação do SuperApp Wallet;
- Não requer instalação prévia;
- Tem menos de 100KB;
- Não necessita de autorização ou cadastro prévio dos clientes;
- Facilita a adaptação da usabilidade para os clientes;
- Não requer cadastro dos bancos (apenas mapeamento do Universal Link e sessão do Pix);
- Não precisa de modificações nos aplicativos bancários;
- Abrange BaaS e Bancos Sociais e não obrigatórios do #JSR;
- É um Super App Wallet multiplataforma (iOS e Android);
- Mantém todo o legado de segurança do método tradicional;
- Está 100% em conformidade com a regulamentação e a LGPD;
- É uma solução pioneira em nível mundial, desenvolvida por um grupo de empresas com uma proposta de padronização aberta para o uso de pagamentos via NFC, visando superar as limitações existentes;
- Entrará em produção (com a Jornada COM Redirecionamento) até o início de outubro de 2024.


## Desafios e Avanços

Os aplicativos instantâneos estão em constante evolução, e a equipe do Tap2Pix já realizou todas as adaptações necessárias, continuando seu desenvolvimento contínuo. 

Atualmente, o Tap2Pix está em fase de pré-lançamento, com os participantes do consórcio e as suas equipes de desenvolvimento trabalhando junto para alinhar o processo de onboarding, a listagem e os fluxos operacionais.

É importante destacar que, após a integração dos parceiros do consórcio, o Tap2Pix avançará para a criação da sua associação e estruturação de sua governança.

Abaixo, detalhamos os desafios encontrados e as soluções instantâneas adotadas para iOS e Android.

Ambos os sistemas de aplicativos instantâneos (iOS e Android) possuem documentação limitada, mas as recentes atualizações feitas pelas plataformas mostram que estão em constante evolução (Principalmente a do Android, que como descrito anteriormente. Ainda não suporta a sua inicialização direta pelo NFC. Necessitando assim haver uma pagina web, que antecede e faz a sua chamada. 

Vale ressaltar, que nenhum dos participantes do Tap2Pix precisará se preocupar com o desenolvimento dos aplicativos instantâneos para plugarem ao Tap2Pix, toda a jornada e adpação necessária já foi feita pela equipe do Tap2Pix.

Os aplicativos instantâneos precisaram ser desenvolvidos utilizando a linguagem nativa das plataformas, como Kotlin no Android e Swift no IOS.

### IOS / Apple
O IOS possui um app instantâneo com ótima UX, onde exibe um card mesmo com Iphone bloqueado. A limitação que encotramos junto ao APP CLIP da Apple foi no que diz respeito a sua invocação através do **link**. A Apple ainda não permite a ativação do app instantâneo utilizando o universal link (dominio tap2pix.app). Porém nesse momento não iremos utilizar a invocação pelo link devido a não necessidade.

### Android / Google
O Android possui a sua limitação na invocação do app instantâneo através do NFC (O Google tem atualizado bastante o seu instant app e a comunidade tem exigido bastante essa invocação pelo NFC). O subterfúgio que tivemos foi em levar para o navegador e a patir do navegador o cliente que clicar em abrir ele executará o app instantâneo. Assim fizemos uma emulação do card do APP CLIP e um botão abrir. 

# [ Próximos passos ] 

	1 - Padronização do QR Code com logo Tap2Pix;
	2 - Criação do Fingerprint;
	3 - Criação do Tap2Crypto (P2P de Stablecoin ou com OFF Ramp para Pix)
	4 - Criação e consolidação do Consócio Tap2Pix
	5 - Implementação da JSR com OpF;
	6 - Incorporação de uma ID e One Click Buy (Estilo Click2Pay da Visa e Mastercard)

 Sugestão dos nomes para a Associação Tap2Pix: **ABTAP - Associação Brasileira de Transações por Aproximação** OU **APAW - Associação de Pagamentos por Aproximação e Wallet**

# [ Junte-se ]
Vamos unir forças para demonstrar que, por meio da colaboração coletiva, que podemos não apenas estabelecer um novo padrão, mas também transformar a maneira como os brasileiros realizam pagamentos contactless utilizando o Pix? Entre em contato!

[Contato via Whatsapp](https://api.whatsapp.com/send?phone=5521992038986&text=Ola%20gostaria%20de%20mais%20informa%C3%A7%C3%B5es%20sobre%20o%20Tap2Pix)

Email: hi@tap2pix.org


Veja mais em tap2pix.org
