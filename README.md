# Tap2Pix NFC Card Emulation Service

Este projeto Android implementa um serviço de emulação de cartão NFC (Host-based Card Emulation, HCE), que permite ao dispositivo simular um cartão NFC e responder a comandos APDU (Application Protocol Data Units). A aplicação emula um cartão NDEF, permitindo a interação com leitores NFC compatíveis. O principal uso é fornecer informações como mensagens de texto ou URLs via NFC.

## Funcionalidades

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
