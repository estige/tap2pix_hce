# Tap2Pix NFC Card Emulation Service

Este projeto Android implementa um serviço de emulação de cartão NFC (Host-based Card Emulation, HCE), que permite ao dispositivo simular um cartão NFC e responder a comandos APDU (Application Protocol Data Units). A aplicação emula um cartão NDEF, permitindo a interação com leitores NFC compatíveis. O principal uso é fornecer informações como mensagens de texto ou URLs via NFC.

## Tap2Pix NFC Card Emulation Service

Tap2Pix NFC Card Emulation Service é um serviço Android que utiliza a tecnologia Host-based Card Emulation (HCE) para emular um cartão NFC e transmitir mensagens NDEF, como textos ou URLs, para leitores NFC. Ele é ideal para aplicações que precisam simular cartões NFC e responder a comandos APDU.

## Padronização da URL Tap2Pix

O formato padrão da URL para uso no Tap2Pix é o seguinte:

**https://tap2pix.app/?qr=ENCODE(copie&cole)**

A URL tap2pix.app foi configurada para garantir a segurança da aplicação, **sendo compatível com os aplicativos instantâneos da Apple (App Clip) e Android (Instant App)**. O uso de Universal Links garante que apenas o domínio tap2pix.app inicie esses apps instantâneos, além de estar alinhado à proposta enviada pelo Banco Central do Brasil (BCB).

## Padrão do Banco Central para o Pix por Aproximação

O Banco Central do Brasil, por meio do Grupo de Trabalho de Padronização e Requisitos Técnicos, propôs uma padronização para pagamentos com PIX por Aproximação + APP Iniciador de Pagamentos no contexto do Open Finance, utilizando a Jornada Sem Redirecionamento (JSR).

## Especificações do Formato

O fluxo proposto consiste no envio, por meio do comando APDU, de uma URI padronizada por terminais de pagamento (maquininhas, smartphones ou pin pads), seguindo o seguinte formato:

**pix://<hostname>?qr=<uri-encoded-emv-qr-string>&sig=<signature>**

	1.“pix://”: NÃO UTILIZADO PELO TAP2PIX Permite o uso do deeplink, que oferece uma camada de segurança fraca. Contudo, qualquer aplicativo poderia utilizar esse deeplink. Quando combinado com o parâmetro "&sig=<signature>", seria possível criptografar o valor do parâmetro "qr=<uri-encoded-emv-qr-string>", aumentando a segurança, mas criando redundância, pois o “copie e cole” do Pix já possui criptografia suficiente.
	2.“<hostname>?”: O uso do domínio (Universal Link) https://tap2pix.app, com um certificado RSA, garante a camada de segurança necessária para comprovação de propriedade e execução exclusiva do aplicativo.
	3.“qr=”: Este é o elemento central da interoperabilidade proposta pelo Banco Central. O QR code precisa estar em formato URI encode, para que os aplicativos bancários possam filtrar e absorver as informações transmitidas via GET. A informação contida no “copie e cole” é essencial para a realização da transação, enquanto as demais informações apenas compõem a personalização dos aplicativos e seus formatos de leitura.

## O projeto Tap2Pix

O projeto Tap2Pix busca a democratização do Pix por Aproximação. Além de estar totalmente preparado para a Jornada SEM Redicionamento, ele também mantém a Jornada COM Redirecionamento.

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

## Exemplo de Transmissão de URL

Abaixo está um exemplo de como seria a transmissão da URL do Tap2Pix com um QR code estático:

**https://tap2pix.app/?qr=00020101021126360014br.gov.bcb.pix0114%2B552199203898652040000530398654041.235802BR5918DEIVISON%20A%20L%20SERPA6009CABO%20FRIO62070503%2A%2A%2A6304BD9A**

Essa estrutura segue as diretrizes de segurança e padronização exigidas pelo Banco Central, garantindo a eficácia e segurança do Tap2Pix em diferentes cenários de uso.

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

## Links dos apps 

- IOS: https://apps.apple.com/us/app/tap2pix/id6624295820?l=pt-BR
- Android: https://play.google.com/store/apps/details?id=org.tap2pix.app&hl=pt_BR

Ps: Para a realização do teste, faça o download em um Android do Tap2Pix, coloque o valor a ser cobrado e aproxime de qualquer IPhone ou Android. 

Nesse momento para os testes, somente temos listado 3 bancos, são eles: Itaú Unibanco, Mercado Pago e Bradesco. 

O modelo atual para o pagamento com aproximação é com a Jornada COM Redirecionamento. Porém nosso app está preparado para ser um ITP e rodar o JSR quando disponível pelo Banco Central do Brasil e Open Finance Brasil. 

A proposta da Tap2Pix.org destaca-se como um Super App Wallet soberano, 100% tupiniquim, disponível em multiplataformas (iOS e Android - IPhone, Google e Samsung)

Que permite a iniciação automática dos pagamentos pela aproximação do celular via NFC ou através da leitura de QR Code. 

Além disso, é o único que oferece ambas as jornadas de pagamento, com e sem redirecionamento. Permitindo uma gradual adaptação dos usuários. 

O mais impressionante é que tudo isso é conduzido sob a governança e total controle dos associados no consórcio!
