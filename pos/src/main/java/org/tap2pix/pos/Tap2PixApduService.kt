package org.tap2pix.pos

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class Tap2PixApduService : HostApduService() {

    // Armazena a mensagem NDEF em um array de bytes
    private lateinit var mNdefRecordFile: ByteArray

    // Variáveis de estado para rastrear se a aplicação, o container e o arquivo NDEF foram selecionados
    private var mAppSelected = false
    private var mCcSelected = false
    private var mNdefSelected = false

    // Método chamado quando o serviço é criado
    override fun onCreate() {
        super.onCreate()

        // Inicializa os estados como não selecionados
        mAppSelected = false
        mCcSelected = false
        mNdefSelected = false

        // Define a mensagem NDEF padrão
        val DEFAULT_MESSAGE = "This is the default message from Tap2Pix"
        val ndefDefaultMessage = getNdefMessage(DEFAULT_MESSAGE)

        // O comprimento máximo é 246, então garantimos que o tamanho não ultrapasse isso
        val nlen = ndefDefaultMessage!!.byteArrayLength
        mNdefRecordFile = ByteArray(nlen + 2)
        mNdefRecordFile[0] = ((nlen and 0xff00) / 256).toByte()  // Parte superior do comprimento
        mNdefRecordFile[1] = (nlen and 0xff).toByte()  // Parte inferior do comprimento
        System.arraycopy(
            ndefDefaultMessage.toByteArray(), // Copia a mensagem NDEF para o array
            0,
            mNdefRecordFile,
            2,
            ndefDefaultMessage.byteArrayLength
        )
    }

    // Método chamado quando o serviço recebe um Intent, por exemplo, contendo uma URL NDEF
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            // Se o intent contém uma URL, gera uma nova mensagem NDEF
            if (intent.hasExtra("ndefUrl")) {
                val ndefMessage = getNdefUrlMessage(intent.getStringExtra("ndefUrl"))
                if (ndefMessage != null) {
                    // Prepara o array de bytes para armazenar a mensagem NDEF
                    val nlen = ndefMessage.byteArrayLength
                    mNdefRecordFile = ByteArray(nlen + 2)
                    mNdefRecordFile[0] = ((nlen and 0xff00) / 256).toByte()
                    mNdefRecordFile[1] = (nlen and 0xff).toByte()
                    System.arraycopy(
                        ndefMessage.toByteArray(),
                        0,
                        mNdefRecordFile,
                        2,
                        ndefMessage.byteArrayLength
                    )
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // Cria uma mensagem NDEF contendo um texto
    private fun getNdefMessage(ndefData: String?): NdefMessage? {
        if (ndefData!!.isEmpty()) {
            return null
        }
        // Cria um registro de texto NDEF
        val ndefRecord = NdefRecord.createTextRecord("en", ndefData)
        return NdefMessage(ndefRecord)
    }

    // Cria uma mensagem NDEF contendo uma URL
    private fun getNdefUrlMessage(ndefData: String?): NdefMessage? {
        if (ndefData!!.isEmpty()) {
            return null
        }
        // Cria um registro de URI NDEF
        val ndefRecord = NdefRecord.createUri(ndefData)
        return NdefMessage(ndefRecord)
    }

    // Lida com os comandos APDU recebidos pelo serviço
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray? {

        // Exibe o comando APDU recebido no log
        Log.d((TAG), "commandApdu: " + Utils.bytesToHex(commandApdu))

        // Verifica se o comando é para selecionar a aplicação
        if (SELECT_APPLICATION.contentEquals(commandApdu)) {
            mAppSelected = true
            mCcSelected = false
            mNdefSelected = false
            Log.d((TAG), "responseApdu: " + Utils.bytesToHex(SUCCESS_SW))
            return SUCCESS_SW
        }

        // Verifica se o container de capacidade foi selecionado
        if (mAppSelected && SELECT_CAPABILITY_CONTAINER.contentEquals(commandApdu)) {
            mCcSelected = true
            mNdefSelected = false
            Log.d((TAG), "responseApdu: " + Utils.bytesToHex(SUCCESS_SW))
            return SUCCESS_SW
        }

        // Verifica se o arquivo NDEF foi selecionado
        if (mAppSelected && SELECT_NDEF_FILE.contentEquals(commandApdu)) {
            mCcSelected = false
            mNdefSelected = true
            Log.d((TAG), "responseApdu: " + Utils.bytesToHex(SUCCESS_SW))
            return SUCCESS_SW
        }

        // Se o comando APDU for de leitura de dados, processa o offset e o comprimento
        if (commandApdu[0] == 0x00.toByte() && commandApdu[1] == 0xb0.toByte()) {
            val offset = (0x00ff and commandApdu[2].toInt()) * 256 + (0x00ff and commandApdu[3].toInt())
            val le = 0x00ff and commandApdu[4].toInt()

            val responseApdu = ByteArray(le + SUCCESS_SW.size)

            // Se o container de capacidade estiver selecionado, retorna o arquivo de capacidade
            if (mCcSelected && offset == 0 && le == CAPABILITY_CONTAINER_FILE.size) {
                System.arraycopy(CAPABILITY_CONTAINER_FILE, offset, responseApdu, 0, le)
                System.arraycopy(SUCCESS_SW, 0, responseApdu, le, SUCCESS_SW.size)
                Log.d((TAG), "responseApdu: " + Utils.bytesToHex(responseApdu))
                return responseApdu
            }

            // Se o arquivo NDEF estiver selecionado, retorna os dados NDEF
            if (mNdefSelected) {
                if (offset + le <= mNdefRecordFile.size) {
                    System.arraycopy(mNdefRecordFile, offset, responseApdu, 0, le)
                    System.arraycopy(SUCCESS_SW, 0, responseApdu, le, SUCCESS_SW.size)
                    Log.d((TAG), "responseApdu: " + Utils.bytesToHex(responseApdu))
                    return responseApdu
                }
            }
        }

        // Retorna falha se o comando não for reconhecido
        return FAILURE_SW
    }

    // Reseta os estados quando o serviço é desativado
    override fun onDeactivated(reason: Int) {
        mAppSelected = false
        mCcSelected = false
        mNdefSelected = false
    }

    // Objetos estáticos usados no serviço, como os comandos APDU e arquivos de capacidade
    companion object {
        const val TAG = "Tap2PixApduService"

        // Comando APDU para selecionar a aplicação
        private val SELECT_APPLICATION = byteArrayOf(
            0x00.toByte(),  // CLA - Classe de instrução
            0xA4.toByte(),  // INS - Código da instrução
            0x04.toByte(),  // P1 - Parâmetro 1
            0x00.toByte(),  // P2 - Parâmetro 2
            0x07.toByte(),  // Número de bytes no campo de dados
            0xD2.toByte(), 0x76.toByte(), 0x00.toByte(), 0x00.toByte(), 0x85.toByte(), 0x01.toByte(), 0x01.toByte(),
            0x00.toByte() // Le - Número máximo de bytes esperados na resposta
        )

        // Comando APDU para selecionar o container de capacidade
        private val SELECT_CAPABILITY_CONTAINER = byteArrayOf(
            0x00.toByte(),  // CLA - Classe de instrução
            0xa4.toByte(),  // INS - Código da instrução
            0x00.toByte(),  // P1 - Parâmetro 1
            0x0c.toByte(),  // P2 - Parâmetro 2
            0x02.toByte(),  // Número de bytes no campo de dados
            0xe1.toByte(), 0x03.toByte() // Identificador do arquivo CC
        )

        // Comando APDU para selecionar o arquivo NDEF
        private val SELECT_NDEF_FILE = byteArrayOf(
            0x00.toByte(),  // CLA - Classe de instrução
            0xa4.toByte(),  // INS - Código da instrução
            0x00.toByte(),  // P1 - Parâmetro 1
            0x0c.toByte(),  // P2 - Parâmetro 2
            0x02.toByte(),  // Número de bytes no campo de dados
            0xE1.toByte(), 0x04.toByte() // Identificador do arquivo NDEF
        )

        // Arquivo de capacidade do container
        private val CAPABILITY_CONTAINER_FILE = byteArrayOf(
            0x00, 0x0f,  // CCLEN - Tamanho do arquivo
            0x20,  // Versão do mapeamento
            0x00, 0x3b,  // Tamanho máximo de resposta APDU
            0x00, 0x34,  // Tamanho máximo de comando APDU
            0x04, 0x06,  // Tag e comprimento
            0xe1.toByte(), 0x04,  // Identificador do arquivo NDEF
            0x00.toByte(), 0xff.toByte(),  // Tamanho máximo NDEF
            0x00,  // Acesso de leitura permitido
            0xff.toByte(),  // Acesso de gravação negado
        )

        // Status Word de sucesso
        private val SUCCESS_SW = byteArrayOf(
            0x90.toByte(),
            0x00.toByte(),
        )

        // Status Word de falha
        private val FAILURE_SW = byteArrayOf(
            0x6a.toByte(),
            0x82.toByte(),
        )
    }
}
